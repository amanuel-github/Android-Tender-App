package com.axuminfo.models;

/**
 * Created by User on 2/8/2020.
 */

public class Office_Image {
    private int id;
    private int officeId;
    private byte[] image;

    public Office_Image(int id, int officeId, byte[] image) {
        this.id = id;
        this.officeId = officeId;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
