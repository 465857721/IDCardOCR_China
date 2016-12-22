package com.kingsoft.idcardocr_china.idcardocr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.kingsoft.idcardocr_china.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class CameraActivity extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private TessBaseAPI baseApi;

    //    Camera.PreviewCallback mp = ;
    private CameraManager cameraManager;
    private boolean hasSurface;
    private String type;
    private Button btn_close, light, btn_resacn;
    private boolean toggleLight = false;
    private Handler mHandler;
    private TextView tv_lightstate, tv_input;
    private String sdPath;
    //    private ImageView iv_close;
//    private View ErrorView;
    private int times = 0;
    private Long opentime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        opentime = System.currentTimeMillis();
        sdPath = Environment.getExternalStorageDirectory() + "/ayy/";
        try {
            copyAssetFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseApi = new TessBaseAPI();

        baseApi.init(sdPath, "eng");
        baseApi.setVariable("tessedit_char_whitelist", "0123456789Xx");
//        baseApi.
        setContentView(R.layout.activity_camera);
        tv_lightstate = (TextView) findViewById(R.id.tv_openlight);
        mHandler = new Handler();
        initLayoutParams();
    }

    /**
     * 重置surface宽高比例为3:4，不重置的话图形会拉伸变形
     */
    private void initLayoutParams() {
//        ErrorView = findViewById(R.id.ll_cameraerrorview);


        btn_close = (Button) findViewById(R.id.btn_close);
        light = (Button) findViewById(R.id.light);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                onBackPressed();

            }
        });
        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long time = System.currentTimeMillis();// 摄像头 初始化 需要时间
                if (time - opentime > 2000) {
                    opentime = time;
                    if (!toggleLight) {
                        toggleLight = true;
                        tv_lightstate.setText("关闭闪关灯");
                        cameraManager.openLight();
                    } else {
                        toggleLight = false;
                        tv_lightstate.setText("打开闪关灯");
                        cameraManager.offLight();
                    }
                }
            }
        });
//        btn_resacn = (Button) findViewById(R.id.btn_rescan);
//        btn_resacn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                times = 0;
//                ErrorView.setVisibility(View.GONE);
//            }
//        });
//        tv_input = (TextView) findViewById(R.id.tv_inputbyself);
//        tv_input.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setResult(RESULT_CANCELED);
//                onBackPressed();
//            }
//        });
//        iv_close = (ImageView) findViewById(R.id.iv_closetips);
//        iv_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                times = 0;
//                ErrorView.setVisibility(View.GONE);
//
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 初始化camera
         */
        cameraManager = new CameraManager();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();

        if (hasSurface) {
            // activity在paused时但不会stopped,因此surface仍旧存在；
            // surfaceCreated()不会调用，因此在这里初始化camera
            initCamera(surfaceHolder);
        } else {
            // 重置callback，等待surfaceCreated()来初始化camera
            surfaceHolder.addCallback(this);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    /**
     * 初始camera
     *
     * @param surfaceHolder SurfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            // 打开Camera硬件设备
            cameraManager.openDriver(surfaceHolder, this);
            // 创建一个handler来打开预览，并抛出一个运行时异常
            cameraManager.startPreview(this);
        } catch (Exception ioe) {
            Log.d("zk", ioe.toString());

        }
    }

    @Override
    protected void onPause() {
        /**
         * 停止camera，是否资源操作
         */
        cameraManager.stopPreview();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    private boolean copyAssetFile() throws Exception {

        String dir = sdPath + "/tessdata";
        String filePath = sdPath + "/tessdata/eng.traineddata";
        File f = new File(dir);
        if (f.exists()) {
        } else {
            f.mkdirs();
        }
        File dataFile = new File(filePath);
        if (dataFile.exists()) {
            return true;// 文件存在
        } else {

            InputStream in = this.getAssets().open("eng.traineddata");

            File outFile = new File(filePath);
            if (outFile.exists()) {
                outFile.delete();
            }
            OutputStream out = new FileOutputStream(outFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }

        return false;
    }

    public String doOcr(Bitmap bitmap) {
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        baseApi.setImage(bitmap);
        String text = baseApi.getUTF8Text();
        baseApi.clear();
//        baseApi.end();
        return replaceErrorChar(text);
    }

    private String replaceErrorChar(String id) {

        id = id.replaceAll("[\\p{Punct}\\p{Space}]+", "");
        id = id.replace("i", "1");
        id = id.replace("I", "1");
        id = id.replace("l", "1");
        id = id.replace("\\", "1");
        id = id.replace("|", "1");
        id = id.replace("/", "1");
        id = id.replace("l", "1");
        id = id.replace("z", "2");
        id = id.replace("￠", "2");
        id = id.replace("ʒ", "3");
        id = id.replace("z", "2");
        id = id.replace("S", "5");
        id = id.replace("s", "5");
        id = id.replace("b", "6");
        id = id.replace("a", "8");
        id = id.replace("B", "8");
        id = id.replace("g", "9");
        id = id.replace("G", "0");
        id = id.replace("D", "0");
        id = id.replace("o", "0");
        id = id.replace("O", "0");
        id = id.replace("U", "0");


        return id;
    }

    @Override
    public void onBackPressed() {
        if (baseApi != null)
            baseApi.end();
        super.onBackPressed();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        ByteArrayOutputStream baos;
        byte[] rawImage;
        Bitmap bitmap;
        Camera.Size previewSize = camera.getParameters().getPreviewSize();//获取尺寸,格式转换的时候要用到
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        YuvImage yuvimage = new YuvImage(
                data,
                ImageFormat.NV21,
                previewSize.width,
                previewSize.height,
                null);
        baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, baos);// 80--JPG图片的质量[0-100],100最高
        rawImage = baos.toByteArray();
        //将rawImage转换成bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options);
        if (bitmap == null) {
            Log.d("zka", "bitmap is nlll");
            return;
        } else {
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            final Bitmap bitmap1 = Bitmap.createBitmap(bitmap, (width - height) / 2, height / 6, height, height * 2 / 3);
            int x, y, w, h;
            x = (int) (bitmap1.getWidth() * 0.340);
            y = (int) (bitmap1.getHeight() * 0.800);
            w = (int) (bitmap1.getWidth() * 0.6 + 0.5f);
            h = (int) (bitmap1.getHeight() * 0.12 + 0.5f);
            Bitmap bit_hm = Bitmap.createBitmap(bitmap1, x, y, w, h);
            String id = doOcr(bit_hm);
//            if (++times > 10)
//                ErrorView.setVisibility(View.VISIBLE);
            if (id.length() == 18) {
                Intent i = new Intent();
                i.putExtra("id", id);
                setResult(RESULT_OK, i);
                onBackPressed();
            }
        }

    }
}
