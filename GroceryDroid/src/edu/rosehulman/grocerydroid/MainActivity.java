package edu.rosehulman.grocerydroid;

import android.app.Activity;
import android.os.Bundle;

/**
 * The main screen displays all a user's shopping lists.
 * 
 * @author Matthew Boutell. 
 * 		   Created Mar 29, 2012.
 */
public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
//		Log.d(MyApplication.GD, "Creating single instance of Database Helper in GDActivity");
//		DatabaseHelper.createInstance(this.getApplicationContext(), true);
//        
//        Item item = new Item(17, "Bananas", 4, 2, 1.50f, 1, ItemUnitLabel.bag, true, 28, 4);
//		ItemDataAdapter ida = new ItemDataAdapter();
//		ida.open();
//		ida.insertItem(item);
//		ida.close();
    }
}