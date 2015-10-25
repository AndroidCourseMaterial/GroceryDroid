package com.mattboutell.grocerydroid2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.mattboutell.grocerydroid2.model.ShoppingList;

import java.util.ArrayList;

/**
 * Created by boutell on 10/24/2015.
 */
public class MainShoppingListAdapterFB extends BaseAdapter {
    private final Context mContext;
    private final LayoutInflater mInflater;
    private ArrayList<ShoppingList> mShoppingLists = new ArrayList<>();

    public MainShoppingListAdapterFB(Context context) {
        Log.d(Constants.TAG, "ShoppingListAdapter");

        mContext = context;
        mInflater = LayoutInflater.from(context);

        String mUid = context.getSharedPreferences(Constants.PREFS, 0).getString(Constants.UID_KEY, "");
        assert (!mUid.isEmpty());

        // ADD OWNER LISTENER
        Firebase mOwnerRef = new Firebase(context.getString(R.string.firebase_url_format, "owners/" + mUid));
        mOwnerRef.addListenerForSingleValueEvent(new OwnerValueEventListener());

        Firebase mListsRef = new Firebase(context.getString(R.string.firebase_url_format, "lists/"));

        Query assignmentsForCourseRef = mListsRef.orderByChild(ShoppingList.OWNER_UID).equalTo(mUid);
        assignmentsForCourseRef.addChildEventListener(new ShoppingListsChildEventListener());
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return mShoppingLists.size();
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
        return mShoppingLists.get(position);
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

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout mainShoppingListView;
        final ShoppingList list = (ShoppingList) this.getItem(position);

        // Create a view if it doesn't exist
        if (convertView == null) {
            mainShoppingListView = new RelativeLayout(mContext);
            mInflater.inflate(R.layout.main_touch_list_row, mainShoppingListView, true);
        } else {
            mainShoppingListView = (RelativeLayout) convertView;
        }

        TextView nameView = (TextView) mainShoppingListView
                .findViewById(R.id.main_list_name);
        nameView.setText(list.getName());

        return mainShoppingListView;
    }

    private class ShoppingListsChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mShoppingLists.add(new ShoppingList(dataSnapshot));
            // TODO: Sort by displayIdx? Collections.sort(mAssignments);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            for (ShoppingList list : mShoppingLists) {
                if (list.getKey().equals(key)) {
                    list.setValues(dataSnapshot);
                    break;
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            for (ShoppingList list : mShoppingLists) {
                if (list.getKey().equals(key)) {
                    mShoppingLists.remove(list);
                    break;
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            // empty
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            Log.e("TAG", "Error: " + firebaseError.getMessage());
        }
    }

    class OwnerValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            String email = (String) dataSnapshot.child("email").getValue();
            String name = (String) dataSnapshot.child("name").getValue();
            Log.d(Constants.TAG, "Email: " + email + ", name = " + name);
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            Log.d(Constants.TAG, "OwnerValueListener cancelled: " + firebaseError);
        }
    }
}
