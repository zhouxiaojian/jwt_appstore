package com.ascf.jwt.appstore.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ascf.jwt.appstore.Constant;
import com.ascf.jwt.appstore.R;
import com.ascf.jwt.appstore.dirparser.ServiceForAccount;

public class SignInActivity extends Activity implements View.OnClickListener {

    public static final String TAG = "SignInActivity";

    private static final int OPT_MENU_SET_IP = 1;

    private static final int DIALOG_SET_IP = 1;

    public static final int OK_BUTTON = R.id.login;
    public static final int CANCEL_BUTTON = R.id.cancel;
    private Button mConfirmBtn = null;
    private Button mCancleBtn = null;
    private EditText mAccountText = null;
    private EditText mPwdText = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_setup_basics);
        mAccountText = (EditText) findViewById(R.id.account_name);
        mPwdText = (EditText) findViewById(R.id.account_password);
        mConfirmBtn = (Button) findViewById(OK_BUTTON);
        mConfirmBtn.setOnClickListener(this);
        mCancleBtn = (Button) findViewById(CANCEL_BUTTON);
        mCancleBtn.setOnClickListener(this);
    }

    private void saveValue(String user, String pwd) {
        ServiceForAccount.getIntance().setContext(this);
        ServiceForAccount.getIntance().saveKeyValue(Constant.KEY_USERNAME, user);
        ServiceForAccount.getIntance().saveKeyValue(Constant.KEY_PASSWORD, pwd);
    }

    @Override
    public void onResume() {
        //restoreValue();
        super.onResume();
    }

    private void restoreValue() {
        ServiceForAccount.getIntance().setContext(this);
        String user = ServiceForAccount.getIntance().getValueByKey(Constant.KEY_USERNAME);
        String pwd = ServiceForAccount.getIntance().getValueByKey(Constant.KEY_PASSWORD);
        if (user != null && !"".equals(user)) {
            this.mAccountText.setText(user);
        }
        if (pwd != null && !"".equals(pwd)) {
            this.mPwdText.setText(pwd);
        }
    }

    //@Override
    public void onClick(View v) {
        switch (v.getId()) {
        case OK_BUTTON: {
            String accname = mAccountText.getText().toString();
            String pwd = mPwdText.getText().toString();
            Intent i = new Intent(Constant.ACTIVITY_ACTION_CHECK);
            i.putExtra(Constant.KEY_USERNAME, accname);
            i.putExtra(Constant.KEY_PASSWORD, pwd);
            saveValue(accname, pwd);
//          i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                    | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            this.startActivityForResult(i, 0);
        }
            break;
        case CANCEL_BUTTON:
            finish();
            break;
        }
    }


    @Override
    public void onStop() {
        hideInputMethod();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void hideInputMethod() {
        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void showInputMethod() {
        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO ....
        if (resultCode == RESULT_OK){
            finish();
        }else {
            Toast.makeText(this, R.string.check_user_fail, Toast.LENGTH_LONG);
        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog dialog = null;
        switch (id) {
        case DIALOG_SET_IP:
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.dialog_ip_port, null);
            final EditText servip = (EditText) view.findViewById(R.id.server_ip_text);
            final EditText portT = (EditText) view.findViewById(R.id.server_port_text);
            ServiceForAccount.getIntance().setContext(this);
            servip.setText(ServiceForAccount.getIntance().getServerIP());
            portT.setText(ServiceForAccount.getIntance().getServerPort());
            dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.set_server_ip_port)
                    .setView(view)
                    .setPositiveButton(android.R.string.ok,
                            new OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ServiceForAccount.getIntance().setContext(SignInActivity.this);
                                    String ip = servip.getText().toString().trim();
                                    String port = portT.getText().toString().trim();
                                    ServiceForAccount.getIntance().saveKeyValue(Constant.KEY_IP, ip);
                                    ServiceForAccount.getIntance().saveKeyValue(Constant.KEY_PORT, port);
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int which) {

                                }
                            }).create();
            dialog.getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                                    | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            break;
        }
        return dialog;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case OPT_MENU_SET_IP:
            showDialog(DIALOG_SET_IP);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, OPT_MENU_SET_IP, 0, R.string.server_setting).setIcon(
                R.drawable.ic_launcher_settings);
        return true;
    }

}
