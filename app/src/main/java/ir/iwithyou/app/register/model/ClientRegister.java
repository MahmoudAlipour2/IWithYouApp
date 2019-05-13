package ir.iwithyou.app.register.model;

import ir.iwithyou.app.pojo.register.Register;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ClientRegister {

    @POST("register")
    Call<Register> register(@Body SendModelForRegister sendModelForRegister);


}
