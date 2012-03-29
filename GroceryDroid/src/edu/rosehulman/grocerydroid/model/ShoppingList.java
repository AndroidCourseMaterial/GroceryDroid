package edu.rosehulman.grocerydroid.model;

import java.util.ArrayList;

import edu.rosehulman.grocerydroid.db.ItemDataAdapter;

/**
 * A shopping list is simply a list of Items.
 * 
 * @author Matthew Boutell. Created Nov 7, 2011.
 */
public class ShoppingList {
	private long id;
	private String name;
	private ArrayList<Item> items;

	/**
	 * Creates a ShoppingList from the given parameters.
	 * 
	 * @param id
	 * @param name
	 * 
	 */
	public ShoppingList(long id, String name) {
		this.id = id;
		this.name = name;
		this.items = new ArrayList<Item>();
	}

	/**
	 * Adds the given item to the shopping list
	 * 
	 * @param item
	 */
	public void addItem(Item item) {
		this.items.add(item);
	}

	/**
	 * Removes the given item from the shopping list
	 * 
	 * @param item
	 * @return True iff the item was removed.
	 */
	public boolean deleteItem(Item item) {
		return this.items.remove(item);
	}

	// id, name, items
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(String.format("%d %s", this.id, this.name));
		for (Item item : this.items) {
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
		return this.id == other.id && this.name.equals(other.name);
	}

	/**
	 * Only includes those items which have been bought.
	 * 
	 * @return The total amount spent on the items in this list.
	 */
	public double totalSpent() {
		double total = 0;
		for (Item item : this.items) {
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
		for (Item item : this.items) {
			total += item.totalPrice();
		}
		return total;
	}

	/**
	 * Returns the value of the field called 'name'.
	 * 
	 * @return Returns the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the field called 'name' to the given value.
	 * 
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the value of the field called 'id'.
	 * 
	 * @return Returns the id.
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * Sets the field called 'id' to the given value.
	 * 
	 * @param id
	 *            The id to set.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 
	 * Lazy-loads the items if needed.
	 * 
	 * @param ida
	 *            The item data adapter
	 * @return The list of Items
	 */
	public ArrayList<Item> getItems() {
		if (this.items == null || this.items.size() == 0) {
			// If items isn't, then lazy-load it.
			ItemDataAdapter ida = new ItemDataAdapter();
			// Shouldn't be null since init'd in constructor. Check anyway.
			if (this.items == null) {
				this.items = new ArrayList<Item>();
			}
			ida.loadAllItemsWithListId(this.items, this.id);
		}
		return this.items;
	}

	/**
	 * Sets the field called 'items' to the given value.
	 * 
	 * @param items
	 *            The items to set.
	 */
	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}
}
