package com.framgia.laredolemurs.screen.tv;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.laredolemurs.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvFragment extends Fragment {

    public TvFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new TvFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv, container, false);
    }

}
