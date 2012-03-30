package edu.rosehulman.grocerydroid;

import java.util.ArrayList;

import edu.rosehulman.grocerydroid.db.DatabaseHelper;
import edu.rosehulman.grocerydroid.db.ItemDataAdapter;
import edu.rosehulman.grocerydroid.db.ShoppingListDataAdapter;
import edu.rosehulman.grocerydroid.model.Item;
import edu.rosehulman.grocerydroid.model.ItemUnitLabel;
import edu.rosehulman.grocerydroid.model.ShoppingList;
import android.app.Activity;
import android.os.Bundle;

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