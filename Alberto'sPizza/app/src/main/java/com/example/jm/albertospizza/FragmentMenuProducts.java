package com.example.jm.albertospizza;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jm.albertospizza.model.OrderProductModel;
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

import static java.security.AccessController.getContext;

public class FragmentMenuProducts extends Fragment {
    private int category_id;
    List<ProductModel> Product_List;
    GridView menuGrid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category_id = getArguments().getInt("category");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_menu_products, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
//        Toast.makeText(getContext(), Integer.toString(category_id), Toast.LENGTH_SHORT).show();
        menuGrid = (GridView)view.findViewById(R.id.grid_menu);
        thumbs();
    }

    private void thumbs() {
        get_products(category_id);
    }

    private class ProductAdapter extends ArrayAdapter {
        private List<ProductModel> product_model_list;
        private int resource;
        private LayoutInflater inflater;
        public ProductAdapter(Context context, int resource, List<ProductModel> objects) {
            super(context, resource, objects);
            product_model_list = objects;
            this.resource = resource;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(resource, null);
            }

            ImageView img = (ImageView)convertView.findViewById(R.id.img_menu_product);
            TextView name = (TextView)convertView.findViewById(R.id.txt_thumb_product_name);
            TextView price = (TextView)convertView.findViewById(R.id.txt_thumb_product_price);
            RelativeLayout thumb = (RelativeLayout)convertView.findViewById(R.id.layout_thumb_parent);
            FloatingActionButton fabOrder = (FloatingActionButton)convertView.findViewById(R.id.fab_thumb_add_to_order);

            name.setText(product_model_list.get(position).getName());
            price.setText(product_model_list.get(position).getPrice());

            Picasso.with(getContext()).load(product_model_list.get(position).getSmall()).placeholder(R.drawable.no_image_product)
                    .error(R.drawable.no_image_product)
                    .into(img, new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });


            final int product_index = product_model_list.get(position).getId();

            thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentProduct fproduct = new FragmentProduct();
                    Bundle args = new Bundle();
                    args.putInt("product_index", product_index);
                    fproduct.setArguments(args);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.frame_mainfragment, fproduct)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            .commit();

                }
            });

            final int productID = product_model_list.get(position).getId();
            final String productName = product_model_list.get(position).getName();
            final Double productPrice = product_model_list.get(position).getPrice_value();
            final int productType = product_model_list.get(position).getType_value();
            final int productUnit = product_model_list.get(position).getUnit_value();
            final String productTypeName = product_model_list.get(position).getType();
            final String productUnitName = product_model_list.get(position).getUnit();
            fabOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean find_index = false;

                    //search for likeness in order list
                    for (int i=0; i < ((Menu)getActivity()).OPMList.size(); i++) {
                        int type = ((Menu)getActivity()).OPMList.get(i).getType();
                        int unit = ((Menu)getActivity()).OPMList.get(i).getUnit();
                        int id = ((Menu)getActivity()).OPMList.get(i).getProduct_id();
                        Double price = ((Menu)getActivity()).OPMList.get(i).getPrice();
                        int quantity = ((Menu)getActivity()).OPMList.get(i).getQuantity();

                        if ((type == productType) && (id == productID) && (unit == productUnit)) {
                            find_index = true;
                            ((Menu)getActivity()).OPMList.get(i).setQuantity(quantity + 1);
                            ((Menu)getActivity()).OPMList.get(i).setPrice(price + productPrice);
                        }
                    }

                    if (!find_index) {
                        OrderProductModel OPM = new OrderProductModel();
                        OPM.setProduct_id(productID);
                        OPM.setQuantity(1);
                        OPM.setProductName(productName);
                        OPM.setPrice(productPrice);
                        OPM.setProductType(productTypeName);
                        OPM.setProductUnit(productUnitName);
                        OPM.setType(productType);
                        OPM.setUnit(productUnit);
                        ((Menu)getActivity()).OPMList.add(OPM);
                    }

                    ((Menu)getActivity()).setHeadTotalQuantity();
                }
            });

            return convertView;
        }
    }

    public class get_products_from_server extends AsyncTask<Integer, String, String> {

        @Override
        protected String doInBackground(Integer... params) {
            int category = params[0];

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            BufferedWriter writer = null;

            try {
                URL url = new URL((new ServerURLs()).GET_PRODUCTS + "&s2=" + Integer.toString(category));
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);

                OutputStream ostream = connection.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(ostream, "UTF-8"));

//                String data_string;
//                data_string = URLEncoder.encode("s2", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(category), "UTF-8");
//
//                writer.write(data_string);
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
                JSONArray products = new JSONArray(result);
                if (products.length() > 0) {
                    Product_List = new ArrayList<>();
                    for (int i=0; i < products.length(); i++) {
                        JSONObject profuct_row = products.getJSONObject(i);
                        ProductModel PM = new ProductModel();

                        PM.setId(profuct_row.getInt("id"));
                        PM.setName(profuct_row.getString("name"));
                        PM.setPrice("P" + Double.toString(profuct_row.getDouble("price")));
                        PM.setPrice_value(profuct_row.getDouble("price"));
                        PM.setPhoto(profuct_row.getString("photo_url"));
                        PM.setMedium(profuct_row.getString("photo_url_medium"));
                        PM.setSmall(profuct_row.getString("photo_url_small"));
                        PM.setSmaller(profuct_row.getString("photo_url_smaller"));
                        PM.setType(profuct_row.getString("type"));
                        PM.setType_value(profuct_row.getInt("type_id"));
                        PM.setUnit(profuct_row.getString("unit"));
                        PM.setUnit_value(profuct_row.getInt("unit_id"));


                        Product_List.add(PM);
                    }


                    ProductAdapter PA = new ProductAdapter(getContext(), R.layout.menu_thumb, Product_List);
                    menuGrid.setAdapter(PA);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                menuGrid.setAdapter(null);
            }
        }
    }

    public void get_products(int category_id) {
        if (((Menu)getActivity()).isNetworkAvailable() == true) {
            new get_products_from_server().execute(category_id);
        } else {
            Toast.makeText(getContext(), "No Interget Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
