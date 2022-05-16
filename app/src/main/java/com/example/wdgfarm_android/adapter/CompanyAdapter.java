package com.example.wdgfarm_android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.model.Company;

import java.util.ArrayList;
import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyHolder> {
    private List<Company> companys = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public CompanyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_info_list, parent, false);
        return new CompanyHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull CompanyHolder holder, int position) {
        Company currentCompany = companys.get(position);
        holder.textViewCode.setText(String.valueOf(currentCompany.getCode()));
        holder.textViewName.setText(currentCompany.getName());
    }

    @Override
    public int getItemCount() {
        return companys.size();
    }

    public void setCompanys(List<Company> companys) {
        this.companys = companys;
        notifyDataSetChanged();
    }

    public Company getCompanyAt(int position) {
        return companys.get(position);
    }

    class CompanyHolder extends RecyclerView.ViewHolder {
        private TextView textViewCode;
        private TextView textViewName;

        public CompanyHolder(View itemView) {
            super(itemView);

            textViewCode = itemView.findViewById(R.id.tv_code_box);
            textViewName = itemView.findViewById(R.id.tv_name_weight);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(companys.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Company company);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
