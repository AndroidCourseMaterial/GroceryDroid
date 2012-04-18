package edu.rosehulman.grocerydroid;

import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import edu.rosehulman.grocerydroid.db.ItemDataAdapter;
import edu.rosehulman.grocerydroid.db.ShoppingListDataAdapter;
import edu.rosehulman.grocerydroid.model.Item;
import edu.rosehulman.grocerydroid.model.ShoppingList;
import edu.rosehulman.grocerydroid.model.ShoppingList.Order;

import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * A base class for any activity that uses a shopping list.
 * 
 * @author Matthew Boutell. Created Apr 11, 2012.
 */
public abstract class ShoppingListActivity extends SherlockFragmentActivity {
	private ShoppingListDataAdapter mSlda;
	private ItemDataAdapter mIda;
	private ShoppingList mShoppingList;
	private ListView mListView;
	private ItemAdapter mItemAdapter;

	
	/**
	 * Creates data adapters for each table, and opens them for writing.
	 *
	 */
	protected void initializeDatabase() {
		this.mSlda = new ShoppingListDataAdapter();
		this.mSlda.open();

		this.mIda = new ItemDataAdapter();
		this.mIda.open();
	}

	/**
	 * Loads the shopping list with the given listId and all of its items from
	 * the database.
	 * 
	 * @param listId
	 */
	protected void initializeShoppingList(long listId) {
		Log.d(MyApplication.GD, "Before call getList");
		setShoppingList(mSlda.getList(listId));
		Log.d(MyApplication.GD, "After call getList");

		for (Item item : mIda.getAllItemsWithListId(listId)) {
			getShoppingList().addItem(item);
		}
		// TODO: update the display (notifyDataSetChanged)
		// mAdapter.notifyDataSetChanged();
	}

	/**
	 * Updates the prompt depending on if any items are present.
	 * 
	 * @param resId
	 */
	protected void updateMainPrompt(int resId) {
		TextView tv = (TextView) findViewById(resId);
		if (this.mShoppingList.getItems(Order.STOCK).size() > 0) {
			tv.setText(R.string.shopping_list_prompt_lists_present);
		} else {
			tv.setText(R.string.shopping_list_prompt_default);
		}
	}

	/**
	 * Returns the value of the field called 'mShoppingList'.
	 * 
	 * @return Returns the mShoppingList.
	 */
	protected ShoppingList getShoppingList() {
		return this.mShoppingList;
	}

	/**
	 * Sets the field called 'mShoppingList' to the given value.
	 * 
	 * @param mShoppingList
	 *            The mShoppingList to set.
	 */
	public void setShoppingList(ShoppingList mShoppingList) {
		this.mShoppingList = mShoppingList;
	}

	/**
	 * Returns the value of the field called 'mListView'.
	 * 
	 * @return Returns the mListView.
	 */
	public ListView getListView() {
		return this.mListView;
	}

	/**
	 * Sets the field called 'mListView' to the given value.
	 * 
	 * @param mListView
	 *            The mListView to set.
	 */
	public void setListView(ListView mListView) {
		this.mListView = mListView;
	}

	/**
	 * Returns the value of the field called 'mItemAdapter'.
	 * 
	 * @return Returns the mItemAdapter.
	 */
	public ItemAdapter getItemAdapter() {
		return this.mItemAdapter;
	}

	/**
	 * Sets the field called 'mItemAdapter' to the given value.
	 * 
	 * @param itemAdapter
	 *            The itemAdapter to set.
	 */
	public void setItemAdapter(ItemAdapter itemAdapter) {
		this.mItemAdapter = itemAdapter;
		mListView.setAdapter(mItemAdapter);
	}

	/**
	 * Launches a dialog to add the name of the given item. Details must be
	 * added via the edit item method.
	 */
	void launchItemDialog() {
		// CONSIDER: Do I even need to call the static newInstance?
		// DialogFragment newFragment = ItemDialogFragment.newInstance();
		ItemDialogFragment newFragment = new ItemDialogFragment();
		newFragment.initializeItem(mShoppingList.getId());
		newFragment.show(getSupportFragmentManager(), "add_item");
	}

	/**
	 * Add the given item to this activity's shopping list and the database,
	 * then update the view.
	 * 
	 * @param item
	 */
	void addItem(Item item) {
		Log.d(MyApplication.GD, "Starting activity addItem");

		Log.d(MyApplication.GD, "Name is " + item.getName() + " item at " + item.hashCode());
		mShoppingList.addItem(item);
		mIda.insertItem(item);
		Log.d(MyApplication.GD, "About to notify array adapter");

		mItemAdapter.notifyDataSetChanged();
		Log.d(MyApplication.GD, "Finished activity addItem");
	}

	/**
	 * Launches a dialog to edit the given item.
	 * 
	 * @param item
	 */
	public void editItem(Item item) {
		// empty
	}

	/**
	 * Deletes the given item from the list
	 * 
	 * @param item
	 */
	public void deleteItem(Item item) {
		// empty
	}
}
