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
    private int resource;
    private LayoutInflater inflater;
    ArrayList <String> layoutdef;

    public SplittedListAdapter(Context c, ArrayList<String> splittedMensagem, ArrayList<String> layout) {

        mContext = c;
        data=splittedMensagem;
        layoutdef = layout;
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

        if(layoutdef.get(position) == "destinatario")
            resource = R.layout.mensagem_destinatario;
        else
            resource = R.layout.mensagem_remetente;

        final RelativeLayout convertViewR = (RelativeLayout) inflater.inflate(resource, null);

        TextView textView = (TextView) convertViewR.findViewById(R.id.Mensagem);

        textView.setText("    "+ data.get(position).toString() + "    ");
        
        return convertViewR;
    }


}