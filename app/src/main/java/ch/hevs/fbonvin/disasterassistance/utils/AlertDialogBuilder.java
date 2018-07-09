package ch.hevs.fbonvin.disasterassistance.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public abstract class AlertDialogBuilder {


    /**
     * Create a simple AlertDialog with a positive action only
     * @param activity Activity in which the dialog is launched
     * @param title Title of the dialog
     * @param message Message of the dialog
     * @param positiveMessage Positive message
     * @param positiveAction Positive action launched when clicked
     */
    public static void showAlertDialogPositive(Activity activity, String title, String message,
                                               String positiveMessage, DialogInterface.OnClickListener positiveAction) {

        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveMessage, positiveAction)
                .create().show();
    }

    /**
     * Create a simple AlertDialog with positive and negative actions
     * @param activity Activity in which the dialog is launched
     * @param title Title of the dialog
     * @param message Message of the dialog
     * @param positiveMessage Positive message
     * @param positiveAction Positive action launched when clicked
     * @param negativeMessage Negative message
     * @param negativeAction Negative action launched when clicked
     */
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
