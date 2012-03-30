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

import android.app.Activity;
import android.os.Bundle;
import edu.rosehulman.grocerydroid.db.DatabaseHelper;
import edu.rosehulman.grocerydroid.db.ItemDataAdapter;
import edu.rosehulman.grocerydroid.db.ShoppingListDataAdapter;

/**
 * The main screen displays all a user's shopping lists.
 * 
 * @author Matthew Boutell. Created Mar 29, 2012.
 */
public class MainActivity extends Activity {
	private ShoppingListDataAdapter slda;
	private ItemDataAdapter ida;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initializeDatabase();

	}

	private void initializeDatabase() {
		DatabaseHelper.createInstance(this);

		this.slda = new ShoppingListDataAdapter();
		this.slda.open();

		this.ida = new ItemDataAdapter();
		this.ida.open();
	}

}