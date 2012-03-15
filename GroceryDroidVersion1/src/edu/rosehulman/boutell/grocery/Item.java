package edu.rosehulman.boutell.grocery;

import android.content.Intent;
import android.util.Log;

/**
 * A single grocery item.
 * 
 * @author Matthew Boutell. Created Nov 7, 2011.
 */
public class Item {

	/** ID */
	public static final String KEY_ID = "id";

	/** Name */
	public static final String KEY_NAME = "name";

	/** Number to stock */
	public static final String KEY_NUM_TO_STOCK = "numberToStock";

	/** Number to buy */
	public static final String KEY_NUM_TO_BUY = "numberToBuy";

	/** Price */
	public static final String KEY_PRICE = "price";

	/** Size */
	public static final String KEY_SIZE = "size";

	/** Unit of measure */
	public static final String KEY_UNIT = "unit";

	/** Whether it has been bought yet */
	public static final String KEY_IS_BOUGHT = "isBought";

	private long id;
	private String name;
	private int nToStock;
	private int nToBuy;
	private float price;
	private float size;
	private ItemUnit unit;
	private boolean isBought;

	/**
	 * Creates a Item from the given parameters.
	 * @param id 
	 * @param name
	 * @param nToStock
	 * @param nToBuy
	 * @param price
	 * @param size
	 * @param unit
	 * @param isBought
	 */
	public Item(long id, String name, int nToStock, int nToBuy, float price,
			float size, ItemUnit unit, boolean isBought) {
		this.id = id;
		this.name = name;
		this.nToStock = nToStock;
		this.nToBuy = nToBuy;
		this.price = price;
		this.size = size;
		this.unit = unit;
		this.isBought = isBought;
	}

	/**
	 * Creates a Item from the given parameters.
	 * 
	 * @param id 
	 * @param name
	 * @param nToStock
	 * @param price
	 * @param size
	 * @param unit
	 */
	public Item(long id, String name, int nToStock, float price, float size,
			ItemUnit unit) {
		this(id, name, nToStock, 0, price, size, unit, false);
	}

	/**
	 * Creates a Item from the given parameters.
	 * 
	 * @param data
	 */
	public Item(Intent data) {
		this.id = data.getLongExtra(KEY_ID, 0); // probably uses the default.
		if (this.id == 0) {
			Log.d(StockActivity.GD, "Creating item with id = 0");
		}
		this.name = data.getStringExtra(KEY_NAME);
		this.nToStock = data.getIntExtra(KEY_NUM_TO_STOCK, 0);
		this.nToBuy = 0;
		this.price = data.getFloatExtra(KEY_PRICE, 0.0f);
		this.size = data.getFloatExtra(KEY_SIZE, 0.0f);
		this.unit = ItemUnit.values()[data.getIntExtra(KEY_UNIT, 0)];
		this.isBought = false;
	}

	/**
	 * Adds this object's data to the intent.
	 * 
	 * @param intent
	 */
	protected void addToIntent(Intent intent) {
		intent.putExtra(KEY_ID, this.id);
		intent.putExtra(KEY_NAME, this.name);
		intent.putExtra(KEY_NUM_TO_STOCK, this.nToStock);
		intent.putExtra(KEY_NUM_TO_BUY, this.nToBuy);
		intent.putExtra(KEY_PRICE, this.price);
		intent.putExtra(KEY_SIZE, this.size);
		intent.putExtra(KEY_UNIT, this.unit.ordinal());
		intent.putExtra(KEY_IS_BOUGHT, this.isBought);
	}

	@Override
	public String toString() {
		// TODO: have String format be locale dependent
		// USA: String s = String.format("%s (%d) $%.2f/%.1f %s", this.name,
		// this.nToStock, this.price, this.size, this.unit.toString());
		String s = String.format("%d %s (%d) %.0fK/%.1f %s %s", this.id,
				this.name, this.nToStock, this.price, this.size, this.unit, this.isBought ? "B" : "N"
						.toString());
		return s;
	}

	
	/**
	 * @return A short string for this item: name and unit
	 */
	public String toShortString() {
		return String.format("%s, %.1f %s", this.name, this.size, this.unit);
	}
	
	/**
	 * Adds one to the number to be bought.
	 * 
	 */
	protected void incrementNToBuy() {
		this.nToBuy++;
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
	 * Returns the value of the field called 'name'.
	 * 
	 * @return Returns the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the value of the field called 'nToStock'.
	 * 
	 * @return Returns the nToStock.
	 */
	public int getNToStock() {
		return this.nToStock;
	}

	/**
	 * Returns the value of the field called 'nToBuy'.
	 * 
	 * @return Returns the nToBuy.
	 */
	public int getNToBuy() {
		return this.nToBuy;
	}

	/**
	 * Returns the value of the field called 'price'.
	 * 
	 * @return Returns the price.
	 */
	public float getPrice() {
		return this.price;
	}

	/**
	 * Returns the value of the field called 'size'.
	 * 
	 * @return Returns the size.
	 */
	public float getSize() {
		return this.size;
	}

	/**
	 * Returns the value of the field called 'unit'.
	 * 
	 * @return Returns the unit.
	 */
	public ItemUnit getUnit() {
		return this.unit;
	}

	/**
	 * Returns the value of the field called 'isBought'.
	 * 
	 * @return Returns the isBought.
	 */
	public boolean isBought() {
		return this.isBought;
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
	 * Sets the field called 'name' to the given value.
	 * 
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the field called 'nToStock' to the given value.
	 * 
	 * @param nToStock
	 *            The nToStock to set.
	 */
	public void setNToStock(int nToStock) {
		this.nToStock = nToStock;
	}

	/**
	 * Sets the field called 'nToBuy' to the given value.
	 * 
	 * @param nToBuy
	 *            The nToBuy to set.
	 */
	public void setNToBuy(int nToBuy) {
		this.nToBuy = nToBuy;
	}

	/**
	 * Sets the field called 'price' to the given value.
	 * 
	 * @param price
	 *            The price to set.
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	/**
	 * Sets the field called 'size' to the given value.
	 * 
	 * @param size
	 *            The size to set.
	 */
	public void setSize(float size) {
		this.size = size;
	}

	/**
	 * Sets the field called 'unit' to the given value.
	 * 
	 * @param unit
	 *            The unit to set.
	 */
	public void setUnit(ItemUnit unit) {
		this.unit = unit;
	}

	/**
	 * Sets the field called 'isBought' to the given value.
	 * 
	 * @param isBought
	 *            The isBought to set.
	 */
	public void setBought(boolean isBought) {
		this.isBought = isBought;
	}

}
