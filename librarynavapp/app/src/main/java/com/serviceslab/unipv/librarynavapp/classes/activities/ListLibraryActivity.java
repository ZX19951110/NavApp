package com.serviceslab.unipv.librarynavapp.classes.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.serviceslab.unipv.librarynavapp.R;
import com.serviceslab.unipv.librarynavapp.classes.model.Library;
import com.serviceslab.unipv.librarynavapp.classes.restfulClasses.APIListService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListLibraryActivity extends AppCompatActivity {

    //Xiaomi robolab
    private final String url = "http://192.168.31.181:2020/";
    //Iman's hotspot
    //private final String url = "http://192.168.43.170:2020/";
    //Miki's home
    //private final String url = "http://192.168.1.104:2020/";
    //Library wifi
    //private final String url = "http://192.168.43.170:2020/";
    //private final String url = "http:/192.168.43.198:2020/";
    private List<Library> libraries = new ArrayList<>();
    private List<String> libraryNames = new ArrayList<>();
    private ListView lv;
    private ArrayAdapter<String> arrayAdapter;
    private ProgressBar spinner;
    private String bookInventory;
    private String bookTitle;
    private String requestTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_library);
        lv = (ListView) findViewById(R.id.lv_library_list);
        spinner = (ProgressBar) findViewById(R.id.list_library_spinner);
        spinner.setIndeterminate(true);
        spinner.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        bookInventory = intent.getExtras().getString("book_inventory");
        bookTitle = intent.getExtras().getString("book_title");
        requestTimestamp = intent.getExtras().getString("requestTimestamp");
        getLibraries();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String item = ((TextView)view).getText().toString();
                String libraryId = libraries.get(position).getId();
                Intent myIntent =
                        new Intent(ListLibraryActivity.this,
                                ListArmadioActivity.class);
                myIntent.putExtra("library_code", libraryId);
                myIntent.putExtra("book_inventory", bookInventory);
                myIntent.putExtra("book_title", bookTitle);
                myIntent.putExtra("requestTimestamp", requestTimestamp);
                ListLibraryActivity.this.startActivity(myIntent);
            }
        });
    }

    private void getLibraries() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIListService service = retrofit.create(APIListService.class);
        Call<List<Library>> call = service.getLibraries();
        call.enqueue(new Callback<List<Library>>() {
            @Override
            public void onResponse(Call<List<Library>> call, Response<List<Library>> response) {
                if(response != null) {
                    libraries = response.body();
                }
                if(libraries.size() != 0) {
                    spinner.setVisibility(View.GONE);
                    for (Library l : libraries) {
                        libraryNames.add(l.getName());
                    }
                    arrayAdapter = new ArrayAdapter<String>(ListLibraryActivity.this,
                            android.R.layout.simple_list_item_1, libraryNames);
                    lv.setAdapter(arrayAdapter);
                } else {
                    dialogServerError();
                }

            }
            @Override
            public void onFailure(Call<List<Library>> call, Throwable t) {
                spinner.setVisibility(View.GONE);
                Log.e("onFailure armadio", "Fail to get the json", t);
                dialogServerError();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void dialogServerError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ListLibraryActivity.this);
        builder.setMessage(R.string.ops)
                .setCancelable(false)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent myIntent = new Intent(ListLibraryActivity.this, MainActivity.class);
                        ListLibraryActivity.this.startActivity(myIntent);
                    }
                });
        builder.create();
        builder.show();
    }
}