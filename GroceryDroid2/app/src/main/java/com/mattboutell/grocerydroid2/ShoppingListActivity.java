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
import android.util.Log;
import android.widget.ListView;

import com.mattboutell.grocerydroid2.db.ItemDataAdapter;
import com.mattboutell.grocerydroid2.db.ShoppingListDataAdapter;
import com.mattboutell.grocerydroid2.model.Item;
import com.mattboutell.grocerydroid2.model.ShoppingList;
import com.mattboutell.grocerydroid2.ItemDialogFragment.Mode;

/**
 * A base class for any activity that uses a shopping list.
 * 
 * @author Matthew Boutell. Created Apr 11, 2012.
 */
public abstract class ShoppingListActivity extends AppCompatActivity {
	private ShoppingListDataAdapter mSlda;
	private ItemDataAdapter mIda;
	private ShoppingList mShoppingList;
	private ListView mListView;
	private ItemAdapter mItemAdapter;

	
	/**
	 * Creates data adapters for each table, and opens them for writing.
	 *
	 */
	protected void initializeDatabase() {
		this.mSlda = new ShoppingListDataAdapter();
		this.mSlda.open();

		this.mIda = new ItemDataAdapter();
		this.mIda.open();
	}

	/**
	 * Returns the value of the field called 'mIda'.
	 * @return Returns the mIda.
	 */
	public ItemDataAdapter getIda() {
		return this.mIda;
	}

	/**
	 * Loads the shopping list with the given listId and all of its items from
	 * the database.
	 * 
	 * @param listId
	 */
	protected void initializeShoppingList(long listId) {
		
		Log.d(MyApplication.GD, "Before call getList");
		setShoppingList(mSlda.getList(listId));
		Log.d(MyApplication.GD, "After call getList");

		getShoppingList().getItems(ShoppingList.Order.AS_IS).clear();
		for (Item item : mIda.getAllItemsWithListId(listId)) {
			getShoppingList().addItem(item);
		}
	}

	/**
	 * Updates the prompt depending on if any items are present.
	 */
	protected abstract void updateMainPrompt();

	/**
	 * Returns the value of the field called 'mShoppingList'.
	 * 
	 * @return Returns the mShoppingList.
	 */
	protected ShoppingList getShoppingList() {
		return this.mShoppingList;
	}

	/**
	 * Sets the field called 'mShoppingList' to the given value.
	 * 
	 * @param mShoppingList
	 *            The mShoppingList to set.
	 */
	protected void setShoppingList(ShoppingList mShoppingList) {
		this.mShoppingList = mShoppingList;
	}

	/**
	 * Returns the value of the field called 'mListView'.
	 * 
	 * @return Returns the mListView.
	 */
	protected ListView getListView() {
		return this.mListView;
	}

	/**
	 * Sets the field called 'mListView' to the given value.
	 * 
	 * @param mListView
	 *            The mListView to set.
	 */
	protected void setListView(ListView mListView) {
		this.mListView = mListView;
	}

	/**
	 * Returns the value of the field called 'mItemAdapter'.
	 * 
	 * @return Returns the mItemAdapter.
	 */
	protected ItemAdapter getItemAdapter() {
		return this.mItemAdapter;
	}

	/**
	 * Sets the field called 'mItemAdapter' to the given value.
	 * 
	 * @param itemAdapter
	 *            The itemAdapter to set.
	 */
	protected void setItemAdapter(ItemAdapter itemAdapter) {
		this.mItemAdapter = itemAdapter;
		mListView.setAdapter(mItemAdapter);
	}

	/**
	 * Launches a dialog to create a new item for editing.
	 */
	protected void launchNewItemDialog() {
		Log.d(MyApplication.GD, "About to create new ItemDialogFragment");
		ItemDialogFragment newFragment = new ItemDialogFragment();
		Log.d(MyApplication.GD, "Just created new ItemDialogFragment");
		newFragment.initializeItem(mShoppingList.getId());
		newFragment.setMode(Mode.ADD);

		Log.d(MyApplication.GD, "About to show new ItemDialogFragment");
		newFragment.show(getFragmentManager(), "add_item");
		Log.d(MyApplication.GD, "Just showed new ItemDialogFragment");
	}

	/**
	 * Launches a dialog to edit the given item. Details must be
	 * added via the edit item method.
	 * 
	 * @param item 
	 */
	protected void launchEditItemDialog(Item item) {
		Log.d(MyApplication.GD, "About to create new ItemDialogFragment");
		ItemDialogFragment newFragment = new ItemDialogFragment();
		Log.d(MyApplication.GD, "Just created new ItemDialogFragment");
		newFragment.setItem(item);
		newFragment.setMode(Mode.EDIT);
		Log.d(MyApplication.GD, "About to show new ItemDialogFragment");
		newFragment.show(getFragmentManager(), "edit_item");
		Log.d(MyApplication.GD, "Just showed new ItemDialogFragment");
	}

	/**
	 * Add the given item to this activity's shopping list and the database,
	 * then update the view.
	 * 
	 * @param item
	 */
	protected void addItem(Item item) {
		mShoppingList.addItem(item);
		mIda.insertItem(item);
		refreshDisplay();
	}

	/**
	 * Updates the pertinent information in the display.
	 *
	 */
	protected abstract void refreshDisplay();
	
	/**
	 * Updates the given item in the database.
	 *
	 * @param item
	 */
	protected void updateItem(Item item) {
		mShoppingList.updateItem(item);
		mIda.updateItem(item);
		refreshDisplay();
	}
	
	/**
	 * Deletes the given item from the list
	 * 
	 * @param item
	 */
	protected void deleteItem(Item item) {
		Log.d(MyApplication.GD, "Deleting from list the item " + item);
		mShoppingList.deleteItem(item);
		Log.d(MyApplication.GD, "Deleting from Db the item " + item);
		mIda.deleteItem(item);
		refreshDisplay();
	}
}
