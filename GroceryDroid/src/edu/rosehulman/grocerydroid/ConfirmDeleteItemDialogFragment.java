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
 * @author Matthew Boutell.
 *         Created Apr 18, 2012.
 */
public class ConfirmDeleteItemDialogFragment extends DialogFragment {

	private static Item item;
	
    /**
     * Creates a new instance of the AlertDialogFragment with the given title.
     *
     * @param title
     * @return A new instance of the AlertDialogFragment with the given title.
     */
    public static ConfirmDeleteItemDialogFragment newInstance() {
        ConfirmDeleteItemDialogFragment frag = new ConfirmDeleteItemDialogFragment();
        return frag;
    }

    /**
     * Sets this fragment's item to the given item.
     *
     * @param itemToDelete
     */
    protected static void setItem(Item itemToDelete) {
    	ConfirmDeleteItemDialogFragment.item = itemToDelete;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
//	                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle(R.string.confirm_delete_item)
                // setMessage()
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
						public void onClick(DialogInterface dialog, int whichButton) {
                            ((ShoppingListActivity)getActivity()).deleteItem(item);
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
