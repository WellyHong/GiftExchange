package com.lukedeighton.wheelsample;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by wellyhong on 2016/12/3.
 */

public class GuestManager {
    private static final String TAG = GuestManager.class.toString();
    private ArrayList<AttendantData> mAttendantDataList = new ArrayList<AttendantData>();
    private Context mContext;
    private ArrayList<String> mAttendantStrings = new ArrayList<String>();
    private ArrayList<String> mRemainderGiftStrings = new ArrayList<String>();
    //    private ArrayList<Integer> mRemainderBingoList = new ArrayList<Integer>();
//    private ArrayList<String> mBingoNumberList = new ArrayList<String>();
    private SharedPreferences mPreference;


    private volatile static GuestManager sInstance;
//    private static String[] sIds;
    //    private static int[] sOthersIds;
//    private static String[] sNames;

//    private static int[] sQRDrawables = {
//            R.drawable.n044020001, R.drawable.n044020002, R.drawable.n044020003, R.drawable.n044020004, R.drawable.n044020005,
//            R.drawable.n044020006, R.drawable.n044020007, R.drawable.n044020008, R.drawable.n044020009, R.drawable.n044020010,
//            R.drawable.n044020011, R.drawable.n044020012, R.drawable.n044020013, R.drawable.n044020014, R.drawable.n044020015,
//            R.drawable.n044020016, R.drawable.n044020017, R.drawable.n044020018, R.drawable.n044020019, R.drawable.n044020020,
//            R.drawable.n044020021, R.drawable.n044020022, R.drawable.n044020023, R.drawable.n044020024, R.drawable.n044020025,
//            R.drawable.n044020026, R.drawable.n044020027, R.drawable.n044020028, R.drawable.n044020029, R.drawable.n044020030,
//            //105
//            R.drawable.n054020001, R.drawable.n054020002, R.drawable.n054020003, R.drawable.n054020004, R.drawable.n054020005,
//            R.drawable.n054020006, R.drawable.n054020007, R.drawable.n054020008, R.drawable.n054020009, R.drawable.n054020010,
//            R.drawable.n054020011, R.drawable.n054020012, R.drawable.n054020013, R.drawable.n054020014, R.drawable.n054020015,
//            R.drawable.n054020016, R.drawable.n054020017, R.drawable.n054020018, R.drawable.n054020019, R.drawable.n054020020,
//            R.drawable.n054020021, R.drawable.n054020022, R.drawable.n054020023, R.drawable.n054020024, R.drawable.n054020025,
//            R.drawable.n054020026, R.drawable.n054020027, R.drawable.n054020028, R.drawable.n054020029, R.drawable.n054020030,
//    };

    public static GuestManager getSingleton(Context c) {
        if (sInstance == null) {
            synchronized (GuestManager.class) {
                if (sInstance == null) {
                    Log.d(TAG, "GuestManager sInstance=null");
                    sInstance = new GuestManager(c);
                }
            }
        }
        return sInstance;
    }

    private GuestManager(Context c) {
        Log.d(TAG, "GuestManager construct");
        mContext = c;

        mPreference = c.getSharedPreferences(Constants.PREFERENCE_NAME_GIFT_EXCHANGE, 0);

//        if(Constants.sIsNeedDB) {
//            sIds = c.getResources().getStringArray(R.array.ids);
//            sNames = c.getResources().getStringArray(R.array.names);
//        }

    }

    public void DestroyInstance() {
        Log.d(TAG, "GuestManager DestroyInstance");
        sInstance = null;
        mContext = null;
        mAttendantDataList.clear();
        mAttendantDataList = null;
    }


    public void addAttendantByScanQRCode(String scanStr) {
//        String[] token = scanStr.split(";");
//        String id = "";
//        if (token.length <= 0 ) {
//            Log.w(TAG, "scan token length:" + token.length );
//            return;
//        } else {
//            id = token[0].replace("\\s", "").trim();
//            Log.d(TAG, "scanne id : " + id);
//        }

        Log.d(TAG, "scanne str : " + scanStr);

        for (String s : mAttendantStrings) {
            if (scanStr.contains(s)) {
                Log.w(TAG, "attendant list contain:" + s);
                return;
            }
        }

        mAttendantStrings.add(scanStr);
        updateAttendantPreference();

        mRemainderGiftStrings.add(scanStr);
        updateRemainderGiftPreference();

        Log.d(TAG, "attendantList add id:" + scanStr);
        AttendantData attendant = new AttendantData();
        attendant.mID = scanStr;
//        attendant.mName = token[1];
        attendant.mIsGiftExchanged = false;
        mAttendantDataList.add(attendant);

    }

//    public void addAttendantByScanQRCode(String scanStr) {
//
//        String[] token = scanStr.split(",");
//        String id = "";
//
//
//        if (token.length <= 0 || !token[0].toLowerCase().contains("n0")) {
//            Log.w(TAG, "scan token length:" + token.length + ", or does not contain n0");
//            return;
//        } else {
//            id = token[0].toLowerCase().replace("\\s", "").trim();
//            Log.d(TAG, "scanne id : " + id);
////            Toast.makeText(mContext, "scanner id:"+token[0], Toast.LENGTH_SHORT);
//        }
//
//        for (String s : mAttendantStrings) {
//            if (id.contains(s)) {
//                Log.w(TAG, "attendant list contain:" + s);
////                Toast.makeText(mContext, "id:"+s+" is existed.", Toast.LENGTH_SHORT);
//                return;
//            }
//        }
//
//
//        for (int i = 0; i < sIds.length; i++) {
//            if (id.contains(sIds[i])) {
//                mAttendantStrings.add(id);
//                updateAttendantPreference();
//
//                mRemainderGiftStrings.add(id);
//                updateRemainderGiftPreference();
//
//                Log.d(TAG, "attendantList add id:" + id);
//                AttendantData attendant = new AttendantData();
//                attendant.mID = sIds[i];
//                attendant.mName = sNames[i];
//                attendant.mIsGiftExchanged = false;
//                attendant.mDrawableId = sQRDrawables[i];
//                attendant.mDrawable = ResourcesCompat.getDrawable(mContext.getResources(), attendant.mDrawableId, mContext.getTheme());
//                mAttendantDataList.add(attendant);
//                return;
//            }
//        }
//
//    }

    public void removeAttendant(String id) {
        AttendantData remove = null;
        for (AttendantData d : mAttendantDataList) {
            if (id.equals(d.mID)) {
                remove = d;
                mAttendantDataList.remove(d);
                mAttendantStrings.remove(d.mID);
                updateAttendantPreference();
                mRemainderGiftStrings.remove(d.mID);
                updateRemainderGiftPreference();
            }
        }
        mAttendantDataList.remove(remove);
    }

    public void exchangeWithAttendant(AttendantData attendant) {
        if (mAttendantDataList.contains(attendant)) {
            String id = attendant.mID;
            attendant.mIsGiftExchanged = true;

            mRemainderGiftStrings.remove(id);
            String giftsStr = "";
            for (String gift : mRemainderGiftStrings) {
                giftsStr += (gift + ",");
            }
            Log.d(TAG, "after exchange "+id+", size:"+mRemainderGiftStrings.size()+", remainder list:"+giftsStr);
            mPreference.edit().putString(Constants.PREFERENCE_REMAINDER_GIFT_LIST, giftsStr).commit();
        }
    }

    public void clearAllData() {
        mPreference.edit()
                .putString(Constants.PREFERENCE_REMAINDER_GIFT_LIST, "")
                .putString(Constants.PREFERENCE_ATTENDANT_LIST, "")
                .commit();

        mAttendantStrings.clear();
        mAttendantDataList.clear();
        mRemainderGiftStrings.clear();
    }

    public void reloadData() {
        mAttendantStrings.clear();
        mAttendantDataList.clear();
        mRemainderGiftStrings.clear();

        String[] attendants = mPreference.getString(Constants.PREFERENCE_ATTENDANT_LIST, "").split("|");
        if (attendants.length > 0) {
            for (String id : attendants) {
                mAttendantStrings.add(id);
                updateAttendantPreference();

                Log.d(TAG, "AttendantData list add id:" + id);
                AttendantData attendant = new AttendantData();
                attendant.mID = id;
                attendant.mIsGiftExchanged = true;
                mAttendantDataList.add(attendant);
            }
        }

        String[] remainderGifts = mPreference.getString(Constants.PREFERENCE_REMAINDER_GIFT_LIST, "").split("|");
        if(remainderGifts.length>0){
            for(String gift:remainderGifts){
                mRemainderGiftStrings.add(gift);
                updateRemainderGiftPreference();
            }
        }

        for(AttendantData attendant:mAttendantDataList){
            for(String gift:mRemainderGiftStrings){
                if(attendant.mID.contains(gift)){
                    attendant.mIsGiftExchanged = false;
                }
            }
        }
    }

//    public void reloadData() {
//        mAttendantStrings.clear();
//        mAttendantDataList.clear();
//        mRemainderGiftStrings.clear();
//
//        String[] attendants = mPreference.getString(Constants.PREFERENCE_ATTENDANT_LIST, "").split("|");
//        if (attendants.length > 0) {
//            for (String id : attendants) {
//                for (int i = 0; i < sIds.length; i++) {
//                    if (id.toLowerCase().contains(sIds[i])) {
//                        mAttendantStrings.add(id);
//                        updateAttendantPreference();
//
//                        Log.d(TAG, "AttendantData list add id:" + id);
//                        AttendantData attendant = new AttendantData();
//                        attendant.mID = sIds[i];
//                        attendant.mName = sNames[i];
//                        attendant.mDrawableId = sQRDrawables[i];
//                        attendant.mDrawable = ResourcesCompat.getDrawable(mContext.getResources(), attendant.mDrawableId, mContext.getTheme());
//                        attendant.mIsGiftExchanged = true;
//                        mAttendantDataList.add(attendant);
//                    }
//                }
//
//            }
//        }
//
//        String[] remainderGifts = mPreference.getString(Constants.PREFERENCE_REMAINDER_GIFT_LIST, "").split("|");
//        if(remainderGifts.length>0){
//            for(String gift:remainderGifts){
//                mRemainderGiftStrings.add(gift);
//                updateRemainderGiftPreference();
//            }
//        }
//
//        for(AttendantData attendant:mAttendantDataList){
//            for(String gift:mRemainderGiftStrings){
//                if(attendant.mID.contains(gift)){
//                    attendant.mIsGiftExchanged = false;
//                }
//            }
//        }
//    }

//    public ArrayList<String> getRemainderList(){
//        return mRemainderGiftStrings;
//    }

    public AttendantData getAttendantById(String id) {
        for (AttendantData attendant : mAttendantDataList) {
            if (id.toLowerCase().contains(attendant.mID)) {
                return attendant;
            }
        }
        return null;
    }

    public void removeAttendant(AttendantData attendant) {
        if (mAttendantDataList.contains(attendant)) {
            mAttendantDataList.remove(attendant);
            mAttendantStrings.remove(attendant.mID);
            updateAttendantPreference();
            mRemainderGiftStrings.remove(attendant.mID);
            updateRemainderGiftPreference();
        }
    }

    private String updateAttendantPreference() {
        String attendantsStr = "";
        for (String attendant : mAttendantStrings) {
            attendantsStr += (attendant + "|");
        }
        mPreference.edit().putString(Constants.PREFERENCE_ATTENDANT_LIST, attendantsStr).commit();
        return attendantsStr;
    }

    private String updateRemainderGiftPreference() {
        String giftsStr = "";
        for (String gift : mRemainderGiftStrings) {
            giftsStr += (gift + "|");
        }
        mPreference.edit().putString(Constants.PREFERENCE_REMAINDER_GIFT_LIST, giftsStr).commit();
        return giftsStr;
    }


//    public void removeAttendant(int idex){
//        for(AttendantData d : mAttendantDataList){
//            if(id.equals(d.mID)){
//                mAttendantDataList.remove(d);
//            }
//        }
//    }

    public int getAttendantsSize() {
        return mAttendantStrings.size();
    }

    public int getRemainderGiftSize() {
        return mRemainderGiftStrings.size();
    }

    public String getRemainderId(int index){
        if(index<=(mRemainderGiftStrings.size()+1)){
            return mRemainderGiftStrings.get(index);
        }
        return "";
    }

    public ArrayList<AttendantData> getAttendantList(){
        return mAttendantDataList;
    }

//    public void removeBingoIndex(int index){
//        if(mRemainderBingoList.size()>index){
//            int number = mRemainderBingoList.remove(index);
//            mBingoNumberList.add(String.valueOf(number));
//            String bingoNumbers = mPreference.getString(Constants.PREFERENCE_BINGO_NUMBER, "");
//            bingoNumbers = bingoNumbers + "," + String.valueOf(number);
//
//            String remainderNumbers = "";
//            for(Integer i : mRemainderBingoList){
//                remainderNumbers = i + ",";
//            }
//            mPreference.edit()
//                    .putString(Constants.PREFERENCE_BINGO_NUMBER, bingoNumbers)
//                    .putString(Constants.PREFERENCE_REMENDER_BINGO_NUMBER, remainderNumbers)
//                    .commit();
//        }
//    }

//    public ArrayList<String> getBingoNumbers(){
//        return mBingoNumberList;
//    }
}
