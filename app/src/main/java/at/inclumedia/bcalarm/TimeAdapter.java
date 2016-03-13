package at.inclumedia.bcalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by boehni on 12/03/16.
 */
public class TimeAdapter extends BaseAdapter {

    private final static String LOG_TAG = TimeAdapter.class.getSimpleName();

    private List<TimeEntry> mItems;
    private Context mContext;
    private DataSetChangedListener mListener;

    public interface DataSetChangedListener {
        void listupdated();
    }

    private static class ViewHolder {
        TextView tvAlarmTime;
        ImageButton ibDeleteAlaram;

        public ViewHolder(View view) {
            tvAlarmTime = (TextView)view.findViewById(R.id.tvAlarmTime);
            ibDeleteAlaram = (ImageButton)view.findViewById(R.id.ibDeleteAlaram);
        }
    }

    public TimeAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return (mItems != null) ? mItems.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder)convertView.getTag();
        }

        // set alarm time text
        final TimeEntry te = (TimeEntry)getItem(position);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        vh.tvAlarmTime.setText(format.format(te.alarmTime));

        // delete alarm handler
        vh.ibDeleteAlaram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // cancel the alarm
                AlarmManager alarmMgr = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(mContext, AlarmReceiver.class);
                PendingIntent alarmIntent = PendingIntent.getBroadcast(mContext, te.alarmId, intent, 0);
                alarmMgr.cancel(alarmIntent);

                // delete alarm from db
                if (!te.delete()) {
                    Log.e(LOG_TAG, "Error deleting alarm from DB");
                }
                else if(mListener != null) {
                    mListener.listupdated();
                }
            }
        });

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    public void setEntries(List<TimeEntry> items) {
        mItems = items;
    }

    public void setChangedListener(DataSetChangedListener listener) {
        mListener = listener;
    }
}
