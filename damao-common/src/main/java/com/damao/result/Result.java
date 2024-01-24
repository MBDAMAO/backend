package com.damao.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<E> implements Serializable {
    private Integer code;
    private String msg;
    private E data;

    /**
     *  静态方法使用泛型符号[E]必须定义为泛型方法，因为静态方法在类加载时候就实例化，
     *  而类中定义的[E]是在类实例化时才能确定，编译器无法确定方法上的泛型类型。
     */
    public static <E> Result<E> success() {
        Result<E> result = new Result<>();
        result.setCode(1);
        return result;
    }

    public static <E> Result<E> success(E data) {
        Result<E> result = new Result<>();
        result.data = data;
        result.code = 1;
        return result;
    }

    public static <E> Result<E> error(String msg) {
        Result<E> result = new Result<>();
        result.msg = msg;
        result.code = 0;
        return result;
    }

}
