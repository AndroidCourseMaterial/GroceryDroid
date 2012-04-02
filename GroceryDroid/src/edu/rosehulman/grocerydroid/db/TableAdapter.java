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


	private SQLiteOpenHelper dbOpenHelper;

	/** Shared db connection: static and singleton */
	protected static SQLiteDatabase db;

	/**
	 * Retrieves the instance of SQLiteDatabase
	 * 
	 * @return The single instance of SQLiteDatabase
	 */
	protected SQLiteDatabase getDb() {
		// CONSIDER: remove this? It's not used currently.
		if (db == null) {
			open();
		}
		return db;
	}

	/**
	 * Creates a new TableAdapter
	 */
	public TableAdapter() {
		this.dbOpenHelper = DatabaseHelper.getInstance();
	}

	/**
	 * Creates a new TableAdapter with an existing database connection
	 * 
	 * @param db
	 *            The database connection to use
	 */
	public TableAdapter(SQLiteDatabase db) {
		TableAdapter.db = db;
	}

	/**
	 * Open a writable version of the database
	 */
	public void open() {
		if (TableAdapter.db != null) {
			TableAdapter.db = null;
		}
		TableAdapter.db = this.dbOpenHelper.getWritableDatabase();
	}

	/**
	 * Close an open database connection.
	 * 
	 * TableAdapters created with an existing connection should not close the
	 * connection to the database.
	 */
	public void close() {
		db = null;
	}

	/**
	 * Adapter for database transactions.
	 */
	public void startTransaction() {
		db.beginTransaction();
	}

	/**
	 * Adapter for database transactions.
	 */
	public void commitTransaction() {
		db.setTransactionSuccessful();
	}

	/**
	 * Adapter for database transactions.
	 */
	public void finishTransaction() {
		db.endTransaction();
	}
}
