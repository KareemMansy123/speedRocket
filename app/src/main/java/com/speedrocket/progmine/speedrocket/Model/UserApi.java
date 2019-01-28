package com.speedrocket.progmine.speedrocket.Model;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;



public interface UserApi {

    @GET("users")
    Call<List<contentSearchAdapter>>getContent();

    @GET("websevUsers")
    Call<ResultModel> getPersonalusers();   // view user data

    @FormUrlEncoded
    @POST("websevUserDetails")
    Call<ResultModel> getProfileAccount(@Field("id") int companyId);

    @FormUrlEncoded
    @POST("websevgetRate")
    Call<ResultModel> rateCompany(@Field("companyId") int companyId,
                                  @Field("userId") int userId,
                                  @Field("rate") float rate
    );

    @FormUrlEncoded
    @POST("websevRate")
    Call<ResultModel> getCompanyRate(@Field("companyId") int id);

    @FormUrlEncoded
    @POST("websevUsersOrders")
    Call<ResultModel> getmyOrders(@Field("userId") int id);

    @FormUrlEncoded
    @POST("websevGetProductById")
    Call<ResultModel> getproductById(@Field("id") int id);


    @FormUrlEncoded
    @POST("websevCompanyDetails")
    Call<ResultModel> getCompanyAccount(@Field("id") int id);

    @FormUrlEncoded
    @POST("websevOffersDetails")
    Call<ResultModel> getOfferDetails(@Field("id") int id, @Field("userId") int userId);

    @FormUrlEncoded
    @POST("websevGetProductDetails")
    Call<ResultModel> getProductDetails(@Field("id") int id);


    @GET("websevOffersNew")
    Call<ResultModel> getOffers(@Query("userId") int userId);

    @GET("websevGetusers")
    Call<ResultModel> getAllUsers() ;

    @GET("websevAllProducts")
    Call<ResultModel> getProducts();

    @FormUrlEncoded
    @POST("webServeSubmitLogin")
    Call<ResultModel> getTokens(@Field("email") String email,
                                @Field("password") String password ,
                                @Field("userId") int userId ,
                                @Field("check") int check);


    @GET("test")
    Call<ResultModel> teest();


    @FormUrlEncoded
    @POST("websevOffersInterestNew")
    Call<ResultModel> getOffersToUserInterest(@Field("interest") String interest,
                                              @Field("FromNav") int check,
                                              @Field("userId") int userId);
                                                //<K.M>
                                              //<K.M>
                                              //<K.M>
                                              //<K.M>
                                              //<K.M>
                                              //<K.M>
                                               // @Query("pageNumber") int pagenumber);


    @FormUrlEncoded
    @POST("websevGetOfferByCompany")
    Call<ResultModel> getOffersToCompanyProfile(@Field("id") int id);

    @FormUrlEncoded
    @POST("websevGetProductByCompany")
    Call<ResultModel> getProductsToCompanyProfile(@Field("id") int id);

    @GET("websevGetUsersByAllOffer")
    Call<ResultModel> getUsersByOffers();

    @GET("StoreUser")
    Call<ResultModel> getErrors();

    @FormUrlEncoded
    @POST("websevRegistration")
    Call<ResultModel> adduser(@Field("firstName") String firstName,
                              @Field("lastName") String lastName,
                              @Field("email") String email,
                              @Field("password") String password,
                              @Field("gender") String gender,
                              @Field("language") String language,
                              @Field("interest") String interest,
                              @Field("mobile") String mobile,
                              @Field("city") String city,
                              @Field("country") String country
    );  // user Registration


    @FormUrlEncoded
    @POST("websevLogin")
    Call<ResultModel> loginuser(
            @Field("email") String email, @Field("password") String password
            ,@Field("check") int check
    );

    @FormUrlEncoded
    @POST("iscompanyConfirmed")
    Call<ResultModel> isConfirmed(
            @Field("userId") int userId
    );

    @FormUrlEncoded
    @POST("isPasswordConfirmed")
    Call<ResultModel> isPasswordConfirmed(
            @Field("email") String email,
            @Field("password") String password,
            @Field("newPassword") String newPassword,
            @Field("userId") int userId
    );

    @FormUrlEncoded
    @POST("websevUserDetails")
    Call<ResultModel> getaccountbyid(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("websevGetUsersByIdOffer")
    Call<ResultModel> getEnteredOnOffer(
            @Field("offersId") int id
    );

    @Multipart
    @POST("webserrupdateUser")
    Call<ResultModel> uploadFile(@Part MultipartBody.Part file,
                                 @Part("file") RequestBody name
    );

    @FormUrlEncoded
    @POST("websevTypes")
    Call<ResultModel> getUsersInterest(
            @Field("lang") String lang
    );


    @FormUrlEncoded
    @POST("websevStoreUserOffer")
    Call<ResultModel> enterOffer(@Field("userId") int userId,
                                 @Field("offersId") int offersId,
                                 @Field("srCoin") int srCoin);


    @GET("websevCategories")
    Call<ResultModel> getCategory();


    @FormUrlEncoded
    @POST("websevUserupdateCoins")
    Call<ResultModel> updateUserCoin(@Field("id") int id,
                                     @Field("coins") int coins
    );  //  updateUserCoin

   @FormUrlEncoded
   @POST("websevgetUserCoins")
   Call<ResultModel> getUserCoins(@Field("id") int id);

    @FormUrlEncoded
    @POST("WebServClosed")
    Call<ResultModel> test(@Field("id") int id

    );  // updateUserCoin

    @FormUrlEncoded
    @POST("websevStoreWinner")
    Call<ResultModel> addProductOnWinners(@Field("userId") int userId,
                                          @Field("offerId") int offerId,
                                          @Field("srCoin") int winnerCoin
    );  //  addProductOnWinners

    @FormUrlEncoded
    @POST("websevWinnerUser")
    Call<ResultModel> getProductWinnersByUser(@Field("userId") int id);


    @FormUrlEncoded
    @POST("websevofferView")
    Call<ResultModel> counterViews(@Field("id") int id);

    @FormUrlEncoded
    @POST("websevUpdateImageUsers")
    Call<ResultModel> updateProfileImage(@Field("id") int id,
                                         @Field("image") String image
    );  //  updateProfileImage



    @Multipart
    @POST("websevUpdateImageUsers")
    Call<ResponseBody> postImage(@Part MultipartBody.Part file,
                                 @Part("file") RequestBody name
    );

    @Multipart
    @POST("WebServOfficalDocs/store")
    Call<ResponseBody> saveImages(@Part MultipartBody.Part file1,
                                  @Part("file1") RequestBody name1,
                                  @Part MultipartBody.Part file2,
                                  @Part("file2") RequestBody nam2e,
                                  @Part("companyId") int companyId,
                                  @Part("userId") int userId

    );
    //   @Part("name") RequestBody name,
    //@Part MultipartBody.Part image


    @FormUrlEncoded
    @POST("websevUpdateUser")
    Call<ResultModel> updateUser(@Field("id") int id,
                                 @Field("firstName") String firstName,
                                 @Field("lastName") String lastName,
                                 @Field("email") String email,
                                 @Field("gender") String gender,
                                 @Field("language") String language,
                                 @Field("interest") String interest,
                                 @Field("mobile") String mobile,
                                 @Field("city") String city,
                                 @Field("country") String country

    );  //  updateUserCoin






    @FormUrlEncoded
    @POST("websevProductsByCategory")
    Call<ResultModel> chooseCategory(
            @Field("categoryId") int categoryId
    );  //  updateProfileImage

    @FormUrlEncoded
    @POST("websevCompanies")
    Call<ResultModel> getCompany(@Field("userId") int id);

    @FormUrlEncoded
    @POST("websevCategoriesBySubCategory")
    Call<ResultModel> getCategoryById(@Field("id") int id);

    @FormUrlEncoded
    @POST("websevCompanyMessage")
    Call<ResultModel> companyMessage(@Field("title") String title,
                                     @Field("companyId") int companyId,
                                     @Field("userId") int userId,
                                     @Field("message") String message
    );//send message



    @FormUrlEncoded
    @POST("websevgetCompanyMessage")
    Call<ResultModel> getCompanyMessages(@Field("id") int id);

    @FormUrlEncoded
    @POST("websevsetSeen")
    Call<ResultModel> setSeen(@Field("id") int id);

    @FormUrlEncoded
    @POST("WebServContact")
    Call<ResultModel> contactUs(@Field("name") String name,
                                @Field("email") String email,
                                @Field("message") String message
    );  //  updateUserCoin

    @FormUrlEncoded
    @POST("websevDeleteCompany")
    Call<ResultModel> deleteCompany(@Field("id") int id);
    @FormUrlEncoded
    @POST("websevDeleteProduct")
    Call<ResultModel> deleteProduct(@Field("id") int id);
    @FormUrlEncoded
    @POST("websevDeleteOffer")
    Call<ResultModel> deleteOffer(@Field("id") int id);

    @FormUrlEncoded
    @POST("websevUpdateCompany")
    Call<ResultModel> updateCompany(@Field("id") int id);




    @FormUrlEncoded
    @POST("websevBanksOfUser")
    Call<ResultModel> getBankAccountWithUserId(@Field("userId") int userId);


    @GET("WebServRocketBanks/getAllBanks")
    Call<ResultModel> getAllSpeedRocketBanks();


    @FormUrlEncoded
    @POST("websevCashRequest")
    Call<ResultModel> saveRequest(@Field("userId") int userId,
                                  @Field("bankId") int bankId,
                                  @Field("amount") int amount
    );


    @FormUrlEncoded
    @POST("websevStoreBanks")
    Call<ResultModel> addBankAccount(
            @Field("userId") int userId,
            @Field("name") String name,
            @Field("bankAccount") String bankAccount,
            @Field("bankAddress") String bankAddress,
            @Field("swift") String swift

    );

    @FormUrlEncoded
    @POST("websevDeleteBank")
    Call<ResultModel> deleteBankAccount(@Field("id") int id);

    @FormUrlEncoded
    @POST("WebServStoreOrder")
    Call<ResultModel> orderCashOnDelivery(@Field("offerId") int offerId,
                                          @Field("userId") int userId,
                                          @Field("price") int price,
                                          @Field("type") int type,
                                          @Field("name") String name,
                                          @Field("phone") String phone,
                                          @Field("address") String address,
                                          @Field("code") String offerCode
    );

    @FormUrlEncoded
    @POST("WebServStoreOrder")
    Call<ResultModel> orderCashOnDelivery(@Field("offerId") int offerId,
                                          @Field("userId") int userId,
                                          @Field("price") int price,
                                          @Field("type") int type,
                                          @Field("name") String name,
                                          @Field("phone") String phone,
                                          @Field("address") String address

    );

    @FormUrlEncoded
    @POST("WebServStoreOrder")
    Call<ResultModel> orderproduct(@Field("productId") int offerId,
                                   @Field("userId") int userId,
                                   @Field("price") int price,
                                   @Field("type") int type,
                                   @Field("name") String name,
                                   @Field("phone") String phone,
                                   @Field("address") String address,
                                   @Field("code") String productCode
    );


    @FormUrlEncoded
    @POST("WebServStoreOrder")
    Call<ResultModel> orderCashOnDelivery(@Field("productsId") String offerId,
                                          @Field("userId") int userId,
                                          @Field("price") String price,
                                          @Field("type") String type,
                                          @Field("amunts") String amunts,
                                          @Field("shippingCost") double shippingCost,
                                          @Field("name") String name,
                                          @Field("phone") String phone,
                                          @Field("address") String address,

                                          @Field("code") String productCode
    );


    @FormUrlEncoded
    @POST("WebServNetCash")
    Call<ResultModel> getMyCash(@Field("id") int id);


    @GET("WebServStorePriceCoin")
    Call<ResultModel> getCoins();

    @FormUrlEncoded
    @POST("WebServStoreOrderCoins")
    Call<ResultModel> setCoinsOrder(@Field("userId") int id, @Field("coins") int coins, @Field("code") String orderCode);

    @FormUrlEncoded
    @POST("WebServCashDaily")
    Call<ResultModel> getMyCashDay(@Field("id") int id);


    @FormUrlEncoded
    @POST("WebServCashMonth")
    Call<ResultModel> getMyCashMonth(@Field("id") int id);

    @POST("websevStoreUserOffer")
    @FormUrlEncoded
    Call<ResultModel> setUserOffer(@Field("userId") int uId, @Field("offersId") int oId, @Field("lastTenSec") int lastTenSec);

    @POST("websevStoreUserOfferTest")
    @FormUrlEncoded
    Call<ResultModel> setUserOfferTest(@Field("userId") int uId, @Field("offersId") int oId, @Field("lastTenSec") int lastTenSec);


    @POST("WebServBasket/store")
    @FormUrlEncoded
    Call<ResultModel> setProductToBasket(@Field("product_id") int productId,
                                         @Field("type") int type,
                                         @Field("shopper_id") int userId);



    @GET("savetempraryOrder")
    Call<ResultModel> setShoppertrack(@Query("product_id") String productId,
                                      @Query("type") String type,
                                      @Query("shopper_id") int userId,
                                      @Query("coinsQuantity") int qty,
                                      @Query("coinsPrice") double price,
                                      @Query("refNumber") String refnum,
                                      @Query("type_letter") String type_letter,
                                      @Query("item_qty") String itemQty,
                                      @Query("shippingCost") double shippingCost,
                                      @Query("shopper_address") String adress,
                                      @Query("shopper_name") String name,
                                      @Query("shopper_phone") String phone
    );

    @GET("removeShopperTrack")
    Call<ResultModel> removeTrack(
            @Query("id") int removableId

    );

    @POST("WebServBasket/delete_from_basket")
    @FormUrlEncoded
    Call<ResultModel> deleteProductFromBasket(@Field("basket_id") int productId,
                                              @Field("shopper_id") int userId);



    @POST("WebServBasket/get_basket")
    @FormUrlEncoded
    Call<ResultModel> getbasketProduct(@Field("shopper_id") int userId);


} // UserApi
