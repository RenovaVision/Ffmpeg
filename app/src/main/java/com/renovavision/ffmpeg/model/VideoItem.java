package com.renovavision.ffmpeg.model;

import android.support.annotation.NonNull;

import com.renovavision.ffmpeg.filter.FilterOperation;

public class VideoItem {

    public String sourcePath;

    public String tempPath;

    public String mimeType;

    public FilterOperation filterOperation;

    public VideoItem(@NonNull String sourcePath, @NonNull String mimeType) {
        this.sourcePath = sourcePath;
        this.mimeType = mimeType;
    }
}
