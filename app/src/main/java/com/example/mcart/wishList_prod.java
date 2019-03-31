package com.example.mcart;

public class wishList_prod {
    private String Product_id;

    public wishList_prod(String product_id) {
        Product_id = product_id;
    }

    public wishList_prod() {
    }

    public String getProduct_id() {
        return Product_id;
    }

    public void setProduct_id(String product_id) {
        Product_id = product_id;
    }
}
