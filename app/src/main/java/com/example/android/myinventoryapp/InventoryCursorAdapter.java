package com.example.android.myinventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;




import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_PNAME;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_PRICE;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_QUANTITY;


public class InventoryCursorAdapter extends CursorAdapter {

    public  InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 );
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView vName = (TextView) view.findViewById(R.id.pname);
        TextView vQuant = (TextView) view.findViewById(R.id.quantity);
        TextView vPrice = (TextView) view.findViewById(R.id.price);


        int nameCI = cursor.getColumnIndex(COLUMN_PNAME);
        int quantCI = cursor.getColumnIndex(COLUMN_QUANTITY);
        int priceCI = cursor.getColumnIndex(COLUMN_PRICE);

        String pName = cursor.getString(nameCI);
        int pQuant = cursor.getInt(quantCI);
        Float pPrice = cursor.getFloat(priceCI);

        String sQuant = Integer.toString(pQuant);
        String sPrice = Float.toString(pPrice);

        vName.setText(pName);
        vQuant.setText(sQuant);
        vPrice.setText(sPrice);

    }
}
