package com.olsplus.balancemall.core.http;


public class BaseBussiness {

//    protected Context context;
//
//    public void updateToken(){
//        String url = ApiConst.BASE_URL + "v1/token";
//        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
//        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
//        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
//        String sign = parseUpdateTokenSign(url, uid, token,timestamp);
//        HttpResultObserver respObserver = new HttpResultObserver<TokenResult>() {
//
//            @Override
//            public void prepare() {
//
//            }
//
//            @Override
//            public void handleSuccessResp(TokenResult data) {
//                if (data == null) {
//                    return;
//                }
//                String token = data.getGoodsToken();
//                SPUtil.put(context,SPUtil.TOKEN,token);
//
//            }
//
//            @Override
//            public void handleFailedResp(String msg) {
//                LogUtil.d("yongyuan,w", "getGoodsToken failed");
//            }
//        };
//        HttpManager.getRetrofit()
//                .create(TokenService.class)
//                .getGoodsToken(uid,token,timestamp,sign)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(respObserver);
//    }
//
//    private String parseUpdateTokenSign(String url, String uid, String token,String timestamp) {
//        Map<String, String> paramMap = new HashMap<String, String>();
//        paramMap.put("uid", uid);
//        paramMap.put("token", token);
//        paramMap.put("timestamp", timestamp);
//        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);
//        return sign;
//    }

}
