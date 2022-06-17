package org.ituns.framework.master.tools.android;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.framework.master.tools.java.IClass;
import org.ituns.framework.master.tools.java.IString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IBundle {
    private static final String TAG = "IBundle";

    private final Gson gson = new Gson();
    private final ArrayMap<String, Object> mMap;

    public IBundle() {
        this(0);
    }

    public IBundle(int capacity) {
        mMap = capacity > 0 ? new ArrayMap<>(capacity) : new ArrayMap<>();
    }

    public static IBundle with(Object object) {
        return new IBundle().inject(object);
    }

    public static IBundle with(Bundle bundle) {
        return new IBundle().putAll(bundle);
    }

    public static IBundle with(IBundle bundle) {
        return new IBundle().putAll(bundle);
    }

    public static IBundle with(Map<String, Object> map) {
        return new IBundle().putAll(map);
    }

    public static IBundle with(@Nullable String key, @Nullable Object value) {
        return new IBundle().put(key, value);
    }

    public <T extends IBundle> T inject(Object data) {
        return inject(data, false, false);
    }

    public <T extends IBundle> T inject(Object data, boolean withEmpty, boolean withNull) {
        try {
            JsonElement json = gson.toJsonTree(data);
            if(json.isJsonObject()) {
                JsonObject object = json.getAsJsonObject();
                for(String key : object.keySet()) {
                    injectElement(key, object.get(key), withEmpty, withNull);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) this;
    }

    private void injectElement(String key, JsonElement element, boolean withEmpty, boolean withNull) {
        if(element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if(primitive.isString()) {
                String value = primitive.getAsString();
                if(IString.notEmpty(value) || withEmpty) {
                    mMap.put(key, value);
                }
            } else if(primitive.isBoolean()) {
                mMap.put(key, primitive.getAsBoolean());
            } else if(primitive.isNumber()) {
                Number number = primitive.getAsNumber();
                if(number instanceof Byte) {
                    mMap.put(key, number.byteValue());
                } else if(number instanceof Short) {
                    mMap.put(key, number.shortValue());
                } else if(number instanceof Integer) {
                    mMap.put(key, number.intValue());
                } else if(number instanceof Long) {
                    mMap.put(key, number.longValue());
                } else if(number instanceof Float) {
                    mMap.put(key, number.floatValue());
                } else if(number instanceof Double) {
                    mMap.put(key, number.doubleValue());
                }
            }
        } else if(element.isJsonNull() && withNull) {
            mMap.put(key, null);
        }
    }

    public <T extends IBundle> T putAll(Bundle bundle) {
        if(bundle != null) {
            for(String key : bundle.keySet()) {
                put(key, bundle.get(key));
            }
        }
        return (T) this;
    }

    public <T extends IBundle> T putAll(IBundle bundle) {
        if(bundle != null) {
            mMap.putAll(bundle.mMap);
        }
        return (T) this;
    }

    public <T extends IBundle> T putAll(Map<String, Object> map) {
        if(map != null) {
            mMap.putAll(map);
        }
        return (T) this;
    }

    public <T extends IBundle> T put(@Nullable String key, @Nullable Object value) {
        mMap.put(key, value);
        return (T) this;
    }

    public Set<String> keySet() {
        return mMap.keySet();
    }

    public boolean containsKey(String key) {
        return mMap.containsKey(key);
    }

    public int size() {
        return mMap.size();
    }

    public boolean isEmpty() {
        return mMap.isEmpty();
    }

    public <V> V remove(String key) {
        Object value = mMap.remove(key);
        if(value != null) {
            return (V) value;
        }
        return null;
    }

    public void clear() {
        mMap.clear();
    }

    @Nullable
    public Object getObject(String key) {
        return mMap.get(key);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Boolean) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Boolean", defaultValue, e);
            return defaultValue;
        }
    }

    public byte getByte(String key) {
        return getByte(key, (byte) 0);
    }

    public Byte getByte(String key, byte defaultValue) {
        Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Byte) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Byte", defaultValue, e);
            return defaultValue;
        }
    }

    public char getChar(String key) {
        return getChar(key, (char) 0);
    }

    public char getChar(String key, char defaultValue) {
        Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Character) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Character", defaultValue, e);
            return defaultValue;
        }
    }

    public short getShort(String key) {
        return getShort(key, (short) 0);
    }

    public short getShort(String key, short defaultValue) {
        Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Short) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Short", defaultValue, e);
            return defaultValue;
        }
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Integer) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Integer", defaultValue, e);
            return defaultValue;
        }
    }

    public long getLong(String key) {
        return getLong(key, 0L);
    }

    public long getLong(String key, long defaultValue) {
        Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Long) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Long", defaultValue, e);
            return defaultValue;
        }
    }

    public float getFloat(String key) {
        return getFloat(key, 0.0f);
    }

    public float getFloat(String key, float defaultValue) {
        Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Float) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Float", defaultValue, e);
            return defaultValue;
        }
    }

    public double getDouble(String key) {
        return getDouble(key, 0.0);
    }

    public double getDouble(String key, double defaultValue) {
        Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Double) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Double", defaultValue, e);
            return defaultValue;
        }
    }

    @Nullable
    public String getString(@Nullable String key) {
        final Object o = mMap.get(key);
        try {
            return (String) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "String", e);
            return null;
        }
    }

    public String getString(@Nullable String key, String defaultValue) {
        final String s = getString(key);
        return (s == null) ? defaultValue : s;
    }

    @Nullable
    public CharSequence getCharSequence(@Nullable String key) {
        final Object o = mMap.get(key);
        try {
            return (CharSequence) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "CharSequence", e);
            return null;
        }
    }

    public CharSequence getCharSequence(@Nullable String key, CharSequence defaultValue) {
        final CharSequence cs = getCharSequence(key);
        return (cs == null) ? defaultValue : cs;
    }

    @Nullable
    public <T extends Serializable> T getSerializable(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (T) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Serializable", e);
            return null;
        }
    }

    @Nullable
    public <T extends Parcelable> T getParcelable(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (T) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Parcelable", e);
            return null;
        }
    }

    @Nullable
    public boolean[] getBooleanArray(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (boolean[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "byte[]", e);
            return null;
        }
    }

    @Nullable
    public byte[] getByteArray(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (byte[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "byte[]", e);
            return null;
        }
    }

    @Nullable
    public char[] getCharArray(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (char[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "char[]", e);
            return null;
        }
    }

    @Nullable
    public short[] getShortArray(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (short[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "short[]", e);
            return null;
        }
    }

    @Nullable
    public int[] getIntArray(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (int[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "int[]", e);
            return null;
        }
    }

    @Nullable
    public long[] getLongArray(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (long[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "long[]", e);
            return null;
        }
    }

    @Nullable
    public float[] getFloatArray(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (float[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "float[]", e);
            return null;
        }
    }

    @Nullable
    public double[] getDoubleArray(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (double[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "double[]", e);
            return null;
        }
    }

    @Nullable
    public String[] getStringArray(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (String[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "String[]", e);
            return null;
        }
    }

    @Nullable
    public CharSequence[] getCharSequenceArray(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (CharSequence[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "CharSequence[]", e);
            return null;
        }
    }

    @Nullable
    public Parcelable[] getParcelableArray(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (Parcelable[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Parcelable[]", e);
            return null;
        }
    }

    @Nullable
    public Serializable[] getSerializableArray(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (Serializable[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Parcelable[]", e);
            return null;
        }
    }

    @Nullable
    public ArrayList<Integer> getIntegerArrayList(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (ArrayList<Integer>) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "ArrayList<Integer>", e);
            return null;
        }
    }

    @Nullable
    public ArrayList<String> getStringArrayList(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (ArrayList<String>) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "ArrayList<String>", e);
            return null;
        }
    }

    @Nullable
    public ArrayList<CharSequence> getCharSequenceArrayList(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (ArrayList<CharSequence>) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "ArrayList<CharSequence>", e);
            return null;
        }
    }

    @Nullable
    public <T extends Parcelable> ArrayList<T> getParcelableArrayList(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (ArrayList<T>) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "ArrayList", e);
            return null;
        }
    }
    @Nullable
    public <T extends Serializable> ArrayList<T> getSerializableArrayList(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (ArrayList<T>) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "ArrayList", e);
            return null;
        }
    }

    @Nullable
    public <T extends Parcelable> SparseArray<T> getSparseParcelableArray(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (SparseArray<T>) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "SparseArray", e);
            return null;
        }
    }
    @Nullable
    public <T extends Serializable> SparseArray<T> getSparseSerializableArray(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (SparseArray<T>) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "SparseArray", e);
            return null;
        }
    }

    @Nullable
    public Bundle getBundle(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (Bundle) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Bundle", e);
            return null;
        }
    }

    @Nullable
    public IBundle getIBundle(@Nullable String key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (IBundle) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Bundle", e);
            return null;
        }
    }

    public Map<String, Object> map() {
        return new ArrayMap<>(mMap);
    }

    public Bundle bundle() {
        Bundle bundle = new Bundle();
        for(String key : mMap.keySet()) {
            putBasicToBundle(bundle, key, mMap.get(key));
        }
        return bundle;
    }

    private void putBasicToBundle(Bundle bundle, String key, Object value) {
        if (value instanceof Byte) {
            bundle.putByte(key, (Byte) value);
        } else if (value instanceof Short) {
            bundle.putShort(key, (Short) value);
        } else if (value instanceof Boolean) {
            bundle.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            bundle.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            bundle.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            bundle.putFloat(key, (Float) value);
        } else if (value instanceof Double) {
            bundle.putDouble(key, (Double) value);
        } else if (value instanceof Character) {
            bundle.putChar(key, (Character) value);
        } else {
            putObjectToBundle(bundle, key, value);
        }
    }

    private void putObjectToBundle(Bundle bundle, String key, Object value) {
        if (value instanceof Size) {
            bundle.putSize(key, (Size) value);
        } else if (value instanceof SizeF) {
            bundle.putSizeF(key, (SizeF) value);
        } else if (value instanceof Binder) {
            bundle.putBinder(key, (Binder) value);
        } else if (value instanceof Bundle) {
            bundle.putBundle(key, (Bundle) value);
        } else if(value instanceof String) {
            bundle.putString(key, (String) value);
        } else if (value instanceof IBinder) {
            bundle.putBinder(key, (IBinder) value);
        } else if (value instanceof Parcelable) {
            bundle.putParcelable(key, (Parcelable) value);
        } else if (value instanceof CharSequence) {
            bundle.putCharSequence(key, (CharSequence) value);
        } else if (value instanceof Serializable) {
            bundle.putSerializable(key, (Serializable) value);
        } else {
            putArrayToBundle(bundle, key, value);
        }
    }

    private void putArrayToBundle(Bundle bundle, String key, Object value) {
        if (value instanceof int[]) {
            bundle.putIntArray(key, (int[]) value);
        } else if (value instanceof byte[]) {
            bundle.putByteArray(key, (byte[]) value);
        } else if (value instanceof char[]) {
            bundle.putCharArray(key, (char[]) value);
        } else if (value instanceof long[]) {
            bundle.putLongArray(key, (long[]) value);
        } else if (value instanceof short[]) {
            bundle.putShortArray(key, (short[]) value);
        } else if (value instanceof float[]) {
            bundle.putFloatArray(key, (float[]) value);
        } else if (value instanceof double[]) {
            bundle.putDoubleArray(key, (double[]) value);
        } else if (value instanceof boolean[]) {
            bundle.putBooleanArray(key, (boolean[]) value);
        } else if (value instanceof String[]) {
            bundle.putStringArray(key, (String[]) value);
        } else if (value instanceof Parcelable[]) {
            bundle.putParcelableArray(key, (Parcelable[]) value);
        } else if (value instanceof CharSequence[]) {
            bundle.putCharSequenceArray(key, (CharSequence[]) value);
        } else {
            putListToBundle(bundle, key, value);
        }
    }

    private void putListToBundle(Bundle bundle, String key, Object value) {
        if (value instanceof ArrayList) {
            putListToBundle(bundle, key, (ArrayList<?>) value);
        } else if(value instanceof List) {
            putListToBundle(bundle, key, new ArrayList<>((List<?>)value));
        } else {
            putSparseToBundle(bundle, key, value);
        }
    }

    private void putListToBundle(Bundle bundle, String key, ArrayList value) {
        if(IClass.isParameterType(value, Integer.class)) {
            bundle.putIntegerArrayList(key, value);
        } else if(IClass.isParameterType(value, String.class)) {
            bundle.putStringArrayList(key, value);
        } else if(IClass.isParameterType(value, CharSequence.class)) {
            bundle.putCharSequenceArrayList(key, value);
        } else if(IClass.isParameterType(value, Parcelable.class)) {
            bundle.putParcelableArrayList(key, value);
        } else {
            Logcat.e("Unsupported type:" + IClass.typeName(value.getClass()));
        }
    }

    private void putSparseToBundle(Bundle bundle, String key, Object value) {
        if(value instanceof SparseArray) {
            bundle.putSparseParcelableArray(key, (SparseArray) value);
        } else if(value != null) {
            Logcat.e("Unsupported type:" + IClass.typeName(value.getClass()));
        } else {
            Logcat.e("Unsupported value:null");
        }
    }

    // Log a message if the value was non-null but not of the expected type
    void typeWarning(String key, Object value, String className,
                     Object defaultValue, ClassCastException e) {
        StringBuilder sb = new StringBuilder();
        sb.append("Key ");
        sb.append(key);
        sb.append(" expected ");
        sb.append(className);
        sb.append(" but value was a ");
        sb.append(value.getClass().getName());
        sb.append(".  The default value ");
        sb.append(defaultValue);
        sb.append(" was returned.");
        Log.w(TAG, sb.toString());
        Log.w(TAG, "Attempt to cast generated internal exception:", e);
    }

    void typeWarning(String key, Object value, String className,
                     ClassCastException e) {
        typeWarning(key, value, className, "<null>", e);
    }
}