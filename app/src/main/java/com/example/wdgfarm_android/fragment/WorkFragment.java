package com.example.wdgfarm_android.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.HandlerThread;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.activity.InfoAddActivity;
import com.example.wdgfarm_android.activity.MainActivity;
import com.example.wdgfarm_android.activity.SelectActivity;
import com.example.wdgfarm_android.api.ApiListener;
import com.example.wdgfarm_android.api.PurchaseApi;
import com.example.wdgfarm_android.databinding.FragmentWorkBinding;
import com.example.wdgfarm_android.model.Weighing;
import com.example.wdgfarm_android.utils.Cas22BytesProtocol;
import com.example.wdgfarm_android.utils.CurrentTime;
import com.example.wdgfarm_android.utils.DatetimePickerFragment;
import com.example.wdgfarm_android.utils.PreferencesKey;
import com.example.wdgfarm_android.utils.SharedPreferencesManager;
import com.example.wdgfarm_android.utils.TcpThread;
import com.example.wdgfarm_android.viewmodel.ApiViewModel;
import com.example.wdgfarm_android.viewmodel.ScaleViewModel;
import com.example.wdgfarm_android.viewmodel.WeighingViewModel;
import com.example.wdgfarm_android.viewmodel.WeighingWorkViewModel;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WorkFragment extends Fragment {
    public static final int INFO_COMPANY = 100;
    public static final int INFO_PRODUCT = 200;
    public static final int INFO_BOX = 300;

    private static final int REQUEST_DATE = 1;
    private static final String INIT_COMPANY = "????????? ???????????????.";
    private static final String INIT_PRODUCT = "????????? ???????????????.";
    private static final String INIT_DATE = "?????????";

    private WeighingViewModel weighingViewModel;
    private SimpleDateFormat format;
    public static Weighing weighing;
    public static WeighingWorkViewModel weighingWorkViewModel;
    public static ApiViewModel apiViewModel;
    public static ScaleViewModel scaleViewModel;
    public static FragmentWorkBinding binding;
    private TcpThread tcpThread;
    private CurrentTime currentTime;

    public WorkFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        weighing = new Weighing();
        weighing.setCompanyName(INIT_COMPANY);
        weighing.setProductName(INIT_PRODUCT);
        weighing.setDate(Calendar.getInstance().getTime());


        format = new SimpleDateFormat("yyyy/MM/dd a hh:mm");

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_work, container, false);
        View view = binding.getRoot();
        apiViewModel = new ViewModelProvider(getActivity()).get(ApiViewModel.class);
        weighingViewModel = new ViewModelProvider(getActivity()).get(WeighingViewModel.class);
        weighingWorkViewModel = new ViewModelProvider(getActivity()).get(WeighingWorkViewModel.class);
        scaleViewModel = new ViewModelProvider(getActivity()).get(ScaleViewModel.class);

        if (SharedPreferencesManager.getString(getContext(), "CONNECTED_SCALE").contains("B")) {
            binding.radioB.setChecked(true);
        } else {
            binding.radioA.setChecked(true);
        }
        binding.radioA.setText(SharedPreferencesManager.getString(getContext(), PreferencesKey.A_SCALE_NAME.name()));
        binding.radioB.setText(SharedPreferencesManager.getString(getContext(), PreferencesKey.B_SCALE_NAME.name()));

        currentTime = new CurrentTime();
        currentTime.CurrentTime(weighingWorkViewModel);
        currentTime.start();

        tcpThread = new TcpThread();

        if (SharedPreferencesManager.getString(getContext(), PreferencesKey.CONNECTED_SCALE.name()).contains("A")) {
            tcpThread.TcpThread(SharedPreferencesManager.getString(getContext(), PreferencesKey.A_SCALE_IP.name()), Integer.parseInt(SharedPreferencesManager.getString(getContext(), PreferencesKey.A_SCALE_PORT.name())), scaleViewModel);
        } else {
            tcpThread.TcpThread(SharedPreferencesManager.getString(getContext(), PreferencesKey.B_SCALE_IP.name()), Integer.parseInt(SharedPreferencesManager.getString(getContext(), PreferencesKey.B_SCALE_PORT.name())), scaleViewModel);
        }

        tcpThread.start();

        //TCP ?????? ???????????? 5??? ?????? ?????? ??????
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if(!scaleViewModel.isConnected.getValue()){
                        if (tcpThread != null) {
                            tcpThread.interrupt();
                            tcpThread = null;
                        }

                        tcpThread = new TcpThread();

                        if (SharedPreferencesManager.getString(getContext(), PreferencesKey.CONNECTED_SCALE.name()).contains("A")) {
                            tcpThread.TcpThread(SharedPreferencesManager.getString(getContext(), PreferencesKey.A_SCALE_IP.name()), Integer.parseInt(SharedPreferencesManager.getString(getContext(), PreferencesKey.A_SCALE_PORT.name())), scaleViewModel);
                        } else {
                            tcpThread.TcpThread(SharedPreferencesManager.getString(getContext(), PreferencesKey.B_SCALE_IP.name()), Integer.parseInt(SharedPreferencesManager.getString(getContext(), PreferencesKey.B_SCALE_PORT.name())), scaleViewModel);
                        }

                        tcpThread.start();
                    }

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        weighingWorkViewModel.date.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String currentTime) {
                if (binding.workDatetimeCheckbox.isChecked()) {
                    binding.workDateBtn.setText(currentTime);
                }
            }
        });

        scaleViewModel.scaleType.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String scaleType) {
                if (tcpThread != null) {
                    tcpThread.interrupt();
                    tcpThread = null;
                }
            }
        });

        scaleViewModel.scaleAName.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.radioA.setText(s);
            }
        });

        scaleViewModel.scaleBName.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.radioB.setText(s);
            }
        });

        scaleViewModel.weight.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String totalWeight) {
                if (Float.parseFloat(totalWeight) < 1.0) {
                    weighingWorkViewModel.prvRepeat.setValue(true);
                }
                binding.totalWeightValue.setText(totalWeight);
                weighing.setTotalWeight(Float.parseFloat(totalWeight));
                weighing.setRealWeight(realWeight(weighing.getTotalWeight(), weighing.getBoxWeight(), weighing.getBoxAccount(), weighing.getPaletteWeight(), weighing.getDeductibleWeight()));
                weighingWorkViewModel.weighing.setValue(weighing);
            }
        });

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                switch (checkId) {
                    case R.id.radio_a:
                        SharedPreferencesManager.setString(getContext(), PreferencesKey.CONNECTED_SCALE.name(), "A");
                        scaleViewModel.scaleType.setValue("A");

                        break;

                    case R.id.radio_b:
                        SharedPreferencesManager.setString(getContext(), PreferencesKey.CONNECTED_SCALE.name(), "B");
                        scaleViewModel.scaleType.setValue("B");
                        break;

                    default:
                        break;
                }
            }
        });

        binding.workCompanyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), SelectActivity.class);
                intent.putExtra(SelectActivity.EXTRA_INFO, INFO_COMPANY);
                startActivityForResult(intent, INFO_COMPANY);
            }
        });

        binding.workProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), SelectActivity.class);
                intent.putExtra(SelectActivity.EXTRA_INFO, INFO_PRODUCT);
                startActivityForResult(intent, INFO_PRODUCT);
            }
        });

        binding.workPriceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().matches("")) {
                    weighing.setProductPrice(0);
                } else {
                    weighing.setProductPrice(Integer.parseInt(editable.toString()));
                }
                weighingWorkViewModel.weighing.setValue(weighing);
            }
        });

        binding.scaleConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.scaleConnectBtn.isChecked()) {
                    if (tcpThread != null) {
                        tcpThread.interrupt();
                        tcpThread = null;
                        scaleViewModel.isConnected.setValue(false);
                    }
                } else {
                    tcpThread = new TcpThread();
                    if (SharedPreferencesManager.getString(getContext(), PreferencesKey.CONNECTED_SCALE.name()).contains("A")) {
                        tcpThread.TcpThread(SharedPreferencesManager.getString(getContext(), PreferencesKey.A_SCALE_IP.name()), Integer.parseInt(SharedPreferencesManager.getString(getContext(), PreferencesKey.A_SCALE_PORT.name())), scaleViewModel);
                    } else {
                        tcpThread.TcpThread(SharedPreferencesManager.getString(getContext(), PreferencesKey.B_SCALE_IP.name()), Integer.parseInt(SharedPreferencesManager.getString(getContext(), PreferencesKey.B_SCALE_PORT.name())), scaleViewModel);
                    }

                    tcpThread.start();
                }
            }
        });

        weighingWorkViewModel.weighing.setValue(weighing);
        binding.boxAccountValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().matches("")) {
                    weighing.setBoxAccount(0);
                } else {
                    weighing.setBoxAccount(Integer.parseInt(editable.toString()));
                }
                weighing.setRealWeight(realWeight(weighing.getTotalWeight(), weighing.getBoxWeight(), weighing.getBoxAccount(), weighing.getPaletteWeight(), weighing.getDeductibleWeight()));
                weighingWorkViewModel.weighing.setValue(weighing);
            }
        });

        scaleViewModel.isConnected.observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                if (isConnected) {
                    binding.connectionState.setImageResource(R.drawable.ic_baseline_circle_24_green);
                    binding.scaleConnectBtn.setChecked(true);
                } else {
                    binding.connectionState.setImageResource(R.drawable.ic_baseline_circle_24_red);
                    binding.scaleConnectBtn.setChecked(false);
                }
            }
        });
        binding.paletteWeightValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().matches("")) {
                    weighing.setPaletteWeight(0);
                } else {
                    weighing.setPaletteWeight(Integer.parseInt(editable.toString()));
                }
                weighing.setRealWeight(realWeight(weighing.getTotalWeight(), weighing.getBoxWeight(), weighing.getBoxAccount(), weighing.getPaletteWeight(), weighing.getDeductibleWeight()));
                weighingWorkViewModel.weighing.setValue(weighing);
            }
        });

        binding.deductibleWeightValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().matches("")) {
                    weighing.setDeductibleWeight(0);
                } else {
                    weighing.setDeductibleWeight(Integer.parseInt(editable.toString()));
                }
                weighing.setRealWeight(realWeight(weighing.getTotalWeight(), weighing.getBoxWeight(), weighing.getBoxAccount(), weighing.getPaletteWeight(), weighing.getDeductibleWeight()));
                weighingWorkViewModel.weighing.setValue(weighing);
            }
        });

        weighingWorkViewModel.weighing.observe(getActivity(), new Observer<Weighing>() {
            @Override
            public void onChanged(Weighing weighing) {
                binding.workCompanyBtn.setText(weighing.getCompanyName());
                binding.workProductBtn.setText(weighing.getProductName());
                binding.workDateBtn.setText(format.format(weighing.getDate()));
                binding.boxSize.setText(weighing.getBoxName());
                binding.boxWeightValue.setText(String.valueOf(weighing.getBoxWeight()));
                binding.realWeightValue.setText(String.valueOf(weighing.getRealWeight()));
            }
        });

        binding.workDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatetimePickerFragment dialog = DatetimePickerFragment.newInstance();
                dialog.setTargetFragment(WorkFragment.this, REQUEST_DATE);

                dialog.show(manager, "DataTimePicker Dialog");
            }
        });

        binding.boxWeightValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), SelectActivity.class);
                intent.putExtra(SelectActivity.EXTRA_INFO, INFO_BOX);
                startActivityForResult(intent, INFO_BOX);
            }
        });


        CheckBox checkBox = (CheckBox) view.findViewById(R.id.work_datetime_checkbox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkBox.isChecked()) {
                    binding.workDateBtn.setEnabled(false);
                } else {
                    binding.workDateBtn.setEnabled(true);
                }
            }
        });

        scaleViewModel.scaleState.observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean state) {
                if (state) {
                    binding.totalWeightValue.setBackground(getResources().getDrawable(R.drawable.work_weight_stable));

                } else {
                    binding.totalWeightValue.setBackground(getResources().getDrawable(R.drawable.work_weight_unstable));


                }
            }
        });

        binding.workSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date(System.currentTimeMillis());

                if (checkBox.isChecked()) {
                    weighing.setDate(date);
                    weighingWorkViewModel.weighing.setValue(weighing);
                    binding.workDateBtn.setTextColor(getResources().getColor(R.color.colorBlack));
                } else {
                    try {
                        weighing.setDate(format.parse(binding.workDateBtn.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (weighingWorkViewModel.prvRepeat.getValue()) {
                    if (!binding.workProductBtn.getText().toString().contains(INIT_PRODUCT) && !binding.workSaveBtn.getText().toString().contains(INIT_COMPANY) && !binding.workDateBtn.getText().toString().contains(INIT_DATE)) {

                        weighing.setTotalWeight(Float.parseFloat(binding.totalWeightValue.getText().toString()));
                        weighing.setBoxWeight(Float.parseFloat(binding.boxWeightValue.getText().toString()));

                        //?????? ????????? 0?????? ??????
                        if (binding.boxAccountValue.getText().toString().matches(""))
                            weighing.setBoxAccount(0);
                        else
                            weighing.setBoxAccount(Integer.parseInt(binding.boxAccountValue.getText().toString()));
                        if (binding.paletteWeightValue.getText().toString().matches(""))
                            weighing.setPaletteWeight(0);
                        else
                            weighing.setPaletteWeight(Float.parseFloat(binding.paletteWeightValue.getText().toString()));
                        if (binding.deductibleWeightValue.getText().toString().matches(""))
                            weighing.setDeductibleWeight(0);
                        else
                            weighing.setDeductibleWeight(Integer.parseInt(binding.deductibleWeightValue.getText().toString()));

                        weighing.setRealWeight(Float.parseFloat(binding.realWeightValue.getText().toString()));

                        weighingWorkViewModel.weighing.setValue(weighing);

                        AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                        dlg.setTitle("????????? ??????");
                        dlg.setMessage("- ???????????? : " + weighing.getCompanyName()
                                + "\n\n- ????????? : " + weighing.getProductName()
                                + "\n\n- ?????? : " + weighing.getProductPrice()
                                + "\n\n- ?????? ?????? : " + weighing.getTotalWeight()
                                + "\n\n- ????????? : " + weighing.getBoxName()
                                + "\n\n- ?????? ?????? : " + weighing.getBoxWeight()
                                + "\n\n- ?????? ?????? : " + weighing.getBoxAccount()
                                + "\n\n- ????????? ?????? : " + weighing.getPaletteWeight()
                                + "\n\n- ?????? ?????? : " + weighing.getDeductibleWeight()
                                + "\n\n- ??? ?????? : " + weighing.getRealWeight());
                        dlg.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (weighing.getCompanyCode() == null) {
                                    weighing.setErpDate("?????? ??????");
                                    weighingViewModel.insert(weighing);
                                    Toast.makeText(getContext(), "?????????????????????.", Toast.LENGTH_SHORT).show();
                                } else {
                                    purchaseApi();
                                }
                                weighingWorkViewModel.prvRepeat.setValue(false);
                            }
                        });
                        dlg.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        dlg.show();

                    } else {
                        Toast.makeText(getContext(), "????????? ????????????", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "????????? ?????? ??? ??????????????????.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            int info = data.getIntExtra(SelectActivity.EXTRA_INFO, 0);
            switch (info) {
                //??????
                case 100:
                    if (data.hasExtra(InfoAddActivity.EXTRA_ID)) {
                        weighing.setCompanyID(data.getIntExtra(InfoAddActivity.EXTRA_ID, 0));
                        weighing.setCompanyCode(data.getStringExtra(InfoAddActivity.EXTRA_CODE));
                        weighing.setCompanyName(data.getStringExtra(InfoAddActivity.EXTRA_NAME));
                    } else {
                        weighing.setCompanyID(0);
                        weighing.setCompanyCode(null);
                        weighing.setCompanyName(data.getStringExtra(InfoAddActivity.EXTRA_NAME));
                    }
                    binding.workCompanyBtn.setTextColor(getResources().getColor(R.color.colorBlack));
                    break;

                //??????
                case 200:
                    weighing.setProductID(data.getIntExtra(InfoAddActivity.EXTRA_ID, 0));
                    weighing.setProductCode(data.getStringExtra(InfoAddActivity.EXTRA_CODE));
                    weighing.setProductName(data.getStringExtra(InfoAddActivity.EXTRA_NAME));
                    if (data.hasExtra(InfoAddActivity.EXTRA_VALUE)) {
                        if (data.getStringExtra(InfoAddActivity.EXTRA_VALUE).matches("")) {
                            weighing.setProductPrice(0);
                        } else {
                            weighing.setProductPrice(Integer.parseInt(data.getStringExtra(InfoAddActivity.EXTRA_VALUE)));
                        }
                    }
                    binding.workPriceEdit.setText(String.valueOf(weighing.getProductPrice()));
                    binding.workProductBtn.setTextColor(getResources().getColor(R.color.colorBlack));
                    break;

                case 300:
                    weighing.setBoxID(data.getIntExtra(InfoAddActivity.EXTRA_ID, 0));
                    weighing.setBoxName(data.getStringExtra(InfoAddActivity.EXTRA_NAME));
                    weighing.setBoxWeight(data.getFloatExtra(InfoAddActivity.EXTRA_VALUE, 0));

                default:
                    break;
            }
            if (requestCode == REQUEST_DATE) {
                Date date = (Date) data.getSerializableExtra(DatetimePickerFragment.EXTRA_DATE);
                weighing.setDate(date);
                binding.workDateBtn.setTextColor(getResources().getColor(R.color.colorBlack));
            }
            weighingWorkViewModel.weighing.setValue(weighing);

        }
    }

    private float realWeight(float total, float boxWeight, int boxAccount, float palleteWeight, float deductibleWeight) {
        float result;
        float deductible;
        result = total - (boxWeight * boxAccount) - palleteWeight;
        deductible = (result / 10) * (deductibleWeight / 1000);

        result = result - deductible;
        result = Float.parseFloat(String.format("%.1f", result));
        return result;
    }

    private void purchaseApi() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

        new PurchaseApi(apiViewModel.zone.getValue(), apiViewModel.sessionID.getValue(), format.format(weighing.getDate()), weighing.getCompanyName(), weighing.getProductName(), weighing.getProductPrice(), weighing.getRealWeight(), getContext(), new ApiListener() {
            @Override
            public void success(String response) throws JSONException {
                Log.d("TAG", "?????? ?????? ??????");
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat erpFormat = new SimpleDateFormat("yyyy/MM/dd a hh:mm:ss");
                weighing.setErpDate(erpFormat.format(date));
                weighingViewModel.insert(weighing);
                Toast.makeText(getContext(), "?????????????????????.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void fail() {
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                dlg.setTitle("?????? ?????? ??????");
                dlg.setMessage("?????? ?????? ??????????????????.");
                dlg.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        weighing.setErpDate("?????? ??????");
                        weighingViewModel.insert(weighing);
                    }
                });
                dlg.setNegativeButton(R.string.retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        purchaseApi();
                    }
                });

                dlg.show();
                Log.e("TAG", "?????? ?????? ??????");

            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
