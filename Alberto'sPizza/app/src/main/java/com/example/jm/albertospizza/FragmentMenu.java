package com.example.jm.albertospizza;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jm.albertospizza.model.OrderProductModel;
import com.example.jm.albertospizza.model.ProductModel;

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
import java.util.ArrayList;
import java.util.List;

public class FragmentMenu extends Fragment {
    LinearLayout category_container;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here;
        category_container = (LinearLayout) getView().findViewById(R.id.layout_menu_tab);
        get_categories();
        changeCategory(0);
    }


    public void changeCategory(int category_id) {
        Bundle b = new Bundle();
        b.putInt("category", category_id);
        Fragment f = new FragmentMenuProducts();
        f.setArguments(b);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_menu_container, f);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public class get_categories_from_server extends AsyncTask<Integer, String, String> {

        @Override
        protected String doInBackground(Integer... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            BufferedWriter writer = null;

            try {
                URL url = new URL((new ServerURLs()).GET_CATEGORIES);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);

                OutputStream ostream = connection.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(ostream, "UTF-8"));

                connection.connect();

                writer.flush();
                writer.close();


                //result
                InputStream istream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(istream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String theResult = buffer.toString();
                return theResult;

            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONArray categories = new JSONArray(result);
                if (categories.length() > 0) {
                    for (int i=0; i < categories.length(); i++) {
                        JSONObject cat_row = categories.getJSONObject(i);

                        Button btn_tab = new Button(getContext());
                        btn_tab.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        btn_tab.setText(cat_row.getString("name"));
                        btn_tab.setId(cat_row.getInt("id"));
                        btn_tab.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        btn_tab.setTextColor(Color.WHITE);

                        final int category_id = cat_row.getInt("id");
                        btn_tab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changeCategory(category_id);
                            }
                        });
                        category_container.addView(btn_tab);

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void get_categories() {
        if (((Menu)getActivity()).isNetworkAvailable() == true) {
            new get_categories_from_server().execute();
        } else {
            Toast.makeText(getContext(), "No Interget Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
