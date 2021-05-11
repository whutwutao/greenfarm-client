package com.example.greenfarm.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends BaseAdapter {
    List<Message> messageList;
    LayoutInflater inflater;
    Context context;

    public MessageAdapter(Context context, List<Message> list) {
        this.messageList = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getIsMeSend() == 1) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int i) {
        return messageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Message mMessage = messageList.get(i);
        String content = mMessage.getContent();
        String time = formatTime(mMessage.getTime());
        int isMeSend = mMessage.getIsMeSend();
        int isRead = mMessage.getIsRead();
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            if (isMeSend == 0) {//对方发送的消息
                view = inflater.inflate(R.layout.item_chat_receive_text, viewGroup, false);
                holder.tv_content = view.findViewById(R.id.tv_content);
                holder.tv_sendtime = view.findViewById(R.id.tv_sendtime);
                holder.tv_display_name = view.findViewById(R.id.tv_display_name);
            } else {
                view = inflater.inflate(R.layout.item_chat_send_text, viewGroup, false);
                holder.tv_content = view.findViewById(R.id.tv_content);
                holder.tv_sendtime = view.findViewById(R.id.tv_sendtime);
                holder.tv_isRead = view.findViewById(R.id.tv_isRead);
            }
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tv_sendtime.setText(time);
        holder.tv_content.setVisibility(View.VISIBLE);
        holder.tv_content.setText(content);

        //如果是自己发送才显示未读已读
        if (isMeSend == 1) {
            if (isRead == 0) {
                holder.tv_isRead.setText("未读");
                holder.tv_isRead.setTextColor(context.getResources().getColor(R.color.jmui_jpush_blue));
            } else if (isRead == 1) {
                holder.tv_isRead.setText("已读");
                holder.tv_isRead.setTextColor(Color.GRAY);
            } else {
                holder.tv_isRead.setText("");
            }
        }else{
//            holder.tv_display_name.setVisibility(View.VISIBLE);
//            holder.tv_display_name.setText("服务器");
        }

        return view;
    }


    class ViewHolder {
        private TextView tv_content, tv_sendtime, tv_display_name, tv_isRead;
    }

    /**
     * 将毫秒数转为日期格式
     *
     * @param timeMillis
     * @return
     */
    private String formatTime(String timeMillis) {
        long timeMillisl=Long.parseLong(timeMillis);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMillisl);
        return simpleDateFormat.format(date);
    }
}
