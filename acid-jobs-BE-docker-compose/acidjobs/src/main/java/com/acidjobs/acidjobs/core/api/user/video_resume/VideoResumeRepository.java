package com.acidjobs.acidjobs.core.api.user.video_resume;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

public interface VideoResumeRepository extends JpaRepository<VideoResume,Long> {
     VideoResume findByUser(User user);
}
