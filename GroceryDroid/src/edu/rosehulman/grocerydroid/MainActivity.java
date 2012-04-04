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

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * The main screen displays all a user's shopping lists.
 * 
 * @author Matthew Boutell. Created Mar 29, 2012.
 */
public class MainActivity extends SherlockActivity {
	private static final int DIALOG_ID_ADD_SHOPPING_LIST = 0;
	private ShoppingListDataAdapter slda;
	private ItemDataAdapter ida;
	private ArrayList<ShoppingList> shoppingLists = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock);// _ForceOverflow); // ABS, must come
											// first
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getSupportActionBar().setIcon(R.drawable.ic_list); // needs to be in
															// hdpi folder
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setSubtitle("Welcome");

		initializeDatabase();

		initializeShoppingLists();
	}

	private void initializeShoppingLists() {
		this.shoppingLists = new ArrayList<ShoppingList>();
		for (ShoppingList list : slda.getAllLists()) {
			this.shoppingLists.add(list);
		}
		updateMainPrompt();
	}

	private void updateMainPrompt() {
		if (this.shoppingLists.size() > 0) {
			TextView tv = (TextView)findViewById(R.id.main_screen_prompt);
			tv.setText(R.string.main_screen_prompt_lists_present);
		}
	}

	private void initializeDatabase() {
		this.slda = new ShoppingListDataAdapter();
		this.slda.open();

		this.ida = new ItemDataAdapter();
		this.ida.open();
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
					ShoppingList newList = new ShoppingList(et.getText()
							.toString());
					slda.insertList(newList);
					// TODO: add list to screen and refresh
					// It does save it to the DB. :)

					updateMainPrompt();
					addListDialog.dismiss();
				}
			});
			dialog = addListDialog;
			break;
		}
		return dialog;
	}

}