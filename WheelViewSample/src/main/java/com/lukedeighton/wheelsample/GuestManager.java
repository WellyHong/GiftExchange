package com.lukedeighton.wheelsample;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by wellyhong on 2016/12/3.
 */

public class GuestManager {
    private static final String TAG = GuestManager.class.toString();
    private ArrayList<AttendantData> mAttendantList = new ArrayList<AttendantData>();
    private Context mContext;
    private ArrayList<String> mAttendantStrings = new ArrayList<String>();
    private ArrayList<Integer> mRemainderBingoList = new ArrayList<Integer>();
    private ArrayList<String> mBingoNumberList = new ArrayList<String>();
    private SharedPreferences mPreference;


    private volatile static GuestManager sInstance;
    private static String[] sIds;
    private static int[] sOthersIds;
    private static String[] sNames;
    private static int[] sQRDrawables = {
            R.drawable.n044020001,R.drawable.n044020002,R.drawable.n044020003,R.drawable.n044020004,R.drawable.n044020005,
            R.drawable.n044020006,R.drawable.n044020007,R.drawable.n044020008,R.drawable.n044020009,R.drawable.n044020010,
            R.drawable.n044020011,R.drawable.n044020012,R.drawable.n044020013,R.drawable.n044020014,R.drawable.n044020015,
            R.drawable.n044020016,R.drawable.n044020017,R.drawable.n044020018,R.drawable.n044020019,R.drawable.n044020020,
            R.drawable.n044020021,R.drawable.n044020022,R.drawable.n044020023,R.drawable.n044020024,R.drawable.n044020025,
            R.drawable.n044020026,R.drawable.n044020027,R.drawable.n044020028,R.drawable.n044020029,R.drawable.n044020030,
            //105
            R.drawable.n044020001,R.drawable.n044020002,R.drawable.n044020003,R.drawable.n044020004,R.drawable.n044020005,
            R.drawable.n044020006,R.drawable.n044020007,R.drawable.n044020008,R.drawable.n044020009,R.drawable.n044020010,
            R.drawable.n044020011,R.drawable.n044020012,R.drawable.n044020013,R.drawable.n044020014,R.drawable.n044020015,
            R.drawable.n044020016,R.drawable.n044020017,R.drawable.n044020018,R.drawable.n044020019,R.drawable.n044020020,
            R.drawable.n044020021,R.drawable.n044020022,R.drawable.n044020023,R.drawable.n044020024,R.drawable.n044020025,
            R.drawable.n044020026,R.drawable.n044020027,R.drawable.n044020028,R.drawable.n044020029,R.drawable.n044020030,
    };

    public static GuestManager getSingleton(Context c) {
        if(sInstance == null) {
            synchronized (GuestManager.class) {
                if(sInstance == null) {
                    Log.d(TAG, "GuestManager sInstance=null");
                    sInstance = new GuestManager(c);
                }
            }
        }
        return sInstance;
    }

    private GuestManager(Context c){
        Log.d(TAG, "GuestManager construct");
        mContext = c;

        sIds = c.getResources().getStringArray(R.array.ids);
        sNames = c.getResources().getStringArray(R.array.names);
        mPreference = c.getSharedPreferences(Constants.PREFERENCE_GIFT_EXCHANGE, 0);

        if(mRemainderBingoList.size()<=0) {
            for (int i = 0; i < 75; i++) {
                mRemainderBingoList.add(i + 1);
            }
        }
//        AttendantData p = new AttendantData();
//        p.mID = "n044020030";
//        mAttendantList.add(p);

    }

    public void DestroyInstance(){
        Log.d(TAG, "GuestManager DestroyInstance");
        sInstance = null;
        mContext = null;
        mAttendantList.clear();
        mAttendantList = null;
    }

    public void addAttendant(AttendantData person){
        Log.d(TAG, "manager add AttendantData:"+person.mID);
        mAttendantList.add(person);
    }

    public void addAttendant(String scanStr){

        String[] token = scanStr.split(";");
        String id = "";

        if(token.length<=0 || !token[0].toLowerCase().contains("n0")){
            Log.w(TAG, "scan token length:"+ token.length+", or does not contain n0");
            return;
        }else{
            id = token[0].toLowerCase().replace(" ", "").trim();
            Log.d(TAG, "scanne id : "+id);
//            Toast.makeText(mContext, "scanner id:"+token[0], Toast.LENGTH_SHORT);
        }

        for(String s:mAttendantStrings){
            if(id.contains(s)){
                Log.w(TAG, "attendant list contain:"+s);
//                Toast.makeText(mContext, "id:"+s+" is existed.", Toast.LENGTH_SHORT);
                return;
            }
        }

        mAttendantStrings.add(id);
        for(String s:mAttendantStrings){
            Log.d(TAG, "added str:"+s);
        }
//        if(mAttendantStrings.contains(id)){
//            Log.w(TAG, id+" has existed.");
//            return;
//        }else{
//            mAttendantStrings.add(id);
//        }

        for(int i=0; i<sIds.length; i++) {
                if (id.contains(sIds[i])) {
                    Log.d(TAG, "attendantList add id:" + id);
                    AttendantData attendant = new AttendantData();
                    attendant.mID = sIds[i];
                    attendant.mName = sNames[i];
                    attendant.mDrawableId = sQRDrawables[i];
                    attendant.mDrawable = ResourcesCompat.getDrawable(mContext.getResources(), attendant.mDrawableId, mContext.getTheme());
                    mAttendantList.add(attendant);
                    return;
                }
        }

    }

    public void removeAttendant(String id){
        for(AttendantData d : mAttendantList){
            if(id.equals(d.mID)){
                mAttendantList.remove(d);
                for(String s:mAttendantStrings){
                    if(s.contains(id)){
                        mAttendantStrings.remove(s);
                    }
                }
            }
        }
    }

    public void removeAttendant(AttendantData attendant){
        if(mAttendantList.contains(attendant)){

            mAttendantList.remove(attendant);
            String id = "";
            for(String s : mAttendantStrings){
                if(s.contains(attendant.mID)){
                    id = s;
                }
            }
            if(!id.isEmpty()){
                mAttendantStrings.remove(id);
            }
        }
    }

//    public void removeAttendant(int idex){
//        for(AttendantData d : mAttendantList){
//            if(id.equals(d.mID)){
//                mAttendantList.remove(d);
//            }
//        }
//    }

    public ArrayList<AttendantData> getAttendantList(){
        return mAttendantList;
    }

    public void removeBingoIndex(int index){
        if(mRemainderBingoList.size()>index){
            int number = mRemainderBingoList.remove(index);
            mBingoNumberList.add(String.valueOf(number));
            String bingoNumbers = mPreference.getString(Constants.PREFERENCE_BINGO_NUMBER, "");
            bingoNumbers = bingoNumbers + "," + String.valueOf(number);

            String remainderNumbers = "";
            for(Integer i : mRemainderBingoList){
                remainderNumbers = i + ",";
            }
            mPreference.edit()
                    .putString(Constants.PREFERENCE_BINGO_NUMBER, bingoNumbers)
                    .putString(Constants.PREFERENCE_REMENDER_BINGO_NUMBER, remainderNumbers)
                    .commit();
        }
    }

    public ArrayList<String> getBingoNumbers(){
        return mBingoNumberList;
    }
}
