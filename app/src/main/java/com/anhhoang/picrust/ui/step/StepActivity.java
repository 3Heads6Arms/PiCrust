package com.anhhoang.picrust.ui.step;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.anhhoang.picrust.R;
import com.anhhoang.picrust.data.Step;

import java.util.ArrayList;
import java.util.List;

public class StepActivity extends AppCompatActivity implements StepFragment.OnStepNavigationListener {
    private static final String TAG = "StepFragmentTag";
    private static final String EXTRA_STEPS = "ExtraSteps";
    private static final String EXTRA_STEP_ID = "ExtraStepId";
    private static final String EXTRA_RECIPE_NAME = "ExtraRecipeName";
    private static final String PRESENTER_KEY = "PresenterKey";
    private StepPresenter stepPresenter;

    public static Intent getStartingIntent(Context context, long stepId, List<Step> steps, String recipeName) {
        Intent intent = new Intent(context, StepActivity.class);
        intent.putExtra(EXTRA_STEPS, new ArrayList<>(steps));
        intent.putExtra(EXTRA_STEP_ID, stepId);
        intent.putExtra(EXTRA_RECIPE_NAME, recipeName);

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
        if (!intent.hasExtra(EXTRA_STEPS) || !intent.hasExtra(EXTRA_STEP_ID) || !intent.hasExtra(EXTRA_RECIPE_NAME)) {
            throw new IllegalArgumentException("StepActivity started without required extra EXTRA_STEPS or EXTRA_STEP_ID or EXTRA_RECIPE_NAME");
        }

        getSupportActionBar().setTitle(intent.getStringExtra(EXTRA_RECIPE_NAME));

        FragmentManager fragmentManager = getSupportFragmentManager();

        StepFragment stepFragment;
        if (savedInstanceState != null) {
            stepFragment = (StepFragment) fragmentManager.findFragmentByTag(TAG);
            stepPresenter = savedInstanceState.getParcelable(PRESENTER_KEY);
            stepPresenter.switchView(stepFragment);
        } else {
            List<Step> steps = intent.getParcelableArrayListExtra(EXTRA_STEPS);
            long stepId = intent.getLongExtra(EXTRA_STEP_ID, 0);
            stepFragment = new StepFragment();
            stepPresenter = new StepPresenter(stepFragment, stepId, steps);

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.step_fragment, stepFragment, TAG)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(PRESENTER_KEY, stepPresenter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStepChanged() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        StepFragment newStepFragment = new StepFragment();

        stepPresenter.switchView(newStepFragment);

        fragmentManager
                .beginTransaction()
                .replace(R.id.step_fragment, newStepFragment, TAG)
                .commit();
    }
}
