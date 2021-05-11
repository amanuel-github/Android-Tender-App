package com.axuminfo.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.axuminfo.BitmapUtility;
import com.axuminfo.MainActivity;
import com.axuminfo.R;
import com.axuminfo.models.Category;
import com.axuminfo.models.Complex_building;
import com.axuminfo.models.Letter;
import com.axuminfo.models.Sub_category;

import java.util.List;

public class Edit_officeFragment extends Fragment {

    public static EditText nameTf, descriptionTf, /*imageTf, */numberTf, longitudeTf, latitudeTf;
    private Spinner buildingIdSpinner, categoryIdSpinner, sub_categoryIdSpinner, letterIdSpinner, levelSpinner;
    private String name, description, /*image, */number, longitude, latitude;
    public static ImageView imageView;
    byte[] image;
    public Button selectImageBtn;
    List<Sub_category> sub_categories;

    private Complex_building complex_building;
    private Category category;
    private Sub_category sub_category;
    private Letter letter;
    private int level=0;
    int i = 0;
    ArrayAdapter<Sub_category> arrayAdapter21;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.edit_office, container, false);

        nameTf=(EditText) view.findViewById(R.id.name);
        selectImageBtn=(Button) view.findViewById(R.id.selectImageBtn);
        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        buildingIdSpinner.setSelection(arrayAdapter.getPosition(Index_officeFragment.selectedItem.complexBuilding()));

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
        categoryIdSpinner.setSelection(arrayAdapter2.getPosition(Index_officeFragment.selectedItem.category()));
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

        sub_categoryIdSpinner=(Spinner) view.findViewById(R.id.categoryId);
        arrayAdapter21=new ArrayAdapter<Sub_category>(this.getActivity(), android.R.layout.simple_spinner_item, MainActivity.db.sub_categories());
        arrayAdapter21.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sub_categoryIdSpinner.setAdapter(arrayAdapter21);
        sub_categoryIdSpinner.setSelection(arrayAdapter21.getPosition(Index_officeFragment.selectedItem.sub_category()));
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
        letterIdSpinner.setSelection(arrayAdapter3.getPosition(Index_officeFragment.selectedItem.letter()));
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
        levelSpinner.setSelection(arrayAdapter4.getPosition(Index_officeFragment.selectedItem.getLevel()));
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

        Button cancelBtn=(Button) view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainActivity.replaceFragment(new Index_officeFragment());
            }
        });

        nameTf.setText(Index_officeFragment.selectedItem.getName());
        descriptionTf.setText(Index_officeFragment.selectedItem.getDescription());

        Bitmap bitmap= BitmapUtility.getImage(Index_officeFragment.selectedItem.getImage());//MainActivity.decodeToImage(Index_officeFragment.selectedItem.getImage());
        imageView.setImageBitmap(null);
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200, 200, false));

        numberTf.setText(Index_officeFragment.selectedItem.getNumber());
        longitudeTf.setText(String.valueOf(Index_officeFragment.selectedItem.getLongitude()));
        latitudeTf.setText(String.valueOf(Index_officeFragment.selectedItem.getLatitude()));

        buildingIdSpinner.setSelection(0/*Index_officeFragment.selectedItem.getBuildingId()*/);
        categoryIdSpinner.setSelection(0/*Index_officeFragment.selectedItem.getCategoryId()*/);
        letterIdSpinner.setSelection(0/*Index_officeFragment.selectedItem.getLetterId()*/);
        levelSpinner.setSelection(0/*Index_officeFragment.selectedItem.getLevel()*/);

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
        }*/
        /*if(number==null||"".equals(number)){
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
        /*if(level==0){
            Toast.makeText(MainActivity.appContext, R.string.level_is_required, Toast.LENGTH_LONG).show();
            return;
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

        boolean success=MainActivity.db.update_offices(Index_officeFragment.selectedItem.getId(), complex_building.getId(), category.getId(), sub_category.getId(), name, level, image, number, letter.getId(), Double.valueOf(longitude), Double.valueOf(latitude), description);
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
            sub_categoryIdSpinner.setSelection(0);
            letterIdSpinner.setSelection(0);
        }

        //empty
        MainActivity.emptyImageBytes();

        return success;
    }
    private void ImageView_Click(View v)
    {
        MainActivity.mainActivity.SelectPic((ImageView)v);
    }

}
