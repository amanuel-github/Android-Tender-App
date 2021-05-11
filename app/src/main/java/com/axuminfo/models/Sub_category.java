package com.axuminfo.models;

import com.axuminfo.MainActivity;

/**
 * Created by User on 2/8/2020.
 */

public class Sub_category {
    private int id;
    private int categoryId;
    private String name;
    private String description;
    private String created_at;
    private String updated_at;

    public Sub_category(int id, int categoryId, String name, String description, String created_at, String updated_at) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return this.name;
    }

    public Category category(){
        for(Category category: MainActivity.db.categories()){
            if(this.getCategoryId()==category.getId())
                return category;
        }
        return null;
    }
}
