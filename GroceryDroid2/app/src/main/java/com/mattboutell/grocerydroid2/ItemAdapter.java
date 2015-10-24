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
import android.widget.ArrayAdapter;

import com.mattboutell.grocerydroid2.model.Item;

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
