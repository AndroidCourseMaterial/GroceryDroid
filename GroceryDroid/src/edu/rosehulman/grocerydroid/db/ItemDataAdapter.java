package edu.rosehulman.grocerydroid.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import edu.rosehulman.grocerydroid.model.Item;
import edu.rosehulman.grocerydroid.model.ItemUnitLabel;

/**
 * All the operations needed to access the shopping list data in the database.
 * 
 * @author Matthew Boutell. Created Mar 22, 2012.
 */
public class ItemDataAdapter extends TableAdapter {
	/** Name of the table for the grocery items */
	static final String TABLE_GROCERY_ITEMS = "groceryItems";

	/** ID */
	static final String DB_KEY_ID = "_id";

	/** ID of the shopping list to which this item belongs */
	static final String DB_KEY_LIST_ID = "listId";

	/** Name */
	static final String DB_KEY_NAME = "name";

	/** Number to stock */
	static final String DB_KEY_NUM_TO_STOCK = "numberToStock";

	/** Number to buy */
	static final String DB_KEY_NUM_TO_BUY = "numberToBuy";

	/** Price */
	static final String DB_KEY_PRICE = "price";

	/** Size */
	static final String DB_KEY_UNIT_SIZE = "unitSize";

	/** Unit of measure */
	static final String DB_KEY_UNIT_LABEL = "unitLabel";

	/** Whether it has been bought yet */
	static final String DB_KEY_IS_BOUGHT = "isBought";

	/** Position in the list when displayed in stock (pantry) order */
	static final String DB_KEY_STOCK_IDX = "stockIdx";

	/** Position in the list when displayed in stock (pantry) order */
	static final String DB_KEY_SHOP_IDX = "shopIdx";

	/**
	 * Puts all of the given item's data into a ContentValues object.
	 * 
	 * @param item
	 * @return The ContentValues object containing the given item's data.
	 */
	private ContentValues toContentValues(Item item) {
		ContentValues newItemValues = new ContentValues();
		newItemValues.put(DB_KEY_LIST_ID, item.getListId());
		newItemValues.put(DB_KEY_NAME, item.getName());
		newItemValues.put(DB_KEY_NUM_TO_STOCK, item.getnStock());
		newItemValues.put(DB_KEY_NUM_TO_BUY, item.getnBuy());
		newItemValues.put(DB_KEY_PRICE, item.getPrice());
		newItemValues.put(DB_KEY_UNIT_SIZE, item.getUnitSize());
		newItemValues.put(DB_KEY_UNIT_LABEL, item.getUnitLabel().ordinal());
		newItemValues.put(DB_KEY_IS_BOUGHT, item.isBought() ? 1 : 0);
		newItemValues.put(DB_KEY_STOCK_IDX, item.getStockIdx());
		newItemValues.put(DB_KEY_SHOP_IDX, item.getShopIdx());
		return newItemValues;
	}

	/**
	 * Inserts the given item. Using a ContentValues object makes it
	 * strongly-typed.
	 * 
	 * @param item
	 * @return The row ID of the new row, -1 if an error.
	 */
	public long insertItem(Item item) {
		// Create a new row of values to insert
		ContentValues newItemValues = this.toContentValues(item);

		// Insert the row
		long rowID = this.db.insert(TABLE_GROCERY_ITEMS, null, newItemValues);
		// Update the ID if it changed
		item.setId(rowID);
		return rowID;
	}

	/**
	 * Updates the given item.
	 * 
	 * @param item
	 * @return True iff exactly one item was successfully updated.
	 * 
	 */
	public boolean updateItem(Item item) {
		ContentValues newItemValues = toContentValues(item);

		return this.db.update(TABLE_GROCERY_ITEMS, newItemValues, DB_KEY_ID
				+ "=" + item.getId(), null) == 1;
	}

	/**
	 * Removes the given item.
	 * 
	 * @param item
	 * @return True iff one item was successfully removed.
	 */
	public boolean deleteItem(Item item) {
		return this.db.delete(TABLE_GROCERY_ITEMS,
				DB_KEY_ID + "=" + item.getId(), null) == 1;
	}

	/**
	 * Removes all items with the given listId.
	 * 
	 * @param listId
	 * @return The number of items deleted.
	 */
	public int deleteAllItemsWithListId(long listId) {
		return this.db.delete(ItemDataAdapter.TABLE_GROCERY_ITEMS,
				DB_KEY_LIST_ID + "=" + listId, null);
	}

	/**
	 * Loads all items in the given shopping list (those having the given list
	 * id) into the list of items.
	 * 
	 * @param items
	 * @param listId
	 */
	public void loadAllItemsWithListId(ArrayList<Item> items, long listId) {
		Cursor c = this.getCursorForAllItemsWithId(listId);
		items.clear();
		if (c.moveToFirst()) {
			do {
				long id = c.getLong(c.getColumnIndexOrThrow(DB_KEY_ID));
				assert (listId == c.getLong(c
						.getColumnIndexOrThrow(DB_KEY_LIST_ID)));
				String name = c.getString(c.getColumnIndexOrThrow(DB_KEY_NAME));
				int nToStock = c.getInt(c
						.getColumnIndexOrThrow(DB_KEY_NUM_TO_STOCK));
				int nToBuy = c.getInt(c
						.getColumnIndexOrThrow(DB_KEY_NUM_TO_BUY));
				float price = c.getFloat(c.getColumnIndexOrThrow(DB_KEY_PRICE));
				float size = c.getFloat(c
						.getColumnIndexOrThrow(DB_KEY_UNIT_SIZE));
				ItemUnitLabel unit = ItemUnitLabel.values()[c.getInt(c
						.getColumnIndexOrThrow(DB_KEY_UNIT_LABEL))];
				boolean isBought = c.getInt(c
						.getColumnIndexOrThrow(DB_KEY_IS_BOUGHT)) == 1;
				int stockIdx = c.getInt(c
						.getColumnIndexOrThrow(DB_KEY_STOCK_IDX));
				int shopIdx = c
						.getInt(c.getColumnIndexOrThrow(DB_KEY_SHOP_IDX));
				Item item = new Item(id, listId, name, nToStock, nToBuy, price,
						size, unit, isBought, stockIdx, shopIdx);
				items.add(item);
			} while (c.moveToNext());
		}
	}

	/**
	 * @param listId
	 * @return A Cursor for all the items with the given list ID.
	 */
	private Cursor getCursorForAllItemsWithId(long listId) {
		String where = DB_KEY_LIST_ID + "=" + listId;
		return this.db.query(TABLE_GROCERY_ITEMS, null, where, null, null,
				null, null);

		/*
		 * Second arg tells which column. Null = all. Could be new String[] {
		 * DB_KEY_ID, DB_KEY_NAME, DB_KEY_NUM_TO_STOCK, DB_KEY_NUM_TO_BUY,
		 * DB_KEY_PRICE, DB_KEY_UNIT_SIZE, DB_KEY_UNIT_LABEL, DB_KEY_IS_BOUGHT,
		 * DB_KEY_STOCK_IDX, DB_KEY_SHOP_IDX }
		 */
	}
}
