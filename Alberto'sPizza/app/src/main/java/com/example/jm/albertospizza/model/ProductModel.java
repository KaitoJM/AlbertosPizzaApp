package com.example.jm.albertospizza.model;

/**
 * Created by JM on 3/12/2017.
 */

public class ProductModel {
    private int id;
    private double price_value;
    private int type_value;
    private String name;
    private String price;
    private String type;
    private String description;
    private int unit_value;
    private String unit;
    private String photo;
    private String medium;
    private String small;
    private String smaller;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice_value() {
        return price_value;
    }

    public void setPrice_value(double price_value) {
        this.price_value = price_value;
    }

    public int getType_value() {
        return type_value;
    }

    public void setType_value(int type_value) {
        this.type_value = type_value;
    }

    public int getUnit_value() {
        return unit_value;
    }

    public void setUnit_value(int unit_value) {
        this.unit_value = unit_value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getSmaller() {
        return smaller;
    }

    public void setSmaller(String smaller) {
        this.smaller = smaller;
    }
}
