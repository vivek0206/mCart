package com.example.mcart;

public class Pro_content {
    private String Pro_name,Pro_price,Pro_info,seller,img_url;



    public Pro_content(String pro_name, String pro_info, String pro_price, String seller, String img_url) {
        Pro_name = pro_name;
        Pro_info=pro_info;
        Pro_price = pro_price;
        this.seller = seller;
        this.img_url=img_url;
    }
    public Pro_content(){

    }

    public String getPro_name() {
        return Pro_name;
    }

    public void setPro_name(String pro_name) {
        Pro_name = pro_name;
    }

    public String getPro_price() {
        return Pro_price;
    }

    public void setPro_price(String pro_info) {
        Pro_price = pro_info;
    }

    public String getPro_info() {
        return Pro_info;
    }

    public void setPro_info(String pro_info) {
        Pro_info = pro_info;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
