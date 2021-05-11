package com.axuminfo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.axuminfo.MainActivity;
import com.axuminfo.R;

public class Edit_letterFragment extends Fragment {

    private EditText nameTf, descriptionTf;
    private String name, description;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.edit_letter, container, false);

        nameTf=(EditText) view.findViewById(R.id.name);
        descriptionTf=(EditText) view.findViewById(R.id.description);

        nameTf.setText(Index_letterFragment.selectedItem.getName());
        descriptionTf.setText(Index_letterFragment.selectedItem.getDescription());

        Button submitBtn=(Button) view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainActivity.closeKeyboard();
                boolean success=submit(v);
                if(success)
                    MainActivity.mainActivity.replaceFragment(new Index_letterFragment());
            }
        });

        Button cancelBtn=(Button) view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainActivity.replaceFragment(new Index_letterFragment());
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

        boolean success=MainActivity.db.update_letters(Index_letterFragment.selectedItem.getId(), name, description);
        Toast.makeText(MainActivity.appContext, success? R.string.row_updated: R.string.operation_failed, Toast.LENGTH_LONG).show();
        if(success){
            nameTf.setText("");
            descriptionTf.setText("");

            MainActivity.mainActivity.replaceFragment(new Index_letterFragment());
        }
        return true;
    }

}
