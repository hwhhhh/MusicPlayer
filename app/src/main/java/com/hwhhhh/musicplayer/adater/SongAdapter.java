package com.hwhhhh.musicplayer.adater;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.hwhhhh.musicplayer.R;
import com.hwhhhh.musicplayer.Service.SongSheetService;
import com.hwhhhh.musicplayer.ServiceImpl.SongSheetServiceImpl;
import com.hwhhhh.musicplayer.dto.SongDto;
import com.hwhhhh.musicplayer.entity.SongBean;
import com.hwhhhh.musicplayer.entity.SongSheetBean;

import org.litepal.LitePal;

import java.util.List;

public class SongAdapter extends BaseAdapter {
    private static final String TAG = "SongAdapter";
    private SongDto songDto;
    private Context mContext;

    public SongAdapter(Context context, SongDto songDto){
        this.songDto = songDto;
        this.mContext = context;
    }

    public SongAdapter(Context context){
        this.songDto = new SongDto(LitePal.findFirst(SongSheetBean.class),LitePal.findAll(SongBean.class));
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return songDto.getSongBeanList().size();
    }

    @Override
    public Object getItem(int i) {
        return songDto.getSongBeanList().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SongBean songBean = (SongBean) getItem(i);
        View contentView;
        ViewHolder viewHolder;
        if (view == null) {
            contentView = LayoutInflater.from(mContext).inflate(R.layout.item_song, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = contentView.findViewById(R.id.item_song_info);
            viewHolder.menu = contentView.findViewById(R.id.item_song_menu);
            contentView.setTag(viewHolder);
        } else {
            contentView = view;
            viewHolder = (ViewHolder) contentView.getTag();
        }
        viewHolder.textView.setText(songBean.getName());
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
        viewHolder.menu.setTag(songBean);
        return contentView;
    }

    private class ViewHolder {
        private TextView textView;
        private ImageView menu;
    }

    private void showPopMenu(final View view) {
        final View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_addtosheet, null);
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        final SongBean songBean = (SongBean) view.getTag();
        final SongAdapter songAdapter = this;
        popupMenu.getMenuInflater().inflate(R.menu.song_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_song_addToSheet:
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        final Dialog dialog = builder.create();
                        ListView listView = v.findViewById(R.id.dialog_list);
                        listView.setAdapter(new MenuSheetAdapter(mContext));
                        dialog.show();
                        if (dialog.getWindow() != null) {
                            dialog.getWindow().setContentView(v);
                        }
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                SongSheetBean songSheetBean = (SongSheetBean) adapterView.getItemAtPosition(i);
                                SongSheetService songSheetService = new SongSheetServiceImpl();
                                if (songSheetService.addSongBean(songBean, songSheetBean)) {
                                    Toast.makeText(mContext, "添加成功！", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "添加失败！", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });
                        break;
                    case R.id.menu_song_delete:
                        if (songBean.getSongSheetId() != 1) {
                            songBean.setSongSheetId(1);
                            songBean.save();
                            Toast.makeText(mContext, "删除成功！", Toast.LENGTH_SHORT).show();
                            songDto.getSongBeanList().remove(songBean);
                            songAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(mContext, "删除失败！为本地音乐！", Toast.LENGTH_SHORT).show();
                        }
                }
                return false;
            }
        });
        popupMenu.show();
    }

}
