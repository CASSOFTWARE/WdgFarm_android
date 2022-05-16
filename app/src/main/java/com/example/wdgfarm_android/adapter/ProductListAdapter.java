package com.example.wdgfarm_android.adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.model.Product;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Product> list;

    public ProductListAdapter(){ list = new ArrayList<>(); }

    @NonNull
    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_product_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Product item = list.get(position);

        viewHolder.tv_code.setText(item.getCode());
        viewHolder.tv_name.setText(item.getName());
        viewHolder.tv_price.setText(item.getPrice());

        viewHolder.cl_product_item_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //TODO: 터치 이벤트
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @BindingAdapter("bind:item")
    public static void bindItem(RecyclerView recyclerView, ObservableArrayList<Product> items) {
        ProductListAdapter adapter = (ProductListAdapter) recyclerView.getAdapter();

        if (adapter != null) {
            adapter.setItem(items);
        }
    }

    private void setItem(ArrayList<Product> items){
        if(items != null){
            list = items;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout cl_product_item_layout;
        private TextView tv_code;
        private TextView tv_name;
        private TextView tv_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cl_product_item_layout = itemView.findViewById(R.id.cl_product_item_layout);
            tv_code = itemView.findViewById(R.id.tv_code);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);

        }
    }
}
