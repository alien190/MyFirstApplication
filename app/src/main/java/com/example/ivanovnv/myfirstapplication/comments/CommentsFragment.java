package com.example.ivanovnv.myfirstapplication.comments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ivanovnv.myfirstapplication.ApiUtils;
import com.example.ivanovnv.myfirstapplication.R;
import com.example.ivanovnv.myfirstapplication.model.Album;
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CommentsFragment extends Fragment {

    private static final String ALBUM_KEY = "ALBUM_KEY";
    private static final String TAG = CommentsFragment.class.getSimpleName();

    CommentsAdapter mCommentsAdapter;
    RecyclerView mRecyclerView;
    Album mAlbum;
    private SwipeRefreshLayout mRefresher;
    Observable mRefreshObservable;
    ImageButton mSendButton;
    Observable<Integer> mObservableClick;


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

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recycler_comments);
        getActivity().setTitle(getString(R.string.comments_title));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommentsAdapter = new CommentsAdapter();
        mRecyclerView.setAdapter(mCommentsAdapter);
        mRefresher = view.findViewById(R.id.sr_comments);
        mRefreshObservable = RxSwipeRefreshLayout.refreshes(mRefresher);
        mSendButton = view.findViewById(R.id.bt_new_comment);


//        mRefreshObservable.flatMap(new Function() {
//            @Override
//            public Object apply(Object o) throws Exception {
//                return null;
//            }
//        })

    }

    @SuppressLint("CheckResult")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAlbum = (Album) getArguments().getSerializable(ALBUM_KEY);

//        mRefreshObservable.subscribe(new Consumer() {
//            @Override
//            public void accept(Object o) throws Exception {
//                int i = 1;
//            }
//        });


        mObservableClick = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final ObservableEmitter emitter) throws Exception {
                mSendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emitter.onNext(10);
                    }
                });
            }
        });

        mRefreshObservable.flatMap(o -> Observable.just(0))
                .mergeWith(Observable.just(0))
                .flatMap(new Function() {
                    @Override
                    public Object apply(Object o) throws Exception {
                        mCommentsAdapter.clearContent();
                        return Observable.just(o);
                    }
                })
                .mergeWith(mObservableClick)
                .flatMapSingle(function)
                .subscribe(observer);



  /*      mRefreshObservable.flatMap(new Function() {
            @Override
            public Object apply(Object o) throws Exception {
                return ApiUtils.getApi().getAlbumComments(mAlbum.getId());
            }
        }).subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Log.d(TAG, "accept: ");
            }
        });


        ApiUtils.getApi().getAlbumComments(mAlbum.getId())
                .subscribeOn(Schedulers.io())
                .doOnSuccess(album -> {
                    showToast(getString(R.string.success_load_fom_server));
                })
                .onErrorReturn(throwable -> {
                    if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {
                        showToast(getString(R.string.error_load_from_server));
                        return null;
                    } else
                        return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mRefresher.setRefreshing(false))
                .doOnSubscribe(disposable -> mRefresher.setRefreshing(true))
                .subscribe(comments -> {
                   // mErrorView.setVisibility(View.GONE);
                  //  mRecyclerView.setVisibility(View.VISIBLE);
                    mCommentsAdapter.addData(comments, true);
                }, throwable -> {
                   // mErrorView.setVisibility(View.VISIBLE);
                   // mRecyclerView.setVisibility(View.GONE);
                });

                */
    }

    private void showToast(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show());
    }

    Observer<Object> observer = new Observer<Object>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Object comments) {
            try {

                mCommentsAdapter.addData((List<Comment>) comments, false);

//                if (comments instanceof List && !((List) comments).isEmpty() && ((List) comments).get(0) instanceof Comment) {
//                    mCommentsAdapter.addData((List<Comment>) comments, true);
//                }
//
//                if (comments instanceof Comment) {
//                    mCommentsAdapter.addComment((Comment) comments);
//                }

            } catch (Throwable t) {
                t.printStackTrace();
            }
//            try {
//                mCommentsAdapter.addData(comments, true);
//            } catch (Throwable t) {
//                t.printStackTrace();
//            }
            int i = 1;
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };

    Function<Integer, Single<List<Comment>>> function = new Function<Integer, Single<List<Comment>>>() {
        @Override
        public Single<List<Comment>> apply(Integer id) throws Exception {

            Single<List<Comment>> comments;

            if (id == 0) {
                comments = ApiUtils.getApi().getAlbumComments(mAlbum.getId());
            } else {
                comments = ApiUtils.getApi().getComment(id).map(comment -> new ArrayList<Comment>() {{
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
                    .doOnSubscribe(disposable -> mRefresher.setRefreshing(true));
        }
    };

}


//new Consumer<List<Comment>>() {
//@Override
//public void accept(List<Comment> comments) throws Exception {
//        mCommentsAdapter.addData(comments, true);
//        }
//        }, new Consumer<Throwable>() {
//@Override
//public void accept(Throwable throwable) throws Exception {
//        throwable.printStackTrace();
//        }
//        }