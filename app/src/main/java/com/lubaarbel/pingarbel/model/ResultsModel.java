package com.lubaarbel.pingarbel.model;

import android.view.View;

import androidx.databinding.BaseObservable;

public class ResultsModel extends BaseObservable {

    private String resultViewTextId;
    private boolean isAuthenticated;
    private int authBtnVisibility;

    public ResultsModel(String resultViewTextId, boolean isAuthenticated, int visibility) {
        this.resultViewTextId = resultViewTextId;
        this.isAuthenticated = isAuthenticated;
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

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
        authBtnVisibility = View.GONE;
        notifyChange();
    }
}
