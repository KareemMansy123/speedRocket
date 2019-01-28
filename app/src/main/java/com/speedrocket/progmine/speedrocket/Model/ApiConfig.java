package com.speedrocket.progmine.speedrocket.Model;




import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ApiConfig
{


    @FormUrlEncoded
    @POST("websevSaveToken")
    Call <ResultModel>saveToken(
            @Field("userId") int userId,
            @Field("token") String Token
    );

    @FormUrlEncoded
    @POST("websevSendSms")
    Call <ResultModel>needVerificationCode(
            @Field("mobile") String mobileNumber

    );

    @FormUrlEncoded
    @POST("websevCheckSms")
    Call <ResultModel>verifyCode(
            @Field("code") String code,
            @Field("mobile") String mobile
    );


    @Multipart
    @POST("websevUpdateUserNew")
    Call <ServerResponse>registerRestOFData(
            @Part("image") RequestBody name,
            @Part MultipartBody.Part file,
            @Part("id") int id,
            @Part("firstName") String firstName ,
            @Part("email") String email ,
            @Part("password") String password ,
            @Part("interest") String interst
    );

    @FormUrlEncoded
    @POST("websevUpdateUserWithoutImage")
    Call <ServerResponse>registerRestOFData(
            @Field("id") int id,
            @Field("firstName") String firstName ,
            @Field("email") String email ,
            @Field("password") String password ,
            @Field("interest") String interst
    );

    @Multipart
    @POST("websevUpdateImageUsers")
    Call <ServerResponse>uploadFile(
            @Part("file") RequestBody name,
            @Part MultipartBody.Part file,
            @Part("id") Integer id
    ); // name and file ----> contain Image

    @FormUrlEncoded
    @POST("websevRegistrationNew")
    Call <ResultModel>registrationNew(
            @Field("mobile") String mobileNumber
    );


    @GET("WebServTrader")
    Call<ResultModel> getTraderPages();

    @Multipart
    @POST("websevAddCompany")
    Call <ServerResponse>uploadFileAndCompanyProfile(
            @Part("file") RequestBody name,
            @Part MultipartBody.Part file,
            @Part("en_name") String en_name,
            @Part("ar_name") String ar_name,
            @Part("email") String email,
            @Part("country") String country,
            @Part("categoryId") int categoryId,
            @Part("city") String city,
            @Part("phone") String phone,
            @Part("fax") String fax,
            @Part("userId") int userId
    );

    @Multipart
    @POST("websevUpdateCompany")
    Call <ServerResponse>uploadFileAndUpdateCompanyProfile(
            @Part("file") RequestBody name,
            @Part MultipartBody.Part file,
            @Part("id") Integer id,
            @Part("en_name") String en_name,
            @Part("ar_name") String ar_name,
            @Part("email") String email,
            @Part("country") String country,
            @Part("categoryId") Integer categoryId,
            @Part("city") String city,
            @Part("phone") String phone,
            @Part("fax") String fax,
            @Part("userId") Integer userId
    );

    @Multipart
    @POST("WebServBankTransfer/store")
    Call<ServerResponse> uploadBncTransport(
            @Part("userId") int userID,
            @Part("receipt_Image") RequestBody name,
            @Part MultipartBody.Part file,
            @Part("bankId") int bankId,
            @Part("date") String date,
            @Part("amount") double amount,
            @Part("refNum") String refNum,
            @Part("status") int stats
    );


    @FormUrlEncoded
    @POST("websevUpdateCompany")
    Call <ServerResponse>uploadFileAndUpdateCompanyProfile2(
            @Field("id") int id,
            @Field("en_name") String en_name,
            @Field("ar_name") String ar_name,
            @Field("email") String email,
            @Field("country") String country,
            @Field("categoryId") int categoryId,
            @Field("city") String city,
            @Field("phone") String phone,
            @Field("fax") String fax,
            @Field("userId") int userId
    );


    @Multipart
    @POST("websevAddProduct")
    Call <ServerResponse>uploadFileAndProductData(
            @Part("file") RequestBody name,
            @Part MultipartBody.Part file,
            @Part("en_title") String en_title,
            @Part("ar_title") String ar_title,
            @Part("en_description") String en_description,
            @Part("ar_description") String ar_description,
            @Part("price") String price,
            @Part("qty") int productQty,
            @Part("companyId") int companyId,
            @Part("categoryId") int categoryid

    );


    @FormUrlEncoded
    @POST("websevUpdateProduct")
    Call <ServerResponse>uploadFileAndProductData1(
            @Field("id") int id,
            @Field("en_title") String en_title,
            @Field("ar_title") String ar_title,
            @Field("en_description") String en_description,
            @Field("ar_description") String ar_description,
            @Field("price") String price,
            @Field("qty") int productQty,
            @Field("companyId") int companyId,
            @Field("categoryId") int categoryId
    );

    @FormUrlEncoded
    @POST("WebServOrders/get_orders")
    Call <ResultModel>getCompanyOrders(@Field("companyId") int id);

    @Multipart
    @POST("websevUpdateProduct")
    Call <ServerResponse>uploadFileAndProductData2(
            @Part("file") RequestBody name,
            @Part MultipartBody.Part file,
            @Part("id") Integer id,
            @Part("en_title") String en_title,
            @Part("ar_title") String ar_title,
            @Part("en_description") String en_description,
            @Part("ar_description") String ar_description,
            @Part("price") String price,
            @Part("companyId") Integer companyId,
            @Part("categoryId") Integer categoryId
         //   @Query("page") int pageNumber //<K.M>
    );



    @FormUrlEncoded
    @POST("websevAddOffers")
    Call <ServerResponse>uploadFileAndOfferData(
            @Field("startTime") String startTime,
            @Field("endTime") String endTime,
            @Field("discount") String discount,
            @Field("productId") String ProductId,
            @Field("packageId") int packageId,
            @Field("companyId") int companyId,
            @Field("refNumber") String refNumber,
            @Field("price") double price

    );

      @GET("websevgetMaxAdvertiseId")
      Call<ResultModel> getMaxAdvertiseOrderId(
      );  //  updateProfileImage

    @Multipart
    @POST("websevAddeOfferImage")
    Call <ServerResponse>uploadImagesWithOffer(
            @Part("file") RequestBody name,
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST("websevProductsImages")
    Call <ServerResponse>uploadImagewithProduct(
            @Part("file") RequestBody name,
            @Part MultipartBody.Part file,
            @Part("productId") int productId
    );

    @Multipart
    @POST("websevUpdateProductsGalleries")
    Call <ServerResponse>updateImagewithProduct(
            @Part("file") RequestBody name,
            @Part MultipartBody.Part file,
            @Part("productId") int productId
    );

    @FormUrlEncoded
    @POST("websevDeleteProductsGalleries")
    Call<ResultModel> deleteProductImage(
            @Field("productId") int productId
    );  //  updateProfileImage


    @GET("websevGetAllCountries")
    Call<ResultModel> getCountriesAndPrices(
    );  //  updateProfileImage

    @GET("GetMaxId")
    Call<ResultModel> getMaxOrderId(
    );  //  updateProfileImage

    @Multipart
    @POST("websevUpdateOffer")
    Call <ServerResponse>uploadFileAndOfferData1(
            @Field("id") int id,
            @Field("en_title") String en_title,
            @Field("ar_title") String ar_title,
            @Field("en_description") String en_description,
            @Field("ar_description") String ar_description,
            @Field("price") String price,
            @Field("type") String type,
            @Field("startTime") String startTime,
            @Field("endTime") String endTime,
            @Field("discount") String discount,
            @Field("count") String count,
            @Field("srcoin") String srcoin,
            @Field("minutes") int minutes,
            @Field("country") int country,
            @Field("companyId") int companyId
    );


    @Multipart
    @POST("websevUpdateOffer")
    Call <ServerResponse>uploadFileAndOfferData2(
            @Field("id") int id,
            @Part("file") RequestBody name,
            @Part MultipartBody.Part file,
            @Part("en_title") String en_title,
            @Part("ar_title") String ar_title,
            @Part("en_description") String en_description,
            @Part("ar_description") String ar_description,
            @Part("price") String price,
            @Part("type") String type,
            @Part("startTime") String startTime,
            @Part("endTime") String endTime,
            @Part("discount") String discount,
            @Part("count") String count,
            @Part("srcoin") String srcoin,
            @Part("minutes") int minutes,
            @Part("country") int country,
            @Part("companyId") int companyId

    );
}
