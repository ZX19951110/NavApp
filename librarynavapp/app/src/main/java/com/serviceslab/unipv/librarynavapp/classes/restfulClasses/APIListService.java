package com.serviceslab.unipv.librarynavapp.classes.restfulClasses;

import com.serviceslab.unipv.librarynavapp.classes.model.Armadio;
import com.serviceslab.unipv.librarynavapp.classes.model.ArmadioAvailability;
import com.serviceslab.unipv.librarynavapp.classes.model.Book;
import com.serviceslab.unipv.librarynavapp.classes.model.ConfirmationType;
import com.serviceslab.unipv.librarynavapp.classes.model.Library;
import com.serviceslab.unipv.librarynavapp.classes.model.OperationType;
import com.serviceslab.unipv.librarynavapp.classes.model.Path;
import com.serviceslab.unipv.librarynavapp.classes.model.Waypoint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by mikim on 17/02/2017.
 */

public interface APIListService {
    @GET("waypoint")
    Call<List<Waypoint>> getWaypoints();

    @GET("path")
    Call<List<Path>> getPaths();

    @GET("armadio")
    Call<List<Armadio>> getArmadi();

    @GET("library")
    Call<List<Library>> getLibraries();

    @GET("armadio_availability")
    Call<List<ArmadioAvailability>> getArmadioAvailabilities();

    @GET("confirmation_type")
    Call<List<ConfirmationType>> getConfirmationTypes();

    @GET("operation_type")
    Call<List<OperationType>> getOperationTypes();

    @GET("book")
    Call<List<Book>> getBooks();

}
