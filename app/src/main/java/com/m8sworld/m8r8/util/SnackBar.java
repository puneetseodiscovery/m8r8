package com.m8sworld.m8r8.util;

import android.app.Activity;
import android.support.design.widget.Snackbar;

public class SnackBar {
    public static Snackbar snackbar(Activity activity, String message) {
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();
        return snackbar;
    }
}
