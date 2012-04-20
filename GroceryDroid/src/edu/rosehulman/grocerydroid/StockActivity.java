package edu.rosehulman.grocerydroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import edu.rosehulman.grocerydroid.model.ShoppingList.Order;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		updateMainPrompt();

		setListView((ListView) findViewById(R.id.stock_list_view));
		StockItemAdapter sia = new StockItemAdapter(this, R.layout.stock_item,
				getShoppingList().getItems(Order.STOCK));
		setItemAdapter(sia);
		sia.setStockActivity(this);

//		getListView().setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View v, int pos,
//					long id) {
//				mSelectedItem = getShoppingList().getItems(Order.STOCK).get(pos);
//				// Is this just launchItemDialog();
//
//				ItemDialogFragment df = new ItemDialogFragment();
//				df.setItem(mSelectedItem);
//				df.show(getSupportFragmentManager(), "choose_action");
//			}
//		});

		// TODO: display icons for shopping, adding an item, and
		// rearranging stock order.

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
		// handle item selection
		switch (menuItem.getItemId()) {
		case R.id.add_name_menu_item:
			launchNewItemDialog();
			return super.onOptionsItemSelected(menuItem);
			// TODO: add reset all button.
			// case :
			// for (Item item : getShoppingList().getItems(Order.AS_IS)) {
			// item.resetNumberToBuy();
			// }
			// super.onOptionsItemSelected(menuItem);
			// move to ShoppingListActivity: int nChanged =
			// mIda.updateAllItemsInList(getShoppingList());
			// return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}
}
