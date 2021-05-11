package com.axuminfo.services;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.axuminfo.BitmapUtility;
import com.axuminfo.MainActivity;
import com.axuminfo.R;
import com.axuminfo.fragments.Main_categories_menuFragment;
import com.axuminfo.models.Category;
import com.axuminfo.models.Complex_building;
import com.axuminfo.models.Letter;
import com.axuminfo.models.Office;
import com.axuminfo.models.Sub_category;
import com.axuminfo.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 12/30/2019.
 */

public class BackgroundWorker extends AsyncTask<String, Void, String> {
    private Context context;
    private String urlString= /*"http://192.168.43.115:81/mekelle_info_directory";*/"http://axuminfo.com/mekelle-info-directory";
    AlertDialog alertDialog=null;

    String TAG_Validation="Validation_Error";

    public BackgroundWorker(Context context){
        this.context=context;
    }

    @Override
    protected String doInBackground(String... params) {
        String OPERATION_TYPE=params[0];

        if(!MainActivity.checkConnection()){
            MainActivity.mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alertDialog.setMessage(MainActivity.appContext.getString(R.string.no_internet));
                    alertDialog.show();
                }
            });
            return null;
        }

        if(OPERATION_TYPE.equals("create_complex_buildings")){
            String encodedData=encodeComplex_buildingsData(params);
            if(encodedData==null){
                Log.d("Test1", "Empty encodedData");
                return null;
            }

            /*try {

                URL url=new URL(urlString+);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(encodedData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                Log.d("Test1", "outputStream: "+outputStream.toString());

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));

                String result="", line="";

                while((line=bufferedReader.readLine())!=null){
                    result+=line;
                }

                bufferedReader.close();
                inputStream.close();

                httpURLConnection.disconnect();
                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            return sendPostData("/create_complex_buildings", encodedData);
        }
        if(OPERATION_TYPE.equals("create_categories")){
            String encodedData=encodeCategoriesData(params);
            if(encodedData==null){
                Log.d("Test1", "Empty encodedData");
                return null;
            }

            return sendPostData("/create_categories", encodedData);
        }
        if(OPERATION_TYPE.equals("create_sub_categories")){
            String encodedData=encodeSub_categoriesData(params);
            if(encodedData==null){
                Log.d("Test1", "Empty encodedData");
                return null;
            }

            return sendPostData("/create_sub_categories", encodedData);
        }
        if(OPERATION_TYPE.equals("create_letters")){
            String encodedData=encodeLettersData(params);
            if(encodedData==null){
                return null;
            }

            return sendPostData("/create_letters", encodedData);
        }
        if(OPERATION_TYPE.equals("create_offices")){
            String encodedData=encodeOfficesData(params);
            if(encodedData==null){
                return null;
            }

            return sendPostData("/create_offices", encodedData);
        }
        if(OPERATION_TYPE.equals("add_office_image")){
            String encodedData=encodeImageData(params);
            if(encodedData==null){
                return null;
            }

            return sendPostData("/add_office_image", encodedData);
        }

        if(OPERATION_TYPE.equals("load_server_data")){
            try {

                URL url=new URL(urlString+"/load_server_data");
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                Log.d("Test1", "httpURLConnection: "+httpURLConnection.getErrorStream());

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));

                String result="", line="";
                while((line=bufferedReader.readLine())!=null){
                    result+=line+"\n";
                }

                bufferedReader.close();
                inputStream.close();

                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(OPERATION_TYPE.equals("login")){
            String encodedData=encodeLoginData(params);
            if(encodedData==null){
                return null;
            }

            return sendPostData("/remote_login", encodedData);
        }

        /*else if(OPERATION_TYPE.equals("submit_payment")){
            String encodedData=validatePaymentData(params);
            if(encodedData!=null){
                return null;
            }

            try {

                URL url=new URL("http://192.168.43.115:8000/payment_records/create_payment/");
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(encodedData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));

                String result="", line="";

                while((line=bufferedReader.readLine())!=null){
                    result+=line+"\n";
                }

                bufferedReader.close();
                inputStream.close();

                httpURLConnection.disconnect();
                Log.d("BackGD", "doInBackground: "+result);

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(OPERATION_TYPE.equals("load_data")){
            try {

                URL url=new URL("http://192.168.43.115:81/BusnessDirectory/complex_buildings-index");
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                Log.d("Test1", "httpURLConnection: "+httpURLConnection.getErrorStream());

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));

                String result="", line="";
                Log.d("Test1", "bufferedReader: "+bufferedReader.readLine());
                while((line=bufferedReader.readLine())!=null){
                    result+=line+"\n";
                }

                bufferedReader.close();
                inputStream.close();

                httpURLConnection.disconnect();
                Log.d("Test1", "doInBackground: "+result);

                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        return null;
    }

    private String encodeComplex_buildingsData(String... params) {
        String encodedData=null;
        String name=params[1];
        String description=params[2];
        try {

            encodedData= URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")+"&" +
                    URLEncoder.encode("description", "UTF-8")+"="+URLEncoder.encode(description, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedData;
    }
    private String encodeCategoriesData(String... params) {
        String encodedData=null;
        String buildingId=params[1];
        String name=params[2];
        String description=params[3];
        try {

            encodedData= URLEncoder.encode("buildingId", "UTF-8")+"="+URLEncoder.encode(/*buildingId*/"", "UTF-8")+"&" +URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")+"&" +
                    URLEncoder.encode("description", "UTF-8")+"="+URLEncoder.encode(description, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedData;
    }

    private String encodeSub_categoriesData(String... params) {
        String encodedData=null;
        String categoryId=params[1];
        String name=params[2];
        String description=params[3];
        try {

            encodedData= URLEncoder.encode("categoryId", "UTF-8")+"="+URLEncoder.encode(categoryId, "UTF-8")+"&" +URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")+"&" +
                    URLEncoder.encode("description", "UTF-8")+"="+URLEncoder.encode(description, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedData;
    }
    private String encodeLettersData(String... params) {
        String encodedData=null;
        String name=params[1];
        String description=params[2];
        try {

            encodedData= URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")+"&" +
                    URLEncoder.encode("description", "UTF-8")+"="+URLEncoder.encode(description, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedData;
    }
    private String encodeLoginData(String... params) {
        String encodedData=null;
        String username=params[1];
        String password=params[2];
        try {

            encodedData= URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(username, "UTF-8")+"&" +
                    URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedData;
    }

    private String encodeOfficesData(String... params) {
        String encodedData=null;

        String buildingId=params[1];
        String categoryId=params[2];
        String sub_categoryId=params[3];
        String name=params[4];
        String level=params[5];
        String imageByteString=params[6];
        String number=params[7];
        String letter=params[8];
        String longitude=params[9];
        String latitude=params[10];
        String description=params[11];

        try {

            encodedData= URLEncoder.encode("buildingId", "UTF-8")+"="+URLEncoder.encode(""/*buildingId*/, "UTF-8")+"" +
                    "&" +URLEncoder.encode("categoryId", "UTF-8")+"="+URLEncoder.encode(categoryId, "UTF-8")+"" +
                    "&" +URLEncoder.encode("sub_categoryId", "UTF-8")+"="+URLEncoder.encode(sub_categoryId, "UTF-8")+"" +
                    "&" +URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")+"" +
                    "&" +URLEncoder.encode("level", "UTF-8")+"="+URLEncoder.encode(""/*level*/, "UTF-8")+"" +
                    "&" +URLEncoder.encode("imageByteString", "UTF-8")+"="+URLEncoder.encode(""/*imageByteString*/, "UTF-8")+"" +
                    "&" +URLEncoder.encode("number", "UTF-8")+"="+URLEncoder.encode(""/*number*/, "UTF-8")+"" +
                    "&" +URLEncoder.encode("letter", "UTF-8")+"="+URLEncoder.encode(""/*letter*/, "UTF-8")+"" +
                    "&" +URLEncoder.encode("longitude", "UTF-8")+"="+URLEncoder.encode(longitude, "UTF-8")+"" +
                    "&" +URLEncoder.encode("latitude", "UTF-8")+"="+URLEncoder.encode(latitude, "UTF-8")+"" +
                    "&" +URLEncoder.encode("description", "UTF-8")+"="+URLEncoder.encode(description, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedData;
    }
    private String encodeImageData(String... params) {
        String encodedData=null;

        String officeId=params[1];
        String imageByteString=params[2];

        try {

            encodedData= URLEncoder.encode("officeId", "UTF-8")+"="+URLEncoder.encode(officeId, "UTF-8")+"" +
                    "&" +URLEncoder.encode("imageByteString", "UTF-8")+"="+URLEncoder.encode(imageByteString, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedData;
    }

    private String validatePaymentData(String... params) {
        String encodedData=null;

        String game=params[1];
        String game_prices=params[2];
        String fullName=params[3];
        String phoneNumber=params[4];
        String paidAmount=params[5];
        String homeOrAwayStatus=params[6];
        String recipientAccountNumber=params[7];

        if(game==null || game_prices==null || fullName==null || phoneNumber==null || paidAmount==null || homeOrAwayStatus==null || recipientAccountNumber==null){
            Log.i(TAG_Validation, "Please fill the required fields");
        }
        if("".equals(game) || "".equals(game_prices) || "".equals(fullName) || "".equals(phoneNumber) || "".equals(paidAmount) || "".equals(homeOrAwayStatus) || "".equals(recipientAccountNumber)){
            Log.i(TAG_Validation, "Please fill the required fields");
        }

        try {

            encodedData= URLEncoder.encode("game", "UTF-8")+"="+URLEncoder.encode(game, "UTF-8")+"&" +
                    URLEncoder.encode("game_prices", "UTF-8")+"="+URLEncoder.encode(game_prices, "UTF-8")+"&" +
                    URLEncoder.encode("fullName", "UTF-8")+"="+URLEncoder.encode(fullName, "UTF-8")+"&" +
                    URLEncoder.encode("phoneNumber", "UTF-8")+"="+URLEncoder.encode((phoneNumber!=null? phoneNumber:""), "UTF-8")+"&" +
                    URLEncoder.encode("paidAmount", "UTF-8")+"="+URLEncoder.encode(paidAmount, "UTF-8")+"&" +
                    URLEncoder.encode("homeOrAwayStatus", "UTF-8")+"="+URLEncoder.encode(homeOrAwayStatus, "UTF-8")+"&" +
                    URLEncoder.encode("recipientAccountNumber", "UTF-8")+"="+URLEncoder.encode(recipientAccountNumber, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedData;
    }

    @Override
    protected void onPreExecute() {
        //close any alert dialog
        if(MainActivity.a_dialog!=null)
            MainActivity.a_dialog.cancel();

        Log.d("BackGD", "onPreExecute");
        alertDialog=new AlertDialog.Builder(MainActivity.appContext)
        .setTitle(R.string.app_name)
                .setCancelable(false)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
        .create();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        //close sync dialog
        if(MainActivity.a_dialog!=null)
            alertDialog.hide();

        //HomeFragment.syncDataBtn.setText(MainActivity.mainActivity.getString(R.string.sync_data))

        if(result==null){
            MainActivity.mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("Test1", "Empty Result");
                    alertDialog.hide();
                    alertDialog.setMessage(MainActivity.appContext.getString(R.string.network_problem));
                    alertDialog.show();
                }
            });
            return;
        }


        Log.d("BackGD", "onPostExecute "+result);
        JSONObject json = null;
        //JSONArray responseGameArray, responseGame_optionArray, responseClubArray, home_or_away_statusArray;
        String responseData="GAMES\n";
        try {
            json = new JSONObject(result);

            if (json.getString("type").equalsIgnoreCase("notification")){
                responseData=json.getString("message");//"1 Raw inserted";
                if(!json.getBoolean("success")){
                   // Toast.makeText(MainActivity.appContext, "Something went wrong. Please Try again.", Toast.LENGTH_LONG).show();
                    alertDialog.setMessage(MainActivity.appContext.getString(R.string.operation_failed)+"\n"+responseData);
                    alertDialog.show();
                    return;
                }

                alertDialog.setMessage(String.valueOf(responseData));
                alertDialog.show();
            }
            else if (json.getString("type").equalsIgnoreCase("data")) {
                JSONArray complex_buildingsArray = json.getJSONArray("complex_buildings");
                JSONArray categoriesArray = json.getJSONArray("categories");
                JSONArray sub_categoriesArray = json.getJSONArray("sub_categories");
                JSONArray lettersArray = json.getJSONArray("letters");
                JSONArray officesArray = json.getJSONArray("offices");
                JSONArray offices_imagesArray = json.getJSONArray("offices_images");



                //clear
                clearDeletedComplex_buildings(complex_buildingsArray);
                clearDeletedCategories(categoriesArray);
                clearDeletedSub_categories(sub_categoriesArray);
                clearDeletedLetters(lettersArray);
               clearDeletedOffices(officesArray);

                    for (int i = 0; i < officesArray.length(); i++) {
                    JSONObject object = officesArray.getJSONObject(i);
                    int id = Integer.valueOf(object.getString("id"));
                    int buildingId = Integer.valueOf(object.getString("buildingId"));
                    int categoryId = Integer.valueOf(object.getString("categoryId"));
                    int sub_categoryId = Integer.valueOf(object.getString("sub_categoryId"));
                    int letterId = Integer.valueOf(object.getString("letterId"));
                    String name = object.getString("name");
                    int level = Integer.valueOf(object.getString("level"));
                    String image = object.getString("image");
                    String number = object.getString("number");
                    double longitude = Double.valueOf(object.getString("longitude"));
                    double latitude = Double.valueOf(object.getString("latitude"));
                    String description = object.getString("description");
                    String isApproved = object.getString("isApproved");
                    String created_at=object.getString("created_at");
                    String updated_at=object.getString("updated_at");

                    byte[] imageBytes= BitmapUtility.getBytes(MainActivity.decodeToImage(image));
                    boolean success=MainActivity.db.insert_offices(id, buildingId, categoryId, sub_categoryId, name, level, imageBytes, number, letterId, longitude, latitude, description, isApproved, created_at, updated_at);
                }
                for (int i = 0; i < offices_imagesArray.length(); i++) {
                    JSONObject object = offices_imagesArray.getJSONObject(i);
                    int id = Integer.valueOf(object.getString("id"));
                    int officeId = Integer.valueOf(object.getString("officeId"));
                    String image = object.getString("image");
                    String created_at=object.getString("created_at");
                    String updated_at=object.getString("updated_at");
                    byte[] imageBytes= BitmapUtility.getBytes(MainActivity.decodeToImage(image));
                    boolean success=MainActivity.db.insert_offices_images(id, officeId, imageBytes, created_at, updated_at);
                }

                for (int i = 0; i < complex_buildingsArray.length(); i++) {
                    JSONObject object = complex_buildingsArray.getJSONObject(i);
                    int id = Integer.valueOf(object.getString("id"));
                    String name = object.getString("name");
                    String description = object.getString("description");
                    String created_at=object.getString("created_at");
                    String updated_at=object.getString("updated_at");

                    boolean success=MainActivity.db.insert_complex_buildings(id, name, description, created_at, updated_at);
                }

                for (int i = 0; i < categoriesArray.length(); i++) {
                    JSONObject object = categoriesArray.getJSONObject(i);
                    int id = Integer.valueOf(object.getString("id"));
                    String name = object.getString("name");
                    int buildingId = Integer.valueOf(object.getString("buildingId"));
                    String description = object.getString("description");
                    String created_at=object.getString("created_at");
                    String updated_at=object.getString("updated_at");

                    boolean success=MainActivity.db.insert_categories(id, buildingId, name, description, created_at, updated_at);
                }

                for (int i = 0; i < sub_categoriesArray.length(); i++) {
                    JSONObject object = sub_categoriesArray.getJSONObject(i);
                    int id = Integer.valueOf(object.getString("id"));
                    String name = object.getString("name");
                    int categoryId = Integer.valueOf(object.getString("categoryId"));
                    String description = object.getString("description");
                    String created_at=object.getString("created_at");
                    String updated_at=object.getString("updated_at");

                    boolean success=MainActivity.db.insert_sub_categories(id, categoryId, name, description, created_at, updated_at);
                }

                for (int i = 0; i < lettersArray.length(); i++) {
                    JSONObject object = lettersArray.getJSONObject(i);
                    int id = Integer.valueOf(object.getString("id"));
                    String name = object.getString("name");
                    String description = object.getString("description");
                    String created_at=object.getString("created_at");
                    String updated_at=object.getString("updated_at");

                    boolean success=MainActivity.db.insert_letters(id, name, description, created_at, updated_at);
                }

                alertDialog.setMessage("Data Synced");
                alertDialog.show();

                MainActivity.mainActivity.replaceFragment(new Main_categories_menuFragment());
            }
            if (json.getString("type").equalsIgnoreCase("authentication")){
                responseData=json.getString("message");//"1 Raw inserted";
                if(!json.getBoolean("success")){
                    User.isLoggedIn=false;
                    alertDialog.setMessage(responseData);
                    alertDialog.show();
                    return;
                }

                MainActivity.mainActivity.showMenuItem(true);


                User.isLoggedIn=true;
                alertDialog.setMessage(String.valueOf(responseData));
                alertDialog.show();

                MainActivity.mainActivity.replaceFragment(new Main_categories_menuFragment());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static String seachPriceString(JSONArray jsonArray, String key){
        for (int i=0; i<jsonArray.length();i++){
            try {
                JSONObject json = new JSONObject(jsonArray.get(i).toString());
                String name=json.getString("name");
                if(name.equalsIgnoreCase(key.trim())) {
                    return json.getString("priceInBirr");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return "";
    }

    private String sendPostData(String partialUrl, String encodedData){
        try {

            URL url=new URL(urlString+partialUrl);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            bufferedWriter.write(encodedData);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));

            String result="", line="";

            while((line=bufferedReader.readLine())!=null){
                result+=line;
            }

            bufferedReader.close();
            inputStream.close();

            httpURLConnection.disconnect();
            return result;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void clearDeletedComplex_buildings(JSONArray objectsArray) throws JSONException {
        List<Integer> newObjectsId=new ArrayList<>();
        for (int i = 0; i < objectsArray.length(); i++) {
            JSONObject object = objectsArray.getJSONObject(i);
            int id = Integer.valueOf(object.getString("id"));
            newObjectsId.add(id);
        }

        for (Complex_building obj: MainActivity.db.complex_buildings()) {
            if(!this.existsInArray(obj.getId(), newObjectsId))
                MainActivity.db.delete_complex_building(obj.getId());
        }
    }
    public void clearDeletedCategories(JSONArray objectsArray) throws JSONException {
        List<Integer> newObjectsId=new ArrayList<>();
        for (int i = 0; i < objectsArray.length(); i++) {
            JSONObject object = objectsArray.getJSONObject(i);
            int id = Integer.valueOf(object.getString("id"));
            newObjectsId.add(id);
        }

        for (Category obj: MainActivity.db.categories()) {
            if(!this.existsInArray(obj.getId(), newObjectsId))
                MainActivity.db.delete_category(obj.getId());
        }
    }
    public void clearDeletedSub_categories(JSONArray objectsArray) throws JSONException {
        List<Integer> newObjectsId=new ArrayList<>();
        for (int i = 0; i < objectsArray.length(); i++) {
            JSONObject object = objectsArray.getJSONObject(i);
            int id = Integer.valueOf(object.getString("id"));
            newObjectsId.add(id);
        }

        for (Sub_category obj: MainActivity.db.sub_categories()) {
            if(!this.existsInArray(obj.getId(), newObjectsId))
                MainActivity.db.delete_sub_category(obj.getId());
        }
    }
    public void clearDeletedLetters(JSONArray objectsArray) throws JSONException {
        List<Integer> newObjectsId=new ArrayList<>();
        for (int i = 0; i < objectsArray.length(); i++) {
            JSONObject object = objectsArray.getJSONObject(i);
            int id = Integer.valueOf(object.getString("id"));
            newObjectsId.add(id);
        }

        for (Letter obj: MainActivity.db.letters()) {
            if(!this.existsInArray(obj.getId(), newObjectsId))
                MainActivity.db.delete_letter(obj.getId());
        }
    }
    public void clearDeletedOffices(JSONArray objectsArray) throws JSONException {
        List<Integer> newObjectsId=new ArrayList<>();
        for (int i = 0; i < objectsArray.length(); i++) {
            JSONObject object = objectsArray.getJSONObject(i);
            int id = Integer.valueOf(object.getString("id"));
            newObjectsId.add(id);
        }

        for (Office obj: MainActivity.db.offices()) {
            if(!this.existsInArray(obj.getId(), newObjectsId))
                MainActivity.db.delete_office(obj.getId());
        }
    }


    public boolean existsInArray(int item, List<Integer> arr){
        for (int i: arr){
            if(i==item)
                return true;
        }

        return  false;
    }
}
