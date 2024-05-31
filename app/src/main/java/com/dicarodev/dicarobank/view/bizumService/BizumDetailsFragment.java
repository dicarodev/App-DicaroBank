package com.dicarodev.dicarobank.view.bizumService;

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

import com.dicarodev.dicarobank.R;
import com.dicarodev.dicarobank.api.DicaroBankApiAdapter;
import com.dicarodev.dicarobank.databinding.FragmentBizumDetailsBinding;
import com.dicarodev.dicarobank.model.bizum.Bizum;
import com.dicarodev.dicarobank.model.bizum.BizumDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BizumDetailsFragment extends Fragment {

    private FragmentBizumDetailsBinding binding;
    private NavController navController;
    private Bizum bizum;
    private TextView tvDetailAmount, tvDetailMessage, tvDetailContact;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBizumDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);

        if (getArguments() != null) {
            bizum = (Bizum) getArguments().getSerializable("bizum");
        }

        tvDetailAmount = binding.bizumDetailAmountTv;
        tvDetailMessage = binding.bizumDetailMessageTv;
        tvDetailContact = binding.bizumDetailContactTv;
        setDetails();

        String token = "Bearer " + getToken();

        binding.sendBizumBtn.setOnClickListener(v -> issueBizum(token));
    }

    private void setDetails() {
        tvDetailAmount.setText(getString(R.string.bizumDetails_amount, String.valueOf(bizum.getAmount())));
        tvDetailMessage.setText(getString(R.string.bizumDetails_message, bizum.getMessage()));
        tvDetailContact.setText(bizum.getContact().getName());
    }

    private void issueBizum(String token) {

        if (bizum != null) {
            double amount = bizum.getAmount();
            String detail = bizum.getMessage().isEmpty() ? "Bizum" : "Bizum: " + bizum.getMessage();
            BizumDto bizumDto = new BizumDto(amount, detail);
            issueBizumRequest(token, bizumDto);
        }
    }

    private String getToken() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("myPreferences", MODE_PRIVATE);
        return preferences.getString("token", "No hay JWT");
    }

    private void issueBizumRequest(String token, BizumDto bizumDto) {

        Call<Void> issueBizumResponse = DicaroBankApiAdapter.getApiService().issueBizum(token, bizumDto);

        issueBizumResponse.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    try {
                        Toast.makeText(getContext(), "Enviado!", Toast.LENGTH_SHORT).show();
                        sleep(2000);

                        navController.navigate(R.id.nav_globalPosition);
                    } catch (InterruptedException e) {
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