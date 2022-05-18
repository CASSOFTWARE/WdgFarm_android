package com.example.wdgfarm_android.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.adapter.WeighingAdapter;
import com.example.wdgfarm_android.databinding.FragmentHistoryBinding;
import com.example.wdgfarm_android.model.Weighing;
import com.example.wdgfarm_android.viewmodel.WeighingViewModel;

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

        return view;
    }
}