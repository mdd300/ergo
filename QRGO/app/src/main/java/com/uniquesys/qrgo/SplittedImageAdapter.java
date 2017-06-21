package com.uniquesys.qrgo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import java.util.concurrent.ExecutionException;

public class SplittedImageAdapter extends BaseAdapter {

    Context mContext;
    List<Bitmap> data;
    List<String> id;
    int getId;
    Activity act;


    public SplittedImageAdapter(Context c, Activity activity, List<Bitmap> splittedBitmaps, List<String> splittedid) {

        mContext = c;
        data=splittedBitmaps;
        id = splittedid;
        act = activity;

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
            imageView.setLayoutParams(new GridView.LayoutParams(180, 180));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);

        }else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(data.get(position));
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String codigo = id.get(position);
                String method = "https://www.uniquesys.com.br/qrgo/pedidos/readqrcodepedido_app";
                String function = "produto";
                Model prodTask = new Model(act);
                prodTask.execute(function,method, codigo);
                String resultado = null;
                try {
                    resultado = prodTask.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Activity parentActivity = act;
                Intent in = new Intent(parentActivity, ProdutoActivity.class);
                in.putExtra("resultado", resultado);
                ((myInterface)parentActivity).startMyIntent(in);
            }
        });


        return imageView;
    }

}