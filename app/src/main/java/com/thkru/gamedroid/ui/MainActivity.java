package com.thkru.gamedroid.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.thkru.gamedroid.R;
import com.thkru.gamedroid.data.Game;
import com.thkru.gamedroid.database.FavHelper;
import com.thkru.gamedroid.utils.FavEvent;
import com.thkru.gamedroid.utils.GameAdapter;
import com.thkru.gamedroid.utils.GameSelectedEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


public class MainActivity extends Activity {

    @Nullable
    @Bind(R.id.item_detail_container)
    FrameLayout container;
    @Nullable
    @Bind(R.id.my_recycler_view)
    RecyclerView recycler;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_list);

        ButterKnife.bind(this);
        populateNormalList();

        if (container != null) {
            mTwoPane = true;
            manageEmptyView();//tablet only
        }
        setupActionbarNav();
        EventBus.getDefault().register(this);
    }

    private void manageEmptyView() {

        RelativeLayout root = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.empty_bg_tablet, null);
        container.addView(root);
        ImageView empty_header = (ImageView) root.findViewById(R.id.iv_empty_header);
        empty_header.setAlpha(40);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(FavEvent e) {
        repopulateList(getActionBar().getSelectedNavigationIndex());
    }

    public void onEvent(GameSelectedEvent e) {
        onItemSelected(e.getGame());
    }

    private int lastActionPos;

    private void setupActionbarNav() {
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);//some deprecated methods, should be fine, just for actionbar

        ArrayList<String> itemList = new ArrayList<String>();
        itemList.add(getString(R.string.all_games));
        itemList.add(getString(R.string.fav_game));
        itemList.add(getString(R.string.about));
        actionBar.setListNavigationCallbacks(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, itemList), new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int i, long l) {

                if (i == 2) {
                    actionBar.setSelectedNavigationItem(lastActionPos);
                    aboutDlg();
                    return false;
                }

                lastActionPos = i;
                repopulateList(i);
                return true;
            }
        });
    }

    private void aboutDlg() { //does not survive tilt, but that's okay (despite leaking)
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(getLayoutInflater().inflate(R.layout.about, null));
        builder.setTitle(R.string.about);
        builder.setPositiveButton(R.string.hire, null);
        builder.create().show();
    }

    private void repopulateList(int i) {
        switch (i) {
            case 0:
                populateNormalList();
                break;
            case 1:
                populateFavList();
                break;
        }
    }

    private void populateNormalList() {
        ArrayList<Game> games = getIntent().getParcelableArrayListExtra("extra");
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new GameAdapter(games));
    }

    private void populateFavList() {
        ArrayList<Game> games = getIntent().getParcelableArrayListExtra("extra");
        List<Integer> favCodes = FavHelper.getFavCodes(this);
        List<Game> favs = new ArrayList<>();

        for (Game f : games) {
            if (favCodes.contains(f.getId())) {
                favs.add(f);
            }
        }
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new GameAdapter(favs));

        if (favs.size() == 0)
            Toast.makeText(this, R.string.no_favs, Toast.LENGTH_SHORT).show();
    }

    public void onItemSelected(Game game) {
        if (mTwoPane) { //show infos in detailFragment on Tablet
            showTabletDetail(game);
        } else { //on phone: start new activity with the detail fragment
            showPhoneDetail(game);
        }
    }

    private void showPhoneDetail(Game game) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra(DetailFragment.GAME, game);
        startActivity(detailIntent);
    }

    private void showTabletDetail(Game game) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(DetailFragment.GAME, game);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(arguments);
        getFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
    }
}
