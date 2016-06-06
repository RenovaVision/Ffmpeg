package com.renovavision.ffmpeg.jni;

import android.content.Context;

public class LicenseJNI {

    public int licenseCheck(String path, Context ctx) {
        String rcStr = "-100";
        rcStr = licenseCheckSimpleJNI(path);
        int rc = Integer.decode(rcStr);
        return rc;
    }


    public native String licenseCheckComplexJNI(String path);

    public native String licenseCheckSimpleJNI(String path);


    static {
        System.loadLibrary("license-jni");
    }
}
