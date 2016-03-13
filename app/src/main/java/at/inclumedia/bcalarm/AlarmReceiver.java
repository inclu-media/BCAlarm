package at.inclumedia.bcalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Created by boehni on 11/03/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent originalIntent) {

        Log.d(LOG_TAG, "Alarm received");

        Intent alarmIntent = new Intent(context,AlarmActivity.class);
        alarmIntent.setAction(Intent.ACTION_RUN);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(alarmIntent);
    }
}
