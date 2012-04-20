package edu.rosehulman.grocerydroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * A custom dialog for alerts.
 *
 * @author Matthew Boutell.
 *         Created Apr 18, 2012.
 */
class AlertDialogFragment extends DialogFragment {

	// CONSIDER: Both this and my confirmDeleteItemDialogFragment are the same
	// except for a title and the listener in the positive button.
	// I could pass them in on construction and reuse this class.
	
    /**
     * Creates a new instance of the AlertDialogFragment with the given title.
     *
     * @param title
     * @return A new instance of the AlertDialogFragment with the given title.
     */
    public static AlertDialogFragment newInstance(int title) {
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        return new AlertDialog.Builder(getActivity())
//	                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                	new DialogInterface.OnClickListener() {
                        @Override
						public void onClick(DialogInterface dialog, int whichButton) {
                            ((MainActivity)getActivity()).deleteList();
                            dialog.dismiss();
                        }
                    }
                )
                .setNegativeButton(R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
						public void onClick(DialogInterface dialog, int whichButton) {
                        	dialog.cancel();
                        }
                    }
                )
                .create();
    }
}
