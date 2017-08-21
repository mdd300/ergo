package com.uniquesys.qrgo.Chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
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

import java.util.List;
import java.util.concurrent.ExecutionException;

public class splittedImageAdapterList extends BaseAdapter {

    Context mContext;
    List<Bitmap> data;
    List<String> id;
    List<String> ult_mensagem;
    Bitmap result;
    private int resource;
    private LayoutInflater inflater;


    public splittedImageAdapterList(Context c, List<Bitmap> splittedBitmaps, List<String> splittedid, List<String> LastMessage) {

        mContext = c;
        data=splittedBitmaps;
        id = splittedid;
        ult_mensagem = LastMessage;
        resource = R.layout.listview_chat;
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

        final RelativeLayout convertViewR = (RelativeLayout) inflater.inflate(resource, null);

        ImageView imageview = (ImageView) convertViewR.findViewById(R.id.complet_image);

        imageview.setImageBitmap(result);

        TextView LMessage = (TextView) convertViewR.findViewById(R.id.ult_mes);
        LMessage.setText(ult_mensagem.get(position));
        convertViewR.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, ConversaActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                in.putExtra("id", id.get(position));
                mContext.startActivity(in);
                ((ChatActivity) mContext).overridePendingTransition(R.anim.anim_slide_right_leave,R.anim.anim_slide_left_leave);
                ((ChatActivity) mContext).finish();

            }
        });

        String codigo = id.get(position);
        String method = "http://uniquesys.jelasticlw.com.br/qrgo/pedidos/dados_user";
        String function = "produto";
        Model prodTask = new Model(mContext);
        prodTask.execute(function,method, codigo);
        String resultado = null;
        try {
            resultado = prodTask.get();
            JSONArray arrayJson = new JSONArray(resultado.toString());
            JSONObject obj = arrayJson.getJSONObject(0);
            String nome = obj.getString("user_nome");
            TextView Nome = (TextView) convertViewR.findViewById(R.id.nome);
            Nome.setText(nome);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertViewR;
    }


}