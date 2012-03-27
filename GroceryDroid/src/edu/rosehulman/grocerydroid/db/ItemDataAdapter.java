package edu.rosehulman.grocerydroid.db;

import android.content.ContentValues;
import edu.rosehulman.grocerydroid.model.Item;


/**
 * All the operations needed to access the shopping list data in the database.
 * @author boutell.
 *         Created Mar 22, 2012.
 */
public class ItemDataAdapter extends TableAdapter {
	static final String TABLE_GROCERY_ITEMS = "groceryItems";
	
	/** ID */
	public static final String DB_KEY_ID = "_id";

	/** Name */
	public static final String DB_KEY_NAME = "name";

	/** Number to stock */
	public static final String DB_KEY_NUM_TO_STOCK = "numberToStock";

	/** Number to buy */
	public static final String DB_KEY_NUM_TO_BUY = "numberToBuy";

	/** Price */
	public static final String DB_KEY_PRICE = "price";

	/** Size */
	public static final String DB_KEY_UNIT_SIZE = "unitSize";

	/** Unit of measure */
	public static final String DB_KEY_UNIT_LABEL = "unitLabel";

	/** Whether it has been bought yet */
	public static final String DB_KEY_IS_BOUGHT = "isBought";

	/** Position in the list when displayed in stock (pantry) order */
	public static final String DB_KEY_STOCK_IDX = "stockIdx";

	/** Position in the list when displayed in stock (pantry) order */
	public static final String DB_KEY_SHOP_IDX = "shopIdx";

	// TODO: add methods to access the database and then test this access.

	// THere are lots of such methods in the old code. Use them.
	// Also get them from Jimmy notes and from CRC cards.
	
	public ItemDataAdapter() {
		super();
	}
	
	/**
	 * Strongly-typed insertion of a new Item.
	 * 
	 * @param item
	 * @return The row ID of the new row.
	 */
	public long insertItem(Item item) {
		// Create a new row of values to insert
		ContentValues newItemValues = new ContentValues();
		newItemValues.put(DB_KEY_NAME, item.getName());
		newItemValues.put(DB_KEY_NUM_TO_STOCK, item.getnStock());
		newItemValues.put(DB_KEY_NUM_TO_BUY, item.getnBuy());
		newItemValues.put(DB_KEY_PRICE, item.getPrice());
		newItemValues.put(DB_KEY_UNIT_SIZE, item.getUnitSize());
		newItemValues.put(DB_KEY_UNIT_LABEL, item.getUnitLabel().ordinal());
		newItemValues.put(DB_KEY_IS_BOUGHT, item.isBought() ? 1 : 0);
		newItemValues.put(DB_KEY_STOCK_IDX, item.getStockIdx());
		newItemValues.put(DB_KEY_SHOP_IDX, item.getShopIdx());
		
		// Insert the row
		long rowID = this.db.insert(ItemDataAdapter.TABLE_GROCERY_ITEMS, null, newItemValues);
		// Update the location if it changed 
		item.setId(rowID);
		return rowID;
	}
	
	/**
	 * Updates the given item.
	 * 
	 * @param rowIndex
	 * @param item
	 * @return True if the update succeeded.
	 * 
	 */
	public boolean updateItem(Item item) {
		// Create a new row of values to insert
		ContentValues newItemValues = new ContentValues();
		newItemValues.put(DB_KEY_NAME, item.getName());
		newItemValues.put(DB_KEY_NUM_TO_STOCK, item.getnStock());
		newItemValues.put(DB_KEY_NUM_TO_BUY, item.getnBuy());
		newItemValues.put(DB_KEY_PRICE, item.getPrice());
		newItemValues.put(DB_KEY_UNIT_SIZE, item.getUnitSize());
		newItemValues.put(DB_KEY_UNIT_LABEL, item.getUnitLabel().ordinal());
		newItemValues.put(DB_KEY_IS_BOUGHT, item.isBought() ? 1 : 0);
		newItemValues.put(DB_KEY_STOCK_IDX, item.getStockIdx());
		newItemValues.put(DB_KEY_SHOP_IDX, item.getShopIdx());
		
		return this.db.update(TABLE_GROCERY_ITEMS, newItemValues, DB_KEY_ID + "="
				+ item.getId(), null) > 0;
	}


	
	
}

