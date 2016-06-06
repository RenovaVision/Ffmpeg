package com.renovavision.ffmpeg.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SeekBar;

import com.renovavision.ffmpeg.R;
import com.renovavision.ffmpeg.adapter.FilterTypeAdapter;
import com.renovavision.ffmpeg.filter.FilterOperation;
import com.renovavision.ffmpeg.filter.FilterOperationFactory;
import com.renovavision.ffmpeg.jni.LoadJNI;
import com.renovavision.ffmpeg.model.VideoItem;
import com.renovavision.ffmpeg.utils.FileUtils;
import com.renovavision.ffmpeg.utils.RectangleItemDecoration;
import com.renovavision.ffmpeg.utils.RecyclerItemClickListener;
import com.renovavision.ffmpeg.worker.VideoFilterWorker;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, VideoFilterWorker.Callback {

    private static final String VIDEO_MP4 = "video/mp4";
    private static final String FILTERING_WAKE_LOCK = "FILTERING_WAKE_LOCK";

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.filter_seek_bar)
    SeekBar seekBar;

    private VideoFilterWorker videoFilterWorker;

    private FilterTypeAdapter filterTypeAdapter;
    private VideoItem videoItem;
    private FilterOperation currentFilterOperation;

    private ProgressDialog progressDialog;

    private AtomicBoolean isVideoFilterWorkerRunning = new AtomicBoolean(false);
    protected AtomicBoolean isActivityVisible = new AtomicBoolean(false);

    private LoadJNI ffmpegJNI;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // configure jni for ffmpeg
        ffmpegJNI = new LoadJNI();
        powerManager = (PowerManager) getSystemService(Activity.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, FILTERING_WAKE_LOCK);

        // video item  - default video item
        videoItem = new VideoItem("android.resource://" + getPackageName() + "/" + R.raw.demo_video, VIDEO_MP4);
        videoItem.tempPath = FileUtils.getTempPath(this, VIDEO_MP4);

        seekBar.setVisibility(View.GONE);

        prepareRecyclerView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

        if (videoFilterWorker != null) {
            videoFilterWorker.cancel(true);
        }
    }

    private void prepareRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final List<FilterOperation.FilterType> filterItemTypes = Arrays.asList(FilterOperation.FilterType.values());
        filterTypeAdapter = new FilterTypeAdapter(filterItemTypes);
        recyclerView.setAdapter(filterTypeAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new RectangleItemDecoration(10));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FilterOperation.FilterType filterType = filterTypeAdapter.getFilterType(position);
                FilterOperation filterOperation = FilterOperationFactory.getFilterOperation(filterType);

                currentFilterOperation = filterOperation;

                if (filterOperation.isCanChangeParams()) {
                    seekBar.setProgress((int) currentFilterOperation.getDefValue());
                    seekBar.setVisibility(View.VISIBLE);
                } else {
                    seekBar.setVisibility(View.GONE);
                }

                videoItem.filterOperation = filterOperation;
            }
        }));
    }

    @OnClick(R.id.apply_filter_button)
    public void onCLick(View view) {
        showProgress();

        wakeLock.acquire();
        videoFilterWorker = new VideoFilterWorker(videoItem, this, ffmpegJNI);
        videoFilterWorker.execute();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (currentFilterOperation == null) {
            return;
        }
        currentFilterOperation.setValue(seekBar.getProgress());
    }

    @Override
    public void onFinished(@NonNull VideoItem videoItem, int lastErrorCode) {
        isVideoFilterWorkerRunning.set(false);
        hideProgress();

        if (wakeLock.isHeld()) {
            wakeLock.release();
        }

        videoFilterWorker = null;

        this.videoItem = videoItem;

        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.setData(Uri.fromFile(new File(videoItem.tempPath)));
        startActivity(intent);
    }

    @NonNull
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityVisible.set(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityVisible.set(false);
        hideProgress();
    }


    public void showProgress() {
        if (isActivityVisible.get() || (progressDialog != null && progressDialog.isShowing())) {
            hideProgress();
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Wait");
            progressDialog.setMessage("Applying filter...");
            progressDialog.show();
        }
    }

    public void hideProgress() {
        if (isActivityVisible.get() && progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
            progressDialog.dismiss();
        }
    }
}
