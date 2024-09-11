package com.hzfy.library.log;

import android.util.Log;

import androidx.annotation.NonNull;

import static com.hzfy.library.log.HzfyLogConfig.MAX_LEN;

public class HzfyConsolePrinter implements HzfyLogPrinter {
    @Override
    public void print(@NonNull HzfyLogConfig config, int level, String tag, @NonNull String content) {
        int len = content.length();
        int countOfSub = len / MAX_LEN;
        if (countOfSub > 0) {
            StringBuilder log = new StringBuilder();
            int index = 0;
            for (int i = 0; i < countOfSub; i++) {
                log.append(content.substring(index, index + MAX_LEN));
                index += MAX_LEN;
            }
            if (index != len) {
                log.append(content.substring(index, len));
            }
            Log.println(level, tag, log.toString());
        } else {
            Log.println(level, tag, content);
        }
    }
}
