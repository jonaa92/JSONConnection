package casasnovas.auger.jsonconnection;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Augger on 30/10/2015.
 */
public class notRegisteredDialog extends DialogFragment{
    static notRegisteredDialog newInstance (String user){
        notRegisteredDialog nrd = new notRegisteredDialog();
        Bundle b = new Bundle();
        b.putString("error", "Error, usuari inexistent. \n" + "Vol crear-lo? \n");
        nrd.setArguments(b);
        return nrd;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog ad = new AlertDialog.Builder(getActivity()).setTitle("Error d'autenticacio")
                .setPositiveButton(getResources().getString(R.string.yesButton), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getActivity().getApplicationContext(), NewUserActivity.class);
                        /*TODO: Al activity NewUserActivity se le pasara el mail que el user habia puesto, para facilitar el registro*/
                        startActivity(i);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.noButton), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setMessage("\tError, usuari inexistent. \n" + "\tVol crear-lo? \n")
                .create();
        return ad;
    }
}
