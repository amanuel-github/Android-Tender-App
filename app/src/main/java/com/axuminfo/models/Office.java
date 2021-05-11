package com.axuminfo.models;

import com.axuminfo.MainActivity;

import java.util.List;

/**
 * Created by User on 2/8/2020.
 */

public class Office {
    private int id;
    private int buildingId;
    private int categoryId;
    private int sub_categoryId;
    private int letterId;
    private String name;
    private int level;
    private String number;
    private double longitude;
    private double latitude;
    private byte[] image;
    private String description;
    private List<Office_Image> officeImageList;
    private String created_at;
    private String updated_at;
    private String isApproved;
    public List<Office_Image> getOfficeImageList() {
        return officeImageList;
    }

    public void setOfficeImageList(List<Office_Image> officeImageList) {
        this.officeImageList = officeImageList;
    }

    public Office(int id, int buildingId, int categoryId, int sub_categoryId, int letterId, String name, int level, byte[] image, String number, double longitude, double latitude, String description, List<Office_Image> office_imageList, String created_at, String updated_at, String isApproved) {
        this.id = id;
        this.buildingId = buildingId;
        this.categoryId = categoryId;
        this.sub_categoryId = sub_categoryId;
        this.letterId = letterId;
        this.name = name;
        this.level = level;
        this.number = number;
        this.longitude = longitude;
        this.latitude = latitude;
        this.image = image;
        this.description = description;
        this.officeImageList=office_imageList;
        this.created_at = created_at;
        this.updated_at=updated_at;
        this.isApproved=isApproved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSub_categoryId() {
        return sub_categoryId;
    }

    public void setSub_categoryId(int sub_categoryId) {
        this.sub_categoryId = sub_categoryId;
    }

    public int getLetterId() {
        return letterId;
    }

    public void setLetterId(int letterId) {
        this.letterId = letterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Complex_building complexBuilding(){
        for(Complex_building complexBuilding: MainActivity.db.complex_buildings()){
            if(this.getBuildingId()==complexBuilding.getId())
                return complexBuilding;
        }
        return null;
    }
    public Category category(){
        for(Category obj: MainActivity.db.categories()){
            if(this.getCategoryId()==obj.getId())
                return obj;
        }
        return null;
    }
    public Sub_category sub_category(){
        for(Sub_category obj: MainActivity.db.sub_categories()){
            if(this.getSub_categoryId()==obj.getId())
                return obj;
        }
        return null;
    }
    public Letter letter(){
        for(Letter obj: MainActivity.db.letters()){
            if(this.getLetterId()==obj.getId())
                return obj;
        }
        return null;
    }

    public byte[] getBytes()
    {
            if (/*image != "" &&*/ image != null)
            {


               // byte[] img = Convert.FromBase64String(image);
                return null;//img;
            }
            return null;
    }
}
