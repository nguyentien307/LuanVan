package com.example.tiennguyen.thesis.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tiennguyen.thesis.R;
import com.example.tiennguyen.thesis.adapters.SongAdapter;
import com.example.tiennguyen.thesis.model.PersonItem;
import com.example.tiennguyen.thesis.model.SongItem;
import com.example.tiennguyen.thesis.service.PlayerService;

import java.util.ArrayList;

import co.mobiwise.library.InteractivePlayerView;
import co.mobiwise.library.OnActionClickedListener;

public class PlayingMusic extends AppCompatActivity implements OnActionClickedListener, View.OnClickListener,
        AdapterView.OnItemClickListener, Toolbar.OnMenuItemClickListener {

//    public static TextView musicTitle;
//    public static TextView artistName;
    public static ImageView icBack;
    public static ImageView icNext;
    public static ImageView icPrevious;
    public static ImageView controlPlayPause;
    public static ImageView icShuffle, icReplay;
    public static ImageView icNextward, icBackward;
    public static InteractivePlayerView interactivePlayerView;
    public static Toolbar toolbar;

    ImageView img_bg;

    ListView lvSongs;
    public static ArrayList<SongItem> songArr;

    Intent playerService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_music);

        initViews();
        setActionBar();
        createArray();
        playerService = new Intent(this, PlayerService.class);
        playerService.putExtra("songIndex", PlayerService.currentSongIndex);
        startService(playerService);
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setOnMenuItemClickListener(this);
    }

    private void createArray() {
        songArr = new ArrayList<>();
        ArrayList<PersonItem> artistArr = new ArrayList<>();
        artistArr.add(new PersonItem("Khánh Linh", "link", 1111));
        songArr.add(new SongItem("Nếu em được lựa chọn", artistArr, "http://mp3.zing.vn/html5/song/LnJnyZGNllELNELTtbmkH"));
        songArr.add(new SongItem("Hạnh Phúc Mong Manh", artistArr, "http://mp3.zing.vn/html5/song/ZmcntLHNXZvmWVLymyFHZmtkpkGkhBXXC"));
        songArr.add(new SongItem("Em Không Là Duy Nhất", artistArr, "http://mp3.zing.vn/html5/song/ZHcmtLnapxhDdCXtmyFmZHykWkGkCvdac"));
        songArr.add(new SongItem("Ta Còn Thuộc Về Nhau", artistArr, "http://mp3.zing.vn/html5/song/ZmxntLmNpJaCJHLtnyvnZmtZpLHZXbVCH"));
        SongAdapter adapter = new SongAdapter(this, R.layout.song_searched_item, songArr);
        lvSongs.setAdapter(adapter);
    }

    private void initViews() {
//        musicTitle = (TextView) findViewById(R.id.music_title);
//        artistName = (TextView) findViewById(R.id.music_artist_name);
        icBack = (ImageView) findViewById(R.id.btn_back);
        interactivePlayerView = (InteractivePlayerView) findViewById(R.id.interactivePlayerView);
        controlPlayPause = (ImageView) findViewById(R.id.control);
        icNext = (ImageView) findViewById(R.id.img_next);
        icPrevious = (ImageView) findViewById(R.id.img_previous);
        icShuffle = (ImageView) findViewById(R.id.btn_shuffle);
        icReplay = (ImageView) findViewById(R.id.btn_replay);
        icNextward = (ImageView) findViewById(R.id.btn_nextward);
        icBackward = (ImageView) findViewById(R.id.btn_backward);

        lvSongs = (ListView) findViewById(R.id.lv_list_song);

        img_bg = (ImageView) findViewById(R.id.img_bg);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bg_player);
        img_bg.startAnimation(animation);

        lvSongs.setOnItemClickListener(this);
        interactivePlayerView.setProgressLoadedColor(getResources().getColor(R.color.progress_bar));

        toolbar = (Toolbar) findViewById(R.id.toolbar_playing);

        icBack.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!PlayerService.mp.isPlaying()) {
            stopService(playerService);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActionClicked(int i) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_list_song:
                playerService = new Intent(this, PlayerService.class);
                playerService.putExtra("songIndex", position);
                startService(playerService);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                finish();
            default:
                break;
        }
        return false;
    }

    //Dialog xử lý tiến trình tải giữa PlayerService và MainActivity
//    private ProgressDialog pdBuff = null;
//    boolean mBufferBroadcastIsRegistered;
//    // Thiết lập BroadcastReceiver
//    private BroadcastReceiver broadcastBufferReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent bufferIntent) {
//            showPD(bufferIntent);
//        }
//    };

//    private void showPD(Intent bufferIntent) {
//        String bufferValue = bufferIntent.getStringExtra("buffering");
//        int bufferIntValue = Integer.parseInt(bufferValue);
//        //Nếu giá trị bufferIntValue bằng 1 thì cho dialog chạy
//        //Nếu giá trị bufferIntValue bằng 2 thì cho dismiss dialog
//        switch (bufferIntValue) {
//            case 0:
//                if (pdBuff != null) {
//                    pdBuff.dismiss();
//                }
//                break;
//            case 1:
//                BufferDialogue();
//                break;
//        }
//    }
//
//    private void BufferDialogue() {
//        pdBuff = ProgressDialog.show(this, "Vui lòng chờ...", "Đang tải dữ liệu...", true);
//        pdBuff.setCancelable(true);
//    }
//
//    @Override
//    protected void onResume() {
//        //Đăng ký broadcast receiver
//        if (!mBufferBroadcastIsRegistered) {
//            registerReceiver(broadcastBufferReceiver, new IntentFilter(PlayerService.BROADCAST_BUFFER));
//            mBufferBroadcastIsRegistered = true;
//        }
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        //Hủy đăng ký broadcast receiver
//        if (mBufferBroadcastIsRegistered) {
//            unregisterReceiver(broadcastBufferReceiver);
//            mBufferBroadcastIsRegistered = false;
//        }
//        super.onPause();
//    }
}
