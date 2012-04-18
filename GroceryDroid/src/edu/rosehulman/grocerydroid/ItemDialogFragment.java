package edu.rosehulman.grocerydroid;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import edu.rosehulman.grocerydroid.model.Item;

/**
 * A dialog for adding an item to a shopping list, or adding a new item.
 * 
 * @author Matthew Boutell. Created Apr 11, 2012.
 */
public class ItemDialogFragment extends DialogFragment {
	private Item mItem;

	/**
	 * Gets a new instance of the dialog.
	 * 
	 * @return A new AddItemDialogFragment
	 */
	static ItemDialogFragment newInstance() {
		return new ItemDialogFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int theme = 0;// R.style.Theme_Sherlock_Dialog;
		int style = DialogFragment.STYLE_NO_TITLE;
		setStyle(style, theme);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_dialog_item,
				container, false);

		EditText sizeBox = (EditText) view
				.findViewById(R.id.item_unit_size_box);
		sizeBox.setText("" + mItem.getUnitSize());

		Spinner unitSpinner = (Spinner) view
				.findViewById(R.id.item_unit_label_spinner);
		ArrayAdapter<Item.UnitLabel> adapter;
		adapter = new ArrayAdapter<Item.UnitLabel>(this.getActivity(),
				android.R.layout.simple_spinner_item, Item.UnitLabel.values());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		unitSpinner.setAdapter(adapter);

		EditText numStockBox = (EditText) view
				.findViewById(R.id.item_num_stock);
		numStockBox.setText("" + mItem.getNStock());

		// If this activity was started with the purpose of editing an existing
		// item, then we use the item passed in the intent to populate the
		// spinners and edit text boxes.
		if (!mItem.getName().equals("")) {
			EditText nameBox = (EditText) view.findViewById(R.id.item_name_box);
			nameBox.setText(mItem.getName());

			// TODO: complete this for starting with activity
			// EditText priceBox = (EditText) findViewById(R.id.priceBox);
			// etPrice.setText(Float.toString(item.getPrice()));
			//
			// EditText etSize = (EditText) findViewById(R.id.sizeBox);
			// etSize.setText(Float.toString(item.getSize()));
			//
			// unitSpinner.setSelection(item.getUnit().ordinal());
			// numToStockSpinner.setSelection(item.getNToStock());
		}

		Button incrementNumStockButton = (Button) view
				.findViewById(R.id.item_increment_num_stock);
		incrementNumStockButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText et = (EditText) view.findViewById(R.id.item_num_stock);
				int numStock = getIntegerFromEditText(et);
				numStock++;
				et.setText(numStock + "");
			}
		});

		Button decrementNumStockButton = (Button) view
				.findViewById(R.id.item_decrement_num_stock);
		decrementNumStockButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText et = (EditText) view.findViewById(R.id.item_num_stock);
				int numStock = getIntegerFromEditText(et);
				numStock--;
				et.setText(numStock + "");
			}
		});

		Button saveButton = (Button) view.findViewById(R.id.item_save_button);
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText et = (EditText) view.findViewById(R.id.item_name_box);
				String name = et.getText().toString();

				et = (EditText) view.findViewById(R.id.item_price_box);
				float price = getFloatFromEditText(et);

				et = (EditText) view.findViewById(R.id.item_unit_size_box);
				float size = getFloatFromEditText(et);

				Spinner spinner = (Spinner) view
						.findViewById(R.id.item_unit_label_spinner);
				int unitIndex = spinner.getSelectedItemPosition();

				et = (EditText) view.findViewById(R.id.item_num_stock);
				int numStock = getIntegerFromEditText(et);

				mItem = new Item(mItem.getId(), mItem.getListId(), name,
						numStock, mItem.getNBuy(), price, size, Item.UnitLabel
								.values()[unitIndex], mItem.isBought(), mItem
								.getStockIdx(), mItem.getShopIdx());
				((ShoppingListActivity) getActivity()).addItem(mItem);
				dismiss();
			}
		});

		Button cancelButton = (Button) view
				.findViewById(R.id.item_cancel_button);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		return view;
	}

	private int getIntegerFromEditText(EditText et) {
		int num = 0;
		try {
			num = Integer.parseInt(et.getText().toString());
		} catch (NumberFormatException e) {
			num = 0;
		}
		return num;
	}

	private float getFloatFromEditText(EditText et) {
		float num = 0.0f;
		try {
			num = Float.parseFloat(et.getText().toString());
		} catch (NumberFormatException e) {
			num = 0.0f;
		}
		return num;
	}

	/**
	 * Creates an item with the given listId.
	 * 
	 * @param listId
	 */
	void initializeItem(long listId) {
		mItem = new Item(listId);
	}
}
