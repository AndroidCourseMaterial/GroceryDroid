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
package com.mattboutell.grocerydroid2.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.mattboutell.grocerydroid2.model.ShoppingList;

import java.util.ArrayList;

/**
 * All the operations needed to access the shopping list table in the database.
 * 
 * @author Matthew Boutell. Created Mar 22, 2012.
 */
public class ShoppingListDataAdapter extends TableAdapter {

	/** Name of the table for the shopping lists */
	static final String TABLE_SHOPPING_LISTS = "shoppingLists";

	/** ID */
	static final String DB_KEY_ID = "id";

	/** Name */
	static final String DB_KEY_NAME = "name";

	/** Display order */
	static final String DB_KEY_DISPLAY_INDEX = "displayIndex";
	
	/**
	 * Puts all of the given shopping list's data into a ContentValues object.
	 * 
	 * @param list
	 * @return The ContentValues object for the given list.
	 */
	private ContentValues toContentValues(ShoppingList list) {
		ContentValues newItemValues = new ContentValues();
		// CONSIDER: when I put an id that was already in the table, it gave me 
		// a sqlite error (code 19). But don't I want to insert by list ID?
		//newItemValues.put(DB_KEY_ID, list.getId());
		newItemValues.put(DB_KEY_NAME, list.getName());
		newItemValues.put(DB_KEY_DISPLAY_INDEX, list.getDisplayIdx());
		return newItemValues;
	}

	/**
	 * Inserts the given list. Using a ContentValues object makes it
	 * strongly-typed.
	 * 
	 * @param list
	 * @return The row ID of the new row, -1 if an error.
	 */
	public long insertList(ShoppingList list) {
		// Create a new row of values to insert
		ContentValues newListValues = this.toContentValues(list);

		// Insert the row
		long rowId = sDb.insert(TABLE_SHOPPING_LISTS, null, newListValues);
		// Update the ID if it changed
		list.setId(rowId);
		return rowId;
	}

	/**
	 * Updates the given list.
	 * 
	 * @param list
	 * @return True iff exactly one list was successfully updated.
	 * 
	 */
	public boolean updateList(ShoppingList list) {
		ContentValues newListValues = toContentValues(list);

		return sDb.update(TABLE_SHOPPING_LISTS, newListValues, DB_KEY_ID + "="
				+ list.getId(), null) == 1;
	}

	/**
	 * Removes the given list.
	 * 
	 * @param list
	 * @return True iff one list was successfully removed.
	 */
	public boolean deleteList(ShoppingList list) {
		return sDb.delete(TABLE_SHOPPING_LISTS, DB_KEY_ID + "=" + list.getId(),
				null) == 1;
	}

	/** 
	 * Gets the list with the given id.
	 * 
	 * @param id
	 * @return The shopping list with the given id.
	 */
	public ShoppingList getList(long id) {
		//TODO: unit-test this 
		String where = DB_KEY_ID + "=" + id;
		Cursor c = sDb.query(TABLE_SHOPPING_LISTS, null, where, null, null,
				null, null);
		c.moveToFirst();
		String name = c.getString(c.getColumnIndexOrThrow(DB_KEY_NAME));
		int displayIndex = c.getInt(c.getColumnIndexOrThrow(DB_KEY_DISPLAY_INDEX));
		c.close();
		return new ShoppingList(id, name, displayIndex);
	}
	
	/**
	 * Update all the lists passed to it.
	 * 
	 * @param lists
	 * @return The number of items which have been updated.
	 */
	public int updateAllLists(ArrayList<ShoppingList> lists) {
		// TODO Unit-test this
		int nUpdated = 0;
		sDb.beginTransaction();
		try {
			for (ShoppingList list : lists) {
				nUpdated += (updateList(list) ? 1 : 0);
			}
			sDb.setTransactionSuccessful();
		} finally {
			sDb.endTransaction();
		}

		return nUpdated;
	}
	
	/**
	 * Returns an iterator over all the lists in the table.
	 * 
	 * @return An iterator over the lists.
	 */
	public ShoppingListDataAdapterIterator getAllLists() {
		Cursor cursor = sDb.query(TABLE_SHOPPING_LISTS, null, null, null, null,
				null, null);
		return new ShoppingListDataAdapterIterator(cursor);
	}

	/**
	 * An iterator for the ShoppingListDataAdapter. Allows one to iterate over
	 * the database to populate a list of lists.
	 * 
	 * @author Jimmy Theis, modified by Matt Boutell. Created Mar 30, 2012.
	 */
	public class ShoppingListDataAdapterIterator extends
			TableAdapterIterator<ShoppingList> {

		/**
		 * Creates a ShoppingListDataAdapterIterator with the given Cursor.
		 * 
		 * @param cursor
		 */
		public ShoppingListDataAdapterIterator(Cursor cursor) {
			super(cursor);
		}

		@Override
		protected ShoppingList getObjectFromNextRow() {
			Cursor c = getCursor();
			long id = c.getLong(c.getColumnIndexOrThrow(DB_KEY_ID));
			String name = c.getString(c.getColumnIndexOrThrow(DB_KEY_NAME));
			int displayIndex = c.getInt(c.getColumnIndexOrThrow(DB_KEY_DISPLAY_INDEX));
			return new ShoppingList(id, name, displayIndex);
		}
	}
}
