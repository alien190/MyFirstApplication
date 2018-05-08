package com.example.ivanovnv.myfirstapplication.comments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ivanovnv.myfirstapplication.R;

import java.util.ArrayList;
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

    public void addData(List<Comment> data, boolean isRefreshed) {
        if (isRefreshed) {
            mComments.clear();
        }
        mComments.addAll(data);
        notifyDataSetChanged();
    }
}
