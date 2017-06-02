package com.uniquesys.qrgo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by victoroshiro on 09/05/17.
 */

public class Imagem extends AsyncTask<Object, Object, Bitmap> {

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        Object function = params[0];
        Object method = params[1];
        Object login_url = method;


        if(function == "imagem") {

            try {
                URL url = new URL((String) login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);

                InputStream inputStream = httpURLConnection.getInputStream();
                /* Buffered is always good for a performance plus. */
                BufferedInputStream bis = new BufferedInputStream(inputStream);

                Bitmap myBitmap = BitmapFactory.decodeStream(bis);

                bis.close();
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

        }

    }


}


