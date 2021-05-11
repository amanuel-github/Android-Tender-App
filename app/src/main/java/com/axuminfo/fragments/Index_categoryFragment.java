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
import com.axuminfo.models.Category;

import java.util.List;

public class Index_categoryFragment extends Fragment {

    private ListView categoryListView;
    private Button showBtn, editBtn, showDeleteBtn, cancelBtn, deleteBtn;
    private long mcategoryId;
    private float screenHeight = 400;
    private LinearLayout actionOptions_PupUp, deleteConfirm_PupUp;
    public static List<Category> categories;
    public static Category category;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.index_category, container, false);
        categoryListView = (ListView) view.findViewById(R.id.listView);
        showBtn = (Button)view.findViewById(R.id.showBtn);
        cancelBtn = (Button)view.findViewById(R.id.cancelBtn);
        deleteBtn = (Button)view.findViewById(R.id.deleteBtn);
        editBtn = (Button)view.findViewById(R.id.editBtn);
        showDeleteBtn = (Button)view.findViewById(R.id.showDeleteBtn);
        actionOptions_PupUp = (LinearLayout)view.findViewById(R.id.actionOptions_PupUp);
        deleteConfirm_PupUp = (LinearLayout)view.findViewById(R.id.deleteConfirm_PupUp);

        categoryListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryListView_ItemClick((Category)parent.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                categoryListView_ItemLongClick(categories.get(position)/*(Category)parent.getSelectedItem()*/);
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
        categories = MainActivity.db.categories();

        CategoryAdapter arrayAdapter=new CategoryAdapter(this.getContext(), categories);
        categoryListView.setAdapter(arrayAdapter);

        Button addBtn=(Button)view.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainActivity.replaceFragment(new Create_categoryFragment());
            }
        });

        return view;
    }


    private void ShowBtn_Click(View v)
    {
        actionOptions_PupUp.setTranslationY(screenHeight);// 250;// screenHeight;
        deleteConfirm_PupUp.setTranslationY(screenHeight);// 250;// screenHeight;
        MainActivity.mainActivity.replaceFragment(new Show_categoryFragment());
    }
    private void ShowDeleteBtn_Click(View v)
    {
        deleteConfirm_PupUp.setTranslationY(screenHeight - 350);// screenHeight-150;
    }

    private void EditBtn_Click(View v)
    {
        MainActivity.mainActivity.replaceFragment(new Edit_categoryFragment());
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
        MainActivity.db.delete_category(mcategoryId);
        MainActivity.mainActivity.replaceFragment(new Index_categoryFragment());
        Toast.makeText(this.getContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
    }

    private void categoryListView_ItemClick(Category category)
    {
        actionOptions_PupUp.setTranslationY(screenHeight);// 250;// screenHeight;
        deleteConfirm_PupUp.setTranslationY(screenHeight);//250;// screenHeight;
    }

    private void categoryListView_ItemLongClick(Category cat){
        mcategoryId = cat.getId();
        category = cat;
        actionOptions_PupUp.setTranslationY(screenHeight - 350);// screenHeight-150;

    }


}
