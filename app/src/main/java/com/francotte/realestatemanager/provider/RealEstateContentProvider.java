package com.francotte.realestatemanager.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.francotte.realestatemanager.database.RealEstateManagerDatabase;
import com.francotte.realestatemanager.model.House;

public class RealEstateContentProvider extends ContentProvider {

    //For data
    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.provider";
    public static final String TABLE_NAME = House.class.getSimpleName();
    public static final Uri URI_HOUSE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if (getContext() != null) {
            long houseId = ContentUris.parseId(uri);
            final Cursor cursor = RealEstateManagerDatabase.getINSTANCE(getContext()).houseDao().getHousesWithCursor(houseId);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }

        throw new IllegalArgumentException("Failed to query row for uri " + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.house/" + AUTHORITY + "." + TABLE_NAME;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        if (getContext() != null) {
            final long id = RealEstateManagerDatabase.getINSTANCE(getContext()).houseDao().createHouse(House.fromContentValues(contentValues));
            if (id != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }
        }

        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (getContext() != null) {
            final int count = RealEstateManagerDatabase.getINSTANCE(getContext()).houseDao().deleteHouse(ContentUris.parseId(uri));
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
        throw new IllegalArgumentException("Failed to delete row into " + uri);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (getContext() != null) {
            final int count = RealEstateManagerDatabase.getINSTANCE(getContext()).houseDao().updateHouse(
                    contentValues.getAsString("category"),
                    contentValues.getAsString("district"),
                    contentValues.getAsInteger("price"),
                    contentValues.getAsBoolean("isEuro"),
                    contentValues.getAsInteger("area"),
                    contentValues.getAsInteger("numberOfRooms"),
                    contentValues.getAsInteger("numberOfBathrooms"),
                    contentValues.getAsInteger("numberOfBedrooms"),
                    contentValues.getAsInteger("school"),
                    contentValues.getAsInteger("shopping"),
                    contentValues.getAsInteger("publicTransport"),
                    contentValues.getAsInteger("swimmingPool"),
                    contentValues.getAsString("description"),
                    contentValues.getAsBoolean("available"),
                    contentValues.getAsString("dateOfEntry"),
                    contentValues.getAsString("dateOfSale"),
                    contentValues.getAsString("realEstateAgent"),
                    contentValues.getAsLong("houseId"));
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
        throw new IllegalArgumentException("Failed to update row into " + uri);
    }
}
