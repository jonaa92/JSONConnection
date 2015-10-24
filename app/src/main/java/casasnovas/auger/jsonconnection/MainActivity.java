package casasnovas.auger.jsonconnection;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    public static final String urlConnect = "https://raspynet.herokuapp.com/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void button (View v) throws ExecutionException, InterruptedException {
        backgroundLogin bl = new backgroundLogin();
        String user, pass;
        EditText et = (EditText) findViewById(R.id.etEmail);
        user = et.getText().toString();
        et = (EditText) findViewById(R.id.etPass);
        pass  = et.getText().toString();
        bl.execute(user, pass);
        if (bl.get()) {
            /*TODO: La funcion isUserFromDb deberá tambien proporcionar el nombre del usuario y tal, todo esto se le tiene que pasar al intent para que la activity que lo recibe lo pueda mostrar*/
            Toast.makeText(this, "CORRECT!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), correctActivity.class);
            startActivity(i);
        }
        else Toast.makeText(this, "INCORRECT :(", Toast.LENGTH_SHORT).show();

    }

    private class backgroundLogin extends AsyncTask<String,Void,Boolean> {


        @Override
        protected Boolean doInBackground(String... params) {
            return URLconnect(params[0], params[1]);
        }
         private boolean URLconnect (String email, String pass){
            try{
                //Connection set-up
                URL url = new URL("https://raspynet.herokuapp.com/login");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                //POST params
                //String urlParameter = "email=prova@gmail.com&password=1234";
                String urlParameters = "email="+email+"&password="+pass;
                Log.d("patata", urlParameters);
                byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
                int postDataLength = postData.length;

                //Connection params
                connection.setDoOutput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("charset", "utf-8");
                connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                connection.setUseCaches(false);

                //POST data
                connection.getOutputStream().write(postData);

                //Retrieve answer from server
                Reader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String bufferLectura=null;
                for ( int c = in.read(); c != -1; c = in.read() ) {
                    //System.out.print((char)c);
                    bufferLectura += (char) c;
                }
                Log.d("BackgroundLogin", bufferLectura);

                return isUserFromDB(bufferLectura);

            }catch (IOException e) {
                Log.d("BackgroundLogin", "Excep: " + e.getMessage());
                return false;
            }
        }
        private boolean isUserFromDB (String response){
            //Comprueba si el string de respuesta del server es un si o un no
            return response.contains("Success");
        }
    }
}
