package com.lubaarbel.pingarbel.model;

import android.view.View;

import androidx.databinding.BaseObservable;

/** Second fragment model **/
public class ResultsModel extends BaseObservable {

    private String resultViewTextId;
    private int authBtnVisibility;

    public ResultsModel(String resultViewTextId, int visibility) {
        this.resultViewTextId = resultViewTextId;
        this.authBtnVisibility = visibility;
    }

    public String getResultViewText() {
        return resultViewTextId;
    }

    public void setResultViewText(String resultViewText) {
        this.resultViewTextId = resultViewText;
        notifyChange();
    }

    public int isAuthBtnVisible() {
        return authBtnVisibility;
    }

    public void setAuthenticated(boolean authenticated) {
        authBtnVisibility = View.GONE;
        notifyChange();
    }
}
