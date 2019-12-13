package com.example.levelapp_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mSensorMagnetometer;

    private AnimatedView mAnimatedView = null;

    public ImageView targetImage;
    private Display mDisplay;
    private Bitmap bitmap;

    private float[] mAccelerometerData = new float[3];
    private float[] mMagnetometerData = new float[3];

    // TextViews to display current sensor values.
    //private TextView mTextSensorAzimuth;
    private TextView mTextSensorPitch;
    private TextView mTextSensorRoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        targetImage = findViewById(R.id.targetImage);
        mTextSensorPitch = findViewById(R.id.valueOfY);
        mTextSensorRoll = findViewById(R.id.valueOfX);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMagnetometer = mSensorManager.getDefaultSensor(
                Sensor.TYPE_MAGNETIC_FIELD);

        mAnimatedView = new AnimatedView(this);

        //Set our content to a view, not like the traditional setting to a layout
        setContentView(mAnimatedView);

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        mDisplay = wm.getDefaultDisplay();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Listeners for the sensors are registered in this callback and
        // can be unregistered in onStop().
        //
        // Check to ensure sensors are available before registering listeners.
        // Both listeners are registered with a "normal" amount of delay
        // (SENSOR_DELAY_NORMAL).
        if (mAccelerometer != null) {
            mSensorManager.registerListener(this, mAccelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mSensorMagnetometer != null) {
            mSensorManager.registerListener(this, mSensorMagnetometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) { }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAnimatedView.onSensorEvent(event);
        }
        int sensorType = event.sensor.getType();

        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                mAccelerometerData = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagnetometerData = event.values.clone();
                break;
            default:
                return;
        }

        float[] rotationMatrix = new float[9];
        boolean rotationOK = SensorManager.getRotationMatrix(rotationMatrix,
                null, mAccelerometerData, mMagnetometerData);

        float orientationValues[] = new float[3];
        float[] rotationMatrixAdjusted = new float[9];
        if (rotationOK) {
            SensorManager.getOrientation(rotationMatrix, orientationValues);
        }

        float azimuth = orientationValues[0];
        float pitch = orientationValues[1];
        float roll = orientationValues[2];

        mTextSensorPitch.setText(getResources().getString(R.string.value_format, pitch));
    }

    public class AnimatedView extends View {

        private static final int CIRCLE_RADIUS = 50
                ; //pixels

        private Paint mPaint, nPaint;
        private int x;
        private int y;
        private int viewWidth;
        private int viewHeight;

        public AnimatedView(Context context) {
            super(context);
            nPaint = new Paint();
            mPaint = new Paint();

            mPaint.setColor(Color.MAGENTA);
            nPaint.setColor(Color.GREEN);

            targetImage = findViewById(R.id.targetImage);

            mTextSensorRoll = findViewById(R.id.valueOfX);

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            viewWidth = w;
            viewHeight = h;
        }

        public void onSensorEvent (SensorEvent event) {
            x = x - (int) event.values[0];
            y = y + (int) event.values[1];
            //Make sure we do not draw outside the bounds of the view.
            //So the max values we can draw to are the bounds + the size of the circle
            if (x <= 0 + CIRCLE_RADIUS) {
                x = 0 + CIRCLE_RADIUS;
            }
            if (x >= viewWidth - CIRCLE_RADIUS) {
                x = viewWidth - CIRCLE_RADIUS;
            }
            if (y <= 0 + CIRCLE_RADIUS) {
                y = 0 + CIRCLE_RADIUS;
            }
            if (y >= viewHeight - CIRCLE_RADIUS) {
                y = viewHeight - CIRCLE_RADIUS;
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
           // canvas.drawCircle(500,1000,400,nPaint);
            canvas.drawCircle(x, y, CIRCLE_RADIUS, mPaint);

            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.target);

            canvas.drawBitmap(bitmap,300, 800, nPaint);
            canvas.drawText(mAccelerometerData.toString(), 100, 100, mPaint);
            mPaint.setTextSize(50);
            //We need to call invalidate each time, so that the view continuously draws
            invalidate();
        }
    }
}
