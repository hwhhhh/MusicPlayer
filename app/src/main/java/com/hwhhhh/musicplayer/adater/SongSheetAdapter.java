package com.hwhhhh.musicplayer.adater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hwhhhh.musicplayer.R;
import com.hwhhhh.musicplayer.entity.SongSheet;

import java.util.ArrayList;
import java.util.List;

public class SongSheetAdapter extends BaseAdapter {
    private List<SongSheet> songSheetList;
    private Context mContext;

    public SongSheetAdapter(Context context, List<SongSheet> songSheetList) {
        this.mContext = context;
        this.songSheetList = songSheetList;
    }

    @Override
    public int getCount() {
        return songSheetList.size();
    }

    @Override
    public Object getItem(int i) {
        return songSheetList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SongSheet songSheet = (SongSheet) getItem(i);
        View contentView;
        ViewHolder viewHolder;
        if (view == null) {
            contentView = LayoutInflater.from(mContext).inflate(R.layout.item_songsheet, null);
            viewHolder = new ViewHolder();
            viewHolder.name = contentView.findViewById(R.id.item_songSheet_name);
            viewHolder.imageView = contentView.findViewById(R.id.item_songSheet_img);
            contentView.setTag(viewHolder);
        } else {
            contentView = view;
            viewHolder = (ViewHolder) contentView.getTag();
        }
        viewHolder.imageView.setImageResource(R.drawable.nabi);
        viewHolder.name.setText(songSheet.getName());
        return contentView;
    }

    /**
     * 局部刷新
     * @param listView 适配器对应的listView
     * @param position item的位置
     */

    public void notifyDataSetChanged(ListView listView, int position) {
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        View view = listView.getChildAt(position - firstVisiblePosition);
        getView(position, view, listView);
    }

    private class ViewHolder {
        TextView name;
        ImageView imageView;
    }
}
