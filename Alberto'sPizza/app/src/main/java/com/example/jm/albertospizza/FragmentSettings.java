package com.example.jm.albertospizza;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class FragmentSettings extends Fragment {
    TextView txtname, txtaddress, txtphone, txtemail;
    Button btn_save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        txtname = (TextView)view.findViewById(R.id.txt_setting_name);
        txtaddress = (TextView)view.findViewById(R.id.txt_setting_address);
        txtphone = (TextView)view.findViewById(R.id.txt_setting_phone);
        txtemail = (TextView)view.findViewById(R.id.txt_setting_email);
        btn_save = (Button)view.findViewById(R.id.btn_setting_save);

        DatabaseOperation DOP = new DatabaseOperation(getContext());
        Cursor info = DOP.getInformation();

        if (info.getCount() > 0) {
            info.moveToFirst();
            txtname.setText(info.getString(1));
            txtaddress.setText(info.getString(4));
            txtphone.setText(info.getString(3));
            txtemail.setText(info.getString(2));
        }

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseOperation DOP = new DatabaseOperation(getContext());
                Cursor check = DOP.getInformation();

                String name = txtname.getText().toString();
                String email = txtemail.getText().toString();
                String phone = txtphone.getText().toString();
                String address = txtaddress.getText().toString();

                if (check.getCount() > 0) {
                    Boolean upd = DOP.updateInformation(name, email, phone, address);

                    if (upd) {
                        Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Oops! Something went wrong while saving. Please check entries.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Boolean ins = DOP.putInformation(name, email, phone, address);

                    if (ins) {
                        Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Oops! Something went wrong while saving. Please check entries.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
