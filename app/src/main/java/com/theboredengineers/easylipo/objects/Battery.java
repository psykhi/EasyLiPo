package com.theboredengineers.easylipo.objects;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by benoit.hocquet on 05/01/2015.
 * Class representing a battery that can be stored inside a database.
 * Usage example :
 * Battery battery = new Battery("Battery1","Zippy","FlightMax",2200,5,40,new byte[]{0,0,1,0,10,20,30},4,new Date(1000*60*60*24*356*(2014-1970)),25);
 */
public class Battery implements Serializable {

    private static final String TAG = "Battery";

    /**
     * Id of the battery directly related to its place in database.
     */
    private int id = -1;

    private String serverId = "";

    /**
     * Name of the battery set by User (ex : "Battery 2","Old blue battery")
     */
    private String name = "";

    /**
     * Brand of the battery (ex : "Zippy")
     */
    private String brand = "";

    /**
     * Type of battery (ex : "Flightmax")
     */
    private String model = "";

    /**
     * Capacity of the battery in mAh
     */
    private int capacity = 2500;

    /**
     * Charge rate in C
     */
    private int chargeRate = 1;

    /**
     * Discharge rate in C
     */
    private int dischargeRate = 35;

    /**
     * NFC tag
     */
    private NfcTag nfcTag = null;

    /**
     * Rating of the battery (is it good or bad)
     */
    private int rating = 0;

    /**
     * Date of battery purchase
     */
    private Date purchaseDate = null;

    /**
     * Number of cycles for the battery
     */
    private int nbOfCycles = 0;

    /**
     * Number of Cells in series
     */
    private int nbS = 3;

    /**
     * Number of Cells in parallel
     */
    private int nbP = 1;
    /**
     * charged ?
     */
    private boolean charged;

    /**
     * Return a formatted string representing the charge rate
     *
     * @return the charge rate
     */
    public String getFormattedChargeRate() {
        if (chargeRate > 0)
            return Integer.toString(chargeRate) + "C";
        else
            return "";
    }

    /**
     * Return a formatted string representing the discharge rate
     *
     * @return the discharge rate
     */
    public String getFormattedDischargeRate() {
        if (dischargeRate > 0)
            return Integer.toString(dischargeRate) + "C";
        else
            return "";
    }

    /**
     * Return a formatted string representing the capacity
     *
     * @return the capacity
     */
    public String getFormattedCapacity() {
        if (capacity > 0)
            return Integer.toString(capacity) + "mAh";
        else
            return "";
    }


    /**
     * Returns the cell configuration of the battery
     *
     * @return the string representing the config (ex : "3S1P","2S","Unknown").
     */
    public String getFormattedCells() {
        if (nbS < 1)
            return "";
        else if (nbP < 1)
            return Integer.toString(nbS) + "S";
        else
            return Integer.toString(nbS) + "S" + Integer.toString(nbP) + "P";
    }

    @Override
    public String toString() {
        return name + " Local " + getLocalId() + " server " + getServerId();

    }

    public Boolean isLocal() {
        return getServerId().equals("");
    }
    /*Getters and Setters*/
    public int getNbS() {
        return nbS;
    }

    public void setNbS(int nbS) {
        this.nbS = nbS;
    }

    public int getNbP() {
        return nbP;
    }

    public void setNbP(int nbP) {
        this.nbP = nbP;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public int getNbOfCycles() {
        return nbOfCycles;
    }

    public void setNbOfCycles(int nbOfCycles) {
        this.nbOfCycles = nbOfCycles;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public NfcTag getTagID() {
        return nfcTag;
    }

    public void setTagID(NfcTag tagID) {
        this.nfcTag = tagID;
    }

    public int getLocalId() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getChargeRate() {
        return chargeRate;
    }

    public void setChargeRate(int chargeRate) {
        this.chargeRate = chargeRate;
    }

    public int getDischargeRate() {
        return dischargeRate;
    }

    public void setDischargeRate(int dischargeRate) {
        this.dischargeRate = dischargeRate;
    }


    /**
     * Constructor
     *
     * @param name  Username of the battery
     * @param tagID The NFC tag ID corresponding to the battery
     */
    public Battery(String name,
                   NfcTag tagID) {
        this.name = name;
        this.nfcTag = tagID;
    }
    public Battery()
    {
        this.name =  "Battery"+ Calendar.getInstance().getDisplayName(Calendar.DATE,Calendar.SHORT, Locale.ENGLISH);
    }


    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Battery))
            return false;
        else
        {
            if (!isLocal()) {
                return ((Battery) o).getServerId().equals(getServerId());
            }
            else {
                return (((Battery) o).getLocalId() == getLocalId());
            }

        }
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public boolean isCharged() {
        return charged;
    }

    public void setCharged(boolean charged) {
        this.charged = charged;
    }
}
