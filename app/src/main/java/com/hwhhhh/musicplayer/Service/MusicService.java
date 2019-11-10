package com.hwhhhh.musicplayer.Service;

import android.content.Context;

import java.util.Map;

public interface MusicService {
    int PLAY_ORDER = 1;
    int PLAY_RANDOM = 4;

    /**
     * 播放musicName
     * @param musicName 音乐名称 包括.mp3 如果参数为null,则播放当前音乐，若音乐已播放则继续播放。
     */
    void play(String musicName);


    void seekTo(int progress);
    /**
     * 暂停
     */
    void onPause();

    /**
     * 继续播放
     */
    void onResume();

    /**
     * 更改播放顺序
     * @param i i为PLAY_ORDER时，为顺序播放；为PLAY_RANDOM为随机播放
     */
    void setPlayOrder(int i);

    /**
     * 下一首
     */
    void next();

    /**
     * 上一首
     */
    void last();

    /**
     * 获取当前进度
     * @return 当前进度 单位：毫秒
     */
    int getCurrentProgress();

    /**
     * destroy MusicService
     */
    void onDestroy();

    /**
     * 获取播放顺序
     * @return 1 为顺序播放， 4 为随机播放
     */
    int getPlayOrder();

    /**
     * 获取当前播放音乐的信息
     * @return  "歌曲名称\n歌手”
     */
    String getCurrentMusicInfo();

    /**
     * 获得总时长
     * @return 单位：毫秒
     */
    int getDuration();

    void loadMusic(String musicName);

    /**
     * 获取音乐列表
     * @return 音乐名称数组
     */
    String[] getMusicNames();

    boolean isPlaying();

    void setMusicChangedListener(MusicChangedListener musicChangedListener);

    void setMusicPlayingChangedListener(MusicPlayingChangedListener musicPlayingChangedListener);
}
