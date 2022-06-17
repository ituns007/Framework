package org.ituns.framework.master.tools.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import org.ituns.framework.master.tools.network.IUri;

import java.io.File;
import java.util.HashMap;

public class IVideo {
    private final MediaMetadataRetriever mmr;

    private IVideo(String url) {
        mmr = new MediaMetadataRetriever();
        mmr.setDataSource(url, new HashMap<>());
    }

    private IVideo(Context context, Uri uri) {
        mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context, uri);
    }

    public long duration() {
        try {
            int keyCode = MediaMetadataRetriever.METADATA_KEY_DURATION;
            return Long.parseLong(mmr.extractMetadata(keyCode));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Bitmap thumbnail() {
        try {
            return mmr.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void release() {
        try {
            mmr.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static IVideo with(String url) {
        return new IVideo(url);
    }

    public static IVideo with(Context context, Uri uri) {
        return new IVideo(context, uri);
    }

    public static long getSize(Context context, Uri uri) {
        try {
            return new File(IUri.with(uri).path(context)).length();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long getDuration(String url) {
        IVideo video = IVideo.with(url);
        long duration = video.duration();
        video.release();
        return duration;
    }

    public static long getDuration(Context context, Uri uri) {
        IVideo video = IVideo.with(context, uri);
        long duration = video.duration();
        video.release();
        return duration;
    }

    public static Bitmap getThumbnail(String url) {
        IVideo video = IVideo.with(url);
        Bitmap thumbnail = video.thumbnail();
        video.release();
        return thumbnail;
    }

    public static Bitmap getThumbnail(Context context, Uri uri) {
        IVideo video = IVideo.with(context, uri);
        Bitmap thumbnail = video.thumbnail();
        video.release();
        return thumbnail;
    }
}
