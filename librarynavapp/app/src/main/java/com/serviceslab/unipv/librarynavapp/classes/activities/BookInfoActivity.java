package com.serviceslab.unipv.librarynavapp.classes.activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.serviceslab.unipv.librarynavapp.R;
import com.serviceslab.unipv.librarynavapp.classes.model.Armadio;
import com.serviceslab.unipv.librarynavapp.classes.model.Book;
import com.serviceslab.unipv.librarynavapp.classes.model.Library;
import com.serviceslab.unipv.librarynavapp.classes.restfulClasses.APIListService;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookInfoActivity extends Activity {

    private Armadio armadio;
    private Library library;
    //Xiaomi robolab
    private final String url = "http://192.168.31.181:2020/";
    //Iman's hotspot
    //private final String url = "http://192.168.43.170:2020/";
    //Miki's home
    //private final String url = "http://192.168.1.104:2020/";
    //Library hotspot
    //private final String url = "http://192.168.1.104:2020/";
    //private final String url = "http:/192.168.43.198:2020/";
    private String codice_topografico_completo;
    private String description;
    private TextView infoTextView;
    private String bookInventory;
    private ProgressBar spinner;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        bookInventory = intent.getExtras().getString("book_inventory");
        Log.i("BookInfoActivity", bookInventory);
        codice_topografico_completo = intent.getExtras().getString("codice_topografico_completo");
        setContentView(R.layout.activity_book_info);
        spinner = (ProgressBar) findViewById(R.id.book_info_spinner);
        spinner.setIndeterminate(true);
        spinner.setVisibility(View.VISIBLE);
        infoTextView = (TextView)findViewById(R.id.book_info_text);
        getBooks(bookInventory);

    }

    public Bitmap getBitmapFromAssets(String fileName) {
        AssetManager assetManager = getAssets();
        try {
            InputStream istr = assetManager.open(fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(istr);
            return bitmap;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    //Get book
    //Getting all books
    private void getBooks(final String inventory) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIListService service = retrofit.create(APIListService.class);
        Call<List<Book>> call = service.getBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(response != null) {
                    try {
                        List<Book> books = response.body();
                        for (Book b : books) {
                            if (b.getInventory().equals(inventory)){
                                book = b;
                                createBookDescriptionText(book,infoTextView);
                                ImageView iv = (ImageView)findViewById(R.id.book_image_view);
                                String imgName = "i" + bookInventory + ".png";
                                Log.i("imgName=",imgName);
                                iv.setImageBitmap(getBitmapFromAssets(imgName));
                            }
                        }
                    } catch (Exception e) {
                        Log.e("onResponse", "There is en error withing the server answer");
                        e.printStackTrace();
                        description = "<body>" + "<h1>" + "No description available for this book."
                                + "</h1>" + "</body>";
                        infoTextView.setText(Html.fromHtml(description));
                    }
                } else {
                    description = "<body>" + "<h1>" + "No description available for this book."
                            + "</h1>" + "</body>";
                    infoTextView.setText(Html.fromHtml(description));
                }
            }
            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e("onFailure armadio", "Fail to get the json", t);
                description = "<body>" + "<h1>" + "No description available for this book."
                        + "</h1>" + "</body>";
                infoTextView.setText(Html.fromHtml(description));
            }
        });
    }

    public void createBookDescriptionText(Book book, TextView textView){
        String htmlDescr;
        String bodyTag = "<body>";
        String bodyCloseTag = "</body>";
        String h1Tag = "<h1>";
        String h1CloseTag = "</h1>";
        String h2Tag="<h2>";
        String h2CloseTag = "</h2>";
        String pTag = "<p>";
        String pCloseTag = "</p>";
        String inventarioLabel = "Numero inventario:";
        String cTopoLabel = "Codice topografico: ";
        String serieLabel = "Serie: ";

        description = bodyTag + h1Tag + book.getTitle() + h1CloseTag
                + h2Tag + inventarioLabel + h2CloseTag
                + pTag + book.getInventory() + pCloseTag
                + h2Tag + serieLabel + h2CloseTag
                + pTag + book.getSerieCode() + pCloseTag
                + h2Tag + cTopoLabel + h2CloseTag
                + pTag + codice_topografico_completo + pCloseTag
                + bodyCloseTag;
        textView.setText(Html.fromHtml(description));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
