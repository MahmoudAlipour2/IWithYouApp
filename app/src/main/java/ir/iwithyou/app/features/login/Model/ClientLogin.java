package ir.iwithyou.app.features.login.Model;

import ir.iwithyou.app.features.register.Model.SendModelForRegister;
import ir.iwithyou.app.pojo.login.Login;
import ir.iwithyou.app.pojo.register.Register;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ClientLogin {

    @POST("login")
    Call<Login> login(@Body SendModeltoLogin sendModeltoLogin);

}
