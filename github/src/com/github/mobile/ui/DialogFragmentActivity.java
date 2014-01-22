package com.github.mobile.ui;

import static com.actionbarsherlock.view.Window.FEATURE_INDETERMINATE_PROGRESS;

import android.os.Bundle;
import com.github.kevinsawicki.wishlist.ViewFinder;
import com.rtyley.roboguice_sherlock.roboguice.activity.RoboSherlockFragmentActivity;

import java.io.Serializable;


/**
 * Created by huibin on 1/21/14.
 */
public abstract class DialogFragmentActivity extends RoboSherlockFragmentActivity implements DialogResultListener {

    protected ViewFinder finder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        finder = new ViewFinder(this);
    }

    protected <V extends Serializable> V getSerializableExtra(final String name) {
        return (V) getIntent().getSerializableExtra(name);
    }

    protected int getIntExtra(final String name) {
        return getIntent().getIntExtra(name, -1);
    }

    protected int[] getIntArrayExtra(final String name) {
        return getIntent().getIntArrayExtra(name);
    }

    protected boolean[] getBooleanArrayExtra(final String name) {
        return getIntent().getBooleanArrayExtra(name);
    }

    protected String getStringExtra(final String name) {
        return getIntent().getStringExtra(name);
    }

    protected String[] getStringArrayExtra(final String name) {
        return getIntent().getStringArrayExtra(name);
    }

    protected CharSequence[] getCharSequenceArrayExtra(final String name) {
        return getIntent().getCharSequenceArrayExtra(name);
    }



    @Override
    public void onDialogResult(int requestCode, int resultCode, Bundle arguments) {
        // Intentionally left blank
    }
}
