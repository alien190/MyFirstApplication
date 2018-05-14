package com.example.ivanovnv.myfirstapplication.comments;

import java.util.List;

public class MessageData {
    private boolean mIsFirstLoad = false;
    private boolean mIsRefreshed = false;
    private boolean mIsAddComment = false;
    private boolean mIsLoadCommentError = false;

    private List<Comment> mComments;
    private int mNewLoadedCommentCount = 0;
    private int mAddedCommentId = -1;

    private CommentForPost mNewCommentForPost;

    public CommentForPost getNewCommentForPost() {
        return mNewCommentForPost;
    }

    public void setNewCommentForPost(CommentForPost mNewCommentForPost) {
        this.mNewCommentForPost = mNewCommentForPost;
    }

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

    public int getNewLoadedCommentCount() {
        return mNewLoadedCommentCount;
    }

    public void setNewLoadedCommentCount(int mNewCommentCount) {
        this.mNewLoadedCommentCount = mNewCommentCount;
    }

    public int getAddedCommentId() {
        return mAddedCommentId;
    }

    public void setAddedCommentId(int newCommentId) {
        this.mAddedCommentId = newCommentId;
    }

}
