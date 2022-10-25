package com.example.mitapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class findFriends extends AppCompatActivity {
    private static final String TAG = "FindFriends";


    BluetoothAdapter bluetoothApdater;
    public ArrayList<BluetoothDevice> btDevice = new ArrayList<>();
    public DeviceAdapterList deviceAdapterList;
    ListView lstDevices;

   // Create a BroadcastReceiver for ACTION_FOUND.
   private final BroadcastReceiver bdReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(bluetoothApdater.ACTION_STATE_CHANGED)) {

                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,bluetoothApdater.ERROR);

                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "STATE TURNING ON");
                        break;

                }
                /*// Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address*/
            }
        }
    };
    private final BroadcastReceiver bdReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };
    private BroadcastReceiver bdReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                btDevice.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                deviceAdapterList = new DeviceAdapterList(context, R.layout.device_adapter_view, btDevice);
                lstDevices.setAdapter(deviceAdapterList);

            }
        }
    };


    @Override
    protected void onDestroy(){
        Log.d(TAG,"Destory Called");
        super.onDestroy();
        unregisterReceiver(bdReceiver1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);
        Button btnONOFF = (Button) findViewById(R.id.btnONOFF);
        Button btnEnableDisable_Discoverable = (Button) findViewById(R.id.btnDiscoverable_on_off);
        lstDevices = (ListView) findViewById(R.id.lvNewDevices);
        btDevice = new ArrayList<>();
        bluetoothApdater = BluetoothAdapter.getDefaultAdapter();


        btnONOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Enabling Disabling");
                enableDisableBT();
            }
        });



    }



    private void enableDisableBT() {
        if(bluetoothApdater == null){
            Log.d(TAG, "Does not support BT");

        }

        if(!bluetoothApdater.isEnabled()){
            Log.d(TAG, "Enabling");

            Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enable);

            IntentFilter btIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(bdReceiver1, btIntent);

        }

        if (bluetoothApdater.isEnabled()){
            Log.d(TAG, "Disabling");
            bluetoothApdater.disable();
            IntentFilter btIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(bdReceiver1, btIntent);

        }

    }



    public void btnEnableDisable_Discoverable(View view) {
        Log.d(TAG, "btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(bluetoothApdater.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(bdReceiver2,intentFilter);

    }

    public void btnDiscover(View view) {
        Log.d(TAG, "Looking for unpaired devices");
        if (bluetoothApdater.isDiscovering()){
            bluetoothApdater.cancelDiscovery();
            Log.d(TAG, "Cancelling discovery");
            checkBTPermissions();
            bluetoothApdater.startDiscovery();
            IntentFilter discoverDevice = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(bdReceiver3, discoverDevice);
        }

        if(!bluetoothApdater.isDiscovering()){
            checkBTPermissions();
            bluetoothApdater.startDiscovery();
            IntentFilter discoverDevice = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(bdReceiver3, discoverDevice);
        }

    }

    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }
}
