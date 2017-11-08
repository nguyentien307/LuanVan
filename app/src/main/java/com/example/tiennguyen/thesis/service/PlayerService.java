package com.example.tiennguyen.thesis.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.tiennguyen.thesis.MainActivity;
import com.example.tiennguyen.thesis.R;
import com.example.tiennguyen.thesis.activities.PlayingMusic;
import com.example.tiennguyen.thesis.model.SongItem;
import com.example.tiennguyen.thesis.util.Utilities;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

import co.mobiwise.library.InteractivePlayerView;
import co.mobiwise.library.OnActionClickedListener;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PlayerService extends Service implements MediaPlayer.OnCompletionListener, OnActionClickedListener,
        View.OnClickListener, View.OnTouchListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener {

//    private WeakReference<TextView> musicTitle, artistName;
    private WeakReference<InteractivePlayerView> interactivePlayerViewWeakReference;
    private WeakReference<ImageView> icNext, icPrevious, icControlPlayPause, icShuffle, icReplay, icNextward, icBackward;
    private WeakReference<Toolbar> toolbarWeakReference;
    private ArrayList<SongItem> songArr = new ArrayList<>();

    private Handler mHandler = new Handler();
    private Utilities utils;
    private int seekForwardTime = 5000; // 5 giây
    private int seekBackwardTime = 5000; // 5 giây
    public static MediaPlayer mp;
    public static int currentSongIndex = -1;
    public static int songindexForPause = 0;
    //Thành lập broadcast identifier and intent
    public static final String BROADCAST_BUFFER = "quyenhua0403";
    Intent bufferIntent;

    private boolean isPausedInCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;
    private static final String TAG = "TELEPHONESERVICE";

    private boolean isRepeat = false;
    private boolean isShuffle = false;

    //BroadCast Receiver sử dụng để lắng nge và phát sóng dữ liệu khi tai nge được cắm
    //Và nếu có cắm tai nge, thì dừng nhạc và dừng service
    private int headsetSwitch = 1;
    private BroadcastReceiver headsetReceiver = new BroadcastReceiver() {
        private boolean headsetConnected = false;
        @Override
        public void onReceive(Context context, Intent intent) {
            // Log.v(TAG, "ACTION_HEADSET_PLUG Intent received");
            if (intent.hasExtra("state")) {
                if (headsetConnected && intent.getIntExtra("state", 0) == 0) {
                    headsetConnected = false;
                    headsetSwitch = 0;
                } else if (!headsetConnected && intent.getIntExtra("state", 0) == 1) {
                    headsetConnected = true;
                    headsetSwitch = 1;
                }
            }
            switch (headsetSwitch) {
                case (0):
                    headsetDisconnected();
                    break;
                case (1):
                    break;
            }
        }
    };

    private void headsetDisconnected() {
        pauseMedia();
    }

    @Override
    public void onCreate() {
        mp = new MediaPlayer();
        mp.setOnCompletionListener(this);
        mp.setOnErrorListener(this);
        mp.setOnPreparedListener(this);
        mp.setOnBufferingUpdateListener(this);
        mp.setOnSeekCompleteListener(this);
        mp.setOnInfoListener(this);
        mp.reset();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);//
        utils = new Utilities();
        bufferIntent = new Intent(BROADCAST_BUFFER);
        //Đăng ký nhận tai nge
        registerReceiver(headsetReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
        songArr = PlayingMusic.songArr;
        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        initUI();
        int songIndex = intent.getIntExtra("songIndex", 0);
        interactivePlayerViewWeakReference.get().setMax(100);
        interactivePlayerViewWeakReference.get().setProgress(0);
        Log.v(TAG, String.valueOf(songIndex));
        if (songIndex != currentSongIndex) {
            playSong(songIndex);
//            initNotification(songIndex);
            currentSongIndex = songIndex;
        } else if (currentSongIndex != -1) {
            toolbarWeakReference.get().setTitle(songArr.get(currentSongIndex).getTitle());
            toolbarWeakReference.get().setSubtitle(songArr.get(currentSongIndex).getArtist().get(0).getName());
            if (mp.isPlaying())
                icControlPlayPause.get().setBackgroundResource(R.drawable.ic_action_pause);
            else
                icControlPlayPause.get().setBackgroundResource(R.drawable.ic_action_play);
        }

        //Nếu có cuộc gọi đến, tạm dừng máy nge nhạc, và resume khi ngắt kết nối cuộc gọi.
        // Get the telephony manager
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                // String stateString = "N/A";
                Log.v(TAG, "Starting CallStateChange");
                switch (state) {
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING://có cuộc gọi đến
                        if (mp != null) {
                            pauseMedia();
                            isPausedInCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE://kết thúc cuộc gọi
                        //Bắt đầu play nhac.
                        if (mp != null) {
                            if (isPausedInCall) {
                                isPausedInCall = false;
                                playMedia();
                            }
                        }
                        break;
                }
            }
        };
        //Đăng ký lắng nge từ việc quản lý điện thoại
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        super.onStart(intent, startId);
        return START_STICKY;
    }

    private void initUI() {
//        musicTitle = new WeakReference<>(PlayingMusic.musicTitle);
//        artistName = new WeakReference<>(PlayingMusic.artistName);

        interactivePlayerViewWeakReference = new WeakReference<>(PlayingMusic.interactivePlayerView);
        icNext = new WeakReference<>(PlayingMusic.icNext);
        icPrevious = new WeakReference<>(PlayingMusic.icPrevious);
        icControlPlayPause = new WeakReference<>(PlayingMusic.controlPlayPause);
        icShuffle = new WeakReference<>(PlayingMusic.icShuffle);
        icReplay = new WeakReference<>(PlayingMusic.icReplay);
        icNextward = new WeakReference<>(PlayingMusic.icNextward);
        icBackward = new WeakReference<>(PlayingMusic.icBackward);
        toolbarWeakReference = new WeakReference<>(PlayingMusic.toolbar);

        interactivePlayerViewWeakReference.get().setOnActionClickedListener(this);
        interactivePlayerViewWeakReference.get().setOnTouchListener(this);
        icNext.get().setOnClickListener(this);
        icPrevious.get().setOnClickListener(this);
        icControlPlayPause.get().setOnClickListener(this);
        icShuffle.get().setOnClickListener(this);
        icReplay.get().setOnClickListener(this);
        icNextward.get().setOnClickListener(this);
        icBackward.get().setOnClickListener(this);
    }

    //Gửi tin nhắn tới PlayingMusic rằng bài hát đã được lưu vào vùng nhớ đệm
    private void sendBufferingBroadcast() {
        // Log.v(TAG, "BufferStartedSent");
        bufferIntent.putExtra("buffering", "1");
        sendBroadcast(bufferIntent);
    }

    //Gửi tin nhắn tới MainActivity rằng bài hát đã sẵn sàn để play
    private void sendBufferCompleteBroadcast() {
        bufferIntent.putExtra("buffering", "0");
        sendBroadcast(bufferIntent);
    }

    private void playSong(int songIndex) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        try {
            mp.reset();
            mp.setDataSource(songArr.get(songIndex).getLink());
            //gửi tin nhắn đến MainActivity để hiển thị đồng bộ
//                sendBufferingBroadcast();
            mp.prepare();
            mp.start();
            toolbarWeakReference.get().setTitle(songArr.get(songIndex).getTitle());
            toolbarWeakReference.get().setSubtitle(songArr.get(songIndex).getArtist().get(0).getName());
            icControlPlayPause.get().setBackgroundResource(R.drawable.ic_action_pause);
            interactivePlayerViewWeakReference.get().setMax(mp.getDuration() / 1000);
            interactivePlayerViewWeakReference.get().setProgress(0);
            updateProgressBar();
//                interactivePlayerViewWeakReference.get().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Gọi sẵn sàn để phát lại bài hát.
    public void onPrepared(MediaPlayer mp) {
        //Gởi tin nhắn kết thúc
//        sendBufferCompleteBroadcast();
        playMedia();
    }

    //Thực hiện chơi nhạc của media
    public void playMedia() {
        if (!mp.isPlaying()) {
            mp.start();
            updateProgressBar();
        }
    }

    //Thực hiện tạm dừng media
    public void pauseMedia() {
        // Log.v(TAG, "Pause Media");
        if (mp.isPlaying()) {
            mp.pause();
        }

    }

    //Thực hiện dừng media
    public void stopMedia() {
        if (mp.isPlaying()) {
            mp.stop();
        }
    }

    //Cập nhật thời gian trên seekbar
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = 0;
            try {
                totalDuration = mp.getDuration();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            long currentDuration = 0;
            try {
                currentDuration = mp.getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            //Hiển thị tổng thời gian
//            songTotalDurationLabel.get().setText("" + utils.milliSecondsToTimer(totalDuration));
            //Hiển thị thời gian hoàn thành
//            songCurrentDurationLabel.get().setText("" + utils.milliSecondsToTimer(currentDuration));

            //Thực hiện việc cập nhật progress bar
//            int progress = (int) (utils.getProgressPercentage(currentDuration,
//                    totalDuration));
            // Log.d("Progress", ""+progress);
            interactivePlayerViewWeakReference.get().setProgress((int) (currentDuration / 1000));
            //Chạy lại sau 0,1s
            mHandler.postDelayed(this, 100);
        }
    };

//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//        //xóa tin nhắn xử lý cập nhật progress bar
//        mHandler.removeCallbacks(mUpdateTimeTask);
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//        mHandler.removeCallbacks(mUpdateTimeTask);
//        int totalDuration = mp.getDuration();
//        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
//        //Thực hiện về trước sau theo số giây
//        mp.seekTo(currentPosition);
//        //Cập nhật lại thời gian ProgressBar
//        updateProgressBar();
//    }

    // --------------------Push Notification
    // Set up the notification ID
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private void initNotification(int songIndex) {
        String ns = Context.NOTIFICATION_SERVICE;
        mNotificationManager = (NotificationManager) getSystemService(ns);
        int icon = R.drawable.ic_action_play;
        CharSequence tickerText = "Audio Book";
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        Context context = getApplicationContext();
        CharSequence songName = songArr.get(songIndex).getTitle();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
//        notification.setLatestEventInfo(context, songName, null, contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (isRepeat) {
            playSong(currentSongIndex);
        } else if (isShuffle) {
            Random rand = new Random();
            currentSongIndex = rand.nextInt(songArr.size() -1);
            playSong(currentSongIndex);
        } else {
            if (currentSongIndex < songArr.size() - 1) {
                playSong(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            } else {
                playSong(0);
                currentSongIndex = 0;
            }
        }
    }

    // Thua
    @Override
    public void onActionClicked(int i) {
        switch (i) {
            case 1:
                isShuffle = true;
                break;
            case 2:
                break;
            case 3:
                isRepeat = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.control:
                if (currentSongIndex != -1) {
                    if (mp.isPlaying()) {
                        if (mp != null) {
                            mp.pause();
//                            interactivePlayerViewWeakReference.get().stop();
                            icControlPlayPause.get().setBackgroundResource(R.drawable.ic_action_play);
                            Log.d("Player Service", "Pause");
                        }
                    } else {
                        // Resume song
                        if (mp != null) {
//                            interactivePlayerViewWeakReference.get().start();
                            mp.start();
                            icControlPlayPause.get().setBackgroundResource(R.drawable.ic_action_pause);
                            Log.d("Player Service", "Play");
                        }
                    }
                }
                break;
            case R.id.img_next:
                if (currentSongIndex < (songArr.size() - 1)) {
                    playSong(currentSongIndex + 1);
                    currentSongIndex = currentSongIndex + 1;
                } else {
                    // play first song
                    playSong(0);
                    currentSongIndex = 0;
                }
                break;
            case R.id.img_previous:
                if (currentSongIndex > 0) {
                    playSong(currentSongIndex - 1);
                    currentSongIndex = currentSongIndex - 1;
                } else {
                    // play last song
                    playSong(songArr.size() - 1);
                    currentSongIndex = songArr.size() - 1;
                }
                break;
            case R.id.btn_shuffle:
                if (isShuffle) {
                    isShuffle = false;
                    icShuffle.get().setImageResource(R.drawable.shuffle_unselected);
                } else {
                    isShuffle = true;
                    icShuffle.get().setImageResource(R.drawable.shuffle_selected);
                }
                break;
            case R.id.btn_replay:
                if (isRepeat) {
                    isRepeat = false;
                    icReplay.get().setImageResource(R.drawable.replay_unselected);
                } else {
                    isRepeat = true;
                    icReplay.get().setImageResource(R.drawable.replay_selected);
                }
                break;
            case R.id.btn_nextward:

                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if (currentPosition + seekForwardTime <= mp.getDuration()) {
                    // forward song
                    mp.seekTo(currentPosition + seekForwardTime);
                } else {
                    // forward to end position
                    mp.seekTo(mp.getDuration());
                }
                break;
            case R.id.btn_backward:
                // get current song position
                int currentPosition2 = mp.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if (currentPosition2 - seekBackwardTime >= 0) {
                    // forward song
                    mp.seekTo(currentPosition2 - seekBackwardTime);
                } else {
                    // backward to starting position
                    mp.seekTo(0);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        currentSongIndex = -1;
        mHandler.removeCallbacks(mUpdateTimeTask);
        Log.d("Player Service", "Player Service Stopped");
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.release();
        }

        if (phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener,
                    PhoneStateListener.LISTEN_NONE);
        }
        //Hủy đăng ký headsetReceiver
        unregisterReceiver(headsetReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }
}
