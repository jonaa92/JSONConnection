package casasnovas.auger.jsonconnection;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class serverInterface {

    public static String doPost (final String url, final String urlParameters) throws IOException {
        final String charset = "UTF-8";
        // Conexion
        Log.d("urlconnect", urlParameters);
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
//        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
//        osw.write(urlParameters,0,postDataLength);
//        osw.flush();

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
        Log.d("urlconnect", "SERVER RESPONSE CODE: " + connection.getResponseCode());
        Log.d("urlconnect", "SERVER RESPONSE: " + response);
        //Devolvemos la respuesta del server
        return response.toString();

    }

    public static void downloadFTP (String user, String pass, String filePath, String savePath) throws IOException {
        String ftpUrl = "ftp://%s:%s@%s/%s";
        String host = "raspynet.no-ip.org";

        ftpUrl = String.format(ftpUrl, user, pass, host, filePath);
        Log.d("provaftp", "URL: " + ftpUrl);
        URL url = new URL(ftpUrl);
        URLConnection urlConnection = url.openConnection();
        FileOutputStream outputStream = new FileOutputStream(savePath);
        InputStream inputStream = urlConnection.getInputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
        inputStream.close();
        /*TODO: Aqui se tiene que comprobar si el archivo que se ha descargado existe en el directorio savepath*/

    }
    //UploadPath -> Directorio del server
    //SavePath -> Directorio del movil
    public static void uploadFTP (String user, String pass, String savePath, String uploadPath) throws IOException {
        String ftpUrl = "ftp://%s:%s@%s/%s";
        String host = "raspynet.no-ip.org";
        ftpUrl = String.format(ftpUrl, user,pass,host,uploadPath);
        Log.d("provaftp", "URL: " + ftpUrl);
        URL url = new URL(ftpUrl);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setDoOutput(true);
        OutputStream outputStream = urlConnection.getOutputStream();
        FileInputStream inputStream = new FileInputStream(savePath);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
        inputStream.close();

    }
}
