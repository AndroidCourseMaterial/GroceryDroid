package edu.rosehulman.grocerydroid;

import android.content.Context;
import android.widget.ArrayAdapter;

import edu.rosehulman.grocerydroid.model.Item;

import java.util.List;

/**
 * TODO Put here a description of what this class does.
 *
 * @author boutell.
 *         Created Apr 18, 2012.
 */
public abstract class ItemAdapter extends ArrayAdapter<Item> {
	/**
	 * Creates an ItemAdapter from the given parameters.
	 * 
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public ItemAdapter(Context context, int textViewResourceId,
			List<Item> objects) {
		super(context, textViewResourceId, objects);
	}

	/**
	 * Creates an ItemAdapter from the given parameters.
	 * 
	 * @param context
	 * @param resource
	 * @param textViewResourceId
	 * @param objects
	 */
	public ItemAdapter(Context context, int resource,
			int textViewResourceId, List<Item> objects) {
		super(context, resource, textViewResourceId, objects);
	}
}
