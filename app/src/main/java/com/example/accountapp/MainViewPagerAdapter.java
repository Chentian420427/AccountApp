package com.example.accountapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.LinkedList;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    //储存所有的 Fragment页面
    LinkedList<MainFragment> fragments = new LinkedList<>();
    //日期列表
    LinkedList<String> dates = new LinkedList<>();


    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        initFragments();
    }

    /**
     * 初始化 记录页面
     */
    private void initFragments(){

//        dates.add("2019-06-01");
//        dates.add("2019-06-02");
//        dates.add("2019-06-15");

       //Log.d("MainViewPagerAdapter",GlobalUtil.getInstance().databaseHelper.getAvaliableDate().toString());
        dates = GlobalUtil.getInstance().databaseHelper.getAvaliableDate();

        //如果不包含今天的日期
        if (!dates.contains(DateUtil.getFormattedDate())){
            dates.addLast(DateUtil.getFormattedDate());
        }

        for (String date:dates){
            MainFragment fragment = new MainFragment(date);
            fragments.add(fragment);
        }


    }


    public void reload(){
        for (MainFragment fragment : fragments){
            fragment.reload();
        }

    }


    /**
     * 得到最后一页下标
     * @return
     */
    public int getLastIndex(){
        return fragments.size() - 1;
    }


    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    public String getDateStr(int index){
        return  dates.get(index);
    }


    public int getTotal(int index){
        int amount = fragments.get(index).getTotalCost();

        return amount;

    }

    public int getIncomeTotal(int index) {
        return fragments.get(index).getTotalIncome();
    }
}
