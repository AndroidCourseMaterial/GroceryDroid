package edu.rosehulman.grocerydroid.test;

import android.test.ActivityInstrumentationTestCase2;
import edu.rosehulman.grocerydroid.GroceryDroidActivity;
import edu.rosehulman.grocerydroid.model.Item;
import edu.rosehulman.grocerydroid.model.ItemUnitLabel;

/**
 * TODO Put here a description of what this class does.
 *
 * @author boutell.
 *         Created Mar 26, 2012.
 */
public class ShoppingListTest extends
		ActivityInstrumentationTestCase2<GroceryDroidActivity> {

	private GroceryDroidActivity mActivity;
	private Item item;
	private static float EPSILON = 0.0000001f;
	
	/**
	 * Calls another constructor with the given hardcoded info.
	 *
	 * @param activityClass
	 */
	public ShoppingListTest() {
		super("edu.rosehulman.grocerydroid", GroceryDroidActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.mActivity = this.getActivity();

		this.item = new Item(17, "Bananas", 4, 2, 1.50f, 1, ItemUnitLabel.bag, true, 28, 4);
	}
	
	public void testPreconditions() {
		// Empty
	}
	
	public void testConstructFullItem() {
		assertEquals(17, this.item.getId());
		assertEquals("Bananas", this.item.getName());
		assertEquals(4, this.item.getnStock());
		assertEquals(2, this.item.getnBuy());
		assertEquals(1.50, this.item.getPrice(), EPSILON);
		assertEquals(1, this.item.getUnitSize(), EPSILON);
		assertEquals(ItemUnitLabel.bag, this.item.getUnitLabel());
		assertTrue(this.item.isBought());
		assertEquals(28, this.item.getStockIdx());
		assertEquals(4, this.item.getShopIdx());
	}
	
	public void testIncrement() {
		this.item.incrementNumberToBuy();
		this.item.incrementNumberToBuy();
		assertEquals(4, this.item.getnBuy());
	}
	
	public void testResetNumberToBuy() {
		this.item.resetNumberToBuy();
		assertEquals(0, this.item.getnBuy());
	}
	
	public void testTotalPrice() {
		// TODO: for whole lists

		assertEquals(3.0f, this.item.totalPrice(), EPSILON);
	}
	
	public void testTotalSpent() {
		// TODO: for whole lists
		
		assertEquals(3.0f, this.item.totalSpent(), EPSILON);
		this.item.setBought(false);
		assertEquals(0.0f, this.item.totalSpent(), EPSILON);
	}
	
	public void testToString() {
		String expected = "17 Bananas (2/4) 2K/1.0 bag B 28 4";
		assertEquals(expected, this.item.toString());
	}
	
	public void testShortString() {
		// TODO when I write it.
	}

	
	
	
	
}
