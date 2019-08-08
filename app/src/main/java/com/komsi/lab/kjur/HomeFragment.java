package com.komsi.lab.kjur;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private IntentIntegrator qrScan;
    private String toast;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        displayToast();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        LinearLayout llStores = view.findViewById(R.id.llStores);
        llStores.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LocationActivity.class);
                startActivity(i);
            }
        });

        qrScan = IntentIntegrator.forSupportFragment(this).setCaptureActivity(ScanQrActivity.class);
        qrScan.setDesiredBarcodeFormats(qrScan.QR_CODE_TYPES);

        LinearLayout llScanQR = view.findViewById(R.id.llScanQR);
        llScanQR.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });

        return view;
    }

    private void displayToast() {
        if(getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getActivity(), "Scan Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String scanText = result.getContents();
                Intent intent = new Intent(getActivity() , ScanQrLoadingActivity.class);
                intent.putExtra("scanText" , scanText);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
