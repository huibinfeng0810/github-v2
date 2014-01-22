package com.github.mobile.ui;

import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * Created by huibin on 1/22/14.
 */
public abstract class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter
        implements FragmentProvider {

    private final SherlockFragmentActivity activity;
    private SherlockFragment selected;


    public FragmentPagerAdapter(SherlockFragmentActivity activity) {
        super(activity.getSupportFragmentManager());
        this.activity = activity;
    }


    @Override
    public void setPrimaryItem(final ViewGroup container, final int position, final Object object) {
        super.setPrimaryItem(container, position, object);

        boolean changed = false;

        if ((object instanceof SherlockFragment)) {
            changed = object != selected;
            selected = (SherlockFragment) object;

        } else {
            changed = object != null;
            selected = null;
        }

        if (changed) {
            activity.invalidateOptionsMenu();
        }
    }
}
