package com.example.new_smart_pillow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    TextView mStatusBlueTv, mPairedTv;
    Button mOnBtn, mOffBtn, mPairedBtn, mDiscoverBtn;
    ImageView mBlueTv;

    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mStatusBlueTv = findViewById(R.id.textView16);
        mPairedTv = findViewById(R.id.textView17);
        mBlueTv = findViewById(R.id.imageView2);

        mOnBtn = findViewById(R.id.button2);
        mOffBtn = findViewById(R.id.button3);
        mDiscoverBtn = findViewById(R.id.button6);
        mPairedBtn = findViewById(R.id.button7);

        //adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //check if bluetooth is available or not
        if (bluetoothAdapter == null) {
            mStatusBlueTv.setText("Bluetooth is not available");
        } else {
            mStatusBlueTv.setText("Bluetooth is available");
        }

        //set image according to bluetooth availability
        if (bluetoothAdapter.isEnabled()) {
            mBlueTv.setImageResource(R.drawable.ic_action_on);
        } else {
            mBlueTv.setImageResource(R.drawable.ic_action_off);
        }

        //on btn click
        mOnBtn.setOnClickListener(v -> {
            if (!bluetoothAdapter.isEnabled()) {
                showToast("Turning On Bluetooth...");
                //intent to on bluetooth
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivityForResult(intent, REQUEST_ENABLE_BT);
            } else {
                showToast("Bluetooth is already On");
            }
        });

        //discover button
        mDiscoverBtn.setOnClickListener(v -> {
            if (!bluetoothAdapter.isDiscovering()) {
                showToast("Making your device discoverable");
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(intent, REQUEST_DISCOVER_BT);
            }
        });

        //off btn
        mOffBtn.setOnClickListener(v -> {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
                showToast("Turning off Bluetooth");
                mBlueTv.setImageResource(R.drawable.ic_action_off);
            }else {
                showToast("Bluetooth is already off");
            }
        });

        //get paired devices
        mPairedBtn.setOnClickListener(v -> {
            if (bluetoothAdapter.isEnabled()) {
                mPairedTv.setText("Paired Devices");
                Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                for (BluetoothDevice device: devices) {
                    mPairedTv.append("\nDevice: " + device.getName() + "," + device);
                }
            } else {
                //bluetooth is off so cant get paired device
                showToast("Turn on bluetooth to get paired devices");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    //bluetooth is on
                    mBlueTv.setImageResource(R.drawable.ic_action_on);
                    showToast("Bluetooth is ON");
                } else {
                    //user denied to turn bluetooth on
                    showToast("Couldn't on bluetooth");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //toast message functions
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}