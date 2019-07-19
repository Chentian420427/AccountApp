package com.example.accountapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddRecordActivity extends AppCompatActivity implements View.OnClickListener,CategoryRecyclerAdapter.OnCategoryClickListener {

    private String userInput = "";

    private TextView amountText;
    private EditText editText;

    private RecyclerView recyclerView;
    private CategoryRecyclerAdapter adapter;

    private String category = "General";
    private RecordBean.RecordType type = RecordBean.RecordType.RECORD_TYPE_EXPENSE;
    private String remark = category;

    RecordBean record = new RecordBean();

    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        findViewById(R.id.keyboard_one).setOnClickListener(this);
        findViewById(R.id.keyboard_two).setOnClickListener(this);
        findViewById(R.id.keyboard_three).setOnClickListener(this);
        findViewById(R.id.keyboard_four).setOnClickListener(this);
        findViewById(R.id.keyboard_five).setOnClickListener(this);
        findViewById(R.id.keyboard_six).setOnClickListener(this);
        findViewById(R.id.keyboard_seven).setOnClickListener(this);
        findViewById(R.id.keyboard_eight).setOnClickListener(this);
        findViewById(R.id.keyboard_nine).setOnClickListener(this);
        findViewById(R.id.keyboard_zero).setOnClickListener(this);

        amountText = findViewById(R.id.textView_amount);
        editText = findViewById(R.id.editText);

        editText.setText(remark);

        handleDot();
        handleTypeChange();
        handleBaseSpace();
        handleDone();

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new CategoryRecyclerAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),4);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.notifyDataSetChanged();

        adapter.setOnCategoryClickListener(this);

        RecordBean record = (RecordBean) getIntent().getSerializableExtra("record");

        if (record != null){
            isEdit = true;
            this.record = record;
        }

    }

    /**
     * 点击 . 的触发事件
     */
    private void handleDot(){
        findViewById(R.id.keyboard_dot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!userInput.contains(".")){
                    userInput += ".";
                }
            }
        });
    }

    /**
     * 按下改变 记录类型的按钮
     */
    private void handleTypeChange(){
        findViewById(R.id.keyboard_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == RecordBean.RecordType.RECORD_TYPE_EXPENSE){
                    type = RecordBean.RecordType.RECORD_TYPE_INCOME;
                }else {
                    type = RecordBean.RecordType.RECORD_TYPE_EXPENSE;
                }

                adapter.changeType(type);
                category = adapter.getSelected();
            }
        });
    }

    /**
     * 退格键 的监听事件
     */
    private void handleBaseSpace(){
        findViewById(R.id.keyboard_backspace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userInput.length() > 0){
                    userInput = userInput.substring(0,userInput.length() - 1);
                }

                //如果最后一位是 。 则再删一位
                if (userInput.length() > 0 && (userInput.charAt(userInput.length() - 1) == '.')){
                    userInput = userInput.substring(0,userInput.length() - 1);
                }

                updateAmountText();

            }
        });
    }

    private void handleDone(){
        findViewById(R.id.keyboard_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!userInput.equals("")){
                    double amount = Double.valueOf(userInput);



                    record.setAmount(amount);
                    if (type == RecordBean.RecordType.RECORD_TYPE_EXPENSE){
                        record.setType(1);
                    }else {
                        record.setType(2);
                    }

                    record.setCatecgory(adapter.getSelected());
                    record.setRemark(editText.getText().toString());

                    if (isEdit){
                        GlobalUtil.getInstance().databaseHelper.editRecord(record.getUuid(),record);
                    }else {
                        GlobalUtil.getInstance().databaseHelper.addRecord(record);
                    }


                    finish();


                }else{

                    Toast.makeText(getApplicationContext(),"金额为0，重新输入",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        //获取点击的按钮
        Button button = (Button) v;
        //直接拿布局在按钮里的值来计算，会更快捷
        String input = button.getText().toString();

        if (userInput.contains(".")){

            //如果只有一个 。 后面没有数字 例如 11.   则可以输入
            //如果有一个。 且后面的位数小于2，例如11.1 则可以输入
            if (userInput.split("\\.").length == 1 || userInput.split("\\.")[1].length() < 2){
                userInput += input;
            }

        }else{
            userInput += input;
        }

        updateAmountText();


    }

    /**
     * 更新 显示的金额 UI
     */
    private void updateAmountText(){

        if (userInput.contains(".")){

            if (userInput.split("\\.").length == 1){
                amountText.setText(userInput + "00");
            }else if (userInput.split("\\.")[1].length() == 1){
                amountText.setText(userInput + "0");
            }else if (userInput.split("\\.")[1].length() == 2){
                amountText.setText(userInput);
            }
        }else{
            if (userInput.equals("")){
                amountText.setText("0.00");
            }else{
                amountText.setText(userInput + ".00");
            }
        }
    }

    @Override
    public void onClick(String category) {
        this.category = category;
        editText.setText(category);
    }
}
