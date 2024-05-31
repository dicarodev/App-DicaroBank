package com.dicarodev.dicarobank.view.globalPosition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dicarodev.dicarobank.R;
import com.dicarodev.dicarobank.databinding.FragmentTransactionDetailsBinding;
import com.dicarodev.dicarobank.model.account.AccountUserDto;
import com.dicarodev.dicarobank.model.transaction.TransactionDto;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransactionDetailsFragment extends Fragment {
    private FragmentTransactionDetailsBinding binding;
    private TransactionDto transactionSelected;
    private AccountUserDto userAccountDto;
    private TextView tvTransactionDate, tvTransactionDetail, tvTransactionAmount, tvOriginAccount, tvDestinyAccount;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTransactionDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        tvTransactionDate = binding.transDetailsDateTv;
        tvTransactionAmount = binding.transDetailsAmountTv;
        tvTransactionDetail = binding.transDetailsDetailTv;
        tvOriginAccount = binding.originAccountTv;
        tvDestinyAccount = binding.destinyAccountTv;

        if (getArguments() != null) {
            transactionSelected = (TransactionDto) getArguments().getSerializable("transactionSelected");
            userAccountDto = (AccountUserDto) getArguments().getSerializable("userAccount");
        }

        if (transactionSelected != null && userAccountDto != null) {
            configureDetails(transactionSelected, userAccountDto);
        } else {
            // Manejo de errores en caso de que los argumentos sean nulos
            Toast.makeText(getContext(), "Error: Datos de transacción no disponibles", Toast.LENGTH_SHORT).show();
        }

    }

    private void configureDetails(TransactionDto transactionDto, AccountUserDto userAccountDto) {
        String formattedDate = formatDate(transactionDto.getTransactionDate());
        String formattedAmount = transactionDto.getOriginAccountNumber().equals(userAccountDto.getAccountNumber()) ?
                formatAmount(transactionDto.getAmount() * -1) :
                formatAmount(transactionDto.getAmount());

        tvTransactionDate.setText(getString(R.string.transactionDetail_date, formattedDate));
        tvTransactionAmount.setText(getString(R.string.transactionDetail_amount, formattedAmount));
        tvTransactionDetail.setText(getString(R.string.transactionDetail_detail, transactionDto.getDetail()));

        tvOriginAccount.setText(getString(R.string.transactionDetail_originAccount, transactionDto.getOriginAccountNumber()));
        tvDestinyAccount.setText(getString(R.string.transactionDetail_destinyAccount, transactionDto.getDestinyAccountNumber()));
    }

    private String formatDate(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM HH:mm", Locale.getDefault());
        try {
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString; // En caso de error, devuelve la cadena original
        }
    }

    private String formatAmount(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(amount);
    }
}
