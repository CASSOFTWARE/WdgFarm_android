package com.example.wdgfarm_android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.model.Weighing;

import java.util.ArrayList;
import java.util.List;

public class WeighingAdapter extends RecyclerView.Adapter<WeighingAdapter.WeighingHolder> {
    private List<Weighing> weighings = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public WeighingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_list, parent, false);
        return new WeighingHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull WeighingHolder holder, int position) {
        Weighing currentWeighing = weighings.get(position);
        holder.textViewDate.setText(String.valueOf(currentWeighing.getDate()));
        holder.textViewCompany.setText(currentWeighing.getCompanyName());
        holder.textViewProduct.setText(currentWeighing.getProductName());
        holder.textViewWeight.setText(String.valueOf(currentWeighing.getRealWeight())+" kg");
    }

    @Override
    public int getItemCount() {
        return weighings.size();
    }

    public void setWeighings(List<Weighing> weighings) {
        this.weighings = weighings;
        notifyDataSetChanged();
    }

    public Weighing getWeighingAt(int position) {
        return weighings.get(position);
    }

    class WeighingHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate;
        private TextView textViewCompany;
        private TextView textViewProduct;
        private TextView textViewWeight;

        public WeighingHolder(View itemView) {
            super(itemView);

            textViewDate = itemView.findViewById(R.id.tv_date);
            textViewCompany = itemView.findViewById(R.id.tv_company);
            textViewProduct = itemView.findViewById(R.id.tv_product);
            textViewWeight = itemView.findViewById(R.id.tv_weight);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(weighings.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Weighing weighing);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
