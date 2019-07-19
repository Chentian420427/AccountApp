package com.example.accountapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private MainViewPagerAdapter pagerAdapter;
    private TickerView amountText;
    private TickerView incomeAmountText;
    private TextView dateText;

    private int currentPagerPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        GlobalUtil.getInstance().setContext(getApplicationContext());
        GlobalUtil.getInstance().mainActivity = this;



        RecordBean bean = new RecordBean();

        getSupportActionBar().setElevation(0);

        amountText = findViewById(R.id.amount_text);
        amountText.setCharacterLists(TickerUtils.provideNumberList());
        incomeAmountText = findViewById(R.id.amount_income_text);
        incomeAmountText.setCharacterLists(TickerUtils.provideNumberList());

        dateText = findViewById(R.id.date_text);

        viewPager = findViewById(R.id.view_pager);

        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(this);

        //每一次进入app的时候，展示最近的日期的记录
        viewPager.setCurrentItem(pagerAdapter.getLastIndex());


        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddRecordActivity.class);
                startActivityForResult(intent,1);
            }
        });





    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.exchange:
                Intent intent = new Intent(MainActivity.this,MenuActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 界面关闭的时候
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pagerAdapter.reload();
        updateHeader();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    /**
     * 有一个page被滑动并且完成后调用此方法
     * @param i
     */
    @Override
    public void onPageSelected(int i) {

        currentPagerPosition = i;
        updateHeader();

    }

    public void updateHeader(){
        String amount = String.valueOf(pagerAdapter.getTotal(currentPagerPosition));
        String incomeAmount = String.valueOf(pagerAdapter.getIncomeTotal(currentPagerPosition));
        amountText.setText(amount);
        incomeAmountText.setText(incomeAmount);

        String date = pagerAdapter.getDateStr(currentPagerPosition);
        dateText.setText(DateUtil.getWeekDay(date));
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
