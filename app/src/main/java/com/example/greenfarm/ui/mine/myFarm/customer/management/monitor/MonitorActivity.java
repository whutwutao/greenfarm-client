package com.example.greenfarm.ui.mine.myFarm.customer.management.monitor;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenfarm.R;
import com.example.greenfarm.utils.videoPlayer.VideoPlayerIJK;
import com.example.greenfarm.utils.videoPlayer.VideoPlayerListener;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MonitorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        String urlStr = "rtmp://192.168.43.192:1935/live/room";

        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Exception e) {
            e.printStackTrace();
        }

        VideoPlayerIJK ijkPlayer = findViewById(R.id.videoView);
        ijkPlayer.setVideoPath(urlStr);

        ijkPlayer.setListener(new VideoPlayerListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {

            }

            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                iMediaPlayer.seekTo(0);
                iMediaPlayer.start();
            }

            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {

                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
                if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    ijkPlayer.setVisibility(View.GONE);
                    Toast.makeText(MonitorActivity.this,"直播已经结束",Toast.LENGTH_SHORT).show();
                } else if (what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START || what == IjkMediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    ijkPlayer.setVisibility(View.VISIBLE);
                    Toast.makeText(MonitorActivity.this, "直播开始",Toast.LENGTH_SHORT).show();

                }
                return false;
            }

            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                iMediaPlayer.start();
            }

            @Override
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {

            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        IjkMediaPlayer.native_profileEnd();
    }

}