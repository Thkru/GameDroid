package com.thkru.gamedroid.utils;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.thkru.gamedroid.R;
import com.thkru.gamedroid.cloud.CloudHelper;
import com.thkru.gamedroid.data.CoverConstants;
import com.thkru.gamedroid.data.Game;
import com.thkru.gamedroid.database.FavHelper;

import java.util.List;

import de.greenrobot.event.EventBus;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private final List<Game> games;

    public GameAdapter(List<Game> games) {
        this.games = games;
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {
        public View root;
        public TextView name;
        public TextView date;
        public TextView dev;
        public ImageView cover;
        public ImageView fav;

        public GameViewHolder(View view) {
            super(view);
            initViewHolder(view);
        }

        private void initViewHolder(View view) {
            root = view.findViewById(R.id.list_item_root);
            name = (TextView) view.findViewById(R.id.tv_list_name);
            date = (TextView) view.findViewById(R.id.tv_list_date);
            dev = (TextView) view.findViewById(R.id.tv_list_dev);
            cover = (ImageView) view.findViewById(R.id.iv_list_gamecover);
            fav = (ImageView) view.findViewById(R.id.iv_fav);
        }
    }

    private int lastPos = -1; //used for animation

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listfragment_item, null);
        return new GameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, final int pos) {

        setClickListener(holder, pos);

        if (pos % 2 == 0)
            holder.root.setBackgroundColor(Color.argb(255, 240, 240, 240));

        setTextCaptions(holder, pos);
        handleFavIcon(holder, pos);
        Ion.with((ImageView) holder.cover)
                .resize(100, 150)
                .load(CloudHelper.getCoverUrlForId(CoverConstants.COVER_LIST, games.get(pos).getHash()));

//        animateItem(holder, pos);
    }

    private void handleFavIcon(GameViewHolder holder, int pos) {
        boolean isFav = FavHelper.isFav(holder.root.getContext(), games.get(pos).getId());
        holder.fav.setVisibility(isFav ? View.VISIBLE : View.INVISIBLE);
    }

    private void setTextCaptions(GameViewHolder holder, int pos) {
        holder.name.setText(games.get(pos).getName());

        if (games.get(pos).getReleaseDate() != null)
            holder.date.setText(games.get(pos).getReleaseDate());

//        if (games.get(pos).getDev() != null)
//            holder.dev.setText(games.get(pos).getDev());
    }

    private void setClickListener(GameViewHolder holder, final int pos) {
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new GameSelectedEvent(games.get(pos)));
            }
        });
    }

    private void animateItem(GameViewHolder holder, int pos) {
        Animation animation = AnimationUtils.loadAnimation(holder.root.getContext(), (pos > lastPos) ? R.anim.up_from_bottom : R.anim.down_from_top);
        holder.root.startAnimation(animation);
        lastPos = pos;
    }

    @Override
    public long getItemId(int i) {
        return games.get(i).getId();
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

}