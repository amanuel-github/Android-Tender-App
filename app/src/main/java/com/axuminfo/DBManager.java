package com.axuminfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.axuminfo.models.Category;
import com.axuminfo.models.Complex_building;
import com.axuminfo.models.Letter;
import com.axuminfo.models.Office;
import com.axuminfo.models.Office_Image;
import com.axuminfo.models.Sub_category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/7/2020.
 */

public class DBManager extends SQLiteOpenHelper {
    private static String DB_NAME="MekelleInfo_3.db";
    private static String complex_buildings="complex_buildings";
    private static String categories="categories";
    private static String sub_categories="sub_categories";
    private static String letters="letters";
    private static String offices="offices";
    private static String offices_images="offices_images";

    public DBManager(Context context) {
        super(context, DB_NAME, null, 1);
        //context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //complex_buildings
        db.execSQL("create table if not exists "+complex_buildings+"(" +
                "id integer primary key AUTOINCREMENT," +
                "name text," +
                "description text," +
                "created_at text," +
                "updated_at text)");
        //categories
        db.execSQL("create table if not exists "+categories+"(" +
                "id integer primary key AUTOINCREMENT," +
                "buildingId integer," +
                "name text," +
                "description text," +
                "created_at text," +
                "updated_at text)");
        //sub_categories
        db.execSQL("create table if not exists "+sub_categories+"(" +
                "id integer primary key AUTOINCREMENT," +
                "categoryId integer," +
                "name text," +
                "description text," +
                "created_at text," +
                "updated_at text)");
        //letters
        db.execSQL("create table if not exists "+letters+"(" +
                "id integer primary key AUTOINCREMENT," +
                "name text," +
                "description text," +
                "created_at text," +
                "updated_at text)");
        //offices
        db.execSQL("create table if not exists "+offices+"(" +
                "id integer primary key AUTOINCREMENT," +
                "buildingId integer," +
                "categoryId integer," +
                "sub_categoryId integer," +
                "letterId integer," +
                "name text," +
                "level integer," +
                "image blob," +
                "number text," +
                "longitude double," +
                "latitude double," +
                "description text," +
                "isApproved text default 'false'," +
                "created_at text," +
                "updated_at text)");
        //offices
        db.execSQL("create table if not exists "+offices_images+"(" +
                "id integer primary key AUTOINCREMENT," +
                "officeId integer," +
                "image blob," +
                "created_at text," +
                "updated_at text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+complex_buildings);
        db.execSQL("drop table if exists "+categories);
        db.execSQL("drop table if exists "+sub_categories);
        db.execSQL("drop table if exists "+letters);
        db.execSQL("drop table if exists "+offices);
        db.execSQL("drop table if exists "+offices_images);

        onCreate(db);
    }

    //Store
    public boolean insert_complex_buildings(int id, String name, String description, String created_at, String updated_at){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        if(id>0)
            contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("created_at", created_at);
        contentValues.put("updated_at", updated_at);

        long status=0;
        if(id>0 && findComplex_building(id)!=null) {
            sqLiteDb.update(complex_buildings, contentValues, "id = ?", new String[]{String.valueOf(id)});
        }else {
            status = sqLiteDb.insert(complex_buildings, null, contentValues);
        }
        return status!=-1;
    }
    public boolean insert_categories(int id, int buildingId, String name, String description, String created_at, String updated_at){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        if(id>0)
            contentValues.put("id", id);
        contentValues.put("buildingId", buildingId);
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("created_at", created_at);
        contentValues.put("updated_at", updated_at);

        long status=0;
        if(id>0 && findCategory(id)!=null) {
            sqLiteDb.update(categories, contentValues, "id = ?", new String[]{String.valueOf(id)});
        }else {
            status = sqLiteDb.insert(categories, null, contentValues);
        }
        return status!=-1;
    }
    public boolean insert_sub_categories(int id, int categoryId, String name, String description, String created_at, String updated_at){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        if(id>0)
            contentValues.put("id", id);
        contentValues.put("categoryId", categoryId);
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("created_at", created_at);
        contentValues.put("updated_at", updated_at);

        long status=0;
        if(id>0 && findSub_category(id)!=null) {
            sqLiteDb.update(sub_categories, contentValues, "id = ?", new String[]{String.valueOf(id)});
        }else {
            status = sqLiteDb.insert(sub_categories, null, contentValues);
        }
        return status!=-1;
    }
    public boolean insert_letters(int id, String name, String description, String created_at, String updated_at){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        if(id>0)
            contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("created_at", created_at);
        contentValues.put("updated_at", updated_at);

        long status=0;
        if(id>0 && findLetter(id)!=null) {
            sqLiteDb.update(letters, contentValues, "id = ?", new String[]{String.valueOf(id)});
        }else {
            status=sqLiteDb.insert(letters, null,contentValues);
        }
        return status!=-1;
    }
    public boolean insert_offices(int id, int buildingId, int categoryId, int sub_categoryId, String name, int level, byte[] image, String number, int letter, double longitude, double latitude, String description, String isApproved, String created_at, String updated_at){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        if(id>0)
            contentValues.put("id", id);
        contentValues.put("buildingId", buildingId);
        contentValues.put("categoryId", categoryId);
        contentValues.put("sub_categoryId", sub_categoryId);
        contentValues.put("name", name);
        contentValues.put("level", level);
        contentValues.put("image", image);
        contentValues.put("number", number);
        contentValues.put("letterId", letter);
        contentValues.put("longitude", longitude);
        contentValues.put("latitude", latitude);
        contentValues.put("description", description);
        contentValues.put("isApproved", isApproved);
        contentValues.put("created_at", created_at);
        contentValues.put("updated_at", updated_at);

        long status=0;
        if(id>0 && findOffice(id)!=null) {
            /*if(isApproved=="false")
                sqLiteDb.delete(offices, "id = ?", new String []{ String.valueOf(id) });
            else*/
                sqLiteDb.update(offices, contentValues, "id = ?", new String[]{String.valueOf(id)});
        }else {
            status=sqLiteDb.insert(offices, null,contentValues);
        }

        /*if(status!=-1){
            Office office=findOfficeByName(name);
            if(office==null)
                return false;
            int officeId=office.getId();
            if(officeId>0){
                return insert_offices_images(0, officeId, image, created_at, updated_at);
            }
        }*/
        return false;
    }
    //Store
    public boolean insert_offices_images(int id, int officeId, byte[] image, String created_at, String updated_at){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        if(id>0)
            contentValues.put("id", id);
        contentValues.put("officeId", officeId);
        contentValues.put("image", image);
        contentValues.put("created_at", created_at);
        contentValues.put("updated_at", updated_at);

        long status=0;
        if(id>0 && findOffices_image(id)!=null) {
            sqLiteDb.update(offices_images, contentValues, "id = ?", new String[]{String.valueOf(id)});
        }else {
            status=sqLiteDb.insert(offices_images, null,contentValues);
        }
        return status!=-1;
    }
    //Update
    public boolean update_categories(int id, int buildingId, String name, String description){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("buildingId", buildingId);
        contentValues.put("name", name);
        contentValues.put("description", description);

        sqLiteDb.update(categories, contentValues, "id = ?", new String[]{ String.valueOf(id) });
        return true;
    }
    //Update
    public boolean update_sub_categories(int id, int categoryId, String name, String description){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("categoryId", categoryId);
        contentValues.put("name", name);
        contentValues.put("description", description);

        sqLiteDb.update(sub_categories, contentValues, "id = ?", new String[]{ String.valueOf(id) });
        return true;
    }
    public boolean update_letters(int id, String name, String description){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);

        sqLiteDb.update(letters, contentValues, "id = ?", new String[]{ String.valueOf(id) });
        return true;
    }
    public boolean update_complex_buildings(int id, String name, String description){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("description", description);

        sqLiteDb.update(complex_buildings, contentValues, "id = ?", new String[]{ String.valueOf(id) });
        return true;
    }
    public boolean update_offices(int id, int buildingId, int categoryId, int sub_categoryId, String name, int level, byte[] image, String number, int letter, double longitude, double latitude, String description){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("id", id);
        contentValues.put("buildingId", buildingId);
        contentValues.put("categoryId", categoryId);
        contentValues.put("sub_categoryId", sub_categoryId);
        contentValues.put("name", name);
        contentValues.put("level", level);
        if(image!=null)
            contentValues.put("image", image);
        contentValues.put("number", number);
        contentValues.put("letterId", letter);
        contentValues.put("longitude", longitude);
        contentValues.put("latitude", latitude);
        contentValues.put("description", description);
        sqLiteDb.update(offices, contentValues, "id = ?", new String[]{ String.valueOf(id) });
        return true;
    }
    public boolean update_office_images(int id, int officeId, byte[] image){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("officeId", officeId);
        contentValues.put("image", image);

        sqLiteDb.update(offices_images, contentValues, "id = ?", new String[]{ String.valueOf(id) });
        return true;
    }
    //Index
    public List<Complex_building> complex_buildings(){
        List<Complex_building> items=new ArrayList<>();

        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        Cursor result=sqLiteDb.rawQuery("select * from "+complex_buildings+" order by name", null);

        while(result.moveToNext()){
            Complex_building complex_building=new Complex_building(result.getInt(0), result.getString(1), result.getString(2), result.getString(3), result.getString(4));
            items.add(complex_building);
        }

        return items;
    }
    public List<Letter> letters(){
        List<Letter> items=new ArrayList<>();

        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        Cursor result=sqLiteDb.rawQuery("select * from "+letters+" order by name", null);

        while(result.moveToNext()){
            Letter letter=new Letter(result.getInt(0), result.getString(1), result.getString(2), result.getString(3), result.getString(4));
            items.add(letter);
        }

        return items;
    }

    public List<Category> categories(){
        List<Category> items=new ArrayList<>();

        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        Cursor result=sqLiteDb.rawQuery("select * from "+categories+" order by name", null);

        while(result.moveToNext()){
            Category category=new Category(result.getInt(0), result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
            items.add(category);
        }

        return items;
    }

    public List<Sub_category> sub_categories(){
        List<Sub_category> items=new ArrayList<>();

        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        Cursor result=sqLiteDb.rawQuery("select * from "+sub_categories+" order by name", null);

        while(result.moveToNext()){
            Sub_category item=new Sub_category(result.getInt(0), result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
            items.add(item);
        }

        return items;
    }

    public List<Sub_category> sub_categories(int categoryId){
        List<Sub_category> items=new ArrayList<>();

        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        Cursor result=sqLiteDb.rawQuery("select * from "+sub_categories+" where categoryId=? order by name", new String[]{""+categoryId});

        while(result.moveToNext()){
            Sub_category item=new Sub_category(result.getInt(0), result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
            items.add(item);
        }

        return items;
    }

    public List<Office> offices(){
        String isApprovedStr="true";//"false"
        List<Office> items=new ArrayList<>();

        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        Cursor result=sqLiteDb.rawQuery("select * from "+offices+" where isApproved=? order by name", new String[]{isApprovedStr});

        while(result.moveToNext()){
            int id=result.getInt(0);
            Office office=new Office(result.getInt(0), result.getInt(1), result.getInt(2), result.getInt(3), result.getInt(4), result.getString(5), result.getInt(6), result.getBlob(7), result.getString(8), result.getDouble(9), result.getDouble(10), result.getString(11), offices_images(id), result.getString(13), result.getString(14), result.getString(12));
            items.add(office);
        }

        return items;
    }
    public List<Office> offices(int sub_categoryId){
        String isApprovedStr="true";//"false"
        List<Office> items=new ArrayList<>();

        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        Cursor result=sqLiteDb.rawQuery("select * from "+offices+" where sub_categoryId=? AND isApproved=? order by name", new String[]{""+sub_categoryId, isApprovedStr});

        while(result.moveToNext()){
            int id=result.getInt(0);
            Office office=new Office(result.getInt(0), result.getInt(1), result.getInt(2), result.getInt(3), result.getInt(4), result.getString(5), result.getInt(6), result.getBlob(7), result.getString(8), result.getDouble(9), result.getDouble(10), result.getString(11), offices_images(id), result.getString(13), result.getString(14), result.getString(12));
            items.add(office);
        }

        return items;
    }

    public List<Office_Image> offices_images(int officeId){
        List<Office_Image> items=new ArrayList<>();

        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        Cursor result=sqLiteDb.rawQuery("select * from "+offices_images+" where officeId=? order by id desc", new String[]{""+officeId});

        while(result.moveToNext()){
            Office_Image office_image=new Office_Image(result.getInt(0), result.getInt(1), result.getBlob(2));
            items.add(office_image);
        }

        return items;
    }
    public List<Office_Image> offices_images(){
        List<Office_Image> items=new ArrayList<>();

        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        Cursor result=sqLiteDb.rawQuery("select * from "+offices_images, null);

        while(result.moveToNext()){
            Office_Image office_image=new Office_Image(result.getInt(0), result.getInt(1), result.getBlob(2));
            items.add(office_image);
        }

        return items;
    }
    public List<Integer> levels(){
        List<Integer> items=new ArrayList<>();
        int level=0;
        while(level <= 100){
            items.add(level++);
        }

        return items;
    }

    //Delete
    public int delete_category(long id){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        int count=sqLiteDb.delete(categories, "id = ?", new String []{ String.valueOf(id) });
        return count;
    }
    //Delete
    public int delete_sub_category(long id){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        int count=sqLiteDb.delete(sub_categories, "id = ?", new String []{ String.valueOf(id) });
        return count;
    }
    public int delete_complex_building(long id){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        int count=sqLiteDb.delete(complex_buildings, "id = ?", new String []{ String.valueOf(id) });
        return count;
    }
    public int delete_letter(long id){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        int count=sqLiteDb.delete(letters, "id = ?", new String []{ String.valueOf(id) });
        return count;
    }
    public int delete_office(long id){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        int count=sqLiteDb.delete(offices, "id = ?", new String []{ String.valueOf(id) });
        return count;
    }
    public int delete_offices_images(long id){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        int count=sqLiteDb.delete(offices_images, "id = ?", new String []{ String.valueOf(id) });
        return count;
    }
    //Find
    public Office findOffice(int id){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        Cursor result=sqLiteDb.rawQuery("select * from "+offices+" where id=? limit 1", new String[]{""+id});

        while(result.moveToNext()){
            Office office=new Office(result.getInt(0), result.getInt(1), result.getInt(2), result.getInt(3), result.getInt(4), result.getString(5), result.getInt(6), result.getBlob(7), result.getString(8), result.getDouble(9), result.getDouble(10), result.getString(11), offices_images(id), result.getString(13), result.getString(14), result.getString(12));
            return office;
        }
        return null;
    }
    public Office_Image findOffices_image(int id){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        Cursor result=sqLiteDb.rawQuery("select * from "+offices_images+" where id=? limit 1", new String[]{String.valueOf(id)});

        while(result.moveToNext()){
            Office_Image office_image=new Office_Image(result.getInt(0), result.getInt(1), result.getBlob(2));
            return office_image;
        }
        return null;
    }
    public Category findCategory(int id){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        Cursor result=sqLiteDb.rawQuery("select * from "+categories+" where id=? limit 1", new String[]{""+id});

        while(result.moveToNext()){
            Category category=new Category(result.getInt(0), result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
            return category;
        }
        return null;
    }
    public Sub_category findSub_category(int id){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        Cursor result=sqLiteDb.rawQuery("select * from "+sub_categories+" where id=? limit 1", new String[]{""+id});

        while(result.moveToNext()){
            Sub_category item=new Sub_category(result.getInt(0), result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
            return item;
        }
        return null;
    }
    public Letter findLetter(int id){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        Cursor result=sqLiteDb.rawQuery("select * from "+letters+" where id=? limit 1", new String[]{""+id});

        while(result.moveToNext()){
            Letter letter=new Letter(result.getInt(0), result.getString(1), result.getString(2), result.getString(3), result.getString(4));
            return letter;
        }
        return null;
    }
    public Complex_building findComplex_building(int id){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        Cursor result=sqLiteDb.rawQuery("select * from "+complex_buildings+" where id=? limit 1", new String[]{""+id});

        while(result.moveToNext()){
            Complex_building complex_building=new Complex_building(result.getInt(0), result.getString(1), result.getString(2), result.getString(3), result.getString(4));
            return complex_building;
        }
        return null;
    }

    public Office findOfficeByName(String name){
        SQLiteDatabase sqLiteDb=this.getWritableDatabase();
        Cursor result=sqLiteDb.rawQuery("select * from "+offices+" where name=? limit 1", new String[]{name});

        while(result.moveToNext()){
            int id=result.getInt(0);
            Office office=new Office(result.getInt(0), result.getInt(1), result.getInt(2), result.getInt(3), result.getInt(4), result.getString(5), result.getInt(6), result.getBlob(7), result.getString(8), result.getDouble(9), result.getDouble(10), result.getString(11), offices_images(id), result.getString(13), result.getString(14), result.getString(12));
            return office;
        }
        return null;
    }
}
