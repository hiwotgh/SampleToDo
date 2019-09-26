package com.example.sampletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> items;

    Button bttAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttAdd = findViewById(R.id.bttAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();


        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){

            @Override
            public void onItemLongClicked(int Position) {
                //Delete the item from the model;
                items.remove(Position);
                //Notify the adapter at which position we deleted the item;
                itemsAdapter.notifyItemRemoved(Position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
       rvItems.setAdapter(itemsAdapter);
       rvItems.setLayoutManager(new LinearLayoutManager (this));


       bttAdd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              String todoItem = etItem.getText().toString();
              //add this two items to the model
               items.add(todoItem);
               //notify the adaptor that we are going to insert an item
               itemsAdapter.notifyItemInserted(items.size()-1);
               etItem.setText("");
               Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
               saveItems();
           }
       });
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");

    }
    //This function will load by reading every line of the data item
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }

    }

    //This function will saves by writing them into the datat file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }

    }
}
