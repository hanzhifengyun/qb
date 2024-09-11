package com.hzfy.library.log;

public class HzfyStackTraceFormatter implements HzfyLogFormatter<StackTraceElement[]> {
    @Override
    public String format(StackTraceElement[] elements) {
        StringBuilder sb = new StringBuilder(128);
        if (elements == null || elements.length == 0) {
            return null;
        }
        if (elements.length == 1) {
            return "\t- "+ elements[0].toString();
        }
        int len = elements.length;
        for (int i = 0; i < len; i++) {
            if (i == 0) {
                sb.append("stackTrace: \n");
            }
            if (i != len - 1) {
                sb.append("\t├ ");
                sb.append(elements[i].toString());
                sb.append("\n");
            } else {
                sb.append("\t└ ");
                sb.append(elements[i].toString());
            }
        }

        return sb.toString();
    }
}
