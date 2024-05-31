package com.dicarodev.dicarobank.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.dicarodev.dicarobank.R;
import com.dicarodev.dicarobank.model.account.AccountUserDto;
import com.dicarodev.dicarobank.model.transaction.TransactionDto;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionsRecyclerViewAdapter extends RecyclerView.Adapter<TransactionsRecyclerViewAdapter.ViewHolder> {

    private List<TransactionDto> transactionDtoList;
    private AccountUserDto userAccountDto;
    private NavController navController;
    public TransactionsRecyclerViewAdapter(List<TransactionDto> transactionDtoList, AccountUserDto userAccountDto, NavController navController) {
        this.transactionDtoList = transactionDtoList;
        this.userAccountDto = userAccountDto;
        this.navController = navController;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTransactionDate, tvTransactionDetail, tvTransactionAmount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTransactionDate = itemView.findViewById(R.id.transactionDate_tv);
            tvTransactionDetail = itemView.findViewById(R.id.transactionDetail_et);
            tvTransactionAmount = itemView.findViewById(R.id.transactionAmount_tv);
        }
    }

    @NonNull
    @Override
    public TransactionsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_transaction, parent, false);
        return new TransactionsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsRecyclerViewAdapter.ViewHolder holder, int position) {
        TransactionDto transactionDto = transactionDtoList.get(position);
        // Formatear la fecha
        String formattedDate = formatDate(transactionDto.getTransactionDate());
        String formattedAmount;

        // Formatear la cantidad
        formattedAmount = transactionDto.getOriginAccountNumber().equals(userAccountDto.getAccountNumber()) ?
                formatAmount(transactionDto.getAmount() * -1) :
                formatAmount(transactionDto.getAmount());

        holder.tvTransactionDate.setText(formattedDate);
        holder.tvTransactionDetail.setText(transactionDto.getDetail());
        holder.tvTransactionAmount.setText(formattedAmount + " €");

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("transactionSelected", transactionDto);
            bundle.putSerializable("userAccount", userAccountDto);
            navController.navigate(R.id.nav_transactionDetails, bundle);

        });
    }

    @Override
    public int getItemCount() {
        return transactionDtoList.size();
    }


    private String formatDate(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM", Locale.getDefault());
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
