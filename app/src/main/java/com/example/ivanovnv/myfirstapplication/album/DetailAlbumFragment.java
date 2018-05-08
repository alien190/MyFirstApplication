package com.example.ivanovnv.myfirstapplication.album;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ivanovnv.myfirstapplication.ApiUtils;
import com.example.ivanovnv.myfirstapplication.App;
import com.example.ivanovnv.myfirstapplication.R;
import com.example.ivanovnv.myfirstapplication.comments.CommentsFragment;
import com.example.ivanovnv.myfirstapplication.db.AlbumSong;
import com.example.ivanovnv.myfirstapplication.db.MusicDao;
import com.example.ivanovnv.myfirstapplication.model.Album;
import com.example.ivanovnv.myfirstapplication.model.Song;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DetailAlbumFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ALBUM_KEY = "ALBUM_KEY";

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefresher;
    private View mErrorView;
    private Album mAlbum;

    @NonNull
    private final SongsAdapter mSongsAdapter = new SongsAdapter();

    public static DetailAlbumFragment newInstance(Album album) {
        Bundle args = new Bundle();
        args.putSerializable(ALBUM_KEY, album);

        DetailAlbumFragment fragment = new DetailAlbumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycler);
        mRefresher = view.findViewById(R.id.refresher);
        mRefresher.setOnRefreshListener(this);
        mErrorView = view.findViewById(R.id.errorView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAlbum = (Album) getArguments().getSerializable(ALBUM_KEY);

        getActivity().setTitle(mAlbum.getName());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mSongsAdapter);

        onRefresh();
    }

    @Override
    public void onRefresh() {
        mRefresher.post(() -> {
            mRefresher.setRefreshing(true);
            getAlbum();
        });
    }

    @SuppressLint("CheckResult")
    private void getAlbum() {

        ApiUtils.getApi()
                .getAlbum(mAlbum.getId())
                .subscribeOn(Schedulers.io())
                .doOnSuccess(album -> {
                    getMusicDao().insertSongs(album.getSongs());
                    getMusicDao().deleteAlbumSongs(album.getId());
                    getMusicDao().insertAlbumSongs(getAlbumSong(album));
                    // List<AlbumSong> tmp = getMusicDao().getAlbumSongsByAlbumId(album.getId());
                    showToast(getString(R.string.success_load_fom_server));
                })
                .onErrorReturn(throwable -> {
                    if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {
                        showToast(getString(R.string.error_load_from_server));
                        Album retAlbum = new Album();
                        retAlbum.setSongs(getMusicDao().getSongsByAlbumId(mAlbum.getId()));
                        return retAlbum;
                    } else
                        return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mRefresher.setRefreshing(false))
                .doOnSubscribe(disposable -> mRefresher.setRefreshing(true))
                .subscribe(album -> {
                    mErrorView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mSongsAdapter.addData(album.getSongs(), true);
                }, throwable -> {
                    mErrorView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                });
    }

    private MusicDao getMusicDao() {
        return ((App) getActivity().getApplication()).getDataBase().getMusicDao();
    }

    private void showToast(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show());
    }

    List<AlbumSong> getAlbumSong(Album album) {
        if (album != null && album.getSongs() != null) {
            ArrayList<AlbumSong> albumSongs = new ArrayList<>();
            for (Song song : album.getSongs()) {
                albumSongs.add(new AlbumSong(0, album.getId(), song.getId()));
            }
            return albumSongs;
        } else return null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_album_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mi_comments) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, CommentsFragment.newInstance())
                    .addToBackStack(CommentsFragment.class.getSimpleName())
                    .commit();

            return true;
        }

        return false;
    }
}
