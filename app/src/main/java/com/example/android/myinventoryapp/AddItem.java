package com.example.android.myinventoryapp;

import android.content.ContentValues;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.EditText;


import com.example.android.myinventoryapp.Data.InventoryDbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_PHONE;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_PNAME;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_PRICE;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_QUANTITY;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_SNAME;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.TABLE_NAME;


public class AddItem extends AppCompatActivity {

   // EditText pNameEdText;
   // EditText priceEdText;
    @BindView(R.id.edit_quantity) EditText quantEdText;
    @BindView(R.id.edit_supplier) EditText suppEdText;
    @BindView(R.id.edit_phone) EditText phoneEdText;
    @BindView(R.id.edit_price) EditText  priceEdText;
    @BindView(R.id.edit_pname) EditText  pNameEdText;

    @OnClick(R.id.save_button)
    public void save(){
        insertData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        ButterKnife.bind(this);
    }

    //activity for adding information to databse
    private void insertData() {
        InventoryDbHelper mDbHelper = new InventoryDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues newItem = new ContentValues();

        //reading info from EditText to String format
        String pnameString = pNameEdText.getText().toString();
        String priceString = priceEdText.getText().toString();
        String quantityString = quantEdText.getText().toString();
        String suppString = suppEdText.getText().toString();
        String phoneString = phoneEdText.getText().toString();

        //Changing String to number format
        float price = Float.parseFloat(priceString);
        int quantity = Integer.parseInt(quantityString);

       //adding to ContentValue
        newItem.put(COLUMN_PNAME, pnameString);
        newItem.put(COLUMN_PRICE, price);
        newItem.put(COLUMN_QUANTITY, quantity);
        newItem.put(COLUMN_SNAME, suppString);
        newItem.put(COLUMN_PHONE, phoneString);

        //Adding to database
        long newRowId = db.insert(TABLE_NAME, null, newItem);

        //After adding its going back to MainActivity
        Intent backIntent = new Intent(AddItem.this, MainActivity.class);
        startActivity(backIntent);

    }
}
