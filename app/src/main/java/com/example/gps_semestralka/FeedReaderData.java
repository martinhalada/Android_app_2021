package com.example.gps_semestralka;

import android.provider.BaseColumns;

public final class FeedReaderData {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderData() {
    }

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "Souradnice";
        public static final String COLUMN_NAME_LAT = "latitude";
        public static final String COLUMN_NAME_LON = "longitude";
        public static final String COLUMN_NAME_ALT = "altitude";
        public static final String COLUMN_NAME_DESC = "description";
    }
}
