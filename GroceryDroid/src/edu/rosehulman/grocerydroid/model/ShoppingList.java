package edu.rosehulman.grocerydroid.model;

import java.util.ArrayList;

import edu.rosehulman.grocerydroid.db.ItemDataAdapter;

/**
 * A shopping list is simply a list of Items.
 * 
 * @author Matthew Boutell. Created Nov 7, 2011.
 */
public class ShoppingList {
	private String name;
	private ArrayList<Item> items;

	/**
	 * Creates a ShoppingList from the given parameters.
	 * 
	 */
	public ShoppingList() {
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
	 * Returns the value of the field called 'items'.
	 * 
	 * @return Returns the items.
	 */
	public ArrayList<Item> getItems() {
		return this.items;
	}

	/** 
	 * 
	 * Lazy-loads the items if needed.
	 *
	 * @param ida The item data adapter
	 * @return The list of Items
	 */
	public ArrayList<Item> getItems(ItemDataAdapter ida) {
		if (this.items == null || this.items.size() == 0) { 
			// If items isn't, then lazy-load it.
			// TODO:
			
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
