package edu.rosehulman.boutell.grocery;

import java.util.ArrayList;

/**
 * A shopping list is simply a list of Items.
 * 
 * @author Matthew Boutell. Created Nov 7, 2011.
 */
public class ShoppingList {
	private ArrayList<Item> items;

	/**
	 * Creates a ShoppingList from the given parameters.
	 * 
	 */
	public ShoppingList() {
		this.items = new ArrayList<Item>();
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
	 * Sets the field called 'items' to the given value.
	 * 
	 * @param items
	 *            The items to set.
	 */
	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}
}
