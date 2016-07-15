package com.ep.joy.bmob.utils;

public interface Validator<T> {
    boolean isValid(T t);

    ValidatorError[] getErrors();
}