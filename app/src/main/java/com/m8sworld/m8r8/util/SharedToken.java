package com.m8sworld.m8r8.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedToken {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedToken(Context context) {
        this.context = context;

    }

    public void setUserId(String token) {
        sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("userId", token);
        ;
        editor.apply();
    }

    public void setCatid(String catid) {
        sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("catId", catid);
        editor.apply();
    }

    public String getUserId() {
        sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("userId", "");
        return data;
    }

    public String getCatId() {
        sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("catId", "");
        return data;
    }

    public void clearShaerd() {
        sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }

}
