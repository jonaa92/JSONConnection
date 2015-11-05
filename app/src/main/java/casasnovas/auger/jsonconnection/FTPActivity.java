package casasnovas.auger.jsonconnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class FTPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button b = (Button) findViewById(R.id.bFTP);
        b.setText(R.string.connect);
        b.setEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void button (View v){
        backgroundFTP bf = new backgroundFTP();
        bf.execute();
        Button b = (Button) findViewById(R.id.bFTP);
        b.setText(" CONNECTING... ");
        b.setEnabled(false);
    }

    private class backgroundFTP extends AsyncTask <Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try {
                serverInterface.provaFTP();
            } catch (IOException e) {
                Log.d("ftpconnect", "Exception: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Button b = (Button) findViewById(R.id.bFTP);
            b.setText(R.string.connect);
            b.setEnabled(true);
        }
    }

}
