
package ir.iwithyou.app.features.sendtoOCR.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("transformedText")
    @Expose
    private String transformedText;

    public String getTransformedText() {
        return transformedText;
    }

    public void setTransformedText(String transformedText) {
        this.transformedText = transformedText;
    }

}
