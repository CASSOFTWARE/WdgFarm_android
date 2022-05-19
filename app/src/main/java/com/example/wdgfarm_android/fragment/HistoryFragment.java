package com.example.wdgfarm_android.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.activity.DetailActivity;
import com.example.wdgfarm_android.adapter.WeighingAdapter;
import com.example.wdgfarm_android.databinding.FragmentHistoryBinding;
import com.example.wdgfarm_android.model.Weighing;
import com.example.wdgfarm_android.viewmodel.WeighingViewModel;

import java.io.Serializable;
import java.util.List;

public class HistoryFragment extends Fragment {


    public HistoryFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentHistoryBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);
        View view = binding.getRoot();

        WeighingViewModel weighingViewModel = new ViewModelProvider(getActivity()).get(WeighingViewModel.class);
        WeighingAdapter weighingAdapter = new WeighingAdapter();
        binding.weighingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.weighingRecyclerView.setHasFixedSize(true);

        binding.weighingRecyclerView.setAdapter(weighingAdapter);

        weighingViewModel.getAllWeighings().observe(getActivity(), new Observer<List<Weighing>>() {
            @Override
            public void onChanged(List<Weighing> weighings) {
                weighingAdapter.setWeighings(weighings);
                binding.weighingRecyclerView.setAdapter(weighingAdapter);

            }
        });

        weighingAdapter.setOnItemClickListener(new WeighingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Weighing weighing) {
                Intent intent = new Intent(getActivity().getApplication(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_ID, weighing.getId());

                intent.putExtra(DetailActivity.EXTRA_COMPANY_ID, weighing.getCompanyID());
                intent.putExtra(DetailActivity.EXTRA_COMPANY_CODE, weighing.getCompanyCode());
                intent.putExtra(DetailActivity.EXTRA_COMPANY_NAME, weighing.getCompanyName());

                intent.putExtra(DetailActivity.EXTRA_PRODUCT_ID, weighing.getProductID());
                intent.putExtra(DetailActivity.EXTRA_PRODUCT_CODE, weighing.getProductCode());
                intent.putExtra(DetailActivity.EXTRA_PRODUCT_NAME, weighing.getProductName());
                intent.putExtra(DetailActivity.EXTRA_PRODUCT_PRPICE, weighing.getProductPrice());

                intent.putExtra(DetailActivity.EXTRA_DATE, weighing.getDate());
                intent.putExtra(DetailActivity.EXTRA_TOTAL_WEIGHT, weighing.getTotalWeight());

                intent.putExtra(DetailActivity.EXTRA_BOX_ID, weighing.getBoxID());
                intent.putExtra(DetailActivity.EXTRA_BOX_NAME, weighing.getBoxName());
                intent.putExtra(DetailActivity.EXTRA_BOX_WEIGHT, weighing.getBoxWeight());

                intent.putExtra(DetailActivity.EXTRA_BOX_ACCOUNT, weighing.getBoxAccount());
                intent.putExtra(DetailActivity.EXTRA_PALETTE_WEIGHT, weighing.getPaletteWeight());
                intent.putExtra(DetailActivity.EXTRA_DEDUCTIBLE_WEIGHT, weighing.getDeductibleWeight());
                intent.putExtra(DetailActivity.EXTRA_REAL_WEIGHT, weighing.getRealWeight());

                startActivity(intent);
            }
        });
        return view;
    }
}