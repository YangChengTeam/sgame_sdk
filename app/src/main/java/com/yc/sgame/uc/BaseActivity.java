package com.yc.sgame.uc;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.yc.sgame.uc.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ToastUtil.init(getApplicationContext());
		onBeforeRequestPermission(savedInstanceState);
		// 如果targetSDKVersion >= 23，就要申请好权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），那么只需要在这里直接调用fetchSplashAD接口。
		if (Build.VERSION.SDK_INT >= 23) {
			checkAndRequestPermission();
		} else {
			onRequestPermissionSuccess();
		}
	}

	protected void onBeforeRequestPermission(Bundle savedInstanceState) {

	}

	/**
	 * 权限请求成功的回调函数
	 */
	protected void onRequestPermissionSuccess() {

	}
	
	/*protected void setTitle(String title){
		((TextView)findViewById(R.id.title)).setText(title);
	}*/
	
	public void backPressed(View v){
		onBackPressed();
	}

	public void switchOrientation() {
		if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	protected List<String> getNecessaryPermissions() {
		return Arrays.asList(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION);
	}
	/**
	 *
	 * ----------非常重要----------
	 *
	 * Android6.0以上的权限适配简单示例：
	 *
	 * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
	 *
	 * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
	 * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
	 */
	@TargetApi(23)
	private void checkAndRequestPermission() {
		List<String> lackedPermission = new ArrayList<String>();
		List<String> necessaryPermissions = getNecessaryPermissions();
		for (String necessaryPermission : necessaryPermissions) {
			if (!(checkSelfPermission(necessaryPermission) == PackageManager.PERMISSION_GRANTED)) {
				lackedPermission.add(necessaryPermission);
			}
		}

		// 权限都已经有了，那么直接调用SDK
		if (lackedPermission.size() == 0) {
			onRequestPermissionSuccess();
		} else {
			// 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
			String[] requestPermissions = new String[lackedPermission.size()];
			lackedPermission.toArray(requestPermissions);
			requestPermissions(requestPermissions, 1024);
		}
	}

	private boolean hasAllPermissionsGranted(int[] grantResults) {
		for (int grantResult : grantResults) {
			if (grantResult == PackageManager.PERMISSION_DENIED) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
			onRequestPermissionSuccess();
		} else {
			// 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
			Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			intent.setData(Uri.parse("package:" + getPackageName()));
			startActivity(intent);
			finish();
		}
	}
}
