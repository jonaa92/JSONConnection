package casasnovas.auger.jsonconnection;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i = new Intent(getApplicationContext(), NewUserActivity.class);
        startActivity(i);

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

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
        Button b = (Button) findViewById(R.id.button);
        b.setText(" CONNECTING... ");
        /*Snackbar.make(b, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/
        b.setEnabled(false);
            /*TODO: La funcion isUserFromDb deberá tambien proporcionar el nombre del usuario y tal, todo esto se le tiene que pasar al intent para que la activity que lo recibe lo pueda mostrar*/
    }

    private class backgroundLogin extends AsyncTask<String,Void,Void> {

        boolean result = false;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Button b = (Button) findViewById(R.id.button);
            b.setText(R.string.connect);
            b.setEnabled(true);
            if (result){
                Intent i = new Intent(getApplicationContext(), correctActivity.class);
                startActivity(i);
            }
            else {
                notRegisteredDialog nrd = notRegisteredDialog.newInstance("backgroundLogin");
                nrd.show(getFragmentManager(), "10");
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            URLconnect(params[0], params[1]);
            return null;
        }
         private void URLconnect (String email, String pass){
            try{
                String urlParameters = "email="+email+"&password="+pass;
                Log.d("urlconnect", urlParameters);
                String bufferLectura = serverInterface.doPost("https://raspynet.herokuapp.com/login", urlParameters);
//                Log.d("urlconnect", bufferLectura);
                result = isUserFromDB(bufferLectura);

            }catch (IOException e) {
                Log.d("BackgroundLogin", "Excep: " + e.getMessage());
                result = false;
            }
        }
        private boolean isUserFromDB (String response){
            //Comprueba si el string de respuesta del server es un si o un no
            return response.contains("Success");
        }
    }
}
