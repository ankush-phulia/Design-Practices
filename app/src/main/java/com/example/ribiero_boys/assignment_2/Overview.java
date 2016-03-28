package com.example.ribiero_boys.assignment_2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Overview extends Fragment {

    String user;
    String pass;

    public Overview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_overview, container, false);

        user = getArguments().getString("username"," ");
        pass = getArguments().getString("password"," ");
        TextView welcome=(TextView) view.findViewById(R.id.Welcome);
        welcome.setText("Welcome " + user);

        return view;
    }

}
