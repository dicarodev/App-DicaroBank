package com.dicarodev.dicarobank.view.globalPosition;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Thread.sleep;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicarodev.dicarobank.R;
import com.dicarodev.dicarobank.adapter.TransactionsRecyclerViewAdapter;
import com.dicarodev.dicarobank.api.DicaroBankApiAdapter;
import com.dicarodev.dicarobank.databinding.FragmentGlobalPositionBinding;
import com.dicarodev.dicarobank.model.account.AccountUserDto;
import com.dicarodev.dicarobank.model.transaction.TransactionDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GlobalPositionFragment extends Fragment {

    private FragmentGlobalPositionBinding binding;
    private NavController navController;
    private TextView tvAccountNumber;
    private TextView tvAccountBalance;
    private AccountUserDto userAccountDto;
    private RecyclerView rvTransactions;
    private List<TransactionDto> transactionDtoList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGlobalPositionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);

        tvAccountNumber = binding.accountNumberTv;
        tvAccountBalance = binding.accountBalanceTv;
        rvTransactions = binding.transactionsRv;

        String token = "Bearer " + getToken();

        transactionDtoList.clear(); // Limpiar la lista de transacciones
        getUserAccount(token); // Obtener la cuenta del usuario y sus transacciones
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Obtener el token del usuario guardado en SharedPreferences
    private String getToken() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("myPreferences", MODE_PRIVATE);
        return preferences.getString("token", "No hay JWT");
    }

    private void getUserAccount(String token) {
        Call<AccountUserDto> accountUserDtoCall = DicaroBankApiAdapter.getApiService().getAccount(token);

        accountUserDtoCall.enqueue(new Callback<AccountUserDto>() {
            @Override
            public void onResponse(Call<AccountUserDto> call, Response<AccountUserDto> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        userAccountDto = response.body();
                        tvAccountNumber.setText(userAccountDto.getAccountNumber());
                        tvAccountBalance.setText(userAccountDto.getBalance() +" €");

                        getOutgoingTransactions(token); // Obtener las transacciones salientes de la cuenta
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

    private void getOutgoingTransactions(String token) {
        Call<List<TransactionDto>> outgoingTransactionsCall = DicaroBankApiAdapter.getApiService().getOutgoingTransactions(token);

        outgoingTransactionsCall.enqueue(new Callback<List<TransactionDto>>() {
            @Override
            public void onResponse(Call<List<TransactionDto>> call, Response<List<TransactionDto>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        transactionDtoList.addAll(response.body());
                        getIncomingTransactions(token);
                    }
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<List<TransactionDto>> call, Throwable throwable) {
                throwable.printStackTrace();
                Toast.makeText(getContext(), "Error al resolver la petición Transacciones", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getIncomingTransactions(String token) {
        Call<List<TransactionDto>> incomingTransactionsCall = DicaroBankApiAdapter.getApiService().getIncomingTransactions(token);

        incomingTransactionsCall.enqueue(new Callback<List<TransactionDto>>() {
            @Override
            public void onResponse(Call<List<TransactionDto>> call, Response<List<TransactionDto>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        transactionDtoList.addAll(response.body());
                        configureTransactionsRV(transactionDtoList, userAccountDto);
                    }
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<List<TransactionDto>> call, Throwable throwable) {
                throwable.printStackTrace();
                Toast.makeText(getContext(), "Error al resolver la petición Transacciones", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void configureTransactionsRV(List<TransactionDto> transactionDtoList, AccountUserDto userAccountDto) {
        transactionDtoList.sort(Comparator.comparing(TransactionDto::getTransactionDate).reversed());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvTransactions.setLayoutManager(layoutManager);
        TransactionsRecyclerViewAdapter transactionsRecyclerViewAdapter = new TransactionsRecyclerViewAdapter(transactionDtoList, userAccountDto, navController);
        rvTransactions.setAdapter(transactionsRecyclerViewAdapter);
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
}