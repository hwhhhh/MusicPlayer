package com.hwhhhh.musicplayer.dto;

import com.hwhhhh.musicplayer.entity.SongBean;
import com.hwhhhh.musicplayer.entity.SongSheetBean;

import java.util.List;

/**
 * 歌曲和对应所处的歌单形成的复合类
 */
public class SongDto {
    private boolean isLocal;
    private SongSheetBean songSheetBean;
    private List<SongBean> songBeanList;

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public SongDto(SongSheetBean songSheetBean, List<SongBean> songBeanList) {
        this.songSheetBean = songSheetBean;
        this.songBeanList = songBeanList;
    }

    public SongDto(SongSheetBean songSheetBean, List<SongBean> songBeanList, boolean isLocal) {
        this.songSheetBean = songSheetBean;
        this.songBeanList = songBeanList;
        this.isLocal = isLocal;
    }

    public SongSheetBean getSongSheetBean() {
        return songSheetBean;
    }

    public void setSongSheetBean(SongSheetBean songSheetBean) {
        this.songSheetBean = songSheetBean;
    }

    public List<SongBean> getSongBeanList() {
        return songBeanList;
    }

    public void setSongBeanList(List<SongBean> songBeanList) {
        this.songBeanList = songBeanList;
    }
}
