package com.example.wdgfarm_android.utils;

public final class URLs {
    //Zone, Login
    public static String COM_CODE = "625956";

    //Login
    public static String USER_ID = "krch12";
//    public static String API_CERT_KEY = "5016c92e7e510484ab20e7013c0848fbd6";       //인증 키      (유효기간 1년)
    public static String API_CERT_KEY = "4afd8a4be2bd140188cbb7e2e3c4cff695";       //TEST 인증 키 (유효기간 2주)
    public static String LAN_TYPE = "ko-KR";

    public static String CONTENT_TYPE = "application/json";

    public static String BASE_TEST_URL = "https://sboapi";
    public static String BASE_REQUEST_URL = "https://oapi";

    //Zone
    public static String ZONE_URL = ".ecount.com/OAPI/V2/Zone";

    //로그인
    public static String LOGIN_URL = ".ecount.com/OAPI/V2/OAPILogin";

    //구매 입력
    public static String PURCHASE_URL = ".ecount.com/OAPI/V2/Purchases/SavePurchases?SESSION_ID=";

    //거래처 등록
    public static String COMPANY_URL = ".ecount.com/OAPI/V2/AccountBasic/SaveBasicCust?SESSION_ID=";

    //품목 등록
    public static String PRODUCT_URL = ".ecount.com/OAPI/V2/InventoryBasic/SaveBasicProduct?SESSION_ID=";

}
