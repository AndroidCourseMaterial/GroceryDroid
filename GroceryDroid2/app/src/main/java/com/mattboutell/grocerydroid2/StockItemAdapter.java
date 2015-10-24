/*
 * Copyright (C) 2012 Matthew Boutell 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.mattboutell.grocerydroid2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mattboutell.grocerydroid2.model.Item;

import java.util.List;

/**
 * A custom adapter for my stock items, which contain text and multiple buttons.
 * 
 * @author Matthew Boutell. Created Nov 7, 2011.
 */
public class StockItemAdapter extends ItemAdapter {

	private int mResourceId;
	private StockActivity mStockActivity;

	/**
	 * Creates a StockItemAdapter from the given parameters.
	 * 
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public StockItemAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);

		// Save the resource so I can inflate it later
		this.mResourceId = textViewResourceId;
	}


	
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
		this.mResourceId = textViewResourceId;
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
		this.mResourceId = textViewResourceId;
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
			vi.inflate(this.mResourceId, stockItemView, true);
		} else {
			stockItemView = (RelativeLayout) convertView;
		}

		final Button addStockButton = (Button) stockItemView
				.findViewById(R.id.add_stock_button);
		addStockButton.setText("Buy " + item.getNBuy());
		addStockButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(MyApplication.GD, "" + v.getId() + " " + position);
				item.incrementNumberToBuy();
				addStockButton.setText("Buy " + item.getNBuy());
				mStockActivity.updateItem(item);
			}
		});

		final Button resetButton = (Button) stockItemView
				.findViewById(R.id.reset_stock_button);
		resetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(MyApplication.GD, "" + v.getId() + " " + position);
				item.resetNumberToBuy();
				addStockButton.setText("Buy " + item.getNBuy());
				mStockActivity.updateItem(item);
			}
		});

		TextView nameView = (TextView) stockItemView
				.findViewById(R.id.stock_item_name);
		nameView.setText(item.getName());

		TextView infoView = (TextView) stockItemView
				.findViewById(R.id.stock_item_info);
		infoView.setText(item.getStockInfo());

		stockItemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mStockActivity.launchEditItemDialog(item);
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
		this.mStockActivity = stockActivity;
	}
}
