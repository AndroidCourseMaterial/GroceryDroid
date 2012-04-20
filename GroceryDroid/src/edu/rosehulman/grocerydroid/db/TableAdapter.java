package edu.rosehulman.grocerydroid.db;

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

	/** Shared db connection: static and singleton */
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
