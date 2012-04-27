/*
 * Copyright (C) 2010 The Android Open Source Project 
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

package edu.rosehulman.grocerydroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import edu.rosehulman.grocerydroid.db.ItemDataAdapter;
import edu.rosehulman.grocerydroid.db.ShoppingListDataAdapter;
import edu.rosehulman.grocerydroid.model.Item;
import edu.rosehulman.grocerydroid.model.ShoppingList;
import edu.rosehulman.grocerydroid.model.ShoppingList.Order;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/**
 * The sort by stock screen allows the user to re-arrange her lists by drag and
 * drop.
 * 
 * @author Matthew Boutell. Created Apr 27, 2012.
 */
public class SortByStockOrderActivity extends SherlockFragmentActivity {
	private SortItemAdapter mAdapter;

	private ShoppingListDataAdapter mSlda;
	private ItemDataAdapter mIda;
	
	private ShoppingList mShoppingList;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_ForceOverflow); // ABS, must come first
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sort_stock_activity);
		getSupportActionBar().setIcon(R.drawable.ic_list); // needs to be in
															// hdpi folder
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = this.getIntent();
		long listId = intent.getLongExtra(MainActivity.KEY_SELECTED_LIST, -1);
		Log.d(MyApplication.GD, "stocking with list " + listId);
		
		initializeDatabase();
		initializeShoppingList(listId);
		getSupportActionBar().setTitle(mShoppingList.getName());
		getSupportActionBar().setSubtitle("Rearrange your list");
		
		TouchListView tlv = (TouchListView) findViewById(R.id.sort_by_stock_order_activity_touch_list_view);
		
		mAdapter = new SortItemAdapter(this,
				R.layout.main_touch_list_row, mShoppingList.getItems(Order.STOCK));
		tlv.setAdapter(mAdapter);
		tlv.setDropListener(onDrop);

		updateMainPrompt();
	}

	private TouchListView.DropListener onDrop = new TouchListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			Item item = mAdapter.getItem(from);

			mAdapter.remove(item);
			mAdapter.insert(item, to);
		}
	};

	private void initializeDatabase() {
		mSlda = new ShoppingListDataAdapter();
		mSlda.open();

		mIda = new ItemDataAdapter();
		mIda.open();
	}

	/**
	 * Loads the shopping list with the given listId and all of its items from
	 * the database.
	 * 
	 * @param listId
	 */
	protected void initializeShoppingList(long listId) {
		Log.d(MyApplication.GD, "Before call getList");
		mShoppingList = mSlda.getList(listId);
		Log.d(MyApplication.GD, "After call getList");

		for (Item item : mIda.getAllItemsWithListId(listId)) {
			mShoppingList.addItem(item);
		}
	}

	/** Use because the prompt is different if there are no lists. */
	private void updateMainPrompt() {
		TextView tv = (TextView) findViewById(R.id.sort_by_stock_order_activity_prompt);
		if (mShoppingList.getItems(Order.AS_IS).size() > 0) {
			tv.setText(R.string.sort_by_stock_order_activity_prompt_lists_present);
		} else {
			tv.setText(R.string.sort_by_stock_order_activity_prompt_no_lists);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.sort_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		case R.id.sort_menu_save_order:
			mShoppingList.setPantryOrderToListOrder(mAdapter); 
			mIda.updateAllItemsInList(mShoppingList);
			Intent returnData = new Intent();
			setResult(RESULT_OK, returnData);
			finish();
			return true;
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

	
