package com.bitmovin.analytics.exoplayer;

import com.bitmovin.analytics.BitmovinAnalytics;
import com.bitmovin.analytics.BitmovinAnalyticsConfig;
import com.google.android.exoplayer2.ExoPlayer;

public class ExoPlayerCollector extends BitmovinAnalytics {

  /**
   * Bitmovin Analytics
   *
   * @param bitmovinAnalyticsConfig {@link BitmovinAnalyticsConfig}
   */
  public ExoPlayerCollector(
      BitmovinAnalyticsConfig bitmovinAnalyticsConfig) {
    super(bitmovinAnalyticsConfig);
  }

  public void attachPlayer(ExoPlayer player) {
    ExoPlayerAdapter adapter = new ExoPlayerAdapter(player, this.bitmovinAnalyticsConfig,
        this.playerStateMachine);

    this.attach(adapter);
  }
}
