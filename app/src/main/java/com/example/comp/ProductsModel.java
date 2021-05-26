package com.example.comp;

public class ProductsModel
{
    private String title_product;
    private String price;
    private String id_product;
    private String img_url;


    public ProductsModel() {
    }

    public ProductsModel(String title_product, String price, String img_url, String id_product) {
        this.title_product = title_product;
        this.price = price;
        this.img_url = img_url;
        this.id_product = id_product;
    }
    public String getTitle_product() {
        return title_product;
    }

    public void setTitle_product(String title_product) {
        this.title_product = title_product;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }


}

