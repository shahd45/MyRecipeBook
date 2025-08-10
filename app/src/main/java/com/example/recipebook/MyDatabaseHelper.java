package com.example.recipebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "RecipeBook.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "my_recipes";
    private static final String COLUME_NAME = "name";
    private static final String COLUME_TIME = "time";
    private static final String COLUME_SERVINGS = "servings";
    private static final String COLUME_INGREDIENTS = "ingredients";
    private static final String COLUME_INSTRUCTIONS = "instructions";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUME_NAME + " TEXT PRIMARY KEY, " +
                        COLUME_TIME + " TEXT, " +
                        COLUME_SERVINGS + " TEXT, " +
                        COLUME_INGREDIENTS + " TEXT, " +
                        COLUME_INSTRUCTIONS + " TEXT);";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addRecipe(String name, String time, String servings, String ingredients, String instructions) {
        SQLiteDatabase db = this.getWritableDatabase();
        //restore all our data from our application
        ContentValues cv = new ContentValues();
        cv.put(COLUME_NAME, name);
        cv.put(COLUME_TIME, time);
        cv.put(COLUME_SERVINGS, servings);
        cv.put(COLUME_INGREDIENTS, ingredients);
        cv.put(COLUME_INSTRUCTIONS, instructions);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "db failed", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            Toast.makeText(context, "db succeeded", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        /*check if the table is not empty*/
        if (db != null) {
            cursor = db.rawQuery(query, null);//execute the raw query
        }
        return cursor;
    }

    public void deleteRecipe(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "name=?", new String[]{name});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }
}
