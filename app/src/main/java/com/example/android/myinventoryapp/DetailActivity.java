package com.example.android.myinventoryapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_PHONE;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_PNAME;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_PRICE;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_QUANTITY;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_SNAME;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.CONTENT_URI;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry._ID;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Uri mCurrentItemUri;
    int ID;
    int quant;
    String phone;
    private static final int EXISTING_ITEM_LOADER = 0;
    @BindView(R.id.dpname)
    TextView tvname;
    @BindView(R.id.dprice)
    TextView tvprice;
    @BindView(R.id.dquant)
    TextView tvquant;
    @BindView(R.id.dsupp)
    TextView tvsupp;
    @BindView(R.id.dphone)
    TextView tvphone;
    @BindView(R.id.drawer_layout)
    DrawerLayout dl;
    @Nullable
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nv)
    NavigationView nv;

    @OnClick(R.id.plus)
    public void add() {
        quant = quant + 1;
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUANTITY, quant);
        getContentResolver().update(mCurrentItemUri, values, null, null);
        String sQuant = Integer.toString(quant);
        tvquant.setText(sQuant);
    }

    @OnClick(R.id.minus)
    public void subtr() {
        if (quant == 0) {
            Toast.makeText(this, R.string.no_more, Toast.LENGTH_SHORT).show();
        } else {
            quant = quant - 1;
            ContentValues values = new ContentValues();
            values.put(COLUMN_QUANTITY, quant);
            getContentResolver().update(mCurrentItemUri, values, null, null);
            String sQuant = Integer.toString(quant);
            tvquant.setText(sQuant);
        }
    }

    @OnClick(R.id.call)
    public void dial() {
        Uri call = Uri.parse("tel:" + phone);
        Intent surf = new Intent(Intent.ACTION_DIAL, call);
        startActivity(surf);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();


        //setting toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        //setting visibility of elements of menu
        Menu nav_Menu = nv.getMenu();
        nav_Menu.findItem(R.id.nav_add).setVisible(false);
        nav_Menu.findItem(R.id.nav_delete).setVisible(false);
        //activity for drawer options
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_edit: {
                        Intent editIntent = new Intent(DetailActivity.this, AddItem.class);
                        Uri currentPetUri = ContentUris.withAppendedId(CONTENT_URI, ID);
                        editIntent.setData(currentPetUri);
                        startActivity(editIntent);
                        break;
                    }
                    case R.id.nav_delete1: {
                        SingleDeleteConfirm();
                        break;
                    }
                    case R.id.home: {
                        Intent editIntent = new Intent(DetailActivity.this, MainActivity.class);
                        Uri currentItemUri = ContentUris.withAppendedId(CONTENT_URI, id);
                        editIntent.setData(currentItemUri);
                        startActivity(editIntent);
                        break;
                    }
                    default:
                        return true;
                }
                return true;
            }
        });
        getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                _ID, COLUMN_PNAME, COLUMN_PRICE, COLUMN_QUANTITY, COLUMN_SNAME, COLUMN_PHONE
        };
        return new CursorLoader(this, mCurrentItemUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int idCI = cursor.getColumnIndex(_ID);
            int pnameCI = cursor.getColumnIndex(COLUMN_PNAME);
            int quantCI = cursor.getColumnIndex(COLUMN_QUANTITY);
            int priceCI = cursor.getColumnIndex(COLUMN_PRICE);
            int suppCI = cursor.getColumnIndex(COLUMN_SNAME);
            int phoneCI = cursor.getColumnIndex(COLUMN_PHONE);

            ID = cursor.getInt(idCI);
            quant = cursor.getInt(quantCI);
            // Extract out the value from the Cursor for the given column index
            String pname = cursor.getString(pnameCI);
            String sname = cursor.getString(suppCI);
            float price = cursor.getFloat(priceCI);
            phone = cursor.getString(phoneCI);

            // Update the views on the screen with the values from the database
            tvname.setText(pname);
            tvsupp.setText(getString(R.string.java_supp) + sname);
            tvphone.setText(getString(R.string.call_java) + phone);
            tvquant.setText(Integer.toString(quant));
            tvprice.setText(getString(R.string.java_price) + Float.toString(price));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    //confirmation and deleting single item displayed
    private void SingleDeleteConfirm() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete1);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                getContentResolver().delete(mCurrentItemUri, null, null);
                finish();

            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dl.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}