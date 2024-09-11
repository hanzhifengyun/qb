package com.hzfy.common.log;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hzfy.library.R;
import com.hzfy.library.log.HzfyLogConfig;
import com.hzfy.library.log.HzfyLogItem;
import com.hzfy.library.log.HzfyLogPrinter;
import com.hzfy.library.log.HzfyLogType;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class HzfyViewPrinter implements HzfyLogPrinter {
    private RecyclerView recyclerView;
    private LogAdapter adapter;
    private HzfyViewPrinterProvider viewProvider;

    public HzfyViewPrinter(Activity activity) {
        FrameLayout rootView = activity.findViewById(android.R.id.content);
        recyclerView = new RecyclerView(activity);
        adapter = new LogAdapter(LayoutInflater.from(recyclerView.getContext()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        viewProvider = new HzfyViewPrinterProvider(rootView, recyclerView);
    }

    /**
     * 获取ViewProvider，通过ViewProvider可以控制log视图的展示和隐藏
     *
     * @return ViewProvider
     */
    @NonNull
    public HzfyViewPrinterProvider getViewProvider() {
        return viewProvider;
    }

    @Override
    public void print(@NonNull HzfyLogConfig config, int level, String tag, @NonNull String printString) {

        // 将log展示添加到recycleView
        adapter.addItem(new HzfyLogItem(System.currentTimeMillis(), level, tag, printString));

        // 滚动到对应的位置
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    private static class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {

        private LayoutInflater inflater;

        private List<HzfyLogItem> logs = new ArrayList<>();

        LogAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        void addItem(HzfyLogItem logItem) {
            logs.add(logItem);
            notifyItemInserted(logs.size() - 1);
        }

        @NonNull
        @Override
        public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.hzfy_log_item, parent, false);
            return new LogViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(LogViewHolder holder, int position) {
            HzfyLogItem logItem = logs.get(position);

            int color = getHighlightColor(logItem.level);
            holder.tagView.setTextColor(color);
            holder.messageView.setTextColor(color);

            holder.tagView.setText(logItem.getFlattened());
            holder.messageView.setText(logItem.log);
        }

        /**
         * 跟进log级别获取不同的高了颜色
         *
         * @param logLevel log 级别
         * @return 高亮的颜色
         */
        private int getHighlightColor(int logLevel) {
            int highlight;
            switch (logLevel) {
                case HzfyLogType.V:
                    highlight = 0xffbbbbbb;
                    break;
                case HzfyLogType.D:
                    highlight = 0xffffffff;
                    break;
                case HzfyLogType.I:
                    highlight = 0xff6a8759;
                    break;
                case HzfyLogType.W:
                    highlight = 0xffbbb529;
                    break;
                case HzfyLogType.E:
                    highlight = 0xffff6b68;
                    break;
                default:
                    highlight = 0xffffff00;
                    break;
            }
            return highlight;
        }

        @Override
        public int getItemCount() {
            return logs.size();
        }
    }


    private static class LogViewHolder extends RecyclerView.ViewHolder {

        TextView tagView;
        TextView messageView;

        LogViewHolder(View itemView) {
            super(itemView);
            tagView = itemView.findViewById(R.id.tag);
            messageView = itemView.findViewById(R.id.message);
        }
    }
}
