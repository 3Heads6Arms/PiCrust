package com.anhhoang.picrust.ui.step;

import com.anhhoang.picrust.data.Step;
import com.anhhoang.picrust.ui.step.StepContracts.Presenter;

import java.util.List;

/**
 * Created by anh.hoang on 9/29/17.
 */

public class StepPresenter implements Presenter {
    private final List<Step> steps;
    private StepContracts.View view;
    private int currentStepId;

    public StepPresenter(StepContracts.View view, int stepId, List<Step> steps) {
        this.currentStepId = stepId;
        this.steps = steps;
        switchView(view);
    }

    @Override
    public void start() {
        loadStep();
    }

    @Override
    public void loadStep() {
        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);

            if (step.getId() == currentStepId) {
                view.showStep(step);
                view.showNextButton(i == steps.size() - 1);
                view.showPrevButton(i == 0);
                return;
            }
        }
    }

    @Override
    public void openPreviousStep() {
        // TODO:
        view.showSelectedStep();
    }

    @Override
    public void openNextStep() {
        // TODO:

        view.showSelectedStep();
    }

    @Override
    public void switchView(StepContracts.View view) {
        this.view = null;
        this.view = view;

        view.setPresenter(this);
    }
}
