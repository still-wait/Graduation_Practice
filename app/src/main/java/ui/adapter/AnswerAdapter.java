package ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ygh.graduation_practice.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import de.hdodenhof.circleimageview.CircleImageView;
import transfer.AnswerTransfer;

public class AnswerAdapter extends RecyclerArrayAdapter<AnswerTransfer> {

    public AnswerAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new AnswerViewHolder(parent);
    }

    public class AnswerViewHolder extends BaseViewHolder<AnswerTransfer> {

        private TextView authorName;
        private TextView content;
        private TextView date;
        private CircleImageView authorFace;

        public AnswerViewHolder(ViewGroup itemView) {
            super(itemView, R.layout.item_answer);
            authorName = $(R.id.answer_author_name);
            content = $(R.id.answer_content);
            date = $(R.id.answer_date);
            authorFace = $(R.id.answer_author_face);
        }

        @Override
        public void setData(AnswerTransfer data) {
            authorName.setText(data.authorName);
            content.setText(data.content);
            date.setText(data.date);
            // Set face
            new GetFace(authorFace, data.authorFace).load();
        }
    }
}
