package edu.rosehulman.boutell.grocery;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

/**
 * Hides the implementation of SQLite from the rest of the app.
 * 
 * @author Matthew Boutell. Created Nov 8, 2011.
 */
public class SqliteAdapter {
	private static final String DATABASE_NAME = "groceryDroidItems.db";
	private static final String DATABASE_TABLE = "groceryItems";
	private static final int DATABASE_VERSION = 20;

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
	public static final String DB_KEY_SIZE = "size";

	/** Unit of measure */
	public static final String DB_KEY_UNIT = "unit";

	/** Whether it has been bought yet */
	public static final String DB_KEY_IS_BOUGHT = "isBought";

	private SQLiteDatabase db;
	private final Context context;
	private DBOpenHelper dbHelper;

	/*
	 * a singleton is a class that is designed to have only one instance. It has
	 * a static method getInstance() that returns the instance; the first time
	 * this method is called, it creates the global instance. Because all
	 * callers get the same instance, they can use this as a point of
	 * interaction. For example activity A may retrieve the instance and call
	 * setValue(3); later activity B may retrieve the instance and call
	 * getValue() to retrieve the last set value.
	 */

	/**
	 * Creates a ToDoDBAdapter from the given parameters.
	 * 
	 * @param context
	 */
	public SqliteAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DBOpenHelper(this.context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	/**
	 * Opens the database
	 * 
	 */
	public void open() {
		try {
			this.db = this.dbHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			this.db = this.dbHelper.getReadableDatabase();
		}
	}

	/**
	 * Deletes the database.
	 */
	public void deleteDB() {
		this.db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
	}

	/**
	 * Closes the database.
	 * 
	 */
	public void close() {
		this.db.close();
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
		newItemValues.put(DB_KEY_NUM_TO_STOCK, item.getNToStock());
		newItemValues.put(DB_KEY_NUM_TO_BUY, item.getNToBuy());
		newItemValues.put(DB_KEY_PRICE, item.getPrice());
		newItemValues.put(DB_KEY_SIZE, item.getSize());
		newItemValues.put(DB_KEY_UNIT, item.getUnit().ordinal());
		newItemValues.put(DB_KEY_IS_BOUGHT, item.isBought() ? 1 : 0);
		// Insert the row
		return this.db.insert(DATABASE_TABLE, null, newItemValues);
	}

	/**
	 * Remove the given item.
	 * 
	 * @param item
	 * @return True iff an item was successfully removed.
	 */
	public boolean removeItem(Item item) {
		return this.db.delete(DATABASE_TABLE, DB_KEY_ID + "=" + item.getId(),
				null) > 0;
	}

	/**
	 * Remove the item with the given ID.
	 * 
	 * @param id
	 * @return True iff an item was successfully removed.
	 */
	public boolean removeItemWithId(long id) {
		return this.db.delete(DATABASE_TABLE, DB_KEY_ID + "=" + id, null) > 0;
	}

	/**
	 * @return A Cursor for all the items.
	 */
	public Cursor getAllGroceryItemsCursor() {
		return this.db.query(DATABASE_TABLE, new String[] { DB_KEY_ID,
				DB_KEY_NAME, DB_KEY_NUM_TO_STOCK, DB_KEY_NUM_TO_BUY,
				DB_KEY_PRICE, DB_KEY_SIZE, DB_KEY_UNIT, DB_KEY_IS_BOUGHT },
				null, null, null, null, null);
	}

	/**
	 * Updates the given array list to contain all the items in the database
	 * 
	 * @param items
	 */
	public void updateItemsUsingDB(ArrayList<Item> items) {
		Cursor c = this.getAllGroceryItemsCursor();
		c.requery(); // refresh
		items.clear();
		if (c.moveToFirst()) {
			do {
				long id = c.getLong(c.getColumnIndexOrThrow(DB_KEY_ID));
				String name = c.getString(c.getColumnIndexOrThrow(DB_KEY_NAME));
				int nToStock = c.getInt(c
						.getColumnIndexOrThrow(DB_KEY_NUM_TO_STOCK));
				int nToBuy = c.getInt(c
						.getColumnIndexOrThrow(DB_KEY_NUM_TO_BUY));
				float price = c.getFloat(c.getColumnIndexOrThrow(DB_KEY_PRICE));
				float size = c.getFloat(c.getColumnIndexOrThrow(DB_KEY_SIZE));
				ItemUnit unit = ItemUnit.values()[c.getInt(c
						.getColumnIndexOrThrow(DB_KEY_UNIT))];
				boolean isBought = c.getInt(c
						.getColumnIndexOrThrow(DB_KEY_IS_BOUGHT)) == 1;
				Item item = new Item(id, name, nToStock, nToBuy, price, size,
						unit, isBought);
				items.add(item);
			} while (c.moveToNext());
		}
	}

	/**
	 * For each item in the items list, update the corresponding item in the
	 * database.
	 * 
	 * @param items
	 */
	public void updateItemsInDB(ArrayList<Item> items) {
		for (Item item : items) {
			updateItem(item);
		}
	}

	/**
	 * Updates the given item in the database.
	 * 
	 * @param rowIndex
	 * @param item
	 * @return True if the update succeeded.
	 * 
	 */
	protected boolean updateItem(Item item) {
		// Create a new row of values to insert
		ContentValues newItemValues = new ContentValues();
		newItemValues.put(DB_KEY_NAME, item.getName());
		newItemValues.put(DB_KEY_NUM_TO_STOCK, item.getNToStock());
		newItemValues.put(DB_KEY_NUM_TO_BUY, item.getNToBuy());
		newItemValues.put(DB_KEY_PRICE, item.getPrice());
		newItemValues.put(DB_KEY_SIZE, item.getSize());
		newItemValues.put(DB_KEY_UNIT, item.getUnit().ordinal());
		newItemValues.put(DB_KEY_IS_BOUGHT, item.isBought() ? 1 : 0);
		return this.db.update(DATABASE_TABLE, newItemValues, DB_KEY_ID + "="
				+ item.getId(), null) > 0;
	}

	// /**
	// * Returns a single row as a Cursor.
	// *
	// * @param rowIndex
	// * @return A Cursor for the given row.
	// * @throws SQLException
	// */
	// public Cursor setCursorToToDoItem(long rowIndex) throws SQLException {
	// Cursor result = this.db.query(true, DATABASE_TABLE, new String[] {
	// KEY_ID, KEY_TASK, KEY_CREATION_DATE }, KEY_ID + "=" + rowIndex,
	// null, null, null, null, null);
	// if (result.getCount() == 0 || !result.moveToFirst()) {
	// throw new SQLException("No to do items found for row: " + rowIndex);
	// }
	// return result;
	// }

	// /**
	// * Extracts a grocery item stored at a given row index in the table.
	// *
	// * @param rowIndex
	// * @return The grocery item stored in the given row of the table
	// * @throws SQLException
	// */
	// public Item getItem(long rowIndex) throws SQLException {
	// Cursor cursor = this.db.query(true, DATABASE_TABLE, new String[] {
	// DB_KEY_ID, DB_KEY_NAME, DB_KEY_NUM_TO_STOCK, DB_KEY_NUM_TO_BUY,
	// DB_KEY_PRICE, DB_KEY_SIZE, DB_KEY_UNIT, DB_KEY_IS_BOUGHT },
	// DB_KEY_ID + "=" + rowIndex, null, null, null, null, null);
	// if (cursor.getCount() == 0 || !cursor.moveToFirst()) {
	// throw new SQLException("No to do items found for row: " + rowIndex);
	// }
	// Extract all fields
	// String name = cursor.getString(cursor
	// .getColumnIndexOrThrow(DB_KEY_NAME));
	//
	// return new Item(/* fields here */);
	// }

	private static class DBOpenHelper extends SQLiteOpenHelper {

		// SQL statement to create a new database
		private static final String DATABASE_CREATE = "create table "
				+ DATABASE_TABLE + " (" + DB_KEY_ID
				+ " integer primary key autoincrement, " + DB_KEY_NAME
				+ " text not null, " + DB_KEY_NUM_TO_STOCK + " integer, "
				+ DB_KEY_NUM_TO_BUY + " integer, " + DB_KEY_PRICE + " float, "
				+ DB_KEY_SIZE + " float, " + DB_KEY_UNIT + " text, "
				+ DB_KEY_IS_BOUGHT + " integer);";

		/**
		 * Creates a DBOpenHelper from the given parameters.
		 * 
		 * @param context
		 * @param name
		 * @param factory
		 * @param version
		 * 
		 */
		public DBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(StockActivity.GD, "Upgrading from version " + oldVersion
					+ " to " + newVersion
					+ ", which will destroy all old data.");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			this.onCreate(db);
		}
	}

}
