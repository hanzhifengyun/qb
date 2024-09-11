package com.hzfy.library.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

public class HzfyLogManager {
    private HzfyLogConfig logConfig;

    private static HzfyLogManager instance;

    private List<HzfyLogPrinter> printerList = new ArrayList<>();

    private HzfyLogManager(HzfyLogConfig config, HzfyLogPrinter[] printers) {
        logConfig = config;
        printerList.addAll(Arrays.asList(printers));
    }

    public static HzfyLogManager getInstance() {
        if (instance == null) {
            throw new NullPointerException("you must call init before use manager");
        }
        return instance;
    }

    public static void init(@NonNull HzfyLogConfig config, HzfyLogPrinter... printers) {
        if (config == null) {
            throw new NullPointerException("config must not be null");
        }
        instance = new HzfyLogManager(config, printers);
    }

    public HzfyLogConfig getConfig() {
        return logConfig;
    }

    public HzfyLogConfig getLogConfig() {
        return logConfig;
    }

    public List<HzfyLogPrinter> getPrinterList() {
        return printerList;
    }


    public void addPrinter(HzfyLogPrinter printer) {
        if (printerList != null) {
            printerList.add(printer);
        }
    }

    public void removePrinter(HzfyLogPrinter printer) {
        if (printerList != null) {
            printerList.remove(printer);
        }
    }
}
