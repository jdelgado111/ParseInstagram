package com.example.janethdelgado.parseinstagram.fragments;

import android.os.Bundle;
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

import com.example.janethdelgado.parseinstagram.Post;
import com.example.janethdelgado.parseinstagram.PostsAdapter;
import com.example.janethdelgado.parseinstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {

    private final String TAG = "PostsFragment";

    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> mPosts;

    protected SwipeRefreshLayout swipeContainer;

    //onCreateView to inflate the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        swipeContainer = view.findViewById(R.id.swipeContainer);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_dark);

        rvPosts = view.findViewById(R.id.rvPosts);

        //create the data source
        mPosts = new ArrayList<>();

        //create the adapter
        adapter = new PostsAdapter(getContext(), mPosts);

        //set the adapter on the recycler view
        //adapter tells the recycler view how to show the contents from the view
        rvPosts.setAdapter(adapter);

        //set the layout manager on the recycler view
        //the layout manager is how you lay out the contents onto the screen
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        queryPosts();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("TwitterClient", "content is being refreshed");
                queryPosts();
            }
        });
    }

    protected void queryPosts() {
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20); //set max number of posts
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);  //show posts in reverse chronological order
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with query");
                    e.printStackTrace();
                    return;
                }

                //add retrieved List of posts to our adapter-connected List
                //mPosts.addAll(posts);
                //adapter.notifyDataSetChanged();

                //clear the existing data
                adapter.clearPosts();
                //show the data we just received
                adapter.addPosts(posts);

                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);

                //log for debugging purposes
                /*for (int i = 0; i < posts.size(); i++) {
                    Post post = posts.get(i);
                    Log.d(TAG, "Post: " + post.getDescription() +
                            ", username: " + post.getUser().getUsername());
                }*/
            }
        });
    }
}
