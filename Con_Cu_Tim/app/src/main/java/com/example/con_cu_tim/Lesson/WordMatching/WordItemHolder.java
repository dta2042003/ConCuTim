package com.example.con_cu_tim.Lesson.WordMatching;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.con_cu_tim.R;

public class WordItemHolder extends RecyclerView.ViewHolder {
    private Button btn;
    private String word;
    private Drawable normal, correct, selected;

    public void setOnClick(View.OnClickListener listener) {
        btn.setOnClickListener(listener);
    }
    public void SetWord(String word) {
        this.word = word;
        btn.setText(word);
    }
    public String getWord() {
        return this.word;
    }

    public WordItemHolder(@NonNull View itemView) {
        super(itemView);
        btn = itemView.findViewById(R.id.word_match_btn);
        normal = ContextCompat.getDrawable(itemView.getContext(), R.drawable.custom_word_btn_normal);
        selected = ContextCompat.getDrawable(itemView.getContext(), R.drawable.custom_word_btn_selected);
        correct = ContextCompat.getDrawable(itemView.getContext(), R.drawable.custom_word_btn_correct);
    }

    public void setButtonBg(int cd) {
        switch(cd) {
            case -1:
            case -2:
                btn.setBackground(normal);
                btn.setEnabled(false);
                break;
            case 0:
                btn.setBackground(selected);
                btn.setClickable(false);
                break;
            case 1:
                btn.setBackground(correct);
                btn.setClickable(false);
                break;
        }
    }

    public void UnSelect() {
        btn.setBackground(normal);
        btn.setEnabled(true);
        btn.setClickable(true);
    }

    public boolean isClickable() {
        return btn != null && btn.isClickable();
    }
}

interface OnWordItemClick {
    int onItemClickListener(WordItemHolder holder);
}
