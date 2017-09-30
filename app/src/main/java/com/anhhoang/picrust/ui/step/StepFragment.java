package com.anhhoang.picrust.ui.step;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anhhoang.picrust.R;
import com.anhhoang.picrust.data.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindBool;
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
    @BindView(R.id.detail_view)
    View detailView;
    @BindBool(R.bool.is_two_pane)
    boolean twoPane;

    private StepContracts.Presenter presenter;
    private SimpleExoPlayer stepExoPlayer;
    private Target thumbnailTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            stepPlayerView.setDefaultArtwork(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };
    private View view;

    public StepFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_step, container, false);
            ButterKnife.bind(this, view);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setImmersiveModeIfValid();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }


    @Override
    public void onDestroy() {
        Picasso.with(getContext())
                .cancelRequest(thumbnailTarget);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!getActivity().isChangingConfigurations()) {
            releasePlayer();
        }
    }

    @Override
    public void setPresenter(StepContracts.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showStep(Step step) {
        if (tvStepDescription != null) {
            tvStepDescription.setText(step.getDescription());
        }

        if (!TextUtils.isEmpty(step.getThumbnailURL())) {
            Picasso.with(getContext())
                    .load(step.getThumbnailURL())
                    .centerInside()
                    .into(thumbnailTarget);
        }

        if (TextUtils.isEmpty(step.getVideoURL())) {
            stepPlayerView.setVisibility(View.GONE);
        } else {
            stepPlayerView.setVisibility(View.VISIBLE);
            if (stepExoPlayer == null) {
                setupPlayer(Uri.parse(step.getVideoURL()));
            }
        }
    }

    @Override
    public void showPrevButton(boolean isVisible) {
        if (btnPrevious != null) {
            btnPrevious.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public void showNextButton(boolean isVisible) {
        if (btnNext != null) {
            btnNext.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public void showSelectedStep() {
        // TODO:
    }

    @OnClick(R.id.previous_button)
    public void onPreviousClicked() {
        presenter.openPreviousStep();
    }

    @OnClick(R.id.next_button)
    public void onNextClicked() {
        presenter.openNextStep();
    }

    private void setupPlayer(Uri mediaUri) {
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();

        if (stepExoPlayer == null) {
            stepExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        }

        MediaSource mediaSource = new ExtractorMediaSource(
                mediaUri,
                new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "PiCrust")),
                new DefaultExtractorsFactory(),
                null,
                null);

        stepExoPlayer.prepare(mediaSource);
        stepExoPlayer.setPlayWhenReady(false);

        stepPlayerView.setPlayer(stepExoPlayer);
    }

    private void releasePlayer() {
        stepExoPlayer.stop();
        stepExoPlayer.release();
        stepExoPlayer = null;
    }

    private void setImmersiveModeIfValid() {
        int orientation = getResources().getConfiguration().orientation;

        // Set immersive view for phone landscape
        if (orientation == Configuration.ORIENTATION_LANDSCAPE && !twoPane) {
            detailView.setVisibility(View.GONE);
            stepPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            getActivity()
                    .getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE);

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        } else if (!twoPane) {
            detailView.setVisibility(View.VISIBLE);
            stepPlayerView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.player_height);
        }
    }
}
