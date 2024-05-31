package com.dicarodev.dicarobank.api;

import com.dicarodev.dicarobank.model.account.AccountUserDto;
import com.dicarodev.dicarobank.model.bizum.BizumDto;
import com.dicarodev.dicarobank.model.transaction.IssueTansactionDto;
import com.dicarodev.dicarobank.model.appUser.SingUpAppUserDto;
import com.dicarodev.dicarobank.model.transaction.TransactionDto;
import com.dicarodev.dicarobank.model.appUser.LogInRequestDto;
import com.dicarodev.dicarobank.model.appUser.LogInResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/login")
    Call<LogInResponseDto> login (@Body LogInRequestDto logInRequestDto);

    @POST("auth/singup")
    Call<Void> singup (@Body SingUpAppUserDto singUpAppUserDto);

    @GET("account/user")
    Call<AccountUserDto> getAccount (@Header("Authorization") String token);

    @GET("transaction/outgoing")
    Call<List<TransactionDto>> getOutgoingTransactions(@Header("Authorization") String token);

    @GET("transaction/incoming")
    Call<List<TransactionDto>> getIncomingTransactions(@Header("Authorization") String token);

    @POST("transaction/issue/transaction")
    Call<Void> issueTransaction(@Header("Authorization") String token, @Body IssueTansactionDto issueTansactionDto);

    @POST("transaction/issue/bizum")
    Call<Void> issueBizum(@Header("Authorization") String token, @Body BizumDto bizumDto);
}
