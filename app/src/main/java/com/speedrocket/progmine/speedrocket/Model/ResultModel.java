package com.speedrocket.progmine.speedrocket.Model;

import java.util.List;



public class ResultModel
{

  //  private List<PersonalUser> users ;  // same name of table josn
   private  List<Testact> allUsers ;

    public List<Testact> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<Testact> allUsers) {
        this.allUsers = allUsers;
    }

    private List<offerId> offersId ;
    private  List <Offer> offers;

    private List<TraderPages> traders ;

    public List<TraderPages> getTraders() {
        return traders;
    }

    public void setTraders(List<TraderPages> traders) {
        this.traders = traders;
    }

    String todayDate ;
    String token ;

    int code ;

    int userId ;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }

    private List<CompanyMessage> companyMessages ;

    public List<CompanyMessage> getCompanyMessages() {
        return companyMessages;
    }

    public void setCompanyMessages(List<CompanyMessage> companyMessages) {
        this.companyMessages = companyMessages;
    }

    private float rate ;
     private boolean unSeen ;

    public boolean isUnSeen() {
        return unSeen;
    }

    public void setUnSeen(boolean unSeen) {
        this.unSeen = unSeen;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    private  List <BasketItem> basketItems;

    List<String> coutries ;
    List<String> prices ;
    List<SpeedRocketBanks> speedBanks ;

    boolean confirmed ;

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public List<SpeedRocketBanks> getSpeedBanks() {
        return speedBanks;
    }

    public void setSpeedBanks(List<SpeedRocketBanks> speedBanks) {
        this.speedBanks = speedBanks;
    }

    public List<String> getCoutries() {
        return coutries;
    }

    public void setCoutries(List<String> coutries) {
        this.coutries = coutries;
    }

    public List<String> getPrices() {
        return prices;
    }

    public void setPrices(List<String> prices) {
        this.prices = prices;
    }

    private  List<Product> products;

    private  List <Image> images ;

    private  List <com.speedrocket.progmine.speedrocket.Model.Category> Category;
    private  List <com.speedrocket.progmine.speedrocket.Model.Company> Companies;
    private  List<Error> errors;

    private  List <BankAccount> Banks;
    private List<CompanyOrder> companyOrders ;
    private List<String> email;
    private  List<String> type;

     private  int net , pending;
    String image ;

    int basketItem ;

    int maxOrdersId ;

    public List<CompanyOrder> getCompanyOrders() {
        return companyOrders;
    }

    public void setCompanyOrders(List<CompanyOrder> companyOrders) {
        this.companyOrders = companyOrders;
    }

    public int getMaxOrdersId() {
        return maxOrdersId;
    }

    public void setMaxOrdersId(int maxOrdersId) {
        this.maxOrdersId = maxOrdersId;
    }

    public int getBasketItem() {
        return basketItem;
    }

    public void setBasketItem(int basketItem) {
        this.basketItem = basketItem;
    }

    String time ;
    String message ,data_1  ;
    int userCoins ;

    public int getUserCoins() {
        return userCoins;
    }

    public void setUserCoins(int userCoins) {
        this.userCoins = userCoins;
    }

    private List<PersonalUser> data ;
    private List<PersonalUser> user ;

    private List<com.speedrocket.progmine.speedrocket.Model.Company> Company ;

    private  List<ProductsWinner> Winners ;

    private List<UserInOffer> userInOffer ;
    public ResultModel(List<String> email) {
        this.email = email;
    }

    public List<String> getEmail() {
        return email;
    }

    public List<PersonalUser> getUser() {
        return user;
    }


    public List<offerId> getOffersId() {
        return offersId;
    }

    public void setOffersId(List<offerId> offersId) {
        this.offersId = offersId;
    }

    public List<UserInOffer> getUsers() {
        return userInOffer;
    }


    public List<com.speedrocket.progmine.speedrocket.Model.Category> getCategory() {
        return Category;
    }

    public void setCategory(List<com.speedrocket.progmine.speedrocket.Model.Category> category) {
        Category = category;
    }

    public void setUsers(List<UserInOffer> userInOffer) {
        this.userInOffer = userInOffer;
    }

    public void setUser(List<PersonalUser> user) {
    this.user = user;
}


    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }


    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public String getData_1() {

        return data_1;
    }

    public void setData_1(String data_1) {
        this.data_1 = data_1;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public List<PersonalUser> getData() {
        return data;
    }

    public void setData(List<PersonalUser> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ProductsWinner> getWinners() {
        return Winners;
    }

    public void setWinners(List<ProductsWinner> winners) {
        Winners = winners;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<com.speedrocket.progmine.speedrocket.Model.Company> getCompany() {
        return Company;
    }

    public void setCompany(List<com.speedrocket.progmine.speedrocket.Model.Company> company) {
        Company = company;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<com.speedrocket.progmine.speedrocket.Model.Company> getCompanies() {
        return Companies;
    }

    public void setCompanies(List<com.speedrocket.progmine.speedrocket.Model.Company> companies) {
        Companies = companies;
    }

    public List<BankAccount> getBanks() {
        return Banks;
    }

    public void setBanks(List<BankAccount> banks) {
        Banks = banks;
    }

    public int getNet() {
        return net;
    }

    public void setNet(int net) {
        this.net = net;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public List<BasketItem> getBasketItems() {
        return basketItems;
    }

    public void setBasketItems(List<BasketItem> basketItems) {
        this.basketItems = basketItems;
    }

    /* public List<PersonalUser> getPersonalusers() {
        return users;
    }

    public void setPersonalusers(List<PersonalUser> personalusers) {
        this.users = personalusers;
    }*/
} // Class Of ResultModel
