package com.theboredengineers.easylipo.ui.activities;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.theboredengineers.easylipo.R;
import com.theboredengineers.easylipo.objects.NfcTag;

/**
 * Created by Alex on 26/07/2015.
 */
public class NfcActivity extends SecuredActivity {

    private String[][] techListsArray;
    private boolean isTagInProcess = false;
    private IntentFilter[] intentFiltersArray;
    private NfcAdapter mAdapter;
    private PendingIntent pendingIntent;
    protected AlertDialog dlgAlertWaitForTag = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");    /* Handles all MIME based dispatches.
                                       You should specify only the ones that you need. */
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        intentFiltersArray = new IntentFilter[]{ndef,};
        techListsArray = new String[][]{new String[]{NfcA.class.getName()}};
        mAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
    }

    public void waitForNFC()
    {
        if (dlgAlertWaitForTag == null) {
            AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(this, R.style.AppTheme_Dark_Dialog);
            dlgBuilder.setNegativeButton(R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            isTagInProcess = false;
                            dlgAlertWaitForTag = null;
                        }
                    });
            dlgBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    //enable again.
                    isTagInProcess = false;
                    dlgAlertWaitForTag = null;
                }
            });

            dlgBuilder.setTitle(R.string.waiting_for_tag);

            dlgBuilder.setCancelable(true);
            dlgAlertWaitForTag = dlgBuilder.create();

            dlgAlertWaitForTag.show();
            isTagInProcess = true;
        } else
            isTagInProcess = false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (!isTagInProcess)
            return;
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        final Ndef tag = Ndef.get(tagFromIntent);

        if (tagFromIntent == null || tag == null)
            return;

        Log.i("NFC", "new NFC tag scanned : " + NfcTag.BuildFromBytes(tagFromIntent.getId()).toFormattedString());


        boolean dismissDialog = true;
        if (tag.isWritable()) {

            dismissDialog = onNfcTagScanned(tagFromIntent, tag);

        } else {
            Toast.makeText(this,"Non writable TAG unsupported",Toast.LENGTH_LONG).show();
        }
        Log.i("NFC", "affect");

        isTagInProcess = false;
        if ((dlgAlertWaitForTag != null) && dismissDialog)
            dlgAlertWaitForTag.dismiss();
        dlgAlertWaitForTag = null;
    }

    protected boolean onNfcTagScanned(Tag tag, Ndef ndef)
    {


        return false;
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(mAdapter != null)
            mAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAdapter != null)
            mAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
    }
}
