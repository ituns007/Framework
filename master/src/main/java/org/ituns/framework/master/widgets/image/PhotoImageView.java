package org.ituns.framework.master.widgets.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import org.ituns.framework.master.modules.media.PhotoPreviewDialog;
import org.ituns.framework.master.service.logcat.Logcat;

import java.io.File;

public class PhotoImageView extends AppCompatImageView implements View.OnClickListener {

    public PhotoImageView(@NonNull Context context) {
        this(context, null);
    }

    public PhotoImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
    }

    public void showPath(String path) {
        showFile(new File(path));
    }

    public void showFile(File file) {
        showUri(Uri.fromFile(file));
    }

    public void showUri(Uri uri) {
        Glide.with(this).asBitmap().load(uri).into(new PhotoTarget() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                setImageBitmap(resource);
            }
        });
    }

    public void showModel(Object model) {
        Glide.with(this).asBitmap().load(model).into(new PhotoTarget() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                setImageBitmap(resource);
            }
        });
    }

    public void showBase64(String base64) {
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        Glide.with(this).asBitmap().load(data).into(new PhotoTarget() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                setImageBitmap(resource);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Drawable drawable = getDrawable();
        if(drawable == null) {
            Logcat.e("drawable is null.");
            return;
        }

        Context context = getContext();
        if(context instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) context;
            PhotoPreviewDialog.showDrawable(activity.getSupportFragmentManager(), drawable);
        }
    }

    private static abstract class PhotoTarget implements Target<Bitmap> {
        private Request request;

        @Override
        public void onLoadStarted(@Nullable Drawable placeholder) {}

        @Override
        public void onLoadFailed(@Nullable Drawable errorDrawable) {}

        @Override
        public void onLoadCleared(@Nullable Drawable placeholder) {}

        @Override
        public void getSize(@NonNull SizeReadyCallback cb) {}

        @Override
        public void removeCallback(@NonNull SizeReadyCallback cb) {}

        @Override
        public void setRequest(@Nullable Request request) {}

        @Nullable
        @Override
        public Request getRequest() {
            return request;
        }

        @Override
        public void onStart() {}

        @Override
        public void onStop() {}

        @Override
        public void onDestroy() {}
    }
}
