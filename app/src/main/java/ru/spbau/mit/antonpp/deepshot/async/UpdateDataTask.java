package ru.spbau.mit.antonpp.deepshot.async;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import ru.spbau.mit.antonpp.deepshot.MainApplication;

/**
 * @author antonpp
 * @since 04/12/15
 */
public class UpdateDataTask extends AsyncTask<Void, Void, Boolean> {

    private final DialogInterface progressDialog;
    private final Context context;

    public UpdateDataTask(DialogInterface progressDialog, Context context) {
        this.progressDialog = progressDialog;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return MainApplication.getDataWrapper().sync();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        progressDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!result) {
            builder.setTitle("Error...").
                    setMessage("Could not synchronize all data.").
                    setIcon(android.R.drawable.ic_dialog_alert);
        } else {
            builder.setTitle("Updated!").setMessage("All data successfully updated.").
                    setIcon(android.R.drawable.ic_dialog_info);
        }

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }
}
