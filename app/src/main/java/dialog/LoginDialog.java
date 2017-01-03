package dialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.ygh.graduation_practice.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import network.NormalPostRequest;
import transfer.UserTransfer;

import static android.content.Context.MODE_PRIVATE;
import static network.volley.url;


public class LoginDialog extends DialogFragment {

    private String username;
    private String password;

    private UserTransfer userTransfer;
    private String httpurl1 = url+":8080/Graduation_practic/login.action";
    private String httpurl2 = url+":8080/Graduation_practic/regist.action";
    private static ProgressDialog dialog;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
//    private Context

    // implements this interface if you want to create the dialog
    public interface LoginDialogCreater {
        LoginDialogListener getLoginDialogListener();
    }

    public interface LoginDialogListener {
        public void getNameAddr(String username);

    }

    LoginDialogListener dialogListener = null;

    /**********
     * 通过这个方法，将回调函数的地址传入到MyDialog中
     *********/
    public void setDialogListener(LoginDialogListener listener) {
        this.dialogListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle(getResources().getString(R.string.login_or_register));
        getDialog().setCanceledOnTouchOutside(false);
        final View dialogView = inflater.inflate(R.layout.dialog_login, container);
        final EditText usernameEditText = ((TextInputLayout) dialogView.findViewById(R.id.input_username)).getEditText();
        usernameEditText.setText(getTag());
        final EditText passwordEditText = ((TextInputLayout) dialogView.findViewById(R.id.input_password)).getEditText();
        pref = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        editor = pref.edit();
        // Set button listener
        dialogView.findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                /**
                 * loading...
                 */

                if (dialog == null) {
                    dialog = new ProgressDialog(getActivity());
                }
                dialog.setMessage("登录中...");
                dialog.setCancelable(false);
                dialog.show();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.username_or_password_empty_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                onLoginDialogLogin(username, password);
            }
        });
        dialogView.findViewById(R.id.button_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.username_or_password_empty_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                onLoginDialogRegister(username, password);
            }
        });
        return dialogView;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    private void onLoginDialogLogin(final String username, String password) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);
        /**
         * 访问网络
         */

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        Request<JSONObject> request = new NormalPostRequest(httpurl1,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String result = null;
                        try {
                            result = response.getString("result");
                            if (result.equals("success")) {

                                if (dialog != null) {
                                    dialog.cancel();
                                    dialog = null;
                                }
//                                position.setText(response.getString("poision"));
                                Toast.makeText(getActivity(), getResources().getString(R.string.login_success), Toast.LENGTH_SHORT).show();
//                                userTransfer = response;
                                LoginDialog.this.dismiss();
                                editor.putString("username", username);
                                Log.e("d登录","  "+response.getString("name"));
//                                String addr=response.getString("image");
                                if (response.getString("name")==null) {
                                    Log.e("2222登录","  "+response.getString("name"));
                                    dialogListener.getNameAddr(username);
                                } else
                                    dialogListener.getNameAddr(response.getString("name"));
                                Log.e("登录成功", "   " + response.getString("name"));
//                                dialogListener.getImageAddr(addr);
                                editor.putString("name", response.getString("name"));
                                editor.commit();
                            } else {

                                if (dialog != null) {
                                    dialog.cancel();
                                    dialog = null;
                                }
                                Toast.makeText(getActivity(), getResources().getString(R.string.login_failed_for_password_error), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(Old_LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                JUtils.ToastLong(getResources().getString(R.string.other_server_error));
                Toast.makeText(getActivity(), "连接服务器错误", Toast.LENGTH_SHORT).show();

                if (dialog != null) {
                    dialog.cancel();
                    dialog = null;
                }
            }
        }, params);
        requestQueue.add(request);
    }


    private void onLoginDialogRegister(final String username, String password) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);
        /**
         * 访问网络
         */

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        Request<JSONObject> request = new NormalPostRequest(httpurl2,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String result = null;
                        try {
                            result = response.getString("result");
                            if (result.equals("success")) {

                                if (dialog != null) {
                                    dialog.cancel();
                                    dialog = null;
                                }
//                                position.setText(response.getString("poision"));
                                Toast.makeText(getActivity(), getResources().getString(R.string.register_success), Toast.LENGTH_SHORT).show();
//                                userTransfer = response;
                                LoginDialog.this.dismiss();
                            } else {

                                if (dialog != null) {
                                    dialog.cancel();
                                    dialog = null;
                                }
                                Toast.makeText(getActivity(), getResources().getString(R.string.register_failed_for_username_exist), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                JUtils.ToastLong(getResources().getString(R.string.other_server_error));
                Toast.makeText(getActivity(), "连接服务器错误", Toast.LENGTH_SHORT).show();

                if (dialog != null) {
                    dialog.cancel();
                    dialog = null;
                }
            }
        }, params);
        requestQueue.add(request);
    }

}
