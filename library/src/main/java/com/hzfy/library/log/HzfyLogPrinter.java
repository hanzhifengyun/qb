package com.hzfy.library.log;

import androidx.annotation.NonNull;

public interface HzfyLogPrinter {
    void print(@NonNull HzfyLogConfig config, int level, String tag, @NonNull String content);
}
