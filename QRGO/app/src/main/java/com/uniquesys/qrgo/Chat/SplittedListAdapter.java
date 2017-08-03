package com.uniquesys.qrgo.Chat;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uniquesys.qrgo.Produtos.Model;
import com.uniquesys.qrgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SplittedListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<String> data;
    TextView textView;

    public SplittedListAdapter(Context c, ArrayList<String> splittedMensagem) {

        mContext = c;
        data=splittedMensagem;
       
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

        textView = new TextView(mContext);

        textView.setText(data.get(position).toString());
        
        return textView;
    }


}