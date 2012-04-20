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
	private Mode mMode;

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
			EditText priceBox = (EditText)view.findViewById(R.id.item_price_box);
			priceBox.setText(Float.toString(mItem.getPrice()));
			
			unitSpinner.setSelection(mItem.getUnitLabel().ordinal());
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
				if (mMode == Mode.ADD) {
					((ShoppingListActivity) getActivity()).addItem(mItem);
				} else if (mMode == Mode.EDIT) {
					((ShoppingListActivity)getActivity()).updateItem(mItem);
				} else {
					// shouldn't get here.
				}
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

		Button deleteButton = (Button) view
				.findViewById(R.id.item_delete_button);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mMode == Mode.EDIT) { 
			        ConfirmDeleteItemDialogFragment df = new ConfirmDeleteItemDialogFragment();
			        df.setItem(mItem);
					df.show(getActivity().getSupportFragmentManager(),
							"confirm");
				} 
				// Otherwise, we are adding this item, so we don't need to delete it.
				// TODO: Remove modes altogether once autocomplete works, since 
				// every item here will exist and be beging added. 
				// CONSIDER: at that point, I will need to make sure that items 
				// that have a name only (from autocomplete) have been saved in the DB
				// and have a unique ID so they can be deleted.
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

	/**
	 * Sets this fragment's item to the given item.
	 * 
	 * @param item
	 */
	void setItem(Item item) {
		mItem = item;
	}

	/**
	 * Returns the value of the field called 'mode'.
	 * 
	 * @return Returns the mode.
	 */
	public Mode getMode() {
		return this.mMode;
	}

	/**
	 * Sets the field called 'mode' to the given value.
	 * 
	 * @param mode
	 *            The mode to set.
	 */
	public void setMode(Mode mode) {
		this.mMode = mode;
	}

	/**
	 * This fragment can be used to edit existing items or to create items.
	 */
	enum Mode {
		/** Add a new item */
		ADD,
		/** Edit an existing item */
		EDIT
	}

}
