package com.bitmovin.exoplayeranalyticsexample;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bitmovin.bitmovinanalyticscollector.analytics.BitmovinAnalytics;
import com.bitmovin.bitmovinanalyticscollector.enums.CDNProvider;
import com.bitmovin.bitmovinanalyticscollector.utils.BitmovinAnalyticsConfig;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private SimpleExoPlayer player;
    private SimpleExoPlayerView simpleExoPlayerView;
    private Button releaseButton;
    private Button createButton;
    private static final DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.a_main_exoplayer);
        releaseButton = findViewById(R.id.release_button);
        releaseButton.setOnClickListener(this);
        createButton = findViewById(R.id.create_button);
        createButton.setOnClickListener(this);
        createPlayer();
    }



    private void createPlayer() {
        if(player==null) {
            TrackSelection.Factory videoTrackSelectionFactory
                    = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            RenderersFactory renderersFactory = new DefaultRenderersFactory(this);
            player = ExoPlayerFactory.newSimpleInstance(renderersFactory,
                    new DefaultTrackSelector(videoTrackSelectionFactory), new DefaultLoadControl());

            BitmovinAnalyticsConfig bitmovinAnalyticsConfig = new BitmovinAnalyticsConfig("9ae0b480-f2ee-4c10-bc3c-cb88e982e0ac", "18ca6ad5-9768-4129-bdf6-17685e0d14d2", this);
            bitmovinAnalyticsConfig.setCdnProvider(CDNProvider.BITMOVIN);
            BitmovinAnalytics analyticsCollector = new BitmovinAnalytics(bitmovinAnalyticsConfig);
            analyticsCollector.attachPlayer(player);

            simpleExoPlayerView.setPlayer(player);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, bandwidthMeter,
                    buildHttpDataSourceFactory(bandwidthMeter));

            Uri uri = Uri.parse("http://bitmovin-a.akamaihd.net/content/MI201109210084_1/mpds/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.mpd");
            Uri uri2 = Uri.parse("http://vm2.dashif.org/livesim-dev/segtimeline_1/testpic_6s/Manifest.mpd");
            Uri uri3 = Uri.parse("https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_ts/master.m3u8");
            Uri uri4 = Uri.parse("http://c742eca4-lp-omega.ums.ustream.tv/playlist/auditorium/channel/9408562/playlist.m3u8?token=208723_1514483442312&appType=11&appVersion=3&ts=1514483442&chunkingType=improved&geo=US&sgn=353890216fdb617d960b71bc428583675e89f0c3&preferredBitrate=0&cdnHost=uhs-akamai.ustream.tv");

            DashMediaSource dashMediaSource = new DashMediaSource(uri, dataSourceFactory,
                    new DefaultDashChunkSource.Factory(dataSourceFactory), null, null);

            HlsMediaSource hlsMediaSource = new HlsMediaSource(uri4,dataSourceFactory,new Handler(), null);


            player.prepare(dashMediaSource);

        }
    }

    private void releasePlayer(){
        if(player != null){
            player.release();
            player = null;
        }
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(Util.getUserAgent(this,
                getString(R.string.app_name)), bandwidthMeter);
    }

    @Override
    public void onClick(View v) {
        if (v == releaseButton){
            releasePlayer();
        }else if (v == createButton){
            createPlayer();
        }
    }
}
