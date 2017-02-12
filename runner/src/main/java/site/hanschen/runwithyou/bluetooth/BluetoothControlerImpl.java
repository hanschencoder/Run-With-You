package site.hanschen.runwithyou.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.inject.Inject;

import site.hanschen.runwithyou.dagger.AppContext;
import site.hanschen.runwithyou.main.devicelist.bean.Device;

import static site.hanschen.runwithyou.bluetooth.BluetoothControlerImpl.ConnectionState.STATE_CONNECTED;
import static site.hanschen.runwithyou.bluetooth.BluetoothControlerImpl.ConnectionState.STATE_CONNECTING;
import static site.hanschen.runwithyou.bluetooth.BluetoothControlerImpl.ConnectionState.STATE_LISTEN;
import static site.hanschen.runwithyou.bluetooth.BluetoothControlerImpl.ConnectionState.STATE_NONE;

public class BluetoothControlerImpl implements BluetoothControler {
    // Debugging
    private static final String TAG = "BluetoothControlerImpl";

    // Name for the SDP record when creating server socket
    private static final String NAME_SECURE   = "BluetoothSecure";
    private static final String NAME_INSECURE = "BluetoothInsecure";

    // Unique UUID for this application
    private static final UUID MY_UUID_SECURE   = UUID.fromString("768665b1-dd18-4115-bd65-bf4b686f7725");
    private static final UUID MY_UUID_INSECURE = UUID.fromString("c155917b-84b1-4d81-a1c0-90b3cdb8f18c");

    // Member fields
    private final BluetoothAdapter  mAdapter;
    private       AcceptThread      mSecureAcceptThread;
    private       AcceptThread      mInsecureAcceptThread;
    private       ConnectThread     mConnectThread;
    private       ConnectedThread   mConnectedThread;
    private       ConnectionState   mState;
    private       BluetoothListener mBluetoothListener;
    private       Device            mTarget;

    // Constants that indicate the current connection state
    enum ConnectionState {
        STATE_NONE,
        STATE_LISTEN,
        STATE_CONNECTING,
        STATE_CONNECTED
    }

    @Inject
    public BluetoothControlerImpl(@AppContext Context context, BluetoothAdapter adapter) {
        this.mAdapter = adapter;
        this.mState = STATE_NONE;
    }

    @Override
    public void listenInsecure(int timeout) {
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread(false, timeout);
            mInsecureAcceptThread.start();
        }
        setState(STATE_LISTEN);
    }

    @Override
    public void listenSecure(int timeout) {
        if (mSecureAcceptThread == null) {
            mSecureAcceptThread = new AcceptThread(true, timeout);
            mSecureAcceptThread.start();
        }
        setState(STATE_LISTEN);
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    @Override
    public void connect(Device device, boolean secure) {
        Log.d(TAG, "connect to: " + device);

        mTarget = device;
        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(mAdapter.getRemoteDevice(device.getAddress()), secure);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    @Override
    public void sendData(byte[] out) {
        ConnectedThread r;
        synchronized (this) {
            if (getState() != STATE_CONNECTED) {
                return;
            }
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    @Override
    public void registerListener(BluetoothListener listener) {
        mBluetoothListener = listener;
    }

    @Override
    public void clearListener() {
        mBluetoothListener = null;
    }

    /**
     * Set the current state of the chat connection
     *
     * @param state An integer defining the current connection state
     */
    private synchronized void setState(ConnectionState state) {
        Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;

        if (mBluetoothListener == null) {
            return;
        }

        switch (state) {
            case STATE_LISTEN:
                mBluetoothListener.onListenStart();
                break;
            case STATE_CONNECTING:
                mBluetoothListener.onConnectStart(mTarget);
                break;
            case STATE_CONNECTED:
                mBluetoothListener.onConnectSucceed(mTarget);
                break;
            default:
                break;
        }
    }

    /**
     * Return the current connection state.
     */
    public synchronized ConnectionState getState() {
        return mState;
    }

    public synchronized void reset() {
        Log.d(TAG, "reset");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if (mSecureAcceptThread != null) {
            mSecureAcceptThread.cancel();
            mSecureAcceptThread = null;
        }

        if (mInsecureAcceptThread != null) {
            mInsecureAcceptThread.cancel();
            mInsecureAcceptThread = null;
        }

        setState(STATE_NONE);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     */
    private synchronized void connected(BluetoothSocket socket, final boolean secure) {
        Log.d(TAG, "connected, Socket Type:" + getSocketType(secure));

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Cancel the accept thread because we only want to connect to one device
        if (mSecureAcceptThread != null) {
            mSecureAcceptThread.cancel();
            mSecureAcceptThread = null;
        }
        if (mInsecureAcceptThread != null) {
            mInsecureAcceptThread.cancel();
            mInsecureAcceptThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket, secure);
        mConnectedThread.start();

        // Send the name of the connected device back to the UI Activity
        setState(STATE_CONNECTED);
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        // Send a failure message back to the Activity
        if (mBluetoothListener != null) {
            mBluetoothListener.onConnectFailed(null);
        }
        // Start the service over to restart listening mode
        reset();
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        // Send a failure message back to the Activity
        if (mBluetoothListener != null) {
            mBluetoothListener.onConnectLost();
        }
        // Start the service over to restart listening mode
        reset();
    }

    private String getSocketType(boolean secure) {
        return secure ? "Secure" : "Insecure";
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted (or until timeout).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;
        private       boolean               mSecure;
        private       int                   mTimeout;

        AcceptThread(boolean secure, int timeout) {
            BluetoothServerSocket tmp = null;
            this.mSecure = secure;
            this.mTimeout = timeout;

            // Create a new listening server socket
            try {
                if (secure) {
                    tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID_SECURE);
                } else {
                    tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME_INSECURE, MY_UUID_INSECURE);
                }
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + getSocketType(secure) + "listen() failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            Log.d(TAG, "Socket Type: " + getSocketType(mSecure) + "BEGIN mAcceptThread" + this);
            setName("AcceptThread " + getSocketType(mSecure));

            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            if (mState != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a successful connection or an exception
                    socket = mmServerSocket.accept(mTimeout);
                } catch (IOException e) {
                    Log.e(TAG, "Socket Type: " + getSocketType(mSecure) + "accept() failed", e);
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (BluetoothControlerImpl.this) {
                        switch (mState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // Situation normal. Start the connected thread.
                                connected(socket, mSecure);
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    Log.e(TAG, "Could not close unwanted socket", e);
                                }
                                break;
                        }
                    }
                } else {
                    if (mBluetoothListener != null) {
                        mBluetoothListener.onListenTimeout();
                    }
                }
            }
            Log.i(TAG, "END mAcceptThread, socket Type: " + getSocketType(mSecure));

            // Reset the AcceptThread because we're done
            synchronized (BluetoothControlerImpl.this) {
                if (mSecure) {
                    mSecureAcceptThread = null;
                } else {
                    mInsecureAcceptThread = null;
                }
            }
        }

        void cancel() {
            Log.d(TAG, "Socket Type" + getSocketType(mSecure) + "cancel " + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Socket Type" + getSocketType(mSecure) + "close() of server failed", e);
            }
        }
    }


    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private       boolean         mSecure;

        ConnectThread(BluetoothDevice device, boolean secure) {
            BluetoothSocket tmp = null;
            mSecure = secure;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                if (secure) {
                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
                } else {
                    tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID_INSECURE);
                }
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + getSocketType(mSecure) + "create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread SocketType:" + getSocketType(mSecure));
            setName("ConnectThread" + getSocketType(mSecure));

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " + getSocketType(mSecure) +
                               " socket during connection failure", e2);
                }
                connectionFailed();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothControlerImpl.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mSecure);
        }

        void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect " + getSocketType(mSecure) + " socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream     mmInStream;
        private final OutputStream    mmOutStream;

        ConnectedThread(BluetoothSocket socket, boolean secure) {
            Log.d(TAG, "create ConnectedThread: " + getSocketType(secure));
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (mState == STATE_CONNECTED) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);

                    // Send the obtained bytes to the UI Activity
                    if (mBluetoothListener != null) {
                        mBluetoothListener.onDataReceived(buffer, bytes);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    // Start the service over to restart listening mode
                    reset();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
                if (mBluetoothListener != null) {
                    mBluetoothListener.onDataSent(buffer);
                }
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
}
