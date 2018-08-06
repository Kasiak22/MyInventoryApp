package com.example.android.myinventoryapp.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;


import static com.example.android.myinventoryapp.Data.InventoryContract.CONTENT_AUTHORITY;
import static com.example.android.myinventoryapp.Data.InventoryContract.ITEM_PATH;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_PNAME;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_PRICE;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.COLUMN_QUANTITY;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.CONTENT_ITEM_TYPE;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.CONTENT_LIST_TYPE;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry.TABLE_NAME;
import static com.example.android.myinventoryapp.Data.InventoryContract.NewEntry._ID;

public class InventoryProvider extends ContentProvider {
    private static final int INVENTORY = 100;
    private static final int ITEM_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY, ITEM_PATH, INVENTORY);

        sUriMatcher.addURI(CONTENT_AUTHORITY, ITEM_PATH + "/#", ITEM_ID);
    }

    private InventoryDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                cursor = database.query(TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ITEM_ID:
                selection = _ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return CONTENT_LIST_TYPE;
            case ITEM_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return insertItem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case INVENTORY:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case ITEM_ID:
                // Delete a single row given by the ID in the URI
                selection = _ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case ITEM_ID:
                selection = _ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    public Uri insertItem(Uri uri, ContentValues values) {
        String pname = values.getAsString(COLUMN_PNAME);
        if (pname == null) {
            throw new IllegalArgumentException("This field cannot be empty");
        }
        Float price = values.getAsFloat(COLUMN_PRICE);
        if (price == null) {
            throw new IllegalArgumentException("This field cannot be empty");
        }
        Integer quantity = values.getAsInteger(COLUMN_QUANTITY);
        if (quantity == null) {
            throw new IllegalArgumentException("This field cannot be empty");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        Long newRow = database.insert(TABLE_NAME, null, values);
        if (newRow == -1) {
            Toast.makeText(getContext(), "Nothing is inserted", Toast.LENGTH_SHORT).show();
        }
        return ContentUris.withAppendedId(uri, newRow);
    }

    public int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(COLUMN_PNAME)) {
            String pname = values.getAsString(COLUMN_PNAME);
            if (pname == null) {
                throw new IllegalArgumentException("This field cannot be empty");
            }
        }
        if (values.containsKey(COLUMN_PRICE)) {
            Float price = values.getAsFloat(COLUMN_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("This field cannot be empty");
            }
        }
        if (values.containsKey(COLUMN_QUANTITY)) {
            Integer quantity = values.getAsInteger(COLUMN_QUANTITY);
            if (quantity == null) {
                throw new IllegalArgumentException("This field cannot be empty");
            }
        }
        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowUpdated = database.update(TABLE_NAME, values, selection, selectionArgs);

        return rowUpdated;
    }
}