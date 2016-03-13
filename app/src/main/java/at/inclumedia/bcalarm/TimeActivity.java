package at.inclumedia.bcalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import com.orm.SugarRecord;

import java.util.Calendar;
import java.util.Date;

public class TimeActivity extends AppCompatActivity {

    private final static String LOG_TAG = TimeActivity.class.getSimpleName();

    private Context mContext;
    private TimePicker mPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        mPicker  = (TimePicker)findViewById(R.id.timePicker);
        mContext = getApplicationContext();

        mPicker.setIs24HourView(true);
    }

    public void addNewTimeentry(View view) {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, mPicker.getCurrentHour());
        now.set(Calendar.MINUTE, mPicker.getCurrentMinute());

        Log.d(LOG_TAG, "Alarm set to: " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + "->" + now.getTimeInMillis());

        // set up repeating alarm
        AlarmManager alarmMgr = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(mContext, intent.hashCode(), intent, 0);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        // save time entry to db
        TimeEntry te = new TimeEntry();
        te.isRepeating = true;
        te.alarmTime = now.getTime();
        te.alarmId = intent.hashCode();
        te.save();

        finish();
    }
}
