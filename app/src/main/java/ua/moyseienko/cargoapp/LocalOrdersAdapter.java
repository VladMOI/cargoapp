package ua.moyseienko.cargoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class LocalOrdersAdapter extends RecyclerView.Adapter<LocalOrdersAdapter.MyViewHolder>{

    private ArrayList<HashMap<String, String>> mData;

    public LocalOrdersAdapter (ArrayList<HashMap<String, String>> data) {
        mData = data;
    }


    @NonNull
    @Override
    public LocalOrdersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
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
    }

    @Override
    public int getItemCount() {
        return 0;
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
        }
    }
}
