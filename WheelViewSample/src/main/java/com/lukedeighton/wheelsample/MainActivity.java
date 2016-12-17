package com.lukedeighton.wheelsample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lukedeighton.wheelview.WheelView;
import com.lukedeighton.wheelview.adapter.WheelArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends ActionBarActivity implements ImageView.OnTouchListener, View.OnClickListener {
    private static final String TAG = MainActivity.class.toString();
    private static final int ITEM_COUNT = 45;
    private static final int[] sGiftDrawables = {
            R.drawable.gift1, R.drawable.gift2, R.drawable.gift3, R.drawable.gift4, R.drawable.gift5,
            R.drawable.gift6, R.drawable.gift7, R.drawable.gift8, R.drawable.gift9, R.drawable.gift10,
            R.drawable.gift11, R.drawable.gift12, R.drawable.gift13, R.drawable.gift14, R.drawable.gift15,
            R.drawable.gift16, R.drawable.gift17,};


    List<Pair<String, Pair<Drawable, Integer>>> mDrawableEntries = new ArrayList<Pair<String, Pair<Drawable, Integer>>>(ITEM_COUNT);
    private ImageView mImageView = null;
    private Button mBtnRegister = null;
    private Button mBtnBingo = null;
    private AttendantData mSelectedGift;
    private TextView mTextViewGiftCount;
    private TextView mTextViewPreExchange;
    private TextView mTextViewPostExchange;
    private String mStrPreExchanger = "XXXXX";
    private String mStrPostExchanger = "XXXXXX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Log.d(TAG, "onCreate");

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mTextViewGiftCount = (TextView) findViewById(R.id.textview_gift_count);
        mTextViewPreExchange = (TextView) findViewById(R.id.text_preExChange);
        mTextViewPostExchange = (TextView) findViewById(R.id.text_postExchange);

        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setOnTouchListener(this);
        mImageView.setVisibility(View.INVISIBLE);
        mBtnRegister = (Button) findViewById(R.id.registerBtn);
        mBtnRegister.setOnClickListener(this);
        mBtnBingo = (Button) findViewById(R.id.btn_bingo);
        mBtnBingo.setOnClickListener(this);

//        Drawable d1 = ResourcesCompat.getDrawable(getResources(), R.drawable.gift2, getTheme());
////        mImageView.setBackgroundResource(R.drawable.gift1);
//        mImageView.setImageDrawable(d1);

        final WheelView wheelView = (WheelView) findViewById(R.id.wheelview);
        if (wheelView == null) {
            return;
        }
//
        //create data for the adapter
        List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(ITEM_COUNT);
        for (int i = 0; i < ITEM_COUNT; i++) {
            Map.Entry<String, Integer> entry = MaterialColor.random(this, "\\D*_500$");
            entries.add(entry);
            Drawable d = ResourcesCompat.getDrawable(getResources(), sGiftDrawables[i % 17], getTheme());
            Pair<Drawable, Integer> p = new Pair(d, entry.getValue());
            Pair<String, Pair<Drawable, Integer>> entry2 = new Pair<String, Pair<Drawable, Integer>>(entry.getKey(), p);
            mDrawableEntries.add(entry2);
        }

        //populate the adapter, that knows how to draw each item (as you would do with a ListAdapter)
//        MaterialColorAdapter wheelAdapter = new MaterialColorAdapter(entries);
        DrawableAdapter wheelAdapter = new DrawableAdapter(mDrawableEntries);
        wheelView.setAdapter(wheelAdapter);

        //a listener for receiving a callback for when the item closest to the selection angle changes
        wheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectListener() {
            @Override
            public void onWheelItemSelected(WheelView parent, Drawable itemDrawable, int position) {
                //get the item at this position
//                Map.Entry<String, Integer> selectedEntry = ((MaterialColorAdapter) parent.getAdapter()).getItem(position);
                Pair<String, Pair<Drawable, Integer>> selectedEntry = ((DrawableAdapter) parent.getAdapter()).getItem(position);
                parent.setSelectionColor(getContrastColor(selectedEntry));
                mImageView.setVisibility(View.VISIBLE);
                mImageView.setBackgroundResource(sGiftDrawables[position % 17]);
            }
        });

        wheelView.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
            @Override
            public void onWheelItemClick(WheelView parent, int position, boolean isSelected) {
                String msg = String.valueOf(position) + " " + isSelected;
//                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });


        //initialise the selection drawable with the first contrast color
        wheelView.setSelectionColor(getContrastColor(entries.get(0)));

        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //wheelView.setSelectionAngle(-wheelView.getAngleForPosition(5));
                wheelView.setMidSelected();
            }
        }, 3000); */
    }

    //get the materials darker contrast
    private int getContrastColor(Map.Entry<String, Integer> entry) {
        String colorName = MaterialColor.getColorName(entry);
        return MaterialColor.getContrastColor(colorName);
    }

    private int getContrastColor(Pair<String, Pair<Drawable, Integer>> entry) {
        String colorName = MaterialColor.getColorName(entry);
        return MaterialColor.getContrastColor(colorName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_clear_all) {
            Log.d(TAG, "menu clear all pressed");
            new AlertDialog.Builder(this)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "confirm clear all");
                            GuestManager.getSingleton(getApplicationContext()).clearAllData();
                            mTextViewGiftCount.setText(String.valueOf(GuestManager.getSingleton(getApplicationContext()).getRemainderGiftSize()));
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            })
                    .setMessage(MainActivity.this.getResources().getString(R.string.text_confirm_clear))
                    .create()
                    .show();


            return true;
        } else if (id == R.id.action_reload) {

            Log.d(TAG, "menu reload pressed");
            new AlertDialog.Builder(this)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "confirm reload");
                            GuestManager.getSingleton(getApplicationContext()).reloadData();
                            mTextViewGiftCount.setText(String.valueOf(GuestManager.getSingleton(getApplicationContext()).getRemainderGiftSize()));
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            })
                    .setMessage(MainActivity.this.getResources().getString(R.string.text_confirm_reload))
                    .create()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.imageView) {
            if (GuestManager.getSingleton(getApplicationContext()).getRemainderGiftSize() <= 0) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setMessage(Html.fromHtml("<Big><Big>" + MainActivity.this.getResources().getString(R.string.text_no_gift) + "</Big></Big>"))
                        .create();
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(28);
                return false;
            }

            mImageView.setVisibility(View.INVISIBLE);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String confirm = getResources().getString(R.string.btn_confirm_select);
            String cancel = getResources().getString(R.string.btn_cancel_select);
            builder
                    .setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "confirm gift click");
                            GuestManager.getSingleton(MainActivity.this.getApplicationContext()).exchangeWithAttendant(mSelectedGift);
                            int size = GuestManager.getSingleton(MainActivity.this.getApplicationContext()).getRemainderGiftSize();
                            mTextViewGiftCount.setText(String.valueOf(size));
                            mTextViewPreExchange.setText(mStrPreExchanger);
                            mTextViewPostExchange.setText(mStrPostExchanger);
                        }
                    })
//                    .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    })
            ;


            final AlertDialog dialog = builder.create();
            LayoutInflater inflater = getLayoutInflater();
            View dialogLayout = inflater.inflate(R.layout.alert_dialog_layout, null);

            Random r = new Random();
            int size = GuestManager.getSingleton(MainActivity.this.getApplicationContext()).getRemainderGiftSize();
            int index = size > 1 ? r.nextInt(size) : 0;
            String id = GuestManager.getSingleton(MainActivity.this.getApplicationContext()).getRemainderId(index);

            mStrPreExchanger = mSelectedGift != null ? mSelectedGift.mID : mStrPreExchanger;
            mSelectedGift = GuestManager.getSingleton(MainActivity.this.getApplicationContext()).getAttendantById(id);
            mStrPostExchanger = mSelectedGift != null ? mSelectedGift.mID : mStrPostExchanger;

            String[] separator = mStrPreExchanger.split(Constants.QR_SEPARATOR);
            mStrPreExchanger = separator.length >= 2 ? separator[1] : mStrPreExchanger;
            separator = mStrPostExchanger.split(Constants.QR_SEPARATOR);
            mStrPostExchanger = separator.length >= 2 ? separator[1] : mStrPostExchanger;
//            ImageView image = (ImageView) dialogLayout.findViewById(R.id.dialog_image_qr);
//            image.setImageResource(mSelectedGift!=null ? mSelectedGift.mDrawableId : R.drawable.gift1);

//            EditText edit = (EditText)dialogLayout.findViewById(R.id.editText_gift);
//            edit.setInputType(InputType.TYPE_NULL);

            TextView textSelected = (TextView) dialogLayout.findViewById(R.id.textView_exchange_person);
            textSelected.setText(mSelectedGift.mID);
            Log.d(TAG, "index:" + index + ",id:" + mSelectedGift.mID);

            dialog.setView(dialogLayout);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.show();
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(28);

            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(28);

//            View view = this.getCurrentFocus();
//            if (dialogLayout != null) {
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(dialogLayout.getWindowToken(), 0);
//            }

//            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                @Override
//                public void onShow(DialogInterface d) {
//                    ImageView image = (ImageView) dialog.findViewById(R.id.dialog_image_qr);
//                    Random r = new Random();
//
//                    int size = GuestManager.getSingleton(MainActivity.this).getAttendantList().size();
//                    int index = size>1 ? r.nextInt(size) : 0;
//                    mSelectedGift = GuestManager.getSingleton(MainActivity.this).getAttendantList().get(index);
//                    Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
//                            mSelectedGift.mDrawableId);
//                    float imageWidthInPX = (float)image.getWidth();
//                    Log.d(TAG, "index:"+index+",id:"+mSelectedGift.mID+",name:"+mSelectedGift.mName);
//                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
//                            Math.round(imageWidthInPX * (float)icon.getHeight() / (float)icon.getWidth()));
//                    image.setLayoutParams(layoutParams);
//                }
//            });
        }
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        int size = GuestManager.getSingleton(getApplicationContext()).getRemainderGiftSize();
        mTextViewGiftCount.setText(String.valueOf(size));
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerBtn) {
            Intent intent = new Intent(this, AttendantActivity.class);
            startActivity(intent);

        }
//        else if(v.getId()==R.id.btn_bingo){
//            if(GuestManager.getSingleton(getApplicationContext()).getBingoNumbers().isEmpty()){
//                new AlertDialog.Builder(this)
//                        .setPositiveButton("ok", new  DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        } )
//                        .setTitle("Dialog")
//                        .setMessage("No Bingo Numbers")
//                        .create()
//                        .show();
//                return;
//            }
//
//            int size = GuestManager.getSingleton(getApplicationContext()).getBingoNumbers().size();
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Log.d(TAG, "bing ok click");
//                }
//            });
//
//            final AlertDialog dialog = builder.create();
//            LayoutInflater inflater = getLayoutInflater();
//            View dialogLayout = inflater.inflate(R.layout.alert_dialog_bingo_layout, null);
//            ImageView image = (ImageView) dialogLayout.findViewById(R.id.dialog_image_qr);
//        }
    }

    static class MaterialColorAdapter extends WheelArrayAdapter<Map.Entry<String, Integer>> {
        MaterialColorAdapter(List<Map.Entry<String, Integer>> entries) {
            super(entries);
        }

        @Override
        public Drawable getDrawable(int position) {
            Drawable[] drawable = new Drawable[]{
                    createOvalDrawable(getItem(position).getValue()),
                    new TextDrawable(String.valueOf(position))
            };
            return new LayerDrawable(drawable);
        }

        private Drawable createOvalDrawable(int color) {
            ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
            shapeDrawable.getPaint().setColor(color);
            return shapeDrawable;
        }
    }

    static class DrawableAdapter extends WheelArrayAdapter<Pair<String, Pair<Drawable, Integer>>> {
        DrawableAdapter(List<Pair<String, Pair<Drawable, Integer>>> entries) {
            super(entries);
        }

        @Override
        public Drawable getDrawable(int position) {
            Drawable[] drawable = new Drawable[]{
                    createOvalDrawable(getItem(position).second.second),
                    getItem(position).second.first,
//                    new TextDrawable(String.valueOf(position))
            };
            return new LayerDrawable(drawable);
        }

        private Drawable createOvalDrawable(int color) {
            ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
            shapeDrawable.getPaint().setColor(color);
            return shapeDrawable;
        }
    }
}