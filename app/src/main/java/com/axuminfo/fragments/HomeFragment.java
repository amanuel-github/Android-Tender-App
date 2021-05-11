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
import com.axuminfo.models.User;
import com.axuminfo.services.BackgroundWorker;


public class HomeFragment extends Fragment {

    public static Button startAppBtn, syncDataBtn;
    private TextView categoriesCountTv, officesCountTv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);

        officesCountTv=(TextView) view.findViewById(R.id.officesCountTv);
        categoriesCountTv=(TextView) view.findViewById(R.id.categoriesCountTv);
        officesCountTv.setText(String.valueOf(MainActivity.db.offices().size()));
        categoriesCountTv.setText(String.valueOf(MainActivity.db.categories().size()));

        startAppBtn=(Button) view.findViewById(R.id.startAppBtn);
        startAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainActivity.replaceFragment(new Search_officeFragment());
            }
        });
        syncDataBtn=(Button) view.findViewById(R.id.syncDataBtn);
        syncDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncDataBtn.setText(getString(R.string.loading_anim));
                BackgroundWorker backgroundWorker=new BackgroundWorker(MainActivity.appContext);
                String OPERATION_TYPE="load_server_data";
                backgroundWorker.execute(OPERATION_TYPE);
            }
        });

        TextView app_nameLb=view.findViewById(R.id.app_nameLb);
        app_nameLb.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!User.isLoggedIn)
                    MainActivity.mainActivity.replaceFragment(new LoginFragment());
                return true;
            }
        });
        return view;
    }
}
