package com.anhhoang.picrust.ui.step;

import com.anhhoang.picrust.BasePresenter;
import com.anhhoang.picrust.BaseView;
import com.anhhoang.picrust.data.Step;

/**
 * Created by anh.hoang on 9/29/17.
 */

public interface StepContracts {
    interface View extends BaseView<Presenter> {
        void showStep(Step step);

        void showPrevButton(boolean isVisible);

        void showNextButton(boolean isVisible);

        void showSelectedStep();
    }

    interface Presenter extends BasePresenter {
        void loadStep();

        void openPreviousStep();

        void openNextStep();

        void switchView(View view);

        void setStep(long stepId);
    }
}
