package com.framgia.laredolemurs.screen.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.framgia.laredolemurs.R;
import com.framgia.laredolemurs.constant.Const;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    @Bind(R.id.about) TextView aboutTextView;
    @Bind(R.id.phone) TextView phoneTextView;
    @Bind(R.id.location) TextView locationTextView;

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
        ButterKnife.bind(this, view);
        getInformation();
        return view;
    }


    private void getInformation() {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/LaredoLemurs",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            if (response != null) {
                                JSONObject object = response.getJSONObject();
                                if(object == null)
                                    return;
                                Const.pageProfilePicture = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                aboutTextView.setText(object.getString("about"));
                                phoneTextView.setText("Phone number: " + object.getString("phone"));
                                JSONObject location = object.getJSONObject("location");
                                locationTextView.setText("Address: " + location.getString("street") + ", " + location.getString("city") + ", " + location.getString("state") + ", " + location.getString("country"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "about,phone,name,location,description,picture.type(large)");
        parameters.putBoolean("redirect", false);
        request.setParameters(parameters);
        request.executeAsync();
    }
}
