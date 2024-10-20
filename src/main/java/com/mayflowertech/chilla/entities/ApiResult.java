package com.mayflowertech.chilla.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("ApiResultFilter")
public class ApiResult<T> {

    private int status;
    private String message;
    
    
    private T result;
    
    private int size;

    public ApiResult(int status, String message, T result) {
        this.status = status;
        this.message = message;
        this.result = result;
        if (result != null && result instanceof List<?>) {
            this.size = ((List<?>) result).size();
        } else {
            this.size = 1; 
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
    
    
}
