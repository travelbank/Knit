package com.travelbank.knit;

/**
 * Created by omerozer on 2/20/18.
 */

public final class MockResponses {

    public static <T> KnitResponse<T> getAny(Class<T> clazz){
        return new KnitResponse<T>(null);
    }

    public static <T> Creator<T> builder(Class<T> clazz){
        return new Creator<T>(clazz);
    }

    public static class Creator<T>{

        private KnitResponse<T> response;

        public Creator(Class<T> clazz){
           this.response = new KnitResponse<>(null);
        }

        public Creator<T> setBody(T body){
            response.setBody(body);
            return this;
        }

        public Creator<T> setErrorMessage(String message){
            response.setErrorMessage(message);
            return this;
        }

        public KnitResponse<T> build(){
            return response;
        }
    }

}
