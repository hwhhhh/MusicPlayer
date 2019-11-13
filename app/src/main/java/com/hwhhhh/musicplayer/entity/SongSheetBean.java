package com.hwhhhh.musicplayer.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class SongSheetBean extends LitePalSupport {
    private int id;
    @Column(unique = true)
    private String name;
    private String imgAddress;

    public SongSheetBean(String name, String imgAddress) {
        this.name = name;
        this.imgAddress = imgAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgAddress() {
        return imgAddress;
    }

    public void setImgAddress(String imgAddress) {
        this.imgAddress = imgAddress;
    }
}
