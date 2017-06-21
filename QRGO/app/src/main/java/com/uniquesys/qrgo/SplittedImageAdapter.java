package com.uniquesys.qrgo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

public class SplittedImageAdapter extends BaseAdapter {

    Context mContext;
    List<Bitmap> data;
    List<String> id;
    int getId;


    public SplittedImageAdapter(Context c, List<Bitmap> splittedBitmaps, List<String> splittedid) {

        mContext = c;
        data=splittedBitmaps;
        id = splittedid;

    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        getId = Integer.parseInt(id.get(position));
        return getId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {

        ImageView imageView;

        if (convertView == null) {

            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(480, 480));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);

        }else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(data.get(position));
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new GridActivity().itemClicked(mContext,id.get(position));
            }
        });


        return imageView;
    }

}