package org.ituns.framework.master;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import org.ituns.framework.master.tools.android.IContext;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.List;

public class ContextWrapper implements Initializer<ContextWrapper> {
    private static final ContextHolder HOLDER = new ContextHolder();

    public static Context get() {
        return HOLDER.getContext();
    }

    @NonNull
    @Override
    public ContextWrapper create(@NonNull Context context) {
        HOLDER.setContext(context);
        return this;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }

    private static class ContextHolder {
        private volatile SoftReference<Context> mContextReference;

        private Context getContext() {
            SoftReference<Context> reference = mContextReference;
            if(reference != null) {
                return reference.get();
            }
            return null;
        }

        private void setContext(Context context) {
            Context applicationContext = IContext.applicationContext(context);
            if(null == applicationContext) {
                return;
            }

            SoftReference<Context> reference = mContextReference;
            if(null != reference) {
                reference.clear();
            }
            mContextReference = new SoftReference<>(applicationContext);
        }
    }
}