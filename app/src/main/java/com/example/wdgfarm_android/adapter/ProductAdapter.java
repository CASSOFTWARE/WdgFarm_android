package com.example.wdgfarm_android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private List<Product> products = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_list, parent, false);
        return new ProductHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product currentProduct = products.get(position);
        holder.textViewCode.setText(String.valueOf(currentProduct.getCode()));
        holder.textViewName.setText(currentProduct.getName());
        holder.textViewPrice.setText(String.valueOf(currentProduct.getPrice()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public Product getProductAt(int position) {
        return products.get(position);
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        private TextView textViewCode;
        private TextView textViewName;
        private TextView textViewPrice;

        public ProductHolder(View itemView) {
            super(itemView);

            textViewCode = itemView.findViewById(R.id.tv_code);
            textViewName = itemView.findViewById(R.id.tv_name);
            textViewPrice = itemView.findViewById(R.id.tv_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(products.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
