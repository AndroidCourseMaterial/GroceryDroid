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
import android.view.View;
import android.widget.EditText;

/**
 * A dialog for adding a list to the main screen.
 *
 * @author Matthew Boutell. Created Apr 11, 2012.
 */
public class AddListDialogFragment extends DialogFragment {
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		int theme = 0;
//		int style = DialogFragment.STYLE_NO_TITLE;
//		setStyle(style, theme);
//	}


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_list_prompt);
        final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_add_list, null);
        builder.setView(view);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            /**
             * This method will be invoked when a button in the dialog is clicked.
             *
             * @param dialog The dialog that received the click.
             * @param which  The button that was clicked (e.g.
             *               {@link DialogInterface#BUTTON1}) or the position
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText et = (EditText) view.findViewById(R.id.add_list_name_edit_text);
                String newListName = et.getText().toString();
                if (!newListName.equals("")) {
                    ((MainActivity) getActivity()).addList(newListName);
                    dismiss();
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        final View view = inflater.inflate(R.layout.fragment_dialog_add_list,
//                container, false);
//
//        Button saveButton = (Button) view
//                .findViewById(R.id.add_list_save_button);
//        Button cancelButton = (Button) view
//                .findViewById(R.id.add_list_cancel_button);
//        saveButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                EditText et = (EditText) view
//                        .findViewById(R.id.add_list_name_edit_text);
//                String newListName = et.getText().toString();
//                if (!newListName.equals("")) {
//                    ((MainActivity) getActivity()).addList(newListName);
//                    dismiss();
//                }
//            }
//        });
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        return view;
//    }
}
