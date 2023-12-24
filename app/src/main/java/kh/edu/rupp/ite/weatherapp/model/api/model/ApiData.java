package kh.edu.rupp.ite.weatherapp.model.api.model;

public class ApiData<T> {
    private final Status status;
    private final T data;

    public ApiData(Status status, T data) {
        this.status = status;
        this.data = data;
    }

    public Status getStatus() {
        return this.status;
    }

    public T getData() {
        return this.data;
    }
}

