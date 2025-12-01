package com.auth.auth.services.interfaces;

import com.auth.auth.dto.PasswordRequest;
import com.auth.auth.dto.RecoveryResponse;

public interface PasswordRecoveryService {

    RecoveryResponse sendRecoveryEmail(Integer rut);

    void resetPassword(String token, PasswordRequest newPassword);

}
