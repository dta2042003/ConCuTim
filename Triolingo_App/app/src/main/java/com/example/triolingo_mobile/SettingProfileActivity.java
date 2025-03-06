package com.example.triolingo_mobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triolingo_mobile.DAO.UserDAO;
import com.example.triolingo_mobile.Model.UserEntity;
import com.example.triolingo_mobile.Model.UserModel;
import com.google.gson.Gson;

//import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingProfileActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    TextView password;
    TextView rePassword;
    TextView name;
    TextView email;
    Bitmap imageBitmap;
    private CircleImageView imageView;
    public UserEntity us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);
        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String json = sharedPref.getString("user", null);
        int userId = 0;
        if (json != null) {
            Gson gson = new Gson();
            UserEntity userLogin = gson.fromJson(json, UserEntity.class);
            userId = userLogin.getId();
        }
        us = UserDAO.getInstance().GetUserById(userId);
        email = findViewById(R.id.editTextTextEmailAddress);
        email.setText(us.getEmail());
        name = findViewById(R.id.editTextTextPersonName);
        name.setText(us.getFullNamel());
        password = findViewById(R.id.editTextTextPassword2);
        password.setText(us.getPassword());
        rePassword = findViewById(R.id.editTextTextPassword);
        findViewById(R.id.btnClose).setOnClickListener(this::onQuit);
        findViewById(R.id.btnSave).setOnClickListener(this::onSave);
        imageView = findViewById(R.id.imageview_account_profile);
        Bitmap b = convertBase64ToBitMap();
        imageView.setImageBitmap(b);
        findViewById(R.id.floatingActionButton).setOnClickListener(this::onEditImage);
    }

    public void onEditImage(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingProfileActivity.this);
        builder.setTitle("Chọn ảnh từ");
        builder.setItems(new CharSequence[]{"Chụp ảnh", "Chọn ảnh"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                        break;
                    case 1:
                        Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent2, REQUEST_IMAGE_PICK);
                        break;
                }
            }
        });
        builder.show();
    }

    String convertToBase64(Bitmap bm) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String avatarUrl = Base64.encodeToString(byteArray, Base64.NO_WRAP);
            return avatarUrl;
        } catch (Exception ex) {
            Log.e("Error:", ex.getMessage());
        }
        return null;
    }

    public Bitmap convertBase64ToBitMap() {
        try {
            String base64String = us.getAvatarUrl();
//            String base64Image = base64String.split(",")[1];
            byte[] decodedString = Base64.decode(base64String, 0);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return decodedByte;
        } catch (Exception ex) {
            Log.e("Error:", ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    imageBitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(imageBitmap);
                    break;
                case REQUEST_IMAGE_PICK:
                    Uri imageUri = data.getData();
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    imageView.setImageURI(imageUri);
                    break;
            }
        }
    }

    public void onQuit(View view) {
        Intent account = new Intent(SettingProfileActivity.this, AccountActivity.class);
        startActivity(account);
    }

    public void onSave(View view) {
        if (!password.getText().toString().equals(rePassword.getText().toString())) {
            rePassword.setError(getString(R.string.repass_not_match_pass));
            return;
        }
        String avtURL = convertToBase64(imageBitmap);
        us.setAvatarUrl(avtURL);
        us.setPassword(password.getText().toString());
        us.setFullNamel(name.getText().toString());
        us.setEmail(email.getText().toString());
        int n = UserDAO.getInstance().udpateUser(us);
        if (n > 0) {
            Toast.makeText(this, getString(R.string.update_profile_user_sucess), Toast.LENGTH_SHORT).show();
            onQuit(view);
        } else {
            name.setError(getString(R.string.update_profile_user_fail));
        }
    }
}