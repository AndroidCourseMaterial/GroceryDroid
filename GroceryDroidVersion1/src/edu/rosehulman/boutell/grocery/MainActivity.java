package edu.rosehulman.boutell.grocery;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Starting point is a list of shopping lists.
 * 
 * @author Matthew Boutell. Created Nov 8, 2011.
 */
public class MainActivity extends Activity {
	/** The whole app (both stock and shop) shares a single database. */
	// TODO: make the dbAdapter a singleton
	// protected static SqliteAdapter dbAdapter;
	
	private static final int DIALOG_ID_ADD_LIST = 1;
	private static final int DIALOG_ID_LIST_SELECTED = 2;
	private static final String KEY_SELECTED_LIST = "KEY_SELECTED_LIST";
	private static final int DIALOG_ID_CONFIRM_DELETE_LIST = 0;

	private String selectedList = null;
	private ArrayList<String> lists;
	private ArrayAdapter<String> arrayAdapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		populateLists();

		ListView lv = (ListView) findViewById(R.id.listOfLists);
		this.arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, this.lists);
		lv.setAdapter(this.arrayAdapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {
				MainActivity.this.selectedList = MainActivity.this.lists.get(pos);
				showDialog(DIALOG_ID_LIST_SELECTED);
			}
		});
	}

	private void populateLists() {
		// TODO: read from shared preferences
		this.lists = new ArrayList<String>();
		this.lists.add("Shoprite");
		this.lists.add("Supasave");
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case DIALOG_ID_LIST_SELECTED:
			AlertDialog.Builder listSelectedBuilder = new AlertDialog.Builder(
					this);
			listSelectedBuilder
					.setTitle("What would you like to do with this list?");
			listSelectedBuilder.setItems(R.array.list_selected_options,
					new DialogInterface.OnClickListener() {		
						@Override
						public void onClick(DialogInterface dialog, int which) {
//							Resources res = getResources();
//							String[] items = res
//									.getStringArray(R.array.list_selected_options);
//							Toast.makeText(MainActivity.this,
//									"Yeah for " + items[which],
//									Toast.LENGTH_SHORT).show();
							Intent intent = null;
							switch (which) {
							case 0:
								intent = new Intent(MainActivity.this, StockActivity.class);
								intent.putExtra(MainActivity.KEY_SELECTED_LIST, MainActivity.this.selectedList);
								startActivity(intent);
								break;
							case 1:
								intent = new Intent(MainActivity.this, ShopActivity.class);
								intent.putExtra(MainActivity.KEY_SELECTED_LIST, MainActivity.this.selectedList);
								startActivity(intent);
								break;
							case 2:
								MainActivity.this.showDialog(DIALOG_ID_CONFIRM_DELETE_LIST);
							}
							dialog.dismiss();
						}
					});
			dialog = listSelectedBuilder.create();
			break;
		case DIALOG_ID_CONFIRM_DELETE_LIST:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.confirm_delete_store)
			       .setCancelable(false)
			       .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			           @Override
					public void onClick(DialogInterface dialog, int id) {
			        	   MainActivity.this.lists.remove(MainActivity.this.selectedList);
			        	   
			        	   // TODO: also remove the associated items from the database

			        	   MainActivity.this.arrayAdapter.notifyDataSetChanged();
			        	   dialog.dismiss();
			           }
			       })
			       .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			           @Override
					public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			dialog = builder.create(); 
		}
		return dialog;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		switch (id) {
		case DIALOG_ID_LIST_SELECTED: 
			dialog.setTitle("How would you like to use the " + this.selectedList + " list?");
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.main_menu_add:
			this.showDialog(DIALOG_ID_ADD_LIST);
			break;
		}
		return true; // consume the menu click
	}

}