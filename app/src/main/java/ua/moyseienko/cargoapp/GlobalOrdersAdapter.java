package ua.moyseienko.cargoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class GlobalOrdersAdapter extends RecyclerView.Adapter<GlobalOrdersAdapter.MyViewHolder> {

    private ArrayList<HashMap<String, String>> mData;

    public GlobalOrdersAdapter(ArrayList<HashMap<String, String>> data) {
        mData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HashMap<String, String> itemData = mData.get(position);
        holder.textViewId.setText(itemData.get("id"));
        holder.textViewAdressFrom.setText(itemData.get("adressfrom"));
        holder.textViewAdressTo.setText(itemData.get("adressTo"));
        holder.textViewSendedAt.setText(itemData.get("sendedAt"));
        holder.textViewRecievedAt.setText(itemData.get("recievedAt"));
        holder.textViewPrice.setText(itemData.get("price"));
        holder.textViewDistance.setText(itemData.get("distance"));
        holder.textViewPackageSize.setText(itemData.get("packageSize"));
        holder.textViewPackageType.setText(itemData.get("packageType"));
        holder.textViewPackageWeight.setText(itemData.get("packageWeight"));
        holder.textViewDriver.setText(itemData.get("driver"));
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
