package casasnovas.auger.jsonconnection;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.Random;

public class NewUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    public void registrarListener (View v) {
        String username, email, password;
        EditText et = (EditText) findViewById(R.id.etUser);
        username = et.getText().toString();

        et = (EditText) findViewById(R.id.etMail);
        email = et.getText().toString();


        et = (EditText) findViewById(R.id.etPassword);
        password = et.getText().toString();

        backgroundRegister br = new backgroundRegister();
        br.execute(username, email, password);
        Button b = (Button) findViewById(R.id.bRegistrar);
        b.setText(" CONNECTING... ");
    }

    private class backgroundRegister extends AsyncTask <String, Void, Void>{
        Boolean b;
        @Override
        protected Void doInBackground(String... params) {
            b = false;
            try {
                postNewUser(params[0], params[1], params[2]);
            } catch (IOException e) {
                Log.d("backgroundRegister", "IOException: "+e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (b){
                Intent i = new Intent(getApplicationContext(), correctActivity.class);
                startActivity(i);
            }
            else {
                Button b = (Button) findViewById(R.id.bRegistrar);
                b.setText(R.string.connect);
            }
        }

        private void postNewUser(String user, String passs, String  mail) throws IOException {
            Random r = new Random();
            Integer token = 300;
            String postData = "name:"+user+"&email:"+mail+"&passw:"+passs+"&token:"+token.toString();
            String resp = serverInterface.doPost("https://raspynet.herokuapp.com/users", postData);
            b = resp.contains("Success");

        }

    }

}
