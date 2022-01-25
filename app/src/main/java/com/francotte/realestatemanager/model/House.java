package com.francotte.realestatemanager.model;

import android.content.ContentValues;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class House implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String category;
    private String district;
    private boolean isEuro;
    private int price;
    private int area;
    private int numberOfRooms;
    private int numberOfBathrooms;
    private int numberOfBedrooms;
    private int school;
    private int shopping;
    private int publicTransport;
    private int swimmingPool;
    private String description;
    private String illustration;
    private String address;
    private boolean available;
    private String dateOfEntry;
    @Nullable
    private String dateOfSale;
    private String realEstateAgent;

    //Constructor
    @Ignore
    public House() {
    }

    public House(String category, String district, boolean b, int price, int area, int numberOfRooms, int numberOfBathrooms, int numberOfBedRooms, int school, int shopping, int publicTransport, int swimmingPool, String description, String picture, boolean available, String dateOfEntry, String dateOfSale, String realEstateAgent) {
    }

    public House(String category, String district, boolean isEuro, int price, int area, int numberOfRooms, int numberOfBathrooms, int numberOfBedrooms, int school,
                 int shopping, int publicTransport, int swimmingPool, String description, String illustration, String address, boolean available, String dateOfEntry,
                 @Nullable String dateOfSale, String realEstateAgent) {
        this.category = category;
        this.district = district;
        this.isEuro = isEuro;
        this.price = price;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.numberOfBedrooms = numberOfBedrooms;
        this.school = school;
        this.shopping = shopping;
        this.publicTransport = publicTransport;
        this.swimmingPool = swimmingPool;
        this.description = description;
        this.illustration = illustration;
        this.address = address;
        this.available = available;
        this.dateOfEntry = dateOfEntry;
        this.dateOfSale = dateOfSale;
        this.realEstateAgent = realEstateAgent;
    }

    //Getter

    public Long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getDistrict() {
        return district;
    }

    public boolean isEuro() {
        return isEuro;
    }

    public int getPrice() {
        return price;
    }

    public int getArea() {
        return area;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public int getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public int getSchool() {
        return school;
    }

    public int getShopping() {
        return shopping;
    }

    public int getPublicTransport() {
        return publicTransport;
    }

    public int getSwimmingPool() {
        return swimmingPool;
    }

    public String getDescription() {
        return description;
    }

    public String getIllustration() {
        return illustration;
    }

    public String getAddress() {
        return address;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getDateOfEntry() {
        return dateOfEntry;
    }

    @Nullable
    public String getDateOfSale() {
        return dateOfSale;
    }

    public String getRealEstateAgent() {
        return realEstateAgent;
    }

    //Setter

    //Utils
    public static House fromContentValues(ContentValues values) {
        final House house = new House();
        if (values.containsKey("category")) house.setCategory(values.getAsString("category"));
        if (values.containsKey("district")) house.setDistrict(values.getAsString("district"));
        if (values.containsKey("price")) house.setCategory(values.getAsString("price"));
        if (values.containsKey("isEuro")) house.setEuro(values.getAsBoolean("isEuro"));
        if (values.containsKey("area")) house.setArea(values.getAsInteger("area"));
        if (values.containsKey("numberOfRooms"))
            house.setNumberOfRooms(values.getAsInteger("numberOfRooms"));
        if (values.containsKey("numberOfBathrooms"))
            house.setNumberOfBathrooms(values.getAsInteger("numberOfBathrooms"));
        if (values.containsKey("numberOfBedrooms"))
            house.setNumberOfBedrooms(values.getAsInteger("numberOfBedrooms"));
        if (values.containsKey("school")) house.setSchool(values.getAsInteger("school"));
        if (values.containsKey("shopping")) house.setShopping(values.getAsInteger("shopping"));
        if (values.containsKey("publicTransport"))
            house.setPublicTransport(values.getAsInteger("publicTransport"));
        if (values.containsKey("swimmingPool"))
            house.setSwimmingPool(values.getAsInteger("swimmingPool"));
        if (values.containsKey("description"))
            house.setDescription(values.getAsString("description"));
        if (values.containsKey("illustration"))
            house.setIllustration(values.getAsString("illustration"));
        if (values.containsKey("address")) house.setAddress(values.getAsString("address"));
        if (values.containsKey("dateOfEntry"))
            house.setDateOfEntry(values.getAsString("dateOfEntry"));
        if (values.containsKey("available")) house.setAvailable(values.getAsBoolean("available"));
        if (values.containsKey("dateOfSale")) house.setDateOfSale(values.getAsString("dateOfSale"));
        if (values.containsKey("realEstateAgent"))
            house.setRealEstateAgent(values.getAsString("realEstateAgent"));
        if (values.containsKey("houseId")) house.setId(values.getAsLong("houseId"));
        return house;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setEuro(boolean euro) {
        isEuro = euro;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public void setNumberOfBathrooms(int numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    public void setNumberOfBedrooms(int numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public void setSchool(int school) {
        this.school = school;
    }

    public void setShopping(int shopping) {
        this.shopping = shopping;
    }

    public void setPublicTransport(int publicTransport) {
        this.publicTransport = publicTransport;
    }

    public void setSwimmingPool(int swimmingPool) {
        this.swimmingPool = swimmingPool;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIllustration(String illustration) {
        this.illustration = illustration;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setDateOfEntry(String dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    public void setRealEstateAgent(String realEstateAgent) {
        this.realEstateAgent = realEstateAgent;
    }

    public void setDateOfSale(@Nullable String dateOfSale) {
        this.dateOfSale = dateOfSale;
    }
}

