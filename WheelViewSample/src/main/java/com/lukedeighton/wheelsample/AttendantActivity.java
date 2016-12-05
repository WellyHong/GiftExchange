package com.lukedeighton.wheelsample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wellyhong on 2016/12/3.
 */

public class AttendantActivity extends Activity implements TextWatcher, View.OnClickListener {
//    private AttendantAdapter mAddapter;
    private ListView mListView;
    private EditText mEditText;
    private TextView mTextViewCount;
    private Button mBtnAdd;
    AttendantAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendant);
        mBtnAdd = (Button)findViewById(R.id.btn_add);
        mBtnAdd.setOnClickListener(this);

        mTextViewCount = (TextView)findViewById(R.id.textview_attendant_count);

        mListView = (ListView)findViewById(R.id.attendantListView);
        mEditText = (EditText)findViewById(R.id.editText2);
        mEditText.addTextChangedListener(this);
        mAdapter = new AttendantAdapter(this,
                R.layout.attendant_list_row_view, GuestManager.getSingleton(this).getList());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AttendantActivity.this);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        GuestManager.getSingleton(AttendantActivity.this).getList().remove(position);
                        AttendantData attendant = GuestManager.getSingleton(AttendantActivity.this).getList().get(position);
                        GuestManager.getSingleton(AttendantActivity.this).removeAttendant(attendant);
                        mAdapter.notifyDataSetChanged();
                        mTextViewCount.setText(String.valueOf(GuestManager.getSingleton(AttendantActivity.this).getList().size()));
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
    public void onClick(View v) {
        if(v.getId()==R.id.btn_add){
            Log.d("AttendantActivity", "Press Add btn : "+mEditText.getText().toString());
            GuestManager.getSingleton(this).addAttendant(mEditText.getText().toString());
            mAdapter.notifyDataSetChanged();
            if(mTextViewCount!=null) {
                mEditText.setText("");
                mTextViewCount.setText(String.valueOf(GuestManager.getSingleton(this).getList().size()));
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private class AttendantHolder{
        TextView txtView;
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

                row.setTag(holder);
            }
            else
            {
                holder = (AttendantHolder)row.getTag();
            }

            AttendantData attendant = this.data.get(position);
            holder.txtView.setText(attendant.mID+","+attendant.mName);
//            holder.imgIcon.setImageResource(weather.icon);

            return row;
        }
    }


}
