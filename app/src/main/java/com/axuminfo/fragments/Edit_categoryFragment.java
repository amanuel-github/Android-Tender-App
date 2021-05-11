package com.axuminfo.fragments;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.axuminfo.MainActivity;
import com.axuminfo.R;
import com.axuminfo.models.Complex_building;

public class Edit_categoryFragment extends Fragment {

    private EditText nameTf, descriptionTf;
    private Spinner buildingIdSpinner;
    private String name, description;
    private Complex_building complex_building;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.edit_category, container, false);

        nameTf=(EditText) view.findViewById(R.id.name);
        descriptionTf=(EditText) view.findViewById(R.id.description);

        buildingIdSpinner=(Spinner) view.findViewById(R.id.buildingId);
        ArrayAdapter<Complex_building> arrayAdapter=new ArrayAdapter<Complex_building>(this.getActivity(), android.R.layout.simple_spinner_item, MainActivity.db.complex_buildings());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        buildingIdSpinner.setAdapter(arrayAdapter);
        buildingIdSpinner.setSelection(arrayAdapter.getPosition(Index_categoryFragment.category.complexBuilding()));
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

        nameTf.setText(Index_categoryFragment.category.getName());
        descriptionTf.setText(Index_categoryFragment.category.getDescription());
        buildingIdSpinner.setSelection(0/*Index_categoryFragment.category.getBuildingId()*/);

        Button submitBtn=(Button) view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainActivity.closeKeyboard();
                boolean success=submit(v);
                if(success)
                    MainActivity.mainActivity.replaceFragment(new Index_categoryFragment());
            }
        });

        Button cancelBtn=(Button) view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainActivity.replaceFragment(new Index_categoryFragment());
            }
        });

        return view;
    }

    public boolean submit(View view){
        name=nameTf.getText().toString();
        description=descriptionTf.getText().toString();
        if(name==null||"".equals(name)){
            Toast.makeText(MainActivity.appContext, R.string.name_is_required, Toast.LENGTH_LONG).show();
            return false;
        }
        if(complex_building==null){
            Toast.makeText(MainActivity.appContext, R.string.buildingId_is_required, Toast.LENGTH_LONG).show();
            return false;
        }

        boolean success=MainActivity.db.update_categories(Index_categoryFragment.category.getId(), complex_building.getId(), name, description);
        Toast.makeText(MainActivity.appContext, success? R.string.row_updated: R.string.operation_failed, Toast.LENGTH_LONG).show();
        if(success){
            buildingIdSpinner.setSelection(0);
            nameTf.setText("");
            descriptionTf.setText("");

            MainActivity.mainActivity.replaceFragment(new Index_categoryFragment());
        }
        return true;
    }

}
