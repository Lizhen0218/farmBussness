package com.farmgame.server.common.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "统一接口返回结构")
public class ApiResponse<T> {

    @Schema(description = "业务状态码，0 表示成功", example = "0")
    private final int code;

    @Schema(description = "响应消息", example = "success")
    private final String message;

    @Schema(description = "响应数据")
    private final T data;

    @Schema(description = "服务端时间戳，毫秒", example = "1780915200000")
    private final long timestamp;

    public ApiResponse(int code, String message, T data, long timestamp) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, "success", data, System.currentTimeMillis());
    }

    public static ApiResponse<Void> success() {
        return success(null);
    }

    public static ApiResponse<Void> failure(ErrorCode errorCode) {
        return failure(errorCode.code(), errorCode.message());
    }

    public static ApiResponse<Void> failure(int code, String message) {
        return new ApiResponse<>(code, message, null, System.currentTimeMillis());
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
