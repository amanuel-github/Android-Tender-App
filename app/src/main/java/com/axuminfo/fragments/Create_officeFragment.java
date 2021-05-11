package com.axuminfo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.axuminfo.Base64CODEC;
import com.axuminfo.BitmapUtility;
import com.axuminfo.models.Category;
import com.axuminfo.models.Complex_building;
import com.axuminfo.models.Letter;
import com.axuminfo.MainActivity;
import com.axuminfo.R;
import com.axuminfo.models.Sub_category;
import com.axuminfo.services.BackgroundWorker;

import java.util.List;
import java.util.Vector;

public class Create_officeFragment extends Fragment {

    public static EditText nameTf, descriptionTf, /*imageTf, */numberTf, longitudeTf, latitudeTf;
    private Spinner buildingIdSpinner, categoryIdSpinner, sub_categoryIdSpinner, letterIdSpinner, levelSpinner;
    public static ImageView imageView;
    public Button selectImageBtn;

    private String name, description, /*image, */number, longitude, latitude;
    byte[] image;

    private Complex_building complex_building;
    private Category category;
    private Sub_category sub_category;
    List<Sub_category> sub_categories;
    private Letter letter;
    private int level=0;
    static Vector< String > IMAGE = new Vector < String > ();
    int i = 0;

    ArrayAdapter<Sub_category> arrayAdapter21;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.create_office, container, false);

        nameTf=(EditText) view.findViewById(R.id.name);
        selectImageBtn=(Button) view.findViewById(R.id.selectImageBtn);
        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /*Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Log.i("photo", "" + intent);
                    startActivityForResult(intent, i);
                    i = i + 1;*/
                ImageView_Click(imageView);
                }
        });
        numberTf=(EditText) view.findViewById(R.id.number);
        longitudeTf=(EditText) view.findViewById(R.id.longitude);
        latitudeTf=(EditText) view.findViewById(R.id.latitude);
        descriptionTf=(EditText) view.findViewById(R.id.description);

        imageView = (ImageView)view.findViewById(R.id.imageView);

        buildingIdSpinner=(Spinner) view.findViewById(R.id.buildingId);
        ArrayAdapter<Complex_building> arrayAdapter=new ArrayAdapter<Complex_building>(this.getActivity(), android.R.layout.simple_spinner_item, MainActivity.db.complex_buildings());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingIdSpinner.setAdapter(arrayAdapter);
        buildingIdSpinner.setSelection(0);
        buildingIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                complex_building=(Complex_building)parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                complex_building=null;
            }
        });

        categoryIdSpinner=(Spinner) view.findViewById(R.id.categoryId);
        ArrayAdapter<Category> arrayAdapter2=new ArrayAdapter<Category>(this.getActivity(), android.R.layout.simple_spinner_item, MainActivity.db.categories());
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryIdSpinner.setAdapter(arrayAdapter2);
        categoryIdSpinner.setSelection(0);
        categoryIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=(Category) parent.getSelectedItem();
                sub_categories=MainActivity.db.sub_categories(category.getId());

                //filter sub categories
                arrayAdapter21.clear();
                arrayAdapter21.addAll(MainActivity.db.sub_categories(category.getId()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category=null;
            }
        });

        sub_categoryIdSpinner=(Spinner) view.findViewById(R.id.sub_categoryId);
        arrayAdapter21=new ArrayAdapter<Sub_category>(this.getActivity(), android.R.layout.simple_spinner_item, MainActivity.db.sub_categories());
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sub_categoryIdSpinner.setAdapter(arrayAdapter21);
        sub_categoryIdSpinner.setSelection(0);
        sub_categoryIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sub_category=(Sub_category) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sub_category=null;
            }
        });

        letterIdSpinner=(Spinner) view.findViewById(R.id.letterId);
        ArrayAdapter<Letter> arrayAdapter3=new ArrayAdapter<Letter>(this.getActivity(), android.R.layout.simple_spinner_item, MainActivity.db.letters());
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        letterIdSpinner.setAdapter(arrayAdapter3);
        letterIdSpinner.setSelection(0);
        letterIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                letter=(Letter) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                letter=null;
            }
        });

        levelSpinner=(Spinner) view.findViewById(R.id.level);
        ArrayAdapter<Integer> arrayAdapter4=new ArrayAdapter<Integer>(this.getActivity(), android.R.layout.simple_spinner_item, MainActivity.db.levels());
        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSpinner.setAdapter(arrayAdapter4);
        levelSpinner.setSelection(0);
        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                level=(Integer) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                level=0;
            }
        });


        Button submitBtn=(Button) view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainActivity.closeKeyboard();
                boolean success=submit(v);
                if(success)
                    MainActivity.mainActivity.replaceFragment(new Index_officeFragment());
            }
        });
        Button detectLocationBtn=(Button) view.findViewById(R.id.detectLocationBtn);
        detectLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMapFragment mapFragment=new MyMapFragment();
                mapFragment.getLastLocation();
                /*if(latLng!=null){
                    latitudeTf.setText(String.valueOf(latLng.latitude));
                    latitudeTf.setText(String.valueOf(latLng.longitude));
                }*/
            }
        });

        return view;
    }

    public boolean submit(View view){
        name=nameTf.getText().toString();
        image= MainActivity.imageBytes;//MainActivity.imageByteString;
        number=numberTf.getText().toString();
        longitude=longitudeTf.getText().toString();
        latitude=latitudeTf.getText().toString();
        description=descriptionTf.getText().toString();

        if(name==null||"".equals(name)){
            Toast.makeText(MainActivity.appContext, R.string.name_is_required, Toast.LENGTH_LONG).show();
            return false;
        }
        /*if(image==null||"".equals(image)){
            Toast.makeText(MainActivity.appContext, R.string.image_is_required, Toast.LENGTH_LONG).show();
            return false;
        }
        if(number==null||"".equals(number)){
            Toast.makeText(MainActivity.appContext, R.string.number_is_required, Toast.LENGTH_LONG).show();
            return false;
        }*/
        if(longitude==null||"".equals(longitude)){
            Toast.makeText(MainActivity.appContext, R.string.longitude_is_required, Toast.LENGTH_LONG).show();
            return false;
        }
        if(latitude==null||"".equals(latitude)){
            Toast.makeText(MainActivity.appContext, R.string.latitude_is_required, Toast.LENGTH_LONG).show();
            return false;
        }
        /*if(level==null){
            Toast.makeText(MainActivity.appContext, R.string.level_is_required, Toast.LENGTH_LONG).show();
            return false;
        }*/
        /*if(complex_building==null){
            Toast.makeText(MainActivity.appContext, R.string.buildingId_is_required, Toast.LENGTH_LONG).show();
            return false;
        }*/
        if(category==null){
            Toast.makeText(MainActivity.appContext, R.string.categoryId_is_required, Toast.LENGTH_LONG).show();
            return false;
        }
        if(sub_category==null){
            Toast.makeText(MainActivity.appContext, R.string.sub_categoryId_is_required, Toast.LENGTH_LONG).show();
            return false;
        }
        /*if(letter==null){
            Toast.makeText(MainActivity.appContext, R.string.letterId_is_required, Toast.LENGTH_LONG).show();
            return false;
        }*/

        BackgroundWorker backgroundWorker=new BackgroundWorker(MainActivity.appContext);
        String OPERATION_TYPE="create_offices";
        String imageByteString = new Base64CODEC().convertToBase64(BitmapUtility.getImage(image));
//        Log.d("Base64", imageByteString);
        backgroundWorker.execute(OPERATION_TYPE, /*""+complex_building.getId()*/null, ""+category.getId(), ""+sub_category.getId(), name, ""+level, imageByteString, number, /*""+letter.getId()*/null, ""+Double.valueOf(longitude), ""+Double.valueOf(latitude), description);

        /*boolean success=MainActivity.db.insert_offices(0, complex_building.getId(), category.getId(), name, level, image, number, letter.getId(), Double.valueOf(longitude), Double.valueOf(latitude), description, "false", "", "");
        Toast.makeText(MainActivity.appContext, success? R.string.row_inserted: R.string.operation_failed, Toast.LENGTH_LONG).show();
        if(success){
            buildingIdSpinner.setSelection(0);
            nameTf.setText("");
            numberTf.setText("");
            longitudeTf.setText("");
            latitudeTf.setText("");
            descriptionTf.setText("");
            levelSpinner.setSelection(0);
            categoryIdSpinner.setSelection(0);
            categoryIdSpinner.setSelection(0);
            letterIdSpinner.setSelection(0);
        }*/
        //empty
        MainActivity.emptyImageBytes();

        return true;
    }
    private void ImageView_Click(View v)
    {
        MainActivity.mainActivity.SelectPic((ImageView)v);
    }


}
