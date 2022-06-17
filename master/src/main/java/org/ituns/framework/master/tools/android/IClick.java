package org.ituns.framework.master.tools.android;

import android.view.View;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class IClick {

    public static void single(View view, View.OnClickListener listener) {
        if(view == null || listener == null) {
            return;
        }
        view.setOnClickListener(new ClickListenerProxy(listener).proxy());
    }

    private static class ClickListenerProxy implements InvocationHandler {
        private final View.OnClickListener listener;

        private long lastClickTime = 0L;

        public ClickListenerProxy(View.OnClickListener listener) {
            this.listener = listener;
        }

        View.OnClickListener proxy() {
            return (View.OnClickListener) Proxy.newProxyInstance(
                    listener.getClass().getClassLoader(),
                    listener.getClass().getInterfaces(),
                    this
            );
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result = null;
            long currentClickTime = System.currentTimeMillis();
            if(currentClickTime - lastClickTime > 300) {
                result = method.invoke(listener, args);
            }
            lastClickTime = currentClickTime;
            return result;
        }
    }
}
