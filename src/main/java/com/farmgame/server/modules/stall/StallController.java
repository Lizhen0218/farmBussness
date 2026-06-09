package com.farmgame.server.modules.stall;

import com.farmgame.server.common.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "摆摊")
@RestController
@RequestMapping("/api/stall")
public class StallController {

    @Operation(summary = "开始摆摊")
    @PostMapping("/start")
    public ApiResponse<Map<String, Object>> start() {
        return ApiResponse.success(Map.of(
                "sessionId", "dev-session",
                "durationSeconds", 60,
                "orders", List.of()
        ));
    }

    @Operation(summary = "结束摆摊并结算")
    @PostMapping("/finish")
    public ApiResponse<Map<String, Object>> finish(@RequestBody FinishStallRequest request) {
        return ApiResponse.success(Map.of(
                "sessionId", request.getSessionId(),
                "coinsAdded", 0,
                "valid", false
        ));
    }

    public static class FinishStallRequest {
        @NotBlank
        private String sessionId;
        @NotNull
        private List<OrderResult> orders;
        private int thiefHandledCount;
        private int tycoonCompletedCount;

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public List<OrderResult> getOrders() {
            return orders;
        }

        public void setOrders(List<OrderResult> orders) {
            this.orders = orders;
        }

        public int getThiefHandledCount() {
            return thiefHandledCount;
        }

        public void setThiefHandledCount(int thiefHandledCount) {
            this.thiefHandledCount = thiefHandledCount;
        }

        public int getTycoonCompletedCount() {
            return tycoonCompletedCount;
        }

        public void setTycoonCompletedCount(int tycoonCompletedCount) {
            this.tycoonCompletedCount = tycoonCompletedCount;
        }
    }

    public static class OrderResult {
        @NotBlank
        private String orderId;
        @NotBlank
        private String recipeId;
        private boolean success;
        private long finishTime;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getRecipeId() {
            return recipeId;
        }

        public void setRecipeId(String recipeId) {
            this.recipeId = recipeId;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public long getFinishTime() {
            return finishTime;
        }

        public void setFinishTime(long finishTime) {
            this.finishTime = finishTime;
        }
    }
}
