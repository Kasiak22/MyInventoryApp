package com.example.android.myinventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
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


public class AddItem extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    @BindView(R.id.edit_quantity)
    EditText quantEdText;
    @BindView(R.id.edit_supplier)
    EditText suppEdText;
    @BindView(R.id.edit_phone)
    EditText phoneEdText;
    @BindView(R.id.edit_price)
    EditText priceEdText;
    @BindView(R.id.edit_pname)
    EditText pNameEdText;
    private static final int EXISTING_ITEM_LOADER = 0;

    @OnClick(R.id.save_button)
    public void save() {
        saveData();
    }

    private Uri mCurrentItemUri;
    Uri newUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();
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
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int pnameCI = cursor.getColumnIndex(COLUMN_PNAME);
            int quantCI = cursor.getColumnIndex(COLUMN_QUANTITY);
            int priceCI = cursor.getColumnIndex(COLUMN_PRICE);
            int suppCI = cursor.getColumnIndex(COLUMN_SNAME);
            int phoneCI = cursor.getColumnIndex(COLUMN_PHONE);

            // Extract out the value from the Cursor for the given column index
            String pname = cursor.getString(pnameCI);
            String sname = cursor.getString(suppCI);
            int quant = cursor.getInt(quantCI);
            float price = cursor.getFloat(priceCI);
            String phone = cursor.getString(phoneCI);

            // Update the views on the screen with the values from the database
            pNameEdText.setText(pname);
            suppEdText.setText(sname);
            phoneEdText.setText(phone);
            quantEdText.setText(Integer.toString(quant));
            priceEdText.setText(Float.toString(price));

    }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        pNameEdText.setText(" ");
        quantEdText.setText(" ");
        priceEdText.setText(" ");
        suppEdText.setText(" ");
        phoneEdText.setText(" ");
    }

    //activity for adding information to databse
    private void saveData() {


        //reading info from EditText to String format
        String pnameString = pNameEdText.getText().toString();
        String priceString = priceEdText.getText().toString();
        String quantityString = quantEdText.getText().toString();
        String suppString = suppEdText.getText().toString();
        String phoneString = phoneEdText.getText().toString();


        if (TextUtils.isEmpty(pnameString) && TextUtils.isEmpty(priceString) && TextUtils.isEmpty(quantityString) && TextUtils.isEmpty(suppString) && TextUtils.isEmpty(phoneString)) {
            return;
        } else {

            ContentValues newItem = new ContentValues();
            //adding to ContentValue
            newItem.put(COLUMN_PNAME, pnameString);
            newItem.put(COLUMN_SNAME, suppString);
            newItem.put(COLUMN_PHONE, phoneString);

            float price = 0;
            if (!TextUtils.isEmpty(priceString)) {
                price = Float.parseFloat(priceString);
            }
            newItem.put(COLUMN_PRICE, price);
            int quantity = 0;
            if (!TextUtils.isEmpty(quantityString)) {
                quantity = Integer.parseInt(quantityString);
            }
            newItem.put(COLUMN_QUANTITY, quantity);

            if (mCurrentItemUri == null) {
                // This is a NEW pet, so insert a new pet into the provider,
                // returning the content URI for the new pet.
                newUri = getContentResolver().insert(CONTENT_URI, newItem);
            } else {
                //Updating
                long newRowId = getContentResolver().update(mCurrentItemUri, newItem, null, null);
            }

            //After adding its going back to MainActivity
           // Intent backIntent = new Intent(AddItem.this, MainActivity.class);
            //startActivity(backIntent);
return;
        }
    }
}
