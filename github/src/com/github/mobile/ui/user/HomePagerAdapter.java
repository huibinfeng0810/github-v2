package com.github.mobile.ui.user;

import android.support.v4.app.Fragment;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.github.mobile.ui.FragmentPagerAdapter;

/**
 * Created by huibin on 1/22/14.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {


    public HomePagerAdapter(SherlockFragmentActivity activity) {
        super(activity);
    }

    @Override
    public Fragment getItem(int i) {
        return null;
    }

    @Override
    public SherlockFragment getSelected() {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
