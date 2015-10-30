/*
 * Copyright (C) 2012 Matthew Boutell 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.mattboutell.grocerydroid2;

import android.support.v7.app.AppCompatActivity;


/**
 * The sort by stock screen allows the user to re-arrange her lists by drag and
 * drop.
 * 
 * @author Matthew Boutell. Created Apr 27, 2012.
 */
public class SortByStockOrderActivity extends AppCompatActivity {
//	private SortItemAdapter mAdapter;
//
//	private ShoppingListDataAdapter mSlda;
//	private ItemDataAdapter mIda;
//
//	private ShoppingList mShoppingList;
//
//	/** Called when the activity is first created. */
//	@SuppressWarnings("ConstantConditions")
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
////		setTheme(R.style.Theme_Sherlock);
////		setTheme(R.style.Theme_Sherlock_ForceOverflow); // ABS, must come first
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.sort_stock_activity);
//		getSupportActionBar().setIcon(R.mipmap.ic_action_grabber);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//		Intent intent = this.getIntent();
//		long listId = intent.getLongExtra(MainActivity.KEY_SELECTED_LIST, -1);
//		Log.d(MyApplication.GD, "sorting by shopping order with list " + listId);
//
//		initializeDatabase();
//		initializeShoppingList(listId);
//		getSupportActionBar().setTitle(mShoppingList.getName());
//		getSupportActionBar().setSubtitle("Rearrange your list");
//
//		TouchListView tlv = (TouchListView) findViewById(R.id.sort_by_stock_order_activity_touch_list_view);
//
//		mAdapter = new SortItemAdapter(this,
//				R.layout.main_touch_list_row, mShoppingList.getItems(ShoppingList.Order.STOCK));
//		tlv.setAdapter(mAdapter);
//		tlv.setDropListener(onDrop);
//
//		updateMainPrompt();
//	}
//
//	private TouchListView.DropListener onDrop = new TouchListView.DropListener() {
//		@Override
//		public void drop(int from, int to) {
//			Item item = mAdapter.getItem(from);
//
//			mAdapter.remove(item);
//			mAdapter.insert(item, to);
//		}
//	};
//
//	private void initializeDatabase() {
//		mSlda = new ShoppingListDataAdapter();
//		mSlda.open();
//
//		mIda = new ItemDataAdapter();
//		mIda.open();
//	}
//
//	/**
//	 * Loads the shopping list with the given listId and all of its items from
//	 * the database.
//	 *
//	 * @param listId
//	 */
//	protected void initializeShoppingList(long listId) {
//		Log.d(MyApplication.GD, "Before call getList");
//		mShoppingList = mSlda.getList(listId);
//		Log.d(MyApplication.GD, "After call getList");
//
//		for (Item item : mIda.getAllItemsWithListId(listId)) {
//			mShoppingList.addItem(item);
//		}
//	}
//
//	/** Use because the prompt is different if there are no lists. */
//	private void updateMainPrompt() {
//		TextView tv = (TextView) findViewById(R.id.sort_by_stock_order_activity_prompt);
//		if (mShoppingList.getItems(ShoppingList.Order.AS_IS).size() > 0) {
//			tv.setText(R.string.sort_by_stock_order_activity_prompt_lists_present);
//		} else {
//			tv.setText(R.string.sort_by_stock_order_activity_prompt_no_lists);
//		}
//	}
//
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.sort_menu, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// handle item selection
//		switch (item.getItemId()) {
//		case R.id.sort_menu_save_order:
//			mShoppingList.setPantryOrderToListOrder(mAdapter);
//			mIda.updateAllItemsInList(mShoppingList);
////			Intent returnData = new Intent();
////			setResult(RESULT_OK, returnData);
//			finish();
//			return true;
//		case android.R.id.home:
//			finish();
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}
}

	
