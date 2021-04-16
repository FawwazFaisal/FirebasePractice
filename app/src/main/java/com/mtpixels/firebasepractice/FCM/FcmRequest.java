package com.mtpixels.firebasepractice.FCM;

/**
 * Created by S.M.Mubbashir.A.Z. on 4/15/2021.
 */
public class FcmRequest {
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    private Data data;
    private String to;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
