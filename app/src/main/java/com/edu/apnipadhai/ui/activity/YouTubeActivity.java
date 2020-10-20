package com.edu.apnipadhai.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edu.apnipadhai.R;
import com.edu.apnipadhai.callbacks.ListItemClickListener;
import com.edu.apnipadhai.model.VideoModel;
import com.edu.apnipadhai.ui.adapter.VideoRelatedAdapter;
import com.edu.apnipadhai.utils.Const;
import com.edu.apnipadhai.utils.FullScreenHelper;
import com.edu.apnipadhai.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.PlayerUiController;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.menu.MenuItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class YouTubeActivity extends AppCompatActivity implements ListItemClickListener<Integer, VideoModel> {

    private VideoRelatedAdapter adapter;
    private RecyclerView rvRelatedVideo;
    private AppCompatTextView tvNoData;
    private ArrayList<VideoModel> list1;

    private YouTubePlayerView youTubePlayerView;
    private AppCompatTextView tvTitle;
    private AppCompatImageView ivBookmark, ivShare;
    private VideoModel videoModel;
    private FullScreenHelper fullScreenHelper = new FullScreenHelper(this, null);
    ActionMode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);


        youTubePlayerView = findViewById(R.id.youtube_player_view);
        videoModel = new Gson().fromJson(getIntent().getStringExtra(Const.VIDEO_MODEL), VideoModel.class);
        initYouTubePlayerView(videoModel.getVideoId());
        init();
        setRecyclerView();
        fetchData();
    }

    private void init() {
        rvRelatedVideo = findViewById(R.id.rvRelatedVideo);
        tvNoData = findViewById(R.id.tvNoData);
    }

    private void setRecyclerView() {
        list1 = new ArrayList<>();
        list1.add(videoModel);
        adapter = new VideoRelatedAdapter(this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvRelatedVideo.setLayoutManager(layoutManager);
        rvRelatedVideo.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Integer type, VideoModel item) {

        switch (type) {
            case Const.TYPE_CLICKED_2:
                shareVideoLink(videoModel.getVideoId());
                break;
            case Const.TYPE_BOOKMARK:
                break;
            default:
                Intent intent = new Intent(this, YouTubeActivity.class);
                intent.putExtra(Const.VIDEO_MODEL, new Gson().toJson(item));
                startActivity(intent);
                break;
        }

    }

    private void fetchData() {
        FirebaseFirestore.getInstance().collection(Const.TABLE_VIDEOS)
                .whereEqualTo("courseId", videoModel.getCourseId()).limit(Const.LIMIT_VIDEOS).limit(Const.LIMIT_VIDEOS).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (queryDocumentSnapshots.size() > 0) {
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                VideoModel model = snapshot.toObject(VideoModel.class);
                                if (!videoModel.getVideoId().equals(model.getVideoId()))
                                    list1.add(model);
                            }
                        }
                        adapter.setList(list1, tvNoData, rvRelatedVideo);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    private void shareVideoLink(String videoLink) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(
                Intent.EXTRA_TEXT, getString(R.string.video_link) + "https://youtu.be/" + videoLink
        );
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
        youTubePlayerView.getPlayerUiController().getMenu().dismiss();
    }

    @Override
    public void onBackPressed() {
        if (youTubePlayerView.isFullScreen())
            youTubePlayerView.exitFullScreen();
        else
            super.onBackPressed();
    }

    private void initYouTubePlayerView(String videoId) {
        // initPlayerMenu();

        // The player will automatically release itself when the activity is destroyed.
        // The player will automatically pause when the activity is stopped
        // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer,
                        getLifecycle(),
                        videoId,
                        0f
                );

                addFullScreenListenerToPlayer();
                // setPlayNextVideoButtonClickListener(youTubePlayer);
            }
        });
    }

    /**
     * Shows the menu button in the player and adds an item to it.
     */
    private void initPlayerMenu() {
        youTubePlayerView.getPlayerUiController()
                .showMenuButton(true)
                .getMenu()
                .addItem(new MenuItem("menu item1", R.drawable.ic_android_black_24dp,
                        view -> Toast.makeText(this, "item1 clicked", Toast.LENGTH_SHORT).show())
                ).addItem(new MenuItem("menu item2", R.drawable.ic_mood_black_24dp,
                view -> Toast.makeText(this, "item2 clicked", Toast.LENGTH_SHORT).show())
        ).addItem(new MenuItem("menu item no icon",
                view -> Toast.makeText(this, "item no icon clicked", Toast.LENGTH_SHORT).show()));
    }

    private void addFullScreenListenerToPlayer() {
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                fullScreenHelper.enterFullScreen();

                addCustomActionsToPlayer();
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fullScreenHelper.exitFullScreen();

                removeCustomActionsFromPlayer();
            }
        });
    }

    /**
     * This method adds a new custom action to the player.
     * Custom actions are shown next to the Play/Pause button in the middle of the player.
     */
    private void addCustomActionsToPlayer() {
        Drawable customAction1Icon = ContextCompat.getDrawable(this, R.drawable.ic_fast_rewind_white_24dp);
        Drawable customAction2Icon = ContextCompat.getDrawable(this, R.drawable.ic_fast_forward_white_24dp);
        assert customAction1Icon != null;
        assert customAction2Icon != null;

        youTubePlayerView.getPlayerUiController().setCustomAction1(customAction1Icon, view ->
                Toast.makeText(this, "custom action1 clicked", Toast.LENGTH_SHORT).show());

        youTubePlayerView.getPlayerUiController().setCustomAction2(customAction2Icon, view ->
                Toast.makeText(this, "custom action1 clicked", Toast.LENGTH_SHORT).show());
    }

    private void removeCustomActionsFromPlayer() {
        youTubePlayerView.getPlayerUiController().showCustomAction1(false);
        youTubePlayerView.getPlayerUiController().showCustomAction2(false);
    }
}
