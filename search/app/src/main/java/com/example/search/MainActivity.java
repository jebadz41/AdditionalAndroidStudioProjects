package com.example.search;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;

    int words = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.my_list);

        List<String> my_list = new ArrayList<>();
        my_list.add("Phone");
        my_list.add("Car");
        my_list.add("Ring");
        my_list.add("Monitor");
        my_list.add("bag");
        my_list.add("Pencil");
        my_list.add("Handkerchief");


   /*     String[] add_word = generateRandomWords(words);

        for(int i = 0; i < words; i++) {
            my_list.add(add_word[i]);
        }*/
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,my_list);
        listView.setAdapter(arrayAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

  /*  public static String[] generateRandomWords(int numberOfWords)
    {
        String[] randomStrings = new String[numberOfWords];
        Random random = new Random();
        for(int i = 0; i < numberOfWords; i++)
        {
            char[] word = new char[random.nextInt(8)+3]; // words of length 3 through 10. (1 and 2 letter words are boring.)
            for(int j = 0; j < word.length; j++)
            {
                word[j] = (char)('a' + random.nextInt(26));
            }
            randomStrings[i] = new String(word);
        }
        return randomStrings;
    }*/

}