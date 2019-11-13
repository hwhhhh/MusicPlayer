package com.hwhhhh.musicplayer.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hwhhhh.musicplayer.MainActivity;
import com.hwhhhh.musicplayer.R;
import com.hwhhhh.musicplayer.Service.MusicChangedListener;
import com.hwhhhh.musicplayer.Service.MusicPlayingChangedListener;
import com.hwhhhh.musicplayer.Service.MusicService;
import com.hwhhhh.musicplayer.ServiceImpl.MusicServiceImpl;
import com.hwhhhh.musicplayer.adater.SongAdapter;
import com.hwhhhh.musicplayer.dto.SongDto;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.List;

public class MusicInfoFragment extends Fragment {
    private static final String TAG = "MusicInfoFragment";
    private static final int REFRESH_SEEKBAR_PROGRESS = 1;
    private static final int REFRESH_SEEKBAR_MAX = 2;
    private static final int REFRESH_HEADER = 3;
    private static final int REFRESH_PLAY = 4;
    private static MusicInfoFragment musicInfoFragment = null;

    private MusicService musicService;
    private View view;
    private SeekBarHandler seekBarHandler;
    private TextView textView_title;
    private SeekBar seekBar;

    public static MusicInfoFragment getInstance() {
        if (musicInfoFragment == null) {
            synchronized (MusicInfoFragment.class) {
                if (musicInfoFragment == null) {
                    musicInfoFragment = new MusicInfoFragment();
                }
            }
        }
        return musicInfoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        seekBarHandler = new SeekBarHandler(this);
        view = inflater.inflate(R.layout.fragment_musicinfo, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView() {
        musicService = MusicServiceImpl.getInstance(getContext());
        textView_title = view.findViewById(R.id.info_head_title);
        seekBar = view.findViewById(R.id.info_seekBar);
        final ImageView imageView_order = view.findViewById(R.id.info_order);
        ImageView imageView_last = view.findViewById(R.id.info_last);
        final ImageView imageView_play = view.findViewById(R.id.info_play);
        ImageView imageView_next = view.findViewById(R.id.info_next);
        ImageView imageView_list = view.findViewById(R.id.info_list);
        imageView_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initOrderFragment();
            }
        });

        musicService.setMusicChangedListener(new MusicChangedListener() {
            @Override
            public void refresh() {
                refreshHeader();
            }
        });
        musicService.setMusicPlayingChangedListener(new MusicPlayingChangedListener() {
            @Override
            public void afterChanged() {
                refreshImgPlay();
                if (musicService.isPlaying()) {
                    send();
                } else {
                    seekBarHandler.removeCallbacksAndMessages(null);
                }
            }
        });

        textView_title.setText(musicService.getCurrentMusicInfo());

        imageView_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicService.getPlayOrder() == MusicService.PLAY_ORDER) {
                    musicService.setPlayOrder(MusicService.PLAY_RANDOM);
                    imageView_order.setImageResource(R.drawable.ic_play_random_black);
                } else {
                    musicService.setPlayOrder(MusicService.PLAY_ORDER);
                    imageView_order.setImageResource(R.drawable.ic_play_order_black);
                }
            }
        });

        imageView_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService.last();
            }
        });

        imageView_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService.next();
            }
        });

        refreshImgPlay();
        if (musicService.isPlaying()) {
            send();
        }
        imageView_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicService.isPlaying()) {
                    musicService.onPause();
                } else {
                    musicService.play(null);
                }
                refreshImgPlay();
            }
        });
        seekBar.setMax(musicService.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicService.seekTo(seekBar.getProgress());
            }
        });

        ImageView imageView_back = view.findViewById(R.id.info_head_back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.d(TAG, "onHiddenChanged: ");
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initView();
        }
    }

    private void refreshImgPlay() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                seekBarHandler.sendEmptyMessage(REFRESH_PLAY);
            }
        }).start();
    }

    private void refreshHeader() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                seekBarHandler.sendEmptyMessage(REFRESH_HEADER);
            }
        }).start();
    }

    /**
     * 新建线程，通过handler定时刷新SeekBar的ui
     */
    private void send() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                seekBarHandler.sendEmptyMessage(REFRESH_SEEKBAR_MAX);
            }
        }).start();
    }

    /**
     * 内部类，避免Handler发生内存泄漏
     * 由handleMessage处理
     */
    private static class SeekBarHandler extends Handler {
        private static final String TAG = "SeekBarHandler";
        WeakReference<MusicInfoFragment> musicInfoFragment;
        private SeekBarHandler(MusicInfoFragment musicInfoFragment) {
            this.musicInfoFragment = new WeakReference<>(musicInfoFragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            MusicInfoFragment activity = musicInfoFragment.get();
            switch (msg.what) {
                case REFRESH_HEADER:
                    activity.textView_title.setText(activity.musicService.getCurrentMusicInfo());
                    break;
                case REFRESH_PLAY:
                    FragmentActivity fragmentActivity = activity.getActivity();
                    if (fragmentActivity != null) {
                        ImageView imageView = fragmentActivity.findViewById(R.id.info_play);
                        if (activity.musicService.isPlaying()) {
                            imageView.setImageResource(R.drawable.ic_pause_black);
                        } else {
                            imageView.setImageResource(R.drawable.ic_play_black);
                        }
                    }
                    break;
                case REFRESH_SEEKBAR_MAX:
                    activity.seekBar.setMax(activity.musicService.getDuration());
                case REFRESH_SEEKBAR_PROGRESS:
                    if (activity.seekBar.getMax() != activity.musicService.getDuration()) {
                        activity.seekBarHandler.removeCallbacksAndMessages(null);
                        activity.send();
                    } else {
                        activity.seekBar.setProgress(activity.musicService.getCurrentProgress());
                        sendEmptyMessageDelayed(REFRESH_SEEKBAR_PROGRESS, 1000);
                    }
                    break;
            }
        }
    }

    private void initOrderFragment() {
        if (getActivity()!=null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = fragmentManager.findFragmentByTag(OrderFragment.class.getName());
            if (fragment == null){
                fragmentTransaction
                        .add(R.id.info_fragment_host, OrderFragment.getInstance(), OrderFragment.class.getName());
            } else {
                fragmentTransaction
                        .show(fragment);
            }
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
