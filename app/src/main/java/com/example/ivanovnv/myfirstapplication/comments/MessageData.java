package com.example.ivanovnv.myfirstapplication.comments;

import java.util.List;

public class MessageData {
    private boolean mIsFirstLoad = false;
    private boolean mIsRefreshed = false;
    private boolean mIsAddComment = false;
    private boolean mIsLoadCommentError = false;

    private List<Comment> mComments;
    private int mNewCommentCount = 0;
    private int mNewCommentId = -1;
    private int mOldCommentCount = 0;


    public boolean isLoadCommentError() {
        return mIsLoadCommentError;
    }

    public void setIsLoadCommentError() {
        this.mIsLoadCommentError = true;
    }

    public boolean isFirstLoad() {
        return mIsFirstLoad;
    }

    public void setIsFirstLoad() {
        this.mIsFirstLoad = true;
    }

    public boolean isRefreshed() {
        return mIsRefreshed;
    }

    public void setIsRefreshed() {
        this.mIsRefreshed = true;
    }

    public boolean isAddComment() {
        return mIsAddComment;
    }

    public void setIsAddComment() {
        this.mIsAddComment = true;
    }

    public List<Comment> getComments() {
        return mComments;
    }

    public void setComments(List<Comment> comments) {
        this.mComments = comments;
    }

    public int getNewCommentCount() {
        return mNewCommentCount;
    }

    public void setNewCommentCount(int mNewCommentCount) {
        this.mNewCommentCount = mNewCommentCount;
    }

    public int getNewCommentId() {
        return mNewCommentId;
    }

    public void setNewCommentId(int newCommentId) {
        this.mNewCommentId = newCommentId;
    }

    public int getOldCommentCount() {
        return mOldCommentCount;
    }

    public void setOldCommentCount(int mOldCommentCount) {
        this.mOldCommentCount = mOldCommentCount;
    }
}
