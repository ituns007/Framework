package org.ituns.framework.master.tools.androidx;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import org.ituns.framework.master.service.logcat.Logcat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

public class AlignLiveData<T> extends LiveData<T> {
    private static final String TAG = "AlignLiveData";

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        try {
            Class<?> observerWrapperClass = Class.forName("androidx.lifecycle.LiveData$ObserverWrapper");
            Class<?> lifecycleBoundObserverClass = Class.forName("androidx.lifecycle.LiveData$LifecycleBoundObserver");

            //取 LiveData 的 mVersion 字段
            Field versionField = getFiled(LiveData.class, "mVersion");
            Object versionObject = versionField.get(this);

            //反射 LifecycleBoundObserver 的构造方法，并创建一个对象实例
            Constructor<?> lifecycleBoundObserverConstructor = getConstructor(
                    lifecycleBoundObserverClass,
                    LiveData.class,
                    LifecycleOwner.class,
                    Observer.class);
            Object lifecycleBoundObserverObject = lifecycleBoundObserverConstructor.newInstance(this, owner, observer);

            //取 AlwaysActiveObserver 的 mLastVersion 字段，并赋值为 LiveData 的 mVersion 字段值
            Field lastVersionField = getFiled(observerWrapperClass, "mLastVersion");
            lastVersionField.set(lifecycleBoundObserverObject, versionObject);

            //取 LiveData 的 mObservers字段，并调用 putIfAbsent 方法
            Field observersField = getFiled(LiveData.class, "mObservers");
            Object observersObject = observersField.get(this);

            Method putIfAbsentMethod = getMethod(observersObject.getClass(), "putIfAbsent", Object.class, Object.class);
            Object existingObject = putIfAbsentMethod.invoke(observersObject, observer, lifecycleBoundObserverObject);

            //同一个 observer 不能添加到不同的 LifecycleOwner 上
            if(existingObject != null) {
                Method isAttachedToMethod = getMethod(observerWrapperClass, "isAttachedTo", LifecycleOwner.class);
                boolean isAttachedToObject = (boolean) isAttachedToMethod.invoke(existingObject, owner);
                if(!isAttachedToObject) {
                    throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
                }
            }

            //observer 已经添加过了，不需要做消息同步
            if(existingObject != null) {
                return;
            }

            owner.getLifecycle().addObserver((LifecycleObserver) lifecycleBoundObserverObject);
        } catch (Exception e) {
            Logcat.e(TAG, "observe exception:", e);
        }
    }

    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
        try {
            Class<?> observerWrapperClass = Class.forName("androidx.lifecycle.LiveData$ObserverWrapper");
            Class<?> alwaysActiveObserverClass = Class.forName("androidx.lifecycle.LiveData$AlwaysActiveObserver");
            Class<?> lifecycleBoundObserverClass = Class.forName("androidx.lifecycle.LiveData$LifecycleBoundObserver");

            //取 LiveData 的 mVersion 字段
            Field versionField = getFiled(LiveData.class, "mVersion");
            Object versionObject = versionField.get(this);

            //反射 AlwaysActiveObserver 的构造方法，并创建一个对象实例
            Constructor<?> alwaysActiveObserverConstructor = getConstructor(alwaysActiveObserverClass, LiveData.class, Observer.class);
            Object alwaysActiveObserverObject = alwaysActiveObserverConstructor.newInstance(this, observer);

            //取 AlwaysActiveObserver 的 mLastVersion 字段，并赋值为 LiveData 的 mVersion 字段值
            Field lastVersionField = getFiled(observerWrapperClass, "mLastVersion");
            lastVersionField.set(alwaysActiveObserverObject, versionObject);

            //取 LiveData 的 mObservers字段，并调用 putIfAbsent 方法
            Field observersField = getFiled(LiveData.class, "mObservers");
            Object observersObject = observersField.get(this);

            Method putIfAbsentMethod = getMethod(observersObject.getClass(), "putIfAbsent", Object.class, Object.class);
            Object existingObject = putIfAbsentMethod.invoke(observersObject, observer, alwaysActiveObserverObject);

            // LifecycleBoundObserver 对象，串种了
            if(lifecycleBoundObserverClass.isInstance(existingObject)) {
                throw new IllegalArgumentException("Cannot add the same observer"
                        + " with different lifecycles");
            }

            //observer 已经添加过了，不需要做消息同步
            if(existingObject != null) {
                return;
            }

            //调用 AlwaysActiveObserver 的 activeStateChanged 方法，同步最新消息给 observer
            Method method = getMethod(observerWrapperClass, "activeStateChanged", boolean.class);
            method.invoke(alwaysActiveObserverObject, true);
        } catch (Exception e) {
            Logcat.e(TAG, "observe forever exception:", e);
        }
    }

    @Override
    public void postValue(T value) {
        super.postValue(value);
    }

    @Override
    public void setValue(T value) {
        super.setValue(value);
        alignAllVersions();
    }

    private void alignAllVersions() {
        try {
            Class<?> observerWrapperClass = Class.forName("androidx.lifecycle.LiveData$ObserverWrapper");

            //取 LiveData 的 mVersion 字段
            Field versionField = getFiled(LiveData.class, "mVersion");
            Object versionObject = versionField.get(this);

            //取 LiveData 的 mObservers字段
            Field observersField = getFiled(LiveData.class, "mObservers");
            Object observersObject = observersField.get(this);

            //调用 mObservers 的 iteratorWithAdditions 方法，遍历取出所有的 ObserverWrapper
            Method iteratorMethod = getMethod(observersObject.getClass(), "iteratorWithAdditions");
            Iterator<Map.Entry> iterator = (Iterator<Map.Entry>) iteratorMethod.invoke(observersObject);
            while (iterator.hasNext()) {
                //取 ObserverWrapper 的 mLastVersion 字段，并赋值为 LiveData 的 mVersion 字段值
                Object observerWrapperObject = iterator.next().getValue();
                Field lastVersionField = getFiled(observerWrapperClass, "mLastVersion");
                lastVersionField.set(observerWrapperObject, versionObject);
            }
        } catch (Exception e) {
            Logcat.e(TAG, "align all versions exception:", e);
        }
    }

    private Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructor(parameterTypes);
        constructor.setAccessible(true);
        return constructor;
    }

    private Field getFiled(Class<?> clazz, String fieldName) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    private Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws Exception {
        Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method;
    }
}
