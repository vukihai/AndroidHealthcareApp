package duong.huy.huong.healthcare.HeartRateMonitor;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import duong.huy.huong.healthcare.R;
import duong.huy.huong.healthcare.db.Heart_Rate;
import duong.huy.huong.healthcare.db.Heart_RateDao;

public class HeartRateActivity extends Activity {

    private static final String TAG = "HeartRateMonitor";
    private static final AtomicBoolean processing = new AtomicBoolean(false);

    private static SurfaceView preview = null;
    private static SurfaceHolder previewHolder = null;
    private static Camera camera = null;

    private static View image = null;
    @SuppressLint("StaticFieldLeak")
    private static ImageView beatImg = null; // hinh trai tim
    @SuppressLint("StaticFieldLeak")
    private static TextView heartRate = null; // textview hien nhip tim
    @SuppressLint("StaticFieldLeak")
    private static TextView time = null; // textview hien thoi gian
    @SuppressLint("StaticFieldLeak")
    private static TextView bmpText = null; // textview hien thoi gian
    private static boolean onBeat = false;
    private static boolean started = false;
    private static boolean measuring = false;
    private static long lastBeatTime = 0;

    private static TextView text = null;

    private static WakeLock wakeLock = null;

    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];

    public static enum TYPE {
        GREEN, RED
    };

    private static TYPE currentType = TYPE.GREEN;

    public static TYPE getCurrent() {
        return currentType;
    }

    private static int beatsIndex = 0;
    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;

    /**
     * {@inheritDoc}
     */
    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);
        beatImg = (ImageView) findViewById(R.id.beat);
        heartRate = (TextView) findViewById(R.id.heart_rate);
        time = (TextView) findViewById(R.id.time);
        preview = (SurfaceView) findViewById(R.id.preview);
        bmpText = (TextView) findViewById(R.id.bmpText);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        image = findViewById(R.id.image);
        text = (TextView) findViewById(R.id.text);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();

        wakeLock.acquire();

        camera = Camera.open();

        startTime = System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();

        wakeLock.release();

        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    private static PreviewCallback previewCallback = new PreviewCallback() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPreviewFrame(byte[] data, Camera cam) {
            if(!measuring) return;
            if (data == null) throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) throw new NullPointerException();
            if (!processing.compareAndSet(false, true)) return;
            int width = size.width;
            int height = size.height;
            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height, width);
            if (imgAvg == 0 || imgAvg == 255) {
                processing.set(false);
                return;
            }

            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int i = 0; i < averageArray.length; i++) {
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }
            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
            if(started) {
                time.setText("Thời gian đo:" + String.valueOf((System.currentTimeMillis() - startTime)/1000)+" s");
            }
            Log.d("heartrate step: ", String.valueOf(imgAvg));
            if (imgAvg < rollingAverage && imgAvg > 180) {
                if(lastBeatTime != 0) {
                    long curTime = System.currentTimeMillis();
                    long step =  curTime- lastBeatTime;
                    if (step < 400) {
                        Log.d("heartrate step: ", String.valueOf(step));
                    } else {
                        lastBeatTime = curTime;
                        if (onBeat) {
                            beats++;
                            if(!started && beats == 4) {
                                started =true;
                                startTime = System.currentTimeMillis();
                            }
                            if(beats == 44) {
                                long endTime = System.currentTimeMillis();
                                double totalTimeInSecs = (endTime - startTime) / 1000d;
                                double bps = ((beats-4) / totalTimeInSecs);
                                int dpm = (int) (bps * 60d);
                                heartRate.setText(String.valueOf(dpm));
                                bmpText.setText("BMP");
                                Heart_Rate mHeart_rate = new Heart_Rate();
                                mHeart_rate.setheart_rate(String.valueOf(dpm));
                                mHeart_rate.sethr_date(String.valueOf(Calendar.getInstance().getTime().getTime()));
                                Heart_RateDao.insertRecord(mHeart_rate);
                                started = false;
                                measuring = false;
                            }
                            if(beats < 44 ){
                                if(beats >=4) heartRate.setText(String.valueOf((int)(beats-4)));
                            }
                            beatImg.setImageResource(R.drawable.green_icon);
                            onBeat = false;
                            // Log.d(TAG, "BEAT!! beats="+beats);
                        }
                    }
                }

            } else if (imgAvg > rollingAverage && imgAvg > 180) {
                onBeat = true;
                beatImg.setImageResource(R.drawable.red_icon);
            }

            if (averageIndex == averageArraySize) averageIndex = 0;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;
            processing.set(false);
        }

    };

    private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
                //Log.e("PreviewDemo-surfaceCallback", "Exception in setPreviewDisplay()", t);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
             //   Log.d(TAG, "Using width=" + size.width + " height=" + size.height);
            }
            camera.setParameters(parameters);
            camera.startPreview();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignore
        }
    };

    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea < resultArea) result = size;
                }
            }
        }

        return result;
    }
    public static void startBtnOnclick(View v) {
        started = false;
        beats = 0;
        bmpText.setText("");
        time.setText("Sẵn sàng đo....");
        heartRate.setText("0");
        measuring = true;
        lastBeatTime = System.currentTimeMillis();
    }
    public void beatHisOnclick(View v) {
        Intent mIntent = new Intent(this, BeatHistoryActivity.class);
        startActivity(mIntent);
    }
    private static Bitmap tobw(Bitmap src)
    {
        int width, height;
        height = src.getHeight();
        width = src.getWidth();
        Bitmap dest = Bitmap.createBitmap(
                src.getWidth(), src.getHeight(), src.getConfig());

        for(int x = 0; x < src.getWidth(); x++){
            for(int y = 0; y < src.getHeight(); y++){

                int pixel = src.getPixel(x, y);

                //get grayscale value
                int gray = (pixel & 0xFF);
                int newPixel;
                //get binary value
                if(gray < 56){
                    newPixel = 0;
                } else{
                    newPixel = 16777215;
                }
                dest.setPixel(x, y, newPixel);
            }
        }

        return dest;
    }
    private static int getBlack(Bitmap src)
    {
        int width, height, res = 0;
        height = src.getHeight();
        width = src.getWidth();
        for(int x = 0; x < src.getWidth(); x++){
            for(int y = 0; y < src.getHeight(); y++){

                int pixel = src.getPixel(x, y);

                //get grayscale value
                int gray = (pixel & 0xFF);
                if(gray < 56) res++;
            }
        }
        return res;
    }


}