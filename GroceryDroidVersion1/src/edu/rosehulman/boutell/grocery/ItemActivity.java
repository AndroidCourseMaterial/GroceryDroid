package edu.rosehulman.boutell.grocery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Used to input or edit information about an item.
 * 
 * @author Matthew Boutell. Created Nov 14, 2011.
 */
public class ItemActivity extends Activity {

	private long itemId;
	
	// TODO: Use numeric keypad

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_information);
		
		EditText sizeBox = (EditText)findViewById(R.id.sizeBox);
		sizeBox.setText("1");
		
		
		Spinner unitSpinner = (Spinner) findViewById(R.id.unitSpinner);
		ArrayAdapter<ItemUnit> adapter;
		adapter = new ArrayAdapter<ItemUnit>(this,
				android.R.layout.simple_spinner_item, ItemUnit.values());
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		unitSpinner.setAdapter(adapter);

		Spinner numToStockSpinner = (Spinner) findViewById(R.id.numStockSpinner);
		ArrayAdapter<CharSequence> adapter1;
		adapter1 = ArrayAdapter
				.createFromResource(this, R.array.num_stock_options,
						android.R.layout.simple_spinner_item);
		adapter1
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		numToStockSpinner.setAdapter(adapter1);
		numToStockSpinner.setSelection(1);

		// If this activity was started with the purpose of editing an existing
		// item, then we use the item passed in the intent to populate the 
		// spinners and edit text boxes.
		Intent itemData = this.getIntent();
		if (itemData.hasExtra(Item.KEY_NAME)) {
			Item item = new Item(itemData);
			this.itemId = item.getId();

			EditText etName = (EditText)findViewById(R.id.nameBox);
			etName.setText(item.getName());

			EditText etPrice = (EditText)findViewById(R.id.priceBox);
			etPrice.setText(Float.toString(item.getPrice()));
			
			EditText etSize = (EditText)findViewById(R.id.sizeBox);
			etSize.setText(Float.toString(item.getSize()));
			
			unitSpinner.setSelection(item.getUnit().ordinal());
			numToStockSpinner.setSelection(item.getNToStock());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		this.getMenuInflater().inflate(R.menu.item_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		super.onOptionsItemSelected(menuItem);
		switch (menuItem.getItemId()) {
		case R.id.doneEditingItemMenu:
			Intent returnData = new Intent();
			EditText et = (EditText) findViewById(R.id.nameBox);
			String name = et.getText().toString();

			et = (EditText) findViewById(R.id.priceBox);
			float price = 0.0f;
			try {
				price = Float.parseFloat(et.getText().toString());
			} catch (NumberFormatException e) {
				price = 0.0f;
			}

			et = (EditText) findViewById(R.id.sizeBox);
			float size = 0.0f;
			try {
				size = Float.parseFloat(et.getText().toString());
			} catch (NumberFormatException e) {
				size = 0.0f;
			}

			Spinner spinner = (Spinner) findViewById(R.id.unitSpinner);
			int unitIndex = spinner.getSelectedItemPosition();

			spinner = (Spinner) findViewById(R.id.numStockSpinner);
			int numToStockIndex = spinner.getSelectedItemPosition();

			Item item = new Item(this.itemId, name, numToStockIndex, price, size, ItemUnit.values()[unitIndex]);
			item.addToIntent(returnData);
			
			setResult(Activity.RESULT_OK, returnData);
			finish();
		}
		return true;
	}

}
