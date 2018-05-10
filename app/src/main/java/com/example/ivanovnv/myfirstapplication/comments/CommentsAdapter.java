package com.example.ivanovnv.myfirstapplication.comments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ivanovnv.myfirstapplication.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter <CommentHolder> {
    private List<Comment> mComments = new ArrayList<>();

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.li_comment, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.bind(mComments.get(position));
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public void addData(List<Comment> data, boolean isRefreshed) throws Exception {
        if (isRefreshed) {
            mComments.clear();
        }
        mComments.addAll(modifyDate(data));
        notifyDataSetChanged();
    }

    List<Comment> modifyDate(List<Comment> comments) throws Exception{
        List<Comment> newComments = new ArrayList<>();
        for (Comment comment:comments) {
            DateFormat dfParse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
            Calendar date = Calendar.getInstance();
            date.setTime(dfParse.parse(comment.getTimestamp()));
            DateFormat dfOut;

             if(Calendar.getInstance().getTimeInMillis() - date.getTimeInMillis() > 86400000) {
                 dfOut = new SimpleDateFormat("dd.MM.yyyy");
             }
             else {
                 dfOut = new SimpleDateFormat("HH:mm:ss");
             }

            comment.setTimestamp(dfOut.format(date.getTime()));
            newComments.add(comment);
        }
        return newComments;
    }
}
