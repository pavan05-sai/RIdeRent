package com.vehiclerental.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class BookingExtensionRequest {
    @NotNull(message = "New extended return date is required")
    private LocalDateTime newReturnDate;

    public BookingExtensionRequest() {
    }

    public BookingExtensionRequest(LocalDateTime newReturnDate) {
        this.newReturnDate = newReturnDate;
    }

    public LocalDateTime getNewReturnDate() {
        return this.newReturnDate;
    }

    public void setNewReturnDate(LocalDateTime newReturnDate) {
        this.newReturnDate = newReturnDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LocalDateTime newReturnDate;

        public Builder newReturnDate(LocalDateTime newReturnDate) {
            this.newReturnDate = newReturnDate;
            return this;
        }

        public BookingExtensionRequest build() {
            return new BookingExtensionRequest(this.newReturnDate);
        }
    }
}
