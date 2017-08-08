package com.uniquesys.qrgo.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uniquesys.qrgo.R;

import java.util.ArrayList;

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