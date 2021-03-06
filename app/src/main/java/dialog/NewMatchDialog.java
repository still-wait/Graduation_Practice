package dialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import network.NormalPostRequest;

import static android.content.Context.MODE_PRIVATE;

public class NewMatchDialog extends DialogFragment {

    private EditText titleEditText;
    private EditText contentEditText;
    private EditText timeEditText;
    private EditText minEditText;
    private EditText maxEditText;
    private EditText locationEditText;
    private SharedPreferences pref;
    private String httpurl1 = "http://192.168.191.1:8080/Graduation_practic/newmatch.action";
    private static ProgressDialog dialog;
    // implements this interface if you want to create the dialog
    public interface NewMatchDialogCreater {
        NewMatchDialogListener getNewMatchDialogListener();
    }

    public interface NewMatchDialogListener {

        void onSuccess();
        void onCancel();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle(R.string.new_match);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        final View dialogView = inflater.inflate(R.layout.dialog_new_match, container);
        titleEditText = ((TextInputLayout) dialogView.findViewById(R.id.input_question_title)).getEditText();
        contentEditText = ((TextInputLayout) dialogView.findViewById(R.id.input_question_content)).getEditText();
        timeEditText = ((TextInputLayout) dialogView.findViewById(R.id.input_time)).getEditText();
        minEditText = ((TextInputLayout) dialogView.findViewById(R.id.input_min)).getEditText();
        maxEditText = ((TextInputLayout) dialogView.findViewById(R.id.input_max)).getEditText();
        locationEditText = ((TextInputLayout) dialogView.findViewById(R.id.input_location)).getEditText();
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
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();
        if (title.isEmpty()) {
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
        outState.putString("title", titleEditText.getText().toString());
        outState.putString("content", contentEditText.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (null != savedInstanceState) {
            titleEditText.setText(savedInstanceState.getString("title"));
            contentEditText.setText(savedInstanceState.getString("content"));
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        ((NewMatchDialogCreater) getActivity()).getNewMatchDialogListener().onCancel();
    }


    /**
     *访问网络
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
        params.put("username", pref.getString("username","tom"));
        // 获取系统时间
        String currentTime = DateFormat.format(
                "yyyy-MM-dd hh:mm:ss", new Date()).toString();
        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        currentTime = sdf.format(new Date());

        params.put("ftime",currentTime);
        params.put("time",timeEditText.getText().toString());
        params.put("totalmin",minEditText.getText().toString());
        params.put("totalmax",maxEditText.getText().toString());
        params.put("location",locationEditText.getText().toString());
        params.put("title",titleEditText.getText().toString());
        params.put("remark",contentEditText.getText().toString());
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
                                Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                                NewMatchDialog.this.dismiss();
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
