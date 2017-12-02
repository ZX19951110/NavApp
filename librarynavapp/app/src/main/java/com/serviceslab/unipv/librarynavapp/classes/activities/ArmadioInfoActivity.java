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
import com.serviceslab.unipv.librarynavapp.classes.model.Library;
import com.serviceslab.unipv.librarynavapp.classes.restfulClasses.APIListService;
import com.serviceslab.unipv.librarynavapp.classes.restfulClasses.APIService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArmadioInfoActivity extends Activity {

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
    private String library_name;
    private String library_address;
    private String library_email;
    private String library_phone;
    private String library_description;
    private final String h1 = "<h1>";
    private final String h1Close = "</h1>";
    private final String h2 = "<h2>";
    private final String h2Close = "</h2>";
    private final String p = "<p>";
    private final String pClose = "</p>";
    private final String body = "<body>";
    private final String bodyClose = "</body>";
    private final String a = "<a>";
    private final String aClose = "</a>";
    private final String ul = "<ul>";
    private final String ulClose = "</ul>";
    private final String li = "<li>";
    private final String liClose = "</li>";
    private String description;
    private TextView infoTextView;
    private String libraryId;
    private String armadioId;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        //codice_topografico_completo = intent.getExtras().getString("codice_topografico_completo");
        libraryId = intent.getExtras().getString(libraryId);
        armadioId = intent.getExtras().getString("armadio_id");
        setContentView(R.layout.activity_armadio_info);
        spinner = (ProgressBar) findViewById(R.id.armadio_info_spinner);
        spinner.setIndeterminate(true);
        spinner.setVisibility(View.VISIBLE);
        getArmadioById(armadioId);
        infoTextView = (TextView)findViewById(R.id.armadio_info_text);

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

    //Getting all shelves
    private void getArmadio(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIListService service = retrofit.create(APIListService.class);
        Call<List<Armadio>> call = service.getArmadi();
        call.enqueue(new Callback<List<Armadio>>() {
            @Override
            public void onResponse(Call<List<Armadio>> call, Response<List<Armadio>> response) {
                if(response != null) {
                    try {
                        List<Armadio> armadi = response.body();
                        Log.i("ArmadioInfoActivity", "armadi??");
                        for (Armadio a : armadi) {
                            //TODO change this
                            /**if (a.getCodiceTopograficoCompleto().equals(codice_topografico_completo)) {
                                armadio = a;
                                getLibrary("1");
                            } else if (a.getId().equals("830c1bae-ed50-4ca2-ad97-dbf786924bf6")){
                                armadio = a;
                                getLibrary("1");
                                Log.i("ArmadioInfoActivity", "armadi??");
                            }**/
                            if (a.getId().equals("830c1bae-ed50-4ca2-ad97-dbf786924bf6")){
                                armadio = a;
                                getLibrary("1");
                                Log.i("ArmadioInfoActivity", "armadi??");
                            }
                        }
                        //createArmadioDescriptionText(armadio, infoTextView);
                    } catch (Exception e) {
                        Log.e("onResponse", "There is en error withing the server answer");
                        e.printStackTrace();
                        description = body + h1 + "No description available for this armadio."
                                + h1Close + bodyClose;
                        infoTextView.setText(Html.fromHtml(description));
                    }
                } else {
                    description = body + h1 + "No description available for this armadio."
                            + h1Close + bodyClose;
                    Log.i("onResponse armadio", "body is null??");
                    infoTextView.setText(Html.fromHtml(description));
                }
            }
            @Override
            public void onFailure(Call<List<Armadio>> call, Throwable t) {
                Log.e("onFailure armadio", "Fail to get the json", t);
                description = body + h1 + "No description available for this armadio."
                        + h1Close + bodyClose;
                infoTextView.setText(Html.fromHtml(description));
            }
        });
    }

    //Get single armadio using its id
    private void getArmadioById(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService service = retrofit.create(APIService.class);
        Call<Armadio> call = service.getArmadioById(id);
        call.enqueue(new Callback<Armadio>() {
            @Override
            public void onResponse(Call<Armadio> call, Response<Armadio> response) {
                if(response != null) {
                    try {
                        armadio = response.body();
                        String libraryID = armadio.getLibraryId();
                        getLibraryById(libraryID);
                    } catch (Exception e) {
                        Log.e("onResponseArmadioById", "There is en error within the server answer");
                        e.printStackTrace();
                        description = body + h1 + "No description available for this armadio."
                                + h1Close + bodyClose;
                        infoTextView.setText(Html.fromHtml(description));
                    }
                } else {
                    description = body + h1 + "No description available for this armadio."
                            + h1Close + bodyClose;
                    Log.i("onResponse armadio", "body is null??");
                    infoTextView.setText(Html.fromHtml(description));
                }
            }
            @Override
            public void onFailure(Call<Armadio> call, Throwable t) {
                Log.e("onFailure armadio", "Fail to get the json", t);
                description = body + h1 + "No description available for this armadio."
                        + h1Close + bodyClose;
                infoTextView.setText(Html.fromHtml(description));
            }
        });
    }

    //Getting all the libraries
    private void getLibrary(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIListService service = retrofit.create(APIListService.class);
        Call<List<Library>> call = service.getLibraries();
        call.enqueue(new Callback<List<Library>>() {
            @Override
            public void onResponse(Call<List<Library>> call, Response<List<Library>> response) {
                if(response!= null) {
                    List<Library> libraries = response.body();
                    Log.i("onResponse library", "body is null??");
                    //Debug purpose/////
                    for (Library l : libraries) {
                        if(l.getName().equals("Laboratorio di Robotica e Computer Services")){
                            library = l;
                            createArmadioDescriptionText(armadio,infoTextView);
                            ImageView iv = (ImageView)findViewById(R.id.armadio_image_view);
                            String imgName = "IMG_20170316_171629472.jpg";
                            iv.setImageBitmap(getBitmapFromAssets(imgName));
                        }
                    }
                    ////////////////////////
                } else {
                    description = body + h1 + "No description available for this Library."
                            + h1Close + bodyClose;
                    Log.i("onResponse library", "body is null??");
                    infoTextView.setText(Html.fromHtml(description));
                }
            }
            @Override
            public void onFailure(Call<List<Library>> call, Throwable t) {
                Log.e("onFailure armadio", "Fail to get the json", t);
                description = body + h1 + "No description available for this armadio."
                        + h1Close + bodyClose;
                infoTextView.setText(Html.fromHtml(description));
            }
        });
    }

    //Getting single library using its id
    private void getLibraryById(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService service = retrofit.create(APIService.class);
        Call<Library> call = service.getLibraryById(id);
        call.enqueue(new Callback<Library>() {
            @Override
            public void onResponse(Call<Library> call, Response<Library> response) {
                if(response != null) {
                    try {
                        library = response.body();
                        spinner.setVisibility(View.VISIBLE);
                        createArmadioDescriptionText(armadio,infoTextView);
                        ImageView iv = (ImageView)findViewById(R.id.armadio_image_view);
                        String imgName = "shelfphoto.png";
                        iv.setImageBitmap(getBitmapFromAssets(imgName));
                    } catch (Exception e) {
                        Log.e("onResponseLibraryById", "There is en error withing the server answer");
                        e.printStackTrace();
                    }
                } else {
                    Log.e("onResponse", "There is en error withing the server answer");
                }
            }
            @Override
            public void onFailure(Call<Library> call, Throwable t) {
                Log.e("onFailureLibraryById", "Fail to get the json", t);
                description = body + h1 + "No description available for the library."
                        + h1Close + bodyClose;
                infoTextView.setText(Html.fromHtml(description));
            }
        });
    }


    public void createArmadioDescriptionText(Armadio armadioRetrieved, TextView textView){
        String htmlDescr;
        String bodyTag = "<body>";
        String bodyCloseTag = "</body>";
        String h1Tag = "<h1>";
        String h1CloseTag = "</h1>";
        String h2Tag="<h2>";
        String h2CloseTag = "</h2>";
        String pTag = "<p>";
        String pCloseTag = "</p>";
        String ulTag = "<ul>";
        String ulCloseTag = "</ul>";
        String liTag = "<li>";
        String liCloseTag = "</li>";
        String armadioLabel = "Armadio numero: ";
        String cTopoLabel = "Codice topografico: ";
        String availabilityLabel = "Accessibilita': ";

        if (armadio.getAvailabilityId().equals("d3c9bea3-265b-4470-bbfb-95599020fd48")) {
            armadio.setAvailabilityId("Not accessible");
        } else if(armadio.getAvailabilityId().equals("69698387-6309-4908-9754-6f14ed16127e")) {
            armadio.setAvailabilityId("Accessible");
        } else if(armadio.getAvailabilityId().equals("d59beb9b-706f-425e-b6ca-a2511772e757")) {
            armadio.setAvailabilityId("Not accessible");
        }
        description = bodyTag + h1Tag + library.getName() + h1CloseTag
                + h1Tag + armadioRetrieved.getSezioneBiblioteca() + h1CloseTag
                + h2Tag + armadioLabel + h2CloseTag
                + pTag + armadioRetrieved.getNumber() + pCloseTag
                + h2Tag + cTopoLabel + h2CloseTag
                + pTag + armadio.getCodiceTopograficoCompleto() + pCloseTag
                + h2Tag + availabilityLabel + h2CloseTag
                + pTag + armadio.getAvailabilityId() + pCloseTag
                + bodyClose;
        textView.setText(Html.fromHtml(description));
    }
}
