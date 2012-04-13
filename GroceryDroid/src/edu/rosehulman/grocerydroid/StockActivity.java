package edu.rosehulman.grocerydroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import edu.rosehulman.grocerydroid.db.ItemDataAdapter;
import edu.rosehulman.grocerydroid.db.ShoppingListDataAdapter;
import edu.rosehulman.grocerydroid.model.Item;
import edu.rosehulman.grocerydroid.model.ShoppingList;
import edu.rosehulman.grocerydroid.model.ShoppingList.Order;


/**
 * The activity used to manage the creation and restocking of the items in a
 * list.
 * 
 * @author Matthew Boutell. Created Apr 12, 2012.
 */
public class StockActivity extends ShoppingListActivity {
	private ShoppingListDataAdapter mSlda;
	private ItemDataAdapter mIda;
	private ShoppingList mList;
	private StockItemAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_ForceOverflow);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stock_layout);
		getSupportActionBar().setIcon(R.drawable.ic_list);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = this.getIntent();
		long listId = intent.getLongExtra(MainActivity.KEY_SELECTED_LIST, -1);
		Log.d(MyApplication.GD, "stocking with list " + listId);
		
		initializeDatabase();
		initializeShoppingList(listId);

		getSupportActionBar().setSubtitle(mList.getName());
	
		ListView lv = (ListView) findViewById(R.id.stock_list_view);
		mAdapter = new StockItemAdapter(this, R.layout.stock_item,
				mList.getItems(Order.STOCK));
		lv.setAdapter(mAdapter);

//		lv.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View v, int pos,
//					long id) {
//				mSelectedItem = mList.getItems(Order.STOCK).get(pos);
//				DialogFragment df = EditItemFragment.newInstance();
//				df.show(getSupportFragmentManager(), "choose_action");
//			}
//		});

		// TODO: display icons for shopping, adding an item, and 
		// rearranging stock order.
	
	
	}
	
	private void initializeDatabase() {
		this.mSlda = new ShoppingListDataAdapter();
		this.mSlda.open();

		this.mIda = new ItemDataAdapter();
		this.mIda.open();
	}

	/** Loads the shopping list and all of its items from the database */
	private void initializeShoppingList(long listId) {
		Log.d(MyApplication.GD, "Before call getList");
		mList = mSlda.getList(listId);
		Log.d(MyApplication.GD, "After call getList");

		for (Item item : mIda.getAllItemsWithListId(listId)) {
			mList.addItem(item);
		}
		// TODO: update the display (notifyDataSetChanged)
	}
	
}
