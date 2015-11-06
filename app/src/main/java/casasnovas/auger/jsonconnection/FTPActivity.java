package casasnovas.auger.jsonconnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FTPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button b = (Button) findViewById(R.id.bFTP);
        b.setText("DOWNLOAD");
        b.setEnabled(true);

        Button b1 = (Button) findViewById(R.id.bFTPupload);
        b1.setText("UPLOAD");
        b1.setEnabled(true);

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
        final backgroundFTP bf = new backgroundFTP();
        switch (v.getId()){
            case R.id.bFTP:
                bf.execute("download");
                Button b = (Button) findViewById(R.id.bFTP);
                b.setText(" CONNECTING... ");
                b.setEnabled(false);
                break;
            case R.id.bFTPupload:
                FileChooser fc = new FileChooser(this);
                fc.setFileListener(new FileChooser.FileSelectedListener() {
                    @Override
                    public void fileSelected(File file) {
                        Log.d("filechooser", "File to upload: " + file.getPath());
                        bf.execute("upload", file.getPath());
                        Button b1 = (Button) findViewById(R.id.bFTPupload);
                        b1.setText(" UPLOADING... ");
                        b1.setEnabled(false);

                    }
                }).showDialog();
                break;
        }

    }

    private class backgroundFTP extends AsyncTask <String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {
            if (params[0].equals("download")) {
                try {
                    serverInterface.downloadFTP("anonymous", "mailinventat", "dir/file", Environment.getExternalStorageDirectory().getPath() + "/download/file");
                } catch (IOException e) {
                    Log.d("ftpconnectDownload", "Exception: " + e.getMessage());
                }
                return null;
            }
            else {
                try {
                    serverInterface.uploadFTP("anonymous", "mailinventat", params[1], "dir/fileprova");
                } catch (IOException e) {
                    Log.d("ftpconnectUpload", "Exception: " + e.getMessage());
                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Button b = (Button) findViewById(R.id.bFTP);
            b.setText("DOWNLOAD");
            b.setEnabled(true);
            Button b1 = (Button) findViewById(R.id.bFTPupload);
            b1.setText("UPLOAD");
            b1.setEnabled(true);
        }
    }

}
