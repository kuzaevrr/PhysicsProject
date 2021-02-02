package com.alina.physicsproject.data.contract;

import android.provider.BaseColumns;

public class AnswerContract {

    public static final class AnswerEntry implements BaseColumns {

        public static final String TABLE_NAME = "answer";
        public static final String COLUMN_PARAGRAPH = "paragraph";
        public static final String COLUMN_APPRAISAL = "appraisal";


        public static final String TYPE_TEXT = "TEXT";
        public static final String TYPE_INTEGER = "INTEGER";

        public static final String CREATE_COMMAND = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " ( " + _ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PARAGRAPH + " " + TYPE_TEXT + ", "
                + COLUMN_APPRAISAL + " " + TYPE_TEXT + " )";

        public static final String DROP_COMMAND = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
