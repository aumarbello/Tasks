package com.example.ahmed.simpdo.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.ahmed.simpdo.utils.AppConstants.*;
import static com.example.ahmed.simpdo.utils.DBSchema.*;

/**
 * Created by ahmed on 8/24/17.
 */

class Helper extends SQLiteOpenHelper {
    Helper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                CREATE_TABLE
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
}
