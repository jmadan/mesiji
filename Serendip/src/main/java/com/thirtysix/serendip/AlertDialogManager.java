package com.thirtysix.serendip;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

public class AlertDialogManager {

    AlertDialog.Builder alert;

    public AlertDialogManager(Context c){
        alert = new AlertDialog.Builder(c);
    }

    public void showAlertDialog(Context context, String title, String message) {
//        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setCancelable(true);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}
