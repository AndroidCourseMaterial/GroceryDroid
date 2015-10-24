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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manages the databases on the system.
 * 
 * Knows how to create new and upgrade existing databases. Also, this class
 * automatically switches from the real database to a mock database if the
 * system is being unit tested
 * 
 * * @author Adapted from code by Kevin Wells:
 * https://github.com/RHIT/Mobile/blob/master/android/src/RHITMobile/src/edu/
 * rosehulman/android/directory/com.mattboutell.grocerydroid2.db/DatabaseHelper.java. Adapted on Mar 22, 2012.
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 6;
	private static DatabaseHelper sSingleton = null;
	private static final String DATABASE_NAME = "shopping_items.com.mattboutell.grocerydroid2.db";
	private static final String MOCK_DATABASE_NAME = "mock_shopping_items.com.mattboutell.grocerydroid2.db";
	private static final boolean MOCK = false;

	/**
	 * Creates or just retrieves a singleton instance of DatabaseHelper. See
	 * http://www.touchlab.co/blog/single-sqlite-connection/ More complicated
	 * than the singleton pattern there, but keeps us from having to pass a
	 * Context into the adapter classes.
	 * 
	 * @param context
	 *            The context to use
	 * @return The single instance of DatabaseHelper
	 */
	public static synchronized DatabaseHelper createInstance(Context context) {
		sSingleton = new DatabaseHelper(context, MOCK ? MOCK_DATABASE_NAME
				: DATABASE_NAME);
		return sSingleton;
	}

	/**
	 * Retrieves the instance of DatabaseHelper
	 * 
	 * @return The single instance of DatabaseHelper
	 */
	public static DatabaseHelper getInstance() {
		assert (sSingleton != null);
		// if (instance == null) {
		// Log.d(MyApplication.GD, "Getting null instance of DatabaseHelper");
		// } else {
		// Log.d(MyApplication.GD,
		// "Getting non-null instance of DataBaseHelper");
		// }
		return sSingleton;
	}

	private DatabaseHelper(Context context, String dbName) {
		// Private to defeat instantiation from elsewhere.
		super(context, dbName, null, DATABASE_VERSION);
	}

	// SQL statement to create a new database
	private static final String CREATE_TABLE_SHOPPING_LISTS = "CREATE TABLE "
			+ ShoppingListDataAdapter.TABLE_SHOPPING_LISTS + " ("
			+ ShoppingListDataAdapter.DB_KEY_ID
			+ " integer primary key autoincrement, "
			+ ShoppingListDataAdapter.DB_KEY_NAME + " text not null, "
			+ ShoppingListDataAdapter.DB_KEY_DISPLAY_INDEX + " integer" + ");";

	// SQL statement to create a new database
	private static final String CREATE_TABLE_GROCERY_ITEMS = "CREATE TABLE "
			+ ItemDataAdapter.TABLE_GROCERY_ITEMS + " ("
			+ ItemDataAdapter.DB_KEY_ID
			+ " integer primary key autoincrement, "
			+ ItemDataAdapter.DB_KEY_LIST_ID + " integer, "
			+ ItemDataAdapter.DB_KEY_NAME + " text not null, "
			+ ItemDataAdapter.DB_KEY_NUM_TO_STOCK + " integer, "
			+ ItemDataAdapter.DB_KEY_NUM_TO_BUY + " integer, "
			+ ItemDataAdapter.DB_KEY_PRICE + " float, "
			+ ItemDataAdapter.DB_KEY_UNIT_SIZE + " float, "
			+ ItemDataAdapter.DB_KEY_UNIT_LABEL + " text, "
			+ ItemDataAdapter.DB_KEY_IS_BOUGHT + " integer,"
			+ ItemDataAdapter.DB_KEY_STOCK_IDX + " integer,"
			+ ItemDataAdapter.DB_KEY_SHOP_IDX + " integer" + ");";

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SHOPPING_LISTS);
		db.execSQL(CREATE_TABLE_GROCERY_ITEMS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "
				+ ItemDataAdapter.TABLE_GROCERY_ITEMS);
		db.execSQL("DROP TABLE IF EXISTS "
				+ ShoppingListDataAdapter.TABLE_SHOPPING_LISTS);

		this.onCreate(db);
	}
}