package com.anhhoang.picrust.ui.step;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anhhoang.picrust.R;
import com.anhhoang.picrust.data.Step;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class StepFragment extends Fragment implements StepContracts.View {
    @BindView(R.id.step_video_player)
    SimpleExoPlayerView stepPlayerView;
    @BindView(R.id.step_description_text_view)
    TextView tvStepDescription;
    @BindView(R.id.previous_button)
    Button btnPrevious;
    @BindView(R.id.next_button)
    Button btnNext;

    private StepContracts.Presenter presenter;

    public StepFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.start();
    }

    @Override
    public void setPresenter(StepContracts.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showStep(Step step) {
        tvStepDescription.setText(step.getDescription());
    }

    @Override
    public void showPrevButton(boolean isVisible) {
        btnPrevious.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showNextButton(boolean isVisible) {
        btnNext.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showSelectedStep() {

    }

    @OnClick(R.id.previous_button)
    public void onPreviousClicked() {
        presenter.openPreviousStep();
    }

    @OnClick(R.id.next_button)
    public void onNextClicked() {
        presenter.openNextStep();
    }
}
