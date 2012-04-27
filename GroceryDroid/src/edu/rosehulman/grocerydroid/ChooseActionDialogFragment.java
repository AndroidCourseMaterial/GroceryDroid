package edu.rosehulman.grocerydroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * A dialog fragment for choosing an action from a list of actions.
 * 
 * @author Matthew Boutell. Created Apr 11, 2012.
 */
class ChooseActionDialogFragment extends DialogFragment {

	/**
	 * Gets a new instance of the dialog fragment
	 * 
	 * @return A new instance of the dialog fragment.
	 */
	static ChooseActionDialogFragment newInstance() {
		return new ChooseActionDialogFragment();
	}

	// CONSIDER: This doesn't appear to do anything.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int theme = R.style.Theme_Sherlock_Dialog;
		int style = DialogFragment.STYLE_NO_TITLE;
		setStyle(style, theme);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// builder.setIcon(R.drawable.alert_dialog_icon)
		builder.setTitle(R.string.choose_action_for_list);
		builder.setItems(R.array.list_selected_options,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MainActivityNonTouch activity = (MainActivityNonTouch) getActivity();
						switch (which) {
						case 0:
							activity.launchStockActivity();
							break;
						case 1:
							activity.launchShopActivity();
							break;
						case 2:
							DialogFragment df = new ConfirmDeleteListDialogFragment();
							df.show(activity.getSupportFragmentManager(),
									"confirm");
							break;
						}
						dialog.dismiss();
					}
				});
		return builder.create();
	}
}
