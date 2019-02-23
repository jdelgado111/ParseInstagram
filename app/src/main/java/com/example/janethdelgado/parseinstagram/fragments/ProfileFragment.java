package com.example.janethdelgado.parseinstagram.fragments;

import android.util.Log;

import com.example.janethdelgado.parseinstagram.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {

    private final String TAG = "ProfileFragment";

    @Override
    protected void queryPosts() {
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20); //set max number of posts
        postQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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