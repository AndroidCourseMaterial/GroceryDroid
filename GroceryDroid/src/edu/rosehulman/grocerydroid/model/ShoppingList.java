package edu.rosehulman.grocerydroid.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.rosehulman.grocerydroid.db.ItemDataAdapter;

/**
 * A shopping list is simply a list of Items.
 * 
 * @author Matthew Boutell. Created Nov 7, 2011.
 */
public class ShoppingList {
	private static final long DEFAULT_ID = 0;
	private long mId;
	private String mName;
	private ArrayList<Item> mItems;

	/**
	 * Creates a ShoppingList from the given parameters.
	 * 
	 * @param id
	 * @param name
	 * 
	 */
	public ShoppingList(long id, String name) {
		mId = id;
		mName = name;
		mItems = new ArrayList<Item>();
	}

	/**
	 * Creates a ShoppingList from the given parameters.
	 * 
	 * @param name
	 * 
	 */
	public ShoppingList(String name) {
		this.mId = DEFAULT_ID;
		this.mName = name;
		this.mItems = new ArrayList<Item>();
	}

	/**
	 * Adds the given item to the shopping list
	 * 
	 * @param item
	 */
	public void addItem(Item item) {
		this.mItems.add(item);
	}

	/**
	 * Removes the given item from the shopping list
	 * 
	 * @param item
	 * @return True iff the item was removed.
	 */
	public boolean deleteItem(Item item) {
		return this.mItems.remove(item);
	}

	/**
	 * Replaces with the given item, the list item that has the same ID.
	 * 
	 * @param item
	 * @return True iff the list contains an item with the same ID as the given
	 *         item.
	 */
	public boolean updateItem(Item item) {
		// TODO: Unit test this
		for (int i = 0; i < this.mItems.size(); i++) {
			Item currentItem = this.mItems.get(i);
			if (item.getId() == currentItem.getId()) {
				this.mItems.set(i, item);
				return true;
			}
		}
		return false;
	}

	// id, name, items
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(String.format("%d %s", this.mId, this.mName));
		for (Item item : this.mItems) {
			result.append("\n  " + item);
		}
		return result.toString();
	}

	/**
	 * Returns true if the given object is a ShoppingList that equals this
	 * ShoppingList.
	 */
	@Override
	public boolean equals(Object object) {
		if (object == null || this.getClass() != object.getClass()) {
			return false;
		}
		ShoppingList other = (ShoppingList) object;
		return this.mId == other.mId && this.mName.equals(other.mName);
	}

	/**
	 * Only includes those items which have been bought.
	 * 
	 * @return The total amount spent on the items in this list.
	 */
	public double totalSpent() {
		double total = 0;
		for (Item item : this.mItems) {
			total += item.totalSpent();
		}
		return total;
	}

	/**
	 * The total price of all the items in this list, whether or not they have
	 * been bought.
	 * 
	 * @return The total price of the items in this list.
	 */
	public double totalPrice() {
		double total = 0;
		for (Item item : this.mItems) {
			total += item.totalPrice();
		}
		return total;
	}

	/**
	 * Each item remembers its order in the pantry, when stocking. This method
	 * updates that order to match the order that the items appear on the screen
	 * (presumably after they have been rearranged by the user), so that it can
	 * be stored in the DB.
	 */
	public void setPantryOrderToListOrder() {
		for (int i = 0; i < this.mItems.size(); i++) {
			// CONSIDER: switch order backwards so that adding an item to the
			// top doesn't affect the order of every item in the list.
			this.mItems.get(i).setStockIdx(i);
		}

	}

	/**
	 * Each item remembers its order in the store, when shopping. This method
	 * updates that order to match the order that the items appear on the screen
	 * (presumably after they have been rearranged by the user), so that it can
	 * be stored in the DB.
	 */
	public void setShoppingOrderToListOrder() {
		for (int i = 0; i < this.mItems.size(); i++) {
			// CONSIDER: switch order backwards so that adding an item to the
			// top doesn't affect the order of every item in the list.
			this.mItems.get(i).setShopIdx(i);
		}
	}

	/**
	 * Returns the value of the field called 'name'.
	 * 
	 * @return Returns the name.
	 */
	public String getName() {
		return this.mName;
	}

	/**
	 * Sets the field called 'name' to the given value.
	 * 
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.mName = name;
	}

	/**
	 * Returns the value of the field called 'id'.
	 * 
	 * @return Returns the id.
	 */
	public long getId() {
		return this.mId;
	}

	/**
	 * Sets the field called 'id' to the given value.
	 * 
	 * @param id
	 *            The id to set.
	 */
	public void setId(long id) {
		this.mId = id;
	}

	/**
	 * Sets the field called 'items' to the given value.
	 * 
	 * @param items
	 *            The items to set.
	 */
	public void setItems(ArrayList<Item> items) {
		this.mItems = items;
	}

	// CONSIDER: since we access AS_IS primarily just for testing, does it
	// make sense to have an overloaded getItems() method that calls
	// getItems(Order.AS_IS)?

	/**
	 * 
	 * Lazy-loads the items if needed.
	 * 
	 * @param order
	 *            The order in which the items will be returned
	 * @param ida
	 *            The item data adapter
	 * @return The list of Items
	 */
	public ArrayList<Item> getItems(Order order) {
		if (this.mItems == null || this.mItems.size() == 0) {
			// If items isn't, then lazy-load it.
			ItemDataAdapter ida = new ItemDataAdapter();
			ida.open();
			// Shouldn't be null since init'd in constructor. Check anyway.
			if (this.mItems == null) {
				this.mItems = new ArrayList<Item>();
			}
			this.mItems.clear();
			for (Item item : ida.getAllItemsWithListId(this.mId)) {
				this.mItems.add(item);
			}
		}

		switch (order) {
		case STOCK:
			Collections.sort(this.mItems, new CompareStockOrder());
			break;
		case SHOP:
			Collections.sort(this.mItems, new CompareShopOrder());
			break;
		default:
			// empty
		}

		return this.mItems;
	}

	private class CompareStockOrder implements Comparator<Item> {
		@Override
		public int compare(Item left, Item right) {
			Integer leftIdx = new Integer(left.getStockIdx());
			Integer rightIdx = new Integer(right.getStockIdx());
			return leftIdx.compareTo(rightIdx);
		}
	}

	private class CompareShopOrder implements Comparator<Item> {
		@Override
		public int compare(Item left, Item right) {
			Integer leftIdx = new Integer(left.getShopIdx());
			Integer rightIdx = new Integer(right.getShopIdx());
			return leftIdx.compareTo(rightIdx);
		}
	}

	/**
	 * Whether the list should be displayed in the order the items appear in the
	 * pantry, the order they appear in the store, or neither.
	 * 
	 * @author Matthew Boutell. Created Mar 30, 2012.
	 */
	public enum Order {
		/** default */
		AS_IS,

		/** pantry order */
		STOCK,

		/** store order */
		SHOP
	}
}
