package com.example.android.myinventoryapp;


import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import com.example.android.myinventoryapp.Data.InventoryDbHelper;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SuppActivity extends AppCompatActivity  {

    InventoryDbHelper ItDbHelper;


    @OnClick(R.id.back_button)
    public void back() {
        Intent backIntent = new Intent(SuppActivity.this, MainActivity.class);
        startActivity(backIntent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supp_list);

        ItDbHelper = new InventoryDbHelper(this);
        ButterKnife.bind(this);
        ListView suppItems = (ListView) findViewById(R.id.supp_list_view);




         }


}

