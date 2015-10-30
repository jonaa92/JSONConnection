package casasnovas.auger.jsonconnection;

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
        // Create the connection
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
        int postDataLength = postData.length;
        // setDoOutput(true) implicitly set's the request type to POST
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        connection.setUseCaches(false);

        // Write to the connection
        OutputStream output = connection.getOutputStream();
        output.write(postData);
        output.close();

        // Check the error stream first, if this is null then there have been no issues with the request
        InputStream inputStream = connection.getErrorStream();
        if (inputStream == null)
            inputStream = connection.getInputStream();

        // Read everything from our stream
        BufferedReader responseReader = new BufferedReader(new InputStreamReader(inputStream, charset));

        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = responseReader.readLine()) != null) {
            response.append(inputLine);
        }
        responseReader.close();

        return response.toString();

    }
}
