package com.serviceslab.unipv.librarynavapp.classes.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mikim on 14/02/2017.
 */

public class Log {
    @SerializedName("id")
    private String id;

    @SerializedName("operation_code")
    private String operationCode;

    @SerializedName("confirmation_code")
    private String confirmationTypeId;

    @SerializedName("request_timestamp")
    private String requestTimestamp;

    @SerializedName("confirmation_timestamp")
    private String confirmationTimestamp;

    @SerializedName("start_nav_timestamp")
    private String startNavTimestamp;

    @SerializedName("end_nav_timestamp")
    private String endNavTimestamp;

    @SerializedName("book_id")
    private String bookId;

    @SerializedName("library_id")
    private String libraryId;

    @SerializedName("armadio_id")
    private String armadioId;

    public Log() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public String getConfirmationTypeId() {
        return confirmationTypeId;
    }

    public void setConfirmationTypeId(String confirmationTypeId) {
        this.confirmationTypeId = confirmationTypeId;
    }

    public String getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(String requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    public String getConfirmationTimestamp() {
        return confirmationTimestamp;
    }

    public void setConfirmationTimestamp(String confirmationTimestamp) {
        this.confirmationTimestamp = confirmationTimestamp;
    }

    public String getStartNavTimestamp() {
        return startNavTimestamp;
    }

    public void setStartNavTimestamp(String startNavTimestamp) {
        this.startNavTimestamp = startNavTimestamp;
    }

    public String getEndNavTimestamp() {
        return endNavTimestamp;
    }

    public void setEndNavTimestamp(String endNavTimestamp) {
        this.endNavTimestamp = endNavTimestamp;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public String getArmadioId() {
        return armadioId;
    }

    public void setArmadioId(String armadioId) {
        this.armadioId = armadioId;
    }

    @Override
    public String toString() {
        return "Log{" +
                //"id=" + id +
                //", operationCode=" + operationCode +
                "operationCode=" + operationCode +
                ", confirmationTypeId=" + confirmationTypeId +
                ", requestTimestamp='" + requestTimestamp + '\'' +
                ", confirmationTimestamp='" + confirmationTimestamp + '\'' +
                ", startNavTimestamp='" + startNavTimestamp + '\'' +
                ", endNavTimestamp='" + endNavTimestamp + '\'' +
                ", bookId=" + bookId +
                ", libraryId=" + libraryId +
                ", armadioId=" + armadioId +
                '}';
    }
}
