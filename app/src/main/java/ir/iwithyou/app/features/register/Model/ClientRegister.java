package ir.iwithyou.app.features.register.Model;

import ir.iwithyou.app.pojo.register.Register;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ClientRegister {

    @POST("register")
    Call<Register> register(@Body SendModelForRegister sendModelForRegister);


}
