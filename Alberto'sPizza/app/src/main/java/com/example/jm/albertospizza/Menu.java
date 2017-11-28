package com.example.jm.albertospizza;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jm.albertospizza.model.OrderProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;


public class Menu extends AppCompatActivity {
    ImageButton btn_order, btn_settings;
    public List<OrderProductModel> OPMList;
    public int totalPrice;
    public int totalQuantity;

    TextView txttotalQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btn_order = (ImageButton)findViewById(R.id.btn_order_page);
        btn_settings = (ImageButton)findViewById(R.id.btn_settings);

        OPMList = new ArrayList<>();
        totalPrice = 0;
        totalQuantity = 0;

        txttotalQuantity = (TextView)findViewById(R.id.txt_item_total_quantity);
        setHeadTotalQuantity();

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoOrders();
            }
        });
        goToPage(new FragmentMenu(), false);

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(new FragmentSettings(), true);
            }
        });
    }

    public void goToPage(Fragment fragment, boolean back) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_mainfragment, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (back == true) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    public void gotoOrders() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_mainfragment, new FragmentOrders());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    public double computeTotalPrice() {
        double total = 0;
        for (int i=0; i < OPMList.size(); i++) {
            total += OPMList.get(i).getPrice();
        }

        return total;
    }

    public int computeTotalQuantity() {
        int total = 0;
        for (int i=0; i < OPMList.size(); i++) {
            total += OPMList.get(i).getQuantity();
        }

        return total;
    }

    public void setHeadTotalQuantity() {
        txttotalQuantity.setText(Integer.toString(computeTotalQuantity()));
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
