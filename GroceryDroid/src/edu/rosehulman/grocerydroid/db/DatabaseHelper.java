package edu.rosehulman.grocerydroid.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import edu.rosehulman.grocerydroid.MyApplication;

/**
 * Manages the databases on the system.
 * 
 * Knows how to create new and upgrade existing databases. Also, this class
 * automatically switches from the real database to a mock database if the
 * system is being unit tested
 * 
 * * @author Adapted from code by Kevin Wells:
 * https://github.com/RHIT/Mobile/blob/master/android/src/RHITMobile/src/edu/
 * rosehulman/android/directory/db/DatabaseHelper.java. Adapted on Mar 22, 2012.
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private static DatabaseHelper instance = null;
	private static final String DATABASE_NAME = "shopping_items.db";
	private static final String MOCK_DATABASE_NAME = "mock_shopping_items.db";
	private static final boolean MOCK = true;

	/**
	 * Create the single, static instance of DatabaseHelper
	 * 
	 * @param context The context to use
	 * @param mock True if a mock database should be used
	 * @return The newly created DatabaseHelper
	 */
	public static DatabaseHelper createInstance(Context context, boolean mock) {
		if (mock) {
			instance = new DatabaseHelper(context.getApplicationContext(), MOCK_DATABASE_NAME);
		} else {
			instance = new DatabaseHelper(context.getApplicationContext(), DATABASE_NAME);
		}
		return instance;
	}
	
	/**
	 * Retrieves the instance of DatabaseHelper
	 * 
	 * @return The single instance of DatabaseHelper
	 */
	public static DatabaseHelper getInstance() {
		assert(instance != null);
		if (instance == null) {
			Log.d(MyApplication.GD, "Getting null instance of DatabaseHelper");
		} else {
			Log.d(MyApplication.GD, "Getting non-null instance of DataBaseHelper");
		}
		return instance;
	}
//
//	// Using simpler singleton pattern in
//	// http://www.javaworld.com/javaworld/jw-04-2003/jw-0425-designpatterns.html
//	/**
//	 * Get or create the single, static instance of DatabaseHelper
//	 * 
//	 * @param context
//	 *            The context to use
//	 * @return The single instance of DatabaseHelper
//	 */
//	public static DatabaseHelper getInstance(Context context) {
//		if (instance == null) {
//			if (MOCK) {
//				instance = new DatabaseHelper(context.getApplicationContext(),
//						MOCK_DATABASE_NAME);
//			} else {
//				instance = new DatabaseHelper(context.getApplicationContext(),
//						DATABASE_NAME);
//			}
//		}
//		return instance;
//	}

	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_SHOPPING_LISTS = "shoppingLists";

	// SQL statement to create a new database
	private static final String CREATE_TABLE_SHOPPING_LISTS = "CREATE TABLE "
			+ TABLE_SHOPPING_LISTS + " (" + ShoppingListDataAdapter.DB_KEY_ID
			+ " integer primary key autoincrement, "
			+ ShoppingListDataAdapter.DB_KEY_NAME + " text not null" + ");";

	// SQL statement to create a new database
	private static final String CREATE_TABLE_GROCERY_ITEMS = 
			"CREATE TABLE " + ItemDataAdapter.TABLE_GROCERY_ITEMS + " (" 
			+ ItemDataAdapter.DB_KEY_ID + " integer primary key autoincrement, "
			+ ItemDataAdapter.DB_KEY_NAME + " text not null, "
			+ ItemDataAdapter.DB_KEY_NUM_TO_STOCK + " integer, "
			+ ItemDataAdapter.DB_KEY_NUM_TO_BUY + " integer, "
			+ ItemDataAdapter.DB_KEY_PRICE + " float, "
			+ ItemDataAdapter.DB_KEY_UNIT_SIZE + " float, "
			+ ItemDataAdapter.DB_KEY_UNIT_LABEL + " text, "
			+ ItemDataAdapter.DB_KEY_IS_BOUGHT + " integer,"
			+ ItemDataAdapter.DB_KEY_STOCK_IDX + " integer,"
			+ ItemDataAdapter.DB_KEY_SHOP_IDX + " integer" + ");";

	private DatabaseHelper(Context context, String dbName) {
		// Private to defeat instantiation from elsewhere.
		super(context, dbName, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SHOPPING_LISTS);
		db.execSQL(CREATE_TABLE_GROCERY_ITEMS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Log.w(MainActivity.GD, "Upgrading from version " + oldVersion
		// + " to " + newVersion
		// + ", which will destroy all old data.");

		db.execSQL("DROP TABLE IF EXISTS " + ItemDataAdapter.TABLE_GROCERY_ITEMS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LISTS);

		this.onCreate(db);
	}

}