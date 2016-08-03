package com.mattboutell.grocerydroid2;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.mattboutell.grocerydroid2.model.ShoppingList;

/**
 * The main screen displays all the user's shopping lists.
 *
 * @author Matthew Boutell. Created Mar 29, 2012. Updated Oct 24, 2015.
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Used for passing data via an Intent
     */
    static final String KEY_SELECTED_LIST = "KEY_SELECTED_LIST";
    /**
     * Used for passing data via an Intent
     */
    static final String KEY_GO_SHOPPING = "GO SHOPPING";
    /**
     * To communicate that this activity should immediately go to the shopping
     * activity once the stock activity finishes.
     */
    static final int GO_SHOPPING = 1;
    private static final int REQUEST_STOCK = 0;
//    private ShoppingListDataAdapter mSlda;
//    private ItemDataAdapter mIda;
    //private ArrayList<ShoppingList> mShoppingLists = null;
    private ShoppingList mSelectedList;
    private MainShoppingListAdapterFB mAdapter;
    private TouchListView.DropListener onDrop = new TouchListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            ShoppingList item = (ShoppingList)mAdapter.getItem(from);
            mAdapter.removeListFromAdapterOnly(item);
            mAdapter.insertListToPositionInAdapterOnly(item, to);
            mAdapter.setPrioritiesToListOrder();
        }
    };

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_action_notepaper_and_shop);
        getSupportActionBar().setSubtitle(R.string.welcome);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment addListFragment = new AddListDialogFragment();
                addListFragment.show(getFragmentManager(), "add_list");
            }
        });

        TouchListView tlv = (TouchListView) findViewById(R.id.main_shopping_list_view);
//        mAdapter = new MainShoppingListAdapter(this,
//                R.layout.main_touch_list_row, mShoppingLists);
        mAdapter = new MainShoppingListAdapterFB(this);
        tlv.setAdapter(mAdapter);
        tlv.setDropListener(onDrop);

        tlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos,
                                    long id) {
                mSelectedList = (ShoppingList)mAdapter.getItem(pos);

                DialogFragment df = ChooseActionDialogFragment.newInstance();
                df.show(getFragmentManager(), "choose_action");
            }
        });

//        initializeDatabase();
        initializeShoppingLists();
    }

//    private void initializeDatabase() {
//        mSlda = new ShoppingListDataAdapter();
//        mSlda.open();
//
//        mIda = new ItemDataAdapter();
//        mIda.open();
//    }

    /**
     * Loads all of the shopping lists from the database
     */
    private void initializeShoppingLists() {
//        mShoppingLists = new ArrayList<>();
//        for (ShoppingList list : mSlda.getAllLists()) {
//            mShoppingLists.add(list);
//        }
//        Collections.sort(mShoppingLists, new CompareDisplayOrder());
        updateMainPrompt();
    }

    /**
     * Each list remembers its order in the display. This method updates that
     * order to match the order that the items appear on the screen (presumably
     * after they have been rearranged by the user), and updates the DB.
     */
    private void setListOrderToDisplayOrder() {
// TODO:
//        for (int i = 0; i < mAdapter.getCount(); i++) {
//            mAdapter.getItem(i).setDisplayIdx(i);
//        }

//		for (int i = 0; i < mShoppingLists.size(); i++) {
//			mShoppingLists.get(i).setDisplayIdx(i);
//		}
//        mSlda.updateAllLists(mShoppingLists);
    }

    /**
     * Use because the prompt is different if there are no lists.
     */
    void updateMainPrompt() {
        // TODO:
        TextView tv = (TextView) findViewById(R.id.main_screen_prompt);
        Log.d(MyApplication.GD, "There are " + mAdapter.getCount() + " list(s)");
        if (mAdapter.getCount() > 0) {
            tv.setText(R.string.main_screen_prompt_lists_present);
        } else {
            tv.setText(R.string.main_screen_prompt_default);
        }
    }

    /**
     * Adds the given list to the activity.
     *
     * @param listName
     */
    void addList(String listName) {
        mAdapter.addList(listName);
        updateMainPrompt();
    }

    /**
     * Deletes the list and all its items.
     */
    void deleteList() {
        mAdapter.removeList(mSelectedList);

//        mShoppingLists.remove(mSelectedList);
//        mSlda.deleteList(mSelectedList);
// TODO
//        mIda.deleteAllItemsWithListId(mSelectedList.getId());
//        mAdapter.notifyDataSetChanged();
        updateMainPrompt();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_level, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
//            case R.id.add_list:
//                DialogFragment addListFragment = new AddListDialogFragment();
//                addListFragment.show(getFragmentManager(), "add_list");
//                return true;
            case R.id.main_about:
                DialogFragment aboutFragment = new AboutDialogFragment();
                aboutFragment.show(getFragmentManager(), "main_about");
                return true;
            case R.id.main_help:
                DialogFragment helpFragment = new HelpDialogFragment();
                helpFragment.show(getFragmentManager(), "main_help");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Launches the stock activity.
     */
    void launchStockActivity() {
        Log.d(MyApplication.GD,
                String.format("Stock list for %s", mSelectedList));
        Intent intent = new Intent(MainActivity.this, StockActivity.class);
        intent.putExtra(MainActivity.KEY_SELECTED_LIST, mSelectedList.getId());
        startActivityForResult(intent, REQUEST_STOCK);
    }

    /**
     * Launches the shop activity.
     */
    void launchShopActivity() {
        Log.d(MyApplication.GD, String.format("Shop at %s", mSelectedList));
        Intent intent = new Intent(MainActivity.this, ShopActivity.class);
        intent.putExtra(MainActivity.KEY_SELECTED_LIST, mSelectedList.getId());
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        if (reqCode == REQUEST_STOCK && resultCode == RESULT_OK) {
            if (data.getIntExtra(KEY_GO_SHOPPING, -1) > 0) {
                launchShopActivity();
            }
        }
    }

//    private class CompareDisplayOrder implements Comparator<ShoppingList> {
//        @Override
//        public int compare(ShoppingList left, ShoppingList right) {
//            Long leftIdx = left.getDisplayIdx();
//            Long rightIdx = right.getDisplayIdx();
//            return leftIdx.compareTo(rightIdx);
//        }
//    }
}
