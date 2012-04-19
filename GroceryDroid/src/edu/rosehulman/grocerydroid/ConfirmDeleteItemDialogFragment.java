package edu.rosehulman.grocerydroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import edu.rosehulman.grocerydroid.model.Item;

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
	protected void setItem(Item itemToDelete) {
		mItem = itemToDelete;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
				// .setIcon(R.drawable.alert_dialog_icon)
				.setTitle(R.string.confirm_delete_item)
				// setMessage()
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
