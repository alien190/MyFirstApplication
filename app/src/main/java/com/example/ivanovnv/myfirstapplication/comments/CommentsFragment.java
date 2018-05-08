package com.example.ivanovnv.myfirstapplication.comments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ivanovnv.myfirstapplication.R;

import java.util.ArrayList;

public class CommentsFragment extends Fragment {

    CommentsAdapter mCommentsAdapter = new CommentsAdapter();
    RecyclerView mRecyclerView;

    public static CommentsFragment newInstance() {

        Bundle args = new Bundle();

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

        mRecyclerView = view.findViewById(R.id.recycler_comments);
        getActivity().setTitle(getString(R.string.comments_title));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mCommentsAdapter);
        mCommentsAdapter.addData(new ArrayList<Comment>(){{add(new Comment("user", "text", "time"));}}, true);

    }
}
