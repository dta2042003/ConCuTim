package com.example.triolingo_mobile.Lesson.Writting;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.triolingo_mobile.R;

public class WrittingActivity extends AppCompatActivity {

    private EditText edtAnswer;
    private Button btnSubmit;
    private TextView txtQuestion, txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writting);

        // Initialize views
        edtAnswer = findViewById(R.id.edt_answer);
        btnSubmit = findViewById(R.id.btn_submit);
        txtQuestion = findViewById(R.id.txt_question);
        txtResult = findViewById(R.id.txt_result);

        // Set click listener for the button
        btnSubmit.setOnClickListener(v -> {
            String answer = edtAnswer.getText().toString().trim();
            if (answer.equalsIgnoreCase("Paris")) {
                txtResult.setText("✅ Correct!");
                txtResult.setTextColor(getResources().getColor(R.color.green, null));
            } else {
                txtResult.setText("❌ Incorrect. Try again!");
                txtResult.setTextColor(getResources().getColor(R.color.red, null));
            }
        });
    }
}
