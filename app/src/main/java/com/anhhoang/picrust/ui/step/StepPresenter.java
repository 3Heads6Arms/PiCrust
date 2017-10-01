package com.anhhoang.picrust.ui.step;

import android.os.Parcel;
import android.os.Parcelable;

import com.anhhoang.picrust.data.Step;
import com.anhhoang.picrust.ui.step.StepContracts.Presenter;

import java.util.List;

/**
 * Created by anh.hoang on 9/29/17.
 */

public class StepPresenter implements Presenter, Parcelable {
    public static final Parcelable.Creator<StepPresenter> CREATOR = new Parcelable.Creator<StepPresenter>() {
        @Override
        public StepPresenter createFromParcel(Parcel source) {
            return new StepPresenter(source);
        }

        @Override
        public StepPresenter[] newArray(int size) {
            return new StepPresenter[size];
        }
    };
    private final List<Step> steps;
    private StepContracts.View view;
    private int currentStepId;

    public StepPresenter(StepContracts.View view, int stepId, List<Step> steps) {
        this.currentStepId = stepId;
        this.steps = steps;
        switchView(view);
    }

    protected StepPresenter(Parcel in) {
        this.steps = in.createTypedArrayList(Step.CREATOR);
        this.currentStepId = in.readInt();
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
                view.showNextButton(i != steps.size() - 1);
                view.showPrevButton(i != 0);
                return;
            }
        }
    }

    @Override
    public void openPreviousStep() {
        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);
            if (step.getId() == currentStepId) {
                if (i <= 0) { // No way i can be less than 0, just to ensure else clause will be greater than 0
                    return;
                } else {
                    currentStepId = i - 1;
                    break;
                }
            }
        }
        view.showSelectedStep();
    }

    @Override
    public void openNextStep() {
        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);
            if (step.getId() == currentStepId) {
                if (i >= steps.size() - 1) {
                    return;
                } else {
                    currentStepId = ++i;
                    break;
                }
            }
        }
        view.showSelectedStep();
    }

    @Override
    public void switchView(StepContracts.View view) {
        this.view = view;

        view.setPresenter(this);
    }

    @Override
    public void setStep(int stepId) {
        currentStepId = stepId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.steps);
        dest.writeInt(this.currentStepId);
    }
}
