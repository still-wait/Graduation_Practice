package ui.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ygh.graduation_practice.MainActivity;
import com.example.ygh.graduation_practice.R;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.utils.JUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import provider.AnswerListProvider;
import storage.CurrentQuestion;
import transfer.AnswerTransfer;
import ui.adapter.AnswerAdapter;
import ui.adapter.GetFace;

public class AnswerFragment extends Fragment implements AnswerListProvider.AnswerListDemander, MainActivity.CommitSuccessCallBack {

    View view;
    EasyRecyclerView recyclerView;
    AnswerAdapter adapter;
    AnswerTransfer currentAnswer;
    int currentPosition = -1;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_answer, container, false);
        // RecyclerView
        recyclerView = (EasyRecyclerView) view.findViewById(R.id.recycler_view_answer);
        recyclerView.setAdapter(adapter = new AnswerAdapter(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setRefreshListener(new Listener());
        recyclerView.showProgress();
        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });
        adapter.setMore(R.layout.view_more, new Listener());
        adapter.setNoMore(R.layout.view_no_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });
        adapter.setOnItemClickListener(new Listener());
        // Long Press Menu
        registerForContextMenu(recyclerView);
        adapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemClick(int position) {
                currentAnswer = adapter.getItem(position);
                return false;
            }
        });
        // Question Header
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View header = getLayoutInflater(savedInstanceState).inflate(R.layout.header_question, parent, false);
                ((TextView) header.findViewById(R.id.question_author_name)).setText(CurrentQuestion.getInstance().authorName);
                ((TextView) header.findViewById(R.id.question_date)).setText(CurrentQuestion.getInstance().date);
                ((TextView) header.findViewById(R.id.question_title)).setText(CurrentQuestion.getInstance().title);
                ((TextView) header.findViewById(R.id.question_content)).setText(CurrentQuestion.getInstance().content);
                // Set face
                new GetFace((CircleImageView) header.findViewById(R.id.question_author_face), CurrentQuestion.getInstance().authorFace).load();
                return header;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
        if (null == savedInstanceState) {
            adapter.clear();
            AnswerListProvider.getInstance(AnswerFragment.this).refresh();
        }
        return view;
    }

    @Override
    public AnswerListProvider.AnswerListLoadListener getAnswerListLoadListener() {
        return new AnswerListProvider.AnswerListLoadListener() {
            @Override
            public void onSuccess(ArrayList<AnswerTransfer> diff) {
                adapter.addAll(diff);
                adapter.notifyDataSetChanged();
                recyclerView.showRecycler();
            }

            @Override
            public void onEmpty(int onPage) {
                adapter.stopMore();
                if (onPage == 0) {
                    recyclerView.showRecycler();
                }
            }

            @Override
            public void onFailure() {
                adapter.pauseMore();
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentPosition != -1) {
            recyclerView.scrollToPosition(currentPosition);
        }
        recyclerView.showRecycler();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle(currentAnswer.content);
        menu.add(0, 0, 0, R.string.copy_answer);
        menu.add(0, 1, 0, R.string.back_to_top);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                JUtils.copyToClipboard(currentAnswer.content);
                break;
            case 1:
                recyclerView.scrollToPosition(0);
        }
        return true;
    }

    @Override
    public void onCommitSuccess() {
        new Listener().onRefresh();
    }

    private class Listener implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener, RecyclerArrayAdapter.OnItemClickListener {

        @Override
        public void onRefresh() {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.clear();
                    AnswerListProvider.getInstance(AnswerFragment.this).refresh();
                }
            }, 500);
        }

        @Override
        public void onItemClick(int position) {
            rippleAnimation(position);
        }

        @Override
        public void onLoadMore() {
            AnswerListProvider.getInstance(AnswerFragment.this).loadNextPage();
        }

        // Ripple 动画
        void rippleAnimation(int position) {
            final View view = AnswerFragment.this.view.findViewById((int) adapter.getItemId(position + 1));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.animate().translationZ(15F).setDuration(300).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            view.animate().translationZ(1f).setDuration(500).start();
                        }
                    }
                }).start();
            }
        }
    }
}
