package com.example.ivanovnv.myfirstapplication.comments;

import android.annotation.SuppressLint;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ivanovnv.myfirstapplication.ApiUtils;
import com.example.ivanovnv.myfirstapplication.R;
import com.example.ivanovnv.myfirstapplication.model.Album;
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding2.view.RxView;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
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
    private Observable mRefreshObservable;
    private ImageButton mSendButton;
    private Observable mSendButtonObservable;
    private LinearLayout mErrorLayout;
    private LinearLayout mNoCommentLayout;


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
        mSendButtonObservable = RxView.clicks(mSendButton);

        mErrorLayout = view.findViewById(R.id.errorView);
        mNoCommentLayout = view.findViewById(R.id.no_comment_view);

        mAlbum = (Album) getArguments().getSerializable(ALBUM_KEY);

        mRefreshObservable = RxSwipeRefreshLayout.refreshes(mRefresher);

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


    Observer<ObservData> observer = new Observer<ObservData>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(ObservData observData) {
            // if (integer == 1) {


            mNoCommentLayout.setVisibility(View.GONE);
            mErrorLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            mRecyclerView.scrollToPosition(mCommentsAdapter.getItemCount() - 1);

            if(observData.isRefreshed()) {
                int addedComsCount = observData.getNewCommentCount() - observData.getOldCommentCount();
                if(addedComsCount == 0) {
                    Toast.makeText(getActivity(), R.string.no_new_comments, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), getString(R.string.add_new_comm) + addedComsCount, Toast.LENGTH_SHORT).show();
                }
            }
            // }
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


    Function<ObservData, Single<ObservData>> getComment = new Function<ObservData, Single<ObservData>>() {
        @Override
        public Single<ObservData> apply(ObservData observData) throws Exception {

            Single<List<Comment>> comments;

            if (observData.isRefreshed() || observData.isFirstLoad()) {
                comments = ApiUtils.getApi().getAlbumComments(mAlbum.getId());
            } else {
                comments = ApiUtils.getApi().getComment(observData.getNewCommentId()).map(comment -> new ArrayList<Comment>() {{
                    add(comment);
                }});
            }

            return comments
                    .subscribeOn(Schedulers.io())
                    .onErrorReturn(throwable -> {
                        if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {
                            showToast(getString(R.string.error_load_from_server));

                            return new ArrayList<Comment>() {{
                                add(new Comment("dsadsa", "from DB", "1964-12-15T00:00:00+00:00"));
                            }};
                        } else
                            return null;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(() -> mRefresher.setRefreshing(false))
                    .flatMap((Function<List<Comment>, Single<ObservData>>) loadedComments -> {
                        observData.setComments(loadedComments);
                        return Single.just(observData);
                    })
                    .doOnSubscribe(disposable -> {
                        if (!mRefresher.isRefreshing()) mRefresher.setRefreshing(true);
                    });
        }
    };

    Function<Object, Single<ObservData>> postComment = new Function<Object, Single<ObservData>>() {
        @Override
        public Single<ObservData> apply(Object o) throws Exception {

            CommentForPost commentForPost = new CommentForPost(mAlbum.getId(), "bla-bla-bla");

            return ApiUtils.getApi().postComment(commentForPost)
                    .subscribeOn(Schedulers.io())
                    .onErrorReturn(throwable -> {
                        if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {
                            showToast(getString(R.string.err_send_comment_net));

                        } else {
                            showToast(getString(R.string.err_send_comment_unkn));
                        }
                        return new CommentId(-1);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> mRefresher.setRefreshing(true))
                    .flatMap((Function<CommentId, SingleSource<ObservData>>) commentId -> {
                        ObservData observData = new ObservData();
                        observData.setIsAddComment();
                        observData.setNewCommentId(commentId.getId());
                        return Single.just(observData);
                    });
        }
    };


    private void createNewObservers(boolean firstTime) {

        ObservData observDataFirstLoad = new ObservData();
        if (firstTime) observDataFirstLoad.setIsFirstLoad();

        mRefreshObservable.flatMap((Function<Object, Observable<ObservData>>) o -> {
            ObservData observData = new ObservData();
            observData.setIsRefreshed();
            return Observable.just(observData);
        })
                .mergeWith(Observable.just(observDataFirstLoad))
                .flatMap(mCommentsAdapter.clearContent)
                .mergeWith(mSendButtonObservable.flatMapSingle(postComment))
                .filter((Predicate<ObservData>) observData -> {
                    if (observData.isRefreshed()) return true;
                    if (observData.isFirstLoad()) return true;
                    if (observData.isAddComment()) {
                        if (observData.getNewCommentId() == -1) mRefresher.setRefreshing(false);
                        return observData.getNewCommentId() != -1;
                    }
                    return false;
                })
                .flatMapSingle(getComment)
                .flatMap(mCommentsAdapter.addComments)
                .subscribe(observer);
    }
}

