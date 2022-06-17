package org.ituns.framework.master.modules.develop.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseLongArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;

import org.ituns.framework.master.R;
import org.ituns.framework.master.modules.develop.activity.DevelopActivity;
import org.ituns.framework.master.modules.develop.adapter.CleanAdapter;
import org.ituns.framework.master.modules.develop.viewitem.CleanItem;
import org.ituns.framework.master.mvvm.action.MVVMAction;
import org.ituns.framework.master.mvvm.fragment.MVVMFragment;
import org.ituns.framework.master.mvvm.viewitem.DividerItem;
import org.ituns.framework.master.mvvm.viewitem.MVVMItem;
import org.ituns.framework.master.tools.java.IString;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class CleanFragment extends MVVMFragment<MVVMItem> {
    private static final long KB = 1024;
    private static final long MB = 1024 * 1024;
    private static final long GB = 1024 * 1024 * 1024;

    private static final String ALL = "^\\S+/\\S+$";
    private static final String TEXT = "^text/\\S+$";
    private static final String IMAGE = "^image/\\S+$";
    private static final String AUDIO = "^audio/\\S+$";
    private static final String VIDEO = "^video/\\S+$";

    private CleanAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fw_fragement_clean, container, false);
        initializeTitle(rootView.findViewById(R.id.title));
        return rootView;
    }

    private void initializeTitle(LinearLayout titleLayout) {
        Activity activity = requireActivity();
        if(activity instanceof DevelopActivity) {
            ((DevelopActivity) activity).onTitleInit(titleLayout);
            ((DevelopActivity) activity).onTitleText("垃圾清理");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new CleanAdapter();
        injectAdapter(adapter);
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        refreshCleanView();
    }

    @Override
    protected void onAdapter(MVVMAction action) {
        super.onAdapter(action);
        switch (action.getInt("category", 0)) {
            case 1: {
                File file = requireContext().getCacheDir();
                String desc = action.getString("desc", "");
                String type = action.getString("type", "");
                showDeleteDialog(file, desc, type);
                break;
            }
            case 2: {
                File file = requireContext().getExternalCacheDir();
                String desc = action.getString("desc", "");
                String type = action.getString("type", "");
                showDeleteDialog(file, desc, type);
                break;
            }
        }
    }

    private void showDeleteDialog(File file, String desc, String type) {
        MessageDialog.build((AppCompatActivity) requireActivity())
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setMessage("删除数据：" + desc + "？")
                .setCancelButton("取消")
                .setOkButton("确定", (dialog, v) -> {
                    deleteCacheFile(file, type);
                    refreshCleanView();
                    return false;
                })
                .show();
    }

    private void refreshCleanView() {
        adapter.setData(buildCacheData());
    }

    private ArrayList<MVVMItem> buildCacheData() {
        ArrayList<MVVMItem> items = new ArrayList<>();
        items.addAll(buildInternalCache());
        items.addAll(buildExternalCache());
        return items;
    }

    private ArrayList<MVVMItem> buildInternalCache() {
        int color = Color.parseColor("#E5E5E5");
        SparseLongArray sparse = getCacheSize(requireContext().getCacheDir());
        ArrayList<MVVMItem> items = new ArrayList<>();
        if(sparse.get(6) > 0) {
            items.add(DividerItem.divide(0.5f, color));
            items.add(CleanItem.title("内部存储"));
            items.add(DividerItem.divide(0.5f, color));
            if(sparse.get(1) > 0) {
                items.add(CleanItem.cache("文本").put("category", 1)
                        .put("type", TEXT)
                        .put("desc", "内部存储-文本")
                        .put("size", getFileSize(sparse.get(1))));
                items.add(DividerItem.divide(0.5f, color, 15.0f));
            }
            if(sparse.get(2) > 0) {
                items.add(CleanItem.cache("图片").put("category", 1)
                        .put("type", IMAGE)
                        .put("desc", "内部存储-图片")
                        .put("size", getFileSize(sparse.get(2))));
                items.add(DividerItem.divide(0.5f, color, 15.0f));
            }
            if(sparse.get(3) > 0) {
                items.add(CleanItem.cache("音频").put("category", 1)
                        .put("type", AUDIO)
                        .put("desc", "内部存储-音频")
                        .put("size", getFileSize(sparse.get(3))));
                items.add(DividerItem.divide(0.5f, color, 15.0f));
            }
            if(sparse.get(4) > 0) {
                items.add(CleanItem.cache("视频").put("category", 1)
                        .put("type", VIDEO)
                        .put("desc", "内部存储-视频")
                        .put("size", getFileSize(sparse.get(4))));
                items.add(DividerItem.divide(0.5f, color, 15.0f));
            }
            if(sparse.get(5) > 0) {
                items.add(CleanItem.cache("其他").put("category", 0)
                        .put("size", getFileSize(sparse.get(5))));
                items.add(DividerItem.divide(0.5f, color, 15.0f));
            }
            items.add(CleanItem.cache("全部").put("category", 1)
                    .put("type", ALL)
                    .put("desc", "内部存储-全部")
                    .put("size", getFileSize(sparse.get(6))));
            items.add(DividerItem.divide(0.5f, color));
        }
        return items;
    }

    private ArrayList<MVVMItem> buildExternalCache() {
        int color = Color.parseColor("#E5E5E5");
        SparseLongArray sparse = getCacheSize(requireContext().getExternalCacheDir());
        ArrayList<MVVMItem> items = new ArrayList<>();
        if(sparse.get(6) > 0) {
            items.add(DividerItem.divide(0.5f, color));
            items.add(CleanItem.title("外部存储"));
            items.add(DividerItem.divide(0.5f, color));
            if(sparse.get(1) > 0) {
                items.add(CleanItem.cache("文本").put("category", 2)
                        .put("type", TEXT)
                        .put("desc", "外部存储-文本")
                        .put("size", getFileSize(sparse.get(1))));
                items.add(DividerItem.divide(0.5f, color, 15.0f));
            }
            if(sparse.get(2) > 0) {
                items.add(CleanItem.cache("图片").put("category", 2)
                        .put("type", IMAGE)
                        .put("desc", "外部存储-图片")
                        .put("size", getFileSize(sparse.get(2))));
                items.add(DividerItem.divide(0.5f, color, 15.0f));
            }
            if(sparse.get(3) > 0) {
                items.add(CleanItem.cache("音频").put("category", 2)
                        .put("type", AUDIO)
                        .put("desc", "外部存储-音频")
                        .put("size", getFileSize(sparse.get(3))));
                items.add(DividerItem.divide(0.5f, color, 15.0f));
            }
            if(sparse.get(4) > 0) {
                items.add(CleanItem.cache("视频").put("category", 2)
                        .put("type", VIDEO)
                        .put("desc", "外部存储-视频")
                        .put("size", getFileSize(sparse.get(4))));
                items.add(DividerItem.divide(0.5f, color, 15.0f));
            }
            if(sparse.get(5) > 0) {
                items.add(CleanItem.cache("其他").put("category", 0)
                        .put("size", getFileSize(sparse.get(5))));
                items.add(DividerItem.divide(0.5f, color, 15.0f));
            }
            items.add(CleanItem.cache("全部").put("category", 2)
                    .put("type", ALL)
                    .put("desc", "外部存储-全部")
                    .put("size", getFileSize(sparse.get(6))));
            items.add(DividerItem.divide(0.5f, color));
        }
        return items;
    }

    private SparseLongArray getCacheSize(File file) {
        SparseLongArray sparse = new SparseLongArray();
        if(file == null) {
            return sparse;
        }

        if(file.isFile()) {
            String mimeType = getMimeType(file);
            long length = file.length();
            if(mimeType.matches(TEXT)) {
                sparse.put(1, sparse.get(1) + length);
            } else if(mimeType.matches(IMAGE)) {
                sparse.put(2, sparse.get(2) + length);
            } else if(mimeType.matches(AUDIO)) {
                sparse.put(3, sparse.get(3) + length);
            } else if(mimeType.matches(VIDEO)) {
                sparse.put(4, sparse.get(4) + length);
            } else {
                sparse.put(5, sparse.get(5) + length);
            }
            sparse.put(6, sparse.get(6) + length);
            return sparse;
        }

        File[] files = file.listFiles();
        if(files == null) {
            return sparse;
        }

        for(File child : files) {
            SparseLongArray childArray = getCacheSize(child);
            sparse.put(1, sparse.get(1) + childArray.get(1));
            sparse.put(2, sparse.get(2) + childArray.get(2));
            sparse.put(3, sparse.get(3) + childArray.get(3));
            sparse.put(4, sparse.get(4) + childArray.get(4));
            sparse.put(5, sparse.get(5) + childArray.get(5));
            sparse.put(6, sparse.get(6) + childArray.get(6));
        }
        return sparse;
    }

    private void deleteCacheFile(File file, String type) {
        if(file == null) {
            return;
        }

        if(file.isFile()) {
            String mimeType = getMimeType(file);
            if(mimeType.matches(type)) {
                file.delete();
            }
        } else if(file.isDirectory()) {
            File[] files = file.listFiles();
            if(files != null) {
                for(File child : files) {
                    deleteCacheFile(child, type);
                }
            }
        }
    }

    private String getMimeType(File file) {
        if(file != null) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if(IString.notEmpty(mimeType) && mimeType.matches(ALL)) {
                return mimeType;
            }
        }
        return "*/*";
    }

    private String getFileSize(long size) {
        if(size >= GB) {
            return String.format(Locale.getDefault(),"%.2fGB", (float) size / GB);
        } else if(size >= MB) {
            return String.format(Locale.getDefault(),"%.2fMB", (float) size / MB);
        } else if(size >= KB) {
            return String.format(Locale.getDefault(),"%.2fKB", (float) size / KB);
        } else {
            return String.format(Locale.getDefault(),"%dB", size);
        }
    }
}
