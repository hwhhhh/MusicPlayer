package com.hwhhhh.musicplayer.ServiceImpl;

import com.hwhhhh.musicplayer.Service.SongSheetService;
import com.hwhhhh.musicplayer.entity.SongSheet;

import org.litepal.LitePal;

import java.util.List;

public class SongSheetServiceImpl implements SongSheetService {
    @Override
    public boolean add(String name, String imgAddress) {
        SongSheet songSheet = new SongSheet(name, imgAddress);
        return songSheet.save();
    }

    @Override
    public int delete(SongSheet songSheet) {
        return songSheet.delete();
    }

    @Override
    public List<SongSheet> findAll() {
        return LitePal.findAll(SongSheet.class);
    }

    @Override
    public SongSheet findLast() {
        return LitePal.findLast(SongSheet.class);
    }
}
