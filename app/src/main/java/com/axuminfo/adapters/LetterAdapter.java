package com.axuminfo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.axuminfo.R;
import com.axuminfo.models.Letter;

import java.util.List;

/**
 * Created by User on 2/8/2020.
 */

public class LetterAdapter extends BaseAdapter {
    List<Letter> mItems;
    Context mContext;

    public LetterAdapter(Context context, List<Letter> items)
    {
        mContext = context;
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Letter getItem(int position) {
        return mItems.get(position);
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

        nameTextView.setText(mItems.get(position).getName());

        return view;
    }
}
