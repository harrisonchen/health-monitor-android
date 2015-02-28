package com.example.justinkhoo.wristbandapp;

import android.app.Application;

/**
 * Created by justinkhoo on 2/13/15.
 */

public class globalvariable extends Application {

    private String someVariable;

    public String getSomeVariable() {
        return someVariable;
    }

    public void setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
    }

    public static Double globaluno;
    public static Double globaldas;
    public static Double globaltres;

    public Double getglobaluno(){
        return globaluno;
    }
    public void setglobaluno(Double globaluno){
        this.globaluno = globaluno;
    }

    public Double getglobaldas(){
        return globaldas;
    }
    public void setglobaldas(Double globaldas){
        this.globaldas = globaldas;
    }

    public Double getglobaltres(){
        return globaltres;
    }
    public void setglobaltres(Double globaltres){
        this.globaltres = globaltres;
    }
}