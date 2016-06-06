package com.renovavision.ffmpeg.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static String getFileSuffixForImage(@NonNull String mimeType) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
    }

    @Nullable
    public static String getTempPath(@NonNull Context context, @NonNull String mimeType) {
        String fileSuffix = "." + getFileSuffixForImage(mimeType);
        File file;
        try {
            file = File.createTempFile("file", fileSuffix, context.getExternalCacheDir());
            return file.getAbsolutePath();
        } catch (IOException e) {
            Log.e(e.getMessage(), "Failed to create a file.");
        }
        return null;
    }
}
