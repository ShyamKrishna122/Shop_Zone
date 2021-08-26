package com.example.ecom.ui.products;

public class ProductModel {
    private final int productId;
    private final String productName;
    private final String productDescription;
    private final String productImage;
    private final String productCategory;
    private final double productRating;
    private final double productPrice;
    private final double productDiscount;
    private final double productWeight;
    private final String[] productAvailableColors;
    private final double[] productAvailableSizes;


    public ProductModel(int productId, String productName, String productDescription, String productImage, String productCategory, double rating, double productPrice, double discount, double weight, String[] availableColors, double[] availableSizes) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productImage = productImage;
        this.productCategory = productCategory;
        this.productRating = rating;
        this.productPrice = productPrice;
        this.productDiscount = discount;
        this.productWeight = weight;
        this.productAvailableColors = availableColors;
        this.productAvailableSizes = availableSizes;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public double getProductRating() {
        return productRating;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public double getProductDiscount() {
        return productDiscount;
    }

    public double getProductWeight() {
        return productWeight;
    }

    public String[] getProductAvailableColors() {
        return productAvailableColors;
    }

    public double[] getProductAvailableSizes() {
        return productAvailableSizes;
    }
}
