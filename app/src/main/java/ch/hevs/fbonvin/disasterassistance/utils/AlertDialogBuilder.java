package ch.hevs.fbonvin.disasterassistance.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public abstract class AlertDialogBuilder {


    public static void showAlertDialogPositive(Activity activity, String title, String message,
                                               String positiveMessage, DialogInterface.OnClickListener positiveAction) {

        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveMessage, positiveAction)
                .create().show();
    }

    public static void showAlertDialogPositiveNegative(Activity activity, String title, String message,
                                                       String positiveMessage, DialogInterface.OnClickListener positiveAction,
                                                       String negativeMessage, DialogInterface.OnClickListener negativeAction) {

        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveMessage, positiveAction)
                .setNegativeButton(negativeMessage, negativeAction)
                .create().show();
    }
}
