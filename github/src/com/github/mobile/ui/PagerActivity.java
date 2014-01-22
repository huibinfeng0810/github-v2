package com.github.mobile.ui;

import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Menu;

/**
 * Created by huibin on 1/21/14.
 */
public abstract class PagerActivity extends DialogFragmentActivity
        implements OnPageChangeListener {

    private boolean menuCreated;

    protected abstract FragmentProvider getProvider();

    protected SherlockFragment getFragment() {
        FragmentProvider provider = getProvider();
        if (provider != null) {
            return provider.getSelected();
        } else {
            return null;
        }

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        SherlockFragment fragment = getFragment();
        if (fragment != null) {
            return fragment.onOptionsItemSelected(item);
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void invalidateOptionsMenu() {
        if (menuCreated)
            super.invalidateOptionsMenu();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SherlockFragment fragment = getFragment();
        if (fragment != null) {
            fragment.onCreateOptionsMenu(menu, getSupportMenuInflater());
        }
        boolean created = super.onCreateOptionsMenu(menu);
        menuCreated = true;
        return created;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
