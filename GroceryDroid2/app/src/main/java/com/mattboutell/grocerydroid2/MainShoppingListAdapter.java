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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mattboutell.grocerydroid2.model.ShoppingList;

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
