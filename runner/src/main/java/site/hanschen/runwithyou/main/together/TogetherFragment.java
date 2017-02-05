package site.hanschen.runwithyou.main.together;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import javax.inject.Inject;

import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.application.RunnerApplication;
import site.hanschen.runwithyou.base.RunnerBaseFragment;


/**
 * @author HansChen
 */
public class TogetherFragment extends RunnerBaseFragment implements View.OnClickListener {

    private static final int REQUEST_BT_ENABLE       = 1;
    private static final int REQUEST_BT_DISCOVERABLE = 2;


    private Button mConnectBtn;
    private Button mDiscoverableBtn;
    @Inject
    BluetoothAdapter mBluetoothAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerTogetherComponent.builder()
                               .applicationComponent(RunnerApplication.getInstance().getAppComponent())
                               .build()
                               .inject(TogetherFragment.this);
        enableBluetooth();
    }

    private void enableBluetooth() {
        if (mBluetoothAdapter == null) {
            Toast.makeText(mContext.getApplicationContext(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_BT_ENABLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_together, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mConnectBtn = (Button) view.findViewById(R.id.together_connect);
        mConnectBtn.setOnClickListener(this);
        mDiscoverableBtn = (Button) view.findViewById(R.id.together_make_discoverable);
        mDiscoverableBtn.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_together_fragment, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_BT_ENABLE:
                if (resultCode != Activity.RESULT_OK) {
                    Toast.makeText(getActivity(), "Bluetooth was not enabled", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_BT_DISCOVERABLE:
                if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getActivity(), "user has rejected the request", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Bluetooth is discoverable now", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.together_connect:
                break;
            case R.id.together_make_discoverable:
                ensureDiscoverable();
                break;
        }
    }

    /**
     * Makes this device discoverable.
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivityForResult(discoverableIntent, REQUEST_BT_DISCOVERABLE);
        } else {
            Toast.makeText(getActivity(), "Bluetooth is discoverable now", Toast.LENGTH_SHORT).show();
        }
    }
}
