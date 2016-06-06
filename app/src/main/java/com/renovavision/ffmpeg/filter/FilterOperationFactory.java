package com.renovavision.ffmpeg.filter;

import android.support.annotation.NonNull;

public class FilterOperationFactory {

    public static FilterOperation getFilterOperation(
            @NonNull FilterOperation.FilterType filterType) {
        if (filterType == FilterOperation.FilterType.BLUR) {
            return new FilterOperation(filterType, 50.f, 50.f, true);
        } else if (filterType == FilterOperation.FilterType.CONTRAST) {
            return new FilterOperation(filterType, 50.f, 50.f, true);
        } else if (filterType == FilterOperation.FilterType.HAZE) {
            return new FilterOperation(filterType, 25.f, 25.f, true);
        } else if (filterType == FilterOperation.FilterType.SATURATION) {
            return new FilterOperation(filterType, 25.f, 25.f, true);
        } else if (filterType == FilterOperation.FilterType.SHARPEN) {
            return new FilterOperation(filterType, 60.f, 60.f, true);
        }
        return new FilterOperation(filterType);
    }
}
