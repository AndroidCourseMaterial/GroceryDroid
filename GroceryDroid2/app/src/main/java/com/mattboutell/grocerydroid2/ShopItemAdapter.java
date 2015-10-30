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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mattboutell.grocerydroid2.model.Item;

/**
 * A custom adapter for my shop items, which contain text and a checkbox.
 * 
 * @author Matthew Boutell. Created Apr 25, 2012.
 */
public class ShopItemAdapter extends ItemAdapter {

	private ShopActivity mShopActivity;
	
	/**
	 * Creates a ShopItemAdapter from the given parameters.
	 * 
	 * @param context
	 * @param shoppingListKey
	 */
	public ShopItemAdapter(Context context, String shoppingListKey) {
		super(context, shoppingListKey);

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RelativeLayout shopItemView;
        final Item item = (Item)getItem(position);

		// Create a view if it doesn't exist
		if (convertView == null) {
			shopItemView = new RelativeLayout(mContext);
			mInflater.inflate(R.layout.shop_item, shopItemView, true);
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
                ((ShopActivity)mShopActivity).refreshDisplay();
            }
        });

		TextView nameView = (TextView) shopItemView
				.findViewById(R.id.shop_item_view_name_and_to_buy);
		nameView.setText(mContext.getString(R.string.shop_item_format, item.getName(), item.getNBuy()));


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
