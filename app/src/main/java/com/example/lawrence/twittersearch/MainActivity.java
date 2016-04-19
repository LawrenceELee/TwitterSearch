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



}

























