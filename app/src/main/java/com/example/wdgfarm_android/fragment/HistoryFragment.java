package com.example.wdgfarm_android.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.activity.DetailActivity;
import com.example.wdgfarm_android.activity.InfoAddActivity;
import com.example.wdgfarm_android.adapter.SpinnerAdapter;
import com.example.wdgfarm_android.adapter.WeighingAdapter;
import com.example.wdgfarm_android.api.ApiListener;
import com.example.wdgfarm_android.api.GroupPurchaseApi;
import com.example.wdgfarm_android.api.PurchaseApi;
import com.example.wdgfarm_android.databinding.FragmentHistoryBinding;
import com.example.wdgfarm_android.model.Weighing;
import com.example.wdgfarm_android.utils.Constants;
import com.example.wdgfarm_android.utils.DatePickerFragment;
import com.example.wdgfarm_android.viewmodel.ApiViewModel;
import com.example.wdgfarm_android.viewmodel.HistoryViewModel;
import com.example.wdgfarm_android.viewmodel.WeighingViewModel;

import org.json.JSONException;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryFragment extends Fragment {

    private SpinnerAdapter spinnerAdapter;
    private static final String DIALOG_DATE = "DialogDateTag";

    private static final int REQUEST_FROM_DATE = 1;
    private static final int REQUEST_TO_DATE = 2;

    private static SimpleDateFormat dateFormat;    //  2022/05/20
    private static SimpleDateFormat format;        //  2022/05/20 ?????? 5:15

    private static HistoryViewModel historyViewModel;
    private static WeighingViewModel weighingViewModel;
    private static FragmentHistoryBinding binding;
    private static ApiViewModel apiViewModel;
    private int failCnt = 0;

    public HistoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);
        View view = binding.getRoot();

        String[] data = {"??????", "??????", "??????", "?????????"};
        spinnerAdapter = new SpinnerAdapter(getContext(), data);

        weighingViewModel = new ViewModelProvider(getActivity()).get(WeighingViewModel.class);
        historyViewModel = new ViewModelProvider(getActivity()).get(HistoryViewModel.class);
        apiViewModel = new ViewModelProvider(getActivity()).get(ApiViewModel.class);


        WeighingAdapter weighingAdapter = new WeighingAdapter();
        binding.weighingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.weighingRecyclerView.setHasFixedSize(true);

        binding.weighingRecyclerView.setAdapter(weighingAdapter);

//        weighingViewModel.getAllWeighings().observe(getActivity(), new Observer<List<Weighing>>() {
//            @Override
//            public void onChanged(List<Weighing> weighings) {
//                weighingAdapter.setWeighings(weighings);
//                binding.historySearchCount.setText(String.valueOf(weighingAdapter.getItemCount()) + " ???");
//                binding.weighingRecyclerView.setAdapter(weighingAdapter);
//
//            }
//        });
        format = new SimpleDateFormat("yyyy/MM/dd a hh:mm");
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        historyViewModel.updateDate.observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                historyViewModel.dateFrom.observe(getActivity(), new Observer<Date>() {
                    @Override
                    public void onChanged(Date date) {
                        binding.buttonSearchDateFrom.setText(dateFormat.format(historyViewModel.dateFrom.getValue()));
                        binding.searchEdittext.setText("");
                        binding.historySpinner.setSelection(0);
                        binding.buttonSendErp.setEnabled(false);
                        weighingViewModel.getFitterDateWeighings(date.getTime(), historyViewModel.dateTo.getValue().getTime()).observe(getActivity(), new Observer<List<Weighing>>() {
                            @Override
                            public void onChanged(List<Weighing> weighings) {
                                if (historyViewModel.spinnerData.getValue().contains("?????????")) {
                                    //weighingAdapter.setWeighings(weighingViewModel.getFitterNotSendWeighings(historyViewModel.dateFrom.getValue().getTime(), historyViewModel.dateTo.getValue().getTime()).getValue());
                                } else {
                                    weighingAdapter.setWeighings(weighings);
                                }
                                binding.historySearchCount.setText(String.valueOf(weighingAdapter.getItemCount()) + " ???");
                            }
                        });
                    }
                });

                historyViewModel.dateTo.observe(getActivity(), new Observer<Date>() {
                    @Override
                    public void onChanged(Date date) {
                        binding.buttonSearchDateTo.setText(dateFormat.format(historyViewModel.dateTo.getValue()));
                        binding.searchEdittext.setText("");
                        binding.historySpinner.setSelection(0);
                        binding.buttonSendErp.setEnabled(false);
                        weighingViewModel.getFitterDateWeighings(historyViewModel.dateFrom.getValue().getTime(), date.getTime()).observe(getActivity(), new Observer<List<Weighing>>() {
                            @Override
                            public void onChanged(List<Weighing> weighings) {
                                if (historyViewModel.spinnerData.getValue().contains("?????????")) {
                                    //weighingAdapter.setWeighings(weighingViewModel.getFitterNotSendWeighings(historyViewModel.dateFrom.getValue().getTime(), historyViewModel.dateTo.getValue().getTime()).getValue());
                                } else {
                                    weighingAdapter.setWeighings(weighings);
                                }
                                binding.historySearchCount.setText(String.valueOf(weighingAdapter.getItemCount()) + " ???");
                            }
                        });
                    }
                });
            }
        });


        binding.buttonSearchYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSearchRange(Constants.DATE_RANGE.YESTERDAY);
            }
        });

        binding.buttonSearchToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSearchRange(Constants.DATE_RANGE.TODAY);
            }
        });

        binding.buttonSearchWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSearchRange(Constants.DATE_RANGE.WEEK);
            }
        });

        binding.buttonSearchDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();

                DatePickerFragment dialog = DatePickerFragment.newInstanceFrom(historyViewModel.dateFrom.getValue());
                dialog.setTargetFragment(HistoryFragment.this, REQUEST_FROM_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        binding.buttonSearchDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();

                DatePickerFragment dialog = DatePickerFragment.newInstanceTo(historyViewModel.dateTo.getValue(), historyViewModel.dateFrom.getValue());
                dialog.setTargetFragment(HistoryFragment.this, REQUEST_TO_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        binding.historySpinner.setAdapter(spinnerAdapter);
        binding.historySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                historyViewModel.spinnerData.setValue(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

                intent.putExtra(DetailActivity.EXTRA_DATE, format.format(weighing.getDate()));
                intent.putExtra(DetailActivity.EXTRA_TOTAL_WEIGHT, weighing.getTotalWeight());

                intent.putExtra(DetailActivity.EXTRA_BOX_ID, weighing.getBoxID());
                intent.putExtra(DetailActivity.EXTRA_BOX_NAME, weighing.getBoxName());
                intent.putExtra(DetailActivity.EXTRA_BOX_WEIGHT, weighing.getBoxWeight());

                intent.putExtra(DetailActivity.EXTRA_BOX_ACCOUNT, weighing.getBoxAccount());
                intent.putExtra(DetailActivity.EXTRA_PALETTE_WEIGHT, weighing.getPaletteWeight());
                intent.putExtra(DetailActivity.EXTRA_DEDUCTIBLE_WEIGHT, weighing.getDeductibleWeight());
                intent.putExtra(DetailActivity.EXTRA_REAL_WEIGHT, weighing.getRealWeight());
                intent.putExtra(DetailActivity.EXTRA_ERP_DATE, weighing.getErpDate());

                startActivity(intent);
            }
        });

        historyViewModel.spinnerData.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.searchEdittext.setText("");
                if (s == "??????" || s == "?????????") {
                    binding.searchEdittext.setEnabled(false);
                } else {
                    binding.searchEdittext.setEnabled(true);
                }
            }
        });
        binding.buttonSearchDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String arg = "%" + binding.searchEdittext.getText().toString() + "%";
                binding.buttonSendErp.setEnabled(false);
                switch (historyViewModel.spinnerData.getValue()) {
                    case "??????":
                        weighingViewModel.getFitterDateWeighings(historyViewModel.dateFrom.getValue().getTime(), historyViewModel.dateTo.getValue().getTime()).observe(getActivity(), new Observer<List<Weighing>>() {
                            @Override
                            public void onChanged(List<Weighing> weighings) {
                                weighingAdapter.setWeighings(weighings);
                                binding.historySearchCount.setText(String.valueOf(weighingAdapter.getItemCount()) + " ???");
                            }
                        });
                        break;

                    case "??????":
                        weighingViewModel.getFitterCompanyWeighings(historyViewModel.dateFrom.getValue().getTime(), historyViewModel.dateTo.getValue().getTime(), arg).observe(getActivity(), new Observer<List<Weighing>>() {
                            @Override
                            public void onChanged(List<Weighing> weighings) {
                                weighingAdapter.setWeighings(weighings);
                                binding.historySearchCount.setText(String.valueOf(weighingAdapter.getItemCount()) + " ???");
                            }
                        });
                        break;

                    case "??????":
                        weighingViewModel.getFitterProductWeighings(historyViewModel.dateFrom.getValue().getTime(), historyViewModel.dateTo.getValue().getTime(), arg).observe(getActivity(), new Observer<List<Weighing>>() {
                            @Override
                            public void onChanged(List<Weighing> weighings) {
                                weighingAdapter.setWeighings(weighings);
                                binding.historySearchCount.setText(String.valueOf(weighingAdapter.getItemCount()) + " ???");
                            }
                        });
                        break;

                    case "?????????":
                        weighingViewModel.getFitterNotSendWeighings(historyViewModel.dateFrom.getValue().getTime(), historyViewModel.dateTo.getValue().getTime()).observe(getActivity(), new Observer<List<Weighing>>() {
                            @Override
                            public void onChanged(List<Weighing> weighings) {
                                weighingAdapter.setWeighings(weighings);
                                binding.historySearchCount.setText(String.valueOf(weighingAdapter.getItemCount() + " ???"));
                            }
                        });
                        binding.buttonSendErp.setEnabled(true);
                        break;

                    default:
                        break;
                }

            }
        });

        binding.buttonSendErp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat formatErp = new SimpleDateFormat("yyyyMMdd");
                failCnt = 0;
                ArrayList<Weighing> weighingArrayList = new ArrayList<>();

                for (int i = 0; i < weighingAdapter.getItemCount(); i++) {
                    Weighing weighing = weighingAdapter.getWeighingAt(i);

                    if (weighing.getCompanyCode() == null) {
                        failCnt++;
                        Toast.makeText(getContext(), failCnt + " ??? ??????", Toast.LENGTH_SHORT).show();
                    } else {
                        weighingArrayList.add(weighing);
                    }
                }
                if (!weighingArrayList.isEmpty()) {
                    new GroupPurchaseApi(apiViewModel.zone.getValue(), apiViewModel.sessionID.getValue(), weighingArrayList, weighingViewModel, getContext(), new ApiListener() {
                        @Override
                        public void success(String response) throws JSONException {
                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat erpFormat = new SimpleDateFormat("yyyy/MM/dd a hh:mm:ss");

                            for (int i = 0; i < weighingArrayList.size(); i++) {
                                weighingArrayList.get(i).setErpDate(erpFormat.format(date));
                                weighingViewModel.update(weighingArrayList.get(i));
                            }
                            Toast.makeText(getContext(), weighingArrayList.size() + " ??? ??????", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void fail() {
                            Toast.makeText(getContext(), "ERP ?????? ??????", Toast.LENGTH_SHORT).show();
                        }
                    }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
        historyViewModel.spinnerData.setValue("??????");
        switch (requestCode) {
            case REQUEST_FROM_DATE:
                date.setHours(0);
                date.setMinutes(0);
                date.setSeconds(0);
                binding.buttonSearchDateFrom.setText(dateFormat.format(date));
                historyViewModel.dateFrom.setValue(date);
                if (historyViewModel.dateTo.getValue().before(historyViewModel.dateFrom.getValue())) {
                    binding.buttonSearchDateTo.setText(dateFormat.format(date));
                    historyViewModel.dateTo.setValue(historyViewModel.dateFrom.getValue());
                }
                historyViewModel.updateDate.setValue(true);
                break;

            case REQUEST_TO_DATE:
                date.setHours(23);
                date.setMinutes(59);
                date.setSeconds(59);
                binding.buttonSearchDateTo.setText(dateFormat.format(date));
                historyViewModel.dateTo.setValue(date);
                historyViewModel.updateDate.setValue(true);
                break;

            default:
                break;
        }

//        if(data.hasExtra(DetailActivity.EXTRA_ERP_DATE)) {
//            if (data.getExtras().getString(DetailActivity.EXTRA_ERP_DATE).contains("?????? ??????")) {
//                weighingViewModel.updateNotSendWeighings(data.getIntExtra(InfoAddActivity.EXTRA_ID, 0), data.getStringExtra(InfoAddActivity.EXTRA_CODE), data.getStringExtra(InfoAddActivity.EXTRA_NAME), data.getExtras().getInt(DetailActivity.EXTRA_ID, 0));
//            }
//        }
    }

    private void setSearchRange(Constants.DATE_RANGE range) {

        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
        calendarFrom.set(Calendar.MINUTE, 0);
        calendarFrom.set(Calendar.SECOND, 0);

        Calendar calendarTo = Calendar.getInstance();
        calendarTo.set(Calendar.HOUR_OF_DAY, 23);
        calendarTo.set(Calendar.MINUTE, 59);
        calendarTo.set(Calendar.SECOND, 59);

        switch (range) {
            case YESTERDAY:
                calendarFrom.add(Calendar.DATE, -1);
                calendarTo.add(Calendar.DATE, -1);
                break;

            case TODAY:
                break;

            case WEEK:
                calendarFrom.add(Calendar.DATE, -7);
                break;
        }

        historyViewModel.dateTo.setValue(calendarTo.getTime());
        historyViewModel.dateFrom.setValue(calendarFrom.getTime());
        historyViewModel.updateDate.setValue(true);

        binding.buttonSearchDateFrom.setText(dateFormat.format(historyViewModel.dateFrom.getValue()));
        binding.buttonSearchDateTo.setText(dateFormat.format(historyViewModel.dateTo.getValue()));

    }
}