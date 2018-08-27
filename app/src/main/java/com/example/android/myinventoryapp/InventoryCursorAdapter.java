package com.example.android.myinventoryapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_PNAME;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_PRICE;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_QUANTITY;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.CONTENT_URI;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry._ID;


public class InventoryCursorAdapter extends CursorAdapter {


    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);

    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView vName = view.findViewById(R.id.pname);
        TextView vQuant = view.findViewById(R.id.quantity);
        TextView vPrice = view.findViewById(R.id.price);
        Button minus = view.findViewById(R.id.sell);

        int idCI = cursor.getColumnIndex(_ID);
        int nameCI = cursor.getColumnIndex(COLUMN_PNAME);
        int quantCI = cursor.getColumnIndex(COLUMN_QUANTITY);
        int priceCI = cursor.getColumnIndex(COLUMN_PRICE);

        final int id = cursor.getInt(idCI);
        String pName = cursor.getString(nameCI);
        final int pQuant = cursor.getInt(quantCI);
        Float pPrice = cursor.getFloat(priceCI);
        String sQuant = Integer.toString(pQuant);
        String sPrice = Float.toString(pPrice);

        vName.setText(pName);
        vQuant.setText(sQuant);
        vPrice.setText(sPrice);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pQuant == 0) {
                    Toast.makeText(context, R.string.no_substraction, Toast.LENGTH_SHORT).show();
                } else {
                    int nQuant = pQuant - 1;
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_QUANTITY, nQuant);
                    Uri uri = ContentUris.withAppendedId(CONTENT_URI, id);
                    context.getContentResolver().update(uri, values, null, null);
                }
            }
        });
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return view;
    }
}
