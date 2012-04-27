package edu.rosehulman.grocerydroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import edu.rosehulman.grocerydroid.model.ShoppingList;

import java.util.List;

/**
 * A custom adapter for my stock items, which contain text and multiple buttons.
 * 
 * @author Matthew Boutell. Created Apr 6, 2012.
 */
public class MainShoppingListAdapter extends ArrayAdapter<ShoppingList> {

	private int mResourceId;

	/**
	 * Creates a MainShoppingListAdapter from the given parameters.
	 * 
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public MainShoppingListAdapter(Context context, int textViewResourceId,
			List<ShoppingList> objects) {
		super(context, textViewResourceId, objects);

		// Save the resource so I can inflate it later
		this.mResourceId = textViewResourceId;
	}

	/**
	 * Creates a MainShoppingListAdapter from the given parameters.
	 * 
	 * @param context
	 * @param resource
	 * @param textViewResourceId
	 * @param objects
	 */
	public MainShoppingListAdapter(Context context, int resource,
			int textViewResourceId, List<ShoppingList> objects) {
		super(context, resource, textViewResourceId, objects);
		this.mResourceId = textViewResourceId;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RelativeLayout mainShoppingListView;
		final ShoppingList list = this.getItem(position);

		// Create a view if it doesn't exist
		if (convertView == null) {
			mainShoppingListView = new RelativeLayout(this.getContext());
			String inflaterService = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
					inflaterService);
			inflater.inflate(this.mResourceId, mainShoppingListView, true);
		} else {
			mainShoppingListView = (RelativeLayout) convertView;
		}

		TextView nameView = (TextView) mainShoppingListView
				.findViewById(R.id.main_list_name);
		nameView.setText(list.getName());

		return mainShoppingListView;
	}
}
