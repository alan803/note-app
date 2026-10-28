
package com.example.noteapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etId, etTitle, etContent;
    Button btnAdd, btnUpdate, btnDelete;
    TextView tvResult;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etId = findViewById(R.id.etId);
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);

        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        tvResult = findViewById(R.id.tvResult);

        dbHelper = new DatabaseHelper(this);

        // DISPLAY NOTES AUTOMATICALLY
        tvResult.setText(dbHelper.getAllNotes());

        // ADD NOTE
        btnAdd.setOnClickListener(v -> {

            if (!validDetails()) return;

            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();

            boolean result = dbHelper.addNote(title, content);

            if (result) {
                showMessage("Note Added");
                clearFields();
                tvResult.setText(dbHelper.getAllNotes());
            } else {
                showMessage("Failed to Add Note");
            }
        });

        // UPDATE NOTE
        btnUpdate.setOnClickListener(v -> {

            if (!validId() || !validDetails()) return;

            int id = Integer.parseInt(
                    etId.getText().toString().trim());

            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();

            boolean result = dbHelper.updateNote(
                    id, title, content);

            if (result) {
                showMessage("Note Updated");
                clearFields();
                tvResult.setText(dbHelper.getAllNotes());
            } else {
                showMessage("Note Not Found");
            }
        });

        // DELETE NOTE
        btnDelete.setOnClickListener(v -> {

            if (!validId()) return;

            int id = Integer.parseInt(
                    etId.getText().toString().trim());

            boolean result = dbHelper.deleteNote(id);

            if (result) {
                showMessage("Note Deleted");
                clearFields();
                tvResult.setText(dbHelper.getAllNotes());
            } else {
                showMessage("Note Not Found");
            }
        });
    }

    // VALIDATE ID
    private boolean validId() {

        String id = etId.getText().toString().trim();

        if (id.isEmpty()) {
            etId.setError("Enter Note ID");
            return false;
        }

        try {
            if (Integer.parseInt(id) <= 0) {
                etId.setError("Enter a valid ID");
                return false;
            }
        } catch (NumberFormatException e) {
            etId.setError("Invalid ID");
            return false;
        }

        return true;
    }

    // VALIDATE NOTE DETAILS
    private boolean validDetails() {

        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("Enter Note Title");
            return false;
        }

        if (content.isEmpty()) {
            etContent.setError("Enter Note Content");
            return false;
        }

        return true;
    }

    // DISPLAY MESSAGE
    private void showMessage(String message) {
        Toast.makeText(this, message,
                Toast.LENGTH_SHORT).show();
    }

    // CLEAR INPUT FIELDS
    private void clearFields() {
        etId.setText("");
        etTitle.setText("");
        etContent.setText("");
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
