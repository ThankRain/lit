package com.zmide.lit.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.BarUtils;
import com.zmide.lit.R;
import com.zmide.lit.animation.Slide;
import com.zmide.lit.object.Diy;
import com.zmide.lit.skin.RippleAnimation;
import com.zmide.lit.skin.SkinFactory;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.ui.DiyActivity;
import com.zmide.lit.ui.HistoryActivity;
import com.zmide.lit.ui.MarkActivity;
import com.zmide.lit.ui.SettingActivity;
import com.zmide.lit.ui.VideoPlayerActivity;
import com.zmide.lit.util.DBC;
import com.zmide.lit.util.MFileUtils;
import com.zmide.lit.util.MSharedPreferenceUtils;
import com.zmide.lit.util.MToastUtils;
import com.zmide.lit.util.MarkEditDialog;
import com.zmide.lit.view.LitWebView;


import java.util.List;
import java.util.Objects;

import static com.zmide.lit.main.MainViewBindUtils.getAdMark;

public class MenuDialog {
	
	private static SkinFactory skinFactory = new SkinFactory();
	
	private static SharedPreferences mSharedPreferences;
	
	private static Dialog d;
	
	@SuppressLint("StaticFieldLeak")
	private static MenuDialog instance = new MenuDialog();
	
	@SuppressLint("StaticFieldLeak")
	private static Activity activity;
	
	public static void init(Activity a) {
		d = new Dialog(a);
		activity = a;
	}
	
	public static MenuDialog getInstance() {
		return instance;
	}
	
	private static LitWebView mWebView() {
        return WebContainerPlus.getWebView();
    }
	
	public static void initDialog() {
		
		
		//可以在style中设定dialog的样式
		d.getLayoutInflater().setFactory2(skinFactory);
		d.setContentView(R.layout.menu);
		WindowManager.LayoutParams lp = Objects.requireNonNull(d.getWindow()).getAttributes();
		lp.gravity = Gravity.BOTTOM;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.dimAmount = 0.2f;
		d.getWindow().setAttributes(lp);
		//设置该属性，dialog可以铺满屏幕
		d.getWindow().setBackgroundDrawable(null);
		View holder = d.findViewById(R.id.menuHolder);
		FrameLayout.LayoutParams lps = (FrameLayout.LayoutParams) holder.getLayoutParams();
		lps.setMargins(30, 30, 30, 30);
		holder.setLayoutParams(lps);
		LinearLayout mMenuMark = d.getWindow().findViewById(R.id.menuMark);
		final LinearLayout mMenuMarked = d.getWindow().findViewById(R.id.menuMarked);
		final LinearLayout mMenuMarkAdd = d.getWindow().findViewById(R.id.menuMarkAdd);
		LinearLayout mMenuPlugin = d.getWindow().findViewById(R.id.menuPlugin);
		LinearLayout mMenuResource = d.getWindow().findViewById(R.id.menuResource_catcher);
		LinearLayout mMenuAdMark = d.getWindow().findViewById(R.id.menuAdMark);
		LinearLayout mMenuRefresh = d.findViewById(R.id.menuRefresh);
		LinearLayout mMenuHistory = d.getWindow().findViewById(R.id.menuHistory);
		LinearLayout mMenuSetting = d.getWindow().findViewById(R.id.menuSetting);
		LinearLayout mMenuUa = d.findViewById(R.id.menuUa);
		final TextView mMenuNightT = d.findViewById(R.id.menuNightT);
		LinearLayout mMenuNight = d.findViewById(R.id.menuNight);
		LinearLayout mMenunoHistory = d.findViewById(R.id.menunoHistory);
		mSharedPreferences = activity.getSharedPreferences("setting", Context.MODE_PRIVATE);
		
		mMenunoHistory.setOnClickListener(view -> {
			boolean noHistory = mSharedPreferences.getString("no_history", "false").equals("false");
			String ct;
			if (noHistory) {
				ct = "true";
				MToastUtils.makeText("已进入无痕模式", MToastUtils.LENGTH_SHORT).show();
			} else {
				ct = "false";
				MToastUtils.makeText("已退出无痕模式", MToastUtils.LENGTH_SHORT).show();
			}
			mSharedPreferences.edit().putString("no_history", ct).apply();
			d.dismiss();
		});
		switch (Integer.parseInt(mSharedPreferences.getString("themeMode", "2"))) {
			
			case 0://DAY
				mMenuNightT.setText("夜间模式");
				break;
			case 1://Night
				mMenuNightT.setText("日间模式");
				break;
			default:
				int currentNightMode = activity.getResources().getConfiguration().uiMode
						& Configuration.UI_MODE_NIGHT_MASK;
				switch (currentNightMode) {
					case Configuration.UI_MODE_NIGHT_YES: {
						mMenuNightT.setText("夜间模式");
						// Night mode is not active, we're in day time
						break;
					}
					case Configuration.UI_MODE_NIGHT_NO: {
						mMenuNightT.setText("日间模式");
						break;
					}
					case Configuration.UI_MODE_NIGHT_UNDEFINED: {
						// We don't know what mode we're in, assume notnight
					}
				}
				/*
				 new Thread(){

				 @Override
				 public void run() {
				 try
				 {
				 sleep(20);
				 }
				 catch (InterruptedException e)
				 {}
				 runOnUiThread(new Runnable() {
				 @Override
				 public void run() {
				 skinFactory.apply();
				 }
				 });
				 }
				 }.start();*/
				
				break;
		}
		mMenuNight.setOnLongClickListener(view -> {
			boolean webDark = mSharedPreferences.getString("web_isdark", "false").equals("true");
			String ct;
			if (webDark) {
				ct = "false";
				MToastUtils.makeText("已关闭网页负片处理", MToastUtils.LENGTH_SHORT).show();
			} else {
				ct = "true";
				MToastUtils.makeText("已开启网页负片处理", MToastUtils.LENGTH_SHORT).show();
			}
			mSharedPreferences.edit().putString("web_isdark", ct).apply();
			d.dismiss();
			return false;
		});
		mMenuNight.setOnClickListener(view -> {
			RippleAnimation.create(activity.findViewById(R.id.mainBall)).setDuration(500).start();
			int ct = 0;
			switch (Integer.parseInt(mSharedPreferences.getString("themeMode", "2"))) {
				
				case 0://DAY
					ct = 1;
					SkinManager.getInstance().loadSkin("");
					mMenuNightT.setText("日间模式");
					BarUtils.setStatusBarLightMode(activity, false);
					BarUtils.setNavBarColor(activity, 0xff303030);
					BarUtils.setNavBarLightMode(activity, false);
					//BarUtils.setNavBarColor(activity,0xff333333);
					//todo 日间模式
					break;
				case 1://Night
					ct = 0;
					SkinManager.getInstance().loadSkin("skin/dark.apk");
					mMenuNightT.setText("夜间模式");
					BarUtils.setStatusBarLightMode(activity, true);
					BarUtils.setNavBarColor(activity, 0xffffffff);
					BarUtils.setNavBarLightMode(activity, true);
					//todo StatusBarUtil.setTranslucentStatus(activity, false);
					break;
				default:
					int currentNightMode = activity.getResources().getConfiguration().uiMode
							& Configuration.UI_MODE_NIGHT_MASK;
					switch (currentNightMode) {
						case Configuration.UI_MODE_NIGHT_YES: {
							ct = 0;
							SkinManager.getInstance().loadSkin("");
							mMenuNightT.setText("日间模式");
							BarUtils.setStatusBarLightMode(activity, false);
							BarUtils.setNavBarColor(activity, 0xff303030);
							BarUtils.setNavBarLightMode(activity, false);
							//todo StatusBarUtil.setTranslucentStatus(activity, true);
							// Night mode is not active, we're in day time
							break;
						}
						case Configuration.UI_MODE_NIGHT_NO: {
							ct = 1;
							SkinManager.getInstance().loadSkin("skin/dark.apk");
							mMenuNightT.setText("夜间模式");
							BarUtils.setStatusBarLightMode(activity, true);
							BarUtils.setNavBarColor(activity, 0xffffffff);
							BarUtils.setNavBarLightMode(activity, true);
							//todo StatusBarUtil.setTranslucentStatus(activity, false);
							// Night mode is active, we're at night!
							break;
						}
						case Configuration.UI_MODE_NIGHT_UNDEFINED: {
							// We don't know what mode we're in, assume notnight
						}
					}
					/*
					 new Thread(){

					 @Override
					 public void run() {
					 try
					 {
					 sleep(20);
					 }
					 catch (InterruptedException e)
					 {}
					 runOnUiThread(new Runnable() {
					 @Override
					 public void run() {
					 skinFactory.apply();
					 }
					 });
					 }
					 }.start();*/
					
					break;
			}
			activity.runOnUiThread(() -> skinFactory.apply());
			mSharedPreferences.edit().putString("themeMode", ct + "").apply();
			d.cancel();
			
		});
		mMenuPlugin.setOnClickListener(view -> {
			Intent i = new Intent(view.getContext(), DiyActivity.class);
			i.putExtra("type", Diy.PLUGIN);
			activity.startActivity(i, ActivityOptions.makeSceneTransitionAnimation(activity, d.getWindow().findViewById(R.id.menuPluginT), "diy").toBundle());
			d.dismiss();
		});
		mMenuUa.setOnClickListener(view -> {
			Intent i = new Intent(view.getContext(), DiyActivity.class);
			i.putExtra("type", Diy.UA);
			activity.startActivity(i, ActivityOptions.makeSceneTransitionAnimation(activity, d.getWindow().findViewById(R.id.menuUAT), "diy").toBundle());
			d.dismiss();
		});
		mMenuRefresh.setOnClickListener(view -> activity.runOnUiThread(() -> {
			mWebView().reload();
			d.dismiss();
		}));
		
		mMenuResource.setOnClickListener(view -> activity.runOnUiThread(() -> {
			List<Uri> uris = ResourceCatcher.getResources(ResourceCatcher.TYPE.VIDEO);
			if (uris.size() != 0){
				activity.startActivity(new Intent(activity, VideoPlayerActivity.class)
						.setData(uris.get(0))
						.putExtra("title",WebContainerPlus.getTitle())
				);
				//new ResourceShowerFragment().show(((BaseActivity)activity).getSupportFragmentManager(), "dialog");
			}else{
				MToastUtils.makeText("暂未嗅探到视频资源").show();
			}
			d.dismiss();
		}));
		
		mMenuAdMark.setOnClickListener(view -> activity.runOnUiThread(() -> {
			getAdMark().setVisibility(View.VISIBLE);
			WebContainerPlus.getWebView().evaluateJavascript("(function () {\n" +
					"    var old;\n" +
					"    this.clicklistener = function (event) {\n" +
					"        var length = document.all.length;\n" +
					"        console.log(length);\n" +
					"        for (var i = 0; i < length; i++) {\n" +
					"            document.all[i].classList.remove(\"selected\");\n" +
					"        }\n" +
					"        this.classList.add(\"selected\");\n" +
					"        let classNames = this.className.split(\".\");\n" +
					"        if (classNames.length === 0)\n" +
					"            classNames = '';\n" +
					"        else\n" +
					"            classNames = '.'+classNames;\n" +
					"        let id = this.id;\n" +
					"        if (typeof id != 'undefined' && id != null && id !== '') id = '#'+id; else id = '';\n" +
					"        window.lit.putElement(this.tagName + classNames + id);\n" +
					"        window.lit.putCode(this + '');\n" +
					"        old = this;\n" +
					"        event.stopPropagation();\n" +
					"        event.preventDefault()\n" +
					"        event.stopImmediatePropagation()\n" +
					"    }\n" +
					"    //这是遍历绑定点击事件\n" +
					"        var length = document.all.length;\n" +
					"        for (var i = 0; i < length; i++) {\n" +
					"            document.all[i].onclick = clicklistener;\n" +
					"        }\n" +
					"    function addCSS(cssText) {\n" +
					"        var style = document.createElement('style'),  //创建一个style元素\n" +
					"            head = document.head || document.getElementsByTagName('head')[0]; //获取head元素\n" +
					"        style.type = 'text/css'; //这里必须显示设置style元素的type属性为text/css，否则在ie中不起作用\n" +
					"        if (style.styleSheet) { //IE\n" +
					"            var func = function () {\n" +
					"                try { //防止IE中stylesheet数量超过限制而发生错误\n" +
					"                    style.styleSheet.cssText = cssText;\n" +
					"                } catch (e) {\n" +
					"\n" +
					"                }\n" +
					"            }\n" +
					"            //如果当前styleSheet还不能用，则放到异步中则行\n" +
					"            if (style.styleSheet.disabled) {\n" +
					"                setTimeout(func, 10);\n" +
					"            } else {\n" +
					"                func();\n" +
					"            }\n" +
					"        } else { //w3c\n" +
					"            //w3c浏览器中只要创建文本节点插入到style元素中就行了\n" +
					"            var textNode = document.createTextNode(cssText);\n" +
					"            style.appendChild(textNode);\n" +
					"        }\n" +
					"        head.appendChild(style); //把创建的style元素插入到head中\n" +
					"    }\n" +
					"    addCSS('.selected{border-style:solid;border-color:#0000ff;}');\n" +
					"})();", null);
			d.dismiss();
		}));
		
		
		mMenuMarked.setOnClickListener(view -> activity.runOnUiThread(() -> {
			DBC.getInstance(view.getContext()).deleteMark(mWebView().getUrl());
			mMenuMarked.setVisibility(View.GONE);
			mMenuMarkAdd.setVisibility(View.VISIBLE);
			MToastUtils.makeText("移除书签成功", MToastUtils.LENGTH_LONG).show();
			d.dismiss();
		}));
		/*TextView tt;
		RecyclerView rv;
		String FolderId = "0";*//*
		@Override
		public void onLoadFoldersIndex(String id) {
			FolderAdapter adapter = new FolderAdapter(activity, DBC.getInstance(activity).getParents(id));
			adapter.setInterface(this);
			rv.setAdapter(adapter);
			FolderId = id;
			String indexName;
			if (Objects.equals(FolderId, "0"))
				indexName = "根目录";
			else
				indexName = DBC.getInstance(activity).getIndexName(FolderId);
			if (Objects.equals(indexName, "") || indexName == null)
				tt.setText(R.string.root_index);
			else
				tt.setText(indexName);
		}*/
		mMenuMarkAdd.setOnClickListener(view -> activity.runOnUiThread(() -> {
			if (!Objects.equals(mWebView().getUrl(), "") && !mWebView().getUrl().startsWith("file:///")) {
				String path = MFileUtils.saveFile(mWebView().getFavicon(), null, false);
				String url0 = mWebView().getUrl();
				String title0 = mWebView().getTitle();
				DBC.getInstance(view.getContext()).addMark(title0, path, url0, 0);
				MToastUtils.makeText(activity,"添加书签成功", "编辑", v -> new MarkEditDialog(activity).createMarkEditDialog(activity, "0", title0, url0, true), MToastUtils.LENGTH_LONG).show();
				mMenuMarked.setVisibility(View.VISIBLE);
				mMenuMarkAdd.setVisibility(View.GONE);
			} else {
				MToastUtils.makeText("添加书签失败", MToastUtils.LENGTH_LONG).show();
			}
			d.dismiss();
		}));
		mMenuMark.setOnClickListener(view -> {
			activity.startActivity(new Intent(activity, MarkActivity.class), ActivityOptions.makeSceneTransitionAnimation(activity, d.getWindow().findViewById(R.id.menuMarkT), "mark").toBundle());
			d.dismiss();
		});
		
		mMenuHistory.setOnClickListener(view -> {
			activity.startActivity(new Intent(activity, HistoryActivity.class), ActivityOptions.makeSceneTransitionAnimation(activity, d.getWindow().findViewById(R.id.menuHistoryT), "history").toBundle());
			d.dismiss();
		});
		
		mMenuSetting.setOnClickListener(view -> {
			activity.startActivity(new Intent(activity, SettingActivity.class), ActivityOptions.makeSceneTransitionAnimation(activity, d.getWindow().findViewById(R.id.menusettingT), "setting").toBundle());
			d.dismiss();
		});
	}
	
	public static void showDialog() {
        mSharedPreferences = MSharedPreferenceUtils.getSharedPreference();
        final boolean res = mSharedPreferences.getString("no_history", "false").equals("false");
        final ImageView mMenuNoHistoryI = d.findViewById(R.id.menuNoHistoryI);
        final LinearLayout mMenuMarked = Objects.requireNonNull(d.getWindow()).findViewById(R.id.menuMarked);
        final LinearLayout mMenuMarkAdd = d.getWindow().findViewById(R.id.menuMarkAdd);
        final LinearLayout mMenuResource = d.getWindow().findViewById(R.id.menuResource_catcher);
        final LitWebView mWebView = WebContainerPlus.getWebView();
        activity.runOnUiThread(() -> {
            if (res) {
                mMenuNoHistoryI.setImageDrawable(SkinManager.getInstance().getDrawable(R.drawable.no_history_closed));

            } else {
                mMenuNoHistoryI.setImageDrawable(SkinManager.getInstance().getDrawable(R.drawable.no_history_open));

            }
        });
		if (DBC.getInstance(activity).getMark(mWebView.getUrl())) {
			activity.runOnUiThread(() -> {
				mMenuMarked.setVisibility(View.VISIBLE);
				mMenuMarkAdd.setVisibility(View.GONE);
			});
		} else {
			activity.runOnUiThread(() -> {
				mMenuMarked.setVisibility(View.GONE);
				mMenuMarkAdd.setVisibility(View.VISIBLE);
			});
		}
		if (mWebView.getUrl() != null)
			if (mWebView.getUrl().equals("") || mWebView.getUrl().startsWith("file:///")) {
				activity.runOnUiThread(() -> {
					mMenuMarkAdd.setVisibility(View.GONE);
					mMenuMarked.setVisibility(View.GONE);
					mMenuResource.setVisibility(View.GONE);
				});
			}else{
				mMenuResource.setVisibility(View.VISIBLE);
			}
		d.show();
		activity.runOnUiThread(() -> skinFactory.apply());
		Slide.slideToUp(d.getWindow().findViewById(R.id.menuHolder));
	}
	
}
