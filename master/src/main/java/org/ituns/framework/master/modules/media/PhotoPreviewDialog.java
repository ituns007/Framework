package org.ituns.framework.master.modules.media;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;

import org.ituns.framework.master.R;
import org.ituns.framework.master.mvvm.action.MVVMAction;
import org.ituns.framework.master.tools.android.IClick;

import java.io.File;

public class PhotoPreviewDialog extends DialogFragment {
    private AppCompatImageView imageView;
    private final MutableLiveData<MVVMAction> liveData = new MutableLiveData<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.PhotoPreviewDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fw_dialog_photo_preview, container, false);
        imageView = contentView.findViewById(R.id.image);
        return contentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        liveData.observe(getViewLifecycleOwner(), actionObserver);
        IClick.single(imageView, v -> dismiss());
    }

    @Override
    public void onDestroyView() {
        liveData.removeObservers(getViewLifecycleOwner());
        super.onDestroyView();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        liveData.postValue(MVVMAction.with(0));
        super.onDismiss(dialog);
    }

    private final Observer<MVVMAction> actionObserver = action -> {
        if(action.code() == 1) {
            Uri uri = (Uri) action.getParcelable("uri");
            Glide.with(PhotoPreviewDialog.this).load(uri).into(imageView);
        } else if(action.code() == 2) {
            Object model = action.getObject("model");
            Glide.with(PhotoPreviewDialog.this).load(model).into(imageView);
        } else if(action.code() == 3) {
            String base64 = action.getString("base64");
            byte[] data = Base64.decode(base64, Base64.DEFAULT);
            Glide.with(PhotoPreviewDialog.this).load(data).into(imageView);
        } else if(action.code() == 4) {
            Bitmap bitmap = (Bitmap) action.getParcelable("bitmap");
            Glide.with(PhotoPreviewDialog.this).load(bitmap).into(imageView);
        } else if(action.code() == 5) {
            Drawable drawable = (Drawable) action.getObject("drawable");
            Glide.with(PhotoPreviewDialog.this).load(drawable).into(imageView);
        }
    };

    public static void showPath(FragmentManager manager, String path) {
        showFile(manager, new File(path));
    }

    public static void showFile(FragmentManager manager, File file) {
        showUri(manager, Uri.fromFile(file));
    }

    public static void showUri(FragmentManager manager, Uri uri) {
        PhotoPreviewDialog dialog = new PhotoPreviewDialog();
        dialog.liveData.postValue(MVVMAction.with(1).put("uri", uri));
        dialog.show(manager, "preview");
    }

    public static void showModel(FragmentManager manager, Object model) {
        PhotoPreviewDialog dialog = new PhotoPreviewDialog();
        dialog.liveData.postValue(MVVMAction.with(2).put("model", model));
        dialog.show(manager, "preview");
    }

    public static void showBase64(FragmentManager manager, String base64) {
        PhotoPreviewDialog dialog = new PhotoPreviewDialog();
        dialog.liveData.postValue(MVVMAction.with(3).put("base64", base64));
        dialog.show(manager, "preview");
    }

    public static void showBitmap(FragmentManager manager, Bitmap bitmap) {
        PhotoPreviewDialog dialog = new PhotoPreviewDialog();
        dialog.liveData.postValue(MVVMAction.with(4).put("bitmap", bitmap));
        dialog.show(manager, "preview");
    }

    public static void showDrawable(FragmentManager manager, Drawable drawable) {
        PhotoPreviewDialog dialog = new PhotoPreviewDialog();
        dialog.liveData.postValue(MVVMAction.with(5).put("drawable", drawable));
        dialog.show(manager, "preview");
    }
}