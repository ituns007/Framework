package org.ituns.framework.master.tools.media;

import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;

import org.ituns.framework.master.service.logcat.Logcat;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class IImage {

    public static byte[] jpeg(Image image) {
        try {
            if(image.getFormat() == ImageFormat.JPEG) {
                return decodeJpeg(image);
            } else if(image.getFormat() == ImageFormat.YUV_420_888) {
                return decodeYuv420(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] decodeJpeg(Image image) {
        Image.Plane[] planes = image.getPlanes();
        if(planes.length <= 0) {
            return null;
        }

        ByteBuffer buffer = planes[0].getBuffer();
        if(buffer == null) {
            return null;
        }

        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        buffer.clear();
        return bytes;
    }

    private static byte[] decodeYuv420(Image image) {
        Rect crop = image.getCropRect();
        int format = image.getFormat();
        int width = crop.width();
        int height = crop.height();
        Image.Plane[] planes = image.getPlanes();
        byte[] rowData = new byte[planes[0].getRowStride()];
        byte[] nv21Data = new byte[width * height * ImageFormat.getBitsPerPixel(format) / 8];

        int channelOffset = 0;
        int outputStride = 1;
        for (int i = 0; i < planes.length; i++) {
            switch (i) {
                case 0:
                    channelOffset = 0;
                    outputStride = 1;
                    break;
                case 1:
                    channelOffset = width * height + 1;
                    outputStride = 2;
                    break;
                case 2:
                    channelOffset = width * height;
                    outputStride = 2;
                    break;
            }

            ByteBuffer buffer = planes[i].getBuffer();
            int rowStride = planes[i].getRowStride();
            int pixelStride = planes[i].getPixelStride();

            int shift = (i == 0) ? 0 : 1;
            int w = width >> shift;
            int h = height >> shift;
            buffer.position(rowStride * (crop.top >> shift) + pixelStride * (crop.left >> shift));
            for (int row = 0; row < h; row++) {
                int length;
                if (pixelStride == 1 && outputStride == 1) {
                    length = w;
                    buffer.get(nv21Data, channelOffset, length);
                    channelOffset += length;
                } else {
                    length = (w - 1) * pixelStride + 1;
                    buffer.get(rowData, 0, length);
                    for (int col = 0; col < w; col++) {
                        nv21Data[channelOffset] = rowData[col * pixelStride];
                        channelOffset += outputStride;
                    }
                }
                if (row < h - 1) {
                    buffer.position(buffer.position() + rowStride - length);
                }
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YuvImage yuv = new YuvImage(nv21Data, ImageFormat.NV21, width, height, null);
        yuv.compressToJpeg(new Rect(0, 0, width, height), 100, out);
        return out.toByteArray();
    }
}
