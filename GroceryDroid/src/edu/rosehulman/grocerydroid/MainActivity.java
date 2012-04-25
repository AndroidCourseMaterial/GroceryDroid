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
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import edu.rosehulman.grocerydroid.db.ItemDataAdapter;
import edu.rosehulman.grocerydroid.db.ShoppingListDataAdapter;
import edu.rosehulman.grocerydroid.model.ShoppingList;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import java.util.ArrayList;

/**
 * The main screen displays all the user's shopping lists.
 * 
 * @author Matthew Boutell. Created Mar 29, 2012.
 */
public class MainActivity extends SherlockFragmentActivity {
	private ShoppingListDataAdapter mSlda;
	private ItemDataAdapter mIda;
	private ArrayList<ShoppingList> mShoppingLists = null;
	private ShoppingList mSelectedList;
	private MainShoppingListAdapter mAdapter;
	/** Used for passing data via an Intent */
	public static final String KEY_SELECTED_LIST = "KEY_SELECTED_LIST";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_ForceOverflow); // ABS, must come first
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getSupportActionBar().setIcon(R.drawable.ic_list); // needs to be in
															// hdpi folder
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setSubtitle("Welcome");

		initializeDatabase();
		initializeShoppingLists();

		ListView lv = (ListView) findViewById(R.id.main_shopping_list_view);
		mAdapter = new MainShoppingListAdapter(this, R.layout.main_list,
				mShoppingLists);
		lv.setAdapter(mAdapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {
				mSelectedList = MainActivity.this.mShoppingLists.get(pos);
				DialogFragment df = ChooseActionDialogFragment.newInstance();
				df.show(getSupportFragmentManager(), "choose_action");
			}
		});
	}

	private void initializeDatabase() {
		this.mSlda = new ShoppingListDataAdapter();
		this.mSlda.open();

		this.mIda = new ItemDataAdapter();
		this.mIda.open();
	}

	/** Loads all of the shopping lists from the database */
	private void initializeShoppingLists() {
		this.mShoppingLists = new ArrayList<ShoppingList>();
		for (ShoppingList list : mSlda.getAllLists()) {
			this.mShoppingLists.add(list);
		}
		updateMainPrompt();
	}

	/** Use because the prompt is different if there are no lists. */
	private void updateMainPrompt() {
		TextView tv = (TextView) findViewById(R.id.main_screen_prompt);
		Log.d(MyApplication.GD, "There are " + mShoppingLists.size() + " lists");
		if (this.mShoppingLists.size() > 0) {
			tv.setText(R.string.main_screen_prompt_lists_present);
		} else {
			tv.setText(R.string.main_screen_prompt_default);
		}
	}

	/**
	 * Adds the given list to the activity.
	 * 
	 * @param listName
	 */
	void addList(String listName) {
		ShoppingList newList = new ShoppingList(listName);
		mSlda.insertList(newList);
		mShoppingLists.add(newList);
		mAdapter.notifyDataSetChanged();
		updateMainPrompt();
	}

	/**
	 * Deletes the list and all its items.
	 */
	void deleteList() {
		mShoppingLists.remove(mSelectedList);
		mSlda.deleteList(mSelectedList);
		mIda.deleteAllItemsWithListId(mSelectedList.getId());
		mAdapter.notifyDataSetChanged();
		updateMainPrompt();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.top_level, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		case R.id.add_list:
			DialogFragment newFragment = new AddListDialogFragment();
			newFragment.show(getSupportFragmentManager(), "add_list");
			return true;
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Launches the stock activity.
	 * 
	 */
	void launchStockActivity() {
		Log.d(MyApplication.GD,
				String.format("Stock list for %s", mSelectedList));
		Intent intent = new Intent(MainActivity.this, StockActivity.class);
		intent.putExtra(MainActivity.KEY_SELECTED_LIST, mSelectedList.getId());
		startActivity(intent);
	}

	/**
	 * Launches the shop activity.
	 * 
	 */
	void launchShopActivity() {
		Log.d(MyApplication.GD, String.format("Shop at %s", mSelectedList));
		Intent intent = new Intent(MainActivity.this, ShopActivity.class);
		intent.putExtra(MainActivity.KEY_SELECTED_LIST, mSelectedList.getId());
		startActivity(intent);
	}
}