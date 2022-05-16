package com.example.wdgfarm_android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.model.Box;

import java.util.ArrayList;
import java.util.List;

public class BoxAdapter extends RecyclerView.Adapter<BoxAdapter.BoxHolder> {
    private List<Box> boxs = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public BoxHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_info_list, parent, false);
        return new BoxHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull BoxHolder holder, int position) {
        Box currentBox = boxs.get(position);
        holder.textViewCodeBox.setText(currentBox.getName());
        holder.textViewNameWeight.setText(String.valueOf(currentBox.getWeight()));

    }

    @Override
    public int getItemCount() {
        return boxs.size();
    }

    public void setBoxs(List<Box> boxs) {
        this.boxs = boxs;
        notifyDataSetChanged();
    }

    public Box getBoxAt(int position) {
        return boxs.get(position);
    }

    class BoxHolder extends RecyclerView.ViewHolder {
        private TextView textViewCodeBox;
        private TextView textViewNameWeight;

        public BoxHolder(View itemView) {
            super(itemView);

            textViewCodeBox = itemView.findViewById(R.id.tv_code_box);
            textViewNameWeight = itemView.findViewById(R.id.tv_name_weight);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(boxs.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Box box);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
