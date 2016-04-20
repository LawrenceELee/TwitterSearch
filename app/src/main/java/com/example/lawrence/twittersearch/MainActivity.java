package com.example.lawrence.twittersearch;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // name of the shared preferences file
    private static final String PREVIOUS_SEARCHES = "previous_searches";

    private EditText mQueryEditText;
    private EditText mTagEditText;
    private FloatingActionButton mSaveFloatingActionButton;
    private SharedPreferences mSavedSearches; // user's favorite searches
    private List<String> mTags; // list of tags for saved searches
    private SearchesAdapter mSearchesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // bind all view widgets with controller
        mQueryEditText = ((TextInputLayout) findViewById(R.id.queryTextInputLayout)).getEditText();
        mQueryEditText.addTextChangedListener(textWatcher);

        mTagEditText = ((TextInputLayout) findViewById(R.id.tagTextInputLayout)).getEditText();
        mQueryEditText.addTextChangedListener(textWatcher);

        // get shared prefs containing previous saved searches
        // mode_private means only this app can access it, other app can't access this file.
        mSavedSearches = getSharedPreferences(PREVIOUS_SEARCHES, MODE_PRIVATE);

        // store the saved tags in ArrayList then sort them (lower case and capital case are same)
        mTags = new ArrayList<>(mSavedSearches.getAll().keySet());
        Collections.sort(mTags, String.CASE_INSENSITIVE_ORDER);

        // ref to RecyclerView and configure it
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // config to use linear layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create recycler view adapter to bind tags (in mTags) to RecyclerView
        mSearchesAdapter = new SearchesAdapter(mTags, itemClickListener, itemLongClickListener);
        recyclerView.setAdapter(mSearchesAdapter);

        // item decorator to put line dividers between items
        recyclerView.addItemDecoration(new ItemDivider(this));

        // register listener to save or edit search for FAB
        mSaveFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mSaveFloatingActionButton.setOnClickListener(saveButtonListener);
        updateSaveFAB(); // hide button because EditTexts are initially empty.
    }

    // method to hide/show FAB based on EditTexts' input/empty
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            updateFAB();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            /* not used */
        }

        @Override
        public void afterTextChanged(Editable editable) {
            /* not used */
        }
    };

    // method to hide/show FAB
    private void updateFAB() {
        if( mQueryEditText.getText().toString().isEmpty() ){
            mTagEditText.getText().toString().isEmpty();
            mSaveFloatingActionButton.hide();
        } else {
            mSaveFloatingActionButton.show();
        }
    }

    // saveButtonListener save a tag-query pair into SharedPreferences
    private final OnClickListener saveButtonListener = new OnClickListener() {

        // add/upate search if neither query nor tag is empty
        @Override
        public void onClick(View view) {
            String query = mQueryEditText.getText().toString();
            String tag = mTagEditText.getText().toString();

            if( !query.isEmpty() && !tag.isEmpty() ){
                // hide virtual keyboard
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(view.getWindowToken(), 0);

                addTaggedSearch(tag, query); // add/update the search
                // clear edit texts
                mQueryEditText.setText("");
                mTagEditText.setText("");
                mQueryEditText.requestFocus();
            }
        }
    };

    // method to add new search to shared prefs file then refresh all buttons
    private void addTaggedSearch(String tag, String query) {
        // get shared prefs editor
        SharedPreferences.Editor editor = mSavedSearches.edit();
        editor.putString(tag, query);
        editor.apply();

        // if tag is new, add to and sort tags, then display updated list
        if( !mTags.contains(tag) ){
            mTags.add(tag);
            Collections.sort(mTags, String.CASE_INSENSITIVE_ORDER);
            mSearchesAdapter.notifyDataSetChanged();
        }
    }

    // click listener (for short press) to launch implicit? intent
    // for internet browser to display result of search
    private final OnClickListener itemClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            // get query string from EditText and create a URL representation
            String tag = ((TextView) view).getText().toString();
            String urlString = getString(R.string.search_URL)
                    + Uri.encode(mSavedSearches.getString(tag, ""), "UTF-8");

            // create an intent to launch browser
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
            startActivity(browserIntent);
        }
    };

    // click listener (for long press) to bring up context menu to edit, share, etc. searches
    private final OnLongClickListener itemLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            // get tag for previous search
            final String tag = ((TextView) view).getText().toString();

            // create alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.share_edit_delete_title, tag));

            builder.setItems(R.array.dialog_items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i) {
                        case 0:     // share
                            shareSearch(tag);
                            break;
                        case 1:     // edit
                            mTagEditText.setText(tag);
                            mQueryEditText.setText(mSavedSearches.getString(tag, ""));
                            break;
                        case 2:     // delete
                            deleteSearch(tag);
                            break;
                    } // end switch
                }
            }); // end setItems anonymous-inner-class object

            // set AlertDialog's negative Button
            builder.setNegativeButton(getString(R.string.cancel), null);

            // display alert dialog
            builder.create().show();

            return true;
        }
    };

    // method to delete a search after confirming on alert dialog
    private void deleteSearch(final String tag) {   // why final?
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.confirm_message, tag));

        // config negative (cancel) button
        builder.setNegativeButton(getString(R.string.cancel), null);

        // config positive (delete) button
        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                mTags.remove(tag);

                // get SharedPrefs editor to remove save search
                SharedPreferences.Editor editor = mSavedSearches.edit();
                editor.remove(tag);
                editor.apply();

                // rebind tags to RecyclerView to show updated list
                mSearchesAdapter.notifyDataSetChanged();
            }
        });

        builder.create().show();
    }

    // method to let user choose app for sharing url (implicit intent?)
    private void shareSearch(String tag) {
        String urlString = getString(R.string.search_URL) +
                Uri.encode(mSavedSearches.getString(tag, ""), "UTF-8");

        // create intent to share
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        // putExtra() is one way to send data from an activity to another activity.
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_search));
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message, urlString));
        shareIntent.setType("text/plain");

        // display apps that can share plain text
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_search)));
    }


}

























