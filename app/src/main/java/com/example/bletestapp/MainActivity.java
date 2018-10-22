package com.example.bletestapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //TODO defining TAG variable
    private static final String TAG = "MainActivity";
    //TODO this will initialise bluetooth
    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO content from activity_main.xml
        setContentView(R.layout.activity_main);
        //TODO defining xml switch context to java object
        Switch switchonoff = (Switch) findViewById(R.id.onoff);
        //TODO initialising Bluetooth for default hardware
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //TODO defining click event of button
        switchonoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO console print
                Log.d(TAG, "onClick: enabling/disabling bluetooth.");
                //TODO calling another function defined below
                enableDisableBT();
            }
        });


        //TODO defining xml button context to java object
        Button btsearch = (Button) findViewById(R.id.btsearch);
        //TODO defining click event of button
        btsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO console print
                Log.d(TAG, "onClick: search bluetooth.");
                mBluetoothAdapter.startDiscovery();
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, filter);
            }
        });



    }

    public void enableDisableBT() {
        //TODO checking bluetooth state
        if (mBluetoothAdapter == null) {
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        } else if (!mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBT: enabling BT.");
            //TODO making Activity to bluetooth Enable
            //Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //TODO starting Activity to bluetooth Enable
            //startActivity(enableBTIntent);
            //TODO creating IntentFilter  with action
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            //TODO calling onReceive of IntentFilter
            registerReceiver(mBroadcastReceiver1, BTIntent);
        } else if (mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBT: disabling BT.");
            //TODO disable the Bluetooth
            mBluetoothAdapter.disable();
            //TODO creating IntentFilter with action
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            //TODO calling onReceive of IntentFilter
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }

    }



    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };


//    private Set<BluetoothDevice> pairedDevices;
//    ListView lv;
//
//    public void list(View v) {
//        pairedDevices = mBluetoothAdapter.getBondedDevices();
//
//        ArrayList list = new ArrayList();
//
//        for (BluetoothDevice bt : pairedDevices) list.add(bt.getName());
//        Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();
//
//        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
//
//        lv.setAdapter(adapter);
//    }

    private ListView btlist;
    private ArrayList<String> mDeviceList = new ArrayList<String>();
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDeviceList.add(device.getName() + "\n" + device.getAddress());
                Log.i("BT", device.getName() + "\n" + device.getAddress());
                btlist.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mDeviceList));
            }
        }
    };

}