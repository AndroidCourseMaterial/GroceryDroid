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
import android.widget.BaseAdapter;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.mattboutell.grocerydroid2.model.Item;

import java.util.ArrayList;

/**
 * A base class for adapters that display items on ShoppingList activities.
 * 
 * @author Matthew Boutell. Created Apr 18, 2012.
 */
public abstract class ItemAdapter extends BaseAdapter {
	// CONSIDER: As I'm writing the one for the Shopping Activity, what can I
	// move here?

    protected final Context mContext;
	protected final LayoutInflater mInflater;
    protected String mShoppingListKey;
    protected Firebase mItemsRef;
    protected  ArrayList<Item> mItems = new ArrayList<>();



    public ItemAdapter(Context context, String shoppingListKey) {

        Log.d(Constants.TAG, "");
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mShoppingListKey = shoppingListKey;
        mItemsRef = new Firebase(context.getString(R.string.firebase_url_format, "items/"));

        // Do Firebase query in child classes, since they order items differently.
        Query itemsForShoppingListRef = mItemsRef.orderByChild(Item.SHOPPING_LIST_KEY).equalTo(shoppingListKey);


        //Query assignmentsForCourseRef = mAssignmentsRef.orderByChild(Assignment.COURSE_KEY).equalTo(courseKey);
        //assignmentsForCourseRef.addChildEventListener(new AssignmentsChildEventListener());


    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return mItems.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // getView is in children.
}
