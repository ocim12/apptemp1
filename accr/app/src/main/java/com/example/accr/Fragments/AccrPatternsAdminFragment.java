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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accr.ApiConnection.DeleteAsyncRetriever;
import com.example.accr.ApiConnection.HttpGETAsyncRetriever;
import com.example.accr.AuxClasses.ApiConstants;
import com.example.accr.ApiConnection.PostPatternAsyncRetriever;
import com.example.accr.Dtos.AttachmentToReturn;
import com.example.accr.Dtos.PatternResponse;
import com.example.accr.Dtos.PatternToAdd;
import com.example.accr.Dtos.UserLoginResponse;
import com.example.accr.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AccrPatternsAdminFragment extends Fragment {

    private UserLoginResponse userLoginResponse;
    private ListView adminsPatternsLV;
    private StringBuilder stringBuilder;
    private String respones = "";
    private ArrayList<PatternResponse> patternsList = new ArrayList<>();
    private ArrayList<String> patternsListNames = new ArrayList<>();
    private List<AttachmentToReturn> attachmentsPattern = new ArrayList<>();
    private Button addPattern;


    public AccrPatternsAdminFragment() {
    }

    public AccrPatternsAdminFragment(UserLoginResponse userLoginResponse) {
        this.userLoginResponse = userLoginResponse;
    }

    public static AccrPatternsAdminFragment newInstance(String param1, String param2) {
        AccrPatternsAdminFragment fragment = new AccrPatternsAdminFragment();
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
        return inflater.inflate(R.layout.fragment_accr_patterns_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        adminsPatternsLV = requireView().findViewById(R.id.patterns_admin_lv);
        addPattern = requireView().findViewById(R.id.add_accr_pattern);

        addPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPatternDialog();
            }
        });

        stringBuilder = new StringBuilder().append(ApiConstants.baseApi + "/api/AccreditationPattern");
        try {
            respones = new HttpGETAsyncRetriever().execute(stringBuilder.toString()).get();
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<PatternResponse>>() {
            }.getType();
            patternsList = gson.fromJson(respones, type);

        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < patternsList.size(); i++) {
            if (patternsList.size() != patternsListNames.size()) {
                patternsListNames.add(patternsList.get(i).name);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, patternsListNames);
        adminsPatternsLV.setAdapter(adapter);
        adminsPatternsLV.setOnItemClickListener((parent, view1, position, id) -> openPatternInfo(position));

        adminsPatternsLV.setOnItemLongClickListener((arg0, arg1, pos, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Czy usunąć wzór akredytacji? \"" + patternsList.get(pos).name + "\"?")
                    .setTitle("Usunięcie wzoru akredytacji");
            builder.setPositiveButton("Tak", (dialog, id1) -> {
                try {
                    String result = new DeleteAsyncRetriever().execute("/api/AccreditationPattern/" + patternsList.get(pos).id).get();

                    patternsList.clear();
                    patternsListNames.clear();

                    stringBuilder = new StringBuilder().append(ApiConstants.baseApi + "/api/AccreditationPattern");
                    try {
                        respones = new HttpGETAsyncRetriever().execute(stringBuilder.toString()).get();
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<PatternResponse>>() {
                        }.getType();
                        patternsList = gson.fromJson(respones, type);

                    } catch (Exception ex) {
                        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    for (int i = 0; i < patternsList.size(); i++) {
                        if (patternsList.size() != patternsListNames.size()) {
                            patternsListNames.add(patternsList.get(i).name);
                        }
                    }

                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, patternsListNames);
                    adminsPatternsLV.setAdapter(adapter1);


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

    public void openPatternInfo(int position) {
        final Dialog patternInfo = new Dialog(getContext());
        patternInfo.setTitle("patterns");
        patternInfo.setContentView(R.layout.pattern_info);

        TextView patternID = patternInfo.findViewById(R.id.pattern_id);
        TextView patternName = patternInfo.findViewById(R.id.pattern_name);
        TextView patternDesc = patternInfo.findViewById(R.id.pattern_desc);
        ListView attachmentListView = patternInfo.findViewById(R.id.attachment_list_pattern);
        Button applyAccr = patternInfo.findViewById(R.id.apply_accr);

        applyAccr.setVisibility(View.INVISIBLE);

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

        attachmentListView.setOnItemLongClickListener((arg0, arg1, pos, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Czy usunąć załącznik \"" + attachmentsPattern.get(pos).name + "\"?")
                    .setTitle("Usunięcie załącznika");
            builder.setPositiveButton("Tak", (dialog, id1) -> {
                try {
                    String result = new DeleteAsyncRetriever().execute("/api/Attachment/" + patternsList.get(position).attachments.get(pos).id).get();
                    patternsList.get(position).attachments.remove(patternsList.get(position).attachments.get(pos));
                    patternsListNames.clear();

                    for (int i = 0; i < patternsList.size(); i++) {
                        if (patternsList.size() != patternsListNames.size()) {
                            patternsListNames.add(patternsList.get(i).name);
                        }
                    }

                    ArrayAdapter<String> adapte1r = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, patternsListNames);
                    adminsPatternsLV.setAdapter(adapte1r);

                    patternInfo.dismiss();

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

        patternInfo.show();
    }

    private ArrayList<String> getAttaNames(List<AttachmentToReturn> attac) {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < attac.size(); i++) {
            names.add(attac.get(i).name);
        }
        return names;
    }

    public void openPatternDialog() {
        final Dialog addAccr = new Dialog(getContext());
        addAccr.setTitle("patterns");
        addAccr.setContentView(R.layout.add_accr_admin);

        EditText accrName = addAccr.findViewById(R.id.patterns_name_admin);
        EditText accrDesc = addAccr.findViewById(R.id.patterns_descr_admin);
        Button addAccrButton = addAccr.findViewById(R.id.add_accr_pattern_button);

        addAccrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PatternToAdd patternToAdd = new PatternToAdd();
                    patternToAdd.name = accrName.getText().toString();
                    patternToAdd.description = accrDesc.getText().toString();
                    String result = new PostPatternAsyncRetriever().execute(patternToAdd).get();

                    patternsList.clear();
                    patternsListNames.clear();

                    stringBuilder = new StringBuilder().append(ApiConstants.baseApi + "/api/AccreditationPattern");
                    try {
                        respones = new HttpGETAsyncRetriever().execute(stringBuilder.toString()).get();
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<PatternResponse>>() {
                        }.getType();
                        patternsList = gson.fromJson(respones, type);

                    } catch (Exception ex) {
                        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    for (int i = 0; i < patternsList.size(); i++) {
                        if (patternsList.size() != patternsListNames.size()) {
                            patternsListNames.add(patternsList.get(i).name);
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, patternsListNames);
                    adminsPatternsLV.setAdapter(adapter);

                    addAccr.dismiss();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        addAccr.show();
    }

}