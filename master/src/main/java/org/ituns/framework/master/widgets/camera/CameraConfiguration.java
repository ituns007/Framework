package org.ituns.framework.master.widgets.camera;

import androidx.annotation.IntDef;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CameraConfiguration {
    private int flashMode;
    private int lensFacing;

    public CameraConfiguration(Builder builder) {
        this.flashMode = builder.flashMode;
        this.lensFacing = builder.lensFacing;
    }

    @FlashMode
    public int flashMode() {
        return flashMode;
    }

    @LensFacing
    public int lensFacing() {
        return lensFacing;
    }

    public void reset(CameraConfiguration config) {
        this.flashMode = config.flashMode;
        this.lensFacing = config.lensFacing;
    }

    public static class Builder {
        private int flashMode;
        private int lensFacing;

        public Builder() {
            flashMode = ImageCapture.FLASH_MODE_OFF;
            lensFacing = CameraSelector.LENS_FACING_BACK;
        }

        public Builder flashMode(@FlashMode int flashMode) {
            this.flashMode = flashMode;
            return this;
        }

        public Builder lensFacing(@LensFacing int lensFacing) {
            this.lensFacing = lensFacing;
            return this;
        }

        public CameraConfiguration build() {
            return new CameraConfiguration(this);
        }
    }

    @IntDef({ImageCapture.FLASH_MODE_AUTO, ImageCapture.FLASH_MODE_ON, ImageCapture.FLASH_MODE_OFF})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FlashMode {}

    @IntDef({CameraSelector.LENS_FACING_FRONT, CameraSelector.LENS_FACING_BACK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LensFacing {}
}