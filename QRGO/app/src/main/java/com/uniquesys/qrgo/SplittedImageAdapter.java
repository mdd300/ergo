package com.uniquesys.qrgo;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.List;

import static android.R.layout.simple_list_item_1;

public class SplittedImageAdapter extends BaseAdapter {

    Context mContext;
    List<Bitmap> data;


    public SplittedImageAdapter(Context c, List<Bitmap> splittedBitmaps) {
        super();
        mContext = c;
        data=splittedBitmaps;

    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        Log.e("Imagem", "Teste");
        ImageView imageView;

        if (convertView == null) {

            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(30, 30));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);
            convertView.setTag(imageView);

        }else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(data.get(position));


        return imageView;
    }

}