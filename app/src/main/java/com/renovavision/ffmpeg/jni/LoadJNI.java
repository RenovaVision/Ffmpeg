package com.renovavision.ffmpeg.jni;

import android.content.Context;
import android.util.Log;

public class LoadJNI {

    private static final String TAG = "ffmpeg4android";

    static {
        System.loadLibrary("loader-jni");
    }


    public void run(String[] args, String workFolder, Context ctx, boolean isValidate) throws RuntimeException {
        load(args, workFolder, getVideokitLibPath(ctx), isValidate);
    }

    public void run(String[] args, String workFolder, Context ctx) throws RuntimeException {
        run(args, workFolder, ctx, true);
    }

    private static String getVideokitLibPath(Context ctx) {
        String videokitLibPath = ctx.getFilesDir().getParent()  + "/lib/libvideokit.so";
        Log.i(TAG, "videokitLibPath: " + videokitLibPath);
        return videokitLibPath;
    }

    public void fExit( Context ctx) {
        fexit(getVideokitLibPath(ctx));
    }

    public native String fexit(String videokitLibPath);

    public native String unload();

    public native String load(String[] args, String videokitSdcardPath, String videokitLibPath, boolean isComplex);
}
