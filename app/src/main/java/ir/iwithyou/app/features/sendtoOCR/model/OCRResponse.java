
package ir.iwithyou.app.features.sendtoOCR.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OCRResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("response")
    @Expose
    private Response response;
    @SerializedName("message")
    @Expose
    private Object message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

}
