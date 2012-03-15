package edu.rosehulman.boutell.grocery;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A custom adapter for my stock items, which contain text and multiple buttons.
 *
 * @author Matthew Boutell.
 *         Created Nov 7, 2011.
 */
public class ShopItemAdapter extends ArrayAdapter<Item> {

	private int resourceId;
	private ShopActivity shopActivity;
	
	/**
	 * Creates a StockItemAdapter from the given parameters.
	 *
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public ShopItemAdapter(Context context, int textViewResourceId,
			List<Item> objects) {
		super(context, textViewResourceId, objects);
		
		// Save the resource so I can inflate it later
		this.resourceId = textViewResourceId;
	}
	
	/**
	 * Creates a StockItemAdapter from the given parameters.
	 *
	 * @param context
	 * @param resource
	 * @param textViewResourceId
	 * @param objects
	 */
	public ShopItemAdapter(Context context, int resource,
			int textViewResourceId, List<Item> objects) {
		super(context, resource, textViewResourceId, objects);
		this.resourceId = textViewResourceId;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RelativeLayout shopItemView;
		final Item item = this.getItem(position);

		
		// Create a view if it doesn't exist
		if (convertView == null) {
			shopItemView = new RelativeLayout(this.getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
			vi.inflate(this.resourceId, shopItemView, true);
			Log.d(StockActivity.GD, "Creating item " + item.toString());

		} else {
			shopItemView = (RelativeLayout)convertView;
			Log.d(StockActivity.GD, "Showing existing item " + item.toString());
		}
		
		final CheckBox buyItemButton = (CheckBox)shopItemView.findViewById(R.id.boughtItemButton);
		buyItemButton.setChecked(item.isBought());
		// Note: using an onCleckedChangeListener was buggy, since it appears that 
		// manually calling setChecked, like I just did, calls the listener.
		buyItemButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				item.setBought(!item.isBought());
				ShopItemAdapter.this.shopActivity.updateDisplayList();
			}
		});
				
		TextView itemView = (TextView)shopItemView.findViewById(R.id.shopItemTextView);
		itemView.setText(item.toShortString() + ": " + item.getNToBuy());
		return shopItemView;
	}

	/**
	 * Saves the given shopActivity, to be used in a later method call.
	 *
	 * @param shopActivity
	 */
	protected void setShopActivity(ShopActivity shopActivity) {
		this.shopActivity = shopActivity;
	}
}
