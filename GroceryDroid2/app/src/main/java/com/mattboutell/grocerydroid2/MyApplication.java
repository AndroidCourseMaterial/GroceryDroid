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
package com.mattboutell.grocerydroid2;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mattboutell.grocerydroid2.db.DatabaseHelper;


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
