package edu.rosehulman.grocerydroid;

import edu.rosehulman.grocerydroid.model.Item;

import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * A base class for any activity that uses a shopping list.
 *
 * @author Matthew Boutell.
 *         Created Apr 11, 2012.
 */
public abstract class ShoppingListActivity extends SherlockFragmentActivity {
	
	/**
	 * Launches a dialog to add the name of the given item. Details must be 
	 * added via the edit item method.
	 * 
	 * @return The new Item.
	 */
	public Item addItem() {
		// empty
		return null;
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
