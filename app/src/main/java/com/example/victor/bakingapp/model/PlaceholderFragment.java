package com.example.victor.bakingapp.model;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.victor.bakingapp.R;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A placeholder fragment containing a simple view.
 */
public  class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    @Override
    public void onStart() {
        super.onStart();
    }

    SimpleExoPlayer mExoPlayer;
    public ArrayList<Step> steps = new ArrayList<>();
    int pos;
    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.step_details)
    TextView mStepInstruction;
    @BindView(R.id.step_imageview)ImageView mStepImageView;
    private Unbinder unbinder;

    private static final String ARG_STEP_NUMBER = "step_number";
    private static final String ARG_STEP_DATA = "step_data";



    public PlaceholderFragment() {
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.pos = args.getInt(ARG_STEP_NUMBER);
        this.steps = args.getParcelableArrayList(ARG_STEP_DATA);

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber, ArrayList<Step> step) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STEP_NUMBER, sectionNumber);
        args.putParcelableArrayList(ARG_STEP_DATA, step);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList("step");
            pos = savedInstanceState.getInt("pos");
        }
        Step currentStep = steps.get(pos);
        String videoUrl = currentStep.getVideoURL();
        if(null != videoUrl && !videoUrl.isEmpty()){
            mPlayerView.setVisibility(View.VISIBLE);
            mStepImageView.setVisibility(View.GONE);
            initializePlayer(Uri.parse(videoUrl));
        }
        else if (null != currentStep.getThumbnailURL() && !currentStep.getThumbnailURL().isEmpty()){
               mPlayerView.setVisibility(View.GONE);
            mStepImageView.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(currentStep.getThumbnailURL()).centerCrop().into(mStepImageView);
        }
        mStepInstruction.setText(currentStep.getDescription());

        return rootView;
    }

    private void initializePlayer(Uri mediaUri){
        if(mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer =  ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            //Data Source
            String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getActivity(),
                    userAgent), new DefaultExtractorsFactory(),null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer(){
        if(mExoPlayer!=null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("step", steps);
        outState.putInt("pos", pos);
        super.onSaveInstanceState(outState);
    }
}

