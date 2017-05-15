package org.neotech.app.applicationlifecycledemo;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ActivityA extends AppCompatActivity implements View.OnClickListener {

    private int activityCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        findViewById(R.id.button).setOnClickListener(this);

        if(savedInstanceState == null) {
            activityCount = getIntent().getIntExtra("activityCount", 0);
        } else {
            activityCount = savedInstanceState.getInt("activityCount", 0);
        }
        if (activityCount != 0) {
            ((TextView) findViewById(android.R.id.text1)).setText(getString(R.string.activity_count, activityCount));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("activityCount", activityCount);
    }

    @Override
    public void onClick(View v) {
        final Intent intent = new Intent(this, ActivityA.class);
        intent.putExtra("activityCount", activityCount + 1);
        startActivity(intent);
    }
}
