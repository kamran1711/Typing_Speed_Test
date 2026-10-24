package com.typing.service;

import com.typing.model.UserProfile;

/**
 * =========================================================================
 * [MEMBER 4 INTEGRATION INTERFACE: USER PROFILE & SETTINGS]
 * =========================================================================
 * Teammate responsible for Member 4 module:
 * Implement this interface with your user profile configuration system.
 */
public interface IUserProfileService {

    UserProfile getCurrentUser();

    void updateProfile(UserProfile profile);
}
