package com.example.ivanovnv.myfirstapplication.comments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ivanovnv.myfirstapplication.ApiUtils;
import com.example.ivanovnv.myfirstapplication.R;
import com.example.ivanovnv.myfirstapplication.model.Album;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class CommentsFragment extends Fragment {

    private static final String ALBUM_KEY = "ALBUM_KEY";
    private static final String TAG = CommentsFragment.class.getSimpleName();

    private CommentsAdapter mCommentsAdapter;
    private RecyclerView mRecyclerView;
    private Album mAlbum;
    private SwipeRefreshLayout mRefresher;
    private ImageButton mSendButton;
    private LinearLayout mErrorLayout;
    private LinearLayout mNoCommentLayout;
    private EditText mNewCommentEditText;

    private Observable<MessageData> mSendButtonObservable;
    private Observable<MessageData> mRefreshObservable;
    private Observable<MessageData> mEditorActionObservable;
    private Observable<MessageData> mKeyObservable;


    public static CommentsFragment newInstance(Album album) {

        Bundle args = new Bundle();

        args.putSerializable(ALBUM_KEY, album);

        CommentsFragment fragment = new CommentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.comments_title));

        mRecyclerView = view.findViewById(R.id.recycler_comments);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommentsAdapter = new CommentsAdapter();
        mRecyclerView.setAdapter(mCommentsAdapter);

        mRefresher = view.findViewById(R.id.sr_comments);
        mSendButton = view.findViewById(R.id.bt_new_comment);
        mErrorLayout = view.findViewById(R.id.errorView);
        mNoCommentLayout = view.findViewById(R.id.no_comment_view);
        mNewCommentEditText = view.findViewById(R.id.et_new_comment);

        mAlbum = (Album) getArguments().getSerializable(ALBUM_KEY);

        createNewObservers(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void showToast(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show());
    }


    Observer<MessageData> observer = new Observer<MessageData>() {
        @Override
        public void onSubscribe(Disposable d) {
        }

        @Override
        public void onNext(MessageData messageData) {

            if (messageData.isRefreshed()) {
                if (messageData.getNewLoadedCommentCount() == 0) {
                    Toast.makeText(getActivity(), R.string.no_new_comments, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.add_new_comm) + messageData.getNewLoadedCommentCount(), Toast.LENGTH_SHORT).show();
                }
            }

            if (messageData.getComments().size() != 0) {
                mNoCommentLayout.setVisibility(View.GONE);
                mErrorLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mRecyclerView.scrollToPosition(mCommentsAdapter.getItemCount() - 1);
            } else {
                mErrorLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                mNoCommentLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            mRecyclerView.setVisibility(View.GONE);
            mNoCommentLayout.setVisibility(View.GONE);
            mErrorLayout.setVisibility(View.VISIBLE);

            createNewObservers(false);
        }

        @Override
        public void onComplete() {

        }
    };


    private void createNewObservers(boolean firstTime) {

        MessageData messageDataFirstLoad = new MessageData();
        if (firstTime) messageDataFirstLoad.setIsFirstLoad();
        Observable<MessageData> FirstLoadObservable = Observable.just(messageDataFirstLoad);


        mRefreshObservable = Observable.create(emitter -> mRefresher.setOnRefreshListener(() -> {
            MessageData messageData = new MessageData();
            messageData.setIsRefreshed();
            emitter.onNext(messageData);
        }));

        mNewCommentEditText.setImeActionLabel("Send", KeyEvent.KEYCODE_ENTER);

        mSendButtonObservable = Observable.create((ObservableOnSubscribe<MessageData>) emitter ->
                mSendButton.setOnClickListener(v -> {
                            MessageData messageData = createNewCommentToPost();
                            if (messageData != null) emitter.onNext(messageData);
                        }
                )).flatMapSingle(postComment);

        mEditorActionObservable = Observable.create((ObservableOnSubscribe<MessageData>) emitter ->
                mNewCommentEditText.setOnEditorActionListener((v, actionId, event) -> {
                    if (actionId == KeyEvent.KEYCODE_ENTER) {
                        MessageData messageData = createNewCommentToPost();
                        if (messageData != null) emitter.onNext(messageData);
                        return true;
                    }
                    return false;
                })).flatMapSingle(postComment);

        mKeyObservable = Observable.create((ObservableOnSubscribe<MessageData>) emitter ->
                mNewCommentEditText.setOnKeyListener((v, keyCode, event) -> {
                    if (event.getAction() == KeyEvent.ACTION_DOWN &&
                            (keyCode == KeyEvent.KEYCODE_ENTER ||
                                    keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER)) {
                        MessageData messageData = createNewCommentToPost();
                        if (messageData != null) emitter.onNext(messageData);
                        return true;
                    }
                    return false;
                })).flatMapSingle(postComment);


        Predicate<MessageData> filterMessage = messageData -> {
            if (messageData.isRefreshed()) return true;
            if (messageData.isFirstLoad()) return true;
            if (messageData.isAddComment()) {
                if (messageData.getAddedCommentId() == -1) mRefresher.setRefreshing(false);
                return messageData.getAddedCommentId() != -1;
            }
            return false;
        };

        mRefreshObservable
                .mergeWith(FirstLoadObservable)
                .mergeWith(mSendButtonObservable)
                .mergeWith(mEditorActionObservable)
                .mergeWith(mKeyObservable)
                .filter(filterMessage)
                .flatMapSingle(getComments)
                .flatMap(mCommentsAdapter.updateComments)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    Function<MessageData, Single<MessageData>> getComments = new Function<MessageData, Single<MessageData>>() {
        @Override
        public Single<MessageData> apply(MessageData messageData) throws Exception {

            Single<List<Comment>> comments;

            if (messageData.isRefreshed() || messageData.isFirstLoad()) {
                comments = ApiUtils.getApi().getAlbumComments(mAlbum.getId());
            } else {
                comments = ApiUtils.getApi().getComment(messageData.getAddedCommentId()).map(comment -> new ArrayList<Comment>() {{
                    add(comment);
                }});
            }

            return comments
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> Log.d(TAG, "getComments.doOnError thread id: " + Thread.currentThread().getId()))
                    .onErrorReturn(throwable -> {
                        Log.d(TAG, "getComments.onErrorReturn thread id: " + Thread.currentThread().getId());
                        if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {
                            showToast(getString(R.string.error_load_from_server_net));
                        } else
                            showToast(getString(R.string.error_load_from_server_unk));
                        return null;
                    })
                    .doOnSubscribe(disposable -> {
                        if (!mRefresher.isRefreshing()) mRefresher.setRefreshing(true);
                    })
                    .doFinally(() -> mRefresher.setRefreshing(false))
                    .flatMap((Function<List<Comment>, Single<MessageData>>) loadedComments -> {
                        Log.d(TAG, "getComments.flatmap thread id: " + Thread.currentThread().getId());
                        messageData.setComments(loadedComments);
                        return Single.just(messageData);
                    });
        }
    };

    Function<MessageData, SingleSource<MessageData>> postComment = new Function<MessageData, SingleSource<MessageData>>() {
        @Override
        public SingleSource<MessageData> apply(MessageData messageData) throws Exception {

            return ApiUtils.getApi().postComment(messageData.getNewCommentForPost())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn(throwable -> {
                        Log.d(TAG, "postComment.onErrorReturn thread id: " + Thread.currentThread().getId());
                        if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {
                            showToast(getString(R.string.err_send_comment_net));

                        } else {
                            showToast(getString(R.string.err_send_comment_unk));
                        }
                        return new CommentId(-1);
                    })
                    .flatMap((Function<CommentId, SingleSource<MessageData>>) commentId -> {
                        messageData.setIsAddComment();
                        messageData.setAddedCommentId(commentId.getId());
                        if (commentId.getId() != -1) {
                            mNewCommentEditText.setText("");
                        }
                        Log.d(TAG, "postComment.flatmap thread id: " + Thread.currentThread().getId());
                        return Single.just(messageData);
                    })
                    .doOnSubscribe(disposable -> mRefresher.setRefreshing(true));

        }
    };

    private MessageData createNewCommentToPost() {
        if (!mNewCommentEditText.getText().toString().isEmpty()) {
            MessageData messageData = new MessageData();
            messageData.setIsAddComment();
            messageData.setNewCommentForPost(new CommentForPost(mAlbum.getId(), mNewCommentEditText.getText().toString()));
            return messageData;
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_text_for_send), Toast.LENGTH_SHORT).show();
            return null;
        }

    }
}

