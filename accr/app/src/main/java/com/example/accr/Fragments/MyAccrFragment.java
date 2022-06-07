package com.example.accr.Fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accr.ApiConnection.HttpGETAsyncRetriever;
import com.example.accr.AuxClasses.ApiConstants;
import com.example.accr.Dtos.AccrToReturn;
import com.example.accr.Dtos.AccreditationPatternToReturn;
import com.example.accr.Dtos.PatternResponse;
import com.example.accr.Dtos.UserLoginResponse;
import com.example.accr.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyAccrFragment extends Fragment {

    private UserLoginResponse userLoginResponse;
    private ListView myAccrListView;
    private List<AccrToReturn> myAccrList = new ArrayList<>();
    private List<String> myAccrListNames = new ArrayList<>();
    private String respones = "";



    public MyAccrFragment() {
        // Required empty public constructor
    }

    public MyAccrFragment(UserLoginResponse userLoginResponse) {
        this.userLoginResponse = userLoginResponse;
    }

    public static MyAccrFragment newInstance(String param1, String param2) {
        MyAccrFragment fragment = new MyAccrFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_accr, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        StringBuilder stringBuilder = new StringBuilder().append(ApiConstants.baseApi + "/api/Accreditation/for-user/").append(userLoginResponse.id);
        try {
            respones = new HttpGETAsyncRetriever().execute(stringBuilder.toString()).get();
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<AccrToReturn>>() {
            }.getType();
            myAccrList = gson.fromJson(respones, type);

        } catch (Exception ex) {
            String asd = ex.toString();
        }

        for (int i = 0; i < myAccrList.size(); i++) {
            if (myAccrList.size() != myAccrListNames.size()) {
                myAccrListNames.add(myAccrList.get(i).name);
            }
        }

        myAccrListView = requireView().findViewById(R.id.my_accr_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, myAccrListNames);
        myAccrListView.setAdapter(adapter);
        myAccrListView.setOnItemClickListener((parent, view1, position, id) -> openAccrInfo(position));

    }

    public void openAccrInfo(int position) {
        final Dialog accrInfo = new Dialog(getContext());
        accrInfo.setTitle("accrs");
        accrInfo.setContentView(R.layout.accr_info);

        TextView myAccrName = accrInfo.findViewById(R.id.my_accr_name);
        TextView myAccrDesc = accrInfo.findViewById(R.id.my_accr_desc);
        TextView myAccrExpire = accrInfo.findViewById(R.id.my_accr_expire);

        myAccrName.setText(myAccrList.get(position).name);
        myAccrDesc.setText(myAccrList.get(position).description);
        myAccrExpire.setText("DateTime");

        accrInfo.show();
    }

}