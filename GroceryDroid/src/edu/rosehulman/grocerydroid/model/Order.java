package edu.rosehulman.grocerydroid.model;

/**
 * Whether the list should be accessed in the order the items appear in the
 * pantry, the order they appear in the store, or neither.
 * 
 * @author boutell. Created Mar 30, 2012.
 */
public enum Order {
	/** default */
	AS_IS,
	
	/** pantry order */
	STOCK,
	
	/** store order */
	SHOP
}
