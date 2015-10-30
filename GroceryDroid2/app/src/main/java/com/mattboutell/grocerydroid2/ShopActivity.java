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

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mattboutell.grocerydroid2.model.Item;
import com.mattboutell.grocerydroid2.model.ShoppingList;

import java.util.ArrayList;

/**
 * The activity used to manage the use of a list in the store.
 * 
 * @author Matthew Boutell. Created Apr 25, 2012.
 */
public class ShopActivity extends ShoppingListActivity {
	private static final int REQUEST_SORTING = 1;
	/** a temporary list for this screen only (so can hide bought items). */
	private ArrayList<Item> mItemsToDisplay;
	private boolean showAll = false;

	@SuppressWarnings("ConstantConditions")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);
		getSupportActionBar().setIcon(R.mipmap.ic_action_shop);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_shop);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				launchNewItemDialog();
			}
		});

		Intent intent = this.getIntent();
		long listId = intent.getLongExtra(MainActivity.KEY_SELECTED_LIST, -1);

		// initializeDatabase();
		initializeShoppingList(listId);
		getSupportActionBar().setTitle(getShoppingList().getName());
		getSupportActionBar().setSubtitle(R.string.shop_activity_subtitle);
		mItemsToDisplay = new ArrayList<>();

		setListView((ListView) findViewById(R.id.shop_list_view));
		ShopItemAdapter sia = new ShopItemAdapter(this, "TODO");
		setItemAdapter(sia);
		sia.setShopActivity(this);

		refreshDisplay();
	}

	/**
	 * Updates the prompt depending on if any items are present.
	 */
	@Override
	protected void updateMainPrompt() {
		// mItemsToDisplay.size() > 0
		TextView tv = (TextView) findViewById(R.id.shop_list_prompt);
		if (getShoppingList().hasItemsToBuy()) {
			tv.setText(R.string.shopping_list_prompt_items_present);
		} else if (getShoppingList().hasItemsAllBought()) {
			tv.setText(R.string.shopping_list_prompt_all_bought);
		} else {
			tv.setText(R.string.shopping_list_prompt_default);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.shop, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		// // Note: similar to findViewById()
		MenuItem showHide = menu.findItem(R.id.shop_menu_show_hide);
		// Toggle the menu title. If current current status is to show, then
		// the menu asks, "Hide?" and vice-versa
		String text = this.showAll ? getString(R.string.shop_menu_hide_bought_items)
				: getString(R.string.shop_menu_show_bought_items);
		showHide.setTitleCondensed(text);
		showHide.setTitle(text);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
//		case R.id.shop_menu_item_add_name:
//			launchNewItemDialog();
//			return super.onOptionsItemSelected(menuItem);
		case R.id.shop_menu_item_reset_all:
			resetBoughtForAllItems();
			return true;
		case R.id.shop_menu_show_hide:
			this.showAll = !this.showAll;
			refreshDisplay();
			return true;
			// TODO: At some point in the flow, I should set hte items as
			// unbought.
			// case R.id.shopMenuDone:
			// for (Item itm : this.shoppingList.getItems()) {
			// itm.setBought(false);
			// }
			// this.finish();
			// }
		case R.id.shop_menu_item_sort_items:
			Intent intent = new Intent(this, SortByShopOrderActivity.class);
			intent.putExtra(MainActivity.KEY_SELECTED_LIST, getShoppingList()
					.getId());
			// CONSIDER: New: starting for result.
			startActivityForResult(intent, REQUEST_SORTING);
			return true;
		case R.id.main_help:
			DialogFragment helpFragment = new HelpDialogFragment();
			helpFragment.show(getFragmentManager(), "main_help");
			return true;
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if (requestCode == REQUEST_SORTING && resultCode == RESULT_OK) {
		refreshDisplay();
	}
}	
	
	private void resetBoughtForAllItems() {
		int nReset = 0;
		ArrayList<Item> items = getShoppingList().getItems(ShoppingList.Order.SHOP);
		for (Item item : items) {
			if (item.isBought()) {
				item.setBought(false);
				nReset++;
			}
		}
		if (nReset > 0) {
			getIda().updateAllItemsInList(getShoppingList());
			refreshDisplay();
		}
	}

	/**
	 * Only displays those items that have been bought.
	 */
	@Override
	protected void refreshDisplay() {
		mItemsToDisplay.clear();
		for (Item item : getShoppingList().getItems(ShoppingList.Order.SHOP)) {
			if (item.getNBuy() > 0) {
				if (this.showAll || !item.isBought()) {
					mItemsToDisplay.add(item);
				}
			}
		}
		updateMainPrompt();

		TextView totalSpentTV = (TextView) findViewById(R.id.shop_layout_total_spent_text);
		// CONSIDER: read format string from resources?
		String s = String.format("Spent $%.2f of $%.2f from list",
				getShoppingList().totalSpent(), getShoppingList().totalPrice());
		totalSpentTV.setText(s);
		getItemAdapter().notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshDisplay();
	}

}
