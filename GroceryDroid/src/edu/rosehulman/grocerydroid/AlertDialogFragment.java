package edu.rosehulman.grocerydroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * A custom dialog for alerts.
 *
 * @author boutell.
 *         Created Apr 18, 2012.
 */
public class AlertDialogFragment extends DialogFragment {

//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		int theme = R.style.Theme_Sherlock_Dialog;
//		int style = DialogFragment.STYLE_NO_TITLE;
//		setStyle(style, theme);
//	}

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
                // setMessage()
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
