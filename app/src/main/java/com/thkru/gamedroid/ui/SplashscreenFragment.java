package com.thkru.gamedroid.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thkru.gamedroid.R;
import com.thkru.gamedroid.cloud.CloudHelper;
import com.thkru.gamedroid.utils.GamesLoadedEvent;
import com.thkru.gamedroid.utils.ServerErrorEvent;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class SplashscreenFragment extends Fragment implements LoaderManager.LoaderCallbacks {

    public SplashscreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this).forceLoad();
        return inflater.inflate(R.layout.fragment_splashscreen, container, false);
    }


    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {

        return new AsyncTaskLoader<Void>(getActivity()) {

            @Override
            public Void loadInBackground() {
                new CloudHelper().doRequest();
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {
    }

    @Override
    public void onLoaderReset(Loader loader) {
        //had to implement it, interfaceContract
    }


    public void onEvent(GamesLoadedEvent e) {
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.putParcelableArrayListExtra("extra", (ArrayList<? extends Parcelable>) e.getGames());
        getActivity().startActivity(i);
        getActivity().finish();
    }

    public void onEvent(ServerErrorEvent e) {
        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.loading_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }
}
