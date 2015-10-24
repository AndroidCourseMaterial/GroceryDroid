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
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;

/**
 * A dialog fragment for choosing an action from a list of actions.
 *
 * @author Matthew Boutell. Created Apr 11, 2012.
 */
public class ChooseActionDialogFragment extends DialogFragment {

    /**
     * Gets a new instance of the dialog fragment
     *
     * @return A new instance of the dialog fragment.
     */
    static ChooseActionDialogFragment newInstance() {
        return new ChooseActionDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_action_for_list);
        builder.setItems(R.array.list_selected_options,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity activity = (MainActivity) getActivity();
                        switch (which) {
                            case 0:
                                activity.launchStockActivity();
                                break;
                            case 1:
                                activity.launchShopActivity();
                                break;
                            case 2:
                                DialogFragment df = new ConfirmDeleteListDialogFragment();
                                df.show(activity.getFragmentManager(), "confirm");
                                break;
                        }
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
}
