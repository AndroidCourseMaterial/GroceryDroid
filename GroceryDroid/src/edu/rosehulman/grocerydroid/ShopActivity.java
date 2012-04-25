package edu.rosehulman.grocerydroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import edu.rosehulman.grocerydroid.model.Item;
import edu.rosehulman.grocerydroid.model.ShoppingList.Order;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import java.util.ArrayList;

/**
 * The activity used to manage the use of a list in the store.
 * 
 * @author Matthew Boutell. Created Apr 25, 2012.
 */
public class ShopActivity extends ShoppingListActivity {
	private ArrayList<Item> mItemsToDisplay;
	private boolean showAll = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_ForceOverflow);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_layout);
		getSupportActionBar().setIcon(R.drawable.ic_action_shop);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = this.getIntent();
		long listId = intent.getLongExtra(MainActivity.KEY_SELECTED_LIST, -1);
		Log.d(MyApplication.GD, "shopping with list " + listId);

		initializeDatabase();
		initializeShoppingList(listId);
		getSupportActionBar().setSubtitle(getShoppingList().getName());
		updateMainPrompt();

		// Copy into temporary list for this screen only (so can toggle which
		// are displayed).
		mItemsToDisplay = new ArrayList<Item>();
		for (Item item : getShoppingList().getItems(Order.SHOP)) {
			mItemsToDisplay.add(item);
		}

		setListView((ListView) findViewById(R.id.shop_list_view));
		ShopItemAdapter sia = new ShopItemAdapter(this, R.layout.shop_item,
				mItemsToDisplay);
		setItemAdapter(sia);
		sia.setShopActivity(this);

		refreshDisplay();
	}

	/**
	 * Updates the prompt depending on if any items are present.
	 */
	@Override
	protected void updateMainPrompt() {
		TextView tv = (TextView) findViewById(R.id.shop_list_prompt);
		if (getShoppingList().getItems(Order.STOCK).size() > 0) {
			tv.setText(R.string.shopping_list_prompt_items_present);
		} else {
			tv.setText(R.string.shopping_list_prompt_default);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.shop, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		// TODO add showHide:
		// // Note: similar to findViewById()
		// MenuItem showHide = menu.findItem(R.id.shopMenuShowHide);
		// // Toggle the menu title. If current current status is to show, then
		// // the menu asks, "Hide?" and vice-versa
		// String text = this.showAll ? getString(R.string.hide_bought)
		// : getString(R.string.show_all);
		// showHide.setTitleCondensed(text);
		// showHide.setTitle(text);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case R.id.shop_menu_item_add_name:
			launchNewItemDialog();
			return super.onOptionsItemSelected(menuItem);
			
		case R.id.shop_menu_item_reset_all:
			resetBoughtForAllItems();
			return true;
			// case R.id.shopMenuShowHide:
			// this.showAll = !this.showAll;
			// updateDisplayList();
			// this.sia.notifyDataSetChanged();
			// break;
			// case R.id.shopMenuDone:
			// for (Item itm : this.shoppingList.getItems()) {
			// itm.setBought(false);
			// }
			// this.finish();
			// }
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}

	private void resetBoughtForAllItems() {
		int nReset = 0;
		ArrayList<Item> items = getShoppingList().getItems(Order.SHOP);
		for (Item item : items) {
			if (item.isBought()) {
				item.setBought(false);
				nReset++;
			}
		}
		if (nReset > 0) {
			getIda().updateAllItemsInList(getShoppingList());
			refreshDisplay();
		}
	}

	/**
	 * Only displays those items that have been bought.
	 */
	@Override
	protected void refreshDisplay() {
		updateMainPrompt();
		mItemsToDisplay.clear();
		for (Item item : getShoppingList().getItems(Order.SHOP)) {
			if (item.getNBuy() > 0) {
				if (this.showAll || !item.isBought()) {
					mItemsToDisplay.add(item);
				}
			}
		}

		TextView totalSpentTV = (TextView) findViewById(R.id.shop_layout_total_spent_text);
		// CONSIDER: read format string from resources?
		String s = String.format("Spent %.2f of %.2f from list",
				getShoppingList().totalSpent(), getShoppingList().totalPrice());
		totalSpentTV.setText(s);
		getItemAdapter().notifyDataSetChanged();
	}
}
