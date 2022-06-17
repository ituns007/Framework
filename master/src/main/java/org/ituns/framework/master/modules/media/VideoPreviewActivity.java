package org.ituns.framework.master.modules.media;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;

import org.ituns.framework.master.R;
import org.ituns.framework.master.mvvm.activity.MVVMActivity;
import org.ituns.framework.master.service.channel.loading.LoadingProxy;
import org.ituns.framework.master.tools.android.IClick;
import org.ituns.framework.master.tools.android.IWindow;

import java.io.File;

public class VideoPreviewActivity extends MVVMActivity<Object> implements Player.EventListener {
    private SimpleExoPlayer exoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fw_activity_video_preview);
        initializeData();
        initializeView();
    }

    private void initializeData() {
        exoPlayer = new SimpleExoPlayer.Builder(this).build();
        exoPlayer.addListener(this);
    }

    private void initializeView() {
        IClick.single(findViewById(R.id.back), v -> finish());
        PlayerView playerView = findViewById(R.id.video);
        playerView.setPlayer(exoPlayer);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Uri uri = intents.getParcelable("uri");
        if(uri != null && exoPlayer != null) {
            exoPlayer.setMediaItem(MediaItem.fromUri(uri));
            exoPlayer.prepare();
        } else {
            showExitDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStatusBarColor();
        if(exoPlayer != null) {
            exoPlayer.play();
        }
    }

    protected void refreshStatusBarColor() {
        IWindow.statusBar(this, false, Color.BLACK);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(exoPlayer != null) {
            exoPlayer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(exoPlayer != null) {
            exoPlayer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onPlaybackStateChanged(int state) {
        if(state == Player.STATE_BUFFERING) {
            LoadingProxy.show();
        } else {
            LoadingProxy.hide();
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        showErrorDialog();
    }

    private void showExitDialog() {
        MessageDialog.build(this)
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setTitle("错误")
                .setMessage("视频路径为空")
                .setCancelable(false)
                .setCancelButton("退出", (dialog, v) -> {
                    finish();
                    return false;
                })
                .show();
    }

    private void showErrorDialog() {
        MessageDialog.build(this)
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setTitle("错误")
                .setMessage("视频播放错误")
                .setCancelable(true)
                .setCancelButton("退出", (dialog, v) -> {
                    finish();
                    return false;
                })
                .setOkButton("重试", (dialog, v) -> {
                    if(exoPlayer != null) {
                        exoPlayer.prepare();
                        exoPlayer.play();
                    }
                    return false;
                })
                .show();
    }

    public static void show(Activity activity, String path) {
        show(activity, new File(path));
    }

    public static void show(Activity activity, File file) {
        show(activity, Uri.fromFile(file));
    }

    public static void show(Activity activity, Uri uri) {
        Intent intent = new Intent(activity, VideoPreviewActivity.class);
        intent.putExtra("uri", uri);
        activity.startActivity(intent);
    }
}