package org.neotech.app.applicationlifecycledemo;

import android.content.Intent;
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

        activityCount = getIntent().getIntExtra("activity_count", 0);
        if(activityCount != 0) {
            ((TextView) findViewById(android.R.id.text1)).setText(getString(R.string.activity_count, activityCount));
        }
    }

    @Override
    public void onClick(View v) {
        final Intent intent = new Intent(this, ActivityA.class);
        intent.putExtra("activity_count", activityCount + 1);
        startActivity(intent);
    }
}
