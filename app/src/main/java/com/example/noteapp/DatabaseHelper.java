
package com.example.noteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "NotesDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Notes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "content TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        // No upgrade required for this basic project.
    }

    // ADD NOTE
    public boolean addNote(String title, String content) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);

        long result = db.insert("Notes", null, values);

        return result != -1;
    }

    // VIEW ALL NOTES
    public String getAllNotes() {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Notes ORDER BY id DESC", null);

        StringBuilder notes = new StringBuilder();

        try {
            while (cursor.moveToNext()) {

                notes.append("ID: ")
                        .append(cursor.getInt(0))
                        .append("\nTitle: ")
                        .append(cursor.getString(1))
                        .append("\nContent: ")
                        .append(cursor.getString(2))
                        .append("\n\n");
            }
        } finally {
            cursor.close();
        }

        if (notes.length() == 0) {
            return "No notes found";
        }

        return notes.toString();
    }

    // UPDATE NOTE
    public boolean updateNote(int id, String title,
                              String content) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);

        int result = db.update(
                "Notes",
                values,
                "id=?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }

    // DELETE NOTE
    public boolean deleteNote(int id) {

        SQLiteDatabase db = getWritableDatabase();

        int result = db.delete(
                "Notes",
                "id=?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }
}