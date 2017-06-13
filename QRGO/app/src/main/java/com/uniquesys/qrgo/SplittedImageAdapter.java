package com.uniquesys.qrgo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

public class SplittedImageAdapter extends BaseAdapter {

    Context mContext;
    List<Bitmap> data;


    public SplittedImageAdapter(Context c, List<Bitmap> splittedBitmaps) {

        mContext = c;
        data=splittedBitmaps;

    }


    @Override
    public int getCount() {
        Log.e("Imagem",String.valueOf(data.size()));
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        Log.e("Imagem", "Teste");
        return null;
    }

    @Override
    public long getItemId(int position) {
        Log.e("Imagem", "Teste");
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        Log.e("Imagem", "Teste");
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


        return imageView;
    }

}