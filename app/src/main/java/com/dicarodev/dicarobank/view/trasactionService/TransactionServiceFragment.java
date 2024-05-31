package com.dicarodev.dicarobank.view.trasactionService;

import static android.content.Context.MODE_PRIVATE;

import static java.lang.Thread.sleep;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dicarodev.dicarobank.R;
import com.dicarodev.dicarobank.api.DicaroBankApiAdapter;
import com.dicarodev.dicarobank.databinding.FragmentTransactionServiceBinding;
import com.dicarodev.dicarobank.model.account.AccountUserDto;
import com.dicarodev.dicarobank.model.transaction.IssueTansactionDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionServiceFragment extends Fragment {

    private FragmentTransactionServiceBinding binding;
    private AccountUserDto userAccountDto;
    private TextView tvOriginAccountNumber, tvAvailableBalance;
    private EditText etDestinyAccountNumber, etDestinyUserName, etDestinyUserSurname, etDestinyTransactionDetail, etDestinyTransactionAmount;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTransactionServiceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        tvOriginAccountNumber = binding.originAccountNumberTv;
        tvAvailableBalance = binding.availableAmountTv;
        etDestinyAccountNumber = binding.destinyAccountNumberEt;
        etDestinyUserName = binding.destinyUserNameEt;
        etDestinyUserSurname = binding.destinyUserSurnameEt;
        etDestinyTransactionDetail = binding.transactionDetailEt;
        etDestinyTransactionAmount = binding.destinyAmountEt;
        Button btnIssueTransaction = binding.issueTransactionBtn;

        clearFields();

        String token = "Bearer " + getToken();

        getUserAccountRequest(token);

        btnIssueTransaction.setOnClickListener(v -> issueTransaction(token));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private String getToken() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("myPreferences", MODE_PRIVATE);
        return preferences.getString("token", "No hay JWT");
    }

    private String formatAmount(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(amount);
    }

    public void configureUserAccountDetails(AccountUserDto userAccountDto) {
        if (userAccountDto != null) {
            tvOriginAccountNumber.setText(getString(R.string.transactionService_origin_account_number, userAccountDto.getAccountNumber()));
            tvAvailableBalance.setText(getString(R.string.transactionService_origin_account_balance, formatAmount(userAccountDto.getBalance())));
        }
    }

    private void issueTransaction(String token) {

        String destinyAccountNumber = etDestinyAccountNumber.getText().toString().trim();
        String destinyTransactionDetail = etDestinyTransactionDetail.getText().toString().trim();
        String destinyTransactionAmount = etDestinyTransactionAmount.getText().toString().trim();
        
        if (validateTransaction(destinyAccountNumber, destinyTransactionDetail, destinyTransactionAmount)) {
            double amount = Double.parseDouble(destinyTransactionAmount);
            IssueTansactionDto issueTransactionDto = new IssueTansactionDto(amount, destinyTransactionDetail, destinyAccountNumber);
            issueTransactionRequest(token, issueTransactionDto);
        }
    }
    
    private boolean validateTransaction(String destinyAccountNumber, String destinyTransactionDetail, String destinyTransactionAmount) {
        boolean isValid = true;

        if (destinyAccountNumber.isEmpty()) {
            etDestinyAccountNumber.setError(getString(R.string.required_field));
            isValid = false;
        }
        
        if (destinyTransactionDetail.isEmpty()) {
            etDestinyTransactionDetail.setError(getString(R.string.required_field));
            isValid = false;
        }
        
        if (destinyTransactionAmount.isEmpty()) {
            etDestinyTransactionAmount.setError(getString(R.string.required_field));
            isValid = false;
        }
        
        return isValid;
    }

    private void clearFields() {
        etDestinyAccountNumber.setText("");
        etDestinyUserName.setText("");
        etDestinyUserSurname.setText("");
        etDestinyTransactionDetail.setText("");
        etDestinyTransactionAmount.setText("");
    }

    private void handleApiError(Response<?> response) {
        try {
            JSONObject errorBody = new JSONObject(response.errorBody().string()); // Extraer el error del cuerpo de la respuesta como un JSON
            String errorMessage = errorBody.getString("message");
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ha habido un error, intentelo más tarde.", Toast.LENGTH_LONG).show();
        }
    }

    private void getUserAccountRequest(String token) {
        Call<AccountUserDto> accountUserDtoCall = DicaroBankApiAdapter.getApiService().getAccount(token);

        accountUserDtoCall.enqueue(new Callback<AccountUserDto>() {
            @Override
            public void onResponse(Call<AccountUserDto> call, Response<AccountUserDto> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        userAccountDto = response.body();
                        configureUserAccountDetails(userAccountDto);
                    }

                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<AccountUserDto> call, Throwable throwable) {
                Toast.makeText(getContext(), "Error al resolver la petición Cuenta", Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void issueTransactionRequest(String token, IssueTansactionDto issueTransactionDto) {

        Call<Void> issueTransactionResponse = DicaroBankApiAdapter.getApiService().issueTransaction(token, issueTransactionDto);
        
        issueTransactionResponse.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    try {
                        clearFields();
                        Toast.makeText(getContext(), "Transacción exitosa", Toast.LENGTH_LONG).show();

                        sleep(2000);

                        getUserAccountRequest(token); // Actualizar el estado de la cuenta
                    }catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Toast.makeText(getContext(), "Error al resolver la petición Transacción", Toast.LENGTH_LONG).show();
            }
        });
    }
}