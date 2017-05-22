package com.uniquesys.qrgo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by victoroshiro on 09/05/17.
 */

public class Imagem extends AsyncTask<Object, Object, Bitmap> {
    AlertDialog alertDialog;
    Context ctx;
    Imagem(Context ctx)
    {
        this.ctx =ctx;
    }
    ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Login Information....");
        dialog = new ProgressDialog(ctx);
        dialog.setTitle("Realizando o carregamento dos dados");
        dialog.setMessage("Aguarde o fim da requisição...");
        dialog.show();
    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        Object function = params[0];
        Object method = params[1];
        Object login_url = method;
        // String reg_url = "http://192.168.56.1/webapp/register.php";
        // String login_url = "http://192.168.56.1/webapp/login.php";


        if(function == "imagem") {

            try {
                URL url = new URL((String) login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);

                InputStream inputStream = httpURLConnection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);

                inputStream.close();
                httpURLConnection.disconnect();
                return myBitmap;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }



    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if(result.equals("Registration Success..."))
        {

        }
        else
        {
            dialog.dismiss();
        }

    }


}


