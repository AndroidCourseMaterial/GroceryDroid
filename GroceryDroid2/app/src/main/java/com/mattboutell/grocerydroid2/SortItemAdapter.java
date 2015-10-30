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
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mattboutell.grocerydroid2.model.Item;

/**
 * A custom adapter for my items.
 * 
 * @author Matthew Boutell. Created Apr 6, 2012.
 */
public class SortItemAdapter extends ItemAdapter {

	/**
	 * Creates a SortItemAdapter from the given parameters.
	 *
	 * @param context
	 * @param shoppingListKey
	 */
	public SortItemAdapter(Context context, String shoppingListKey) {
		super(context, shoppingListKey);

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RelativeLayout view;
		final Item item = (Item)getItem(position);

		// Create a view if it doesn't exist
		if (convertView == null) {
			view = new RelativeLayout(mContext);
			mInflater.inflate(R.layout.main_touch_list_row, view, true);
		} else {
			view = (RelativeLayout) convertView;
		}

		TextView nameView = (TextView) view.findViewById(R.id.main_list_name);
		nameView.setText(item.getName());
		return view;
	}
}
