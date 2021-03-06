package com.lukedeighton.wheelsample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CheckBox;

import java.util.ArrayList;

/**
 * Created by wellyhong on 2016/12/3.
 */

public class AttendantActivity extends Activity implements TextWatcher, View.OnClickListener, TextView.OnEditorActionListener {
//    private AttendantAdapter mAddapter;
    private static final String TAG = AttendantActivity.class.toString();
    private ListView mListView;
    private EditText mEditText;
    private TextView mTextViewCount;
    private Button mBtnAdd;
    private Button mBtnBack;
    AttendantAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendant);
        mBtnAdd = (Button)findViewById(R.id.btn_add);
        mBtnAdd.setOnClickListener(this);
        mBtnBack = (Button)findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);

        Log.d(TAG, "onCreate");

        mTextViewCount = (TextView)findViewById(R.id.textview_attendant_count);

        mListView = (ListView)findViewById(R.id.attendantListView);
        mEditText = (EditText)findViewById(R.id.editText2);
        mEditText.requestFocus();
        mEditText.addTextChangedListener(this);
        mEditText.setOnEditorActionListener(this);
        mAdapter = new AttendantAdapter(AttendantActivity.this,
                R.layout.attendant_list_row_view, GuestManager.getSingleton(getApplicationContext()).getAttendantList());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Log.d(TAG, "onItemClick:"+position);
                AlertDialog.Builder builder = new AlertDialog.Builder(AttendantActivity.this);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        GuestManager.getSingleton(AttendantActivity.this).getAttendantList().remove(position);
                        AttendantHolder holder = (AttendantHolder)view.getTag();
                        GuestManager.getSingleton(AttendantActivity.this.getApplicationContext()).removeAttendant(holder.attendant);
                        mAdapter.notifyDataSetChanged();
                        mTextViewCount.setText(String.valueOf(GuestManager.getSingleton(AttendantActivity.this.getApplicationContext()).getAttendantsSize()));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });


                final AlertDialog dialog = builder.create();
//                LayoutInflater inflater = getLayoutInflater();
                dialog.show();
            }
        });

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume");
        int size = GuestManager.getSingleton(getApplicationContext()).getAttendantsSize();
        mTextViewCount.setText(String.valueOf(size));
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_add){
            Log.d("AttendantActivity", "Press Add btn : "+mEditText.getText().toString());
            GuestManager.getSingleton(this).addAttendantByScanQRCode(mEditText.getText().toString(), this);
            mAdapter.notifyDataSetChanged();
            mListView.post(new Runnable() {
                @Override
                public void run() {
                    mListView.smoothScrollToPosition(0);
                }
            });
            mEditText.requestFocus();
            if(mTextViewCount!=null) {
                mEditText.setText("");
                mTextViewCount.setText(String.valueOf(GuestManager.getSingleton(this).getAttendantsSize()));
            }
        }else if(v.getId()==R.id.btn_back){
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        Log.d(TAG, "beforeTextChanged:"+s);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        Log.d(TAG, "onTextChanged:"+s);
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TAG, "afterTextChanged:"+s.toString());
        String newLine = System.getProperty("line.separator");
        if (s.toString().contains(newLine)) {
            Log.d(TAG, "afterTextChanged: str has new line.");
        }

        if(s.toString().isEmpty()){
            return;
        }

        if (s.toString().contains(newLine)) {
            GuestManager.getSingleton(this).addAttendantByScanQRCode(s.toString(), this);
            mAdapter.notifyDataSetChanged();
            mListView.post(new Runnable() {
                @Override
                public void run() {
                    mListView.smoothScrollToPosition(0);
                }
            });
            mEditText.requestFocus();
            if(mTextViewCount!=null) {
                mEditText.setText("");
                mTextViewCount.setText(String.valueOf(GuestManager.getSingleton(this).getAttendantsSize()));
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i== EditorInfo.IME_NULL){
            Log.d(TAG, "receive enter press");
            GuestManager.getSingleton(this).addAttendantByScanQRCode(mEditText.getText().toString(), this);
            mAdapter.notifyDataSetChanged();
            mListView.post(new Runnable() {
                @Override
                public void run() {
                    mListView.smoothScrollToPosition(0);
                }
            });
            mEditText.requestFocus();
            if(mTextViewCount!=null) {
                mEditText.setText("");
                mTextViewCount.setText(String.valueOf(GuestManager.getSingleton(getApplicationContext()).getAttendantsSize()));
            }
//            return true;
        }
        return false;
    }

    private class AttendantHolder{
        TextView txtView;
        CheckBox checkBox;
        AttendantData attendant;
    }

    private class AttendantAdapter extends ArrayAdapter<AttendantData>{

        Context context;
        int layoutResourceId;
        ArrayList<AttendantData> data;


        public AttendantAdapter(Context context, int layoutResourceId, ArrayList<AttendantData> data) {
            super(context, layoutResourceId,  data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            AttendantHolder holder = null;

            if(row == null)
            {
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);

                holder = new AttendantHolder();
//                holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
                holder.txtView = (TextView)row.findViewById(R.id.row_textView);
                holder.checkBox = (CheckBox)row.findViewById(R.id.checkBox);
                holder.checkBox.setClickable(false);
                row.setTag(holder);
            }
            else
            {
                holder = (AttendantHolder)row.getTag();
            }

            AttendantData attendant = this.data.get(position);
            holder.attendant = attendant;
//            holder.txtView.setText(attendant.mID+","+attendant.mName);
            holder.txtView.setText(attendant.mID);
            holder.checkBox.setChecked(attendant.mIsGiftExchanged);
//            holder.imgIcon.setImageResource(weather.icon);

            return row;
        }
    }


}
