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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.mattboutell.grocerydroid2.model.Item;

/**
 * A custom dialog for alerts.
 * 
 * @author Matthew Boutell. Created Apr 18, 2012.
 */
public class ConfirmDeleteItemDialogFragment extends DialogFragment {

	// CONSIDER: I used a constructor and stored instance data.
	// The Android examples show dialogs as static. I'm not sure why they need
	// to be.
	private Item mItem;

	/**
	 * Sets this fragment's item to the given item.
	 * 
	 * @param itemToDelete
	 */
	public void setItem(Item itemToDelete) {
		mItem = itemToDelete;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
				// .setIcon(R.drawable.alert_dialog_icon)
				.setTitle(R.string.confirm_delete_item)
				.setCancelable(false)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								((ShoppingListActivity) getActivity())
										.deleteItem(mItem);
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
							}
						}).create();
	}
}
