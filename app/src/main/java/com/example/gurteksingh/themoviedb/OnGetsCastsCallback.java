package com.example.gurteksingh.themoviedb;

import java.util.List;

public interface OnGetsCastsCallback {

    void onSuccess(List<Cast> casts);

    void onError();
}
