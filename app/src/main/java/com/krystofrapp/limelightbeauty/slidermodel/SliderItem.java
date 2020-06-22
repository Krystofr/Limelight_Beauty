package com.krystofrapp.limelightbeauty.slidermodel;

public class SliderItem {

    //private String description;
    private int image;
/*
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    */

    public SliderItem() {
    }

    public SliderItem(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
