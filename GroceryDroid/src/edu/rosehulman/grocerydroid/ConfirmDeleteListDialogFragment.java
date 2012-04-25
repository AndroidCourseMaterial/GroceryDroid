package edu.rosehulman.grocerydroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * A custom dialog for alerts.
 * 
 * @author Matthew Boutell. Created Apr 18, 2012.
 */
class ConfirmDeleteListDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
				// .setIcon(R.drawable.alert_dialog_icon)
				.setTitle(R.string.confirm_delete_list)
				.setCancelable(false)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								((MainActivity) getActivity()).deleteList();
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
