package com.axuminfo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.axuminfo.fragments.Index_categoryFragment;
import com.axuminfo.fragments.Index_officeFragment;
import com.axuminfo.fragments.Index_sub_categoryFragment;
import com.axuminfo.fragments.Main_categories_menuFragment;
import com.axuminfo.fragments.Search_officeFragment;
import com.axuminfo.models.Office;
import com.axuminfo.models.User;
import com.axuminfo.services.BackgroundWorker;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Stack<Fragment> backFragmentStack;
    public static Context appContext;
    public static DBManager db;
    public static MainActivity mainActivity;

    public static ImageView mSelectedPic;
    public static String imageByteString = "";
    public static byte[] imageBytes;
    public static boolean appCloseChoice = false;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    public static Office selectedOffice;
    public static boolean to_index=true;
    public static User loggedUser=null;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    public static NavigationView navigationView;
    public static AlertDialog a_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appContext=MainActivity.this;
        mainActivity=this;
        db=new DBManager(this);

        backFragmentStack = new Stack<Fragment>();

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        // Tie DrawerLayout events to the ActionBarToggle
        drawerLayout.addDrawerListener(drawerToggle);

        navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*drawerToggle=new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerToggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();*/

        //Opend default fragment ONLY at APP Start
       if(savedInstanceState==null) {
           replaceFragment(new Main_categories_menuFragment());
           //navigationView.setCheckedItem(R.id.nav_home);
       }

        this.showMenuItem(false);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,  R.string.navigation_drawer_close);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            if (backFragmentStack.size() <= 1)
            {
                closeApp();
            }
            else if (backFragmentStack.size() > 0)
            {
                getSupportFragmentManager().popBackStack();
                Fragment currentFragment = backFragmentStack.pop();
            }
            //super.onBackPressed();
        }
    }

    public void closeApp()
    {
        String message = "Are you sure to close this application? ";
        AlertDialog.Builder a_builder=new AlertDialog.Builder(MainActivity.appContext);
        a_builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.Yes_Continue, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        finishAffinity();
                        System.exit(0);
                    }
                })
                .setNegativeButton(R.string.No_Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        a_dialog=a_builder.create();
        a_dialog.setTitle(R.string.Alert_Title);
        a_dialog.show();




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            /*case R.id.nav_map:
                replaceFragment(new MyMapFragment());
                *//*Intent i=new Intent(this, MapActivity.class);
                startActivity(i);*//*
                break;*/
            case R.id.nav_home:
                //replaceFragment(new HomeFragment());
                replaceFragment(new Main_categories_menuFragment());
                break;
            case R.id.nav_sync:
                BackgroundWorker backgroundWorker=new BackgroundWorker(MainActivity.appContext);
                String OPERATION_TYPE="load_server_data";

                showAlertDialog(getString(R.string.loading_anim), getString(R.string.OK), null);

                backgroundWorker.execute(OPERATION_TYPE);
                break;
            case R.id.nav_search_office:
                replaceFragment(new Search_officeFragment());
                break;
            /*case R.id.nav_complex_building:
                //replaceFragment(new Create_complex_buildingFragment());
                replaceFragment(new Index_complex_buildingFragment());
                break;
            case R.id.nav_letter:
                //replaceFragment(new Create_letterFragment());
                replaceFragment(new Index_letterFragment());
                break;*/
            case R.id.nav_category:
                //replaceFragment(new Create_categoryFragment());
                replaceFragment(new Index_categoryFragment());
                break;
            case R.id.nav_sub_category:
                //replaceFragment(new Create_sub_categoryFragment());
                replaceFragment(new Index_sub_categoryFragment());
                break;
            case R.id.nav_office:
                //replaceFragment(new Create_officeFragment());
                replaceFragment(new Index_officeFragment());
                break;
            /*case R.id.nav_login:
                replaceFragment(new LoginFragment());
                break;*/
            case R.id.nav_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Share To");
                sendIntent.setType("text/plain");
                Intent.createChooser(sendIntent, "Share via");
                startActivity(sendIntent);
                break;
            /*case R.id.nav_send:
                Toast.makeText(this, "Send", Toast.LENGTH_LONG).show();
                break;*/

        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

        public void removeInnerFragments(){
            FragmentManager fm = getFragmentManager();
            android.app.Fragment xmlFragment = fm.findFragmentById(R.id.mapArea);
            if (xmlFragment != null) {
                fm.beginTransaction().remove(xmlFragment).commit();
            }
        }
    public void replaceFragment(Fragment newFragment){

        removeInnerFragments();

        backFragmentStack.push(newFragment);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newFragment).addToBackStack(null).commit();
    }
    //Checking App Permissions
    public static boolean checkIfAlreadyhavePermission() {
        int CALL_PHONE = ContextCompat.checkSelfPermission(MainActivity.appContext, Manifest.permission.CALL_PHONE);
        int READ_SMS = ContextCompat.checkSelfPermission(MainActivity.appContext, Manifest.permission.READ_SMS);
        int READ_PHONE_STATE = ContextCompat.checkSelfPermission(MainActivity.appContext, Manifest.permission.READ_PHONE_STATE);
        int GET_ACCOUNTS = ContextCompat.checkSelfPermission(MainActivity.appContext, Manifest.permission.GET_ACCOUNTS);
        int INTERNET = ContextCompat.checkSelfPermission(MainActivity.appContext, Manifest.permission.INTERNET);
        int ACCESS_NETWORK_STATE = ContextCompat.checkSelfPermission(MainActivity.appContext, Manifest.permission.ACCESS_NETWORK_STATE);

        if (ACCESS_NETWORK_STATE == PackageManager.PERMISSION_GRANTED && INTERNET == PackageManager.PERMISSION_GRANTED && CALL_PHONE == PackageManager.PERMISSION_GRANTED && READ_SMS == PackageManager.PERMISSION_GRANTED && READ_PHONE_STATE == PackageManager.PERMISSION_GRANTED && GET_ACCOUNTS == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    public void requestAppPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.GET_ACCOUNTS},
                1);
    }

//Local Data
    static String LocalDataPreference="LocalDataPreference";
    static String phoneNumber="phoneNumber";
    public static void setPhoneNumber(String phoneNum){
        SharedPreferences p=appContext.getSharedPreferences(LocalDataPreference,Context.MODE_PRIVATE);
        SharedPreferences.Editor e=p.edit();
        e.putString(phoneNumber,phoneNum);
        e.commit();
    }
    public static String getPhoneNumber(){
        SharedPreferences sharedPreferences=appContext.getSharedPreferences(LocalDataPreference, Context.MODE_PRIVATE);
        return sharedPreferences.getString(phoneNumber, "");
    }

    //--End of Local Data

    public static boolean showAlertDialog(String message, String positive, String negative){
        final boolean[] result = {false};
        AlertDialog.Builder a_builder=new AlertDialog.Builder(MainActivity.appContext);
        a_builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result[0] =true;
                        dialog.cancel();
                    }
                })
                .setNegativeButton(negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result[0] =false;
                        dialog.cancel();
                    }
                });

        AlertDialog a_dialog=a_builder.create();
        a_dialog.setTitle(R.string.Alert_Title);
        a_dialog.show();

        return result[0];
    }

    //Check if there is Internet Connection
    public static boolean checkConnection() {
        if(true)
        return true;

        ConnectivityManager connectivityManager = (ConnectivityManager) MainActivity.appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        String result = "";
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    void imageDecoder(){
        //encode image to base64
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        //Bitmap bitmap1= ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        //OR
        Bitmap bitmap2= BitmapFactory.decodeResource(getResources(), R.drawable.ic_home);
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes=baos.toByteArray();
        String imageString= Base64.encodeToString(imageBytes, Base64.DEFAULT);

        //decode base64 to image
        /*imageBytes=Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedImage=BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);*/
        Bitmap decodedImage=decodeToImage(imageString);

        ImageView imageIv=null;
        imageIv.setImageBitmap(decodedImage);
    }


    public void SelectPic(ImageView selectedPic)
    {
        mSelectedPic = selectedPic;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.startActivityForResult(Intent.createChooser(intent, "Select item image"), 0);
    }


    public boolean showMenuItem(boolean visible) {
        Menu myMenu=navigationView.getMenu();
        //myMenu.findItem(R.id.nav_complex_building).setVisible(visible);
        myMenu.findItem(R.id.nav_category).setVisible(visible);
        myMenu.findItem(R.id.nav_sub_category).setVisible(visible);
        //myMenu.findItem(R.id.nav_letter).setVisible(visible);
        myMenu.findItem(R.id.nav_office).setVisible(visible);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            imageUri = data.getData();
            mSelectedPic.setImageURI(imageUri);
        }

        if (resultCode == RESULT_OK) {
            InputStream stream = null;
            try {
                stream = getContentResolver().openInputStream(data.getData());
                /*mSelectedPic.setImageBitmap(null);
                mSelectedPic.setImageBitmap(this.decodeBitmapFromStream(data.getData(), 100, 100));
*/
                //GC.Collect();
                //*//*
                //MemoryStream memoryStream = new MemoryStream();
                ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
                BitmapFactory.decodeStream(stream).compress(Bitmap.CompressFormat.JPEG, 10, memoryStream);
                //bitmap.Compress(Bitmap.CompressFormat.Webp, 100, memoryStream);

                imageBytes = memoryStream.toByteArray();
                Bitmap bitmap=(ResizeBitmap.decodeSampledBitmapFromResource(getResources(), imageBytes, 50,50));
                imageBytes=BitmapUtility.getBytes(bitmap);

//                imageByteString = Convert.ToBase64String(picByteData);
//                GC.Collect();
               //imageByteString = new Base64CODEC().convertToBase64(BitmapUtility.getImage(imageBytes));
                imageByteString = new Base64CODEC().convertToBase64(bitmap);
                    //Bitmap bitmap=MainActivity.decodeToImage(imageByteString);
                bitmap=ResizeBitmap.decodeSampledBitmapFromResource(getResources(), imageBytes, 200, 200);
                mSelectedPic.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200, 200, false));

                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
/*

    private void WebClient_UploadValuesCompleted(object sender, UploadValuesCompletedEventArgs e)
    {
        RunOnUiThread(()=>{
                Console.WriteLine(Encoding.UTF8.GetString(e.Result));
            });
    }
*/
    private Bitmap decodeBitmapFromStream(Uri data, int requestedWidth, int requestedHeight) throws FileNotFoundException {
        InputStream stream = getContentResolver().openInputStream(data);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream);

        options.inSampleSize = CalculateInSampleSize(options, requestedHeight, requestedWidth);

        stream = getContentResolver().openInputStream(data);
        options.inJustDecodeBounds = false;
        Bitmap bitMap = BitmapFactory.decodeStream(stream, null, options);

        return bitMap;
    }
    private int CalculateInSampleSize(BitmapFactory.Options options, int requestedHeight, int requestedWidth)
    {
        int height = options.outHeight;
        int width = options.outWidth;
        int insumpleSize = 1;

        if(height>requestedHeight || width > requestedWidth)
        {
            int halfHeight= height/2;
            int halfWidth=width/2;

            while((halfHeight/insumpleSize)>requestedHeight && (halfWidth / insumpleSize) > requestedWidth)
            {
                insumpleSize ^= 2;
            }
        }
        return insumpleSize;
    }

    //---------toget Screen Width and Height
    public int ConvertPixelsToDp(float pixelValue)
    {
        int dp = (int)((pixelValue) / getResources().getDisplayMetrics().density);
        return dp;
    }


/*

    public int[] getScreen_Width_and_Height()
    {
        var metrics = Resources.DisplayMetrics;
        var widthInDp = ConvertPixelsToDp(metrics.WidthPixels);
        var heightInDp = ConvertPixelsToDp(metrics.HeightPixels);
        return new int[] { widthInDp, heightInDp };
    }
*/
    public void closeKeyboard()
    {
        InputMethodManager inputManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }


    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        closeKeyboard();
        return true;
    }

    public void clearFragmentBackStack()
    {
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount() - 1; i++)
        {
            getSupportFragmentManager().popBackStack();
        }
    }


    /*---------------*/
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public static Bitmap decodeToImage(String imageString){
        //decode base64 to image
        byte[] imageBytes=Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedImage=BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }

    public static void manageImageViewScale(View v, boolean isImageFitToScreen)
    {
        ImageView imageImageView=(ImageView)v;
        int img_width = MainActivity.mainActivity.ConvertPixelsToDp(imageImageView.getWidth());
        int img_height = MainActivity.mainActivity.ConvertPixelsToDp(imageImageView.getHeight());

        if (isImageFitToScreen)
        {
            //imageImageView.LayoutParameters=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WrapContent, FrameLayout.LayoutParams.WrapContent);
            imageImageView.setLayoutParams(new FrameLayout.LayoutParams(img_height, img_width));
            imageImageView.setAdjustViewBounds(true);
        }
        else
        {
            imageImageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            //imageImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    public static void emptyImageBytes(){
        //empty
        MainActivity.imageBytes=null;
    }
}
