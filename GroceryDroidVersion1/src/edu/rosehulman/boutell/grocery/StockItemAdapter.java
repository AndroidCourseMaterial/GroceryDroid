package edu.rosehulman.boutell.grocery;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A custom adapter for my stock items, which contain text and multiple buttons.
 * 
 * @author Matthew Boutell. Created Nov 7, 2011.
 */
public class StockItemAdapter extends ArrayAdapter<Item> {

	private int resourceId;
	private StockActivity stockActivity;

	/**
	 * Creates a StockItemAdapter from the given parameters.
	 * 
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public StockItemAdapter(Context context, int textViewResourceId,
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
	public StockItemAdapter(Context context, int resource,
			int textViewResourceId, List<Item> objects) {
		super(context, resource, textViewResourceId, objects);
		this.resourceId = textViewResourceId;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RelativeLayout stockItemView;
		final Item item = this.getItem(position);

		// Create a view if it doesn't exist
		if (convertView == null) {
			stockItemView = new RelativeLayout(this.getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					inflater);
			vi.inflate(this.resourceId, stockItemView, true);
		} else {
			stockItemView = (RelativeLayout) convertView;
		}

		final Button addStockButton = (Button) stockItemView
				.findViewById(R.id.addStockButton);
		addStockButton.setText("Buy " + item.getNToBuy());
		addStockButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(StockActivity.GD, "" + v.getId() + " " + position);
				item.incrementNToBuy();
				addStockButton.setText("Buy " + item.getNToBuy());
			}
		});

		final Button resetButton = (Button) stockItemView
				.findViewById(R.id.resetStockButton);
		resetButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(StockActivity.GD, "" + v.getId() + " " + position);
				item.setNToBuy(0);
				addStockButton.setText("Buy " + item.getNToBuy());
			}
		});

		TextView itemView = (TextView) stockItemView
				.findViewById(R.id.stockItemTextView);
		itemView.setText(item.toString());

		itemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StockItemAdapter.this.stockActivity.editItem(item);
			}
		});

		itemView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				StockItemAdapter.this.stockActivity.deleteItem(item);
				return true; // consume the long click
			}
		});

		return stockItemView;
	}

	/**
	 * Saves the given stockActivity, to be used in a later method call.
	 * 
	 * @param stockActivity
	 */
	protected void setStockActivity(StockActivity stockActivity) {
		this.stockActivity = stockActivity;
	}
}
