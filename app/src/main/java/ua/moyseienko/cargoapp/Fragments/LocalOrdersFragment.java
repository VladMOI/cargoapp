package ua.moyseienko.cargoapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import ua.moyseienko.cargoapp.services.externaldb.ExternalCreateOrder;
import ua.moyseienko.cargoapp.services.externaldb.ExternalSelectOrderByEmail;
import ua.moyseienko.cargoapp.services.externaldb.ExternalSelectOrders;
import ua.moyseienko.cargoapp.services.localdb.LocalSelectUser;

public class LocalOrdersFragment extends Fragment  {
    View rootview;
    RecyclerView recyclerView;
    String mEmail;
    ArrayList<HashMap<String, String>> orders;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview =  inflater.inflate(R.layout.fragment_local_orders, container, false);

        recyclerView = rootview.findViewById(R.id.rvLocal);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        LocalSelectUser selectUser = new LocalSelectUser();
        ArrayList<HashMap<String, String>> user = selectUser.selectUser(rootview.getContext());
        HashMap<String, String> map = user.get(0);
        String email = map.get("email");
        mEmail = email;
        System.out.println("Email found: " + email);
        System.out.println("Trying to select orders...");


        try {
            getOrders();
            LocalOrdersAdapter adapter = new LocalOrdersAdapter(orders);
            recyclerView.setAdapter(adapter);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return rootview;
    }

    public Thread.State getOrders() throws InterruptedException {
        Thread.State status;
        Thread getOrdersThread =  new Thread(){
            public void run(){
                ExternalSelectOrderByEmail externalSelectOrderByEmail = new ExternalSelectOrderByEmail();
                String resultSelect = externalSelectOrderByEmail.selectOrderByEmail(mEmail);
                ArrayList<HashMap<String, String>> parsedResult = parseOrdersString(resultSelect);
                System.out.println("parsedResult = " + parsedResult);
                orders = parsedResult;
            }
        };
        getOrdersThread.start();
        status = getOrdersThread.getState();
        System.out.println("Join... status = " + status);
        getOrdersThread.join();
        System.out.println("After join... status = " + status);
        status = getOrdersThread.getState();
        return status;
    }

    public ArrayList<HashMap<String, String>> parseOrdersString(String ordersString) {
        ArrayList<HashMap<String, String>> ordersList = new ArrayList<>();
        try {

            JSONArray ordersArray = new JSONArray(ordersString);
            if (ordersString != "") {
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

}