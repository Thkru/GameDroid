package com.thkru.gamedroid.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.thkru.gamedroid.R;

public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_detail);

//        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailFragment.GAME, getIntent().getParcelableExtra(DetailFragment.GAME));
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
