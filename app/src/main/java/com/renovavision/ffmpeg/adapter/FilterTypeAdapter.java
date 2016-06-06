package com.renovavision.ffmpeg.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.renovavision.ffmpeg.R;
import com.renovavision.ffmpeg.filter.FilterOperation;

import java.util.List;

public class FilterTypeAdapter extends RecyclerView.Adapter<FilterTypeViewHolder> {

    @NonNull
    protected List<FilterOperation.FilterType> filterItemTypes;

    public FilterTypeAdapter(@NonNull List<FilterOperation.FilterType> filterItemTypes) {
        this.filterItemTypes = filterItemTypes;
    }

    @Override
    public FilterTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.filter_effect_item, null);
        return new FilterTypeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FilterTypeViewHolder holder, int position) {
        holder.onBindData(getFilterType(position));
    }

    @Override
    public int getItemCount() {
        return filterItemTypes.size();
    }

    public FilterOperation.FilterType getFilterType(int position) {
        return filterItemTypes.get(position);
    }
}
