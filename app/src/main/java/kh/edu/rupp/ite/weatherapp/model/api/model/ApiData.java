package kh.edu.rupp.ite.weatherapp.model.api.model;

public class ApiData<T> {
    private Status status;
    private T data;

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

