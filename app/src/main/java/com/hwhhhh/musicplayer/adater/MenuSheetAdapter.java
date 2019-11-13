package com.hwhhhh.musicplayer.adater;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hwhhhh.musicplayer.R;
import com.hwhhhh.musicplayer.entity.SongSheetBean;

import org.litepal.LitePal;

import java.util.List;

public class MenuSheetAdapter extends BaseAdapter {
    private static final String TAG = "MenuSheetAdapter";
    private List<SongSheetBean> songSheetBeanList;
    private Context mContext;
    public MenuSheetAdapter(Context context) {
        songSheetBeanList = LitePal.where("id != 1").find(SongSheetBean.class);
        mContext = context;
    }
    @Override
    public int getCount() {
        return songSheetBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return songSheetBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SongSheetBean songSheetBean = (SongSheetBean) getItem(i);
        String name = songSheetBean.getName();
        ViewHolder viewHolder;
        View contentView;
        if (view == null) {
            contentView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_list, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = contentView.findViewById(R.id.item_dialog_name);
            contentView.setTag(viewHolder);
        } else {
            contentView = view;
            viewHolder = (ViewHolder) contentView.getTag();
        }
        viewHolder.textView.setText(name);
        return contentView;
    }

    private class ViewHolder {
        TextView textView;
    }
}
