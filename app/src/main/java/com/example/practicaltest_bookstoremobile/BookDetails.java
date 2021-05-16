package com.example.practicaltest_bookstoremobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import model.Item;

public class BookDetails extends AppCompatActivity {

    TextView txtBookTitle, txtBookPublishedDate, txtBookAuthor, txtBookDesc;
    Button btnBuyLink;
    CheckBox cbxFavorite;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        txtBookTitle = findViewById(R.id.txt_book_title);
        txtBookPublishedDate = findViewById(R.id.txt_book_publishedDate);
        txtBookAuthor = findViewById(R.id.txt_book_author);
        txtBookDesc = findViewById(R.id.txt_book_description);
        btnBuyLink = findViewById(R.id.btn_buy_book);
        cbxFavorite = findViewById(R.id.cbx_book_Favorite);

        settings = getSharedPreferences("Favorites",
                Context.MODE_PRIVATE);


        Gson gson = new Gson();
        if (!getIntent().getStringExtra("Book").isEmpty()) {
            String json = getIntent().getStringExtra("Book");
            Item book = gson.fromJson(json, Item.class);

            txtBookTitle.setText(book.getVolumeInfo().getTitle() != null ?
                    book.getVolumeInfo().getTitle() : "");

            txtBookPublishedDate.setText(book.getVolumeInfo().getPublishedDate() != null ?
                    book.getVolumeInfo().getPublishedDate() : "");

            txtBookAuthor.setText(book.getVolumeInfo().getAuthors() != null ?
                    book.getVolumeInfo().getAuthors().toString() : "");

            txtBookDesc.setText(book.getVolumeInfo().getDescription() != null ?
                    book.getVolumeInfo().getDescription() : "");

            if (book.getSaleInfo().getBuyLink() != null ) {
                btnBuyLink.setVisibility(View.VISIBLE);
                btnBuyLink.setOnClickListener(v -> {
                    Uri uri = Uri.parse(book.getSaleInfo().getBuyLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                });
            }

            cbxFavorite.setChecked(settings.contains(book.getId()));

            cbxFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SharedPreferences.Editor editor = settings.edit();
                if (isChecked) {
                    editor.putString(book.getId()
                            , json);
                } else {
                    editor.remove(book.getId());
                }
                editor.apply();
            });

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}