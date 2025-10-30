package com.edu.auth.dto;

import lombok.Builder;
import lombok.Data;

/**
 *
 *
 * @author hjw
 * @since 2025-10-27
 */
@Data
@Builder
public class ValidateTokenResponse {
    private Boolean valid;
    private Long remainingTime;
}
