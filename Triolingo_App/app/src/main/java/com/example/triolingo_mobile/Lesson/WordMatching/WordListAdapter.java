package com.example.triolingo_mobile.Lesson.WordMatching;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.triolingo_mobile.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordItemHolder> {
    private HashMap<String, Integer> wordMap;
    private OnWordItemClick _listener;
    private WordItemHolder oldSelected;
    private List<WordItemHolder> _cache;
    private boolean isAnswer;

    public WordListAdapter(HashMap<String, Integer> wordMap, OnWordItemClick listener, boolean isAnswer) {
        List<String> shuffleList = new ArrayList<>(wordMap.keySet());
        Collections.shuffle(shuffleList);
        this.wordMap = new HashMap<>();
        for (String word : shuffleList) {
            this.wordMap.put(word, wordMap.get(word));
        }
        this._listener = listener;
        this._cache = new ArrayList<>();
        oldSelected = null;
        this.isAnswer = isAnswer;
    }

    @NonNull
    @Override
    public WordItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_word_matching_row, parent, false);
        WordItemHolder viewHolder = new WordItemHolder(v);
        _cache.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WordItemHolder holder, int position) {
        String word = new ArrayList<>(wordMap.keySet()).get(position);
        holder.SetWord(word);
        holder.setOnClick(v -> {
            int returnCd = _listener.onItemClickListener(holder);
            if (returnCd == 0 && oldSelected != null) {
                // unselect any selected one
                oldSelected.UnSelect();
                oldSelected = holder;
            }
            else if (returnCd != 0) {
                oldSelected = null;
            }
            else {
                oldSelected = holder;
            }
            if (isAnswer && returnCd == -1) {
                holder.UnSelect();
            } else {
                holder.setButtonBg(returnCd);
            }
        });
    }

    public void FinalizeLastSelected(boolean isCorrect) {
        if (oldSelected != null && isCorrect) {
            oldSelected.setButtonBg(1);
        }
        else if (oldSelected != null && isAnswer) {
            oldSelected.UnSelect();
        }
        else if (oldSelected != null) {
            oldSelected.setButtonBg(-1);
        }
        oldSelected = null;
    }

    @Override
    public int getItemCount() {
        return wordMap.size();
    }

    void MarkAllIncorrect() {
        for (WordItemHolder item : _cache) {
            if (item != null && item.isClickable()) {
                item.setButtonBg(-1);
            }
        }
    }

    public void SetLastQuestionWrong() {
        if (isAnswer || oldSelected == null) {
            return;
        }
        oldSelected.setButtonBg(-1);
    }
}
