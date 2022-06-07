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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accr.ApiConnection.HttpGETAsyncRetriever;
import com.example.accr.AuxClasses.ApiConstants;
import com.example.accr.ApiConnection.PutAsyncDecisionRetriever;
import com.example.accr.Dtos.AppToReturn;
import com.example.accr.Dtos.AttachmentToReturn;
import com.example.accr.Dtos.UserLoginResponse;
import com.example.accr.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AccrsAdminFragment extends Fragment {

    private UserLoginResponse userLoginResponse;
    private ListView accrsLv;
    private StringBuilder stringBuilder;
    private String respones;
    private List<AppToReturn> appsToMenage = new ArrayList<>();
    private List<String> appsToMenageNames = new ArrayList<>();
    private List<AttachmentToReturn> attachments = new ArrayList<>();
    private List<String> attachmentsNames = new ArrayList<>();

    public AccrsAdminFragment() {
        // Required empty public constructor
    }

    public AccrsAdminFragment(UserLoginResponse userLoginResponse) {
        this.userLoginResponse = userLoginResponse;
    }


    public static AccrsAdminFragment newInstance(String param1, String param2) {
        AccrsAdminFragment fragment = new AccrsAdminFragment();
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
        return inflater.inflate(R.layout.fragment_accrs_admin, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        accrsLv = requireView().findViewById(R.id.apps_to_menage);

        stringBuilder = new StringBuilder().append(ApiConstants.baseApi + "/api/Application/non-approved");
        try {
            respones = new HttpGETAsyncRetriever().execute(stringBuilder.toString()).get();
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<AppToReturn>>() {
            }.getType();
            appsToMenage = gson.fromJson(respones, type);

        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < appsToMenage.size(); i++) {
            if (appsToMenage.size() != appsToMenageNames.size()) {
                appsToMenageNames.add(appsToMenage.get(i).name);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, appsToMenageNames);
        accrsLv.setAdapter(adapter);
        accrsLv.setOnItemClickListener((parent, view1, position, id) -> openPatternInfo(position));
    }

    private void openPatternInfo(int position) {
        final Dialog menageApp = new Dialog(getContext());
        menageApp.setTitle("accrs");
        menageApp.setContentView(R.layout.menage_acc);

        TextView appName = menageApp.findViewById(R.id.accr_name_menage);
        TextView appDesc = menageApp.findViewById(R.id.accr_descr_menage);
        TextView appStatus = menageApp.findViewById(R.id.accr_status_menage);
        TextView appComment = menageApp.findViewById(R.id.accr_comment_menage);
        ListView applicationsListView = menageApp.findViewById(R.id.attachment_list_menage_admin);
        EditText newComment = menageApp.findViewById(R.id.admins_comment);
        Button declineApp = menageApp.findViewById(R.id.decline_app);
        Button acceptApp = menageApp.findViewById(R.id.accept_app);


        appName.setText(appsToMenage.get(position).name);
        appDesc.setText(appsToMenage.get(position).description);
        appStatus.setText("" + appsToMenage.get(position).approved);
        appComment.setText(appsToMenage.get(position).adminComment);

        StringBuilder stringBuilder = new StringBuilder().append(ApiConstants.baseApi + "/api/Application/").append(appsToMenage.get(position).id);
        try {
            respones = new HttpGETAsyncRetriever().execute(stringBuilder.toString()).get();
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<AttachmentToReturn>>() {
            }.getType();
            attachments = gson.fromJson(respones, type);

        } catch (Exception ex) {
            String asd = ex.toString();
        }

        for (int i = 0; i < attachments.size(); i++) {
            if (attachments.size()!= attachmentsNames.size()) {
                attachmentsNames.add(appsToMenage.get(i).name);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, attachmentsNames);
        applicationsListView.setAdapter(adapter);

        declineApp.setOnClickListener(v -> {
          //todo
        });

        acceptApp.setOnClickListener(v -> {
            try {
                String result = new PutAsyncDecisionRetriever().execute(appsToMenage.get(position).id).get();
                appsToMenage.clear();
                appsToMenageNames.clear();

                StringBuilder stringBuilde1 = new StringBuilder().append(ApiConstants.baseApi + "/api/Application/non-approved");
                try {
                    respones = new HttpGETAsyncRetriever().execute(stringBuilde1.toString()).get();
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<AppToReturn>>() {
                    }.getType();
                    appsToMenage = gson.fromJson(respones, type);

                } catch (Exception ex) {
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

                for (int i = 0; i < appsToMenage.size(); i++) {
                    if (appsToMenage.size() != appsToMenageNames.size()) {
                        appsToMenageNames.add(appsToMenage.get(i).name);
                    }
                }

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, appsToMenageNames);
                accrsLv.setAdapter(adapter1);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            menageApp.dismiss();
        });

        menageApp.show();
    }


}