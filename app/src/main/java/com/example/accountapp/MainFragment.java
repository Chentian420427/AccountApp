package com.example.accountapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;

@SuppressLint("ValidFragment")
public class MainFragment extends Fragment implements AdapterView.OnItemLongClickListener {

    private View rootView;
    private TextView textView;
    private ListView listView;
    private ListViewAdapter listViewAdapter;

    private LinkedList<RecordBean> records = new LinkedList<>();

    private String date = "";

    @SuppressLint("ValidFragment")
    public MainFragment(String date){
        this.date = date;

//        records.add(new RecordBean());
//        records.add(new RecordBean());
//        records.add(new RecordBean());
//        records.add(new RecordBean());

        records = GlobalUtil.getInstance().databaseHelper.readRecords(date);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main,container,false);
        initView();
        return rootView;

    }

    public void reload(){
        records = GlobalUtil.getInstance().databaseHelper.readRecords(date);

        if (listViewAdapter == null){
            listViewAdapter = new ListViewAdapter(getActivity().getApplicationContext());
        }


        listViewAdapter.setData(records);
        listView.setAdapter(listViewAdapter);

        //如果列表有数据，则不显示  提示信息
        if (listViewAdapter.getCount() > 0){
            rootView.findViewById(R.id.no_record_layout).setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 初始化视图，找到相应的UI组件
     */
    private void initView(){
        textView = rootView.findViewById(R.id.day_text);
        listView = rootView.findViewById(R.id.listView);

        textView.setText(date);

        listViewAdapter = new ListViewAdapter(getContext());
        listViewAdapter.setData(records);
        listView.setAdapter(listViewAdapter);

        //如果列表有数据，则不显示  提示信息
        if (listViewAdapter.getCount() > 0){
            rootView.findViewById(R.id.no_record_layout).setVisibility(View.INVISIBLE);
        }

        //监听listView里的列表长按点击事件
        listView.setOnItemLongClickListener(this);

    }

    public int getTotalCost(){
        double totalCost = 0;

        for (RecordBean record : records){
            if (record.getType() == 1){
                totalCost += record.getAmount();
            }

        }

        return (int)totalCost;

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        showDialog(position);

        return false;
    }

    /**
     * 展示  删除 编辑 模态框
     * @param index
     */
    private void showDialog(int index){
        final String[] options = {"删除","编辑"};

        final RecordBean selectedRecord = records.get(index);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.create();

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0){

                    //删除
                    String uuid = selectedRecord.getUuid();
                    GlobalUtil.getInstance().databaseHelper.removeRecord(uuid);
                    reload();
                    GlobalUtil.getInstance().mainActivity.updateHeader();

                }else if (which == 1){
                    //编辑
                    Intent intent = new Intent(getActivity(),AddRecordActivity.class);
                    Bundle extra = new Bundle();

                    extra.putSerializable("record",selectedRecord);
                    intent.putExtras(extra);

                    startActivityForResult(intent,1);

                }
            }
        });

        builder.setNegativeButton("取消",null);
        builder.create().show();
    }

    public int getTotalIncome() {
        double totalIncome = 0;
        for (RecordBean record : records) {
            if (record.getType() == 2){
                totalIncome += record.getAmount();
            }
        }
        return (int) totalIncome;
    }
}
