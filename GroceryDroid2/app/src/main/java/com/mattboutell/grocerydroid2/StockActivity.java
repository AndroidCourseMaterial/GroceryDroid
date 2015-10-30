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
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mattboutell.grocerydroid2.model.Item;
import com.mattboutell.grocerydroid2.model.ShoppingList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * The activity used to manage the creation and restocking of the items in a
 * list.
 * 
 * @author Matthew Boutell. Created Apr 12, 2012.
 */
public class StockActivity extends ShoppingListActivity {
	// private AutoCompleteTextView mNameBox;
	// private ImageView mEditIcon;
	// private ArrayAdapter<String> mAutoAdapter;

	@SuppressWarnings("ConstantConditions")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(MyApplication.GD, "Begin onCreate stock");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock);
		getSupportActionBar().setIcon(R.mipmap.ic_action_notepaper);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_stock);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
                launchNewItemDialog();
			}
		});

		Intent intent = this.getIntent();
		long listId = intent.getLongExtra(MainActivity.KEY_SELECTED_LIST, -1);
		Log.d(MyApplication.GD, "stocking with list " + listId);

//		initializeDatabase();
		initializeShoppingList(listId);
		getSupportActionBar().setTitle(getShoppingList().getName());
		getSupportActionBar().setSubtitle(R.string.stock_activity_subtitle);

		setListView((ListView) findViewById(R.id.stock_list_view));
		// Manage the adapter's list myself in refreshDisplay.
		// StockItemAdapter sia = new StockItemAdapter(this,
		// R.layout.stock_item,
		// getShoppingList().getItems(Order.STOCK));
		StockItemAdapter sia = new StockItemAdapter(this, "TODO:SHOP_LIST_KEY");
		setItemAdapter(sia);
		sia.setStockActivity(this);
		refreshDisplay();
		Log.d(MyApplication.GD, "End onCreate stock");
	}

	@Override
	protected void onResume() {
		Log.d(MyApplication.GD, "Begin onresume stock");
		super.onResume();
		initializeShoppingList(getShoppingList().getId());
		refreshDisplay();
		Log.d(MyApplication.GD, "End onresume stock");
	}

	@Override
	protected void refreshDisplay() {
		updateMainPrompt();
// TODO: update
//		getItemAdapter().clear();
//		for (Item item : getShoppingList().getItems(ShoppingList.Order.STOCK)) {
//			getItemAdapter().add(item);
//		}
		getItemAdapter().notifyDataSetChanged(); // redundant?
	}

	/**
	 * Updates the prompt depending on if any items are present.
	 */
	@Override
	protected void updateMainPrompt() {
		TextView tv = (TextView) findViewById(R.id.stock_list_prompt);
		if (getShoppingList().getItems(ShoppingList.Order.STOCK).size() > 0) {
			tv.setText(R.string.shopping_list_prompt_items_present);
		} else {
			tv.setText(R.string.shopping_list_prompt_default);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.stock, menu);
		// TODO: add this back in once I get autocomplte working:
		// <item
		// android:id="@+id/add_name_menu_item"
		// android:icon="@drawable/ic_action_plus"
		// android:showAsAction="collapseActionView|always"
		// android:title="@string/add_item"
		// android:actionLayout="@layout/add_name_group"
		// />

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
//		case R.id.stock_menu_item_add_name:
//			launchNewItemDialog();
//			return super.onOptionsItemSelected(menuItem);
		case R.id.stock_menu_item_reset_all:
			resetNumberToBuyForAllItems();
			return true;
		case R.id.stock_menu_item_go_shopping:
			// Note: My original idea was to launch the ShopActivity right from
			// here. But
			// if I added an item in ShopActivity, it wasn't updated when the
			// back button
			// was pressed. The current solution is cleaner.
			Intent returnData = new Intent();
			returnData.putExtra(MainActivity.KEY_GO_SHOPPING,
					MainActivity.GO_SHOPPING);
			setResult(RESULT_OK, returnData);
			finish();

			return true;
		case R.id.stock_menu_item_sort_items:
			Intent intent = new Intent(this, SortByStockOrderActivity.class);
			intent.putExtra(MainActivity.KEY_SELECTED_LIST, getShoppingList()
					.getId());
			startActivity(intent);
			return true;
// TODO: Add back in if production		
//		case R.id.stock_menu_item_load_items_from_spreadsheet:
//			loadItemsFromSpreadsheet();
//			return true;
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

	private void resetNumberToBuyForAllItems() {
		int nReset = 0;
		ArrayList<Item> items = getShoppingList().getItems(ShoppingList.Order.AS_IS);
		for (Item item : items) {
			if (item.getNBuy() > 0) {
				item.resetNumberToBuy();
				nReset++;
			}
		}
		if (nReset > 0) {
			getIda().updateAllItemsInList(getShoppingList());
			refreshDisplay();
		}
	}

	private void loadItemsFromSpreadsheet() {
		ArrayList<Item> spreadsheetItems = new ArrayList<>();
		long dummyID = 0;
		long listId = getShoppingList().getId();
		Resources r = this.getResources();
		// InputStream myFile = r.openRawResource(R.raw.shoprite_list);
		InputStream myFile = r.openRawResource(R.raw.sams_list);
		Scanner sc = new Scanner(myFile);
		String line;
		while (sc.hasNextLine()) {
			line = sc.nextLine();
			String[] tokens = line.split(",");
			List<String> tokenList = Arrays.asList(tokens);
			Log.d(MyApplication.GD, tokenList.toString());
			int nToStock = 0;
			int i = 0;
			try {
				if (tokens[i].length() > 0) {
					nToStock = Integer.parseInt(tokens[i]);
				}
			} catch (NumberFormatException nfe) {
				Log.d(MyApplication.GD,
						String.format("Error reading [%s] as int", tokens[i]));
			}

			i++;
			float price = 0;
			try {
				if (tokens[i].length() > 0) {
					price = Float.parseFloat(tokens[i]);
				}
			} catch (NumberFormatException nfe) {
				Log.d(MyApplication.GD,
						String.format("Error reading [%s] as float", tokens[i]));
			}

			i++;
			float size = 0;
			try {
				if (tokens[i].length() > 0) {
					size = Float.parseFloat(tokens[i]);
				}
			} catch (NumberFormatException nfe) {
				Log.d(MyApplication.GD, 
						String.format("Error reading [%s] as float", tokens[i]));
			}
			i++;
			Item.UnitLabel unitLabel = Item.UnitLabel.unit;
			String potentialUnit = tokens[i];
			for (Item.UnitLabel u : Item.UnitLabel.values()) {
				if (u.toString().equalsIgnoreCase(potentialUnit)) {
					unitLabel = u;
				}
			}
			i++;
			// Clean it up
			String name = tokens[i].trim();
			if (name.charAt(0) == '\"') {
				name = name.substring(1);
			}
			spreadsheetItems.add(new Item(dummyID, listId, name, nToStock,
					price, size, unitLabel));
		}

		// Remove the current data
		getIda().deleteAllItemsWithListId(listId);

		// replace with current data
		getShoppingList().setItems(spreadsheetItems);

		Log.d(MyApplication.GD, spreadsheetItems.toString());
		// Insert into db and get real ID
		// Conc mod exception...
		for (Item item : getShoppingList().getItems(ShoppingList.Order.AS_IS)) {
			// for (int i = 0; i < spreadsheetItems.size(); i++) {
			// Item item = spreadsheetItems.get(i);
			long id = getIda().insertItem(item);
			item.setId(id);
		}
		refreshDisplay();
	}
}
