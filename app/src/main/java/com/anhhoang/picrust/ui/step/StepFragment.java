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
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * A placeholder fragment containing a simple view.
 */
public class StepFragment extends Fragment implements StepContracts.View {

    @BindView(R.id.step_video_player)
    SimpleExoPlayerView stepPlayerView;
    @BindView(R.id.step_description_text_view)
    TextView tvStepDescription;
    @Nullable
    @BindView(R.id.previous_button)
    Button btnPrevious;
    @Nullable
    @BindView(R.id.next_button)
    Button btnNext;
    @Nullable
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

    private long playbackPosition;

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
            if (parent != null) {
                parent.removeView(view);
            }
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
    public void onStop() {
        super.onStop();

        Picasso.with(getContext())
                .cancelRequest(thumbnailTarget);

        if (stepExoPlayer != null) {
            playbackPosition = stepExoPlayer.getCurrentPosition();
        }

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
                initializePlayer(Uri.parse(step.getVideoURL()));
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
        if (getActivity() instanceof OnStepNavigationListener) {
            ((OnStepNavigationListener) getActivity()).onStepChanged();
        }
    }

    @Optional
    @OnClick(R.id.previous_button)
    public void onPreviousClicked() {
        presenter.openPreviousStep();
    }

    @Optional
    @OnClick(R.id.next_button)
    public void onNextClicked() {
        presenter.openNextStep();
    }

    private void initializePlayer(Uri mediaUri) {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory trackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();

        stepExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);

        MediaSource mediaSource = new ExtractorMediaSource(
                mediaUri,
                new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "PiCrust")),
                new DefaultExtractorsFactory(),
                null,
                null);

        stepExoPlayer.prepare(mediaSource);
        stepExoPlayer.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    stepExoPlayer.seekTo(0);
                    stepExoPlayer.setPlayWhenReady(false);
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity() {

            }
        });

        stepPlayerView.setPlayer(stepExoPlayer);

        if (playbackPosition > 0) {
            stepExoPlayer.seekTo(playbackPosition);
            playbackPosition = 0;
        }
    }

    private void releasePlayer() {
        if (stepExoPlayer != null) {
            stepExoPlayer.stop();
            stepExoPlayer.release();
            stepExoPlayer = null;
        }
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

    interface OnStepNavigationListener {
        void onStepChanged();
    }
}
