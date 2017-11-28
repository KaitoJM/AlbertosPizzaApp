package com.example.jm.albertospizza;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jm.albertospizza.model.OrderProductModel;
import com.example.jm.albertospizza.model.ProductModel;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class FragmentOrders extends Fragment {
    ListView orderList;
    TextView txt_total_price;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        orderList = (ListView)view.findViewById(R.id.list_orders);
        txt_total_price = (TextView)view.findViewById(R.id.txt_order_total_price);
        txt_total_price.setText("P" + Double.toString(((Menu)getActivity()).computeTotalPrice()));

        Button btn_order_now = (Button)view.findViewById(R.id.btn_order_now);

        btn_order_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        orders();
    }

    private void orders() {
        OrderAdapter PA = new OrderAdapter(getContext(), R.layout.order_row, ((Menu)getActivity()).OPMList);
        orderList.setAdapter(PA);
    }

    private class OrderAdapter extends ArrayAdapter {
        private List<OrderProductModel> order_model_list;
        private int resource;
        private LayoutInflater inflater;
        public OrderAdapter(Context context, int resource, List<OrderProductModel> objects) {
            super(context, resource, objects);
            order_model_list = objects;
            this.resource = resource;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(resource, null);
            }

            final TextView quantity = (TextView)convertView.findViewById(R.id.txt_order_row_quantity);
            TextView name = (TextView)convertView.findViewById(R.id.txt_order_row_name);
            TextView price = (TextView)convertView.findViewById(R.id.txt_order_row_price);
            TextView txtType = (TextView)convertView.findViewById(R.id.txt_order_row_type);
            TextView txtUnit = (TextView)convertView.findViewById(R.id.txt_order_row_unit);
            ImageButton btn_inc = (ImageButton)convertView.findViewById(R.id.btn_order_increment);
            ImageButton btn_dec = (ImageButton)convertView.findViewById(R.id.btn_order_decrement);

            quantity.setText(Integer.toString(order_model_list.get(position).getQuantity()));
            name.setText(order_model_list.get(position).getProductName());
            price.setText(Double.toString(order_model_list.get(position).getPrice()));
            txtType.setText(order_model_list.get(position).getProductType());
            txtUnit.setText(order_model_list.get(position).getProductUnit());

            final int item_quantity = order_model_list.get(position).getQuantity();
            final double item_price = order_model_list.get(position).getPrice();
            final int index = position;

            btn_inc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double price_by_one = item_price / item_quantity;
                    double dec_price = item_price + price_by_one;
                    order_model_list.get(index).setPrice(dec_price);
                    order_model_list.get(index).setQuantity(item_quantity + 1);

                    txt_total_price.setText("P" + Double.toString(((Menu)getActivity()).computeTotalPrice()));
                    ((Menu)getActivity()).setHeadTotalQuantity();
                    orders();
                }
            });

            btn_dec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double price_by_one = item_price / item_quantity;
                    double dec_price = item_price - price_by_one;
                    int thequantity = item_quantity - 1;
                    order_model_list.get(index).setPrice(dec_price);
                    order_model_list.get(index).setQuantity(thequantity);

                    if (thequantity <= 0) {
                        remove(order_model_list.get(index));
                    }

                    txt_total_price.setText("P" + Double.toString(((Menu)getActivity()).computeTotalPrice()));
                    ((Menu)getActivity()).setHeadTotalQuantity();
                    orders();
                }
            });

            return convertView;
        }
    }
}
