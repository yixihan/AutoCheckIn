package com.yixihan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author : yixihan
 * @create : 2022-09-16-13:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    /**
     * 状态码
     */
    private int code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 返回数据
     */
    private Map<String, Object> data;


    /**
     * 成功
     *
     * @param code 状态码
     * @param msg  信息
     * @param data 数据
     * @return 状态码 + 信息 + 数据
     */
    public static Result success(int code, String msg, Map<String, Object> data) {
        return new Result (code, msg, data);
    }


    /**
     * 成功
     *
     * @param msg 信息
     * @return 信息
     */
    public static Result success(String msg) {
        return success (200, msg, null);
    }


    /**
     * 成功
     *
     * @param data 返回
     * @return 返回
     */
    public static Result success(Map<String, Object> data) {
        return success (200, "success !", data);
    }


    /**
     * 成功
     *
     * @param msg  信息
     * @param data 数据
     * @return 信息 + 数据
     */
    public static Result success(String msg, Map<String, Object> data) {
        return success (200, msg, data);
    }


    /**
     * 成功
     *
     * @return null
     */
    public static Result success() {
        return new Result (200, "success !", null);
    }


    /**
     * 失败
     *
     * @param code 状态码
     * @param msg  信息
     * @param data 数据
     * @return 状态码 + 信息 + 数据
     */
    public static Result fail(int code, String msg, Map<String, Object> data) {
        return new Result (code, msg, data);
    }


    /**
     * 失败
     *
     * @param msg 信息
     * @return 信息
     */
    public static Result fail(String msg) {
        return fail (444, msg, null);
    }


    /**
     * 失败
     *
     * @param data 数据
     * @return 数据
     */
    public static Result fail(Map<String, Object> data) {
        return fail (444, "fail !", data);
    }


    /**
     * 失败
     *
     * @param msg  信息
     * @param data 数据
     * @return 信息 + 数据
     */
    public static Result fail(String msg, Map<String, Object> data) {
        return fail (444, msg, data);
    }


    /**
     * 失败 返回
     *
     * @param code 状态码
     * @param msg  信息
     * @return 状态码 + 信息
     */
    public static Result fail(int code, String msg) {
        return fail (code, msg, null);
    }


    /**
     * 失败
     *
     * @param code 状态码
     * @param data 参数
     * @return 状态码 + 参数
     */
    public static Result fail(int code, Map<String, Object> data) {
        return fail (code, "fail !", data);
    }


    /**
     * 失败
     *
     * @return null
     */
    public static Result fail() {
        return new Result (444, "fail !", null);
    }
}