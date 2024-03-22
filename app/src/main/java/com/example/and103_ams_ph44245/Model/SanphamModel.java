package com.example.and103_ams_ph44245.Model;

public class SanPhamModel {
    private String _id;
    private String image,name;
    private int quantity,price,inventory;

    public SanPhamModel() {
    }

    public SanPhamModel(String _id, String image, String name, int quantity, int price, int inventory) {
        this._id = _id;
        this.image = image;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.inventory = inventory;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }
}
