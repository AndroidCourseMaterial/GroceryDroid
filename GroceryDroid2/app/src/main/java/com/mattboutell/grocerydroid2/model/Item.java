package com.mattboutell.grocerydroid2.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.mattboutell.grocerydroid2.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * A single grocery item.
 * 
 * @author Matthew Boutell. Created Nov 7, 2011.
 */
public class Item implements Parcelable {

	// Firebase
	public static final String NAME = "NAME";
	public static final String SHOPPING_LIST_KEY = "SHOPPING_LIST_KEY";
	public static final String NUM_STOCK = "NUM_STOCK";
	public static final String NUM_BUY = "NUM_BUY";
	public static final String PRICE = "PRICE";
	public static final String UNIT_SIZE = "UNIT_SIZE";
	public static final String UNIT_LABEL = "UNIT_LABEL";
	public static final String IS_BOUGHT = "IS_BOUGHT";
	public static final String STOCK_INDEX = "STOCK_INDEX";
	public static final String SHOP_INDEX = "SHOP_INDEX";

	// CONSIDER: use a BigDecimal for the price and unitSize so that comparisons
	// are easier.
	private static final float EPSILON = 0.000001f;

	private static final String DEFAULT_KEY = "";

	private String mKey;
	private String mShoppingListKey;  // the list to which it belongs

	private String mName;
	private int mNumStock;
	private int mNumBuy;
	private double mPrice;
	// CONSIDER: should I have a simple Unit class that encapsulates unit size
	// and label?
	private double mUnitSize;
	private UnitLabel mUnitLabel;
	private boolean mIsBought;
	private int mStockIdx;
	private int mShopIdx;

	// key, listKey, name, nStock, nBuy, price, unitSize, unitLabel, isBought, stockIdx, shopIdx

	public Item(String key, String shoppingListKey, String name, int nStock, int nBuy,
			float price, float unitSize, UnitLabel unitLabel, boolean isBought,
			int stockIdx, int shopIdx) {
		this.mKey = key;
		this.mShoppingListKey = shoppingListKey;
		setName(name);
		this.mNumStock = nStock;
		this.mNumBuy = nBuy;
		this.mPrice = price;
		this.mUnitSize = unitSize;
		this.mUnitLabel = unitLabel;
		this.mIsBought = isBought;
		this.mStockIdx = stockIdx;
		this.mShopIdx = shopIdx;
	}


	public Item(String key, String shoppingListKey, String name, int nStock, float price,
			float unitSize, UnitLabel unitLabel) {
		this(key, shoppingListKey, name, nStock, 0, price, unitSize, unitLabel, false, 0,
				0);
	}

	public Item(String shoppingListKey) {
		this(DEFAULT_KEY, shoppingListKey, "", 1, 1, 0.0f, 1.0f, UnitLabel.unit, false,
				0, 0);
	}

	// Used by Firebase for existing items
	public Item(DataSnapshot snapshot) {
		mKey = snapshot.getKey();
		setValues(snapshot);
	}

    @SuppressWarnings("unchecked")
	public void setValues(DataSnapshot snapshot) {
		if (!(snapshot.getKey().equals(mKey))) {
			Log.w(Constants.TAG, "Attempt to set values on item with different key");
			return;
		}
		Map<String, Object> values = (Map<String, Object>)snapshot.getValue();
		if (values != null) {
			mName = (String) values.get(NAME);
			mShoppingListKey = (String)values.get(SHOPPING_LIST_KEY);
			mNumStock = (int)values.get(NUM_STOCK);
			mNumBuy = (int)values.get(NUM_BUY);
			mPrice = (double)values.get(PRICE);
			mUnitSize = (double)values.get(UNIT_SIZE);
            int unitIndex = (int)values.get(UNIT_LABEL);
            mUnitLabel = UnitLabel.values()[unitIndex];
			mIsBought = (boolean)values.get(IS_BOUGHT);
			mStockIdx = (int)values.get(STOCK_INDEX);
			mShopIdx = (int)values.get(SHOP_INDEX);
		}
	}

	public Map<String, Object> valuesMap() {
		Map<String, Object> map = new HashMap<>();
        map.put(NAME, mName);
        map.put(SHOPPING_LIST_KEY, mShoppingListKey);
        map.put(NUM_STOCK, mNumStock);
        map.put(NUM_BUY, mNumBuy);
        map.put(PRICE, mPrice);
        map.put(UNIT_SIZE, mUnitSize);
        map.put(UNIT_LABEL, mUnitLabel.ordinal());
        map.put(IS_BOUGHT, mIsBought);
        map.put(STOCK_INDEX, mStockIdx);
        map.put(SHOP_INDEX, mShopIdx);
		return map;
	}


	@Override
	public String toString() {
		// CONSIDER: have String format be locale dependent
		// USA: String s = String.format("%s (%d) $%.2f/%.1f %s", this.name,
		// this.nToStock, this.price, this.size, this.unit.toString());
		return String.format("%s %s %s (%d/%d) %.0fK/%.1f %s %s %d %d",
				this.mKey, this.mShoppingListKey, this.mName, this.mNumBuy,
				this.mNumStock, this.mPrice, this.mUnitSize, this.mUnitLabel,
				this.mIsBought ? "B" : "N", this.mStockIdx, this.mShopIdx);
	}

//	/**
//	 * @return A short string for this item: name and unit
//	 */
//	public String toShortString() {
//		return String.format("%s, %.1f %s", this.mName, this.mUnitSize,
//				this.mUnitLabel);
//	}

	private boolean isIntegerValue(double value) {
		return (Math.abs(value - (int) value) < Item.EPSILON);
	}

	/**
	 * Returns information useful to display when stocking the item.
	 * 
	 * @return A string representation of some of this item's information.
	 */
	public String getStockInfo() {
		// TODO: unit-test this.
		// return String.format("Stock %d @ %.0fK/%.1f %s", this.mNumStock,
		// this.mPrice, this.mUnitSize, this.mUnitLabel);

		StringBuilder builder = new StringBuilder();
		
		builder.append(String.format("Stock %d", this.mNumStock));
		if (this.mPrice > EPSILON || this.mUnitSize > EPSILON) {
			String formatter;
			if (isIntegerValue(this.mUnitSize)) {
				formatter = " @ $%.2f/%.0f %s";
			} else {
				formatter = "@ $%.2f/%.1f %s";
			}
			builder.append(String.format(formatter, this.mPrice,
					this.mUnitSize, this.mUnitLabel));
		}
		return builder.toString();
	}

	/**
	 * Returns information useful to display when shopping for the item.
	 * 
	 * @return A string representation of some of this item's information.
	 */
	public String getShopInfo() {
		// TODO: unit-test this.
		StringBuilder builder = new StringBuilder();
		
		if (this.mPrice > EPSILON || this.mUnitSize > EPSILON) {
			String formatter;
			if (isIntegerValue(this.mUnitSize)) {
				formatter = "$%.2f/%.0f %s";
			} else {
				formatter = "$%.2f/%.1f %s";
			}
			builder.append(String.format(formatter, this.mPrice,
					this.mUnitSize, this.mUnitLabel));
		}
		return builder.toString();
// Old style for ZMK
//		return String.format("%.0fK/%.1f %s", this.mPrice, this.mUnitSize,
//				this.mUnitLabel);
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
		return this.mKey.equals(other.mKey) && this.mShoppingListKey.equals(other.mShoppingListKey)
				&& this.mName.equals(other.mName)
				&& this.mNumBuy == other.mNumBuy
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

    public String getKey() {
        return mKey;
    }

	// CONSIDER: removing unused setters/getters.

	/**
	 * Returns the value of the field called 'price'.
	 * 
	 * @return Returns the price.
	 */
	public double getPrice() {
		return this.mPrice;
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
	 * Returns the value of the field called 'nBuy'.
	 * 
	 * @return Returns the nBuy.
	 */
	public int getNBuy() {
		return this.mNumBuy;
	}

	/**
	 * Returns the value of the field called 'unitSize'.
	 * 
	 * @return Returns the unitSize.
	 */
	public double getUnitSize() {
		return this.mUnitSize;
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

    public String getShoppingListKey() {
        return mShoppingListKey;
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

		/** quart */
		gallon,

		/** quart */
		quart,

		/** box */
		box,

		/** bag */
		bag,

		/** count */
		count,

		/** square feet */
		sqft
	}

	@Override
	public int describeContents() {
		// Since no special objects in the marchalled representation
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(mKey);
		out.writeString(mShoppingListKey);
		out.writeString(mName);
		out.writeInt(mNumStock);
		out.writeInt(mNumBuy);
		out.writeDouble(mPrice);
		out.writeDouble(mUnitSize);
		out.writeInt(mUnitLabel.ordinal());
		out.writeInt(mIsBought ? 1 : 0);
		out.writeInt(mStockIdx);
		out.writeInt(mShopIdx);
	}

	/**
	 * The CREATOR, required by the parcelable interface.
	 */
	public static final Creator<Item> CREATOR = new Creator<Item>() {
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
		mKey = in.readString();
		mShoppingListKey = in.readString();
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
