package com.komsi.lab.kjur;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {


    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        LinearLayout llOrderH = view.findViewById(R.id.llOrderH);
        llOrderH.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), OrderHistoryActivity.class);
                startActivity(i);
            }
        });

        LinearLayout llCPay = view.findViewById(R.id.llCPay);
        llCPay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChoosePaymentActivity.class);
                startActivity(i);
            }
        });

        LinearLayout llWPay = view.findViewById(R.id.llWPay);
        llWPay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), WaitingPaymentActivity.class);
                startActivity(i);
            }
        });

        //TextView countBadge = view.findViewById(R.id.countBadge);
        //countBadge.bringToFront();

        return view;
    }

}
