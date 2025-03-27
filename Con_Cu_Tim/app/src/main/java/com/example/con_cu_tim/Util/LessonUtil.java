package com.example.con_cu_tim.Util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.con_cu_tim.DAO.QuestionDAO;
import com.example.con_cu_tim.Lesson.LessonFinishActivity;
import com.example.con_cu_tim.Lesson.ListenChoice.ListenChoiceActivity;
import com.example.con_cu_tim.Lesson.QuestionChoice.QuestionChoiceActivity;
import com.example.con_cu_tim.Lesson.Reading.ReadingActivity;
import com.example.con_cu_tim.Lesson.WordMatching.WordMatchingActivity;
import com.example.con_cu_tim.Model.Exercise;
import com.example.con_cu_tim.Model.Question;
import com.example.con_cu_tim.R;
import com.example.con_cu_tim.Units.ListUnits;

import java.util.ArrayList;

public class LessonUtil {

    private static ArrayList<Exercise> listExercise;
    private static ArrayList<Question> listQuestion;
    private static int currentExerciseNo,courseStudentId;
    public static void setListExercise(ArrayList<Exercise> listExercise) {
        LessonUtil.listExercise = listExercise;
    }

    public static int getCourseStudentId() {
        return courseStudentId;
    }

    public static void setCourseStudentId(int courseStudentId) {
        LessonUtil.courseStudentId = courseStudentId;
    }

    public static ArrayList<Question> getListQuestion() {
        return listQuestion;
    }

    public static void nextExercise(int exerciseNo, int curPoint,
                                    int totalPoint, int curProgress, Context c){
        int progressPercent = (int)100/listExercise.size();
        if (exerciseNo == listExercise.size()){
            Intent intent = new Intent(c, LessonFinishActivity.class);
            intent.putExtra("lessonId",listExercise.get(0).getLessonId());
            intent.putExtra("courseStudentId", getCourseStudentId());
            intent.putExtra("curPoint", curPoint);
            intent.putExtra("totalPoint", totalPoint);
            intent.putExtra("progressPercent", progressPercent);
            intent.putExtra("curProgress", curProgress);
            c.startActivity(intent);
        } else {
            Exercise currentEx = listExercise.get(exerciseNo);
            currentExerciseNo = exerciseNo;
            Log.i("next", "next type:" + currentEx.getTypeId());
            if(currentEx.getTypeId()==9){
                Intent intent = new Intent(c, WordMatchingActivity.class);
                intent.putExtra("exerciseNo", exerciseNo);
                intent.putExtra("progressPercent", progressPercent);
                intent.putExtra("curPoint", curPoint);
                intent.putExtra("totalPoint", totalPoint);
                intent.putExtra("curProgress", curProgress);
                intent.putExtra("exerciseId", currentEx.getId());
                c.startActivity(intent);
            }else{
                listQuestion = QuestionDAO.getInstance().getQuestionsByExId(currentEx.getId(),"STATUS>0");
                if(listQuestion.size()>0){
                    nextQuestion(0,curPoint, totalPoint, curProgress,c);
                }else{
                    nextExercise(exerciseNo+1,curPoint,totalPoint,0,c);
                }
            }
        }
    }

    public static void nextQuestion(int questionNo, int curPoint,
                                    int totalPoint, int curProgress, Context c){
        int progressPercent = (int)100/listQuestion.size();
        if (questionNo == listQuestion.size()){
            nextExercise(currentExerciseNo+1,curPoint,totalPoint,0,c);
        } else {
            Intent intent;
            switch (listExercise.get(currentExerciseNo).getTypeId()){
                case 7:
                    intent = new Intent(c, ReadingActivity.class);
                    break;
                case 8:
                    intent = new Intent(c, QuestionChoiceActivity.class);
                    break;
                case 10:
                    intent = new Intent(c, ListenChoiceActivity.class);
                    break;
                default:
                    intent = new Intent();
                    break;
            }
            intent.putExtra("progressPercent", progressPercent);
            intent.putExtra("curPoint", curPoint);
            intent.putExtra("totalPoint", totalPoint);
            intent.putExtra("curProgress", curProgress);
            intent.putExtra("quesNo", questionNo);
            Log.i("next", "quesL:"+listQuestion.size());
            c.startActivity(intent);
        }
    }

    // when click close icon or click go back in control
    public static void closeLesson(Context c, View main){
        View view = View.inflate(c, R.layout.lesson_close, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        PopupWindow popupWindow = new PopupWindow(view, width, height, false);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//        blur background layout
        main.getForeground().setAlpha(100);
        popupWindow.setOutsideTouchable(true);


        main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                main.getForeground().setAlpha(0);
                return true;
            }
        });

        Button cancelBtn = view.findViewById(R.id.button_cancel);
        Button quitBtn = view.findViewById(R.id.btn_quit);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                main.getForeground().setAlpha(0);
            }
        });

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TODO: quit lesson and go back to lesson list
                Toast.makeText(c, "quit lesson", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(view.getContext(), ListUnits.class);
                i.putExtra("id",listExercise.get(0).getLesson().getUnit().getCourseId());
                view.getContext().startActivity(i);
            }
        });

    }

    public static int getLoadedExerciseCount() {
        return listExercise == null ? 0 : listExercise.size();
    }
}
