package com.serviceslab.unipv.librarynavapp.classes.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.serviceslab.unipv.librarynavapp.R;
import com.serviceslab.unipv.librarynavapp.classes.algorithm.MyComparator;
import com.serviceslab.unipv.librarynavapp.classes.model.Armadio;
import com.serviceslab.unipv.librarynavapp.classes.model.Library;
import com.serviceslab.unipv.librarynavapp.classes.restfulClasses.APIListService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListArmadioActivity extends Activity {

    //Xiaomi robolab
    private final String url = "http://192.168.31.181:2020/";
    //Iman's hotspot
    //private final String url = "http://192.168.43.170:2020/";
    //Miki's home
    //private final String url = "http://192.168.1.104:2020/";
    //Library wifi
    //private final String url = "http://192.168.43.170:2020/";
    //private final String url = "http:/192.168.43.198:2020/";
    private List<Armadio> armadi = new ArrayList<>();
    private List<String> armadiNumbers = new ArrayList<>();
    private ListView lv;
    private ArrayAdapter<String> arrayAdapter;
    private ProgressBar spinner;
    private String libraryId;
    private String bookInventory;
    private String bookTitle;
    private String requestTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        libraryId = intent.getExtras().getString("library_code");
        bookInventory = intent.getExtras().getString("book_inventory");
        bookTitle = intent.getExtras().getString("book_title");
        requestTimestamp = intent.getExtras().getString("requestTimestamp");
        setContentView(R.layout.activity_list_armadio);
        lv = (ListView) findViewById(R.id.lv_armadio_list);
        spinner = (ProgressBar) findViewById(R.id.list_armadio_spinner);
        spinner.setIndeterminate(true);
        spinner.setVisibility(View.VISIBLE);
        getArmadi();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String item = ((TextView) view).getText().toString();
                String armadioId = armadi.get(position).getId();
                String codice_topografico_completo =
                        armadi.get(position).getCodiceTopograficoCompleto();
                Toast.makeText(ListArmadioActivity.this,
                        "codice topografico" + codice_topografico_completo, Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(ListArmadioActivity.this, LibraryMapsActivity.class);
                myIntent.putExtra("codice_topografico_completo", codice_topografico_completo);
                myIntent.putExtra("library_code", libraryId);
                myIntent.putExtra("book_inventory", bookInventory);
                myIntent.putExtra("book_title", bookTitle);
                myIntent.putExtra("requestTimestamp", requestTimestamp);
                //Try to put armadioId in order to retrieve the related armadio.
                myIntent.putExtra("armadio_id", armadioId);
                ListArmadioActivity.this.startActivity(myIntent);
            }
        });
    }

    private void getArmadi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIListService service = retrofit.create(APIListService.class);
        Call<List<Armadio>> call = service.getArmadi();
        call.enqueue(new Callback<List<Armadio>>() {
            @Override
            public void onResponse(Call<List<Armadio>> call, Response<List<Armadio>> response) {
                List<Armadio> shelves = new ArrayList<Armadio>();
                if (response != null) {
                    shelves = response.body();
                    //armadi = response.body();
                }
                if (shelves.size() != 0) {
                    Collections.sort(shelves, new MyComparator());
                    spinner.setVisibility(View.GONE);
                    for(Armadio s : shelves) {
                        if(s.getLibraryId().equals(libraryId)) {
                            armadiNumbers.add("Armadio numero: " + s.getNumber());
                            armadi.add(s);
                        }
                    }
                    arrayAdapter = new ArrayAdapter<String>(ListArmadioActivity.this,
                            android.R.layout.simple_list_item_1, armadiNumbers);
                    lv.setAdapter(arrayAdapter);
                } else {
                    spinner.setVisibility(View.GONE);
                    dialogServerError();
                }
            }

            @Override
            public void onFailure(Call<List<Armadio>> call, Throwable t) {
                spinner.setVisibility(View.GONE);
                Log.e("onFailure armadio", "Fail to get the json", t);
                dialogServerError();
            }
        });
    }

    private void dialogServerError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListArmadioActivity.this);
        builder.setMessage(R.string.ops)
                .setCancelable(false)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent myIntent = new Intent(ListArmadioActivity.this, MainActivity.class);
                        ListArmadioActivity.this.startActivity(myIntent);
                    }
                });
        builder.create();
        builder.show();
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
}

