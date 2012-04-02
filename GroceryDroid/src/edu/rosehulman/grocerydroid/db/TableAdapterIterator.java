package edu.rosehulman.grocerydroid.db;

import android.database.Cursor;

import java.util.Iterator;

/**
 * A generic TableIterator of type T.
 * 
 * @author Jimmy Theis. Created March 30, 2012.
 * @param <T>
 */
public abstract class TableAdapterIterator<T> implements Iterator<T>,
		Iterable<T> {
	/**
	 * This table's cursor.
	 */
	protected Cursor mCursor;

	/**
	 * Create a TableAdapterInteractor of the given type.
	 * 
	 * @param cursor
	 */
	public TableAdapterIterator(Cursor cursor) {
		mCursor = cursor;
		mCursor.moveToFirst();
	}

	/**
	 * Gets the cursor.
	 * 
	 * @return This iterator's cursor
	 */
	protected Cursor getCursor() {
		return mCursor;
	}

	/**
	 * Returns one object from the Cursor's next row.
	 * 
	 * @return One object from the next row of the Cursor
	 */
	protected abstract T getObjectFromNextRow();

	/**
	 * @return The cursor's count
	 */
	public int getCount() {
		return mCursor.getCount();
	}

	@Override
	public boolean hasNext() {
		/* To make this an Iterator */
		// Jimmy: I had to remove the -1 here.
		return mCursor.getPosition() < mCursor.getCount(); // - 1;
	}

	@Override
	public T next() {
		/* To make this an Iterator */
		// Jimmy: I had to swap the order of the statements. moveToFirst was
		// already pointing to the first object, so I need to get it first, then
		// move to the next one.
		T data = getObjectFromNextRow();
		mCursor.moveToNext();
		return data;
	}

	@Override
	public void remove() {
		/* To make this an Iterator */
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<T> iterator() {
		/* To make this an Iterable */
		return this;
	}
}