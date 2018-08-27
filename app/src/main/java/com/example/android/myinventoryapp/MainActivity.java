package com.example.android.myinventoryapp;


import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.content.CursorLoader;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_PNAME;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_PRICE;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_QUANTITY;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.CONTENT_URI;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry._ID;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ITEM_LOADER = 0;
    InventoryCursorAdapter mAdapter;
    @BindView(R.id.database)
    ListView db_list;
    @BindView(R.id.drawer_layout)
    DrawerLayout dl;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nv)
    NavigationView nv;
    int quant;


    @OnItemClick(R.id.database)
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent editIntent = new Intent(MainActivity.this, DetailActivity.class);
        Uri currentItemUri = ContentUris.withAppendedId(CONTENT_URI, id);
        editIntent.setData(currentItemUri);
        startActivity(editIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mAdapter = new InventoryCursorAdapter(this, null);
        db_list.setAdapter(mAdapter);
        db_list.setEmptyView(emptyView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        //setting visibility of element of drawer menu
        Menu nav_Menu = nv.getMenu();
        nav_Menu.findItem(R.id.nav_delete1).setVisible(false);
        nav_Menu.findItem(R.id.nav_edit).setVisible(false);
        nav_Menu.findItem(R.id.home).setVisible(false);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_add: {
                        Intent addIntent = new Intent(MainActivity.this, AddItem.class);
                        startActivity(addIntent);
                        break;
                    }
                    case R.id.nav_delete:
                        deleteConfirm();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
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
        if (cursor.moveToFirst()) {
            int quantCI = cursor.getColumnIndex(COLUMN_QUANTITY);
            quant = cursor.getInt(quantCI);
        }

        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    //setting menu for navigation drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dl.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //confirmation and deleting all items
    private void deleteConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.deleteAll);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // after accepting delete everything
                getContentResolver().delete(CONTENT_URI, null, null);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //finishing dialog after canceling
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
