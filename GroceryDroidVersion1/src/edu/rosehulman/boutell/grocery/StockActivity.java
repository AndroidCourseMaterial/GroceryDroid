package edu.rosehulman.boutell.grocery;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Screen for creating a shopping list.
 * 
 * @author Matthew Boutell. Created Nov 7, 2011.
 */
public class StockActivity extends Activity {
	private static final int EDIT_ITEM = 1;
	private static final int GET_ITEM = 2;

	/** For debug log messages */
	public static final String GD = "GD";
	private static final int DIALOG_ID_CONFIRM_DELETE = 1;

	private ShoppingList shoppingList;
	private ListView itemListView;

	private StockItemAdapter sia;

	private SqliteAdapter dbAdapter;
	private Cursor groceryListCursor;
	private Item itemToDelete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stock_layout);

		this.shoppingList = new ShoppingList();
		this.itemListView = (ListView) findViewById(R.id.stockListView);

		ArrayList<Item> items = this.shoppingList.getItems();

		this.sia = new StockItemAdapter(this, R.layout.stock_item, items);
		this.sia.setStockActivity(this);

		// TODO: Get whole item to respond to a long-click.
		// Attempts to do so:
		// this.sia = new StockItemAdapter(this,
		// android.R.layout.simple_list_item_1, items);

		// ArrayAdapters work, but are too simple.
		// ArrayAdapter<Item> aa = new ArrayAdapter<Item>(this,
		// android.R.layout.simple_list_item_1, this.shoppingList.getItems());
		// this.itemListView.setAdapter(aa);

		// this.itemListView
		// .setOnItemLongClickListener(new OnItemLongClickListener() {
		// @Override
		// public boolean onItemLongClick(AdapterView<?> parent,
		// View view, int pos, long id) {
		// // parent.getItemAtPosition(pos);
		// Toast.makeText(StockActivity.this, "" + pos,
		// Toast.LENGTH_SHORT).show();
		// return false;
		// }
		// });

		this.itemListView.setAdapter(this.sia);

		Button addItemButton = (Button) findViewById(R.id.addItemButton);
		addItemButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView tv = (TextView) findViewById(R.id.stockTitle);
				tv.setText("Adding item");
				Intent intent = new Intent(StockActivity.this,
						ItemActivity.class);
				startActivityForResult(intent, GET_ITEM);
			}
		});

		this.dbAdapter = new SqliteAdapter(this);

		// Open or create the database
		this.dbAdapter.open();

//		reloadListFromSpreadsheet(items);

		this.populateGroceryList();
	}

	private void populateGroceryList() {
		Log.d(GD, "starting to pop");
		this.groceryListCursor = this.dbAdapter.getAllGroceryItemsCursor();
		Log.d(GD, "got the cursor");
		startManagingCursor(this.groceryListCursor);
		refreshItemsFromDB();
	}

	private void refreshItemsFromDB() {
//		new UpdateFromDatabaseTask().execute();
		this.dbAdapter.updateItemsUsingDB(this.shoppingList.getItems());
		this.sia.notifyDataSetChanged();
	}

	private class UpdateFromDatabaseTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			StockActivity.this.dbAdapter.updateItemsUsingDB(StockActivity.this.shoppingList.getItems());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			StockActivity.this.sia.notifyDataSetChanged();
			Toast.makeText(StockActivity.this, "Loaded list...", Toast.LENGTH_SHORT).show();
	     }
		
	 }
	
	
	private void saveGroceryListToDB() {
		// Use an asynch task since it was unresponsive.
		// new SaveGroceryListToDBTask().execute();
		this.dbAdapter.updateItemsInDB(this.shoppingList.getItems());
	}

	private class SaveGroceryListToDBTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			StockActivity.this.dbAdapter.updateItemsUsingDB(StockActivity.this.shoppingList.getItems());
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			Toast.makeText(StockActivity.this, "Saved list...", Toast.LENGTH_SHORT).show();
			// Need some way to block the UI until this is done. Inconsistency issues...
		}
	}

	/**
	 * Launches an ItemActivity to edit this item.
	 * 
	 * @param item
	 */
	protected void editItem(Item item) {
		Intent intent = new Intent(StockActivity.this, ItemActivity.class);
		item.addToIntent(intent);
		//this.shoppingList.getItems().get(position).addToIntent(intent);
		startActivityForResult(intent, EDIT_ITEM);
	}

	/**
	 * Deletes the given item from the list.
	 *
	 * @param item 
	 */
	protected void deleteItem(Item item) {
		this.itemToDelete = item;
		this.showDialog(DIALOG_ID_CONFIRM_DELETE);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);
		Dialog dialog = null;
		switch (id) {
		case DIALOG_ID_CONFIRM_DELETE:
			// Create a new AlertDialog from a Builder so you can change all its
			// attributes using methods, not params to a constructor.
			AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(
					this);
			aboutDialogBuilder.setMessage(R.string.confirm_delete);
			aboutDialogBuilder.setCancelable(true);
			aboutDialogBuilder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							StockActivity.this.dbAdapter
									.removeItem(StockActivity.this.itemToDelete);
							refreshItemsFromDB();
							dialog.dismiss();
						}
					});
			aboutDialogBuilder.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			dialog = aboutDialogBuilder.create();
			break;

		}
		return dialog;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case GET_ITEM:
			if (resultCode == Activity.RESULT_OK) {
				Item item = new Item(data);
				long rowID = this.dbAdapter.insertItem(item);
				item.setId(rowID);
				Log.d(GD, "row id inserted: " + rowID);
				refreshItemsFromDB();
			}
			break;
		case EDIT_ITEM:
			if (resultCode == Activity.RESULT_OK) {
				Item item = new Item(data);
				this.dbAdapter.updateItem(item);

				Log.d(GD, "edited item with id = " + item.getId());
				refreshItemsFromDB();
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// TODO: save list to SQL and rest to prefs
		
		// TODO: when we pause to go to another activity, we want to save
		// using an asynch task, but if we pause to quit, then we want 
		// to save using a regular DB save (so it completes before the 
		// pause ends
		
		this.saveGroceryListToDB();
		Log.d(GD, "Pausing, saving.");

		// SharedPreferences uiState =
		// this.getPreferences(Context.MODE_PRIVATE);
		// SharedPreferences.Editor editor = uiState.edit();
		// editor.putString(KEY_TEXT_ENTRY,
		// this.myEditText.getText().toString());
		// editor.putBoolean(KEY_ADDING_ITEM, this.addingNew);
		// editor.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.populateGroceryList();
		Log.d(GD, "Resuming, loading!");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.dbAdapter.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.stock_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.stockMenuReloadList:
			reloadListFromSpreadsheet(this.shoppingList.getItems());
			populateGroceryList();
			break;
		case R.id.stockMenuResetList:
			for (Item itm : this.shoppingList.getItems()) {
				itm.setNToBuy(0);
				itm.setBought(false);
			}
			this.sia.notifyDataSetChanged();
			break;
		}
		return true;
	}

	private void reloadListFromSpreadsheet(ArrayList<Item> items) {
		Resources r = this.getResources();
		InputStream myFile = r.openRawResource(R.raw.shoprite_list);
		Scanner sc = new Scanner(myFile);
		items.clear();

		String line;
		while (sc.hasNextLine()) {
			line = sc.nextLine();
			String[] tokens = line.split(",");
			List<String> tokenList = Arrays.asList(tokens);
			Log.d(GD, tokenList.toString());
			int nToStock = 0;
			int i = 0;
			if (tokens[i].length() > 0) {
				nToStock = Integer.parseInt(tokens[i]);
			}
			i++;
			int price = 0;
			if (tokens[i].length() > 0) {
				price = Integer.parseInt(tokens[i]);
			}
			i++;
			float size = 0;
			if (tokens[i].length() > 0) {
				size = Float.parseFloat(tokens[i]);
			}
			i++;
			ItemUnit unit = ItemUnit.unit;
			String potentialUnit = tokens[i];
			for (ItemUnit u : ItemUnit.values()) {
				if (u.toString().equalsIgnoreCase(potentialUnit)) {
					unit = u;
				}
			}
			i++;
			// Clean it up
			String name = tokens[i].trim();
			if (name.charAt(0) == '\"') {
				name = name.substring(1);
			}
			long dummyID = 0;
			items.add(new Item(dummyID, name, nToStock, price, size, unit));
		}

		// Delete and re-open the DB so we can fill it again with the spreadsheet data.
		// Didn't work. Somehow onPause got called, which assumes there is a table, 
		// but it couldn't find one once it was deleted.
		//		this.dbAdapter.deleteDB();
//		this.dbAdapter.open();
	
		// TODO: Fix this hack!
		for (long i = 0; i < 1000; i++) {
			this.dbAdapter.removeItemWithId(i);
		}
	 	 
		
		Log.d(GD, items.toString());
		// Insert and get real ID
		for (Item item : items) {
			long id = this.dbAdapter.insertItem(item);
			item.setId(id);
		}
	}
}
