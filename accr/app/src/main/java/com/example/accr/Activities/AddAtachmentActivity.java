package com.example.accr.Activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.accr.ApiConnection.AddFileAsyncRetriever;
import com.example.accr.Dtos.AddFileToApplicationRequest;
import com.example.accr.Dtos.AppToReturn;
import com.example.accr.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AddAtachmentActivity extends AppCompatActivity {

    private EditText attachmentName;
    private EditText attachmentDescription;
    private TextView fileName;
    private Button addFile;
    private Button addAttachment;
    private ArrayList<AppToReturn> appsList = new ArrayList<>();
    private int position = 0;
    private String jsonApps;
    private int requestCode = 1;
    private Uri uri;
    private File file;
    private String myJpgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_atachment);
        initializeWidgets();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }

            Uri uri = data.getData();
            String src = uri.getPath();

            file = new File(src);

            Cursor returnCursor =
                    getContentResolver().query(uri, null, null, null, null);

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();

            fileName.setText(returnCursor.getString(nameIndex));
        }
    }

    private void initializeWidgets() {

        attachmentName = findViewById(R.id.attachment_name);
        attachmentDescription = findViewById(R.id.attachment_descr);
        addFile = findViewById(R.id.add_file_attachment);
        addFile.setOnClickListener(v -> chooseFile());
        addAttachment = findViewById(R.id.add_attachment);
        addAttachment.setOnClickListener(v -> addAttachmentToApp());
        fileName = findViewById(R.id.file_name_attachment);

        Intent intent = getIntent();
        jsonApps = intent.getExtras().getString("json-apps");
        position = intent.getExtras().getInt("index");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<AppToReturn>>() {
        }.getType();
        appsList = gson.fromJson(jsonApps, type);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
    }

    private void addAttachmentToApp() {
        AddFileToApplicationRequest request = new AddFileToApplicationRequest();

        String path = Environment.getExternalStorageDirectory().getPath();
        myJpgPath = path + "/Download/3408.jpg";

        file = new File(myJpgPath);

        request.file = file;
        request.description = attachmentDescription.getText().toString();
        request.name = attachmentName.getText().toString();
        request.applicationId = appsList.get(position).id;
        request.url = myJpgPath;

        try {
            String reqeust = new AddFileAsyncRetriever().execute(request).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void chooseFile() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, requestCode);
    }

    public File getFile() {
        return file;
    }
}