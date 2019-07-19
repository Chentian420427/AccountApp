package com.example.accountapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.LinkedList;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private LayoutInflater mInfiater;
    public Context mContext;

    private LinkedList<CategoryResBean> cellList = GlobalUtil.getInstance().costRes;



    private String selected = "";


    private OnCategoryClickListener onCategoryClickListener;

    public String getSelected() {
        return selected;
    }

    public void setOnCategoryClickListener(OnCategoryClickListener onCategoryClickListener) {
        this.onCategoryClickListener = onCategoryClickListener;
    }


    public CategoryRecyclerAdapter(Context context){
        //GlobalUtil.getInstance().setContext(context);
        this.mContext = context;
        mInfiater = LayoutInflater.from(mContext);

        cellList = GlobalUtil.getInstance().costRes;
        if (cellList.size() > 20){
            cellList.clear();
            GlobalUtil.getInstance().addRes();
            cellList = GlobalUtil.getInstance().costRes;
        }

        selected = cellList.get(0).title;
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = mInfiater.inflate(R.layout.cell_category,viewGroup,false);
        CategoryViewHolder myViewHolder = new CategoryViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i) {
        final CategoryResBean res = cellList.get(i);

        categoryViewHolder.imageView.setImageResource(res.resBlack);
        categoryViewHolder.textView.setText(res.title);

        categoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = res.title;
                notifyDataSetChanged();

                if (onCategoryClickListener!=null){
                    onCategoryClickListener.onClick(res.title);
                }
            }
        });

        //若选择的图标标题和当前标题对应，就显示一个选择框
        if (categoryViewHolder.textView.getText().toString().equals(selected)){
            categoryViewHolder.background.setBackgroundResource(R.drawable.bg_edit_text);
        }else {
            categoryViewHolder.background.setBackgroundResource(R.color.colorPrimary);
        }
    }


    //给外部调用更改资源
    public void changeType(RecordBean.RecordType type){
        if (type == RecordBean.RecordType.RECORD_TYPE_EXPENSE){
            cellList = GlobalUtil.getInstance().costRes;
            if (cellList.size() > 20){
                cellList.clear();
                GlobalUtil.getInstance().addRes();
                cellList = GlobalUtil.getInstance().costRes;
            }
        }else {
            cellList = GlobalUtil.getInstance().earnRes;
            if (cellList.size() > 12){
                cellList.clear();
                GlobalUtil.getInstance().addRes();
                cellList = GlobalUtil.getInstance().earnRes;
            }
        }

        selected = cellList.get(0).title;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return cellList.size();
    }


    public interface OnCategoryClickListener{

        void onClick(String category);
    }
}

class CategoryViewHolder extends RecyclerView.ViewHolder{

    RelativeLayout background;
    ImageView imageView;
    TextView textView;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        background = itemView.findViewById(R.id.cell_background);
        imageView = itemView.findViewById(R.id.imageView_category);
        textView = itemView.findViewById(R.id.textView_category);
    }
}
