package com.renovavision.ffmpeg.filter;


import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.renovavision.ffmpeg.R;

public class FilterOperation {

    // filters type
    public enum FilterType {

        CRUSH(R.string.filter_crush),
        BLUE(R.string.filter_blue),
        VERDE(R.string.filter_verde),
        VIGNETTE(R.string.filter_vignette),
        CONTRAST(R.string.filter_contrast),
        BLUR(R.string.filter_blur),
        SHARPEN(R.string.filter_sharpen),
        HAZE(R.string.filter_haze),
        SATURATION(R.string.filter_saturation);

        @StringRes
        private final int resId;

        FilterType(@StringRes int resId) {
            this.resId = resId;
        }

        public int getResId() {
            return resId;
        }
    }


    private static final float MIN = 1.f;
    private static final float MAX = 100.f;

    // default filter value
    private float value;
    private final float defValue;

    private FilterType filterType;

    private boolean canChangeParams;

    public FilterOperation(@NonNull FilterType filterType) {
        this(filterType, 0.f, 0.f, false);
    }

    public FilterOperation(@NonNull FilterType filterType, float defValue, float value, boolean canChangeParams) {
        this.filterType = filterType;
        this.defValue = defValue;
        this.value = value;
        this.canChangeParams = canChangeParams;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public float getDefValue() {
        return defValue;
    }

    public float getValue() {
        return value;
    }

    public boolean isCanChangeParams() {
        return canChangeParams;
    }

    public void setValue(float value) {
        this.value = Math.max(MIN, Math.min(MAX, value));
    }
}
