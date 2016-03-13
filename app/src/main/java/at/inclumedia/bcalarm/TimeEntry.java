package at.inclumedia.bcalarm;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by boehni on 12/03/16.
 */
public class TimeEntry extends SugarRecord {
    boolean isRepeating;
    Date alarmTime;
    int alarmId;

    public TimeEntry() {}
}
