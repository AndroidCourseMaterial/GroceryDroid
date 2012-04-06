/*
 * Copyright (C) 2010 The Android Open Source Project 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package edu.rosehulman.grocerydroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import edu.rosehulman.grocerydroid.db.ItemDataAdapter;
import edu.rosehulman.grocerydroid.db.ShoppingListDataAdapter;
import edu.rosehulman.grocerydroid.model.ShoppingList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import java.util.ArrayList;

/**
 * The main screen displays all the user's shopping lists.
 * 
 * @author Matthew Boutell. Created Mar 29, 2012.
 */
public class MainActivity extends SherlockActivity {
	private static final int DIALOG_ID_ADD_SHOPPING_LIST = 0;
	private static final int DIALOG_ID_LIST_SELECTED = 1;
	private static final int DIALOG_ID_CONFIRM_DELETE_LIST = 2;
	private ShoppingListDataAdapter mSlda;
	private ItemDataAdapter mIda;
	private ArrayList<ShoppingList> mShoppingLists = null;
	private ShoppingList mSelectedList;
	// private ArrayAdapter<String> mAdapter;
	private MainShoppingListAdapter mAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_ForceOverflow); // ABS, must come first
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getSupportActionBar().setIcon(R.drawable.ic_list); // needs to be in
															// hdpi folder
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setSubtitle("Welcome");

		initializeDatabase();
		initializeShoppingLists();

		ListView lv = (ListView) findViewById(R.id.main_shopping_list_view);
		mAdapter = new MainShoppingListAdapter(this, R.layout.main_list,
				mShoppingLists);
		lv.setAdapter(mAdapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {
				mSelectedList = MainActivity.this.mShoppingLists.get(pos);
				showDialog(DIALOG_ID_LIST_SELECTED);
			}
		});
	}

	private void initializeDatabase() {
		this.mSlda = new ShoppingListDataAdapter();
		this.mSlda.open();

		this.mIda = new ItemDataAdapter();
		this.mIda.open();
	}

	private void initializeShoppingLists() {
		this.mShoppingLists = new ArrayList<ShoppingList>();
		for (ShoppingList list : mSlda.getAllLists()) {
			this.mShoppingLists.add(list);
		}
		updateMainPrompt();
	}

	private void updateMainPrompt() {
		TextView tv = (TextView) findViewById(R.id.main_screen_prompt);
		Log.d(MyApplication.GD, "There are " + mShoppingLists.size() + " lists");
		if (this.mShoppingLists.size() > 0) {
			tv.setText(R.string.main_screen_prompt_lists_present);
		} else {
			tv.setText(R.string.main_screen_prompt_default);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.top_level, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		case R.id.add_list:
			this.showDialog(DIALOG_ID_ADD_SHOPPING_LIST);
			return true;
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// CONSIDER: Dialogs are now deprecated. I am supposed to use a
	// DialogFragment. For pre-Honeycomb phones (like mine currently), it would
	// have to be accessible via the compatibility library. Do I want to mix
	// that and Sherlock?
	//
	// ActionBarSherlock says it provides a SherlockDialogFragment
	// http://actionbarsherlock.com/faq.html
	// but I can't find it in the library.
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case DIALOG_ID_ADD_SHOPPING_LIST:
			final Dialog addListDialog = new Dialog(this);
			addListDialog.setContentView(R.layout.dialog_add_list);
			// customDialog.setTitle("Rose-Hulman");

			Button saveButton = (Button) addListDialog
					.findViewById(R.id.add_list_save_button);
			saveButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					EditText et = (EditText) addListDialog
							.findViewById(R.id.add_list_name_edit_text);
					String newListName = et.getText().toString();
					if (!newListName.equals("")) {
						ShoppingList newList = new ShoppingList(newListName);
						mSlda.insertList(newList);
						mShoppingLists.add(newList);
						mAdapter.notifyDataSetChanged();
						updateMainPrompt();
						addListDialog.dismiss();
					}
				}
			});
			dialog = addListDialog;
			break;
		case DIALOG_ID_LIST_SELECTED:
			Log.d(MyApplication.GD, "List selected");
			AlertDialog.Builder listSelectedBuilder = new AlertDialog.Builder(
					this);
			listSelectedBuilder
					.setTitle("What would you like to do with this list?");
			listSelectedBuilder.setItems(R.array.list_selected_options,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = null;
							switch (which) {
							case 0:
								Log.d(MyApplication.GD, String.format(
										"Stock list for %s",
										mSelectedList.getName()));
								// intent = new Intent(MainActivity.this,
								// StockActivity.class);
								// intent.putExtra(MainActivity.KEY_SELECTED_LIST,
								// MainActivity.this.selectedList);
								// startActivity(intent);
								break;
							case 1:
								Log.d(MyApplication.GD, String.format(
										"Shop at %s", mSelectedList));
								// intent = new Intent(MainActivity.this,
								// ShopActivity.class);
								// intent.putExtra(MainActivity.KEY_SELECTED_LIST,
								// MainActivity.this.selectedList);
								// startActivity(intent);
								break;
							case 2:
								MainActivity.this
										.showDialog(DIALOG_ID_CONFIRM_DELETE_LIST);
							}
							dialog.dismiss();
						}
					});
			dialog = listSelectedBuilder.create();
			break;
		case DIALOG_ID_CONFIRM_DELETE_LIST:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.confirm_delete_list)
					.setCancelable(false)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface di,
										int dialogId) {
									mShoppingLists.remove(mSelectedList);
									mSlda.deleteList(mSelectedList);
									// TODO: test this once I can add items to
									// lists.
									mIda.deleteAllItemsWithListId(mSelectedList
											.getId());
									mAdapter.notifyDataSetChanged();
									updateMainPrompt();
									di.dismiss();
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface di,
										int dialogId) {
									di.cancel();
								}
							});

			dialog = builder.create();
		}
		return dialog;
	}
}