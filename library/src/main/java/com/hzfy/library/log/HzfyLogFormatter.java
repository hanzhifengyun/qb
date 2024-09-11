package com.hzfy.library.log;

public interface HzfyLogFormatter<T> {
    String format(T data);
}
