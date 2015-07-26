package com.theboredengineers.easylipo.objects;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Created by Benoit on 08/01/2015.
 */
public class NfcTag implements Serializable {

    byte[] bytes;
    String string;
    private static final String mimeType = "com.theboredengineers/easylipo";

    public NfcTag(byte[] bytes) {
        this.bytes = bytes;
        this.string = bytesToHex(bytes);
    }

    public NfcTag(Tag tag) {
        this(tag.getId());
    }

    public NfcTag(String str) {
        this.string = str;
        this.bytes = hexToBytes(str);
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public boolean equals(Object ob) {
        if (ob instanceof NfcTag) {
            NfcTag oth = (NfcTag) ob;
            return oth.string.equals(this.string);
        } else if (ob instanceof String) {
            if (this.string.equals(((String) ob).toUpperCase(Locale.US)))
                return true;
            else if (this.toFormattedString().equals(((String) ob).toUpperCase(Locale.US)))
                return true;
            else
                return false;
        } else if (ob instanceof byte[]) {
            byte[] otherBytes = (byte[]) ob;
            NfcTag otherTag = NfcTag.BuildFromBytes(otherBytes);
            return this.equals(otherTag);
        } else
            return false;
    }

    public String toFormattedString() {
        // add enough space for an additional ":" for every 2 chars:
        char[] chars = new char[string.length() + (string.length() / 2) - 1];

        // this offset will give us the first ":" position from the LEFT:
        int offset = string.length() % 2;
        int idx = 0, strIdx = 0;

        for (; strIdx < string.length(); idx++, strIdx++) {
            if (((strIdx % 2) == offset) && (strIdx != 0))
                chars[idx++] = ':';
            chars[idx] = string.charAt(strIdx);
        }

        String str2 = new String(chars);
        return str2;
    }

    public byte[] toBytes() {
        return bytes;
    }

    public static NfcTag BuildFromBytes(byte[] bTag) {
        return new NfcTag(bTag);
    }

    public static NfcTag BuildFromTag(Tag tag) {
        return new NfcTag(tag);
    }

    public static NfcTag BuildFromString(String strTag) {
        if(strTag == null)
            return null;
        if (strTag.length() % 2 == 1)
            strTag = "0" + strTag;
        return new NfcTag(strTag);
    }


    /**
     * Convert hex string to an array of bytes
     *
     * @param s string containing 0..F, and an EVEN number (2 used for one byte).
     * @return the array of bytes
     */
    private byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * Create string from byte array
     *
     * @param bytes the byte array
     * @return the string containing an even number of 0..F.
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @SuppressLint("NewApi")
    /**
     * @brief Formats an NFC Tag with our mime type
     * @param tag the NFC tag
     */
    public  static void formatNdef(Ndef tag,String server_id, Boolean force) {
        NdefMessage ndefMessage;
        NdefRecord ndefRecord;
        boolean alreadyWellFormatted = false;

        //Insert mime theboredengineers/easylipo.
        byte[] mimeData = server_id.getBytes();

        ndefMessage = tag.getCachedNdefMessage();

        if (ndefMessage != null && ndefMessage.getRecords().length == 1) {
            byte TypeAsByte[];
            ndefRecord = ndefMessage.getRecords()[0];
            TypeAsByte = ndefRecord.getType();
            String type;
            type = new String(TypeAsByte);
            if (type.equals(mimeType))
                alreadyWellFormatted = true;
        }

        if (tag.isWritable() && (force ||!alreadyWellFormatted)) {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ndefRecord = NdefRecord.createMime(mimeType, mimeData);
            }
            else
            {
                ndefRecord = new NdefRecord(
                        NdefRecord.TNF_MIME_MEDIA ,mimeType.getBytes(Charset.forName("US-ASCII")),
                        new byte[0]
                        , mimeData);
            }
            ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});

            try {
                tag.connect();
                tag.writeNdefMessage(ndefMessage);
                tag.close();
                Log.i("NFC", "tag formatted as " + mimeType + ":" + server_id);
            } catch (IOException | FormatException e) {
                Log.i("NFC", "Can't format... Unexpected");
            }
        } else {
            if (alreadyWellFormatted)
                Log.i("NFC", "Tag is already formatted");
            else
                Log.i("NFC", "Tag is not editable.");
        }

    }

    public static String getServerIDFromIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs != null) {
            NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                msgs[i] = (NdefMessage) rawMsgs[i];
            }
            if(msgs.length != 0) {
                if (msgs[0] != null)
                    return new String(msgs[0].getRecords()[0].getPayload());
                else return "";
            }
            else
                return "";
        }
        else
            return "";
    }
}
