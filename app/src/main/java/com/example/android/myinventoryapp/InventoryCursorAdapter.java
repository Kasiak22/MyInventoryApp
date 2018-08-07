package com.example.android.myinventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_PNAME;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_PRICE;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_QUANTITY;


public class InventoryCursorAdapter extends CursorAdapter {


    public  InventoryCursorAdapter(Context context, Cursor c) {
       super (context, c, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView vName = (TextView) view.findViewById(R.id.pname);
        TextView vQuant = (TextView) view.findViewById(R.id.quantity);
       TextView vPrice = (TextView) view.findViewById(R.id.price);
        Button plus = (Button)view.findViewById(R.id.plus);
        Button minus = (Button)view.findViewById(R.id.minus);


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

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView vQuant = (TextView) view.findViewById(R.id.quantity);
                String cos =vQuant.getText().toString();
                int nQuant = Integer.parseInt(cos);
               nQuant = nQuant +1;
               String snQuant = Integer.toString(nQuant);
                vQuant.setText(snQuant);
            }

    });
      /*  minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nQuant = pQuant -1;
                String snQuant = Integer.toString(nQuant);
                vQuant.setText(snQuant);
            }

        });
*/

}
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }
}
