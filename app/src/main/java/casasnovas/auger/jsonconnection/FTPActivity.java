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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTPFile;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FTPActivity extends AppCompatActivity {


    /*
      backgroundFTP Commands:
        "upload" (backgroundftp.execute("upload")) -> Uploads a file to server
        "download" -> Downloads a file from the server
        "ls" -> ls files
        "delete" -> delete file selected
     */
    FTPFile[] filesystem;
    String location, itemSelectedName;
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

        location = "dir";
        itemSelectedName = null;
        TextView tv = (TextView) findViewById(R.id.tvCurrent);
        tv.setText("Current directory: " + location);
        backgroundFTP bf;
        bf = new backgroundFTP();
        bf.execute("ls");

    }
    public void button (View v){
        final backgroundFTP bf;
        bf = new backgroundFTP();
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
                        bf.execute("upload", file.getPath(), file.getName());
                        Button b1 = (Button) findViewById(R.id.bFTPupload);
                        b1.setText(" UPLOADING... ");
                        b1.setEnabled(false);

                    }
                }).showDialog();
                break;
            case R.id.bRefresh:
                final ListView listView = (ListView) findViewById(R.id.lvLS);
                String array[] = new String[filesystem.length];
                for (int i=0; i<filesystem.length; ++i) array[i] = filesystem[i].getName();
                listView.setAdapter(new ArrayAdapter<>(getApplication().getApplicationContext(), R.layout.listviewitem, R.id.listviewitemtext, array));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        /*TODO: para hacer que se mantenga seleccionado un item, hay que crear un layout nuevo*/
                        TextView selectedItem = (TextView) findViewById(R.id.tvSelectedItem);
                        selectedItem.setText(filesystem[position].getName());
                        itemSelectedName = filesystem[position].getName();
                    }
                });
                break;
            case R.id.bDelete:
                bf.execute("delete");
        }

    }


    private class backgroundFTP extends AsyncTask <String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {
            switch (params[0]) {
                case "download":
                    try {
                        if (itemSelectedName != null) {
                            serverInterface.downloadFTP("anonymous", "mailinventat", location + "/" + itemSelectedName, Environment.getExternalStorageDirectory().getPath() + "/download/"+itemSelectedName);
                        }

                    } catch (IOException e) {
                        Log.d("ftpconnectDownload", "Exception: " + e.getMessage());
                    }
                    break;
                case "upload":
                    try {
                        serverInterface.uploadFTP("anonymous", "mailinventat", params[1], "dir/" + params[2]);
                    } catch (IOException e) {
                        Log.d("ftpconnectUpload", "Exception: " + e.getMessage());
                    }
                    break;
                case "ls":
                    try {
                        filesystem = serverInterface.lsFTP(location, "anonymous", "patata");
                    } catch (IOException e) {
                        Log.d("ftpconnectLS", "Exception: " + e.getMessage());
                    }
                    break;
                case "delete":
                    try {
                        if (itemSelectedName != null) {
                            /*TODO: crear un dialog de "estas seguro de querer eliminar?" */
                            serverInterface.deletefile(location + "/" + itemSelectedName, "anonymous", "patata");
                        }
                    } catch (IOException e) {
                        Log.d("ftpconnectDelete", "Exception: " +e.getMessage());
                    }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //Restauramos el valor de los botones
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
