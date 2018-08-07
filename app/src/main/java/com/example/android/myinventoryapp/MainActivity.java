package com.example.android.myinventoryapp;


import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.content.CursorLoader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_PNAME;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_PRICE;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_QUANTITY;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.CONTENT_URI;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry._ID;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int ITEM_LOADER = 0;
    InventoryCursorAdapter mAdapter;


    @OnClick(R.id.add_button)
    public void add() {
        Intent addIntent = new Intent(MainActivity.this, AddItem.class);
        startActivity(addIntent);
    }

    @OnClick(R.id.supp_button)
    public void onClick(View view) {
        Intent suppIntent = new Intent(MainActivity.this, SuppActivity.class);
        startActivity(suppIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        ListView db_list = (ListView) findViewById(R.id.database);
         mAdapter = new InventoryCursorAdapter(this, null);
        db_list.setAdapter(mAdapter);

        db_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent editIntent = new Intent(MainActivity.this, AddItem.class);
                Uri currentItemUri = ContentUris.withAppendedId(CONTENT_URI, id);
                editIntent.setData(currentItemUri);
                startActivity(editIntent);
            }});


        getLoaderManager().initLoader(ITEM_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String[] projection = {
                _ID,
                COLUMN_PNAME,
                COLUMN_PRICE,
                COLUMN_QUANTITY
        };
        return new CursorLoader(this, CONTENT_URI,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


}