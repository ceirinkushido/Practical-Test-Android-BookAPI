package com.example.practicaltest_bookstoremobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adapter.BookAdapter;
import model.Item;
import model.Items;
import network.GetBookDataService;
import network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private Integer currentIndex = 0;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences("Favorites", Context.MODE_PRIVATE);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_book_list);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    if (!recyclerView.canScrollVertically(1)) {
                        //Log.d("D", "MyScrollView: Bottom has been reached");
                        loadItems(currentIndex += 10);
                    }
                    if (!recyclerView.canScrollVertically(-1)) {
                        //Log.d("D", "MyScrollView: TOP has been reached");
                        loadItems(currentIndex > 0 ? currentIndex -= 10 : 0);
                    }
                    super.onScrollStateChanged(recyclerView, newState);
                }
            }
        });

        loadItems(currentIndex);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btn_show_favorites) {
            currentIndex = -10;

            Gson gson = new Gson();

            Items books = new Items();
            List<Item> items = new ArrayList<>();
            Map<String, ?> allEntries = settings.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                String json = settings.getString(entry.getKey(), "");
                items.add(gson.fromJson(json, Item.class));
            }
            books.setItems(items);
            generateBookList(books);
        }
        return super.onOptionsItemSelected(item);
    }


    private void loadItems(Integer loadIndex) {
        GetBookDataService service = RetrofitInstance.getRetrofitInstance().create(GetBookDataService.class);

        Call<JsonObject> call = service.getBookData("books/v1/volumes?q=ios&maxResults=10" +
                "&startIndex="+loadIndex);
        //Log.e("URL Called", call.request().url() + "");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (!response.isSuccessful())
                    Log.e("F: ", "" + response.raw());
                else {
                    assert response.body() != null;
                    Items bookAdapter = new Gson().fromJson(response.body(), Items.class);
                    generateBookList(bookAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.e("F: ", "" + t);
            }
        });
    }

    private void generateBookList(Items empDataList) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_book_list);

        BookAdapter adapter = new BookAdapter(empDataList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
