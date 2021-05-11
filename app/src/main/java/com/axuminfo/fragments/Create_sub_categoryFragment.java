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
import com.axuminfo.models.Category;
import com.axuminfo.models.Complex_building;
import com.axuminfo.services.BackgroundWorker;

public class Create_sub_categoryFragment extends Fragment {

    private EditText nameTf, descriptionTf;
    private Spinner categoryIdSpinner;
    private String name, description;
    private Category category;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.create_sub_category, container, false);

        nameTf=(EditText) view.findViewById(R.id.name);
        descriptionTf=(EditText) view.findViewById(R.id.description);

        categoryIdSpinner=(Spinner) view.findViewById(R.id.categoryId);
        ArrayAdapter<Category> arrayAdapter=new ArrayAdapter<Category>(this.getActivity(), android.R.layout.simple_spinner_item, MainActivity.db.categories());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categoryIdSpinner.setAdapter(arrayAdapter);
        categoryIdSpinner.setSelection(0);
        categoryIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=(Category) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category=null;
            }
        });

        Button submitBtn=(Button) view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainActivity.closeKeyboard();
                boolean success=submit(v);
                if(success)
                MainActivity.mainActivity.replaceFragment(new Index_sub_categoryFragment());
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
        if(category==null){
            Toast.makeText(MainActivity.appContext, R.string.categoryId_is_required, Toast.LENGTH_LONG).show();
            return false;
        }

        BackgroundWorker backgroundWorker=new BackgroundWorker(MainActivity.appContext);
        String OPERATION_TYPE="create_sub_categories";
        backgroundWorker.execute(OPERATION_TYPE, ""+category.getId(), name, description);

        return true;
    }

}
