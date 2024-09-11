package com.hzfy.library.log;

public abstract class HzfyLogConfig {
    static int MAX_LEN = 512;

    static HzfyThreadFormatter sThreadFormatter = new HzfyThreadFormatter();
    static HzfyStackTraceFormatter sStackTraceFormatter = new HzfyStackTraceFormatter();

    public JsonParser injectJsonParser() {
        return null;
    }


    public String getGlobalTag() {
        return "HzfyLog";
    }

    public boolean enable() {
        return true;
    }


    public boolean includeThread() {
        return true;
    }

    public int stackTraceDepth() {
        return 5;
    }

    public int showStackTraceLevel(){
        return HzfyLogType.W;
    }

    public HzfyLogPrinter[] printers() {
        return null;
    }


    public interface JsonParser {
        String toJson(Object content);
    }
}
