package ua.moyseienko.cargoapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import ua.moyseienko.cargoapp.GlobalOrdersAdapter;
import ua.moyseienko.cargoapp.R;
import ua.moyseienko.cargoapp.services.externaldb.ExternalSelectOrders;

public class GlobalCatalogueFragment extends Fragment {
    View rootView;
    RecyclerView recyclerView;
    public GlobalCatalogueFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_gcatalogue, container, false);
        recyclerView = rootView.findViewById(R.id.rvGlobal);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        GlobalOrdersAdapter adapter = new GlobalOrdersAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);


        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(clickListener);
        getOrders(new OrdersCallback() {
            @Override
            public void onOrdersReceived(ArrayList<HashMap<String, String>> orders) {
                // делаем что-то с полученными данными
                GlobalOrdersAdapter adapter = new GlobalOrdersAdapter(orders);
                System.out.println("Orders " + orders);
                recyclerView.setAdapter(adapter);
            }
        });

        return rootView;
    }

    public View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.fab:
                    startActivity(new Intent(rootView.getContext(), CreateOrder.class));
                    break;
            }
        }
    };

    public interface OrdersCallback {
        void onOrdersReceived(ArrayList<HashMap<String, String>> orders);
    }

    public void getOrders(OrdersCallback callback) {
        new Thread(new Runnable() {
            public void run() {
                ExternalSelectOrders externalSelectOrders = new ExternalSelectOrders();
                String response = externalSelectOrders.selectOrders();
                System.out.println("Response = " + response);
                ArrayList<HashMap<String, String>> result = parseOrdersString(response); // метод, который будет парсить ответ

                // выполнение обновления адаптера в основном потоке
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onOrdersReceived(result);
                        System.out.println("ParsedData = " + result);
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        }).start();
    }
    public ArrayList<HashMap<String, String>> parseOrdersString(String ordersString) {
        ArrayList<HashMap<String, String>> ordersList = new ArrayList<>();
        try {
            JSONArray ordersArray = new JSONArray(ordersString);
            for (int i = 0; i < ordersArray.length(); i++) {
                JSONArray orderData = ordersArray.getJSONArray(i);
                HashMap<String, String> order = new HashMap<>();
                order.put("id", String.valueOf(orderData.getInt(0)));
                order.put("adressfrom", orderData.getString(1));
                order.put("adressTo", orderData.getString(2));
                order.put("sendedAt", orderData.getString(3));
                order.put("recievedAt", orderData.getString(4));
                order.put("price", String.valueOf(orderData.getInt(5)));
                order.put("distance", String.valueOf(orderData.getInt(6)));
                order.put("packageSize", orderData.getString(7));
                order.put("packageType", orderData.getString(8));
                order.put("packageWeight", String.valueOf(orderData.getInt(9)));
                order.put("driver", orderData.getString(10));
                ordersList.add(order);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ordersList;
    }
}