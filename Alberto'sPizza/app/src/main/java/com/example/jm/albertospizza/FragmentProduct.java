package com.example.jm.albertospizza;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jm.albertospizza.model.OrderProductModel;
import com.example.jm.albertospizza.model.ProductCombinationModel;
import com.example.jm.albertospizza.model.ProductModel;
import com.squareup.picasso.Picasso;

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

public class FragmentProduct extends Fragment {

    int curre_index = 0;

    ImageButton btn_typeNext, getBtn_typePrev;
    TextView txt_type, txt_unit, txt_price, txt_name, txt_description;
    ImageView img;
    FloatingActionButton fab_add;

    int product_index = 0;
    String product_name;
    String product_description;
    List<ProductCombinationModel> Combination_List;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        product_index = getArguments().getInt("product_index", 0);

        return inflater.inflate(R.layout.fragment_fragment_product, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        btn_typeNext = (ImageButton)view.findViewById(R.id.btn_product_nextType);
        getBtn_typePrev = (ImageButton)view.findViewById(R.id.btn_product_prevType);
        txt_price = (TextView)view.findViewById(R.id.txt_product_page_price);
        txt_type = (TextView)view.findViewById(R.id.txt_product_type);
        txt_unit = (TextView)view.findViewById(R.id.txt_product_unit);
        txt_name = (TextView)view.findViewById(R.id.txt_product_name);
        txt_description = (TextView)view.findViewById(R.id.txt_product_description);
        fab_add = (FloatingActionButton)view.findViewById(R.id.fab_product_add);
        img = (ImageView)view.findViewById(R.id.img_product);

//        changeType();

        btn_typeNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curre_index < Combination_List.size() - 1) {
                    curre_index++;
                } else {
                    curre_index = 0;
                }
                changeType();
            }
        });

        getBtn_typePrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curre_index <= 0) {
                    curre_index = Combination_List.size() - 1;
                } else {
                    curre_index--;
                }
                changeType();
            }
        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_to_cart();
            }
        });

        fab_add.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                custom_quantity_input(v);
                return true;
            }
        });

        display_product_details();
    }

    public void display_product_details() {
        if (((Menu)getActivity()).isNetworkAvailable() == true) {
            new get_product_detail_from_server().execute(product_index);
        } else {
            Toast.makeText(getContext(), "No Interget Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeType() {
        txt_type.setText(Combination_List.get(curre_index).getType());
        txt_unit.setText(Combination_List.get(curre_index).getUnit());
        txt_price.setText("P" + Double.toString(Combination_List.get(curre_index).getPrice()));
    }

    public void add_to_cart() {
        boolean find_index = false;

        //search for likeness in order list
        for (int i=0; i < ((Menu)getActivity()).OPMList.size(); i++) {
            int type = ((Menu)getActivity()).OPMList.get(i).getType();
            int unit = ((Menu)getActivity()).OPMList.get(i).getUnit();
            int id = ((Menu)getActivity()).OPMList.get(i).getProduct_id();
            Double price = ((Menu)getActivity()).OPMList.get(i).getPrice();
            int quantity = ((Menu)getActivity()).OPMList.get(i).getQuantity();

            if ((type == Combination_List.get(curre_index).getType_id()) && (id == product_index) && (unit == Combination_List.get(curre_index).getUnit_id())) {
                find_index = true;
                ((Menu)getActivity()).OPMList.get(i).setQuantity(quantity + 1);
                Double additional_price = Combination_List.get(curre_index).getPrice();
                ((Menu)getActivity()).OPMList.get(i).setPrice(price + additional_price);
            }
        }

        if (!find_index) {
            OrderProductModel OPM = new OrderProductModel();
            OPM.setProduct_id(product_index);
            OPM.setQuantity(1);
            OPM.setProductName(product_name);
            OPM.setPrice(Combination_List.get(curre_index).getPrice());
            OPM.setProductType(Combination_List.get(curre_index).getType());
            OPM.setProductUnit(Combination_List.get(curre_index).getUnit());
            OPM.setType(Combination_List.get(curre_index).getType_id());
            OPM.setUnit(Combination_List.get(curre_index).getUnit_id());
            ((Menu)getActivity()).OPMList.add(OPM);
        }

        ((Menu)getActivity()).setHeadTotalQuantity();
    }

    public void custom_quantity_input(View view) {
        Product_custom_input_monitor PCIM = new Product_custom_input_monitor();
        Bundle args = new Bundle();
        args.putInt("product_id", product_index);
        PCIM.setArguments(args);

        getFragmentManager().beginTransaction()
                .replace(R.id.frame_product_image, new Product_custom_input_monitor())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

        getFragmentManager().beginTransaction()
                .replace(R.id.frame_product_details, new Product_custom_input_keypad())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

        FloatingActionButton me = (FloatingActionButton)view;
        me.setImageResource(R.mipmap.ic_check_white_24dp);
    }

    public class get_product_detail_from_server extends AsyncTask<Integer, String, String> {

        @Override
        protected String doInBackground(Integer... params) {
            int product_id = params[0];

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            BufferedWriter writer = null;

            try {
                URL url = new URL((new ServerURLs()).GET_PRODUCT + Integer.toString(product_id));
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
                JSONObject product = new JSONObject(result);
                product_name = product.getString("name");
                product_description = product.getString("description");

                txt_name.setText(product_name);
                txt_description.setText(product_description);
                txt_type.setText(product.getString("default_type"));
                txt_unit.setText(product.getString("default_unit"));
                txt_price.setText("P" + Double.toString(product.getDouble("default_price")));
                set_image(product.getString("photo_url_medium"));

                JSONArray combination = product.getJSONArray("price");

                if (combination.length() > 0) {
                    Combination_List = new ArrayList<>();
                    for (int i=0; i < combination.length(); i++) {
                        JSONObject comb_row = combination.getJSONObject(i);
                        ProductCombinationModel PCM = new ProductCombinationModel();

                        PCM.setType_id(comb_row.getInt("type_id"));
                        PCM.setUnit_id(comb_row.getInt("unit_id"));
                        PCM.setType(comb_row.getString("type"));
                        PCM.setUnit(comb_row.getString("unit"));
                        PCM.setPrice(comb_row.getDouble("price"));

                        Combination_List.add(PCM);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void set_image(String src) {
        Picasso.with(getContext()).load(src).placeholder(R.drawable.no_image_product)
                .error(R.drawable.no_image_product)
                .into(img, new com.squareup.picasso.Callback() {

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

    }
}
