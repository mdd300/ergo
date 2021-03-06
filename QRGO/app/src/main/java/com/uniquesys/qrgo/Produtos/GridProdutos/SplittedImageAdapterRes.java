package com.uniquesys.qrgo.Produtos.GridProdutos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.uniquesys.qrgo.Produtos.Model;
import com.uniquesys.qrgo.Produtos.ProdutoActivity;
import com.uniquesys.qrgo.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SplittedImageAdapterRes extends BaseAdapter {

    Context mContext;
    List<Bitmap> data;
    List<String> id;
    String user_id;
    String user_hash;

    public SplittedImageAdapterRes(Context c, List<Bitmap> splittedBitmaps, List<String> splittedid, String user, String hash) {

        mContext = c;
        data=splittedBitmaps;
        id = splittedid;
        user_hash = hash;
        user_id = user;
    }


    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {

        ImageView imageView;
        if (convertView == null) {

            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(380, 380));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1,2,1,2);



        }else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(data.get(position));
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String codigo = id.get(position);
                String method = "http://192.168.0.85/erp/vendas_produtos/getList";
                String function = "produto";
                Model prodTask = new Model(mContext);
                prodTask.execute(function,method, codigo,user_id,user_hash,"prod_id");
                String resultado = null;
                try {
                    resultado = prodTask.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Intent in = new Intent(mContext, ProdutoActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                in.putExtra("id", codigo);
                in.putExtra("resultado", resultado);
                mContext.startActivity(in);
                ((GridActivity) mContext).overridePendingTransition(R.anim.anim_slide_right,R.anim.anim_slide_left);
                ((GridActivity) mContext).finish();

            }
        });

        return imageView;
    }


}