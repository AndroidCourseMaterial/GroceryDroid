package edu.rosehulman.grocerydroid.db;

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
	static final String DB_KEY_ID = "id";

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
		// CONSIDER: when I put an id that was already in the table, it gave me 
		// a sqlite error (code 19). But don't I want to insert by list ID?
		//newItemValues.put(DB_KEY_ID, list.getId());
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
		long rowId = db.insert(TABLE_SHOPPING_LISTS, null, newListValues);
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

		return db.update(TABLE_SHOPPING_LISTS, newListValues, DB_KEY_ID + "="
				+ list.getId(), null) == 1;
	}

	/**
	 * Removes the given list.
	 * 
	 * @param list
	 * @return True iff one list was successfully removed.
	 */
	public boolean deleteList(ShoppingList list) {
		return db.delete(TABLE_SHOPPING_LISTS, DB_KEY_ID + "=" + list.getId(),
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
		Cursor c = db.query(TABLE_SHOPPING_LISTS, null, where, null, null,
				null, null);
		c.moveToFirst();
		String name = c.getString(c.getColumnIndexOrThrow(DB_KEY_NAME));
		return new ShoppingList(id, name);
	}
	
	
	/**
	 * Returns an iterator over all the lists in the table.
	 * 
	 * @return An iterator over the lists.
	 */
	public ShoppingListDataAdapterIterator getAllLists() {
		Cursor cursor = db.query(TABLE_SHOPPING_LISTS, null, null, null, null,
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
			return new ShoppingList(id, name);
		}
	}
}
