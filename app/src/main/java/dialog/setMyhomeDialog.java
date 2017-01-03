package dialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import static android.content.Context.MODE_PRIVATE;
import static network.volley.url;

public class setMyhomeDialog extends DialogFragment {

    private EditText nameEditText;
    private EditText sexEditText;
    private EditText ageEditText;
    private EditText tellEditText;
    private SharedPreferences pref;
    private String httpurl1 = url + ":8080/Graduation_practic/updateuser.action";
    private static ProgressDialog dialog;

    // implements this interface if you want to create the dialog
    public interface NewMyhomeDialogCreater {
        NewMyhomeDialogListener getNewMatchDialogListener();
    }

    public interface NewMyhomeDialogListener {

        public void getNameAddr(String username, String sex, String age, String tell);

        void onCancel();

    }

    NewMyhomeDialogListener myhomeDialogListener = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle("修改");
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        final View dialogView = inflater.inflate(R.layout.dialog_set_myhome, container);
        nameEditText = ((TextInputLayout) dialogView.findViewById(R.id.input_name)).getEditText();
        sexEditText = ((TextInputLayout) dialogView.findViewById(R.id.input_sex)).getEditText();
        ageEditText = ((TextInputLayout) dialogView.findViewById(R.id.input_age)).getEditText();
        tellEditText = ((TextInputLayout) dialogView.findViewById(R.id.input_tell)).getEditText();
        onViewStateRestored(savedInstanceState);
        pref = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        // Button
        dialogView.findViewById(R.id.button_give_up).setOnClickListener(new Listener());
        dialogView.findViewById(R.id.button_commit).setOnClickListener(new Listener());
        return dialogView;

    }


    public class Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_give_up:
                    getDialog().dismiss();
                    break;
                case R.id.button_commit:
                    onCommit();
                    break;
            }

        }
    }

    private void onCommit() {
        String name = nameEditText.getText().toString();
        String sex = sexEditText.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(getActivity(), "标题不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        onnewDialogMatch();
//        Call<InformationTransfer> commitQuestionCall = RestApi.getHaruueKnowWebApiService().question(CurrentUser.getInstance().token, title, content);
//        commitQuestionCall.enqueue(new Callback<InformationTransfer>() {
//            @Override
//            public void onResponse(Response<InformationTransfer> response) {
//                if (response.code() == 200) {
//                    JUtils.Toast(getResources().getString(R.string.commit_Match_success));
//                    getDialog().dismiss();
//                    ((NewMatchDialogCreater) getActivity()).getNewMatchDialogListener().onSuccess();
//                } else {
//                    JUtils.Toast(getResources().getString(R.string.fail_please_retry));
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                JUtils.Toast(getResources().getString(R.string.network_error));
//            }
//        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", nameEditText.getText().toString());
        outState.putString("content", sexEditText.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (null != savedInstanceState) {
            nameEditText.setText(savedInstanceState.getString("title"));
            sexEditText.setText(savedInstanceState.getString("content"));
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        ((NewMyhomeDialogCreater) getActivity()).getNewMatchDialogListener().onCancel();
    }


    /**
     * 访问网络
     */
    private void onnewDialogMatch() {
        /**
         * loading...
         */

        if (dialog == null) {
            dialog = new ProgressDialog(getActivity());
        }
        dialog.setMessage("添加中...");
        dialog.setCancelable(false);
        dialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", pref.getString("username", "tom"));
        params.put("name", nameEditText.getText().toString());
        params.put("sex", sexEditText.getText().toString());
        params.put("age", ageEditText.getText().toString());
        params.put("tellphone", tellEditText.getText().toString());

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
                                myhomeDialogListener.getNameAddr(nameEditText.getText().toString(), sexEditText.getText().toString(), ageEditText.getText().toString(), tellEditText.getText().toString());
//                                position.setText(response.getString("poision"));
                                Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                                setMyhomeDialog.this.dismiss();
//                                userTransfer = response;
                            } else {

                                if (dialog != null) {
                                    dialog.cancel();
                                    dialog = null;
                                }
                                Toast.makeText(getActivity(), "未知错误", Toast.LENGTH_SHORT).show();
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
}
