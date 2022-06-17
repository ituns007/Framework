package org.ituns.framework.master.tools.network;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import org.ituns.framework.master.tools.storage.IStorage;
import org.ituns.framework.master.tools.storage.IStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class IUri {
    private Uri uri;

    private IUri(Uri uri) { this.uri = uri; }

    public static IUri with(Context context, File file) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues values = new ContentValues();
            ContentResolver resolver = context.getContentResolver();
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return with(resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values));
            } else {
                return with(resolver.insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values));
            }
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authorith = context.getPackageName() + ".fileprovider";
            return with(FileProvider.getUriForFile(context, authorith, file));
        } else {
            return with(Uri.fromFile(file));
        }
    }

    public static IUri with(Uri uri) {
        return new IUri(uri);
    }

    public Uri uri() {
        return uri;
    }

    public InputStream input(Context context) {
        try {
            String url = uri.toString();
            ContentResolver resolver = context.getContentResolver();
            if(url.startsWith("http") || url.startsWith("https")) {
                return new URL(url).openStream();
            } else if(url.startsWith("file:///android_asset/")) {
                String assetPath = url.substring("file:///android_asset/".length());
                return context.getAssets().open(assetPath);
            } else {
                return resolver.openInputStream(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public OutputStream output(Context context) {
        try {
            return context.getContentResolver().openOutputStream(uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取 Mime-Type
     * @param context
     * @param uri
     * @return
     */
    public static String getType(Context context, Uri uri) {
        String type = getContentType(context, uri);
        if(type == null) type = getNetworkType(context, uri);
        if(type == null) type = getDefaultType(context, uri);
        return type;
    }

    private static String getContentType(Context context, Uri uri) {
        try {
            return context.getContentResolver().getType(uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getNetworkType(Context context, Uri uri) {
        try {
            URL url = new URL(uri.toString());
            URLConnection connection = url.openConnection();
            return connection.getContentType();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getDefaultType(Context context, Uri uri) {
        try {
            String url = uri.toString();
            String extension = MimeTypeMap.getFileExtensionFromUrl(url);
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String path(Context context) {
        if(uri == null) return null;

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            if(isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {                       //ExternalStorageProvider
                    return parseExternalStorageDocument(uri);
                } else if (isDownloadsDocument(uri)) {                      //DownloadsProvider
                    return parseDownloadsDocument(context, uri);
                } else if (isMediaDocument(uri)) {                          //MediaProvider
                    return parseMediaDocument(context, uri);
                }
            } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())) {
                String path = contentDataColumn(context, uri, null, null);
                return TextUtils.isEmpty(path) ? pathByCopyFile(context, uri) : path;
            } else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            } else {
                return uri.getPath();
            }
        }

        if(ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())) {
            return pathByCopyFile(context, uri);
        } else if(ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        } else {
            return uri.getPath();
        }
    }

    private static boolean isDocumentUri(Context context, Uri uri) {
        try {
            return DocumentsContract.isDocumentUri(context, uri);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static String parseExternalStorageDocument(Uri uri) {
        String docId = DocumentsContract.getDocumentId(uri);
        String[] split = docId.split(":");
        if ("primary".equalsIgnoreCase(split[0])) {
            return Environment.getExternalStorageDirectory() + "/" + split[1];
        }
        return null;
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static String parseDownloadsDocument(Context context, Uri uri) {
        try {
            String id = DocumentsContract.getDocumentId(uri);
            if (id.startsWith("raw:")) {
                return id.replaceFirst("raw:", "");
            }

            Uri downloadUri = Uri.parse("content://downloads/public_downloads");
            Uri contentUri = ContentUris.withAppendedId(downloadUri, Long.valueOf(id));
            String path = contentDataColumn(context, contentUri, null, null);
            return TextUtils.isEmpty(path) ? pathByCopyFile(context, uri) : path;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static String parseMediaDocument(Context context, Uri uri) {
        try {
            String docId = DocumentsContract.getDocumentId(uri);
            String[] split = docId.split(":");
            Uri contentUri = null;
            if ("image".equals(split[0])) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(split[0])) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(split[0])) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
            String selection = MediaStore.Images.Media._ID + "=?";
            String[] selectionArgs = new String[]{split[1]};
            String path = contentDataColumn(context, contentUri, selection, selectionArgs);
            return TextUtils.isEmpty(path) ? pathByCopyFile(context, uri) : path;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过拷贝文件的方式获取路径
     *
     * @param context
     * @param uri
     * @return
     */
    private static String pathByCopyFile(Context context, Uri uri) {
        File cacheFile = createCacheFile(context, uri);
        if(cacheFile == null) {
            return null;
        }

        try {
            ContentResolver resolver = context.getContentResolver();
            InputStream is = resolver.openInputStream(uri);
            FileOutputStream fos = new FileOutputStream(cacheFile);
            IStream.output(fos).write(is);
            return cacheFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建缓存文件
     *
     * @param context
     * @param uri
     * @return
     */
    private static File createCacheFile(Context context, Uri uri) {
        String fileName = contentDisplayName(context, uri);
        if(fileName != null) {
            File dir = IStorage.cache(context).file("uri");
            dir.mkdirs();
            fileName = System.currentTimeMillis() + "-" + fileName;
            return new File(dir, fileName);
        }
        return null;
    }

    /**
     * 获取 Uri 的 DisplayName 字段
     *
     * @param context
     * @param uri
     * @return
     */
    private static String contentDisplayName(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            ContentResolver resolver = context.getContentResolver();
            cursor = resolver.query(uri, null, null,
                    null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                return cursor.getString(columnIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 获取 Uri 的 Data 字段
     *
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    private static String contentDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        try {
            String[] projection = { MediaStore.MediaColumns.DATA };
            ContentResolver resolver = context.getContentResolver();
            cursor = resolver.query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                return cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
