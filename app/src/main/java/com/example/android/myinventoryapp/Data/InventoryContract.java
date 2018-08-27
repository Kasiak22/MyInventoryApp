package com.example.android.myinventoryapp.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class InventoryContract {
    private InventoryContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.android.myinventoryapp";


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String ITEM_PATH = "item";

    public static abstract class NewEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, ITEM_PATH);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of items.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + ITEM_PATH;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + ITEM_PATH;

        public static final String TABLE_NAME = "Stuff";
        public static final String COLUMN_PNAME = "ProductName";
        public static final String COLUMN_PRICE = "Price";
        public static final String COLUMN_QUANTITY = "Quantity";
        public static final String COLUMN_SNAME = "SupplierName";
        public static final String COLUMN_PHONE = "SupplierPhoneNumber";
        public final static String _ID = BaseColumns._ID;
    }
}

