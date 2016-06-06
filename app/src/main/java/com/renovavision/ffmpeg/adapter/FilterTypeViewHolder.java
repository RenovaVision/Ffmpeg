package com.renovavision.ffmpeg.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.renovavision.ffmpeg.R;
import com.renovavision.ffmpeg.filter.FilterOperation;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FilterTypeViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.filter_text)
    public TextView filterText;

    public FilterTypeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBindData(@NonNull FilterOperation.FilterType filterType) {
        filterText.setText(filterType.getResId());
    }
}
