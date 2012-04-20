package edu.rosehulman.grocerydroid;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A dialog for adding a list to the main screen.
 *
 * @author Matthew Boutell.
 *         Created Apr 11, 2012.
 */
class AddListDialogFragment extends DialogFragment {

	
	/**
	 * Returns a new instance of the AddList dialog
	 *
	 * @return A new instance of the AddList dialog.
	 */
	static AddListDialogFragment newInstance() {
		return new AddListDialogFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int theme = 0;//R.style.Theme_Sherlock_Dialog;
		int style = DialogFragment.STYLE_NO_TITLE;
		setStyle(style, theme);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_dialog_add_list, container, false);
		
		Button saveButton = (Button) view.findViewById(R.id.add_list_save_button);
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText et = (EditText)view.findViewById(R.id.add_list_name_edit_text);
				String newListName = et.getText().toString();
				if (!newListName.equals("")) {
					((MainActivity)getActivity()).addList(newListName);
					dismiss();
				}
			}
		});
		return view;
	}
}

