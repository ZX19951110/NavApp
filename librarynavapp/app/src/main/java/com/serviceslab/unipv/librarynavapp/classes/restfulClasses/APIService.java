package com.serviceslab.unipv.librarynavapp.classes.restfulClasses;

import com.serviceslab.unipv.librarynavapp.classes.model.Armadio;
import com.serviceslab.unipv.librarynavapp.classes.model.ArmadioAvailability;
import com.serviceslab.unipv.librarynavapp.classes.model.ConfirmationType;
import com.serviceslab.unipv.librarynavapp.classes.model.Library;
import com.serviceslab.unipv.librarynavapp.classes.model.Log;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mikim on 16/02/2017.
 */

public interface APIService {
    @GET("armadio")
    Call<Armadio> getArmadio();

    @GET("library")
    Call<Library> getLibrary();

    @GET("armadio")
    Call<Armadio> getArmadioById(@Query("id") String armadioId);

    @GET("library")
    Call<Library> getLibraryById(@Query("id") String libraryId);

    @POST("log")
    Call<Log> postLog(@Body Log log);

}
