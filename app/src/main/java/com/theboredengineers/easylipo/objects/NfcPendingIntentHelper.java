package com.theboredengineers.easylipo.objects;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.util.Log;

/**
 * Created by Alex on 19/01/2015.
 */
public class NfcPendingIntentHelper {
    public String[][] techListsArray;
    public IntentFilter[] intentFiltersArray;
    public NfcAdapter adapter;
    private PendingIntent pendingIntent;
    private Activity activity;
    public boolean isListening = false;

    public NfcPendingIntentHelper(Activity activity) {
        this.activity = activity;
        pendingIntent = PendingIntent.getActivity(
                activity, 0, new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");    /* Handles all MIME based dispatches.
                                       You should specify only the ones that you need. */
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        intentFiltersArray = new IntentFilter[]{ndef,};
        techListsArray = new String[][]{new String[]{NfcA.class.getName()}};
        adapter = NfcAdapter.getDefaultAdapter(activity.getApplicationContext());
    }

    public void disableForegroundDispatch() {
        if (adapter != null)
            adapter.disableForegroundDispatch(activity);
        else
            Log.e("Nfc", "Devices seems not to support NFC.");


    }

    public void enableForegroundDispatch() {
        if (adapter != null)
            adapter.enableForegroundDispatch(activity, pendingIntent, intentFiltersArray, techListsArray);
        else
            Log.e("Nfc", "Devices seems not to support NFC.");
    }
}
