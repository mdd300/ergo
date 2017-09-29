package com.uniquesys.qrgo.Produtos.CheckOut;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uniquesys.qrgo.Produtos.Model;
import com.uniquesys.qrgo.Produtos.ProdutoActivity;
import com.uniquesys.qrgo.R;
import com.uniquesys.qrgo.model.Imagem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SplittedGridVariantes extends BaseAdapter {

    Context mContext;
    String resultado;
    List<Bitmap> data;
    String user_id;
    String user_hash;
    JSONObject obj;
    Bitmap result;
    private int resource;
    private LayoutInflater inflater;

    public SplittedGridVariantes(Context c, ArrayList<Bitmap> splittedBit) {

        mContext = c;
        data = splittedBit;
        resource = R.layout.layout_checkout_produtos;
        inflater = LayoutInflater.from(mContext);
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

        int size = Math.min(data.get(position).getWidth(), data.get(position).getHeight());
        int x = (data.get(position).getWidth() - size) / 2;
        int y = (data.get(position).getHeight() - size) / 2;

        Bitmap squared = Bitmap.createBitmap(data.get(position), x, y, size, size);

        result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        ImageView imageView;
        if (convertView == null) {

            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(80, 80));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1,2,1,2);


        }else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(result);

        return imageView;
    }


}