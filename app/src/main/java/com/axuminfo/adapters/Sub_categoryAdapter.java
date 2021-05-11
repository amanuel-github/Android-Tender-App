package com.axuminfo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.axuminfo.R;
import com.axuminfo.models.Category;
import com.axuminfo.models.Sub_category;

import java.util.List;

/**
 * Created by User on 2/8/2020.
 */

public class Sub_categoryAdapter extends BaseAdapter {
    List<Sub_category> mSub_categories;
    Context mContext;

    public Sub_categoryAdapter(Context context, List<Sub_category> sub_categories)
    {
        mContext = context;
        mSub_categories = sub_categories;
    }

    @Override
    public int getCount() {
        return mSub_categories.size();
    }

    @Override
    public Sub_category getItem(int position) {
        return mSub_categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if (view == null)
            view = LayoutInflater.from(mContext).inflate(R.layout.generic_list_row, null, false);

        TextView nameTextView = (TextView)view.findViewById(R.id.nameTv);

        nameTextView.setText(mSub_categories.get(position).getName());

        return view;
    }
}
