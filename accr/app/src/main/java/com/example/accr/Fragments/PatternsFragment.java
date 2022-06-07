package com.example.accr.Fragments;

import android.app.AlertDialog;
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
import com.example.accr.ApiConnection.PostApplicationAsyncRetriever;
import com.example.accr.AuxClasses.ApiConstants;
import com.example.accr.AuxClasses.SharedPreferencesManager;
import com.example.accr.Dtos.ApplicationToAdd;
import com.example.accr.Dtos.AttachmentToReturn;
import com.example.accr.Dtos.PatternResponse;
import com.example.accr.Dtos.UserLoginResponse;
import com.example.accr.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PatternsFragment extends Fragment {

    private UserLoginResponse userLoginResponse;
    private ListView patternsListView;
    private List<PatternResponse> patternsList = new ArrayList<>();
    private List<AttachmentToReturn> attachmentsPattern = new ArrayList<>();
    private String respones = "";


    public PatternsFragment() {
        // Required empty public constructor
    }

    public PatternsFragment(UserLoginResponse userLoginResponse) {
        this.userLoginResponse = userLoginResponse;
    }

    public static PatternsFragment newInstance(String param1, String param2) {
        PatternsFragment fragment = new PatternsFragment();
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
        return inflater.inflate(R.layout.fragment_patterns, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        StringBuilder stringBuilder = new StringBuilder().append(ApiConstants.baseApi + "/api/AccreditationPattern");
        try {
            respones = new HttpGETAsyncRetriever().execute(stringBuilder.toString()).get();
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<PatternResponse>>() {
            }.getType();
            patternsList = gson.fromJson(respones, type);

        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        patternsListView = requireView().findViewById(R.id.patterns_listview);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getPatternsNames(patternsList));
        patternsListView.setAdapter(adapter);

        patternsListView.setOnItemClickListener((parent, view1, position, id) -> openPatternInfo(position));
    }

    public void openPatternInfo(int position) {
        final Dialog patternInfo = new Dialog(getContext());
        patternInfo.setTitle("patterns");
        patternInfo.setContentView(R.layout.pattern_info);

        TextView patternID = patternInfo.findViewById(R.id.pattern_id);
        TextView patternName = patternInfo.findViewById(R.id.pattern_name);
        TextView patternDesc = patternInfo.findViewById(R.id.pattern_desc);
        ListView attachmentListView = patternInfo.findViewById(R.id.attachment_list_pattern);
        Button applyAccr = patternInfo.findViewById(R.id.apply_accr);

        applyAccr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ApplicationToAdd applicationToAdd = new ApplicationToAdd();
                    applicationToAdd.patternId = patternsList.get(position).id;
                    applicationToAdd.userId = SharedPreferencesManager.getUser().id;
                    String result = new PostApplicationAsyncRetriever().execute(applicationToAdd).get();
                    patternInfo.dismiss();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        patternID.setText("" + patternsList.get(position).id);
        patternName.setText(patternsList.get(position).name);
        patternDesc.setText(patternsList.get(position).description);

        attachmentsPattern = patternsList.get(position).attachments;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getAttaNames(attachmentsPattern));
        attachmentListView.setAdapter(adapter);

        attachmentListView.setOnItemClickListener((parent, view, position1, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Czy pobrać załącznik \"" + attachmentsPattern.get(position1).name + "\"?")
                    .setTitle("Pobranie załącznika");
            builder.setPositiveButton("Tak", (dialog, id1) -> Toast.makeText(getContext(), "pls tak pobierz mnie", Toast.LENGTH_SHORT).show()).setNegativeButton("Nie", (dialog, id12) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        patternInfo.show();
    }

    private ArrayList<String> getPatternsNames(List<PatternResponse> patterns) {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < patterns.size(); i++) {
            names.add(patterns.get(i).name);
        }
        return names;
    }

    private ArrayList<String> getAttaNames(List<AttachmentToReturn> attac) {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < attac.size(); i++) {
            names.add(attac.get(i).name);
        }
        return names;
    }

}