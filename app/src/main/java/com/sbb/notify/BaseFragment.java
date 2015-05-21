package com.sbb.notify;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by sbb on 2015/5/21.
 */
public abstract class BaseFragment extends Fragment implements LoaderImpl{

    View view;
    public AtomicBoolean init = new AtomicBoolean(false);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(getLayoutId(), null);
        } else {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view);
            }
        }
        return view;
    }

    public abstract int getLayoutId();

    @Override
    public void refresh() {
        if (getActivity() != null && init.compareAndSet(false, true)) {
            doRefresh();
        }
    }

    public abstract void doRefresh();
}
