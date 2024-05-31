package com.dicarodev.dicarobank.view;

import static java.lang.Thread.sleep;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dicarodev.dicarobank.R;
import com.dicarodev.dicarobank.api.DicaroBankApiAdapter;
import com.dicarodev.dicarobank.databinding.ActivityLoginBinding;
import com.dicarodev.dicarobank.model.appUser.LogInRequestDto;
import com.dicarodev.dicarobank.model.appUser.LogInResponseDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private ActivityResultLauncher<String> requestReadContactsPermissionLauncher; // Permisos de lectura de contactos
    private EditText etUserDni;
    private EditText etPassword;
    private LogInResponseDto userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeLuncherPermission(); //Define el launcher para el permiso de lectura de contactos
        requestReadPermission(); // Llama al launcher para solicitar el permiso de lectura de contactos
    }

    @Override
    protected void onStart() {
        super.onStart();
        etUserDni = binding.userDniEt;
        etPassword = binding.passwordEt;

        Button loginBtn = binding.loginBtn;
        loginBtn.setOnClickListener(v -> logInRequest());

        TextView tvSingup = binding.singupTv;
        tvSingup.setOnClickListener(v -> goSingUpActivity());
    }

    private void logInRequest() {
        String userDni = etUserDni.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        LogInRequestDto logInRequestDto = new LogInRequestDto(userDni, password);

        Call<LogInResponseDto> logInResponse = DicaroBankApiAdapter.getApiService().login(logInRequestDto);

        logInResponse.enqueue(new Callback<LogInResponseDto>() {
            @Override
            public void onResponse(Call<LogInResponseDto> call, Response<LogInResponseDto> response) {
                if (response.isSuccessful()) {
                    userAuth = response.body();
                    saveToken(userAuth.getToken()); // Guarda el JWT en las SharedPreferences

                    try {
                        sleep(500);
                        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                        mainIntent.putExtra("userName", userAuth.getName());
                        startActivity(mainIntent);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    handleApiError(response);
                    etUserDni.setText("");
                    etUserDni.requestFocus();
                    etPassword.setText("");
                }
            }

            @Override
            public void onFailure(Call<LogInResponseDto> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Error al resolver la petición", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveToken(String token) {
        SharedPreferences preferences = getSharedPreferences("myPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("token", token);
        editor.apply();
    }

    private void goSingUpActivity() {
        Intent singupIntent = new Intent(this, SingupActivity.class);
        startActivity(singupIntent);
    }

    private void handleApiError(Response<?> response) {
        try {
            JSONObject errorBody = new JSONObject(response.errorBody().string()); // Extraer el error del cuerpo de la respuesta como un JSON
            String errorMessage = errorBody.getString("message");
            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrectos.", Toast.LENGTH_LONG).show();
        }
    }

    // Inicializa el launcher para solicitar permisos de lectura de contactos
    private void initializeLuncherPermission() {
        requestReadContactsPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isReadContactsGranted -> {
            if (isReadContactsGranted) {
                // Si se otorgan los permisos, solicitar la información de contactos
                requestReadPermission();
            } else {
                Log.d("ContactsFragment", "Permission denied, cannot display application content");
            }
        });
    }

    // Metodo para verificar y solicitar permisos de lectura de contactos si no se tienen
    private void requestReadPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestReadContactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS);
        }
    }
}