package com.mattboutell.grocerydroid2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mattboutell.grocerydroid2.model.ShoppingList;

import java.util.ArrayList;

/**
 * The main screen displays all the user's shopping lists.
 *
 * @author Matthew Boutell. Created Mar 29, 2012. Updated Oct 24, 2015.
 */
public class MainActivity extends AppCompatActivity {
//    private ShoppingListDataAdapter mSlda;
//    private ItemDataAdapter mIda;
    private ArrayList<ShoppingList> mShoppingLists = null;
    private ShoppingList mSelectedList;
//    private MainShoppingListAdapter mAdapter;
    private static final int REQUEST_STOCK = 0;

    /** Used for passing data via an Intent */
    static final String KEY_SELECTED_LIST = "KEY_SELECTED_LIST";

    /** Used for passing data via an Intent */
    static final String KEY_GO_SHOPPING = "GO SHOPPING";

    /**
     * To communicate that this activity should immediately go to the shopping
     * activity once the stock activity finishes.
     */
    static final int GO_SHOPPING = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
