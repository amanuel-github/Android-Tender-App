package com.axuminfo.fragments;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.axuminfo.Base64CODEC;
import com.axuminfo.BitmapUtility;
import com.axuminfo.MainActivity;
import com.axuminfo.R;
import com.axuminfo.ResizeBitmap;
import com.axuminfo.adapters.ImageAdapter;
import com.axuminfo.models.Office;
import com.axuminfo.models.Office_Image;
import com.axuminfo.services.BackgroundWorker;

import java.util.ArrayList;

public class Show_office_detailFragment extends Fragment {

    private TextView nameTf, descriptionTf, buildingIdTf, numberTf, longitudeTf, latitudeTf, categoryIdTf, sub_categoryIdTf, letterIdTf, levelTf;
    ImageView imageView;
    boolean isImageFitToScreen = false;

    RecyclerView recyclerView;
    ArrayList<Bitmap> liste_received;
    static Office office;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_office_detail, container, false);
        //if(office==null)
            office=Index_officeFragment.selectedItem;

        nameTf = (TextView) view.findViewById(R.id.name);
        /*imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.manageImageViewScale(v, isImageFitToScreen);
                isImageFitToScreen = !isImageFitToScreen;
            }
        });
        imageView.setVisibility(View.GONE);*/

        numberTf = (TextView) view.findViewById(R.id.number);
        longitudeTf = (TextView) view.findViewById(R.id.longitude);
        latitudeTf = (TextView) view.findViewById(R.id.latitude);
        descriptionTf = (TextView) view.findViewById(R.id.description);

        buildingIdTf = (TextView) view.findViewById(R.id.buildingId);
        categoryIdTf = (TextView) view.findViewById(R.id.categoryId);
        sub_categoryIdTf = (TextView) view.findViewById(R.id.sub_categoryId);
        letterIdTf = (TextView) view.findViewById(R.id.letterId);
        levelTf = (TextView) view.findViewById(R.id.level);

        nameTf.setText(office.getName());
        descriptionTf.setText(!"".equals(office.getDescription())? office.getDescription():" .....");
        //imageView.setImageBitmap(MainActivity.decodeToImage(Index_officeFragment.selectedItem.getImage()));
        Bitmap bitmap = BitmapUtility.getImage(office.getImage());//MainActivity.decodeToImage(Index_officeFragment.selectedItem.getImage());
        /*imageView.setImageBitmap(null);
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200, 200, false));*/
        longitudeTf.setText(String.valueOf(office.getLongitude()));
        latitudeTf.setText(String.valueOf(office.getLatitude()));
        //buildingIdTf.setText(office.complexBuilding().toString());
        categoryIdTf.setText(office.category().toString());
        sub_categoryIdTf.setText(office.sub_category().toString());
        numberTf.setText(office.getNumber());
        //levelTf.setText("G+ "+String.valueOf(office.getLevel()));
        //letterIdTf.setText(office.letter().toString());

        Button indexBtn = (Button) view.findViewById(R.id.indexBtn);
        indexBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainActivity.replaceFragment(new Search_officeFragment());
            }
        });

        Button showMapBtn = (Button) view.findViewById(R.id.showMapBtn);
        showMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.to_index = false;
                //MyMapFragment.selectedOffice=Index_officeFragment.selectedItem;
                /*
                Get User location and put it here
                MyMapFragment.latLngPlace1=new LatLng(Index_officeFragment.selectedItem.getLatitude(), Index_officeFragment.selectedItem.getLongitude());
                */
                MyMapFragment.latLngPlace2 = new LatLng(office.getLatitude(), office.getLongitude());
                MainActivity.mainActivity.replaceFragment(new MyMapFragment());
            }
        });

        /*Button addImageBtn=(Button) view.findViewById(R.id.addImageBtn);
        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setVisibility(View.VISIBLE);
                MainActivity.mainActivity.SelectPic(imageView);
            }
        });
        Button submitBtn=(Button) view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainActivity.closeKeyboard();
                boolean success=submit(v);
                if(success){
                    //office=MainActivity.db.findOffice(""+office.getId());
                    MainActivity.mainActivity.replaceFragment(new Index_officeFragment());
                }
            }
        });*/

        TextView imagesCountLB=view.findViewById(R.id.imagesCountLB);
        imagesCountLB.setText(imagesCountLB.getText()+": "+office.getOfficeImageList().size());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        liste_received = new ArrayList<>();
        if (office.getOfficeImageList()!=null) {
            for (Office_Image officeImage: office.getOfficeImageList()) {
                liste_received.add(ResizeBitmap.decodeSampledBitmapFromResource(getResources(), officeImage.getImage(),400,200));
            }
        }

        ImageAdapter adapter = new ImageAdapter(MainActivity.appContext, liste_received);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.mainActivity.getApplicationContext(),2);
//        recyclerView.setLayoutManager(layoutManager);
        Image_zoomFragment.view=view;
        Image_zoomFragment.shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);


        return view;
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (MainActivity.appContext.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("info", "Permission is granted");
                return true;
            } else {
                Log.v("info", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("info", "Permission is granted");
            return true;
        }
    }


    public boolean submit(View view){
        byte[] image= MainActivity.imageBytes;//MainActivity.imageByteString;

        if(image==null||"".equals(image)){
            Toast.makeText(MainActivity.appContext, R.string.image_is_required, Toast.LENGTH_LONG).show();
            return false;
        }

        BackgroundWorker backgroundWorker=new BackgroundWorker(MainActivity.appContext);
        String OPERATION_TYPE="add_office_image";
        String imageByteString = new Base64CODEC().convertToBase64(BitmapUtility.getImage(image));
        backgroundWorker.execute(OPERATION_TYPE, ""+office.getId(), imageByteString);

        boolean success=MainActivity.db.insert_offices_images(0, office.getId(), image, "", "");
        Toast.makeText(MainActivity.appContext, success? R.string.row_inserted: R.string.operation_failed, Toast.LENGTH_LONG).show();

    //empty
        MainActivity.emptyImageBytes();

        return success;
    }

}