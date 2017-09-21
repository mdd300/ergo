package com.uniquesys.qrgo.Produtos;

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
    public Model(Context ctx)
    {
        this.ctx =ctx;
    }

    public Model() {

    }


    @Override
    protected void onPreExecute() {

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
        String data = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(login_name, "UTF-8") + "&" +
                URLEncoder.encode("senha", "UTF-8") + "=" + URLEncoder.encode(login_pass, "UTF-8") + "&" +
                URLEncoder.encode("format", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(1), "UTF-8");
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
    String user = params[3];
    String hash = params[4];
    String campo = params[5];
    try {
        URL url = new URL(login_url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        OutputStream outputStream = httpURLConnection.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        String data = URLEncoder.encode("where", "UTF-8") + "=" + URLEncoder.encode(campo + " = " + codigo, "UTF-8") + "&"
                + URLEncoder.encode("minimal", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8") + "&"
                + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8") + "&"
                + URLEncoder.encode("user_hash", "UTF-8") + "=" + URLEncoder.encode(hash, "UTF-8");
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
        if(function == "produtoEst") {
            String codigo = params[2];
            String user = params[3];
            String hash = params[4];
            String campo = params[5];
            String pagina = params[6];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("where", "UTF-8") + "=" + URLEncoder.encode(campo + " = " + codigo, "UTF-8") + "&"
                        + URLEncoder.encode("minimal", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8") + "&"
                        + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8") + "&"
                        + URLEncoder.encode("user_hash", "UTF-8") + "=" + URLEncoder.encode(hash, "UTF-8") + "&"
                        + URLEncoder.encode("pagina", "UTF-8") + "=" + URLEncoder.encode(pagina, "UTF-8");
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
    if(function == "listagem") {
        String pagina = params[2];
        String user = params[3];
        String hash = params[4];

        try {
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = URLEncoder.encode("pagina", "UTF-8") + "=" + URLEncoder.encode(pagina, "UTF-8")+ "&"
                    + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8") + "&"
                    + URLEncoder.encode("user_hash", "UTF-8") + "=" + URLEncoder.encode(hash, "UTF-8") + "&"
                    + URLEncoder.encode("minimal", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");
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
        if(function == "carrinho") {
            String codigo = params[2];
            String qtde = params[3];
            String tipo = params[4];
            String user_id = params[5];
            String hash = params[6];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("prod_id", "UTF-8") + "=" + URLEncoder.encode(codigo, "UTF-8") + "&"
                        + URLEncoder.encode("quantidade", "UTF-8") + "=" + URLEncoder.encode(qtde, "UTF-8") + "&"
                        + URLEncoder.encode("prod_principal", "UTF-8") + "=" + URLEncoder.encode(tipo, "UTF-8") + "&"
                        + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                        + URLEncoder.encode("user_hash", "UTF-8") + "=" + URLEncoder.encode(hash, "UTF-8");
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

        if(function == "user_id") {
            String codigo = params[2];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(codigo, "UTF-8");
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
        if(function == "listagem") {
            String pagina = params[2];
            String user_id = params[3];
            String hash = params[4];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("pagina", "UTF-8") + "=" + URLEncoder.encode(pagina, "UTF-8")
                        + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                        + URLEncoder.encode("hash", "UTF-8") + "=" + URLEncoder.encode(hash, "UTF-8") + "&"
                        + URLEncoder.encode("minimal", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");;
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
        if(function == "carrinho") {
            String codigo = params[2];
            String qtde = params[3];
            String tipo = params[4];
            String user_id = params[5];
            String hash = params[6];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("prod_id", "UTF-8") + "=" + URLEncoder.encode(codigo, "UTF-8") + "&"
                        + URLEncoder.encode("quantidade", "UTF-8") + "=" + URLEncoder.encode(qtde, "UTF-8") + "&"
                        + URLEncoder.encode("prod_principal", "UTF-8") + "=" + URLEncoder.encode(tipo, "UTF-8") + "&"
                        + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                        + URLEncoder.encode("user_hash", "UTF-8") + "=" + URLEncoder.encode(hash, "UTF-8");
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
        if(function == "pesquisa") {
            String pagina = params[2];
            String pesquisa = params[3];
            String user = params[4];
            String hash = params[5];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("pagina", "UTF-8") + "=" + URLEncoder.encode(pagina, "UTF-8") + "&"
                        + URLEncoder.encode("search", "UTF-8") + "=" + URLEncoder.encode(pesquisa, "UTF-8") + "&"
                        + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8") + "&"
                        + URLEncoder.encode("user_hash", "UTF-8") + "=" + URLEncoder.encode(hash, "UTF-8");
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
        if(function == "hash_user") {
            String user_id = params[2];
            String hash = params[3];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                        + URLEncoder.encode("hash", "UTF-8") + "=" + URLEncoder.encode(hash, "UTF-8");
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
        return null;
    }



    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {


    }


}
