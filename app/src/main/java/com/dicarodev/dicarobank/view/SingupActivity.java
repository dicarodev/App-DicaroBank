package com.dicarodev.dicarobank.view;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dicarodev.dicarobank.R;
import com.dicarodev.dicarobank.api.DicaroBankApiAdapter;
import com.dicarodev.dicarobank.databinding.ActivitySingupBinding;
import com.dicarodev.dicarobank.model.appUser.SingUpAppUserDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingupActivity extends AppCompatActivity {
    private ActivitySingupBinding binding;
    private EditText etUserDni;
    private EditText etUserName;
    private EditText etUserSurname;
    private EditText etUserPhone;
    private EditText etUserEmail;
    private EditText etUserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySingupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.singup_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ImageButton arrowBackBtn = binding.arrowBackBtn;
        arrowBackBtn.setBackground(null);
        arrowBackBtn.setOnClickListener(v -> finish());

        etUserDni = binding.userDniSingupEt;
        etUserName = binding.userNameEt;
        etUserSurname = binding.userSurnameEt;
        etUserPhone = binding.userPhoneEt;
        etUserEmail = binding.userEmailEt;
        etUserPassword = binding.userPasswordEt;

        Button singupBtn = binding.singupBtn;
        singupBtn.setOnClickListener(v -> singUpUser());
    }

    private void singUpUser() {
        String userDni = etUserDni.getText().toString().trim();
        String userName = etUserName.getText().toString().trim();
        String userSurname = etUserSurname.getText().toString().trim();
        String userPhone = etUserPhone.getText().toString().trim();
        String userEmail = etUserEmail.getText().toString().trim();
        String userPassword = etUserPassword.getText().toString().trim();

        if (validateUser(userDni, userName, userSurname, userPhone, userEmail, userPassword)) {
            SingUpAppUserDto newSingupUserDto = new SingUpAppUserDto(userDni, userName, userSurname, userPhone, userEmail, userPassword);
            singUpRequest(newSingupUserDto);
        }

    }

    private void singUpRequest(SingUpAppUserDto singUpAppUserDto) {

        Call<Void> singUpResponse = DicaroBankApiAdapter.getApiService().singup(singUpAppUserDto);

        singUpResponse.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    try {
                        Toast.makeText(getApplicationContext(), "Le damos la bienvenida a Dicaro Bank.", Toast.LENGTH_LONG).show();

                        sleep(3000);

                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                } else {

                    try {
                        JSONObject errorBody = new JSONObject(response.errorBody().string()); // Extraer el cuerpo de la respuesta como un JSON
                        String errorMessage = errorBody.getString("message");
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();

                        sleep(3000);

                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(loginIntent);
                        finish();

                    } catch (JSONException | IOException | InterruptedException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Ha habido un error, intentelo más tarde.", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Error al resolver la petición", Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean validateUserDni(String userDni) {
        String dniLetters = "TRWAGMYFPDXBNJZSQVHLCKE";
        String regexDni = "(^\\d{8}[A-Z&&[^IÑOU]])";

        if (userDni.isEmpty()) {
            etUserDni.setError(getString(R.string.required_field));
            return false;
        } else if (!userDni.matches(regexDni)) {
            etUserDni.setText("");
            etUserDni.setError(getString(R.string.dni_pattern));
            return false;
        } else {
            char inputLetter = userDni.charAt(userDni.length() - 1);
            char validLetter = dniLetters.charAt(Integer.parseInt(userDni.substring(0, userDni.length() - 1)) % 23);

            if (inputLetter != validLetter) {
                etUserDni.setText("");
                etUserDni.setError(getString(R.string.dni_no_valid));
                return false;
            }
        }
        return true;
    }

    private boolean validateUser(String userDni, String userName, String userSurname, String userPhone, String userEmail, String userPassword) {
        boolean isValid = true;
        String regexPhone = "(^\\d{9})";

        if (!validateUserDni(userDni)) {
            isValid = false;
        }

        if (userName.isEmpty()) {
            etUserName.setError(getString(R.string.required_field));
            isValid = false;
        }

        if (userSurname.isEmpty()) {
            etUserSurname.setError(getString(R.string.required_field));
            isValid = false;
        }

        if (userPhone.isEmpty()) {
            etUserPhone.setError(getString(R.string.required_field));
            isValid = false;
        } else if(!userPhone.matches(regexPhone)) {
            etUserPhone.setText("");
            etUserPhone.setError(getString(R.string.phone_pattern));
            isValid = false;
        }

        if (userEmail.isEmpty()) {
            etUserEmail.setError(getString(R.string.required_field));
            isValid = false;
        }

        if (userPassword.isEmpty()) {
            etUserPassword.setError(getString(R.string.required_field));
            isValid = false;
        }

        return isValid;
    }
}