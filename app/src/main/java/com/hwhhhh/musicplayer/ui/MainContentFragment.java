package com.hwhhhh.musicplayer.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hwhhhh.musicplayer.MainActivity;
import com.hwhhhh.musicplayer.R;
import com.hwhhhh.musicplayer.Service.SongSheetService;
import com.hwhhhh.musicplayer.ServiceImpl.SongSheetServiceImpl;
import com.hwhhhh.musicplayer.adater.SongSheetAdapter;
import com.hwhhhh.musicplayer.dto.SongDto;
import com.hwhhhh.musicplayer.entity.SongBean;
import com.hwhhhh.musicplayer.entity.SongSheetBean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MainContentFragment extends Fragment {
    private static final String TAG = "MainContentFragment";

    private View view;
    private static MainContentFragment mainContentFragment;
    private SongSheetService songSheetService;

    public static MainContentFragment getInstance() {
        if (mainContentFragment == null) {
            synchronized (MainContentFragment.class) {
                if (mainContentFragment == null) {
                    mainContentFragment = new MainContentFragment();
                }
            }
        }
        return mainContentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_content, container, false);
        songSheetService = new SongSheetServiceImpl();
        initView();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Log.d(TAG, "onHiddenChanged: show");
            initView();
        }
    }

    private void initView() {

        //歌单
        ListView listView = view.findViewById(R.id.main_listView_songSheet);
        final List<SongSheetBean> data = songSheetService.findAll();
        final SongSheetAdapter songSheetAdapter = new SongSheetAdapter(getContext(), data);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_create_sheet, null);
        listView.setAdapter(songSheetAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    SongSheetBean songSheetBean = (SongSheetBean) adapterView.getItemAtPosition(i);
                    List<SongBean> songBeanList = songSheetService.findSongBeanBySongSheetId(songSheetBean.getId());
                    if (i!=0) {
                        EventBus.getDefault().postSticky(new SongDto(songSheetBean, songBeanList));
                    } else {
                        EventBus.getDefault().postSticky(new SongDto(songSheetBean, songBeanList, true));
                    }
                    mainActivity.enterSongContentFragment();
                }
            }
        });
        ImageView imageView = view.findViewById(R.id.main_add);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView cancel = view1.findViewById(R.id.dialog_cancel);
                TextView sure = view1.findViewById(R.id.dialog_sure);
                final EditText editText = view1.findViewById(R.id.dialog_ed);
                final Dialog dialog = builder.create();
                dialog.show();
                if (dialog.getWindow() != null) {
                    ViewGroup viewGroup = (ViewGroup) view1.getParent();
                    if (viewGroup != null) {
                        viewGroup.removeView(view1);
                        Log.d(TAG, "onClick: parent != null");
                    }
                    dialog.getWindow().setContentView(view1);
                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                }
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (songSheetService.add(editText.getText().toString().trim(), null)) {
                            data.add(new SongSheetBean(editText.getText().toString().trim(), null));
                            songSheetAdapter.notifyDataSetChanged();
                        }
                        dialog.dismiss();
                    }
                });

            }
        });

    }

}
