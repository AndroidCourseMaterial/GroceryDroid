package edu.rosehulman.grocerydroid.model;

/**
 * Whether the list should be displayed in the order the items appear in the
 * pantry, the order they appear in the store, or neither.
 * 
 * @author boutell. Created Mar 30, 2012.
 */
public enum DisplayOrder {
	/** default */
	AS_IS,
	
	/** pantry order */
	STOCK,
	
	/** store order */
	SHOP
}
