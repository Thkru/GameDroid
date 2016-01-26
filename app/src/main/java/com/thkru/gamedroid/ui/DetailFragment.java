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

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link MainActivity}
 * in two-pane mode (on tablets) or a {@link DetailActivity}
 * on handsets.
 */
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

        initPhoneAndWebButtons(v);

        handleFavourit(v);
        return v;
    }

    private void initTextsForGame(View v) {
        ((TextView) v.findViewById(R.id.tv_code)).setText(""+mGame.getId());
        ((TextView) v.findViewById(R.id.tv_name)).setText(mGame.getReleaseDate());

        String phone = mGame.getText();
        if (phone == null || phone.length() == 0)
            phone = getResources().getString(R.string.no_phone);

        ((TextView) v.findViewById(R.id.tv_phone)).setText(phone);

//        String site = mGame.getCoverUrl();
//        if (site == null || site.length() == 0)
//            site = getResources().getString(R.string.no_web);
//        ((TextView) v.findViewById(R.id.tv_website)).setText(site);
    }

    private boolean isTelephonyEnabled() {   //checks if your device can make calls... obviously ^
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

//    private boolean isValidWeb() {
//        String web = mGame.getCoverUrl();
//        return web != null && web.length() > 0 && (web.startsWith("http") || web.startsWith("www"));//additional checks possible
//    }

    private boolean isValidPhone() {
        String phone = mGame.getText();
        return phone != null && phone.length() > 0;//additional checks possible
    }

    private void initPhoneAndWebButtons(View v) {

//        if (!isValidWeb()) {
//            v.findViewById(R.id.btn_website).setEnabled(false);
//            v.findViewById(R.id.btn_website).setBackgroundResource(R.drawable.kbutton_inactive);
//        }
//        if (!isValidPhone() || !isTelephonyEnabled()) {
//            v.findViewById(R.id.btn_call).setEnabled(false);
//            v.findViewById(R.id.btn_call).setBackgroundResource(R.drawable.kbutton_inactive);
//        }
//
//        v.findViewById(R.id.btn_website).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                String web = mGame.getCoverUrl();
//                web = web.startsWith("http://") ? web : "http://".concat(web); //could have used Stringbuilder instead...
//                i.setData(Uri.parse(web));
//                startActivity(i);
//            }
//        });

        v.findViewById(R.id.btn_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL); //could have used "Call" instead, but this is more comfortable and needs no extra permission
                intent.setData(Uri.parse("tel:" + mGame.getText()));
                startActivity(intent);
            }
        });
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
