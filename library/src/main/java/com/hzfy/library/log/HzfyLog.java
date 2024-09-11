package com.hzfy.library.log;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * 日志打印工具类
 */
public class HzfyLog {

    private static final String LOG_PACKAGE;

    static {
        String className = HzfyLog.class.getName();
        LOG_PACKAGE = className.substring(0, className.lastIndexOf('.') + 1);
    }

    public static void v(Object... content) {
        vt(null, content);
    }

    public static void vt(String tag, Object... content) {
        log(HzfyLogType.V, tag, content);
    }

    public static void d(Object... content) {
        dt(null, content);
    }

    public static void dt(String tag, Object... content) {
        log(HzfyLogType.D, tag, content);
    }

    public static void i(Object... content) {
        it(null, content);
    }

    public static void it(String tag, Object... content) {
        log(HzfyLogType.I, tag, content);
    }

    public static void w(Object... content) {
        wt(null, content);
    }

    public static void wt(String tag, Object... content) {
        log(HzfyLogType.W, tag, content);
    }

    public static void e(Object... content) {
        et(null, content);
    }

    public static void et(String tag, Object... content) {
        log(HzfyLogType.E, tag, content);
    }

    public static void a(Object... content) {
        at(null, content);
    }

    public static void at(String tag, Object... content) {
        log(HzfyLogType.A, tag, content);
    }

    public static void log(@HzfyLogType.TYPE int type, String tag, Object... contents) {
        log(HzfyLogManager.getInstance().getConfig(), type, tag, contents);
    }


    public static void log(HzfyLogConfig config, @HzfyLogType.TYPE int type, String tag, Object... contents) {
        if (config == null) {
            return;
        }
        if (!config.enable()) {
            return;
        }
        if (tag == null) {
            tag = config.getGlobalTag();
        }
        StringBuilder sb = new StringBuilder();
        if (config.includeThread()) {
            String threadInfo = HzfyLogConfig.sThreadFormatter.format(Thread.currentThread());
            sb.append(threadInfo).append("\n");
        }
        if (config.showStackTraceLevel() <= type && config.stackTraceDepth() > 0) {
            String stackTrace = HzfyLogConfig.sStackTraceFormatter.format(
                    HzfyStackTraceUtil.getCroppedRealStackTrack(new Throwable().getStackTrace(), LOG_PACKAGE, config.stackTraceDepth()));
            sb.append(stackTrace).append("\n");
        }
        String body = parseBody(config, contents);
        if (body != null) {//替换转义字符\
            body = body.replace("\\\"", "\"");
        }
        sb.append(body).append("\n");
        List<HzfyLogPrinter> printerList =
                config.printers() != null ? Arrays.asList(config.printers()) : HzfyLogManager.getInstance().getPrinterList();
        if (printerList == null) {
            return;
        }
        for (HzfyLogPrinter printer : printerList) {
            printer.print(config, type, tag, sb.toString());
        }
    }

    private static String parseBody(@NonNull HzfyLogConfig config, Object[] contents) {
        if (contents == null) {
            return "";
        }
        if (config.injectJsonParser() != null) {
            //只有一个数据且为String的情况下不再进行序列化
            if (contents.length == 1 && contents[0] instanceof String) {
                return (String) contents[0];
            }
            return config.injectJsonParser().toJson(contents);
        }
        StringBuilder sb = new StringBuilder();
        for (Object content : contents) {
            sb.append(content.toString()).append(";");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
