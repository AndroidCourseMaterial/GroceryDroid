package edu.rosehulman.grocerydroid;

import android.content.Context;
import android.widget.ArrayAdapter;

import edu.rosehulman.grocerydroid.model.Item;

import java.util.List;

/**
 * A base class for adapters that display items on ShoppingList activities.
 * 
 * @author Matthew Boutell. Created Apr 18, 2012.
 */
public abstract class ItemAdapter extends ArrayAdapter<Item> {
	// CONSIDER: As I'm writing the one for the Shopping Activity, what can I
	// move here?

	/**
	 * Creates an ItemAdapter from the given parameters.
	 * 
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public ItemAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

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
	public ItemAdapter(Context context, int resource, int textViewResourceId,
			List<Item> objects) {
		super(context, resource, textViewResourceId, objects);
	}
}
