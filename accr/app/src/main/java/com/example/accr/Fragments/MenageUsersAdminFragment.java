package com.example.accr.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.accr.ApiConnection.DeleteAsyncRetriever;
import com.example.accr.ApiConnection.HttpGETAsyncRetriever;
import com.example.accr.AuxClasses.ApiConstants;
import com.example.accr.Dtos.AppToReturn;
import com.example.accr.Dtos.UserLoginResponse;
import com.example.accr.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MenageUsersAdminFragment extends Fragment {

    private UserLoginResponse userLoginResponse;
    private StringBuilder stringBuilder;
    private StringBuilder userInfo;
    private String respones = "";
    private List<UserLoginResponse> usersList = new ArrayList<>();
    private List<String> usersListNames = new ArrayList<>();
    private ListView usersListView;



    public MenageUsersAdminFragment() {
        // Required empty public constructor
    }

    public MenageUsersAdminFragment(UserLoginResponse userLoginResponse) {
        this.userLoginResponse = userLoginResponse;
    }

    public static MenageUsersAdminFragment newInstance(String param1, String param2) {
        MenageUsersAdminFragment fragment = new MenageUsersAdminFragment();
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
        return inflater.inflate(R.layout.fragment_menage_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        stringBuilder = new StringBuilder().append(ApiConstants.baseApi + "/api/User/users");
        try {
            respones = new HttpGETAsyncRetriever().execute(stringBuilder.toString()).get();
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<UserLoginResponse>>() {
            }.getType();
            usersList = gson.fromJson(respones, type);

        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < usersList.size(); i++) {
            if (usersList.size() != usersListNames.size()) {
                userInfo = new StringBuilder().append(usersList.get(i).name).append(" ").append(usersList.get(i).surname);
                usersListNames.add(userInfo.toString().toUpperCase());
            }
        }


        usersListView = requireView().findViewById(R.id.users_list_view);

        for (int i = 0; i < usersList.size(); i++) {

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, usersListNames);
        usersListView.setAdapter(adapter);
      //  usersListView.setOnItemClickListener((parent, view1, position, id) -> openAppInfo(position));


        usersListView.setOnItemLongClickListener((arg0, arg1, pos, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Czy usunąć użytkownika \"" + usersList.get(pos).name + "\"?")
                    .setTitle("Usunięcie użytkownika");
            builder.setPositiveButton("Tak", (dialog, id1) -> {
                try {
                    String result = new DeleteAsyncRetriever().execute("/api/User/" + usersList.get(pos).id).get();
                    usersList.remove(usersList.get(pos));
                    usersListNames.clear();
                    for (int i = 0; i < usersList.size(); i++) {
                        if (usersList.size() != usersListNames.size()) {
                            userInfo = new StringBuilder().append(usersList.get(i).name).append(" ").append(usersList.get(i).surname);
                            usersListNames.add(userInfo.toString().toUpperCase());
                        }
                    }
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, usersListNames);
                    usersListView.setAdapter(adapter1);
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


    }
}