package com.renovavision.ffmpeg.worker;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.renovavision.ffmpeg.filter.FilterCommandFactory;
import com.renovavision.ffmpeg.jni.LoadJNI;
import com.renovavision.ffmpeg.model.VideoItem;
import com.renovavision.ffmpeg.utils.FileUtils;

import java.lang.ref.WeakReference;

public class VideoFilterWorker extends AsyncTask<Void, Void, VideoItem> {

    private static final String TAG = VideoFilterWorker.class.getSimpleName();

    public static final int VIDEO_PROCESSING_ERROR = -1;
    public static final int VIDEO_PROCESSING_FINISHED = 0;

    @NonNull
    protected WeakReference<Callback> callbackWeakReference;

    @NonNull
    protected VideoItem videoItem;

    private WeakReference<LoadJNI> ffmpegJNIReference;

    public VideoFilterWorker(@NonNull VideoItem videoItem, @NonNull Callback callback, @NonNull LoadJNI ffmpegJNI) {
        this.videoItem = videoItem;
        this.callbackWeakReference = new WeakReference<>(callback);
        ffmpegJNIReference = new WeakReference<>(ffmpegJNI);
    }

    protected VideoItem processItem(@NonNull Context context, @NonNull VideoItem videoItem) {
        Callback callback = callbackWeakReference.get();
        LoadJNI ffmpegJNI = ffmpegJNIReference.get();
        if (ffmpegJNI == null) {
            if (callback != null) {
                callback.onFinished(videoItem, VIDEO_PROCESSING_ERROR);
            }
            return videoItem;
        }

        if (TextUtils.isEmpty(videoItem.tempPath)) {
            videoItem.tempPath = FileUtils.getTempPath(context, videoItem.mimeType);
            if (TextUtils.isEmpty(videoItem.tempPath)) {
                Log.e(TAG, "Failed to create temp file.");
                // can not transform video - will use original photo instead
                videoItem.tempPath = videoItem.sourcePath;
                return videoItem;
            }
        }

        String[] filterCmd = FilterCommandFactory.getFilterCommand(videoItem);
        String fullPath = videoItem.tempPath;
        String workPath = fullPath.substring(0, fullPath.lastIndexOf("/") + 1);

        try {
            ffmpegJNI.run(filterCmd, workPath, context);
            if (callback != null) {
                callback.onFinished(videoItem, VIDEO_PROCESSING_FINISHED);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to filter video.");
            if (callback != null) {
                callback.onFinished(videoItem, VIDEO_PROCESSING_ERROR);
            }
        }

        return videoItem;
    }

    @Override
    protected VideoItem doInBackground(Void... params) {
        Callback callback = callbackWeakReference.get();
        if (callback == null) {
            return videoItem;
        }

        Context context = callback.getContext();
        return processItem(context, videoItem);
    }

    @Override
    protected void onPostExecute(VideoItem videoItem) {
        Callback callback = callbackWeakReference.get();
        if (callback != null) {
            callback.onFinished(videoItem, 0);
        }
    }

    public interface Callback {

        void onFinished(@NonNull VideoItem videoItem, int lastErrorCode);

        @NonNull
        Context getContext();

    }

}