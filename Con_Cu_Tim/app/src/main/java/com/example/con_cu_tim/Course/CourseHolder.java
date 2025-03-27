package com.example.con_cu_tim.Course;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.con_cu_tim.DAO.StudentCourseDAO;
import com.example.con_cu_tim.Model.Course;
import com.example.con_cu_tim.Model.UserEntity;
import com.example.con_cu_tim.R;
import com.example.con_cu_tim.Units.ListUnits;
import com.google.gson.Gson;

public class CourseHolder extends RecyclerView.ViewHolder {
    private CardView card;
    private TextView edit_id;
    private TextView edit_name;
    private TextView edit_rate;
    private Context context;
    private Course course;
    private Button btn;

    public CourseHolder(@NonNull View itemView,Context context) {
        super(itemView);
        this.context = context;
    }

    private void bindingAction(View itemView) {
        card.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        if(course.isAssign()==false){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setTitle("Đăng ký khóa học");
            builder.setMessage("Bạn có chắc sẽ đăng ký khóa học "+course.getName()+" không?");
            builder.setPositiveButton("Tôi đồng ý",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                            int userId = 2;
                            String json = sharedPref.getString("user", null);
                            if (json != null) {
                                Gson gson = new Gson();
                                UserEntity userLogin = gson.fromJson(json, UserEntity.class);
                                userId = userLogin.getId();
                            }
                            if(StudentCourseDAO.getInstance().assignStudentToCourse(userId,course.getId())){
                                Intent intent = new  Intent(itemView.getContext(), ListUnits.class);
                                intent.putExtra("id", course.getId());
                                itemView.getContext().startActivity(intent);
                            }
                        }
                    });
            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            Intent intent = new  Intent(itemView.getContext(), ListUnits.class);
            intent.putExtra("id", course.getId());
            itemView.getContext().startActivity(intent);
        }
    }

    public void onClickGuide(View v) {
        Intent intent = new  Intent(itemView.getContext(), CourseDescriptionActivity.class);
        intent.putExtra("id", course.getId());
        intent.putExtra("name", course.getName());
        intent.putExtra("isAssigned", course.isAssign());
        intent.putExtra("description", course.getDescription());
        itemView.getContext().startActivity(intent);
    }
    private void bindingView(View itemView) {
        if(course.isAssign()){
            edit_id = itemView.findViewById(R.id.course_id);
            edit_name = itemView.findViewById(R.id.course_name);
            edit_rate = itemView.findViewById(R.id.course_rate);
            card = itemView.findViewById(R.id.unit_card_view);
            btn = itemView.findViewById(R.id.btn_backToAccount);
            card.setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.unit_card_view_not).setVisibility(View.GONE);
        }else{
            edit_id = itemView.findViewById(R.id.course_id_not);
            edit_name = itemView.findViewById(R.id.course_name_not);
            edit_rate = itemView.findViewById(R.id.course_rate_not);
            card = itemView.findViewById(R.id.unit_card_view_not);
            btn = itemView.findViewById(R.id.course_desc_not);
            card.setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.unit_card_view).setVisibility(View.GONE);
        }
    }

    public void setView(@NonNull Course cour) {
        course = cour;
        bindingView(itemView);
        bindingAction(itemView);
        edit_id.setText(cour.getId()+"");
        edit_name.setText(cour.getName()+"");
        edit_rate.setText(cour.getRateAverage()+"");
        if (cour==null||cour.getDescription() == null || cour.getDescription().equals("") || cour.getDescription().trim().equals("")){
            btn.setVisibility(View.INVISIBLE);
        }else{
            btn.setOnClickListener(this::onClickGuide);
        }
    }
}
