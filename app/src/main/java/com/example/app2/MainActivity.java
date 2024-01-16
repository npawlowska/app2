package com.example.app2;
import android.widget.AdapterView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int EDIT_REQUEST_CODE = 1;
    private static final String PREFS_NAME = "MyPrefsFile";

    private ArrayList<String> items;
    private ListView list;
    private Button button;
    private EditText editText;
    private ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.edit_text);

        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<String>(this, R.layout.custom_list_item, R.id.text_item, items) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                Button removeButton = view.findViewById(R.id.button_remove);
                Button editButton = view.findViewById(R.id.button_edit);

                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeItem(position);
                    }
                });

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startEditActivity(position);
                    }
                });

                return view;
            }
        };
        list.setAdapter(itemsAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        // Dodano obsługę zaznaczania elementów za pomocą CheckBox w ListView
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toggleItem(position);
            }
        });

        // Wczytaj zapisane dane przy uruchomieniu aplikacji
        loadItems();
    }

    private void startEditActivity(int position) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("itemText", items.get(position));
        startActivityForResult(intent, EDIT_REQUEST_CODE);
    }

    private void toggleItem(int position) {
        // Oznaczanie/zdejmowanie oznaczenia dla elementu w ArrayList
        String selectedItem = items.get(position);
        if (selectedItem.startsWith("[x] ")) {
            items.set(position, selectedItem.replace("[x] ", ""));
        } else {
            items.set(position, "[x] " + selectedItem);
        }

        itemsAdapter.notifyDataSetChanged();
    }

    private void removeItem(int position) {
        // Usuwanie elementu z ArrayList
        items.remove(position);
        itemsAdapter.notifyDataSetChanged();

        // Zapisz zaktualizowaną listę po usunięciu elementu
        saveItems();
    }

    private void addItem(View view) {
        String itemText = editText.getText().toString();

        if (!itemText.equals("")) {
            items.add(itemText);
            itemsAdapter.notifyDataSetChanged();
            editText.setText("");

            // Zapisz zaktualizowaną listę po dodaniu elementu
            saveItems();
        } else {
            Toast.makeText(getApplicationContext(), "Please enter text...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            // Pobierz zmodyfikowany tekst i pozycję z EditActivity
            int position = data.getIntExtra("position", -1);
            String modifiedText = data.getStringExtra("modifiedText");

            // Zaktualizuj element listy
            if (position != -1) {
                items.set(position, modifiedText);
                itemsAdapter.notifyDataSetChanged();

                // Zapisz zaktualizowaną listę po edycji elementu
                saveItems();
            }
        }
    }

    private void saveItems() {
        // Zapisz listę do SharedPreferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        Set<String> set = new HashSet<>(items);
        editor.putStringSet("items", set);
        editor.apply();
    }

    private void loadItems() {
        // Wczytaj listę z SharedPreferences przy uruchomieniu aplikacji
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> set = settings.getStringSet("items", null);
        if (set != null) {
            items.clear();
            items.addAll(set);
            itemsAdapter.notifyDataSetChanged();
        }
    }
}

