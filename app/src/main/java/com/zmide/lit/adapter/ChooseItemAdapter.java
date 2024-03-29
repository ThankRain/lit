package com.zmide.lit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.bean.SettingChild;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.util.MDialogUtils;
import com.zmide.lit.util.MSharedPreferenceUtils;

/**
 * Created by xeu on 2019/12/13.
 */

public class ChooseItemAdapter extends RecyclerView.Adapter<ChooseItemAdapter.MyViewHolder> {
	
	private SettingChild settings;
	private LayoutInflater mInflater;
	
	private SharedPreferences sharedPreferences;
	
	private TextView tv;
	
	private MDialogUtils d;
	
	//提供一个合适的构造方法
	ChooseItemAdapter(Context context, SettingChild settings, TextView tv, MDialogUtils d) {
		this.d = d;
		this.tv = tv;
		this.settings = settings;
		mInflater = LayoutInflater.from(context);
		
	}
	
	/**
	 * 将布局转换为View并传递给自定义的MyViewHolder
	 *
	 * @param viewGroup viewGroup
	 * @param viewType  int
	 * @return holder
	 */
	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		View view = mInflater.inflate(R.layout.choose_item, viewGroup, false);
		return new MyViewHolder(view);
	}
	
	/**
	 * 建立起MyViewHolder中视图与数据的关联
	 *
	 * @param viewHolder holder
	 * @param position   int
	 */
	@Override
	public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
		final String msg = settings.choose[position];
		if (settings.isWebSetting)
			sharedPreferences = MSharedPreferenceUtils.getWebViewSharedPreference();
		else
			sharedPreferences = MSharedPreferenceUtils.getSharedPreference();
		if (msg.contentEquals(tv.getText()))
			viewHolder.mTitle.setTextColor(0xff2196f3);
		viewHolder.mTitle.setText(msg);
		viewHolder.mParent.setOnClickListener(view -> {
			sharedPreferences.edit().putString(settings.target, position + "").apply();
			d.cancel();
			tv.setText(msg);
		});
	}
	
	
	/**
	 * 获取item的数目
	 *
	 * @return int
	 */
	@Override
	public int getItemCount() {
		return settings.choose.length;
	}
	
	//自定义的ViewHolder，持有item的所有控件
	static class MyViewHolder extends RecyclerView.ViewHolder {
		private TextView mTitle;
		private LinearLayout mParent;
		
		MyViewHolder(View view) {
			super(view);
			mTitle = view.findViewById(R.id.chooseItemTextView);
			mParent = view.findViewById(R.id.chooseItemParent);
			mTitle.setTextColor(SkinManager.getInstance().getColor(R.color.accent));
			mParent.setBackground(SkinManager.getInstance().getDrawable(R.drawable.ripple_normal));
		}
	}
	
	
}
