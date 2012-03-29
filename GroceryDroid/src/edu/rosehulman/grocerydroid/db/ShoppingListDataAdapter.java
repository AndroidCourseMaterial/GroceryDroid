package edu.rosehulman.grocerydroid.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import edu.rosehulman.grocerydroid.model.ShoppingList;

/**
 * All the operations needed to access the shopping list table in the database.
 * 
 * @author Matt Boutell. Created Mar 22, 2012.
 */
public class ShoppingListDataAdapter extends TableAdapter {

	/** Name of the table for the shopping lists */
	static final String TABLE_SHOPPING_LISTS = "shoppingLists";

	/** ID */
	static final String DB_KEY_ID = "_id";

	/** Name */
	static final String DB_KEY_NAME = "name";

	/**
	 * Puts all of the given shopping list's data into a ContentValues object.
	 * 
	 * @param list
	 * @return The ContentValues object for the given list.
	 */
	private ContentValues toContentValues(ShoppingList list) {
		ContentValues newItemValues = new ContentValues();
		newItemValues.put(DB_KEY_ID, list.getId());
		newItemValues.put(DB_KEY_NAME, list.getName());
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
		long rowID = this.db.insert(TABLE_SHOPPING_LISTS, null, newListValues);
		// Update the ID if it changed
		list.setId(rowID);
		return rowID;
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

		return this.db.update(TABLE_SHOPPING_LISTS, newListValues, DB_KEY_ID
				+ "=" + list.getId(), null) == 1;
	}

	/**
	 * Removes the given list.
	 * 
	 * @param list
	 * @return True iff one list was successfully removed.
	 */
	public boolean deleteList(ShoppingList list) {
		return this.db.delete(TABLE_SHOPPING_LISTS,
				DB_KEY_ID + "=" + list.getId(), null) == 1;
	}

	/**
	 * Loads all shopping lists into the given list of shopping lists.
	 * 
	 * @param lists 
	 */
	public void loadAllLists(ArrayList<ShoppingList> lists) {
		Cursor c = this.getCursorForAllItems();
		lists.clear();
		if (c.moveToFirst()) {
			do {
				long id = c.getLong(c.getColumnIndexOrThrow(DB_KEY_ID));
				String name = c.getString(c.getColumnIndexOrThrow(DB_KEY_NAME));

				ShoppingList list = new ShoppingList(id, name);
				lists.add(list);
			} while (c.moveToNext());
		}
	}

	/**
	 * @return A Cursor for all the items.
	 */
	private Cursor getCursorForAllItems() {
		return this.db.query(TABLE_SHOPPING_LISTS, null, null, null, null,
				null, null);

		/*
		 * Second arg tells which column. Null = all. Could be new String[] {
		 * DB_KEY_ID, DB_KEY_NAME}
		 */
	}
}
