package com.axuminfo.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.axuminfo.R;
import com.axuminfo.fragments.Image_zoomFragment;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.PicHolder> {
    static Bitmap bitmap;
    public static class PicHolder extends RecyclerView.ViewHolder {
        public ImageView image_gallery;

        public PicHolder(View itemview) {
            super(itemview);
            image_gallery = (ImageView) itemview.findViewById(R.id.image_gallery);
            image_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Image_zoomFragment.zoomImageFromThumb(v, image_gallery.getDrawable());

                }
            });
        }
    }

    private ArrayList<Bitmap> data;
    private Context context;

    public ImageAdapter(Context mcontext, ArrayList<Bitmap> mdata) {
        context = mcontext;
        data = mdata;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public ImageAdapter.PicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View galleryView = inflater.inflate(R.layout.image_gallery, parent, false);
        PicHolder picHolder = new PicHolder(galleryView);
        return picHolder;
    }


    @Override
    public void onBindViewHolder(PicHolder holder, int position) {
        bitmap = data.get(position);

        ImageView imageView = holder.image_gallery;
        imageView.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
