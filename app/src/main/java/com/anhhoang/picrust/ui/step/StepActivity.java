package com.anhhoang.picrust.ui.step;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.anhhoang.picrust.R;
import com.anhhoang.picrust.data.Step;

import java.util.ArrayList;
import java.util.List;

public class StepActivity extends AppCompatActivity {
    public static final String EXTRA_STEPS = "ExtraSteps";
    private static final String EXTRA_STEP_ID = "ExtraStepId";

    public static Intent getStartingIntent(Context context, int stepId, List<Step> steps) {
        Intent intent = new Intent(context, StepActivity.class);
        intent.putExtra(EXTRA_STEPS, new ArrayList<>(steps));
        intent.putExtra(EXTRA_STEP_ID, stepId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (!intent.hasExtra(EXTRA_STEPS) && !intent.hasExtra(EXTRA_STEP_ID)) {
            throw new IllegalArgumentException("StepActivity started without required extra EXTRA_STEPS");
        }

        List<Step> steps = intent.getParcelableArrayListExtra(EXTRA_STEPS);
        int stepId = intent.getIntExtra(EXTRA_STEP_ID, 0);
    }
}
