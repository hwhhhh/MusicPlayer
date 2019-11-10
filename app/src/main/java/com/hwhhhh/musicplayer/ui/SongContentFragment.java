package com.hwhhhh.musicplayer.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hwhhhh.musicplayer.R;
import com.hwhhhh.musicplayer.Service.MusicService;
import com.hwhhhh.musicplayer.ServiceImpl.MusicServiceImpl;
import com.hwhhhh.musicplayer.adater.SongAdapter;

public class SongContentFragment extends Fragment {
    private static final String TAG = "SongContentFragment";

    private View view;
    private MusicService musicService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_song, container, false);
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

    private void initView() {
        musicService = MusicServiceImpl.getInstance(getContext());
        ListView listView = view.findViewById(R.id.song_list);
        TextView textView_title = view.findViewById(R.id.song_title);
        listView.setAdapter(new SongAdapter(getContext(), musicService.getMusicNames()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String musicName = (String) adapterView.getItemAtPosition(i);
                musicService.play(musicName);
            }
        });
    }
}
