package com.turbolisten.bot.service.common.domain;


import com.turbolisten.bot.service.common.codeconst.ResponseCodeConst;

/**
 * 返回类
 *
 * @param <T>
 * @author gangquan
 */
public class ResponseDTO<T> {

    protected Integer code;

    protected String msg;

    protected Boolean success;

    protected T data;

    public ResponseDTO() {
    }

    public ResponseDTO(ResponseCodeConst responseCodeConst, String msg) {
        this.code = responseCodeConst.getCode();
        this.msg = msg;
        this.success = responseCodeConst.isSuccess();
    }

    public ResponseDTO(ResponseCodeConst responseCodeConst, T data) {
        super();
        this.code = responseCodeConst.getCode();
        this.msg = responseCodeConst.getMsg();
        this.data = data;
        this.success = responseCodeConst.isSuccess();
    }

    public ResponseDTO(ResponseCodeConst responseCodeConst, T data, Boolean isSuccess) {
        super();
        this.code = responseCodeConst.getCode();
        this.msg = responseCodeConst.getMsg();
        this.data = data;
        this.success = isSuccess;
    }

    public ResponseDTO(ResponseCodeConst responseCodeConst, T data, String msg) {
        super();
        this.code = responseCodeConst.getCode();
        this.msg = msg;
        this.data = data;
        this.success = responseCodeConst.isSuccess();
    }

    private ResponseDTO(ResponseCodeConst responseCodeConst) {
        this.code = responseCodeConst.getCode();
        this.msg = responseCodeConst.getMsg();
        this.success = responseCodeConst.isSuccess();
    }

    public ResponseDTO(ResponseDTO responseDTO) {
        this.code = responseDTO.getCode();
        this.msg = responseDTO.getMsg();
        this.success = responseDTO.isSuccess();
    }

    public ResponseDTO(ResponseCodeConst codeConst, boolean isSuccess) {
        this.code = codeConst.getCode();
        this.msg = codeConst.getMsg();
        this.success = isSuccess;
    }

    public ResponseDTO(int code, String msg, boolean isSuccess) {
        this.code = code;
        this.msg = msg;
        this.success = isSuccess;
    }

    public static <T> ResponseDTO<T> succ() {
        return new ResponseDTO(ResponseCodeConst.SUCCESS);
    }

    public static <T> ResponseDTO<T> succData(T data, String msg) {
        return new ResponseDTO(ResponseCodeConst.SUCCESS, data, msg);
    }

    public static <T> ResponseDTO<T> succData(T data) {
        return new ResponseDTO(ResponseCodeConst.SUCCESS, data);
    }

    public static ResponseDTO succMsg(String msg) {
        return new ResponseDTO(ResponseCodeConst.SUCCESS, msg);
    }

    public static <T> ResponseDTO<T> wrap(ResponseCodeConst codeConst) {
        return new ResponseDTO<>(codeConst);
    }

    public static <T> ResponseDTO<T> wrap(ResponseCodeConst codeConst, boolean isSuccess) {
        return new ResponseDTO<>(codeConst, isSuccess);
    }

    public static ResponseDTO wrap(ResponseDTO responseDTO) {
        return new ResponseDTO<>(responseDTO.getCode(), responseDTO.getMsg(), responseDTO.isSuccess());
    }

    public static ResponseDTO wrap(ResponseDTO responseDTO, boolean isSuccess) {
        return new ResponseDTO<>(responseDTO.getCode(), responseDTO.getMsg(), isSuccess);
    }


    public static <T> ResponseDTO<T> wrapData(ResponseCodeConst codeConst, T t) {
        return new ResponseDTO<T>(codeConst, t);
    }

    public static <T> ResponseDTO<T> wrapMsg(ResponseCodeConst codeConst, String msg) {
        return new ResponseDTO<T>(codeConst, msg);
    }

    public String getMsg() {
        return msg;
    }

    public ResponseDTO setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public int getCode() {
        return code;
    }

    public ResponseDTO setCode(Integer code) {
        this.code = code;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResponseDTO setData(T data) {
        this.data = data;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ResponseDTO{" + "code=" + code + ", msg='" + msg + '\'' + ", success=" + success + ", data=" + data + '}';
    }
}
