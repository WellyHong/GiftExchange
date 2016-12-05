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
    private static String[] s104Ids;
    private static String[] s105Ids;
    private static int[] sOthersIds;
    private static String[] s104Names;
    private static String[] s105Names;
    private static int[] s104QRDrawables = {
            R.drawable.n044020001,R.drawable.n044020002,R.drawable.n044020003,R.drawable.n044020004,R.drawable.n044020005,
            R.drawable.n044020006,R.drawable.n044020007,R.drawable.n044020008,R.drawable.n044020009,R.drawable.n044020010,
            R.drawable.n044020011,R.drawable.n044020012,R.drawable.n044020013,R.drawable.n044020014,R.drawable.n044020015,
            R.drawable.n044020016,R.drawable.n044020017,R.drawable.n044020018,R.drawable.n044020019,R.drawable.n044020020,
            R.drawable.n044020021,R.drawable.n044020022,R.drawable.n044020023,R.drawable.n044020024,R.drawable.n044020025,
            R.drawable.n044020026,R.drawable.n044020027,R.drawable.n044020028,R.drawable.n044020029,R.drawable.n044020030,
    };

    private static int[] s105QRDrawables = {
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
                    sInstance = new GuestManager(c);
                }
            }
        }
        return sInstance;
    }

    private GuestManager(Context c){

        mContext = c;

        s104Ids = c.getResources().getStringArray(R.array.ids_104);
        s105Ids = c.getResources().getStringArray(R.array.ids_105);
        s104Names = c.getResources().getStringArray(R.array.names_104);
        s105Names = c.getResources().getStringArray(R.array.names_105);
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
        sInstance = null;
        mContext = null;
        mAttendantList.clear();
        mAttendantList = null;
    }

    public void addAttendant(AttendantData person){
        Log.d(TAG, "manager add AttendantData:"+person.mID);
        mAttendantList.add(person);
    }

    public void addAttendant(String id){

        String[] token = id.split(";");

        if(token.length<=0 || !token[0].toLowerCase().contains("n0")){
            return;
        }else{
            Log.d(TAG, "scanne id is"+token[0]);
            Toast.makeText(mContext, "scanner id:"+token[0], Toast.LENGTH_SHORT);
        }

        for(String s:mAttendantStrings){
            if(token[0].toLowerCase().contains(s)){
                Log.d(TAG, "attendant list contain:"+s);
                Toast.makeText(mContext, "id:"+s+" is existed.", Toast.LENGTH_SHORT);
                return;
            }
        }

        mAttendantStrings.add(token[0].toLowerCase());
//        if(mAttendantStrings.contains(id)){
//            Log.w(TAG, id+" has existed.");
//            return;
//        }else{
//            mAttendantStrings.add(id);
//        }

        for(int i=0; i<s104Ids.length; i++) {
                if (token[0].toLowerCase().contains(s104Ids[i])) {
                    Log.d(TAG, "manager add 104 id:" + token[0]);
                    AttendantData attendant = new AttendantData();
                    attendant.mID = s104Ids[i];
                    attendant.mName = s104Names[i];
                    attendant.mDrawableId = s104QRDrawables[i];
                    attendant.mDrawable = ResourcesCompat.getDrawable(mContext.getResources(), attendant.mDrawableId, mContext.getTheme());
                    mAttendantList.add(attendant);
                    return;
                }
        }

        for(int i=0; i<s105Ids.length; i++) {
                if (token[0].toLowerCase().contains(s105Ids[i])) {
                    Log.d(TAG, "manager add 105 id:" + token[0]);
                    AttendantData attendant = new AttendantData();
                    attendant.mID = s105Ids[i];
                    attendant.mName = s105Names[i];
                    attendant.mDrawableId = s105QRDrawables[i];
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

    public ArrayList<AttendantData> getList(){
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
