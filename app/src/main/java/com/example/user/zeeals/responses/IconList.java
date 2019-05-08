package com.example.user.zeeals.responses;

import com.example.user.zeeals.model.Icon;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class IconList {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("serve")
    @Expose
    private List<Icon> serve = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setServe(List<Icon> serve) {
        this.serve = serve;
    }

//    public List<Icon> getIconList() {
//        return serve;
//    }
        public ArrayList<String> getIconList(){
        ArrayList<String> iconList = new ArrayList<>();

        for(int i=0; i<serve.size();i++){
            String x = serve.get(i).getSysVal();
            String y = (x.split(Pattern.quote("\\"))[2]).substring(0,4);
            iconList.add(y);
        }
        return iconList;
    }
}
