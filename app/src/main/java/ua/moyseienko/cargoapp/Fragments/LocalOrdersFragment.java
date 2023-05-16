package ua.moyseienko.cargoapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import ua.moyseienko.cargoapp.GlobalOrdersAdapter;
import ua.moyseienko.cargoapp.LocalOrdersAdapter;
import ua.moyseienko.cargoapp.R;
import ua.moyseienko.cargoapp.SelectOrdersCallback;
import ua.moyseienko.cargoapp.services.externaldb.ExternalCreateOrder;
import ua.moyseienko.cargoapp.services.externaldb.ExternalSelectOrderByEmail;
import ua.moyseienko.cargoapp.services.externaldb.ExternalSelectOrders;
import ua.moyseienko.cargoapp.services.localdb.LocalSelectUser;

public class LocalOrdersFragment extends Fragment  {
    View rootview;
    String localOrders;
    RecyclerView recyclerView;
    String mEmail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview =  inflater.inflate(R.layout.fragment_local_orders, container, false);
        recyclerView = rootview.findViewById(R.id.rvLocal);
        LocalSelectUser selectUser = new LocalSelectUser();
        ArrayList<HashMap<String, String>> user = selectUser.selectUser(rootview.getContext());
        HashMap<String, String> map = user.get(0);
        String email = map.get("email");
        mEmail = email;
        System.out.println("Email found: " + email);
        System.out.println("Trying to select orders...");
        ExternalSelectOrderByEmail externalSelectOrders = new ExternalSelectOrderByEmail();
        localOrders = externalSelectOrders.selectOrderByEmail(email);
        getOrders(new OrdersCallback() {
            @Override
            public void onOrdersReceived(ArrayList<HashMap<String, String>> localOrders) {
                // делаем что-то с полученными данными
                LocalOrdersAdapter adapter = new LocalOrdersAdapter(localOrders);
                System.out.println("localOrders == " + localOrders);
                recyclerView.setAdapter(adapter);
            }
        });

        System.out.println("localOrders = " + localOrders);
        return rootview;
    }

    public ArrayList<HashMap<String, String>> parseOrdersString(String ordersString) {
        ArrayList<HashMap<String, String>> ordersList = new ArrayList<>();
        try {

            JSONArray ordersArray = new JSONArray(ordersString);
            if(ordersString != ""){
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ordersList;
    }

    public interface OrdersCallback {
        void onOrdersReceived(ArrayList<HashMap<String, String>> orders);
    }

    public void getOrders(OrdersCallback callback) {
        new Thread(new Runnable() {
            public void run() {
                ExternalSelectOrderByEmail externalSelectOrders = new ExternalSelectOrderByEmail();
                String response = externalSelectOrders.selectOrderByEmail(mEmail);
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
}