package com.alina.physicsproject.data.contract;

import android.provider.BaseColumns;

public class SignContract {

    public static final class MailEntry implements BaseColumns {

        public static final String TABLE_NAME = "sign";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_MAIL = "mail";


        public static final String TYPE_TEXT = "TEXT";
        public static final String TYPE_INTEGER = "INTEGER";

        public static final String CREATE_COMMAND = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " ( " + _ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " " + TYPE_TEXT + ", "
                + COLUMN_MAIL + " " + TYPE_TEXT + " )";

        public static final String DROP_COMMAND = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
