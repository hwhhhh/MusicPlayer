package com.hwhhhh.musicplayer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.hwhhhh.musicplayer.ui.MainContentFragment;
import com.hwhhhh.musicplayer.ui.MainFragment;
import com.hwhhhh.musicplayer.ui.MusicInfoFragment;
import com.hwhhhh.musicplayer.ui.SongContentFragment;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.initialize(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initMainFragment(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//避免点击editText时，软键盘遮挡输入框
    }

    private void initMainFragment(Bundle bundle) {
        if (bundle == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction
                    .add(R.id.fragment_host, MainFragment.getInstance(), MainFragment.class.getName())
                    .add(R.id.fragment_host, MusicInfoFragment.getInstance(), MusicInfoFragment.class.getName())
                    .add(R.id.main_content, MainContentFragment.getInstance(), MainContentFragment.class.getName())
                    .hide(MusicInfoFragment.getInstance())
                    .commit();
        }
    }

    public void enterMusicInfoFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .hide(MainFragment.getInstance())
                .show(MusicInfoFragment.getInstance())
                .addToBackStack(null)
                .commit();
    }

    public void enterSongContentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(SongContentFragment.class.getName());
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .hide(MainContentFragment.getInstance());
        if (fragment == null) {
            fragmentTransaction
                    .add(R.id.main_content, SongContentFragment.getInstance(), SongContentFragment.class.getName());
        } else {
            fragmentTransaction
                    .show(fragment);
        }
        fragmentTransaction
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            Log.d(TAG, "onBackPressed: " + fragmentManager.getBackStackEntryCount());
            fragmentManager.popBackStack();
        }
    }
}
