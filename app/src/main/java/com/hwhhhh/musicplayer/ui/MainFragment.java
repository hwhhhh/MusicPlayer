package com.hwhhhh.musicplayer.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hwhhhh.musicplayer.MainActivity;
import com.hwhhhh.musicplayer.R;
import com.hwhhhh.musicplayer.Service.MusicChangedListener;
import com.hwhhhh.musicplayer.Service.MusicPlayingChangedListener;
import com.hwhhhh.musicplayer.Service.MusicService;
import com.hwhhhh.musicplayer.ServiceImpl.MusicServiceImpl;

import java.lang.ref.WeakReference;

public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    private static final int REFRESH_FOOTER = 3;
    private static final int REFRESH_PLAY = 4;
    private static MainFragment mainFragment = null;

    private View view;
    private MusicService musicService;
    private TextView textView_song;
    private FooterHandler footerHandler;

    public static MainFragment getInstance() {
        if (mainFragment == null) {
            synchronized (MainFragment.class) {
                if (mainFragment == null) {
                    mainFragment = new MainFragment();
                }
            }
        }
        return mainFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        view = inflater.inflate(R.layout.fragment_main, container, false);
        initMainView();
        footerHandler = new FooterHandler(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 初始化main布局
     */
    private void initMainView() {
        musicService = MusicServiceImpl.getInstance(getContext());
        //footer中的播放按钮
        final ImageView ImageView_play = view.findViewById(R.id.main_footer_play);
        ImageView_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService.play(null);
                if (musicService.isPlaying()) {
                    ImageView_play.setImageResource(R.drawable.ic_pause_red);
                } else {
                    ImageView_play.setImageResource(R.drawable.ic_play_red);
                }
            }
        });

        textView_song = view.findViewById(R.id.main_footer_song);
        textView_song.setText(musicService.getCurrentMusicInfo());
        musicService.setMusicChangedListener(new MusicChangedListener() {
            @Override
            public void refresh() {
                refreshFooter();
            }
        });
        musicService.setMusicPlayingChangedListener(new MusicPlayingChangedListener() {
            @Override
            public void afterChanged() {
                refreshPlay();
            }
        });

        LinearLayout footer = view.findViewById(R.id.main_footer);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterMusicInfoFragment();
            }
        });
    }


    private void enterMusicInfoFragment() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.enterMusicInfoFragment();
        }
    }

    private void refreshFooter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                footerHandler.sendEmptyMessage(REFRESH_FOOTER);
            }
        }).start();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.d(TAG, "onHiddenChanged: ");
        if (!hidden) {
            refreshPlay();
            initMainView();
        }
        super.onHiddenChanged(hidden);
    }

    private void refreshPlay() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                footerHandler.sendEmptyMessage(REFRESH_PLAY);
            }
        }).start();
    }


    /**
     * 内部类，避免Handler发生内存泄漏
     * 由handleMessage处理
     */
    private static class FooterHandler extends Handler {
        private static final String TAG = "SeekBarHandler";

        WeakReference<MainFragment> mainFragment;
        private FooterHandler(MainFragment mainFragment) {
            this.mainFragment = new WeakReference<>(mainFragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            MainFragment activity = mainFragment.get();
            switch (msg.what) {
                case REFRESH_FOOTER:
                    activity.textView_song.setText(activity.musicService.getCurrentMusicInfo());
                    break;
                case REFRESH_PLAY:
                    if (activity.getActivity() != null) {
                        ImageView imageView = activity.getActivity().findViewById(R.id.main_footer_play);
                        if (activity.musicService.isPlaying()) {
                            imageView.setImageResource(R.drawable.ic_pause_red);
                        } else {
                            imageView.setImageResource(R.drawable.ic_play_red);
                        }
                    }
                    break;
            }
        }
    }
}
