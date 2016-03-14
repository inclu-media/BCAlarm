package at.inclumedia.bcalarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AlarmActivity extends AppCompatActivity {

    private static final String LOG_TAG = AlarmActivity.class.getSimpleName();
    private static final int RING_TIME_SEC = 180; // 3 minutes
    private static final String PREF_LEGACY = "pref_legacy";

    private SoundPool mSoundPool;
    private int mSoundNotificationId;
    private Vibrator mVibrator;
    private long[] mPattern = {300, 300, 300, 300, 300, 300};
    private CountDownTimer mTimer;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // window mode
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // sound
        mSoundPool = new SoundPool(2, AudioManager.STREAM_ALARM, 0);
        mSoundNotificationId = mSoundPool.load(this, R.raw.notification, 1);
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i2) {
                soundPool.play(mSoundNotificationId, 1.f, 1.f, 0, 0, 1.f);
            }
        });

        // vibration
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mVibrator.vibrate(mPattern, -1);


        setContentView(R.layout.activity_alarm);
    }

    /**
     * Fix attempt
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (!mPrefs.getBoolean(PREF_LEGACY, false)) {
            Log.i(LOG_TAG, "Using updated code");
            processTimer();
        }
    }

    /**
     * Legacy code
     * There is a chance, that CountDownTimer blocks onStart "partly" and prohibits
     * the Activity from popping up on time. In onResume this problem should not be
     * occurring.
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (mPrefs.getBoolean(PREF_LEGACY, false)) {
            Log.i(LOG_TAG, "Using legacy code");
            processTimer();
        }
    }

    private void processTimer() {
        mTimer = new CountDownTimer(RING_TIME_SEC * 1000, 1000) {
            private int seconds = 0;

            @Override
            public void onTick(long l) {
                seconds++;
                update();
            }

            @Override
            public void onFinish() {
                update();
                finish();
            }

            private void update() {

                /* TODO: display timer
                final Calendar calendar = new GregorianCalendar();
                Date now = calendar.getTime();
                Button confirmButton = (Button) findViewById(R.id.confirm_button);

                long timeInterval = timeOut.getTime() - now.getTime();
                long seconds = timeInterval >= 0 ? timeInterval / 1000 : 0;

                calendar.setTime(new Date(seconds * 1000));
                confirmButton.setText(new SpannableStringBuilder(spannableString).append("\n" + countDownDateFormat.format(calendar.getTime())));
                */

                if (this.seconds % 10 == 0 && mSoundPool != null && mVibrator != null) {
                    mSoundPool.play(mSoundNotificationId, 1.f, 1.f, 1, 0, 1.f);
                    mVibrator.vibrate(mPattern, -1);
                }

            }
        };
        mTimer.start();
    }


    public void onConfirm(View view) {
        finish();
    }

    @Override
    public void finish() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        mSoundPool.unload(mSoundNotificationId);
        mSoundPool.release();
        mSoundPool = null;

        mVibrator.cancel();
        mVibrator = null;

        super.finish();
    }
}
