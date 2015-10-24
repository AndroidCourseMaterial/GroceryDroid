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
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mattboutell.grocerydroid2.model.Item;

import java.util.List;

/**
 * A custom adapter for my shop items, which contain text and a checkbox.
 * 
 * @author Matthew Boutell. Created Apr 25, 2012.
 */
public class ShopItemAdapter extends ItemAdapter {

	private int mResourceId;
	private ShopActivity mShopActivity;
	
	/**
	 * Creates a ShopItemAdapter from the given parameters.
	 * 
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public ShopItemAdapter(Context context, int textViewResourceId,
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
	public ShopItemAdapter(Context context, int resource,
			int textViewResourceId, List<Item> objects) {
		super(context, resource, textViewResourceId, objects);
		this.mResourceId = textViewResourceId;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RelativeLayout shopItemView;
		final Item item = this.getItem(position);

		// Create a view if it doesn't exist
		if (convertView == null) {
			shopItemView = new RelativeLayout(this.getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					inflater);
			vi.inflate(this.mResourceId, shopItemView, true);
		} else {
			shopItemView = (RelativeLayout) convertView;
		}

		final CheckBox buyItemButton = (CheckBox)shopItemView.findViewById(R.id.shop_item_view_buy_button);
		buyItemButton.setChecked(item.isBought());
		// Note: using an onCheckedChangeListener was buggy, since it appears that 
		// manually calling setChecked, like I just did, calls the listener.
		buyItemButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				item.setBought(!item.isBought());
				Log.d(MyApplication.GD, "Clicked on item " + item.toString());
				mShopActivity.refreshDisplay();
			}
		});

		TextView nameView = (TextView) shopItemView
				.findViewById(R.id.shop_item_view_name_and_to_buy);
		nameView.setText(item.getName() + "- buy " + item.getNBuy());

		TextView infoView = (TextView) shopItemView
				.findViewById(R.id.shop_item_view_info);
		infoView.setText(item.getShopInfo());

		shopItemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mShopActivity.launchEditItemDialog(item);
			}
		});

		return shopItemView;
	}

	/**
	 * Saves the given shopActivity, to be used in a later method call.
	 * 
	 * @param shopActivity
	 */
	protected void setShopActivity(ShopActivity shopActivity) {
		this.mShopActivity = shopActivity;
	}
}
