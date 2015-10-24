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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.mattboutell.grocerydroid2.db.ItemDataAdapter;
import com.mattboutell.grocerydroid2.db.ShoppingListDataAdapter;
import com.mattboutell.grocerydroid2.model.Item;
import com.mattboutell.grocerydroid2.model.ShoppingList;


/**
 * The sort by shop screen allows the user to re-arrange her lists by drag and
 * drop.
 * 
 * Consider: it is identical to the corresponding version for the stock screen,
 * except for which database key is changed.
 * 
 * @author Matthew Boutell. Created Apr 27, 2012.
 */
public class SortByShopOrderActivity extends AppCompatActivity {
	private SortItemAdapter mAdapter; // TODO

	private ShoppingListDataAdapter mSlda;
	private ItemDataAdapter mIda;
	
	private ShoppingList mShoppingList;
	
	/** Called when the activity is first created. */
	@SuppressWarnings("ConstantConditions")
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		setTheme(R.style.Theme_Sherlock_ForceOverflow); // ABS, must come first
//		setTheme(R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);
		// Re-using stock layout.
		setContentView(R.layout.sort_stock_activity);
		getSupportActionBar().setIcon(R.mipmap.ic_action_grabber); // needs to be in
															// hdpi folder
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = this.getIntent();
		long listId = intent.getLongExtra(MainActivity.KEY_SELECTED_LIST, -1);
		Log.d(MyApplication.GD, "sorting by shopping order with list " + listId);
		
		initializeDatabase();
		initializeShoppingList(listId);
		getSupportActionBar().setTitle(mShoppingList.getName());
		getSupportActionBar().setSubtitle("Rearrange your list");
		
		TouchListView tlv = (TouchListView) findViewById(R.id.sort_by_stock_order_activity_touch_list_view);

		// Note: Shop and stock different. But only change to layout needed 
		TextView tv = (TextView)findViewById(R.id.sort_by_stock_order_activity_prompt);
		tv.setText(R.string.sort_by_shop_order_activity_prompt_lists_present);
		
		// Note: Shop and stock different  
		mAdapter = new SortItemAdapter(this,
				R.layout.main_touch_list_row, mShoppingList.getItems(ShoppingList.Order.SHOP));
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

	/** Use because the prompt is different if there are no lists. 
	 * Note: Shop and stock different  */
	private void updateMainPrompt() {
		// Reusing from stock.
		TextView tv = (TextView) findViewById(R.id.sort_by_stock_order_activity_prompt);
		if (mShoppingList.getItems(ShoppingList.Order.AS_IS).size() > 0) {
			tv.setText(R.string.sort_by_shop_order_activity_prompt_lists_present);
		} else {
			tv.setText(R.string.sort_by_stock_order_activity_prompt_no_lists);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.sort_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		case R.id.sort_menu_save_order:
			// Note: Shop and stock different
			mShoppingList.setShoppingOrderToListOrder(mAdapter);
			mIda.updateAllItemsInList(mShoppingList);
			// CONSIDER: This doesn't appear to be working!
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

	
