package com.renovavision.ffmpeg.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RectangleItemDecoration extends RecyclerView.ItemDecoration {

    protected final int mSpace;

    public RectangleItemDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpace / 2;
        outRect.right = mSpace / 2;
        outRect.top = mSpace / 2;
        outRect.bottom = mSpace / 2;
    }
}
