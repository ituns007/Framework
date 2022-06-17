package org.ituns.framework.master.widgets.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.Image;
import android.util.AttributeSet;
import android.util.Rational;
import android.util.Size;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCaseGroup;
import androidx.camera.core.ViewPort;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.framework.master.tools.android.IScreen;
import org.ituns.framework.master.tools.media.IImage;

import java.io.ByteArrayOutputStream;

public abstract class CameraAnalysisView extends FrameLayout implements ImageAnalysis.Analyzer {
    private Camera camera;
    private final PreviewView previewView;
    private final ExecutorHolder executorHolder;
    private final CameraConfiguration cameraConfiguration;
    private final ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    public CameraAnalysisView(@NonNull Context context) {
        this(context, null);
    }

    public CameraAnalysisView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraAnalysisView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        previewView = new PreviewView(context);
        cameraConfiguration = new CameraConfiguration.Builder().build();
        executorHolder = new ExecutorHolder();
        cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        initializeView(context);
    }

    private void initializeView(Context context) {
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(previewView, params);
    }

    @Override
    @CallSuper
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        executorHolder.shutdown();
    }

    public void enableFlash(boolean flash) {
        try {
            camera.getCameraControl().enableTorch(flash);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public final void startPreview(LifecycleOwner owner) {
        startPreview(owner, new CameraConfiguration.Builder().build());
    }

    public final void startPreview(LifecycleOwner owner, CameraConfiguration config) {
        cameraConfiguration.reset(config);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider provider = cameraProviderFuture.get();
                post(() -> startPreview(owner, provider));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    private void startPreview(LifecycleOwner owner, ProcessCameraProvider provider) {
        try {
            //旋转方向
            int targetRotation = getDisplay().getRotation();
            Logcat.e("Rotation:" + targetRotation);

            //构建剪裁尺寸
            int viewWidth = getWidth();
            int viewHeight = getHeight();
            Rational rational = new Rational(viewWidth, viewHeight);
            Logcat.e("ViewWidth:" + viewWidth + ", ViewHeight:" + viewHeight);

            //构建图像尺寸
            int screenWidth = (int) IScreen.width(getContext());
            int screenHeight = (int) IScreen.height(getContext());
            Size targetResolution = new Size(screenWidth, screenHeight);
            Logcat.e("ScreenWidth:" + screenWidth + ", ScreenHeight:" + screenHeight);

            //初始化相机选择器
            CameraSelector cameraSelector = new CameraSelector.Builder()
                    .requireLensFacing(cameraConfiguration.lensFacing())
                    .build();

            //初始化剪裁区域
            ViewPort viewPort = new ViewPort.Builder(rational, targetRotation)
                    .setScaleType(ViewPort.FILL_CENTER)
                    .build();

            //初始化预览
            Preview preview = new Preview.Builder()
                    .setTargetResolution(targetResolution)
                    .setTargetRotation(targetRotation)
                    .build();

            //初始化图像分析
            ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setTargetResolution(targetResolution)
                    .setTargetRotation(targetRotation)
                    .build();
            imageAnalysis.setAnalyzer(executorHolder.get(), this);

            //初始化用例组
            UseCaseGroup useCaseGroup = new UseCaseGroup.Builder()
                    .setViewPort(viewPort)
                    .addUseCase(preview)
                    .addUseCase(imageAnalysis)
                    .build();

            provider.unbindAll();
            camera = provider.bindToLifecycle(owner, cameraSelector, useCaseGroup);
            enableFlash(cameraConfiguration.flashMode() == ImageCapture.FLASH_MODE_ON);
            preview.setSurfaceProvider(previewView.getSurfaceProvider());
            onStartPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onStartPreview() {};

    public final void stopPreview() {
        try {
            camera = null;
            ProcessCameraProvider provider = cameraProviderFuture.get();
            provider.unbindAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            onStopPreview();
        }
    }

    protected void onStopPreview() {};

    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        try {
            Image image = imageProxy.getImage();
            if(image == null) {
                Logcat.e("image is null.");
                return;
            }

            byte[] bytes = IImage.jpeg(image);
            if(bytes == null) {
                Logcat.e("bytes is null.");
                return;
            }

            //剪裁区域
            Rect rect = imageProxy.getCropRect();
            Logcat.e("Image Rect:" + rect);

            //图像旋转方向
            int rotation = imageProxy.getImageInfo().getRotationDegrees();
            Logcat.e("Image Rotation:" + rotation);

            //构建输入图像
            Bitmap inputImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Logcat.e("Input Image Width:" + inputImage.getWidth());
            Logcat.e("Input Image Height:" + inputImage.getHeight());

            //输入图像转换矩阵
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            if(cameraConfiguration.lensFacing() == CameraSelector.LENS_FACING_FRONT) {
                matrix.postScale(-1, 1);
            }

            //构建输出图像
            Bitmap outputImage = Bitmap.createBitmap(inputImage, rect.left, rect.top,
                    rect.width(), rect.height(), matrix, false);
            Logcat.e("Output Image Width:" + outputImage.getWidth());
            Logcat.e("Output Image Height:" + outputImage.getHeight());

            //发送输出图像
            onImageData(outputImage);

            //回收Bitmap
            if(!inputImage.isRecycled()) {
                inputImage.recycle();
            }

            image.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            imageProxy.close();
        }
    }

    protected void onImageData(Bitmap bitmap) {
        //Bitmap -> bytes，JPEG格式
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        //发送图像数据
        onImageData(stream.toByteArray());

        //回收Bitmap
        if(!bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    protected void onImageData(byte[] bytes) {}
}
