package com.android.chalkboard.school.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.chalkboard.R;
import com.android.chalkboard.school.model.StandardFeesModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFeesFragment extends DialogFragment implements View.OnClickListener {
    private ImageView closeIv;
    private LinearLayout feescontainer;
    private Button addStandard, submit;
    private ArrayList<EditTextStandardFees> editTextStandardFeesList;
    private ArrayList<StandardFeesModel> standardFeesModels;
    private TextInputEditText standard,fees;

    public AddFeesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_fees, container, false);
        closeIv = view.findViewById(R.id.iv_close);
        closeIv.setOnClickListener(this);
        feescontainer = view.findViewById(R.id.fees_key_value_container);
        addStandard = view.findViewById(R.id.pnb_add_standard);
        submit = view.findViewById(R.id.pnb_submit_fee);
        submit.setOnClickListener(this);
        addStandard.setOnClickListener(this);
        editTextStandardFeesList = new ArrayList<>();
        standardFeesModels = new ArrayList<>();
        addEditTextToList(view);
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id){
            case R.id.iv_close:
                getDialog().dismiss();
                break;
            case R.id.pnb_add_standard:
                addStandardView();
                break;
            case R.id.pnb_submit_fee:
                getAllStandardFees();
                break;
        }
    }

    private void getAllStandardFees() {
        for(int i = 0; i< editTextStandardFeesList.size(); i++){
            if (editTextStandardFeesList.get(i).getStandard().getText().toString().length() !=0 && editTextStandardFeesList.get(i).getFees().getText().toString().length() !=0) {
                StandardFeesModel model = new StandardFeesModel(editTextStandardFeesList.get(i).getStandard().getText().toString()
                        , Integer.parseInt(editTextStandardFeesList.get(i).getFees().getText().toString()));
                standardFeesModels.add(model);
            }
        }
        ((AddSchoolActivity)getActivity()).setStandardFees(standardFeesModels);
        getDialog().dismiss();

    }

    private void addStandardView() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.add_fee_item,null);
        feescontainer.addView(view,feescontainer.getChildCount());
        addEditTextToList(view);

    }
// to add the edittext address to the list for all the dynamically generated views
    private void addEditTextToList(View view) {
        standard = view.findViewById(R.id.et_standard);
        fees = view.findViewById(R.id.et_fees_value);
        editTextStandardFeesList.add(new EditTextStandardFees(standard,fees));
    }

    public class EditTextStandardFees{
        private TextInputEditText standard;
        private TextInputEditText fees;

        public TextInputEditText getStandard() {
            return standard;
        }

        public void setStandard(TextInputEditText standard) {
            this.standard = standard;
        }

        public TextInputEditText getFees() {
            return fees;
        }

        public void setFees(TextInputEditText fees) {
            this.fees = fees;
        }

        public EditTextStandardFees(TextInputEditText standard, TextInputEditText fees) {
            this.standard = standard;
            this.fees = fees;

        }
    }
}
