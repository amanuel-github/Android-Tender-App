package com.axuminfo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.axuminfo.MainActivity;
import com.axuminfo.R;
import com.axuminfo.adapters.CategoryAdapter;
import com.axuminfo.adapters.Sub_categoryAdapter;
import com.axuminfo.models.Category;
import com.axuminfo.models.Office;
import com.axuminfo.models.Sub_category;

import java.util.List;

public class Sub_categories_menuFragment extends Fragment {

    private ListView listView;
    private long mselectedItemId;
    public static List<Sub_category> items;
    public static Sub_category selectedItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.sub_categories_menu, container, false);
        listView = (ListView) view.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.mainActivity.closeKeyboard();
                listView_ItemClick((Sub_category) items.get(position));
            }
        });

        Category category=Main_categories_menuFragment.selectedItem;

        //Retrive Programs
        if(category!=null)
            items = MainActivity.db.sub_categories(category.getId());
        else
            items = MainActivity.db.sub_categories();

        Sub_categoryAdapter arrayAdapter=new Sub_categoryAdapter(this.getContext(), items);
        listView.setAdapter(arrayAdapter);
        return view;
    }

    private void listView_ItemClick(Sub_category item)
    {
        mselectedItemId = item.getId();
        selectedItem = item;
        MainActivity.mainActivity.replaceFragment(new Search_officeFragment());
    }



}
