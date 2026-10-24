package com.typing.service.mock;

import com.typing.model.UserProfile;
import com.typing.service.IUserProfileService;

/**
 * =========================================================================
 * [MEMBER 4 MOCK IMPLEMENTATION: USER PROFILE SERVICE]
 * =========================================================================
 * Mock user data provider for dashboard header and user stats.
 */
public class MockUserProfileService implements IUserProfileService {

    private UserProfile currentUser;

    public MockUserProfileService() {
        this.currentUser = UserProfile.createDefault();
    }

    @Override
    public UserProfile getCurrentUser() {
        return currentUser;
    }

    @Override
    public void updateProfile(UserProfile profile) {
        if (profile != null) {
            this.currentUser = profile;
        }
    }
}
