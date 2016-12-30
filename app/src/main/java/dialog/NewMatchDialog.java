package dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.ygh.graduation_practice.R;
import com.jude.utils.JUtils;

import network.RestApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import storage.CurrentUser;
import transfer.InformationTransfer;

public class NewMatchDialog extends DialogFragment {

    private EditText titleEditText;
    private EditText contentEditText;

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
        getDialog().setTitle(R.string.new_question);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        final View dialogView = inflater.inflate(R.layout.dialog_new_match, container);
        titleEditText = ((TextInputLayout) dialogView.findViewById(R.id.input_question_title)).getEditText();
        contentEditText = ((TextInputLayout) dialogView.findViewById(R.id.input_question_content)).getEditText();
        onViewStateRestored(savedInstanceState);
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
            JUtils.Toast(getResources().getString(R.string.title_cannot_be_empty));
            return;
        }
        Call<InformationTransfer> commitQuestionCall = RestApi.getHaruueKnowWebApiService().question(CurrentUser.getInstance().token, title, content);
        commitQuestionCall.enqueue(new Callback<InformationTransfer>() {
            @Override
            public void onResponse(Response<InformationTransfer> response) {
                if (response.code() == 200) {
                    JUtils.Toast(getResources().getString(R.string.commit_Match_success));
                    getDialog().dismiss();
                    ((NewMatchDialogCreater) getActivity()).getNewMatchDialogListener().onSuccess();
                } else {
                    JUtils.Toast(getResources().getString(R.string.fail_please_retry));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                JUtils.Toast(getResources().getString(R.string.network_error));
            }
        });
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
}
