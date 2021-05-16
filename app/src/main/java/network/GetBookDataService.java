package network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetBookDataService {
    @GET
    Call<JsonObject> getBookData(@Url String url);
}
