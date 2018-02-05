package co.kubo.indiesco.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Diego on 3/02/2018.
 */

public class AuthToken {
    @SerializedName("auth_token")
    @Expose
    private String auth_token;

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }
}
