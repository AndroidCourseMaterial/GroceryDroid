package edu.rosehulman.boutell.grocery;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The list view that is used while in the store.
 * 
 * @author Matthew Boutell. Created Nov 22, 2011.
 */
public class ShopActivity extends Activity {

	private ShoppingList shoppingList;
	private ListView itemListView;
	private ArrayList<Item> itemsToDisplay;

	private ShopItemAdapter sia;

	private SqliteAdapter dbAdapter;
	private Cursor groceryListCursor;

	private boolean showAll = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_layout);

		this.shoppingList = new ShoppingList();
		this.itemsToDisplay = new ArrayList<Item>();
		this.itemListView = (ListView) findViewById(R.id.shopListView);
		this.sia = new ShopItemAdapter(this, R.layout.shop_item,
				this.itemsToDisplay);
		this.sia.setShopActivity(this);
		this.itemListView.setAdapter(this.sia);

		//Button addItemButton = (Button) findViewById(R.id.addItemToShopListButton);
		// TODO: when the button is pressed, it should ask for data. But number
		// to buy should be set to 1 automatically?
		// addItemButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// TextView tv = (TextView) findViewById(R.id.stockTitle);
		// tv.setText("Adding item");
		// Intent intent = new Intent(StockActivity.this,
		// ItemActivity.class);
		// startActivityForResult(intent, GET_ITEM);
		// }
		// });

		// TODO: It could be better to make the db a singleton. Change this?
		this.dbAdapter = new SqliteAdapter(this);

		// Open or create the database
		this.dbAdapter.open();

		this.populateGroceryList();
	}

	private void populateGroceryList() {
		this.groceryListCursor = this.dbAdapter.getAllGroceryItemsCursor();
		startManagingCursor(this.groceryListCursor);
		refreshItemsFromDB();
	}

	private void refreshItemsFromDB() {
		this.dbAdapter.updateItemsUsingDB(this.shoppingList.getItems());
		updateDisplayList();
	}

	/**
	 * Updates the display list depending on what has been bought.
	 */
	protected void updateDisplayList() {
		this.itemsToDisplay.clear();
		float totalSpent = 0.0f;

		for (Item item : this.shoppingList.getItems()) {
			if (item.getNToBuy() > 0) {
				if (this.showAll || !item.isBought()) {
					this.itemsToDisplay.add(item);
				}
			}

			if (item.isBought()) {
				totalSpent += item.getNToBuy() * item.getPrice();
			}

		}
		TextView totalSpentTV = (TextView) findViewById(R.id.totalSpentText);
		totalSpentTV.setText(this.getString(R.string.total_spent) + " K"
				+ Float.toString(totalSpent));
		this.sia.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.shop_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		// Note: similar to findViewById()
		MenuItem showHide = menu.findItem(R.id.shopMenuShowHide);
		// Toggle the menu title. If current current status is to show, then
		// the menu asks, "Hide?" and vice-versa
		String text = this.showAll ? getString(R.string.hide_bought)
				: getString(R.string.show_all);
		showHide.setTitleCondensed(text);
		showHide.setTitle(text);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.shopMenuShowHide:
			this.showAll = !this.showAll;
			updateDisplayList();
			this.sia.notifyDataSetChanged();
			break;
		case R.id.shopMenuReset:
			for (Item itm : this.shoppingList.getItems()) {
				itm.setBought(false);
				updateDisplayList();
				this.sia.notifyDataSetChanged();
			}
			break;
		case R.id.shopMenuDone:
			for (Item itm : this.shoppingList.getItems()) {
				itm.setBought(false);
			}
			this.finish();
		}
		return true;
	}

}
