package com.framgia.laredolemurs.screen.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.framgia.laredolemurs.R;
import com.framgia.laredolemurs.application.BaseApplication;
import com.framgia.laredolemurs.constant.Const;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getProfilePicture();
        return view;
    }

    private void getProfilePicture() {
        Bundle parameters = new Bundle();
        parameters.putString("type", "large");
        parameters.putBoolean("redirect", false);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/LaredoLemurs/picture",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            Log.i("TAG", "json " + response.getRawResponse());
                            JSONObject object = response.getJSONObject();
                            Const.pageProfilePicture = object.getJSONObject("data").getString("url");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }
}
