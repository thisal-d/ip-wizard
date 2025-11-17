package lk.thisald.ipwizard;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class SettingsActivity extends AppCompatActivity {
    ImageButton backBtn;
    Button saveBtn;
    EditText ipRangeCountInputText;
    TextWatcher ipRangeCountInputTextWatcher;
    String ipRangeCount, oldIpRangeCount;
    String SETTINGS_DIRECTORY, SETTINGS_FILE_NAME;

    public String getIpRangesDisplayCount() {
        try {
            File settingsFile = new File(SETTINGS_DIRECTORY, SETTINGS_FILE_NAME);
            if (!settingsFile.exists()) {
                settingsFile.createNewFile();
                FileWriter settingsFileWriter = new FileWriter(settingsFile);
                settingsFileWriter.write("100");
                settingsFileWriter.close();
                return "100";
            } else {
                Scanner settingsFileReader = new Scanner(settingsFile);
                return settingsFileReader.nextLine();
            }
        } catch (Exception e) {
            return "100";
        }
    }

    public void saveSettings() {
        ipRangeCount = ipRangeCountInputText.getText().toString();
        try {
            File settingsFile = new File(SETTINGS_DIRECTORY, SETTINGS_FILE_NAME);
            if (!ipRangeCount.equals(oldIpRangeCount)) {
                FileWriter settingsFileWriter = new FileWriter(settingsFile);
                settingsFileWriter.write(ipRangeCount);
                settingsFileWriter.close();
                restart();
            } else {
                back();
            }
        } catch (Exception e) {
        }
    }

    public void restart() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void back() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backBtn = findViewById(R.id.backButton);
        saveBtn = findViewById(R.id.saveButton);
        ipRangeCountInputText = findViewById(R.id.ipRangesCountInputText);

        SETTINGS_DIRECTORY = getFilesDir().toString();
        SETTINGS_FILE_NAME = "settings.txt";

        oldIpRangeCount = getIpRangesDisplayCount();
        ipRangeCountInputText.setText(getIpRangesDisplayCount());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettings();
            }
        });

        ipRangeCountInputTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                saveBtn.setEnabled(Valid.isValidIpRangeCount(ipRangeCountInputText.getText().toString()));
            }
        };

        ipRangeCountInputText.addTextChangedListener(ipRangeCountInputTextWatcher);
    }
}