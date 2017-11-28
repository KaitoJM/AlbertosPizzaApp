package com.example.jm.albertospizza;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class Product_custom_input_monitor extends Fragment {

    TextView txt_quantity;
    Button b0, b1, b2, b3, b4, b5, b6, b7, b8, b9;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_custom_input_monitor, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here

//        txt_quantity = (TextView)view.findViewById(R.id.txt_cutom_input_quantity);
//        b0 = (Button)view.findViewById(R.id.btn_0);
//        b1 = (Button)view.findViewById(R.id.btn_1);
//        b2 = (Button)view.findViewById(R.id.btn_2);
//        b3 = (Button)view.findViewById(R.id.btn_3);
//        b4 = (Button)view.findViewById(R.id.btn_4);
//        b5 = (Button)view.findViewById(R.id.btn_5);
//        b6 = (Button)view.findViewById(R.id.btn_6);
//        b7 = (Button)view.findViewById(R.id.btn_7);
//        b8 = (Button)view.findViewById(R.id.btn_8);
//        b9 = (Button)view.findViewById(R.id.btn_9);
//
//        b0.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                compute_quantity(v);
//            }
//        });
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                compute_quantity(v);
//            }
//        });
//        b2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                compute_quantity(v);
//            }
//        });
//        b3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                compute_quantity(v);
//            }
//        });
//        b4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                compute_quantity(v);
//            }
//        });
//        b5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                compute_quantity(v);
//            }
//        });
//        b6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                compute_quantity(v);
//            }
//        });
//        b7.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                compute_quantity(v);
//            }
//        });
//        b8.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                compute_quantity(v);
//            }
//        });
//        b9.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                compute_quantity(v);
//            }
//        });

    }

    public void compute_quantity(View view) {
//        Button btn = (Button) view;
//        String num = btn.getText().toString();
//
//        txt_quantity.setText(txt_quantity.getText().toString() + num);
    }
}
