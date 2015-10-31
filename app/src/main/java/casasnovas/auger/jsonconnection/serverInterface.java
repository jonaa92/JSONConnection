package casasnovas.auger.jsonconnection;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Auger on 30/10/2015.
 */
public class serverInterface {

    public static String doPost (final String url, final String urlParameters) throws IOException {
        final String charset = "UTF-8";
        // Conexion
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
        int postDataLength = postData.length;
        // connection params (de POST)
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", Integer.toString(postDataLength)); // AQUI
        connection.setUseCaches(false);

        // Hacer el post de postData (tiene que ser un byte[]. Arriba se ha definido el lenght
        OutputStream output = connection.getOutputStream();
        output.write(postData);
        output.close();


        // Check the error stream
        InputStream inputStream = connection.getErrorStream();
        if (inputStream == null)
            inputStream = connection.getInputStream();

        // Read response from servver
        BufferedReader responseReader = new BufferedReader(new InputStreamReader(inputStream, charset));

        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = responseReader.readLine()) != null) {
            response.append(inputLine);
        }
        responseReader.close();
        Log.d("urlconnect", "SERVER RESPONSE: " + response);
        //Devolvemos la respuesta del server
        return response.toString();

    }
}
