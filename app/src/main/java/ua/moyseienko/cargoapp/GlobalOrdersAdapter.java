package ua.moyseienko.cargoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import ua.moyseienko.cargoapp.services.externaldb.ExternalAlterOrder;
import ua.moyseienko.cargoapp.services.externaldb.ExternalSelectOrderByEmail;
import ua.moyseienko.cargoapp.services.localdb.LocalSelectUser;

public class GlobalOrdersAdapter extends RecyclerView.Adapter<GlobalOrdersAdapter.MyViewHolder> {
    View view;
    private ArrayList<HashMap<String, String>> mData;

    public GlobalOrdersAdapter(ArrayList<HashMap<String, String>> data) {
        mData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        if(mData.size() == 0 || mData == null){
            Toast.makeText(view.getContext(), "No orders found!", Toast.LENGTH_SHORT).show();
        }
        return new MyViewHolder(view);
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HashMap<String, String> itemData = mData.get(position);
        HashMap<String, String> formatedData = parseData(itemData);
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
        holder.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on " + position);

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.btnApply.getContext());
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to apply for this order?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("Dialog yes");
                        int id = Integer.parseInt(itemData.get("id"));
                        applyForOrder(id);
                        dialogInterface.dismiss();
                        mData.remove(position);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("Dialog no");
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void applyForOrder(int orderId){
        LocalSelectUser selectUser = new LocalSelectUser();
        ArrayList<HashMap<String, String>> resultSelectUser = selectUser.selectUser(view.getContext());
        System.out.println("Result select = " + resultSelectUser);

        HashMap<String,String> localUser = resultSelectUser.get(0);
        String email = localUser.get("email");
        System.out.println("Email form local db  = " + email);

        //new thread
        try {
            alterOrderRequest(orderId, email);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void alterOrderRequest(int id, String email) throws InterruptedException {
        Thread alterOrderThread = new Thread(
        ){
            @Override
            public void run() {
                ExternalAlterOrder externalAlterOrder = new ExternalAlterOrder();
                String result = externalAlterOrder.alterOrder(id, email);
            }
        };
        alterOrderThread.start();
        alterOrderThread.join();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

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
        Button btnApply;
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
            btnApply = itemView.findViewById(R.id.btnApply);
        }
    }


}
