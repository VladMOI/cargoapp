package ua.moyseienko.cargoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import ua.moyseienko.cargoapp.services.externaldb.ExternalDeleteOrder;
import ua.moyseienko.cargoapp.services.externaldb.ExternalSelectUser;
import ua.moyseienko.cargoapp.services.externaldb.ExternalUnAttachOrder;
import ua.moyseienko.cargoapp.services.externaldb.ExternalUpdateUserBalance;
import ua.moyseienko.cargoapp.services.localdb.LocalUpdateUser;

public class LocalOrdersAdapter extends RecyclerView.Adapter<LocalOrdersAdapter.MyViewHolder>{
    private ArrayList<HashMap<String, String>> mData;
    View view;

    public LocalOrdersAdapter (ArrayList<HashMap<String, String>> data) {
        mData = data;
        System.out.println("Data in adapter = " + mData);

    }


    @NonNull
    @Override
    public LocalOrdersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_orders_fragment, parent, false);
        return new LocalOrdersAdapter.MyViewHolder(view);
    }

    public HashMap<String, String> parseData(HashMap<String, String> map)
    {
        HashMap<String, String> formatedData = new HashMap<>();
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            // Получение и преобразование дат
            Date receivedDate = inputDateFormat.parse((String) map.get("recievedAt"));
            Date sentDate = inputDateFormat.parse((String) map.get("sendedAt"));

            // Форматирование дат в нужном формате
            String formattedReceivedDate = outputDateFormat.format(receivedDate);
            String formattedSentDate = outputDateFormat.format(sentDate);

            // Вывод результатов
            System.out.println("Received Date: " + formattedReceivedDate);
            System.out.println("Sent Date: " + formattedSentDate);
            formatedData.put("recievedAt", formattedReceivedDate);
            formatedData.put("sendedAt", formattedSentDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedData;
    }

    @Override
    public void onBindViewHolder(@NonNull LocalOrdersAdapter.MyViewHolder holder, int position) {
        HashMap<String, String> itemData = mData.get(position);
        System.out.println("ItemData = " + itemData);
        HashMap<String, String> formatedData = parseData(itemData);
        System.out.println("Item " + position + ", = " + formatedData);
        holder.textViewId.setText(itemData.get("id"));
        holder.textViewAdressFrom.setText(itemData.get("adressfrom"));
        holder.textViewAdressTo.setText(itemData.get("adressTo"));
        holder.textViewSendedAt.setText(formatedData.get("sendedAt"));
        holder.textViewRecievedAt.setText(formatedData.get("recievedAt"));
        holder.textViewPrice.setText(itemData.get("price"));
        holder.textViewDistance.setText(itemData.get("distance"));
        holder.textViewPackageSize.setText(itemData.get("packageSize"));
        holder.textViewPackageType.setText(itemData.get("packageType"));
        holder.textViewPackageWeight.setText(itemData.get("packageWeight"));
        holder.textViewDriver.setText(itemData.get("driver"));

        holder.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Button done");
                int id = Integer.parseInt(holder.textViewId.getText().toString());
                try{
                    orderDone(id);
                    mData.remove(position);
                    notifyDataSetChanged();
                    int orderPrice = Integer.parseInt(holder.textViewPrice.getText().toString());
                    String email = holder.textViewDriver.getText().toString();
                    updateExternalUser(orderPrice, email);
                    updateLocalUser(orderPrice);
                    Toast.makeText(view.getContext(), "Order done!" + orderPrice + "$ added to your balance.", Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Button cancel");

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to cancel this order?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int id = Integer.parseInt(holder.textViewId.getText().toString());
                        try {
                            orderCancel(id);
                            mData.remove(position);
                            notifyDataSetChanged();
                            dialogInterface.dismiss();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }



    public void updateExternalUser(int orderPrice, String email) throws InterruptedException{

        Thread updateUserThread = new Thread(){
            @Override
            public void run() {
                super.run();
                ExternalUpdateUserBalance updateUserBalance = new ExternalUpdateUserBalance();
                String result = updateUserBalance.updateUser(email, orderPrice);
            }
        };
        updateUserThread.start();
        updateUserThread.join();
    }
    public void updateLocalUser(int orderPrice){
        LocalUpdateUser localUpdateUser = new LocalUpdateUser();
        long rowsAffected = localUpdateUser.update(view.getContext(), orderPrice);
        System.out.println("updateLocalUser: rowsAffected = " + rowsAffected);
    }
    public void orderCancel(int id) throws  InterruptedException{
        Thread orderCancelThread = new Thread(){
            @Override
            public void run() {
                super.run();
                ExternalUnAttachOrder externalUnAttachOrder = new ExternalUnAttachOrder();
                String result = externalUnAttachOrder.unAttachOrder(id);
            }
        };
        orderCancelThread.start();
        orderCancelThread.join();
    }

    public void orderDone(int id) throws InterruptedException {
        Thread orderDoneThread = new Thread(){
            @Override
            public void run() {
                super.run();
                ExternalDeleteOrder externalDeleteOrder = new ExternalDeleteOrder();
                String result = externalDeleteOrder.deleteOrder(id);
            }
        };
        orderDoneThread.start();
        orderDoneThread.join();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewId;
        TextView textViewAdressFrom;
        TextView textViewAdressTo;
        TextView textViewSendedAt;
        TextView textViewRecievedAt;
        TextView textViewPrice;
        TextView textViewDistance;
        TextView textViewPackageSize;
        TextView textViewPackageType;
        TextView textViewPackageWeight;
        TextView textViewDriver;
        Button btnCancel;
        Button btnDone;


        public MyViewHolder(View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.text_view_id);
            textViewAdressFrom = itemView.findViewById(R.id.text_view_adress_from);
            textViewAdressTo = itemView.findViewById(R.id.text_view_adress_to);
            textViewSendedAt = itemView.findViewById(R.id.text_view_sended_at);
            textViewRecievedAt = itemView.findViewById(R.id.text_view_recieved_at);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            textViewDistance = itemView.findViewById(R.id.text_view_distance);
            textViewPackageSize = itemView.findViewById(R.id.text_view_package_size);
            textViewPackageType = itemView.findViewById(R.id.text_view_package_type);
            textViewPackageWeight = itemView.findViewById(R.id.text_view_package_weight);
            textViewDriver = itemView.findViewById(R.id.text_view_driver);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            btnDone = itemView.findViewById(R.id.btnDone);
        }
    }
}
