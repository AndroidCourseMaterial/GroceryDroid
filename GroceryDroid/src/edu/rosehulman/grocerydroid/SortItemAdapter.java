package edu.rosehulman.grocerydroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import edu.rosehulman.grocerydroid.model.Item;

import java.util.List;

/**
 * A custom adapter for my items.
 * 
 * @author Matthew Boutell. Created Apr 6, 2012.
 */
public class SortItemAdapter extends ItemAdapter {

	private int mResourceId;

	/**
	 * Creates a ShopItemAdapter from the given parameters.
	 * 
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public SortItemAdapter(Context context, int textViewResourceId,
			List<Item> objects) {
		super(context, textViewResourceId, objects);

		// Save the resource so I can inflate it later
		this.mResourceId = textViewResourceId;
	}

	/**
	 * Creates a ShopItemAdapter from the given parameters.
	 * 
	 * @param context
	 * @param resource
	 * @param textViewResourceId
	 * @param objects
	 */
	public SortItemAdapter(Context context, int resource,
			int textViewResourceId, List<Item> objects) {
		super(context, resource, textViewResourceId, objects);
		this.mResourceId = textViewResourceId;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RelativeLayout view;
		final Item item = this.getItem(position);

		// Create a view if it doesn't exist
		if (convertView == null) {
			view = new RelativeLayout(this.getContext());
			String inflaterService = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
					inflaterService);
			inflater.inflate(this.mResourceId, view, true);
		} else {
			view = (RelativeLayout) convertView;
		}

		TextView nameView = (TextView) view
				.findViewById(R.id.main_list_name);
		nameView.setText(item.getName());

		return view;
	}
}
