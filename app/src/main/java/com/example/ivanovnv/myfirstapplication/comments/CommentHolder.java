package com.example.ivanovnv.myfirstapplication.comments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ivanovnv.myfirstapplication.R;

class CommentHolder extends RecyclerView.ViewHolder{

    private TextView mUserName;
    private TextView mText;
    private TextView mDate;

    public CommentHolder(View itemView) {
        super(itemView);

        mUserName = itemView.findViewById(R.id.tv_user);
        mText = itemView.findViewById(R.id.tv_text);
        mDate = itemView.findViewById(R.id.tv_timestamp);
    }

    public void bind(Comment comment) {
        if (comment != null) {
            mUserName.setText(comment.getAuthor());
            mText.setText(comment.getText());
            mDate.setText(comment.getTimestamp());
        }
    }
}
