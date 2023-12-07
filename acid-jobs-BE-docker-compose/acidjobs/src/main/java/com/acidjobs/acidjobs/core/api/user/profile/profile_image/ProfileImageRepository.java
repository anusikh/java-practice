package com.acidjobs.acidjobs.core.api.user.profile.profile_image;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage,Long> {
	ProfileImage findByUser(User user);
}
