package com.hwhhhh.musicplayer.Service;

import com.hwhhhh.musicplayer.entity.SongSheet;

import java.util.List;

public interface SongSheetService {
    boolean add(String name, String imgAddress);
    int delete(SongSheet songSheet);
    List<SongSheet> findAll();
    SongSheet findLast();
}
