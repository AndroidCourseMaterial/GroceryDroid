/*
 * Copyright (C) 2012 Matthew Boutell 
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
package com.mattboutell.grocerydroid2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.mattboutell.grocerydroid2.model.Item;

/**
 * A dialog for adding an item to a shopping list, or adding a new item.
 *
 * @author Matthew Boutell. Created Apr 11, 2012. Updated Oct 24, 2015.
 */
public class ItemDialogFragment extends DialogFragment {
    private Item mItem;
    private Mode mMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NO_TITLE;
        setStyle(style, getTheme());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams")
        final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_item, null);
        builder.setView(view);
        AutoCompleteTextView nameBox = (AutoCompleteTextView) view
                .findViewById(R.id.item_name_box);
        nameBox.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        nameBox.setHint(R.string.item_fragment_name_hint);
        String[] foodNames = getResources().getStringArray(R.array.food_names);
        ArrayAdapter<String> autoAdapter = new ArrayAdapter<>(
                getActivity(), R.layout.dropdown_item, foodNames);
        nameBox.setAdapter(autoAdapter);

        EditText priceBox = (EditText) view.findViewById(R.id.item_price_box);
        priceBox.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        priceBox.setHint(R.string.item_fragment_price_hint);

        // first true : is signed, second one : is decimal
        priceBox.setKeyListener(new DigitsKeyListener(false, true));

        EditText sizeBox = (EditText) view
                .findViewById(R.id.item_unit_size_box);
        sizeBox.setText(getString(R.string.float_format, mItem.getUnitSize()));


        Spinner unitSpinner = (Spinner) view
                .findViewById(R.id.item_unit_label_spinner);
        ArrayAdapter<Item.UnitLabel> adapter;
        adapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_item, Item.UnitLabel.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);

        EditText numStockBox = (EditText) view
                .findViewById(R.id.item_num_stock);
        numStockBox.setText(getString(R.string.int_format, mItem.getNStock()));

        // If this activity was started with the purpose of editing an existing
        // item, then we use the item passed in the intent to populate the
        // spinners and edit text boxes.
        if (!mItem.getName().equals("")) {
            // TODO Make non-focusable, non-touchable so it doesn't kick off the drop down.
            // But it doesn't work!
            nameBox.setFocusable(false);
            nameBox.setFocusableInTouchMode(false);
            nameBox.setText(mItem.getName());
            // Reset the focus & touch
            nameBox.setFocusable(true);
            nameBox.setFocusableInTouchMode(true);
            priceBox.setText(getString(R.string.float_format, mItem.getPrice()));
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
                et.setText(getString(R.string.int_format, numStock));
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
                et.setText(getString(R.string.int_format, numStock));
            }
        });

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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

                String listKey = ((ShoppingListActivity)getActivity()).getShoppingListKey();

                mItem = new Item(mItem.getKey(), mItem.getShoppingListKey(), name,
                        numStock, mItem.getNBuy(), price, size, Item.UnitLabel
                        .values()[unitIndex], mItem.isBought(), mItem
                        .getStockIdx(), mItem.getShopIdx());
                if (mMode == Mode.ADD) {
                    ((ShoppingListActivity) getActivity()).getItemAdapter().addItem(mItem);
                } else if (mMode == Mode.EDIT) {
                    ((ShoppingListActivity) getActivity()).getItemAdapter().editItem(mItem);
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
            /**
             * This method will be invoked when a button in the dialog is clicked.
             *
             * @param dialog The dialog that received the click.
             * @param which  The button that was clicked (e.g.
             *               {@link DialogInterface#BUTTON1}) or the position
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mMode == Mode.EDIT) {
                    ConfirmDeleteItemDialogFragment df = new ConfirmDeleteItemDialogFragment();
                    df.setItem(mItem);
                    df.show(getActivity().getFragmentManager(),
                            "confirm");
                }
                // Otherwise, we are adding this item, so we don't need to
                // delete it.
                // TODO: Remove modes altogether once autocomplete works, since
                // every item here will exist and be beging added.
                // CONSIDER: at that point, I will need to make sure that items
                // that have a name only (from autocomplete) have been saved in
                // the DB
                // and have a unique ID so they can be deleted.
                dismiss();
            }
        });



        return builder.create();
    }


    private int getIntegerFromEditText(EditText et) {
        int num;
        try {
            num = Integer.parseInt(et.getText().toString());
        } catch (NumberFormatException e) {
            num = 0;
        }
        return num;
    }

    private float getFloatFromEditText(EditText et) {
        float num;
        try {
            num = Float.parseFloat(et.getText().toString());
        } catch (NumberFormatException e) {
            num = 0.0f;
        }
        return num;
    }

    void initializeItem(String shoppingListKey) {
        mItem = new Item(shoppingListKey);
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
     * Sets the field called 'mode' to the given value.
     *
     * @param mode The mode to set.
     */
    public void setMode(Mode mode) {
        this.mMode = mode;
    }

    /**
     * This fragment can be used to edit existing items or to create items.
     */
    enum Mode {
        /**
         * Add a new item
         */
        ADD,
        /**
         * Edit an existing item
         */
        EDIT
    }

}
