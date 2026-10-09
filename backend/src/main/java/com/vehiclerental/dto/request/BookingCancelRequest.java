package com.vehiclerental.dto.request;

import jakarta.validation.constraints.NotBlank;
public class BookingCancelRequest {
    @NotBlank(message = "Cancellation reason is required")
    private String reason;

    public BookingCancelRequest() {
    }

    public BookingCancelRequest(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String reason;

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public BookingCancelRequest build() {
            return new BookingCancelRequest(this.reason);
        }
    }
}
