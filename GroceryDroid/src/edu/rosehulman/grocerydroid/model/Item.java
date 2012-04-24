package edu.rosehulman.grocerydroid.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import edu.rosehulman.grocerydroid.MyApplication;

/**
 * A single grocery item.
 * 
 * @author Matthew Boutell. Created Nov 7, 2011.
 */
public class Item implements Parcelable {

	// CONSIDER: use a BigDecimal for the price and unitSize so that comparisons
	// are easier.
	private static final float EPSILON = 0.000001f;

	private static final long DEFAULT_ID = 0;
	private long mId;
	private long mListId; // the list to which it belongs
	private String mName;
	private int mNumStock;
	private int mNumBuy;
	private float mPrice;
	// CONSIDER: should I have a simple Unit class that encapsulates unit size
	// and label?
	private float mUnitSize;
	private UnitLabel mUnitLabel;
	private boolean mIsBought;
	private int mStockIdx;
	private int mShopIdx;

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
			float price, float size, UnitLabel unit, boolean isBought,
			int stockIdx, int shopIdx) {
		this.mId = id;
		this.mListId = listId;
		setName(name);
		this.mNumStock = nStock;
		this.mNumBuy = nBuy;
		this.mPrice = price;
		this.mUnitSize = size;
		this.mUnitLabel = unit;
		this.mIsBought = isBought;
		this.mStockIdx = stockIdx;
		this.mShopIdx = shopIdx;
	}

	/**
	 * Creates a Item from the given parameters.
	 * 
	 * @param listId
	 */
	public Item(long listId) {
		this(DEFAULT_ID, listId, "", 1, 1, 0.0f, 1.0f, UnitLabel.unit, false,
				0, 0);
	}

	@Override
	public String toString() {
		// CONSIDER: have String format be locale dependent
		// USA: String s = String.format("%s (%d) $%.2f/%.1f %s", this.name,
		// this.nToStock, this.price, this.size, this.unit.toString());
		String s = String.format("%d %d %s (%d/%d) %.0fK/%.1f %s %s %d %d",
				this.mId, this.mListId, this.mName, this.mNumBuy, this.mNumStock,
				this.mPrice, this.mUnitSize, this.mUnitLabel, this.mIsBought ? "B"
						: "N", this.mStockIdx, this.mShopIdx);
		return s;
	}

	/**
	 * @return A short string for this item: name and unit
	 */
	public String toShortString() {
		return String.format("%s, %.1f %s", this.mName, this.mUnitSize,
				this.mUnitLabel);
	}

	/**
	 * Returns information useful to display when stocking the item.
	 * 
	 * @return A string representation of some of this item's information.
	 */
	public String getStockInfo() {
		// TODO: unit-test this.
		return String.format("Stock %d @ %.0fK/%.1f %s", this.mNumStock,
				this.mPrice, this.mUnitSize, this.mUnitLabel);
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
		return this.mId == other.mId && this.mListId == other.mListId
				&& this.mName.equals(other.mName) && this.mNumBuy == other.mNumBuy
				&& this.mNumStock == other.mNumStock
				&& Math.abs(this.mPrice - other.mPrice) < EPSILON
				&& Math.abs(this.mUnitSize - other.mUnitSize) < EPSILON
				&& this.mUnitLabel == other.mUnitLabel
				&& this.mIsBought == other.mIsBought
				&& this.mStockIdx == other.mStockIdx
				&& this.mShopIdx == other.mShopIdx;
	}

	/**
	 * Adds one to the number to be bought.
	 * 
	 */
	public void incrementNumberToBuy() {
		this.mNumBuy++;
	}

	/**
	 * Sets the number to buy back to 0.
	 */
	public void resetNumberToBuy() {
		this.mNumBuy = 0;
	}

	// id, name, nStock, nBuy, cost, unitSize, unitLabel, isBought,
	// stockIdx, shopIdx

	/**
	 * @return The total amount spent on this item: 0 if not yet bought.
	 */
	public double totalSpent() {
		return this.mIsBought ? totalPrice() : 0;
	}

	/**
	 * @return The total price of this item (price * number to buy) if it were
	 *         bought.
	 */
	public double totalPrice() {
		return this.mNumBuy * this.mPrice;
	}

	// CONSIDER: removing unused setters/getters.

	/**
	 * Returns the value of the field called 'price'.
	 * 
	 * @return Returns the price.
	 */
	public float getPrice() {
		return this.mPrice;
	}

	/**
	 * Sets the field called 'price' to the given value.
	 * 
	 * @param price
	 *            The price to set.
	 */
	public void setPrice(float price) {
		this.mPrice = price;
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
	 * Returns the value of the field called 'listId'.
	 * 
	 * @return Returns the listId.
	 */
	public long getListId() {
		return this.mListId;
	}

	/**
	 * Sets the field called 'listId' to the given value.
	 * 
	 * @param listId
	 *            The listId to set.
	 */
	public void setListId(long listId) {
		this.mListId = listId;
	}

	/**
	 * Returns the value of the field called 'name'.
	 * 
	 * @return Returns the name.
	 */
	public String getName() {
		return this.mName;
	}

	private static String capitalString(String word) {
		String capWord = word;
		if (capWord != null) {
			capWord = capWord.trim();
			if (capWord.length() > 0) {
				Log.d(MyApplication.GD, "Capitalizing " + word);
				capWord = Character.toUpperCase(capWord.charAt(0))
						+ capWord.substring(1);
			}
		}
		return capWord;
	}
	
	/**
	 * Sets the field called 'name' to the given value, trimmed and capitalized.
	 * 
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.mName = capitalString(name);
	}

	/**
	 * Returns the value of the field called 'nStock'.
	 * 
	 * @return Returns the nStock.
	 */
	public int getNStock() {
		return this.mNumStock;
	}

	/**
	 * Sets the field called 'nStock' to the given value.
	 * 
	 * @param nStock
	 *            The nStock to set.
	 */
	public void setNStock(int nStock) {
		this.mNumStock = nStock;
	}

	/**
	 * Returns the value of the field called 'nBuy'.
	 * 
	 * @return Returns the nBuy.
	 */
	public int getNBuy() {
		return this.mNumBuy;
	}

	/**
	 * Sets the field called 'nBuy' to the given value.
	 * 
	 * @param nBuy
	 *            The nBuy to set.
	 */
	public void setNBuy(int nBuy) {
		this.mNumBuy = nBuy;
	}

	/**
	 * Returns the value of the field called 'unitSize'.
	 * 
	 * @return Returns the unitSize.
	 */
	public float getUnitSize() {
		return this.mUnitSize;
	}

	/**
	 * Sets the field called 'unitSize' to the given value.
	 * 
	 * @param unitSize
	 *            The unitSize to set.
	 */
	public void setUnitSize(float unitSize) {
		this.mUnitSize = unitSize;
	}

	/**
	 * Returns the value of the field called 'unitLabel'.
	 * 
	 * @return Returns the unitLabel.
	 */
	public UnitLabel getUnitLabel() {
		return this.mUnitLabel;
	}

	/**
	 * Sets the field called 'unitLabel' to the given value.
	 * 
	 * @param unitLabel
	 *            The unitLabel to set.
	 */
	public void setUnitLabel(UnitLabel unitLabel) {
		this.mUnitLabel = unitLabel;
	}

	/**
	 * Returns the value of the field called 'isBought'.
	 * 
	 * @return Returns the isBought.
	 */
	public boolean isBought() {
		return this.mIsBought;
	}

	/**
	 * Sets the field called 'isBought' to the given value.
	 * 
	 * @param isBought
	 *            The isBought to set.
	 */
	public void setBought(boolean isBought) {
		this.mIsBought = isBought;
	}

	/**
	 * Returns the value of the field called 'stockIdx'.
	 * 
	 * @return Returns the stockIdx.
	 */
	public int getStockIdx() {
		return this.mStockIdx;
	}

	/**
	 * Sets the field called 'stockIdx' to the given value.
	 * 
	 * @param stockIdx
	 *            The stockIdx to set.
	 */
	public void setStockIdx(int stockIdx) {
		this.mStockIdx = stockIdx;
	}

	/**
	 * Returns the value of the field called 'shopIdx'.
	 * 
	 * @return Returns the shopIdx.
	 */
	public int getShopIdx() {
		return this.mShopIdx;
	}

	/**
	 * Sets the field called 'shopIdx' to the given value.
	 * 
	 * @param shopIdx
	 *            The shopIdx to set.
	 */
	public void setShopIdx(int shopIdx) {
		this.mShopIdx = shopIdx;
	}

	/**
	 * A list of potential units that this item can be sold in.
	 * 
	 * @author Matthew Boutell. Created Nov 7, 2011.
	 */
	public enum UnitLabel {
		/** default */
		unit,

		/** grams */
		g,

		/** kilograms */
		kg,

		/** milliliters */
		mL,

		/** liters */
		L,

		/** pounds */
		lb,

		/** ounces */
		oz,

		/** box */
		box,

		/** bag */
		bag
	}

	@Override
	public int describeContents() {
		// Since no special objects in the marchalled representation
		return 0;
	}

     @Override
	public void writeToParcel(Parcel out, int flags) {
         out.writeLong(mId);
         out.writeLong(mListId);
         out.writeString(mName);
         out.writeInt(mNumStock);
         out.writeInt(mNumBuy);
         out.writeFloat(mPrice);
         out.writeFloat(mUnitSize);
         out.writeInt(mUnitLabel.ordinal());
         out.writeInt(mIsBought ? 1 : 0);
         out.writeInt(mStockIdx);
         out.writeInt(mShopIdx);
     }

     /**
     * The CREATOR, required by the parcelable interface.
     */
    public static final Parcelable.Creator<Item> CREATOR
             = new Parcelable.Creator<Item>() {
         @Override
		public Item createFromParcel(Parcel in) {
             return new Item(in);
         }

		@Override
		public Item[] newArray(int size) {
             return new Item[size];
         }
     };
     
     private Item(Parcel in) {
    	 mId = in.readLong();
    	 mListId = in.readLong();
    	 mName = in.readString();
    	 mNumStock = in.readInt();
         mNumBuy = in.readInt();
         mPrice = in.readFloat();
         mUnitSize = in.readFloat();
         mUnitLabel = UnitLabel.values()[in.readInt()];
         mIsBought = in.readInt() == 1;
    	 mStockIdx = in.readInt();
         mShopIdx = in.readInt();
     }
}
