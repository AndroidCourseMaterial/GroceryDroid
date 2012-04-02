package edu.rosehulman.grocerydroid;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import edu.rosehulman.grocerydroid.db.DatabaseHelper;

/**
 * Initialize static application data
 */
public class MyApplication extends Application {
	
	/**
	 * Tag for debugging to logcat.
	 */
	public static final String GD = "GD";
	
	/**
	 * The global database helper
	 */
	public SQLiteOpenHelper dbHelper;
	
	private static MyApplication instance = null;
	
	private static final boolean PURGE_DB = false;
	
	/**
	 * Should we check for intensive work on the UI thread?
	 */
	public static boolean CHECK_UI_THREAD = false;
	
	/**
	 * Get the globally accessible application instance
	 * 
	 * @return The one instance of MyApplication
	 */
	public static MyApplication getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		instance = this;
		
		Context context = getApplicationContext();
		Log.d(GD, "Creating single instance of Database Helper in MyApplication");
		this.dbHelper = DatabaseHelper.createInstance(context);
		instance = this;
		
		//start with a fresh database every run
		if (PURGE_DB) {
			purgeDb();
		}
		
	}

	/**
	 * Purges the database
	 */
	public void purgeDb() {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		db.beginTransaction();
		this.dbHelper.onUpgrade(db, 0, 0);
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}
}
