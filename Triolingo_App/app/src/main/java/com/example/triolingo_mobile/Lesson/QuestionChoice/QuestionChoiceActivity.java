package com.example.triolingo_mobile.Lesson.QuestionChoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.triolingo_mobile.DAO.ExerciseDAO;
import com.example.triolingo_mobile.Lesson.ListenChoice.ListenChoiceActivity;
import com.example.triolingo_mobile.Model.AnswerModel;
import com.example.triolingo_mobile.Model.Exercise;
import com.example.triolingo_mobile.Model.Question;
import com.example.triolingo_mobile.R;
import com.example.triolingo_mobile.Util.LessonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class QuestionChoiceActivity extends AppCompatActivity {

    public int currentAnswer = -1;
    public int currentClick = -1;
    int curPoint = 0;
    int totalPoint = 0;
    public Button crt_btn;

//    public String ques = "Choose correct answer";
//    public String description = "The meaning of Potato in VN";
//    public List<AnswerModel> ansList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_question_choice);
        FrameLayout main = findViewById(R.id.main_layout);
        main.getForeground().setAlpha(0);

        ConstraintLayout header = (ConstraintLayout) findViewById(R.id.include);
        ProgressBar progressBar = header.findViewById(R.id.lesson_progressBar);

        Intent intent = getIntent();
        ExerciseDAO exDao = ExerciseDAO.getInstance();

        int quesNo = intent.getIntExtra("quesNo", -1);
        int progressPercent = intent.getIntExtra("progressPercent", -1);
        curPoint += intent.getIntExtra("curPoint", -1);
        totalPoint += intent.getIntExtra("totalPoint", -1);
        int curProgress = intent.getIntExtra("curProgress", -1);
        progressBar.setProgress(curProgress);
        Question question = LessonUtil.getListQuestion().get(quesNo);
        ArrayList<AnswerModel> ansList = exDao.getAnswerOfQuestion(question.getId(),"STATUS>0");
        Collections.shuffle(ansList);
        String ques = question.getQuestion1();
        totalPoint += question.getMark();

        while (ansList.size()<4){
            ansList.add(new AnswerModel(-1, -1, "", 1, false));
        }

        TextView textQues = findViewById(R.id.lesson_text_ques);
        String displayAns = ques + "\"";
        textQues.setText(displayAns);

        AnswerModel ans = exDao.getCorrectAnswer(ansList);
        String answer = ans.getAnswer();
        ArrayList<Button> listButton = new ArrayList<>();

        ImageView closeLesson = findViewById(R.id.lesson_close);
        closeLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LessonUtil.closeLesson(QuestionChoiceActivity.this, main);
            }
        });

        int i = 1;
        for (AnswerModel choice : ansList) {
            String idName = "lesson_ans_" + i;
            int id = getResources().getIdentifier(idName, "id", getPackageName());
            Button btn = (Button) findViewById(id);
            if(choice.getId()>0){
                listButton.add(btn);
                btn.setText(choice.getAnswer());
                if (choice.getId() == ans.getId()) crt_btn = btn;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // switch choice and change background
                        if (currentClick != -1 && currentClick != id) {
                            Button btnOld = (Button) findViewById(currentClick);
                            btnOld.setBackgroundTintList(ContextCompat.getColorStateList(QuestionChoiceActivity.this, R.color.white));
                        }
                        currentClick = id;
                        //set color for choose answer
                        Button btn = (Button) view;
                        btn.setBackgroundTintList(ContextCompat.getColorStateList(QuestionChoiceActivity.this, R.color.btn_ans_choice));
                        if (btn.getText().equals(answer)) {
                            currentAnswer = 1;
                        } else {
                            currentAnswer = 0;
                        }
                        Log.i("logQC", currentAnswer + "");

                        // check answer button
                        Button btnCheck = (Button) findViewById(R.id.button_check);
                        btnCheck.setClickable(true);
                        btnCheck.setTextColor(ContextCompat.getColorStateList(QuestionChoiceActivity.this, R.color.white));
                        btnCheck.setBackgroundTintList(ContextCompat.getColorStateList(QuestionChoiceActivity.this, R.color.correct_text));
                        btnCheck.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // lock view after check answer
                                lockButtons(listButton);
                                ConstraintLayout footer = (ConstraintLayout) findViewById(R.id.include2);
                                footer.setVisibility(View.GONE);
                                Button continueBtn;
//                            Toast.makeText(QuestionChoiceActivity.this, "click checks", Toast.LENGTH_SHORT).show();
                                if (currentAnswer == 1) {
                                    ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.answer_correct);
                                    cl.setVisibility(View.VISIBLE);
                                    continueBtn = findViewById(R.id.lesson_btn_continue1);
                                    curPoint += question.getMark();
                                } else {
                                    ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.answer_incorrect);
                                    cl.setVisibility(View.VISIBLE);
                                    continueBtn = findViewById(R.id.lesson_btn_continue0);
                                    crt_btn.setBackgroundTintList(ContextCompat.getColorStateList(QuestionChoiceActivity.this, R.color.correct_ans));
                                    btn.setBackgroundTintList(ContextCompat.getColorStateList(QuestionChoiceActivity.this, R.color.incorrect_ans));
                                }
                                progressBar.setProgress(curProgress + progressPercent);
                                continueBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        LessonUtil.nextQuestion(quesNo+1, curPoint,
                                                totalPoint,curProgress + progressPercent,
                                                QuestionChoiceActivity.this);
                                    }
                                });
                            }
                        });
                    }
                });
            }else{
                btn.setVisibility(View.GONE);
            }
            i++;
        }
    }

    public void lockButtons(@NonNull ArrayList<Button> listButton) {
        for (Button btn : listButton) {
            btn.setClickable(false);
        }
    }


}