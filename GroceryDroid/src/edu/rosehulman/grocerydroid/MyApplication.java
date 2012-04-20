package edu.rosehulman.grocerydroid;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
	public SQLiteOpenHelper mDbHelper;
	
	private static MyApplication mSingleton = null;
	
	private static final boolean PURGE_DB = false;
	
	/**
	 * Get the globally accessible application instance
	 * 
	 * @return The one instance of MyApplication
	 */
	public static MyApplication getInstance() {
		return mSingleton;
	}
	
	@Override
	public void onCreate() {
		mSingleton = this;
		
		Context context = getApplicationContext();
		//Log.d(GD, "Creating single instance of Database Helper in MyApplication");
		this.mDbHelper = DatabaseHelper.createInstance(context);
		mSingleton = this;
		
		//start with a fresh database every run
		if (PURGE_DB) {
			purgeDb();
		}
		
	}

	/**
	 * Purges the database
	 */
	public void purgeDb() {
		SQLiteDatabase db = this.mDbHelper.getWritableDatabase();
		db.beginTransaction();
		this.mDbHelper.onUpgrade(db, 0, 0);
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}
}
