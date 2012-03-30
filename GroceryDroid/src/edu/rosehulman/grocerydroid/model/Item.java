package edu.rosehulman.grocerydroid.model;

/**
 * A single grocery item.
 * 
 * @author Matthew Boutell. Created Nov 7, 2011.
 */
public class Item {

	/** For debugging */
	public static final String GD = "GD";
	private static final float EPSILON = 0.000001f;
	private long id;
	private long listId; // the list to which it belongs
	private String name;
	private int nStock;
	private int nBuy;
	private float price;
	// CONSIDER: should I have a simple Unit class that encapsulates unit size
	// and label?
	private float unitSize;
	private ItemUnitLabel unitLabel;
	private boolean isBought;
	private int stockIdx;
	private int shopIdx;

	// id, listId, name, nStock, nBuy, price, unitSize, unitLabel, isBought,
	// stockIdx,
	// shopIdx

	/**
	 * Creates a Item from the given parameters.
	 * 
	 * @param id
	 * @param listId
	 * @param name
	 * @param nStock
	 * @param nBuy
	 * @param price
	 * @param size
	 * @param unit
	 * @param isBought
	 * @param stockIdx
	 * @param shopIdx
	 */
	public Item(long id, long listId, String name, int nStock, int nBuy,
			float price, float size, ItemUnitLabel unit, boolean isBought,
			int stockIdx, int shopIdx) {
		this.id = id;
		this.listId = listId;
		this.name = name;
		this.nStock = nStock;
		this.nBuy = nBuy;
		this.price = price;
		this.unitSize = size;
		this.unitLabel = unit;
		this.isBought = isBought;
		this.stockIdx = stockIdx;
		this.shopIdx = shopIdx;
	}

	// /**
	// * Creates a Item from the given parameters.
	// *
	// * @param id
	// * @param listId
	// * @param name
	// * @param nStock
	// * @param price
	// * @param size
	// * @param unit
	// */
	// public Item(long id, long listId, String name, int nStock, float price,
	// float size,
	// ItemUnitLabel unit) {
	// this(id, listId, name, nStock, 0, price, size, unit, false, -1, -1);
	// }

	// CONSIDER: may not need next 2 methods any more. Update if I do.
	// /**
	// * Creates a Item from the given parameters.
	// *
	// * @param data
	// */
	// public Item(Intent data) {
	// this.id = data.getLongExtra(KEY_ID, 0); // probably uses the default.
	// if (this.id == 0) {
	// Log.d(Item.GD, "Creating item with id = 0");
	// }
	// this.name = data.getStringExtra(KEY_NAME);
	// this.nStock = data.getIntExtra(KEY_NUM_TO_STOCK, 0);
	// this.nBuy = 0;
	// this.price = data.getFloatExtra(KEY_PRICE, 0.0f);
	// this.unitSize = data.getFloatExtra(KEY_SIZE, 0.0f);
	// this.unitLabel = ItemUnit.values()[data.getIntExtra(KEY_UNIT, 0)];
	// this.isBought = false;
	// }

	// /**
	// * Adds this object's data to the intent.
	// *
	// * @param intent
	// */
	// protected void addToIntent(Intent intent) {
	// intent.putExtra(KEY_ID, this.id);
	// intent.putExtra(KEY_NAME, this.name);
	// intent.putExtra(KEY_NUM_TO_STOCK, this.nStock);
	// intent.putExtra(KEY_NUM_TO_BUY, this.nBuy);
	// intent.putExtra(KEY_PRICE, this.price);
	// intent.putExtra(KEY_SIZE, this.unitSize);
	// intent.putExtra(KEY_UNIT, this.unitLabel.ordinal());
	// intent.putExtra(KEY_IS_BOUGHT, this.isBought);
	// }

	@Override
	public String toString() {
		// CONSIDER: have String format be locale dependent
		// USA: String s = String.format("%s (%d) $%.2f/%.1f %s", this.name,
		// this.nToStock, this.price, this.size, this.unit.toString());
		String s = String.format("%d %d %s (%d/%d) %.0fK/%.1f %s %s %d %d",
				this.id, this.listId, this.name, this.nBuy, this.nStock,
				this.price, this.unitSize, this.unitLabel, this.isBought ? "B"
						: "N", this.stockIdx, this.shopIdx);
		return s;
	}

	/**
	 * @return A short string for this item: name and unit
	 */
	public String toShortString() {
		return String.format("%s, %.1f %s", this.name, this.unitSize,
				this.unitLabel);
	}

	/**
	 * Returns true if the given item is an Item that equals this Item.
	 */
	@Override
	public boolean equals(Object object) {
		if (object == null || this.getClass() != object.getClass()) {
			return false;
		}
		Item other = (Item) object;
		return this.id == other.id 
			&& this.listId == other.listId
			&& this.name.equals(other.name) && this.nBuy == other.nBuy
			&& this.nStock == other.nStock
			&& Math.abs(this.price - other.price) < EPSILON
			&& Math.abs(this.unitSize - other.unitSize) < EPSILON
			&& this.unitLabel == other.unitLabel
			&& this.isBought == other.isBought
			&& this.stockIdx == other.stockIdx
			&& this.shopIdx == other.shopIdx;
	}

	// id, listId, name, nStock, nBuy, price, unitSize, unitLabel, isBought,
	// stockIdx,
	// shopIdx

	/**
	 * Adds one to the number to be bought.
	 * 
	 */
	public void incrementNumberToBuy() {
		this.nBuy++;
	}

	/**
	 * Sets the number to buy back to 0.
	 */
	public void resetNumberToBuy() {
		this.nBuy = 0;
	}

	// id, name, nStock, nBuy, cost, unitSize, unitLabel, isBought,
	// stockIdx, shopIdx

	/**
	 * @return The total amount spent on this item: 0 if not yet bought.
	 */
	public double totalSpent() {
		return this.isBought ? totalPrice() : 0;
	}

	/**
	 * @return The total price of this item (price * number to buy) if it were
	 *         bought.
	 */
	public double totalPrice() {
		return this.nBuy * this.price;
	}

	// CONSIDER: removing the setters/getters.

	/**
	 * Returns the value of the field called 'price'.
	 * 
	 * @return Returns the price.
	 */
	public float getPrice() {
		return this.price;
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
	 * Returns the value of the field called 'listId'.
	 * 
	 * @return Returns the listId.
	 */
	public long getListId() {
		return this.listId;
	}

	/**
	 * Sets the field called 'listId' to the given value.
	 * 
	 * @param listId
	 *            The listId to set.
	 */
	public void setListId(long listId) {
		this.listId = listId;
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
	 * Returns the value of the field called 'nStock'.
	 * 
	 * @return Returns the nStock.
	 */
	public int getnStock() {
		return this.nStock;
	}

	/**
	 * Sets the field called 'nStock' to the given value.
	 * 
	 * @param nStock
	 *            The nStock to set.
	 */
	public void setnStock(int nStock) {
		this.nStock = nStock;
	}

	/**
	 * Returns the value of the field called 'nBuy'.
	 * 
	 * @return Returns the nBuy.
	 */
	public int getnBuy() {
		return this.nBuy;
	}

	/**
	 * Sets the field called 'nBuy' to the given value.
	 * 
	 * @param nBuy
	 *            The nBuy to set.
	 */
	public void setnBuy(int nBuy) {
		this.nBuy = nBuy;
	}

	/**
	 * Returns the value of the field called 'unitSize'.
	 * 
	 * @return Returns the unitSize.
	 */
	public float getUnitSize() {
		return this.unitSize;
	}

	/**
	 * Sets the field called 'unitSize' to the given value.
	 * 
	 * @param unitSize
	 *            The unitSize to set.
	 */
	public void setUnitSize(float unitSize) {
		this.unitSize = unitSize;
	}

	/**
	 * Returns the value of the field called 'unitLabel'.
	 * 
	 * @return Returns the unitLabel.
	 */
	public ItemUnitLabel getUnitLabel() {
		return this.unitLabel;
	}

	/**
	 * Sets the field called 'unitLabel' to the given value.
	 * 
	 * @param unitLabel
	 *            The unitLabel to set.
	 */
	public void setUnitLabel(ItemUnitLabel unitLabel) {
		this.unitLabel = unitLabel;
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
	 * Sets the field called 'isBought' to the given value.
	 * 
	 * @param isBought
	 *            The isBought to set.
	 */
	public void setBought(boolean isBought) {
		this.isBought = isBought;
	}

	/**
	 * Returns the value of the field called 'stockIdx'.
	 * 
	 * @return Returns the stockIdx.
	 */
	public int getStockIdx() {
		return this.stockIdx;
	}

	/**
	 * Sets the field called 'stockIdx' to the given value.
	 * 
	 * @param stockIdx
	 *            The stockIdx to set.
	 */
	public void setStockIdx(int stockIdx) {
		this.stockIdx = stockIdx;
	}

	/**
	 * Returns the value of the field called 'shopIdx'.
	 * 
	 * @return Returns the shopIdx.
	 */
	public int getShopIdx() {
		return this.shopIdx;
	}

	/**
	 * Sets the field called 'shopIdx' to the given value.
	 * 
	 * @param shopIdx
	 *            The shopIdx to set.
	 */
	public void setShopIdx(int shopIdx) {
		this.shopIdx = shopIdx;
	}
}
