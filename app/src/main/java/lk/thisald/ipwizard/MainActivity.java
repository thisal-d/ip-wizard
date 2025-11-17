package lk.thisald.ipwizard;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    // all the vars
    Integer usableDisplayWidth, usableDisplayHeight;
    Float displayDensity;
    Integer CLASS_A_MAX_IPS_COUNT, CLASS_B_MAX_IPS_COUNT, CLASS_C_MAX_IPS_COUNT, maxIpsCount;

    String CLASS_A_DEFAULT_SUBNET_MASK, CLASS_B_DEFAULT_SUBNET_MASK, CLASS_C_DEFAULT_SUBNET_MASK;
    Boolean isIpValid, isHostCountValid, isSubnetCountValid, isSubnetMaskValid;
    String validatedIpAddress, validatedSubnetMask;
    Integer reqHostCount, reqSubnetCount;
    String IP_ADDRESS_HINT, HOST_COUNT_HINT, SUBNET_COUNT_HINT, SUBNET_MASK_HINT;
    String ipClass;
    Integer section1WidgetHeightDp, section2WidgetHeightDp, section3WidgetHeightDp, section4WidgetHeightPx,
            versionLabelHeightDp;
    String subnetCount, hostCount, subnetMask, ipAddress;
    String calculatedSubnetMask;
    Integer calculatedNetBitsCount, calculatedHostBitCount;
    Double calculatedIpAddressCount, calculatedSubnetCount, calculatedHostCount;
    Integer[] netAndHostBitsCount = new Integer[2];
    Integer IP_RANGES_DISPLAY_COUNT, IP_RANGES_TEXT_VIEW_WIDTH, SPLITTER_TEXT_VIEW_WIDTH;

    TableRow[] ipRangesDisplayTableRows;
    TextView[][] ipRangesDisplayTextViews;
    // TextWatchers
    TextWatcher ipAddressInputTextWatcher, subnetMaskInputTextWatcher, subnetCountInputTextWatcher,
            hostCountInputTextWatcher;

    // All the widgets
    ConstraintLayout widgetSection1, widgetSection2, widgetSection3, widgetSection4;
    TableLayout ipRangeOutputTable;
    ScrollView ipRangeOutputScrollview;
    EditText ipAddressInputText, hostCountInputText, subnetCountInputText, subnetMaskInputText;
    TextView ipAddressCountOutputText, hostCountOutputText,
            subnetMaskOutputText, subnetCountOutputText,
            availableIpAddressCountOutputText, defaultSubnetMaskOutputText, ipClassOutputText,
            ipRangesCountDisplayOutputText;

    ColorStateList defaultTextColor;
    ImageButton settingBtn;

    String FILE_DIRECTORY, SETTINGS_FILE_NAME;

    // used to set disable input text boxes edit text
    private void setEnabled(EditText widget, String hint) {
        widget.setEnabled(true);
        widget.setHint(hint);
    }

    // used to set disable input text boxes edit text
    private void setDisabled(EditText widget, String hint) {
        widget.setEnabled(false);
        widget.setHint(hint);
    }

    // generate the ip ranges as array and display of it
    private void displayIpRange(String calculatedSubnetMask) {
        String[][] ranges = Calculate.generateRange(calculatedSubnetMask, ipClass, validatedIpAddress,
                IP_RANGES_DISPLAY_COUNT);
        ipRangeOutputTable.removeAllViewsInLayout();
        for (int i = 0; i < IP_RANGES_DISPLAY_COUNT; i++) {
            if (ranges[i][0] == null)
                break;
            ipRangesDisplayTextViews[i][0].setText(ranges[i][0]);
            ipRangesDisplayTextViews[i][2].setText(ranges[i][1]);
            ipRangeOutputTable.addView(ipRangesDisplayTableRows[i]);
        }
    }

    private void resetOutputs() {
        subnetCountOutputText.setText("0");
        ipAddressCountOutputText.setText("0");
        hostCountOutputText.setText("0");
        subnetMaskOutputText.setText("0.0.0.0");
        ipRangeOutputTable.removeAllViewsInLayout();
    }

    public Boolean setHistory(String fileName) {
        File file = new File(FILE_DIRECTORY, fileName);
        try {
            if (!file.exists()) {
                return false;
            }
            Scanner readFile = new Scanner(file);
            String valueIs = readFile.nextLine();
            String value = readFile.nextLine();
            readFile.close();
            if (valueIs.equals("subnet_count"))
                subnetCountInputText.setText(value);
            if (valueIs.equals("host_count"))
                hostCountInputText.setText(value);
            if (valueIs.equals("subnet_mask"))
                subnetMaskInputText.setText(value);
            if (valueIs.equals("ip_address"))
                ipAddressInputText.setText(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void saveHistory(String fileName, String valueIs, String value) {
        File file = new File(FILE_DIRECTORY, fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writeFile = new FileWriter(file);
            writeFile.write(valueIs);
            writeFile.write("\n");
            writeFile.write(value);
            writeFile.close();
        } catch (Exception e) {
        }
    }

    private void calculate() {
        if (isSubnetCountValid || isHostCountValid || isSubnetMaskValid) {
            // calculate net_bits_count & host_bit_count
            if (isSubnetCountValid)
                netAndHostBitsCount = Calculate.getNetBitsHostBits(ipClass, reqSubnetCount, -1);
            if (isHostCountValid)
                netAndHostBitsCount = Calculate.getNetBitsHostBits(ipClass, -1, reqHostCount + 2);
            if (isSubnetMaskValid)
                netAndHostBitsCount = Calculate.getNetBitsHostBits(validatedSubnetMask, ipClass);
            // passing to to another var to calculate net_bits_count & host_bit_count
            calculatedNetBitsCount = netAndHostBitsCount[0];
            calculatedHostBitCount = netAndHostBitsCount[1];
            // check if subnet mask is valid if not we have to calculate it
            if (!isSubnetMaskValid)
                calculatedSubnetMask = Calculate.generateSubnetMask(ipClass, calculatedNetBitsCount,
                        calculatedHostBitCount);
            else
                calculatedSubnetMask = validatedSubnetMask;
            calculatedSubnetCount = Math.pow(2.0, calculatedNetBitsCount);
            calculatedIpAddressCount = Math.pow(2.0, calculatedHostBitCount);
            if (calculatedIpAddressCount < 2)
                calculatedHostCount = 0.0;
            else
                calculatedHostCount = calculatedIpAddressCount - 2;

            subnetCountOutputText.setText(String.valueOf(calculatedSubnetCount.intValue()));
            ipAddressCountOutputText.setText(String.valueOf(calculatedIpAddressCount.intValue()));
            hostCountOutputText.setText(String.valueOf(calculatedHostCount.intValue()));
            subnetMaskOutputText.setText(calculatedSubnetMask);

            displayIpRange(calculatedSubnetMask);
        }
    }

    private void validateSubnetCount() {
        subnetCount = subnetCountInputText.getText().toString();
        saveHistory("value.txt", "subnet_count", subnetCount);
        if (!subnetCount.equals("")) {
            setDisabled(hostCountInputText, "-");
            setDisabled(subnetMaskInputText, "-");
            isSubnetCountValid = Valid.isValidSubnetCount(subnetCount, maxIpsCount);
            if (isSubnetCountValid) {
                subnetCountInputText.setTextColor(defaultTextColor);
                reqSubnetCount = Integer.parseInt(subnetCount);
                calculate();
            } else {
                subnetCountInputText.setTextColor(Color.argb(255, 255, 0, 0));
                resetOutputs();
            }
        } else {
            setEnabled(hostCountInputText, HOST_COUNT_HINT);
            setEnabled(subnetMaskInputText, SUBNET_MASK_HINT);
            isSubnetCountValid = false;
            resetOutputs();
        }
    }

    private void validateHostCount() {
        hostCount = hostCountInputText.getText().toString();
        saveHistory("value.txt", "host_count", hostCount);
        if (!hostCount.equals("")) {
            setDisabled(subnetCountInputText, "-");
            setDisabled(subnetMaskInputText, "-");
            isHostCountValid = Valid.isValidHostCount(hostCount, maxIpsCount);
            if (isHostCountValid) {
                hostCountInputText.setTextColor(defaultTextColor);
                reqHostCount = Integer.parseInt(hostCount);
                calculate();
            } else {
                hostCountInputText.setTextColor(Color.argb(255, 255, 0, 0));
                resetOutputs();
            }
        } else {
            setEnabled(subnetCountInputText, SUBNET_COUNT_HINT);
            setEnabled(subnetMaskInputText, SUBNET_MASK_HINT);
            isHostCountValid = false;
            resetOutputs();
        }
    }

    private void validateSubnetMask() {
        subnetMask = subnetMaskInputText.getText().toString();
        saveHistory("value.txt", "subnet_mask", subnetMask);
        if (!subnetMask.equals("")) {
            setDisabled(hostCountInputText, "-");
            setDisabled(subnetCountInputText, "-");
            isSubnetMaskValid = Valid.isValidSubnetMask(subnetMask, ipClass);
            if (isSubnetMaskValid) {
                subnetMaskInputText.setTextColor(defaultTextColor);
                validatedSubnetMask = subnetMask;
                setFormattedSubnetMask();
                calculate();
            } else {
                subnetMaskInputText.setTextColor(Color.argb(255, 255, 0, 0));
                resetOutputs();
            }
        } else {
            setEnabled(hostCountInputText, HOST_COUNT_HINT);
            setEnabled(subnetCountInputText, SUBNET_COUNT_HINT);
            isSubnetMaskValid = false;
            resetOutputs();
        }
    }

    private void setFormattedSubnetMask() {
        new RemoveTextWatcher(subnetMaskInputText, subnetMaskInputTextWatcher).run();
        validatedSubnetMask = Calculate.getFormattedAddress(validatedSubnetMask);
        subnetMaskInputText.setText(validatedSubnetMask);
        subnetMaskInputText.addTextChangedListener(subnetMaskInputTextWatcher);
    }

    private void setFormattedIpAddress() {
        new RemoveTextWatcher(ipAddressInputText, ipAddressInputTextWatcher).run();
        validatedIpAddress = Calculate.getFormattedAddressByClass(validatedIpAddress, ipClass);
        ipAddressInputText.setText(validatedIpAddress);
        ipAddressInputText.addTextChangedListener(ipAddressInputTextWatcher);
    }

    private void setIpAddressDetails(String ipClass, String ipsCount, String defaultSubnetMask) {
        defaultSubnetMaskOutputText.setText(defaultSubnetMask);
        availableIpAddressCountOutputText.setText(ipsCount);
        ipClassOutputText.setText(ipClass);
    }

    private void validateIpAddress() {
        ipAddress = ipAddressInputText.getText().toString();
        saveHistory("ip_address.txt", "ip_address", ipAddress);
        isIpValid = Valid.ipFormatIsValid(ipAddress);
        if (isIpValid) {
            ipAddressInputText.setTextColor(defaultTextColor);
            ipClass = Valid.getIpClass(ipAddress);
            validatedIpAddress = ipAddress;
            if (ipClass.equals("C")) {
                maxIpsCount = CLASS_C_MAX_IPS_COUNT;
                setIpAddressDetails("Class C", CLASS_C_MAX_IPS_COUNT.toString(), CLASS_C_DEFAULT_SUBNET_MASK);
            } else if (ipClass.equals("B")) {
                maxIpsCount = CLASS_B_MAX_IPS_COUNT;
                setIpAddressDetails("Class B", CLASS_B_MAX_IPS_COUNT.toString(), CLASS_B_DEFAULT_SUBNET_MASK);
            } else {
                maxIpsCount = CLASS_A_MAX_IPS_COUNT;
                setIpAddressDetails("Class A", CLASS_A_MAX_IPS_COUNT.toString(), CLASS_A_DEFAULT_SUBNET_MASK);
            }
            setFormattedIpAddress();
            if (subnetMaskInputText.isEnabled())
                validateSubnetMask();
            if (hostCountInputText.isEnabled())
                validateHostCount();
            if (subnetCountInputText.isEnabled())
                validateSubnetCount();
            calculate();
        } else {
            resetOutputs();
            maxIpsCount = 0;
            ipAddressInputText.setTextColor(Color.argb(255, 255, 0, 0));
            if (subnetMaskInputText.isEnabled())
                validateSubnetMask();
            if (hostCountInputText.isEnabled())
                validateHostCount();
            if (subnetCountInputText.isEnabled())
                validateSubnetCount();
            setIpAddressDetails("None", "None", "None");
        }
    }

    public void goToSettings() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public int getIpRangesDisplayCount() {
        try {
            File settingsFile = new File(FILE_DIRECTORY, SETTINGS_FILE_NAME);
            if (!settingsFile.exists()) {
                settingsFile.createNewFile();
                FileWriter settingsFileWriter = new FileWriter(settingsFile);
                settingsFileWriter.write("100");
                settingsFileWriter.close();
                return 100;
            } else {
                Scanner settingsFileReader = new Scanner(settingsFile);
                return Integer.parseInt(settingsFileReader.nextLine());
            }
        } catch (Exception e) {
            return 100;
        }
    }

    // convert px to dp ,this uses when displaying ip ranges
    // calculate the section 4 height
    public int pxToDp(int px) {
        return (int) (px / displayDensity);
    }

    public int dpToPx(int dp) {
        return (int) (dp * displayDensity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get display width & height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        usableDisplayWidth = displayMetrics.widthPixels;
        usableDisplayHeight = displayMetrics.heightPixels;
        displayDensity = Resources.getSystem().getDisplayMetrics().density;

        // get all widget using id
        widgetSection1 = findViewById(R.id.section1);
        widgetSection2 = findViewById(R.id.section2);
        widgetSection3 = findViewById(R.id.section3);
        widgetSection4 = findViewById(R.id.section4);
        ipRangeOutputTable = findViewById(R.id.ipAddressRangeTableOutput);
        ipAddressInputText = findViewById(R.id.ipAddressTextInput);
        hostCountInputText = findViewById(R.id.hostCountTextInput);
        subnetCountInputText = findViewById(R.id.subnetCountTextInput);
        subnetMaskInputText = findViewById(R.id.subnetMaskTextInput);
        ipAddressCountOutputText = findViewById(R.id.ipAddressesCountTextOutput);
        hostCountOutputText = findViewById(R.id.hostCountTextOutput);
        subnetMaskOutputText = findViewById(R.id.subnetMaskTextOutput);
        subnetCountOutputText = findViewById(R.id.subnetCountTextOutput);
        ipRangeOutputScrollview = findViewById(R.id.ipAddressRangesOutputScrollview);
        availableIpAddressCountOutputText = findViewById(R.id.availableIpAddressCountOutputText);
        defaultSubnetMaskOutputText = findViewById(R.id.defaulSubnetMaskOututText);
        ipClassOutputText = findViewById(R.id.ipClassTextOutput);
        settingBtn = findViewById(R.id.settingButton);
        ipRangesCountDisplayOutputText = findViewById(R.id.ipRangesDisplayCountOutputText);

        // widget size
        section1WidgetHeightDp = 80;
        section2WidgetHeightDp = 195;
        section3WidgetHeightDp = 140;
        versionLabelHeightDp = 10;

        // calculate widget section_4 height and set it
        section4WidgetHeightPx = usableDisplayHeight - (dpToPx(section1WidgetHeightDp) + dpToPx(section2WidgetHeightDp)
                + dpToPx(section3WidgetHeightDp) + dpToPx(versionLabelHeightDp));
        widgetSection4.setMaxHeight(section4WidgetHeightPx);
        widgetSection4.setMinHeight(section4WidgetHeightPx);

        defaultTextColor = ipAddressInputText.getTextColors();

        FILE_DIRECTORY = getFilesDir().toString();
        SETTINGS_FILE_NAME = "settings.txt";
        // constant values
        IP_ADDRESS_HINT = "192.168.0.0";
        HOST_COUNT_HINT = "62";
        SUBNET_COUNT_HINT = "4";
        SUBNET_MASK_HINT = "255.255.192.0";
        CLASS_A_MAX_IPS_COUNT = 1677721;
        CLASS_B_MAX_IPS_COUNT = 65536;
        CLASS_C_MAX_IPS_COUNT = 256;
        CLASS_A_DEFAULT_SUBNET_MASK = "255.0.0.0";
        CLASS_B_DEFAULT_SUBNET_MASK = "255.255.0.0";
        CLASS_C_DEFAULT_SUBNET_MASK = "255.255.255.0";
        maxIpsCount = 0;
        ipClass = "N";
        IP_RANGES_DISPLAY_COUNT = getIpRangesDisplayCount();
        ipRangesCountDisplayOutputText.setText(IP_RANGES_DISPLAY_COUNT.toString());

        isIpValid = false;
        isHostCountValid = false;
        isSubnetCountValid = false;
        isSubnetMaskValid = false;

        SPLITTER_TEXT_VIEW_WIDTH = 50;
        IP_RANGES_TEXT_VIEW_WIDTH = (usableDisplayWidth - SPLITTER_TEXT_VIEW_WIDTH * 2) / 2;
        ipRangesDisplayTableRows = new TableRow[IP_RANGES_DISPLAY_COUNT];
        ipRangesDisplayTextViews = new TextView[IP_RANGES_DISPLAY_COUNT][3];

        // Pre Create widget for get more performance
        // used to display ranges
        for (int i = 0; i < IP_RANGES_DISPLAY_COUNT; i++) {
            ipRangesDisplayTableRows[i] = new TableRow(this);

            ipRangesDisplayTextViews[i][0] = new TextView(this);
            ipRangesDisplayTextViews[i][0].setTextSize(16);
            ipRangesDisplayTextViews[i][0].setTypeface(Typeface.DEFAULT_BOLD);
            ipRangesDisplayTextViews[i][0].setPadding(0, 0, 50, 0);
            ipRangesDisplayTextViews[i][0].setWidth(IP_RANGES_TEXT_VIEW_WIDTH);
            ipRangesDisplayTextViews[i][0].setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

            ipRangesDisplayTextViews[i][1] = new TextView(this);
            ipRangesDisplayTextViews[i][1].setText("-");
            ipRangesDisplayTextViews[i][1].setWidth(100);
            ipRangesDisplayTextViews[i][1].setTextSize(16);
            ipRangesDisplayTextViews[i][1].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            ipRangesDisplayTextViews[i][2] = new TextView(this);
            ipRangesDisplayTextViews[i][2].setTextSize(16);
            ipRangesDisplayTextViews[i][2].setTypeface(Typeface.DEFAULT_BOLD);
            ipRangesDisplayTextViews[i][2].setPadding(50, 0, 0, 0);
            ipRangesDisplayTextViews[i][2].setWidth(IP_RANGES_TEXT_VIEW_WIDTH);
            ipRangesDisplayTextViews[i][2].setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

            ipRangesDisplayTableRows[i].addView(ipRangesDisplayTextViews[i][0]);
            ipRangesDisplayTableRows[i].addView(ipRangesDisplayTextViews[i][1]);
            ipRangesDisplayTableRows[i].addView(ipRangesDisplayTextViews[i][2]);
        }

        // This Text watcher checking host count input box
        hostCountInputTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateHostCount();
            }
        };
        hostCountInputText.addTextChangedListener(hostCountInputTextWatcher);

        // This Text watcher checking subnet count input box
        subnetCountInputTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateSubnetCount();
            }
        };
        subnetCountInputText.addTextChangedListener(subnetCountInputTextWatcher);

        // This Text watcher checking subnet mask input box
        subnetMaskInputTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateSubnetMask();
            }
        };
        subnetMaskInputText.addTextChangedListener(subnetMaskInputTextWatcher);

        // This Text watcher checking ip address input box
        ipAddressInputTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateIpAddress();
            }
        };
        ipAddressInputText.addTextChangedListener(ipAddressInputTextWatcher);

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSettings();
            }
        });

        setHistory("value.txt");
        setHistory("ip_address.txt");
    }
}