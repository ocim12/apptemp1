package com.example.accr.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
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

import com.example.accr.Activities.AddAtachmentActivity;
import com.example.accr.ApiConnection.DeleteAsyncRetriever;
import com.example.accr.ApiConnection.HttpGETAsyncRetriever;
import com.example.accr.AuxClasses.ApiConstants;
import com.example.accr.Dtos.AppToReturn;
import com.example.accr.Dtos.UserAttachmentToReturn;
import com.example.accr.Dtos.UserLoginResponse;
import com.example.accr.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyAppsFragment extends Fragment {

    private UserLoginResponse userLoginResponse;
    private ListView myAppsListView;
    private List<String> myAppsListNames = new ArrayList<>();
    private ArrayList<AppToReturn> appsList = new ArrayList<>();
    private String respones = "";
    private StringBuilder stringBuilder;

    public MyAppsFragment() {
    }

    public MyAppsFragment(UserLoginResponse userLoginResponse) {
        this.userLoginResponse = userLoginResponse;
    }

    public static MyAppsFragment newInstance(String param1, String param2) {
        MyAppsFragment fragment = new MyAppsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_apps, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        stringBuilder = new StringBuilder().append(ApiConstants.baseApi + "/api/Application/for-user/").append(userLoginResponse.id);
        try {
            respones = new HttpGETAsyncRetriever().execute(stringBuilder.toString()).get();
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<AppToReturn>>() {
            }.getType();
            appsList = gson.fromJson(respones, type);

        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < appsList.size(); i++) {
            if (appsList.size() != myAppsListNames.size()) {
                myAppsListNames.add(appsList.get(i).name);
            }
        }

        myAppsListView = requireView().findViewById(R.id.my_apps_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, myAppsListNames);
        myAppsListView.setAdapter(adapter);
        myAppsListView.setOnItemClickListener((parent, view1, position, id) -> openAppInfo(position));
    }

    public void openAppInfo(int position) {
        final Dialog appInfo = new Dialog(getContext());

        stringBuilder = new StringBuilder().append(ApiConstants.baseApi + "/api/Application/for-user/").append(userLoginResponse.id);
        try {
            respones = new HttpGETAsyncRetriever().execute(stringBuilder.toString()).get();
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<AppToReturn>>() {
            }.getType();
            appsList = gson.fromJson(respones, type);

        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < appsList.size(); i++) {
            if (appsList.size() != myAppsListNames.size()) {
                myAppsListNames.add(appsList.get(i).name);
            }
        }

        appInfo.setTitle("apps");
        appInfo.setContentView(R.layout.app_info);
        List<UserAttachmentToReturn> attachments = appsList.get(position).userAttachments;

        ListView attachmentListView = appInfo.findViewById(R.id.attachment_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getAttachmentsNames(appsList.get(position).userAttachments));
        attachmentListView.setAdapter(adapter);

        attachmentListView.setOnItemClickListener((parent, view, position1, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Czy pobrać załącznik \"" + attachments.get(position1).name + "\"?")
                    .setTitle("Pobranie załącznika");
            builder.setPositiveButton("Tak", (dialog, id1) -> Toast.makeText(getContext(), "pls tak pobierz mnie", Toast.LENGTH_SHORT).show()).setNegativeButton("Nie", (dialog, id12) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        attachmentListView.setOnItemLongClickListener((arg0, arg1, pos, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Czy usunąć załącznik \"" + attachments.get(pos).name + "\"?")
                    .setTitle("Usunięcie załącznika");
            builder.setPositiveButton("Tak", (dialog, id1) -> {
                try {
                    String result = new DeleteAsyncRetriever().execute("/api/UserAttachment/" + attachments.get(position).id).get();
                    attachments.remove(attachments.get(position));
                    appInfo.dismiss();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).setNegativeButton("Nie", (dialog, id12) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        });

        TextView accrName = appInfo.findViewById(R.id.accr_name);
        TextView accrDesc = appInfo.findViewById(R.id.accr_descr);
        TextView accrStatus = appInfo.findViewById(R.id.accr_status);
        TextView accrComment = appInfo.findViewById(R.id.accr_comment);
        Button addAttachment = appInfo.findViewById(R.id.add_attachment_but);
        accrName.setText(appsList.get(position).name);
        accrDesc.setText(appsList.get(position).description);
        accrStatus.setText("" + appsList.get(position).approved);
        accrComment.setText(appsList.get(position).adminComment);
        addAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddAtachmentActivity.class);
                intent.putExtra("json-apps", respones);
                intent.putExtra("index", position);
                startActivity(intent);
                appInfo.dismiss();
            }
        });

        appInfo.show();
    }

    private ArrayList<String> getAttachmentsNames(List<UserAttachmentToReturn> attachments) {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < attachments.size(); i++) {
            names.add(attachments.get(i).name);
        }
        return names;
    }

}