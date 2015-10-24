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

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Provide generic functionality to all database adapters
 * 
 * @author Matthew Boutell, based on code from Kevin Wells. Created Mar 29,
 *         2012.
 */
public abstract class TableAdapter {


	private SQLiteOpenHelper mDbOpenHelper;

	/** Shared com.mattboutell.grocerydroid2.db connection: static and singleton */
	protected static SQLiteDatabase sDb;

	/**
	 * Retrieves the instance of SQLiteDatabase
	 * 
	 * @return The single instance of SQLiteDatabase
	 */
	protected SQLiteDatabase getDb() {
		// CONSIDER: remove this? It's not used currently.
		if (sDb == null) {
			open();
		}
		return sDb;
	}

	/**
	 * Creates a new TableAdapter
	 */
	public TableAdapter() {
		mDbOpenHelper = DatabaseHelper.getInstance();
	}

	/**
	 * Creates a new TableAdapter with an existing database connection
	 * 
	 * @param db
	 *            The database connection to use
	 */
	public TableAdapter(SQLiteDatabase db) {
		TableAdapter.sDb = db;
	}

	/**
	 * Open a writable version of the database
	 */
	public void open() {
		if (TableAdapter.sDb != null) {
			TableAdapter.sDb = null;
		}
		TableAdapter.sDb = mDbOpenHelper.getWritableDatabase();
	}

	/**
	 * Close an open database connection.
	 * 
	 * TableAdapters created with an existing connection should not close the
	 * connection to the database.
	 */
	public void close() {
		sDb = null;
	}

	/**
	 * Adapter for database transactions.
	 */
	public void startTransaction() {
		sDb.beginTransaction();
	}

	/**
	 * Adapter for database transactions.
	 */
	public void commitTransaction() {
		sDb.setTransactionSuccessful();
	}

	/**
	 * Adapter for database transactions.
	 */
	public void finishTransaction() {
		sDb.endTransaction();
	}
}
