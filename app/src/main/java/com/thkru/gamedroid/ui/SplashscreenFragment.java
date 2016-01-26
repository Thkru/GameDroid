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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.thkru.gamedroid.cloud.CloudHelper;
import com.thkru.gamedroid.data.Game;

import com.thkru.gamedroid.R;

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

        return new AsyncTaskLoader<List<Game>>(getActivity()) {

            @Override
            public List<Game> loadInBackground() {

                try
                {
                    return new CloudHelper().getGamesFromServer();
                }
                catch (IOException e)
                {
                    e.printStackTrace();//could check for specific exception here, i.e. showing dialogs via publishProgress in UI Thread
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {

        if(o == null ) { //when Exception (or 30s Timeout) during connection, can be tested in airplaneMode+Wifi off
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.loading_error), Toast.LENGTH_LONG).show();
            //could finish the app OR handle dialog, OR retry, OR ... whatever
            return;
        }

        Intent i = new Intent(getActivity(), MainActivity.class);
        i.putParcelableArrayListExtra("extra", (ArrayList<Parcelable>)o);
        getActivity().startActivity(i);
        getActivity().finish();
    }

    @Override
    public void onLoaderReset(Loader loader) {
        //had to implement it, interfaceContract
    }
}
