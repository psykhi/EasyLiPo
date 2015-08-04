package com.theboredengineers.easylipo.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.theboredengineers.easylipo.R;
import com.theboredengineers.easylipo.objects.Battery;
import com.theboredengineers.easylipo.ui.views.RichNumberPicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Alex on 14/01/2015.
 */
public class FragmentNewBattery extends BaseFragment {

    private int step = 0;
    public static final int BATTERY_NAME_BRAND_MODEL = 0;
    public static final int BATTERY_DETAILS = 1;
    public static final int BATTERY_DATE = 2;

    private EditText brand, model, name, capacity, cycles;
    private Switch charged;
    private RichNumberPicker charge, discharge, cells;

    private Button nextButton;
    private Date date;
    private OnNewBatteryFragmentNextButtonClicked listener;
    private Button doneButton;
    private BatterySupplier batterySupplier;
    private EditText dateText;


    public FragmentNewBattery() {

    }

    public static FragmentNewBattery newInstance(int position, Battery battery) {
        FragmentNewBattery fragment = new FragmentNewBattery();

        Bundle args = new Bundle();
        args.putInt("step", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = null;
        switch (step) {
            case BATTERY_NAME_BRAND_MODEL:
                v = inflater.inflate(R.layout.fragment_new_battery_step_one_name_brand_model, container, false);
                brand = (EditText) v.findViewById(R.id.create_brand);
                model = (EditText) v.findViewById(R.id.create_model);
                name = (EditText) v.findViewById(R.id.create_name);
                model.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_DONE) {
                            setBatteryAttributes();
                            listener.onNext(step);
                        }
                        return false;
                    }
                });
                break;
            case BATTERY_DETAILS:
                v = inflater.inflate(R.layout.fragment_new_battery_step_two_electrical_characteristics, container, false);
                break;
            case BATTERY_DATE:
                v = inflater.inflate(R.layout.fragment_new_battery_step_three_date_charged_cycles, container, false);
                charged = (Switch) v.findViewById(R.id.create_charged);
                cycles = (EditText) v.findViewById(R.id.create_cycles);

                v.findViewById(R.id.create_date_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDatePickerDialog();
                    }
                });
                dateText = (EditText) v.findViewById(R.id.create_date);
                dateText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b)
                            showDatePickerDialog();
                    }
                });
                dateText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDatePickerDialog();
                    }
                });
                doneButton = (Button) v.findViewById(R.id.button_skip);
                break;
            default:
                v = inflater.inflate(R.layout.fragment_new_battery_step_one_name_brand_model, container, false);
                break;
        }

        if (doneButton != null)
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setBatteryAttributes();
                    listener.onDone();
                }
            });
        nextButton = (Button) v.findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBatteryAttributes();
                listener.onNext(step);
            }
        });

        return v;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (step == BATTERY_DATE) {
            cycles.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

    }

    private void showDatePickerDialog() {
        final Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                cal.set(i, i1, i2);
                date = cal.getTime();
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.US);
                dateText.setText(format.format(date));
                // Hide the keyboard if it was there
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void setBatteryAttributes() {

        Battery battery = batterySupplier.getBattery();


        switch (step) {
            case BATTERY_NAME_BRAND_MODEL:


                battery.setBrand(brand.getText().toString());
                battery.setName(name.getText().toString());
                battery.setModel(model.getText().toString());
                break;
            case BATTERY_DETAILS:
                capacity = (EditText) getView().findViewById(R.id.create_capacity);
                charge = (RichNumberPicker) getView().findViewById(R.id.create_charge);
                discharge = (RichNumberPicker) getView().findViewById(R.id.create_discharge);
                cells = (RichNumberPicker) getView().findViewById(R.id.create_cells);

                try {
                    battery.setCapacity(Integer.parseInt(capacity.getText().toString()));
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Missing capacity in mAh", Toast.LENGTH_SHORT).show();
                }
                battery.setChargeRate(charge.getTrueValue());
                battery.setDischargeRate(discharge.getTrueValue());
                battery.setNbS(cells.getTrueValue());
                break;
            case BATTERY_DATE:


                battery.setPurchaseDate(date);
                battery.setCharged(charged.isChecked());
                try {
                    battery.setNbOfCycles(Integer.parseInt(cycles.getText().toString()));
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Missing number of cycles", Toast.LENGTH_SHORT).show();
                }

            default:
                break;

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            step = getArguments().getInt("step");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnNewBatteryFragmentNextButtonClicked) activity;
            batterySupplier = (BatterySupplier) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNewBatteryFragmentNextButtonClicked");
        }
    }

    public interface OnNewBatteryFragmentNextButtonClicked {
        void onNext(int step);

        void onDone();
    }

    public interface BatterySupplier {
        Battery getBattery();

    }


}
