package org.ituns.framework.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import org.ituns.framework.R;
import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.framework.master.service.tasks.BackProxy;
import org.ituns.framework.master.service.tasks.MainProxy;
import org.ituns.framework.master.tools.calendar.IMillis;
import org.ituns.framework.master.tools.media.IVideo;

public class Test1Activity extends AppCompatActivity {
    private AppCompatImageView imageView;
    private AppCompatTextView detailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        imageView = findViewById(R.id.image);
        detailView = findViewById(R.id.detail);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        createThumb();
    }

    private void createThumb() {
        BackProxy.post(() -> {
//            String url = "http://cxylfile.oss-cn-shanghai.aliyuncs.com/yl/20220509/iOS_register_tz_sh_view_123_1652095450304.mp4?Expires=1967512880&OSSAccessKeyId=LTAI5tD1c5UxTy12JWvnXhNm&Signature=fXXG2Lza1G%2BDVZsNy%2For0kREkw4%3D";
            String url = "https://vd3.bdstatic.com/mda-ne85x3gw17x9nh2q/sc/cae_h264/1652071632849737542/mda-ne85x3gw17x9nh2q.mp4?v_from_s=hkapp-haokan-hna&auth_key=1652164646-0-0-76b730f41872bc8c09a1a69813f344b8&bcevod_channel=searchbox_feed&pd=1&cd=0&pt=3&logid=0446299664&vid=10588174514774712831&abtest=101830_2-17451_1&klogid=0446299664";

            Logcat.e("Time1:" + IMillis.with().formatSlash());
            IVideo video = IVideo.with(url);
            Logcat.e("Time2:" + IMillis.with().formatSlash());
            Bitmap bitmap = video.thumbnail();
            Logcat.e("Time3:" + IMillis.with().formatSlash());
            long duration = video.duration();
            Logcat.e("Time4:" + IMillis.with().formatSlash());
            video.release();
            Logcat.e("Time5:" + IMillis.with().formatSlash());

            refreshThumb(bitmap, duration);
        });
    }

    private void refreshThumb(Bitmap bitmap, long duration) {
        MainProxy.post(() -> {
            imageView.setImageBitmap(bitmap);
            detailView.setText("Duration:" + duration);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void load(Activity activity) {
        Intent intent = new Intent(activity, Test1Activity.class);
        activity.startActivity(intent);
    }
}