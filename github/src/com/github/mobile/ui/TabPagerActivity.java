package com.github.mobile.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import com.github.kevinsawicki.wishlist.ViewUtils;
import com.github.mobile.R;
import com.github.mobile.util.TypefaceUtils;

/**
 * Created by huibin on 1/21/14.
 */
public abstract class TabPagerActivity<V extends PagerAdapter & FragmentProvider>
        extends PagerActivity implements TabHost.OnTabChangeListener, TabHost.TabContentFactory {


    protected ViewPager pager;

    protected TabHost host;

    protected V adapter;


    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        host.setCurrentTab(position);
    }

    @Override
    public void onTabChanged(String tabId) {
        updateCurrentItem(host.getCurrentTab());

    }


    @Override
    public View createTabContent(String s) {
        return ViewUtils.setGone(new View(getApplication()), true);
    }


    private void updateCurrentItem(final int newPosition) {
        if (newPosition > -1 && newPosition < adapter.getCount()) {
            pager.setItem(newPosition);
            setCurrentItem(newPosition);
        }
    }

    protected abstract V createAdapter();

    protected String getTitle(final int position) {
        return adapter.getPageTitle(position).toString();
    }

    @Override
    protected FragmentProvider getProvider() {
        return null;
    }

    protected String getIcon(final int position) {
        return null;
    }

    protected TabPagerActivity<V> setGone(boolean gone) {
        ViewUtils.setGone(host, gone);
        ViewUtils.setGone(pager, gone);
        return this;
    }

    protected void setCurrentItem(final int position) {
        // Intentionally left blank
    }

    protected int getContentView() {
        return R.layout.pager_with_tabs;
    }


    private void createPager() {
        adapter = createAdapter();
        invalidateOptionsMenu();
        pager.setAdapter(adapter);
    }

    protected void createTabs() {
        if (host.getTabWidget().getTabCount() > 0) {
            // Crash on Gingerbread if tab isn't set to zero since adding a
            // new tab removes selection state on the old tab which will be
            // null unless the current tab index is the same as the first
            // tab index being added
            host.setCurrentTab(0);
            host.clearAllTabs();
        }
        LayoutInflater inflater = getLayoutInflater();
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            TabHost.TabSpec spec = host.newTabSpec("tab" + i);
            spec.setContent(this);
            View view = inflater.inflate(R.layout.tab, null);
            TextView icon = (TextView) view.findViewById(R.id.tv_icon);
            String iconText = getIcon(i);
            if (!TextUtils.isEmpty(iconText))
                icon.setText(getIcon(i));
            else
                ViewUtils.setGone(icon, true);
            TypefaceUtils.setOcticons(icon);
            ((TextView) view.findViewById(R.id.tv_tab)).setText(getTitle(i));

            spec.setIndicator(view);
            host.addTab(spec);

            int background;
            if (i == 0)
                background = R.drawable.tab_selector_right;
            else if (i == count - 1)
                background = R.drawable.tab_selector_left;
            else
                background = R.drawable.tab_selector_left_right;
            ((ImageView) view.findViewById(R.id.iv_tab))
                    .setImageResource(background);
        }

    }

    /**
     * Configure tabs and pager
     */
    protected void configureTabPager() {
        if (adapter == null) {
            createPager();
            createTabs();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentView());
        pager = (ViewPager) findViewById(R.id.vp_pages);
        pager.setOnPageChangeListener(this);
        host = (TabHost) findViewById(R.id.th_tabs);
        host.setup();
        host.setOnTabChangedListener(this);
    }

}
