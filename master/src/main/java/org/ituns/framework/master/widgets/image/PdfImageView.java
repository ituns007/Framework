package org.ituns.framework.master.widgets.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.ituns.framework.master.R;
import org.ituns.framework.master.modules.media.PhotoPreviewDialog;
import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.framework.master.tools.storage.IStorage;
import org.ituns.framework.master.tools.storage.IStream;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PdfImageView extends FrameLayout {
    private static final int MODE_PAGER = 0;
    private static final int MODE_SCROLL = 1;

    private static final int ORIENTATION_VERTICAL = 0;
    private static final int ORIENTATION_HORIZONTAL = 1;

    private final File cache;
    private final PdfAdapter adapter;
    private final RecyclerView recyclerView;
    private final LinearLayoutManager layoutManager;

    public PdfImageView(@NonNull Context context) {
        this(context, null);
    }

    public PdfImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        cache = IStorage.cache(context).file(System.currentTimeMillis() + ".pdf");
        adapter = new PdfAdapter();
        recyclerView = new RecyclerView(context);
        layoutManager = new LinearLayoutManager(context);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(recyclerView, params);

        int mode = MODE_PAGER;
        int orientation = ORIENTATION_VERTICAL;
        if(attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PdfImageView, 0, 0);
            for(int i = 0; i < ta.getIndexCount(); i++) {
                int attr = ta.getIndex(i);
                if(attr == R.styleable.PdfImageView_mode) {
                    mode = ta.getInt(R.styleable.PdfImageView_mode, 0);
                } else if(attr == R.styleable.PdfImageView_orientation) {
                    orientation = ta.getInt(R.styleable.PdfImageView_orientation, 0);
                }
            }
            ta.recycle();
        }
        setMode(mode);
        setOrientation(orientation);
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(cache.exists()) {
            cache.delete();
        }
    }

    private void setMode(int mode) {
        if(mode == MODE_PAGER) {
            PagerSnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
        }
    }

    public void setOrientation(@Orientation int orientation) {
        if(orientation == ORIENTATION_VERTICAL) {
            layoutManager.setOrientation(RecyclerView.VERTICAL);
        } else if(orientation == ORIENTATION_HORIZONTAL) {
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        }
    }

    public void showData(byte[] data) {
        try {
            IStream.output(new FileOutputStream(cache)).write(data);
            showFile(cache);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showFile(File file) {
        try {
            ParcelFileDescriptor descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            adapter.setRenderer(new PdfRenderer(descriptor));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class PdfAdapter extends RecyclerView.Adapter<PdfHolder> {
        private PdfRenderer renderer;

        public void setRenderer(PdfRenderer renderer) {
            this.renderer = renderer;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public PdfHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            PdfView pdfView = new PdfView(getContext());
            pdfView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            return new PdfHolder(pdfView);
        }

        @Override
        public void onBindViewHolder(@NonNull PdfHolder holder, int position) {
            if(renderer != null) {
                PdfRenderer.Page page = renderer.openPage(position);
                Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                holder.setBitmap(bitmap);
                page.close();
            }
        }

        @Override
        public int getItemCount() {
            if(renderer != null) {
                return renderer.getPageCount();
            }
            return 0;
        }
    }

    public static class PdfHolder extends RecyclerView.ViewHolder {
        private final AppCompatImageView imageView;

        public PdfHolder(@NonNull AppCompatImageView imageView) {
            super(imageView);
            this.imageView = imageView;
        }

        public void setBitmap(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }

    public static class PdfView extends AppCompatImageView {

        public PdfView(@NonNull Context context) {
            this(context, null);
        }

        public PdfView(@NonNull Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public PdfView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }
    }

    @IntDef({ORIENTATION_VERTICAL, ORIENTATION_HORIZONTAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {}
}