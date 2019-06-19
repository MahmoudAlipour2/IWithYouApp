package ir.iwithyou.app.features.sendtoOCR.webService;

import ir.iwithyou.app.features.sendtoOCR.model.OCRResponse;
import ir.iwithyou.app.features.sendtoOCR.model.Response;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {
    @Multipart
    @POST("transform/")
    Call<Response> upload(@Part MultipartBody.Part body, @Header("Authorization") String authToken);
}
