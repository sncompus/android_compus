package com.compus.account;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.compus.Constants;
import com.compus.R;
import com.compus.app.CompusApplication;
import com.compus.tools.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener {
	
	private EditText login_passwd_edit;
	
	private EditText login_user_edit;
	
	private Button login_login_btn;
	
	private CompusApplication appCookie;
	  @Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.login);
	        appCookie = ((CompusApplication)getApplicationContext());
	        prepareView();
	  }
	  
		private void prepareView() {
			login_passwd_edit = (EditText) findViewById(R.id.login_passwd_edit);
			login_user_edit = (EditText) findViewById(R.id.login_user_edit);
			login_login_btn = (Button) findViewById(R.id.login_login_btn);
			findViewById(R.id.login_passwd_edit).setOnClickListener(this);
			findViewById(R.id.login_user_edit).setOnClickListener(this);
			findViewById(R.id.login_login_btn).setOnClickListener(this);
		}

		private void prepareIntent() {
			
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.login_reback_btn://���ذ�ť
				LoginActivity.this.finish();//�ر����Activity  ������һ��Activity
				break;
			case R.id.login_login_btn://�����¼��ť   �����ж�  �û����������Ƿ�Ϊ��
				String userEditStr = login_user_edit.getText().toString().trim();
				String passwdEditStr = login_passwd_edit.getText().toString().trim();
				if(("".equals(userEditStr) || null == userEditStr) || 
						("".equals(passwdEditStr) || null == passwdEditStr)){//ֻҪ�û�����������һ��Ϊ��
					new AlertDialog.Builder(LoginActivity.this)
					.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
					.setTitle("��¼ʧ��")
					.setMessage("ѧ�Ż����벻��Ϊ�գ�������ѧ�Ż�����")
					.create().show();
				}else{
					List<NameValuePair> pairs = new ArrayList<NameValuePair>();
					pairs.add(new BasicNameValuePair("UserName", userEditStr));
					pairs.add(new BasicNameValuePair("Password", passwdEditStr));
					HttpUtil.login(Constants.URL_LOGIN, pairs, appCookie);
				}
				break;

			default:
				break;
			}
		}

}
