package com.m8sworld.m8r8.util;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MultiPart {
    Context context;
    String string;

    public MultiPart(Context context, String string) {
        this.context = context;
        this.string = string;
    }

    public MultipartBody.Part sendImageFileToserver(Bitmap bitMap) throws IOException {
        File filesDir = context.getFilesDir();
        File file = new File(filesDir, "image" + ".png");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData(string, file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "bill_image");

        return body;
    }
}
