package me.kevinnguyen.omgandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    TextView mainTextView;
    Button mainButton;
    EditText mainEditText;
    ListView mainListView;
    ArrayAdapter mArrayAdapter;
    ArrayList mNameList = new ArrayList();
    ShareActionProvider mShareActionProvider;

    // persistent data
    private static final String PREFS = "prefs";
    private static final String PREF_NAME = "name";
    SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Access the TextView defined in layout XML
        // and then set its text
        mainTextView = (TextView) findViewById(R.id.main_textview);
        mainTextView.setText("Set in Java");

        // 2. Access the button defined in layout xml
        // and listen for it here
        mainButton = (Button) findViewById(R.id.main_button);
        mainButton.setOnClickListener(this);

        // 3. Access the EditText defined in layout XML
        mainEditText = (EditText) findViewById(R.id.main_edittext);

        // 4. Access ListView
        mainListView = (ListView) findViewById(R.id.main_listview);

        // Create an ArrayAdapter for the ListView
        mArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, mNameList);

        // Set the ListView to use the ArrayAdapter
        mainListView.setAdapter(mArrayAdapter);

        // 5. Set activity to respond to items being selected
        mainListView.setOnItemClickListener(this);

        // 7. Greet user or ask for their name if it's new
        displayWelcome();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Access the Share Item defined in menu XML
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);

        // Access the object responsible for putting together the sharing sub menu
        if (shareItem != null) {
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        }

        // Create an intent to share your content
        setShareIntent();
        return true;
    }

    private void setShareIntent() {

        if (mShareActionProvider != null) {
            // create an intent with the contents of the textView
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Android Development");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mainTextView.getText());

            // provider should know to work with Intent
            mShareActionProvider.setShareIntent(shareIntent);

        }
    }


    @Override
    public void onClick(View v) {
        // Take what was typed into the EditText and
        // Use it in TextView
        mainTextView.setText(mainEditText.getText().toString() +
                " is learning Android Development! Yay!");

        // Add that value to the listView
        mNameList.add(mainEditText.getText().toString());
        mArrayAdapter.notifyDataSetChanged();

        // 6. The text you'd like to share has changed
        // update
        setShareIntent();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // grab item's position and contents to the console in Debug
        Log.d("OMG Android", position +": " + mNameList.get(position));
    }

    public void displayWelcome() {

        // Access the device's key-value storage
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);

        // Read the user's name which is empty if not found
        String name = mSharedPreferences.getString(PREF_NAME, "");

        if (name.length() > 0) {

            // If the name is valid, display a toast welcoming them
            Toast.makeText(this, "Welcome back, " + name + "!", Toast.LENGTH_LONG).show();
        } else {

            // show dialogue to ask for name
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Hello");
            alert.setMessage("What is your name?");

            // Create EditText for entry
            final EditText input = new EditText(this);
            alert.setView(input);

            // Make an OK button to save the name
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Grab the EditText's input
                    String inputName = input.getText().toString();

                    // put it into memory
                    SharedPreferences.Editor e = mSharedPreferences.edit();
                    e.putString(PREF_NAME, inputName);
                    e.commit();

                    // welcome new user
                    Toast.makeText(getApplicationContext(), "Welcome" + inputName
                            + "!", Toast.LENGTH_LONG).show();
                }
                });
            // Make a "Cancel" button that dismisses alert
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton){}

            });
            alert.show();

        }
    }
}
