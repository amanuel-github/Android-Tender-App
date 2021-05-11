package com.axuminfo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.axuminfo.MainActivity;
import com.axuminfo.R;
import com.axuminfo.adapters.CategoryAdapter;
import com.axuminfo.adapters.LetterAdapter;
import com.axuminfo.models.Category;
import com.axuminfo.models.Letter;
import com.axuminfo.models.Office;
import com.axuminfo.models.Sub_category;
import com.axuminfo.models.User;

import java.util.List;

public class Main_categories_menuFragment extends Fragment {

    private ListView listView;
    private long mselectedItemId;
    public static List<Category> items;
    public static Category selectedItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.main_categories_menu, container, false);
        listView = (ListView) view.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.mainActivity.closeKeyboard();
                listView_ItemClick((Category) items.get(position));
            }
        });


        //Retrive Programs
        items = MainActivity.db.categories();

        CategoryAdapter arrayAdapter=new CategoryAdapter(this.getContext(), items);
        listView.setAdapter(arrayAdapter);

        view.findViewById(R.id.app_nameLb).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!User.isLoggedIn)
                    MainActivity.mainActivity.replaceFragment(new LoginFragment());
                return true;
            }
        });

        return view;
    }

    private void listView_ItemClick(Category item){
        selectedItem = item;
        MainActivity.mainActivity.replaceFragment(new Sub_categories_menuFragment());
    }



}
