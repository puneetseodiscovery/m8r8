package com.m8sworld.m8r8.util;

import android.app.ProgressDialog;
import android.content.Context;


public class ProgressBarClass {
    public static ProgressDialog showProgressDialog(Context context) {
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setMessage("Please wait...");
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.setCanceledOnTouchOutside(false);
        return m_Dialog;
    }


}
