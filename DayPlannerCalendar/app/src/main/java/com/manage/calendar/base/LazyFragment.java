package com.manage.calendar.base;

import androidx.viewbinding.ViewBinding;

public abstract class LazyFragment<VB extends ViewBinding> extends BaseFragment<VB> {

    private boolean isLoaded = false;

    @Override
    public void onResume() {
        super.onResume();
        if (!isLoaded && !isHidden()) {
            lazyInit();
            isLoaded = true;
        } else {
            visibility();
        }
    }

    public abstract void lazyInit();

    public void visibility() {

    }

    public void onInitData() {

    }
}
