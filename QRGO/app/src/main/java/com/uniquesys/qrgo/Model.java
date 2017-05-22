package com.uniquesys.qrgo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by victoroshiro on 09/05/17.
 */

public class Model extends AsyncTask<String,Void,String> {
    AlertDialog alertDialog;
    Context ctx;
    Model(Context ctx)
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
    protected String doInBackground(String... params) {
        String function = params[0];
        String method = params[1];
        String login_url = method;
        // String reg_url = "http://192.168.56.1/webapp/register.php";
        // String login_url = "http://192.168.56.1/webapp/login.php";

if(function == "login") {
    String login_name = params[2];
    String login_pass = params[3];
    try {
        URL url = new URL(login_url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        OutputStream outputStream = httpURLConnection.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        String data = URLEncoder.encode("email-login", "UTF-8") + "=" + URLEncoder.encode(login_name, "UTF-8") + "&" +
                URLEncoder.encode("senha-login", "UTF-8") + "=" + URLEncoder.encode(login_pass, "UTF-8") + "&" +
                URLEncoder.encode("format", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(true), "UTF-8");
        bufferedWriter.write(data);
        bufferedWriter.flush();
        bufferedWriter.close();
        outputStream.close();

        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
        String response = "";
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            response += line;
        }
        bufferedReader.close();
        inputStream.close();
        httpURLConnection.disconnect();
        return response;


    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
if(function == "produto") {
        String codigo = params[2];

        try {
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = URLEncoder.encode("codigo", "UTF-8") + "=" + URLEncoder.encode(codigo, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String response = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            Log.e("Resultado",response.toString());
            return response;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
}

        return null;
    }



    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("Registration Success..."))
        {
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
        }
        else
        {
            dialog.dismiss();
        }

    }


}


