package com.thkru.gamedroid.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import com.thkru.gamedroid.cloud.CloudHelper;
import com.thkru.gamedroid.data.Game;
import com.thkru.gamedroid.database.FavHelper;
import com.thkru.gamedroid.utils.FavEvent;

import com.thkru.gamedroid.R;

import de.greenrobot.event.EventBus;

public class DetailFragment extends Fragment {

    public static final String GAME = "game";
    private Game mGame;

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGame = getArguments().getParcelable(GAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_detail, container, false);

        initTextsForGame(v);

        ImageView img = (ImageView) v.findViewById(R.id.iv_gamecover);
        Picasso.with(getActivity()).load(CloudHelper.getCoverUrlForId(mGame.getId())).into(img);
        img.setAlpha(20);//ion has problems using alpha afterwards, picasso doesn't

        handleFavourit(v);
        return v;
    }

    private void initTextsForGame(View v) {
        ((TextView) v.findViewById(R.id.tv_name)).setText("" + mGame.getName());
        ((TextView) v.findViewById(R.id.tv_date)).setText(mGame.getReleaseDate());
        ((TextView) v.findViewById(R.id.tv_dev)).setText(mGame.getDev());
        ((TextView) v.findViewById(R.id.tv_description)).setText(mGame.getText());
    }

    private void handleFavourit(View v) {
        View fav = v.findViewById(R.id.btn_favourite);

        fav.setSelected(FavHelper.isFav(getActivity().getApplicationContext(), mGame.getId())); //pressed when fav

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFavNow = FavHelper.handleFavourite(getActivity().getApplicationContext(), mGame.getId());
                view.setSelected(isFavNow);
                EventBus.getDefault().post(new FavEvent());
            }
        });
    }
}
