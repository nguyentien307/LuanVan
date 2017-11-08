package com.example.tiennguyen.thesis.util;

/**
 * Created by Quyen Hua on 11/7/2017.
 */

public class Utilities {
    public static String convertDuration(long duration){
        long minutes = (duration/1000)/60;
        long seconds = (duration/1000)%60;
        String converted = String.format("%d:%02d", minutes, seconds);
        return converted;
    }

    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        //Chuyển đổi thời gian sang định dạng Hours:Minutes:Seconds
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        //Thêm giờ nếu cóư

        String convert = String.format("%d:%02d", minutes, seconds);

        //kết quả trả về
        return convert;
    }

    public int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        //Tính toán tỉ lệ %
        percentage =(((double)currentSeconds)/totalSeconds)*100;

        //trả về giá trị %
        return percentage.intValue();
    }

    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        //trả về thời gian hiện tại trong mili giây
        return currentDuration * 1000;
    }
}
