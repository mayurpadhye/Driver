package com.ccube9.driver.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ccube9.driver.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment  {

View v;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_home, container, false);

        return v;
    }//onCreateClose

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getActivity(), "dfdfdsf", Toast.LENGTH_SHORT).show();
    }

    public void initView()
    {

    }//initViewClose



}
