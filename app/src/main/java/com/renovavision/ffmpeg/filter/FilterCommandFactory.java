package com.renovavision.ffmpeg.filter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.renovavision.ffmpeg.model.VideoItem;

import java.util.Locale;

// class for generation FFMpeg commands
public class FilterCommandFactory {

    // main ffmpeg command - will add filter special args
    public static final String[] CMD_FFMPEG_ARGS = new String[]{"ffmpeg", "-y", "-i", "%s", "-strict", "experimental", "-ar", "44100", "-vf", "%s", "-vcodec", "mpeg4", "%s"};

    // contrast filter
    public static final String CMD_FORMAT_CONTRAST = "scale=hd720,eq=contrast=%.2f";

    // blue filter
    public static final String CMD_FORMAT_BLUE = "scale=hd720,curves=green='0.50/0.55':blue='0.5/0.67'";

    // verde filter
    public static final String CMD_FORMAT_VERDE = "scale=hd720,colorlevels=rimin=0.039:gimin=0.049:bimin=0.039:rimax=0.96:gimax=0.96:bimax=0.96";

    // sharpen filter
    public static final String CMD_FORMAT_SHARPEN = "scale=hd720,unsharp=luma_msize_x=5:luma_msize_y=5:luma_amount=%.2f";

    // haze filter
    public static final String CMD_FORMAT_HAZE = "scale=hd720,curves=blue='0.5/%.2f'";

    // saturation filter
    public static final String CMD_FORMAT_SATURATION = "scale=hd720,eq=saturation=%.2f";

    // vignette filter
    public static final String CMD_FORMAT_VIGNETTE = "scale=hd720,vignette=PI/2";

    // blur filter
    public static final String CMD_FORMAT_BLUR = "scale=hd720,boxblur=luma_radius=%d:luma_power=2";

    // crush filter
    public static final String CMD_FORMAT_CRUSH = "scale=hd720,curves=green='0.50/0.67':red='0.5/0.55'";

    @Nullable
    public static String[] getFilterCommand(@NonNull VideoItem item) {

        FilterOperation filterOperation = item.filterOperation;
        if (filterOperation == null) {
            return null;
        }
        FilterOperation.FilterType filterType = filterOperation.getFilterType();
        if (filterType == FilterOperation.FilterType.BLUE) {
            return getArgs(item, CMD_FORMAT_BLUE);
        } else if (filterType == FilterOperation.FilterType.BLUR) {
            return getFFmpegBlurFilterCmd(item, filterOperation.getValue());
        } else if (filterType == FilterOperation.FilterType.CONTRAST) {
            return getFFmpegContrastFilterCmd(item, filterOperation.getValue());
        } else if (filterType == FilterOperation.FilterType.CRUSH) {
            return getArgs(item, CMD_FORMAT_CRUSH);
        } else if (filterType == FilterOperation.FilterType.HAZE) {
            return getFFmpegHazeFilterCmd(item, filterOperation.getValue());
        } else if (filterType == FilterOperation.FilterType.SATURATION) {
            return getFFmpegSaturationFilterCmd(item, filterOperation.getValue());
        } else if (filterType == FilterOperation.FilterType.SHARPEN) {
            return getFFmpegSharpenFilterCmd(item, filterOperation.getValue());
        } else if (filterType == FilterOperation.FilterType.VERDE) {
            return getArgs(item, CMD_FORMAT_VERDE);
        } else if (filterType == FilterOperation.FilterType.VIGNETTE) {
            return getArgs(item, CMD_FORMAT_VIGNETTE);
        }
        return null;
    }

    @NonNull
    public static String[] getFFmpegBlurFilterCmd(@NonNull VideoItem item, float value) {
        return getArgs(item, String.format(Locale.ENGLISH, CMD_FORMAT_BLUR, (int) (1.f + value / 5.f)));
    }

    @NonNull
    public static String[] getFFmpegContrastFilterCmd(@NonNull VideoItem item, float value) {
        return getArgs(item, String.format(Locale.ENGLISH, CMD_FORMAT_CONTRAST, (value - 50.f) / 26.f));
    }

    @NonNull
    public static String[] getFFmpegHazeFilterCmd(@NonNull VideoItem item, float value) {
        return getArgs(item, String.format(Locale.ENGLISH, CMD_FORMAT_HAZE, .5f + (value - 50.f) / 167.f));
    }

    @NonNull
    public static String[] getFFmpegSaturationFilterCmd(@NonNull VideoItem item, float value) {
        return getArgs(item, String.format(Locale.ENGLISH, CMD_FORMAT_SATURATION, value / 34.f));
    }

    @NonNull
    public static String[] getFFmpegSharpenFilterCmd(@NonNull VideoItem item, float value) {
        return getArgs(item, String.format(Locale.ENGLISH, CMD_FORMAT_SHARPEN, (value - 50.f) / 34.f));
    }

    @NonNull
    private static String[] getArgs(@NonNull VideoItem item, String value) {
        String[] args = CMD_FFMPEG_ARGS;
        args[3] = item.sourcePath;
        args[9] = value;
        args[12] = item.tempPath;
        return args;
    }
}
