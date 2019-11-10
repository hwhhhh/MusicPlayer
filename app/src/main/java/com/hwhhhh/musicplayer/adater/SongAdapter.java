package com.hwhhhh.musicplayer.adater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hwhhhh.musicplayer.R;

public class SongAdapter extends BaseAdapter {
    private String[] songNames;
    private Context mContext;

    public SongAdapter(Context context, String[] songNames){
        this.songNames = songNames;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return songNames.length;
    }

    @Override
    public Object getItem(int i) {
        return songNames[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String songName = (String) getItem(i);
        View contentView;
        ViewHolder viewHolder;
        if (view == null) {
            contentView = LayoutInflater.from(mContext).inflate(R.layout.item_song, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = contentView.findViewById(R.id.item_song_info);
            contentView.setTag(viewHolder);
        } else {
            contentView = view;
            viewHolder = (ViewHolder) contentView.getTag();
        }
        viewHolder.textView.setText(songName);
        return contentView;
    }

    private class ViewHolder {
        private TextView textView;
    }
}
