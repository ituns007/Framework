package org.ituns.framework.master.tools.media;

import android.content.Context;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;

import org.ituns.framework.master.tools.network.IUri;

public class IExif {

    public static int degree(Context context, Uri uri) {
        try {
            int degree = 0;
            ExifInterface exif = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                exif = new ExifInterface(IUri.with(uri).input(context));
            } else {
                exif = new ExifInterface(IUri.with(uri).path(context));
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
            return degree;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
