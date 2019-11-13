package com.hwhhhh.musicplayer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hwhhhh.musicplayer.R;
import com.hwhhhh.musicplayer.adater.SongAdapter;
import com.hwhhhh.musicplayer.dto.SongDto;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class OrderFragment extends Fragment {
    private View view;
    private SongDto songDto;
    private static OrderFragment orderFragment;

    public static OrderFragment getInstance() {
        if (orderFragment == null) {
            synchronized (OrderFragment.class) {
                if (orderFragment == null) {
                    orderFragment = new OrderFragment();
                }
            }
        }
        return orderFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        view = inflater.inflate(R.layout.fragment_musicinfo_order, container, false);
        initView();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initView();
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    private void initView() {
        ListView listView = view.findViewById(R.id.info_song_list);
        if (songDto != null) {
            listView.setAdapter(new SongAdapter(getContext(),this.songDto));
        } else {
            listView.setAdapter(new SongAdapter(getContext()));
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC, sticky = true)
    public void onGetMessage(SongDto songDto) {
        if (songDto != null) {
            this.songDto = songDto;
        }
    }
}
