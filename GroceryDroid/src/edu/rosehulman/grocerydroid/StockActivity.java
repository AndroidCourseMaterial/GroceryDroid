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
 * The activity used to manage the creation and restocking of the items in a
 * list.
 * 
 * @author Matthew Boutell. Created Apr 12, 2012.
 */
public class StockActivity extends ShoppingListActivity {
	// private AutoCompleteTextView mNameBox;
	// private ImageView mEditIcon;
	// private ArrayAdapter<String> mAutoAdapter;

	private static final int SORT_ITEMS = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(MyApplication.GD, "Begin onCreate stock");
		setTheme(R.style.Theme_Sherlock_ForceOverflow);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stock_layout);
		getSupportActionBar().setIcon(R.drawable.ic_list);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = this.getIntent();
		long listId = intent.getLongExtra(MainActivity.KEY_SELECTED_LIST, -1);
		Log.d(MyApplication.GD, "stocking with list " + listId);

		initializeDatabase();
		initializeShoppingList(listId);
		getSupportActionBar().setSubtitle(getShoppingList().getName());

		setListView((ListView) findViewById(R.id.stock_list_view));
// Manage the adapter's list myself in refreshDisplay.
//		StockItemAdapter sia = new StockItemAdapter(this, R.layout.stock_item,
//				getShoppingList().getItems(Order.STOCK));
		StockItemAdapter sia = new StockItemAdapter(this, R.layout.stock_item);
		setItemAdapter(sia);
		sia.setStockActivity(this);

		refreshDisplay();

		// getListView().setOnItemClickListener(new OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> parent, View v, int pos,
		// long id) {
		// mSelectedItem = getShoppingList().getItems(Order.STOCK).get(pos);
		// // Is this just launchItemDialog();
		//
		// ItemDialogFragment df = new ItemDialogFragment();
		// df.setItem(mSelectedItem);
		// df.show(getSupportFragmentManager(), "choose_action");
		// }
		// });

		// LayoutInflater inflater = (LayoutInflater)
		// getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// View layout = inflater.inflate(R.layout.add_name_group, null);
		// mNameBox = (AutoCompleteTextView) layout
		// .findViewById(R.id.add_name_autocomplete);
		// mEditIcon = (ImageView) layout.findViewById(R.id.edit_icon);
		// mNameBox.setCompletionHint(this.getString(R.string.item_name));
		// String[] names = new String[] { "bob", "joe", "caleb", "jonathan",
		// "elise" };
		// mAutoAdapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, names);
		// mNameBox.setAdapter(mAutoAdapter);

		// mEditIcon.setOnKeyListener(new OnKeyListener() {
		// @Override
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		// // If the event is a key-down event on the "enter" button
		// if ((event.getAction() == KeyEvent.ACTION_DOWN)
		// && (keyCode == KeyEvent.KEYCODE_ENTER)) {
		// Toast.makeText(StockActivity.this, tv.getText(),
		// Toast.LENGTH_SHORT).show();
		// return true;
		// }
		// return false;
		// }
		// });

		Log.d(MyApplication.GD, "End onCreate stock");
	}

	@Override
	protected void onResume() {
		Log.d(MyApplication.GD, "Begin onresume stock");
		super.onResume();
		initializeShoppingList(getShoppingList().getId());
		refreshDisplay();
		Log.d(MyApplication.GD, "End onresume stock");
	}
	
	@Override
	protected void refreshDisplay() {
		updateMainPrompt();
		getItemAdapter().clear();
		for (Item item : getShoppingList().getItems(Order.STOCK)) {
			getItemAdapter().add(item);
		}
		getItemAdapter().notifyDataSetChanged(); // redundant?
	}

	/**
	 * Updates the prompt depending on if any items are present.
	 */
	@Override
	protected void updateMainPrompt() {
		TextView tv = (TextView) findViewById(R.id.stock_list_prompt);
		if (getShoppingList().getItems(Order.STOCK).size() > 0) {
			tv.setText(R.string.shopping_list_prompt_items_present);
		} else {
			tv.setText(R.string.shopping_list_prompt_default);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.stock, menu);
		// TODO: add this back in once I get autocomplte working:
		// <item
		// android:id="@+id/add_name_menu_item"
		// android:icon="@drawable/ic_action_plus"
		// android:showAsAction="collapseActionView|always"
		// android:title="@string/add_item"
		// android:actionLayout="@layout/add_name_group"
		// />

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case R.id.stock_menu_item_add_name:
			launchNewItemDialog();
			return super.onOptionsItemSelected(menuItem);
		case R.id.stock_menu_item_reset_all:
			resetNumberToBuyForAllItems();
			return true;
		case R.id.stock_menu_item_go_shopping:
			// Note: My original idea was to launch the ShopActivity right from here. But 
			// if I added an item in ShopActivity, it wasn't updated when the back button
			// was pressed. The current solution is cleaner.
			Intent returnData = new Intent();
			returnData.putExtra(MainActivity.KEY_GO_SHOPPING, MainActivity.GO_SHOPPING);
			setResult(RESULT_OK, returnData);
			finish();

			return true;
		case R.id.stock_menu_item_sort_items:
			Intent intent = new Intent(this, SortByStockOrderActivity.class);
			intent.putExtra(MainActivity.KEY_SELECTED_LIST, getShoppingList().getId());
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}

	private void resetNumberToBuyForAllItems() {
		int nReset = 0;
		ArrayList<Item> items = getShoppingList().getItems(Order.AS_IS);
		for (Item item : items) {
			if (item.getNBuy() > 0) {
				item.resetNumberToBuy();
				nReset++;
			}
		}
		if (nReset > 0) {
			getIda().updateAllItemsInList(getShoppingList());
			refreshDisplay();
		}
	}
}
