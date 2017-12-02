package com.serviceslab.unipv.librarynavapp.classes.model;

import android.support.v7.app.AlertDialog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mikim on 06/02/2017.
 */

public class Armadio {
    @SerializedName("id")
    private String id;

    @SerializedName("number")
    private String number;

    @SerializedName("codice_topografico_completo")
    private String codiceTopograficoCompleto;

    @SerializedName("sezione_citta")
    private String sezioneCitta;

    @SerializedName("sezione_ente")
    private String sezioneEnte;

    @SerializedName("sezione_biblioteca")
    private String sezioneBiblioteca;

    @SerializedName("sezione_sede")
    private String sezioneSede;

    @SerializedName("sezione_piano")
    private String sezionePiano;

    @SerializedName("sezione_locale")
    private String sezioneLocale;

    @SerializedName("sezione_armadio")
    private String sezioneArmadio;

    @SerializedName("sezione_ripiano")
    private String sezioneRipiano;

    @SerializedName("availability_id")
    private String availabilityId;

    @SerializedName("library_id")
    private String libraryId;

    public Armadio() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCodiceTopograficoCompleto() {
        return codiceTopograficoCompleto;
    }

    public void setCodiceTopograficoCompleto(String codiceTopograficoCompleto) {
        this.codiceTopograficoCompleto = codiceTopograficoCompleto;
    }

    public String getSezioneCitta() {
        return sezioneCitta;
    }

    public void setSezioneCitta(String sezioneCitta) {
        this.sezioneCitta = sezioneCitta;
    }

    public String getSezioneEnte() {
        return sezioneEnte;
    }

    public void setSezioneEnte(String sezioneEnte) {
        this.sezioneEnte = sezioneEnte;
    }

    public String getSezioneBiblioteca() {
        return sezioneBiblioteca;
    }

    public void setSezioneBiblioteca(String sezioneBiblioteca) {
        this.sezioneBiblioteca = sezioneBiblioteca;
    }

    public String getSezioneSede() {
        return sezioneSede;
    }

    public void setSezioneSede(String sezioneSede) {
        this.sezioneSede = sezioneSede;
    }

    public String getSezionePiano() {
        return sezionePiano;
    }

    public void setSezionePiano(String sezionePiano) {
        this.sezionePiano = sezionePiano;
    }

    public String getSezioneLocale() {
        return sezioneLocale;
    }

    public void setSezioneLocale(String sezioneLocale) {
        this.sezioneLocale = sezioneLocale;
    }

    public String getSezioneArmadio() {
        return sezioneArmadio;
    }

    public void setSezioneArmadio(String sezioneArmadio) {
        this.sezioneArmadio = sezioneArmadio;
    }

    public String getSezioneRipiano() {
        return sezioneRipiano;
    }

    public void setSezioneRipiano(String sezioneRipiano) {
        this.sezioneRipiano = sezioneRipiano;
    }

    public String getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(String availabilityId) {
        this.availabilityId = availabilityId;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    @Override
    public String toString() {
        return "Armadio{" +
                "id=" + id +
                ", number=" + number +
                ", codiceTopograficoCompleto='" + codiceTopograficoCompleto + '\'' +
                ", sezioneCitta='" + sezioneCitta + '\'' +
                ", sezioneEnte='" + sezioneEnte + '\'' +
                ", sezioneBiblioteca='" + sezioneBiblioteca + '\'' +
                ", sezioneSede='" + sezioneSede + '\'' +
                ", sezionePiano='" + sezionePiano + '\'' +
                ", sezioneLocale='" + sezioneLocale + '\'' +
                ", sezioneArmadio='" + sezioneArmadio + '\'' +
                ", sezioneRipiano='" + sezioneRipiano + '\'' +
                ", availabilityId=" + availabilityId +
                ", libraryId=" + libraryId +
                '}';
    }
}