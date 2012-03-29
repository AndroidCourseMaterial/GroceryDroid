package edu.rosehulman.grocerydroid.db;

import java.util.HashMap;
import java.util.Map;

import edu.rosehulman.grocerydroid.MyApplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Provide generic functionality to all database adapters 
 *
 * @author Matthew Boutell, based on code from Kevin Wells.
 *         Created Mar 29, 2012.
 */
public abstract class TableAdapter {

	// TODO: remove the map and refs. This is overkill for me.
	// TODO: instead, make the actual db be static and singleton. 
	// Jimmy says: protected getDB() { if not there, calls open once() }
	// I'm not sure how that fits. See where the db is called.
	
	private static Map<SQLiteDatabase, Integer> dbCount;

	private SQLiteOpenHelper dbOpenHelper;
	
	static {
		dbCount = new HashMap<SQLiteDatabase, Integer>();
	}

	/** Shared db connection */
	protected SQLiteDatabase db;

	/**
	 * Creates a new TableAdapter
	 */
	public TableAdapter() {
		this.dbOpenHelper = DatabaseHelper.getInstance();
	}

	/**
	 * Creates a new TableAdapter with an existing database connection
	 * 
	 * @param db The database connection to use
	 */
	public TableAdapter(SQLiteDatabase db) {
		this.db = db;
		ref(db);
	}

	/**
	 * Open a writable version of the database
	 */
	public void open() {
		if (this.db != null) {
			deref(this.db);
			this.db = null;
		}
		this.db = this.dbOpenHelper.getWritableDatabase();
		ref(this.db);

		verifyThread();
	}

	/**
	 * Close an open database connection.
	 * 
	 * TableAdapters created with an existing connection should not close the
	 * connection to the database.
	 */
	public void close() {
		deref(this.db);
		this.db = null;
	}
	
	public void startTransaction() {
		this.db.beginTransaction();
	}
	
	public void commitTransaction() {
		this.db.setTransactionSuccessful();
	}
	
	public void finishTransaction() {
		this.db.endTransaction();
	}

	/**
	 * Convert the result of a single cell into a String
	 * 
	 * @param cursor An active cursor, with a single column and row
	 * @return the String contained in the only cell
	 */
	protected String getString(Cursor cursor) {
		assert (cursor.getColumnCount() == 1);
		assert (cursor.getCount() == 1);
		cursor.moveToFirst();
		return cursor.getString(0);
	}

	/**
	 * Retrieve a boolean value from the database
	 * 
	 * @param cursor The cursor to read from, moved to the appropriate row
	 * @param column The index of the column to read
	 * @return True if the value is 1, False if 0
	 */
	protected boolean getBoolean(Cursor cursor, int column) {
		int val = cursor.getInt(column);
		assert (val == 0 || val == 1);
		return val == 1;
	}

	/**
	 * Convert an array of strings to a list of columns
	 * 
	 * @param args
	 *            A list of column names
	 * @return A string separating the column names with commas
	 */
	protected String columns(String... args) {
		StringBuilder builder = new StringBuilder(" ");
		for (String arg : args) {
			builder.append(arg);
			builder.append(",");
		}
		builder.setCharAt(builder.length() - 1, ' ');
		return builder.toString();
	}

	/**
	 * Format a table/column pair
	 * 
	 * @param table
	 *            The table to use
	 * @param column
	 *            The column to use
	 * @return table.column
	 */
	protected String column(String table, String column) {
		return table + "." + column;
	}
	
	/**
	 * Format a table/column pair
	 * 
	 * @param column
	 *            The column to use
	 * @param alias
	 *            The alias of the column
	 * @return table.column AS alias
	 */
	protected String columnAlias(String column, String alias) {
		return column + " AS " + alias;
	}

	/**
	 * Format a table name/alias pair
	 * 
	 * @param name
	 *            The table name
	 * @param alias
	 *            The alias of the table
	 * 
	 * @return name alias
	 */
	protected String table(String name, String alias) {
		return name + " " + alias;
	}

	private static void verifyThread() {
		if (!MyApplication.CHECK_UI_THREAD)
			return;

		//make sure we are not the main thread
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		for (StackTraceElement frame : stack) {
			if ("main".equals(frame.getMethodName()) &&
				frame.getClassName().contains("NativeStart") &&
				frame.isNativeMethod()) {
				throw new RuntimeException("DB access on UI thread");
			}
		}
	}
	
	private static void ref(SQLiteDatabase db) {
		synchronized (dbCount) {
			int count = 0;
			
			if (dbCount.containsKey(db)) {
				count = dbCount.get(db);
			}
			
			dbCount.put(db, count + 1);
		}
	}
	
	private static void deref(SQLiteDatabase db) {
		synchronized (dbCount) {
			int count = dbCount.get(db);
			
			if (count == 1) {
				dbCount.remove(db);
				db.close();
			} else {
				dbCount.put(db, count - 1);
			}
		}
	}

}
