package com.hwhhhh.musicplayer.adater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.hwhhhh.musicplayer.R;
import com.hwhhhh.musicplayer.Service.SongSheetService;
import com.hwhhhh.musicplayer.ServiceImpl.SongSheetServiceImpl;
import com.hwhhhh.musicplayer.entity.SongSheetBean;

import java.util.List;

public class SongSheetAdapter extends BaseAdapter {
    private static final String TAG = "SongSheetAdapter";
    private List<SongSheetBean> songSheetList;
    private Context mContext;
    private SongSheetService songSheetService;

    public SongSheetAdapter(Context context, List<SongSheetBean> songSheetList) {
        this.mContext = context;
        this.songSheetList = songSheetList;
        this.songSheetService = new SongSheetServiceImpl();
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
        SongSheetBean songSheet = (SongSheetBean) getItem(i);
        View contentView;
        final ViewHolder viewHolder;
        if (view == null) {
            contentView = LayoutInflater.from(mContext).inflate(R.layout.item_songsheet, null);
            viewHolder = new ViewHolder();
            viewHolder.name = contentView.findViewById(R.id.item_songSheet_name);
            viewHolder.imageView = contentView.findViewById(R.id.item_songSheet_img);
            viewHolder.menu = contentView.findViewById(R.id.item_songSheet_menu);
            contentView.setTag(viewHolder);
        } else {
            contentView = view;
            viewHolder = (ViewHolder) contentView.getTag();
            if (i == 0) {
                viewHolder.menu.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.menu.setVisibility(View.VISIBLE);
            }
        }
        viewHolder.imageView.setImageResource(R.drawable.nabi);
        viewHolder.name.setText(songSheet.getName());
        viewHolder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        showPopMenu(view);
                    }
                });
            }
        });
        viewHolder.menu.setTag(getItem(i));
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
        ImageView menu;
    }

    private void showPopMenu(final View view) {
        final SongSheetAdapter songSheetAdapter = this;
        final SongSheetBean songSheet = (SongSheetBean) view.getTag();
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        popupMenu.getMenuInflater().inflate(R.menu.song_sheet_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                songSheetService.delete(songSheet);
                songSheetList.remove(songSheet);
                songSheetAdapter.notifyDataSetChanged();
                return false; 
            }
        });
        popupMenu.show();
    }
}
