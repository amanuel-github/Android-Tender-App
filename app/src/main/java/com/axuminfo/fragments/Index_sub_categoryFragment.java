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
import android.widget.Toast;

import com.axuminfo.MainActivity;
import com.axuminfo.R;
import com.axuminfo.adapters.CategoryAdapter;
import com.axuminfo.adapters.Sub_categoryAdapter;
import com.axuminfo.models.Category;
import com.axuminfo.models.Sub_category;

import java.util.List;

public class Index_sub_categoryFragment extends Fragment {

    private ListView sub_categoryListView;
    private Button showBtn, editBtn, showDeleteBtn, cancelBtn, deleteBtn;
    private long msub_categoryId;
    private float screenHeight = 400;
    private LinearLayout actionOptions_PupUp, deleteConfirm_PupUp;
    public static List<Sub_category> sub_categories;
    public static Sub_category sub_category;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.index_sub_category, container, false);
        sub_categoryListView = (ListView) view.findViewById(R.id.listView);
        showBtn = (Button)view.findViewById(R.id.showBtn);
        cancelBtn = (Button)view.findViewById(R.id.cancelBtn);
        deleteBtn = (Button)view.findViewById(R.id.deleteBtn);
        editBtn = (Button)view.findViewById(R.id.editBtn);
        showDeleteBtn = (Button)view.findViewById(R.id.showDeleteBtn);
        actionOptions_PupUp = (LinearLayout)view.findViewById(R.id.actionOptions_PupUp);
        deleteConfirm_PupUp = (LinearLayout)view.findViewById(R.id.deleteConfirm_PupUp);

        sub_categoryListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sub_categoryListView_ItemClick((Sub_category)parent.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sub_categoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                sub_categoryListView_ItemLongClick(sub_categories.get(position)/*(Category)parent.getSelectedItem()*/);
                return true;
            }
        });
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowBtn_Click(v);
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditBtn_Click(v);
            }
        });
        showDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDeleteBtn_Click(v);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteBtn_Click(v);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelBtn_Click(v);
            }
        });

        //Retrive Programs
        if(Index_categoryFragment.category!=null)
            sub_categories = MainActivity.db.sub_categories(Index_categoryFragment.category.getId());
        else
            sub_categories = MainActivity.db.sub_categories();

        Sub_categoryAdapter arrayAdapter=new Sub_categoryAdapter(this.getContext(), sub_categories);
        sub_categoryListView.setAdapter(arrayAdapter);

        Button addBtn=(Button)view.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainActivity.replaceFragment(new Create_sub_categoryFragment());
            }
        });

        return view;
    }


    private void ShowBtn_Click(View v)
    {
        actionOptions_PupUp.setTranslationY(screenHeight);// 250;// screenHeight;
        deleteConfirm_PupUp.setTranslationY(screenHeight);// 250;// screenHeight;
        MainActivity.mainActivity.replaceFragment(new Show_sub_categoryFragment());
    }
    private void ShowDeleteBtn_Click(View v)
    {
        deleteConfirm_PupUp.setTranslationY(screenHeight - 350);// screenHeight-150;
    }

    private void EditBtn_Click(View v)
    {
        MainActivity.mainActivity.replaceFragment(new Edit_sub_categoryFragment());
    }

    private void CancelBtn_Click(View v)
    {
        actionOptions_PupUp.setTranslationY(screenHeight);// 250;// screenHeight;
        deleteConfirm_PupUp.setTranslationY(screenHeight);// 250;// screenHeight;
        Toast.makeText(this.getContext(), "  Operation Canceled", Toast.LENGTH_SHORT).show();
    }


    private void DeleteBtn_Click(View v)
    {
        actionOptions_PupUp.setTranslationY(screenHeight);// 250;// screenHeight;
        deleteConfirm_PupUp.setTranslationY(screenHeight);// 250;// screenHeight;
        MainActivity.db.delete_category(msub_categoryId);
        MainActivity.mainActivity.replaceFragment(new Index_sub_categoryFragment());
        Toast.makeText(this.getContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
    }

    private void sub_categoryListView_ItemClick(Sub_category item)
    {
        actionOptions_PupUp.setTranslationY(screenHeight);// 250;// screenHeight;
        deleteConfirm_PupUp.setTranslationY(screenHeight);//250;// screenHeight;
    }

    private void sub_categoryListView_ItemLongClick(Sub_category item){
        msub_categoryId = item.getId();
        sub_category = item;
        actionOptions_PupUp.setTranslationY(screenHeight - 350);// screenHeight-150;

    }



}
