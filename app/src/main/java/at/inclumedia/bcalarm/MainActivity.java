package at.inclumedia.bcalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;


public class MainActivity extends AppCompatActivity implements TimeAdapter.DataSetChangedListener {

    private List<TimeEntry> mEntries;
    private TimeAdapter mAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set up list view
        mAdapter = new TimeAdapter(getApplicationContext());
        mAdapter.setChangedListener(this);
        mListView = (ListView)findViewById(R.id.lvAlarms);
        mListView.setAdapter(mAdapter);
        updateEntries();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TimeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void listupdated() {
        updateEntries();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        updateEntries();
    }

    private void updateEntries() {
        mEntries = TimeEntry.findWithQuery(TimeEntry.class, "select * from TIME_ENTRY");
        mAdapter.setEntries(mEntries);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
