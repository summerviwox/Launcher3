package com.summer.logic;

import java.io.Serializable;

public class BaseRes<T> implements Serializable {

    public T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
