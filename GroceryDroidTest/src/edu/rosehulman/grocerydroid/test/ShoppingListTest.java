package edu.rosehulman.grocerydroid.test;

import java.util.ArrayList;

import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import edu.rosehulman.grocerydroid.MainActivity;
import edu.rosehulman.grocerydroid.db.DatabaseHelper;
import edu.rosehulman.grocerydroid.db.ShoppingListDataAdapter;
import edu.rosehulman.grocerydroid.model.Item;
import edu.rosehulman.grocerydroid.model.ItemUnitLabel;
import edu.rosehulman.grocerydroid.model.ShoppingList;

/**
 * Tests ShoppingList and ShoppingListDataAdapter.
 * 
 * @author Matthew Boutell. Created Mar 29, 2012.
 */
public class ShoppingListTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity;
	private Item bananas;
	private Item oranges;
	private Item beef;

	private ShoppingListDataAdapter slda;
	private DatabaseHelper dbHelper;
	private ShoppingList list1;
	private ShoppingList list2;
	private ArrayList<ShoppingList> lists;
	private static float EPSILON = 0.0000001f;

	/**
	 * Calls another constructor with the given hardcoded info.
	 * 
	 * @param activityClass
	 */
	public ShoppingListTest() {
		super("edu.rosehulman.grocerydroid", MainActivity.class);
	}

	/**
	 * Purges the database
	 */
	public void purgeDb() {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		db.beginTransaction();
		this.dbHelper.onUpgrade(db, 0, 0);
		db.setTransactionSuccessful();
		db.endTransaction();
		// db.close();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.mActivity = this.getActivity();

		// CONSIDER: could just pass mActivity instead of the app context?
		this.dbHelper = DatabaseHelper.createInstance(this.mActivity
				.getApplicationContext());

		this.bananas = new Item(1, 1, "Bananas", 4, 2, 1.50f, 1,
				ItemUnitLabel.bag, true, 28, 4);
		this.oranges = new Item(2, 1, "Oranges", 2, 2, 3.00f, 1,
				ItemUnitLabel.bag, true, 27, 5);
		this.beef = new Item(3, 2, "Beef", 3, 1, 4.50f, 1, ItemUnitLabel.lb,
				true, 30, 8);
		this.list1 = new ShoppingList(1, "Shoprite");
		this.list2 = new ShoppingList(2, "Walmart");
		this.lists = new ArrayList<ShoppingList>();
		this.lists.add(this.list1);
		this.lists.add(this.list2);

		this.slda = new ShoppingListDataAdapter();
		this.slda.open();
	}

	/**
	 * Tests the list operation.
	 */
	public void testAddItem() {
		this.list2.addItem(this.bananas);
		this.list2.addItem(this.oranges);
		this.list2.addItem(this.beef);

		String expected = "2 Walmart" + "\n  " + this.bananas + "\n  "
				+ this.oranges + "\n  " + this.beef;
		assertEquals(expected, this.list2.toString());
		assertEquals(3, this.list2.getItems().size());
	}

	/**
	 * Tests the list operation.
	 */
	public void testDeleteItem() {
		this.list1.addItem(this.bananas);
		this.list1.addItem(this.oranges);
		this.list1.addItem(this.beef);
		assertTrue(this.list1.deleteItem(this.bananas));
		assertTrue(this.list1.deleteItem(this.beef));
		assertEquals(1, this.list1.getItems().size());
		assertEquals(this.oranges, this.list1.getItems().get(0));
	}

	/**
	 * Tests the list operation.
	 */
	public void testConstructList() {
		assertEquals(1, this.list1.getId());
		assertEquals("Shoprite", this.list1.getName());
		assertEquals(2, this.list2.getId());
		assertEquals("Walmart", this.list2.getName());
	}

	/**
	 * Tests the list operation.
	 */
	public void testInsert() {
		purgeDb();
		this.slda.insertList(this.list1);
		this.slda.insertList(this.list2);

		// TODO: test that it's in the DB. Currently just use a utility to
		// check.
	}

	/**
	 * Tests the list operation.
	 */
	public void testUpdate() {
		purgeDb();
		this.slda.insertList(this.list1);
		this.list1.setName("Supasave");
		this.slda.updateList(this.list1);
	}

	/**
	 * Tests the list operation.
	 */
	public void testDelete() {
		purgeDb();
		this.slda.insertList(this.list1);
		assertFalse(this.slda.deleteList(this.list2));
		assertTrue(this.slda.deleteList(this.list1));
	}

	/**
	 * Tests the list operation.
	 */
	public void testLoadAllLists() {
		purgeDb();
		this.slda.insertList(this.list1);
		this.slda.insertList(this.list2);

		ArrayList<ShoppingList> someLists = new ArrayList<ShoppingList>();
		this.slda.loadAllLists(someLists);

		// The first one in the DB should be the matching list.
		// The IDs may not match, though, so we don't test them.
		assertEquals(this.list1, someLists.get(0));
		assertEquals(this.list2, someLists.get(1));
		assertEquals(2, someLists.size());
	}

	/**
	 * Tests the list operation.
	 */
	public void testTotalPrice() {
		assertEquals(0, this.list1.totalPrice(), EPSILON);
		assertEquals(0, this.list2.totalPrice(), EPSILON);
		this.list1.addItem(this.bananas);
		this.list1.addItem(this.oranges);
		assertEquals(9.0f, this.list1.totalPrice(), EPSILON);
	}

	/**
	 * Tests the list operation.
	 */
	public void testTotalSpent() {
		assertEquals(0, this.list1.totalPrice(), EPSILON);
		assertEquals(0, this.list2.totalPrice(), EPSILON);
		this.list1.addItem(this.bananas);
		this.oranges.setBought(false);
		this.list1.addItem(this.oranges);
		this.list1.addItem(this.beef);
		assertEquals(7.50f, this.list1.totalSpent(), EPSILON);
	}

	/**
	 * Tests the list operation.
	 */
	public void testToString() {
		this.list1.addItem(this.bananas);

		String expected = "1 Shoprite\n  " + this.bananas;
		assertEquals(expected, this.list1.toString());
	}
}
