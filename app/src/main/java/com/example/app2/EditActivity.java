package com.example.app2;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    private EditText editTextView;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTextView = findViewById(R.id.edit_text_edit);
        saveButton = findViewById(R.id.button_save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        // Pobierz dane przekazane z MainActivity
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", -1);
        String itemText = intent.getStringExtra("itemText");

        // Ustaw dane w polu tekstowym
        if (position != -1 && itemText != null) {
            editTextView.setText(itemText);
        } else {
            // Jeśli dane są nieprawidłowe, zakończ aktywność
            finish();
        }
    }

    private void saveChanges() {
        // Pobierz zmodyfikowany tekst z pola tekstowego
        String modifiedText = editTextView.getText().toString();

        // Przygotuj intent do przekazania danych z powrotem do MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("position", getIntent().getIntExtra("position", -1));
        resultIntent.putExtra("modifiedText", modifiedText);

        // Ustaw rezultat i zakończ aktywność
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
