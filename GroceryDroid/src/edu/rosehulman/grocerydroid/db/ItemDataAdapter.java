package edu.rosehulman.grocerydroid.db;

import android.content.ContentValues;
import android.database.Cursor;

import edu.rosehulman.grocerydroid.model.Item;
import edu.rosehulman.grocerydroid.model.Item.UnitLabel;
import edu.rosehulman.grocerydroid.model.ShoppingList;

/**
 * All the operations needed to access the shopping list data in the database.
 * 
 * @author Matthew Boutell. Created Mar 22, 2012.
 */
public class ItemDataAdapter extends TableAdapter {
	/** Name of the table for the grocery items */
	static final String TABLE_GROCERY_ITEMS = "groceryItems";

	/** ID */
	static final String DB_KEY_ID = "id";

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
		newItemValues.put(DB_KEY_NUM_TO_STOCK, item.getNStock());
		newItemValues.put(DB_KEY_NUM_TO_BUY, item.getNBuy());
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
		ContentValues newItemValues = toContentValues(item);

		// Insert the row
		long rowId = sDb.insert(TABLE_GROCERY_ITEMS, null, newItemValues);
		// Update the ID if it changed
		item.setId(rowId);
		return rowId;
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

		return sDb.update(TABLE_GROCERY_ITEMS, newItemValues, DB_KEY_ID + "="
				+ item.getId(), null) == 1;
	}

	/**
	 * Removes the given item.
	 * 
	 * @param item
	 * @return True iff one item was successfully removed.
	 */
	public boolean deleteItem(Item item) {
		return sDb.delete(TABLE_GROCERY_ITEMS, DB_KEY_ID + "=" + item.getId(),
				null) == 1;
	}

	/**
	 * Removes all items with the given listId.
	 * 
	 * @param listId
	 * @return The number of items deleted.
	 */
	public int deleteAllItemsWithListId(long listId) {
		return sDb.delete(ItemDataAdapter.TABLE_GROCERY_ITEMS, DB_KEY_LIST_ID
				+ "=" + listId, null);
	}

	/**
	 * For each item in the shopping list, update the corresponding item in the
	 * database.
	 * 
	 * @param list
	 * @return The number of items which have been updated.
	 */
	public int updateAllItemsInList(ShoppingList list) {
		int nUpdated = 0;
		sDb.beginTransaction();
		try {
			for (Item item : list.getItems(ShoppingList.Order.AS_IS)) {
				nUpdated += (updateItem(item) ? 1 : 0);
			}
			sDb.setTransactionSuccessful();
		} finally {
			sDb.endTransaction();
		}

		return nUpdated;
	}

	/**
	 * Returns an iterator over the items with the given list ID. Intended to be
	 * used in a foreach loop:
	 * 
	 * for (Item item: ida.getAllItemsWithListId(id)) { items.add(item); }
	 * 
	 * @param listId
	 * @return An iterator over the items with the given list ID.
	 */
	public ItemDataAdapterIterator getAllItemsWithListId(long listId) {
		String where = DB_KEY_LIST_ID + "=" + listId;
		Cursor cursor = sDb.query(TABLE_GROCERY_ITEMS, null, where, null, null,
				null, null);
		return new ItemDataAdapterIterator(cursor);
	}

	/**
	 * An iterator for the ItemDataAdapter. Allows one to iterate over the
	 * database to populate an item or items.
	 * 
	 * @author Jimmy Theis, modified by Matthew Boutell. Created Mar 30, 2012.
	 */
	public class ItemDataAdapterIterator extends TableAdapterIterator<Item> {

		/**
		 * Creates an ItemDataAdapterIterator with the given Cursor.
		 * 
		 * @param cursor
		 */
		public ItemDataAdapterIterator(Cursor cursor) {
			super(cursor);
		}

		@Override
		protected Item getObjectFromNextRow() {
			Cursor c = getCursor();
			long id = c.getLong(c.getColumnIndexOrThrow(DB_KEY_ID));
			long listId = c.getLong(c.getColumnIndexOrThrow(DB_KEY_LIST_ID));
			String name = c.getString(c.getColumnIndexOrThrow(DB_KEY_NAME));
			int nToStock = c.getInt(c
					.getColumnIndexOrThrow(DB_KEY_NUM_TO_STOCK));
			int nToBuy = c.getInt(c.getColumnIndexOrThrow(DB_KEY_NUM_TO_BUY));
			float price = c.getFloat(c.getColumnIndexOrThrow(DB_KEY_PRICE));
			float size = c.getFloat(c.getColumnIndexOrThrow(DB_KEY_UNIT_SIZE));
			UnitLabel unit = UnitLabel.values()[c.getInt(c
					.getColumnIndexOrThrow(DB_KEY_UNIT_LABEL))];
			boolean isBought = c.getInt(c
					.getColumnIndexOrThrow(DB_KEY_IS_BOUGHT)) == 1;
			int stockIdx = c.getInt(c.getColumnIndexOrThrow(DB_KEY_STOCK_IDX));
			int shopIdx = c.getInt(c.getColumnIndexOrThrow(DB_KEY_SHOP_IDX));
			return new Item(id, listId, name, nToStock, nToBuy, price, size,
					unit, isBought, stockIdx, shopIdx);
		}
	}
}
