package com.example.tiennguyen.thesis.util;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Quyen Hua on 10/28/2017.
 */

public class MyHandle extends Handler {

    HandleFooterView handleFooterView;
    public MyHandle(HandleFooterView handleFooterView) {
        this.handleFooterView = handleFooterView;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                handleFooterView.setFooterView(msg);
                break;
            case 1:
                handleFooterView.setMoreListView(msg);
                break;
            default:
                break;
        }
    }

    public interface HandleFooterView {
        void setFooterView(Message msg);
        void setMoreListView(Message msg);
    }
}

