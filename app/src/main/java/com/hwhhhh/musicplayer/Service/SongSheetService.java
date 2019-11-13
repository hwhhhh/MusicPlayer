package com.hwhhhh.musicplayer.Service;

import com.hwhhhh.musicplayer.entity.SongBean;
import com.hwhhhh.musicplayer.entity.SongSheetBean;

import java.util.List;

public interface SongSheetService {
    boolean add(String name, String imgAddress);
    int delete(SongSheetBean songSheet);

    /**
     * 找出所有歌单，包括本地歌单
     * @return 歌单列表
     */
    List<SongSheetBean> findAll();

    /**
     * 找出歌单对应的歌曲
     * @param songSheetId 歌单id
     * @return  歌曲列表
     */
    List<SongBean> findSongBeanBySongSheetId(int songSheetId);

    boolean addSongBean(SongBean songBean, SongSheetBean songSheetBean);
}
