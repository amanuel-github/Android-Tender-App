package com.axuminfo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.axuminfo.MainActivity;
import com.axuminfo.R;

public class Show_letterFragment extends Fragment {

    private TextView nameTf, descriptionTf;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.show_letter, container, false);

        nameTf=(TextView) view.findViewById(R.id.name);
        descriptionTf=(TextView) view.findViewById(R.id.description);

        nameTf.setText(Index_letterFragment.selectedItem.getName());
        descriptionTf.setText(Index_letterFragment.selectedItem.getDescription());

        Button indexBtn=(Button) view.findViewById(R.id.indexBtn);
        indexBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainActivity.replaceFragment(new Index_letterFragment());
            }
        });

        return view;
    }

}
