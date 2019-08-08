package com.komsi.lab.kjur.api;

import com.komsi.lab.kjur.model.AddCartResponse;
import com.komsi.lab.kjur.model.CartListResponse;
import com.komsi.lab.kjur.model.ChangePassUserResponse;
import com.komsi.lab.kjur.model.CreateOrderResponse;
import com.komsi.lab.kjur.model.DetailUserResponse;
import com.komsi.lab.kjur.model.EditUserResponse;
import com.komsi.lab.kjur.model.ForgotPasswordResponse;
import com.komsi.lab.kjur.model.GamapayResponse;
import com.komsi.lab.kjur.model.LocationListResponse;
import com.komsi.lab.kjur.model.LoginResponse;
import com.komsi.lab.kjur.model.LogoutResponse;
import com.komsi.lab.kjur.model.OrderCPaymentListResponse;
import com.komsi.lab.kjur.model.OrderDetailResponse;
import com.komsi.lab.kjur.model.OrderHistoryListResponse;
import com.komsi.lab.kjur.model.OrderWPaymentListResponse;
import com.komsi.lab.kjur.model.PayConfirmResponse;
import com.komsi.lab.kjur.model.PayMethodListResponse;
import com.komsi.lab.kjur.model.ProductDetailResponse;
import com.komsi.lab.kjur.model.ProductListResponse;
import com.komsi.lab.kjur.model.RemoveAllCartResponse;
import com.komsi.lab.kjur.model.RemoveItemCartResponse;
import com.komsi.lab.kjur.model.ResendVerifyEmailResponse;
import com.komsi.lab.kjur.model.SignUpResponse;
import com.komsi.lab.kjur.model.StoresListResponse;
import com.komsi.lab.kjur.model.VerifyEmailResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BaseApiService {

    @FormUrlEncoded
    @POST("register")
    Call<SignUpResponse> registerUser(
            @Header("Accept") String accept,
            @Field("name") String name,
            @Field("email") String email,
            @Field("no_telepon") String telepon,
            @Field("status") String status,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> loginUser(
            @Field("email") String email,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("forgot-password")
    Call<ForgotPasswordResponse> fpUser(
            @Header("Accept") String accept,
            @Field("email") String email);

    @GET("detail")
    Call<DetailUserResponse> detailUser(
            @Header("Accept") String accept,
            @Header("Authorization") String authToken);

    @FormUrlEncoded
    @POST("email/verify")
    Call<VerifyEmailResponse> verifyUser(
            @Header("Authorization") String authToken,
            @Field("verification_code") String verifyCode);

    @GET("email/resend")
    Call<ResendVerifyEmailResponse> resendVerifyUser(
            @Header("Authorization") String authToken);

    @GET("logout")
    Call<LogoutResponse> logoutUser(
            @Header("Authorization") String authToken);

    @GET("lokasi")
    Call<LocationListResponse> locStore(
            @Header("Authorization") String authToken);

    @GET("toko/{id}")
    Call<StoresListResponse> storeList(
            @Header("Authorization") String authToken,
            @Path("id") int id);

    @GET("toko/{id}/stok-barang")
    Call<ProductListResponse> productList(
            @Header("Authorization") String authToken,
            @Path("id") String id);

    @GET("barang/detail/{id}")
    Call<ProductDetailResponse> productDetail(
            @Header("Authorization") String authToken,
            @Path("id") String id);

    @FormUrlEncoded
    @POST("keranjang/create/{id}")
    Call<AddCartResponse> addCart(
            @Header("Authorization") String authToken,
            @Path("id") String id,
            @Field("kuantitas") int kuantitas);

    @GET("keranjang")
    Call<CartListResponse> cartList(
            @Header("Authorization") String authToken);

    @DELETE("keranjang/delete")
    Call<RemoveAllCartResponse> removeAll(
            @Header("Authorization") String authToken);

    @DELETE("keranjang/delete/{id}")
    Call<RemoveItemCartResponse> removeItem(
            @Header("Authorization") String authToken,
            @Path("id") int id);

    @FormUrlEncoded
    @POST("transaksi/create")
    Call<CreateOrderResponse> createOrder(
            @Header("Authorization") String authToken,
            @Field("jumlah_bayar") int jumlahBayar);

    @GET("jenis-pembayaran")
    Call<PayMethodListResponse> paymentList(
            @Header("Authorization") String authToken);

    @FormUrlEncoded
    @PUT("transaksi/pay/{id}")
    Call<PayConfirmResponse> paymentConfirm(
            @Header("Authorization") String authToken,
            @Path("id") String id,
            @Field("jenis_pembayaran_id") int jenisBayarId);

    @GET("transaksi/success/{id}")
    Call<PayConfirmResponse> paymentSuccess(
            @Header("Authorization") String authToken,
            @Path("id") String id);

    @GET("transaksi/failed/{id}")
    Call<PayConfirmResponse> paymentFailed(
            @Header("Authorization") String authToken,
            @Path("id") String id);

    @GET("transaksi/riwayat")
    Call<OrderHistoryListResponse> orderHistory(
            @Header("Authorization") String authToken);

    @GET("transaksi/{id}")
    Call<OrderDetailResponse> orderDetail(
            @Header("Authorization") String authToken,
            @Path("id") String id);

    @GET("transaksi/pilih-metode")
    Call<OrderCPaymentListResponse> paymentChoose(
            @Header("Authorization") String authToken);

    @GET("transaksi/waiting")
    Call<OrderWPaymentListResponse> paymentWaiting(
            @Header("Authorization") String authToken);

    @GET("saldo/riwayat")
    Call<GamapayResponse> gamapay(
            @Header("Authorization") String authToken);

    @FormUrlEncoded
    @PUT("edit")
    Call<EditUserResponse> editUser(
            @Header("Authorization") String authToken,
            @Field("email") String email,
            @Field("no_telepon") String phone,
            @Field("tanggal_lahir") String birthDate);

    @FormUrlEncoded
    @PUT("edit-password")
    Call<ChangePassUserResponse> changePassUser(
            @Header("Authorization") String authToken,
            @Field("password_current") String passwordCurrent,
            @Field("password") String passwordNew,
            @Field("password_confirmation") String passwordNewConfirm);
}
