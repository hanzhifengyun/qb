package com.hzfy.library.log;

public class HzfyThreadFormatter implements HzfyLogFormatter<Thread> {
    @Override
    public String format(Thread thread) {
        return "Thread: " + thread.getName();
    }
}
