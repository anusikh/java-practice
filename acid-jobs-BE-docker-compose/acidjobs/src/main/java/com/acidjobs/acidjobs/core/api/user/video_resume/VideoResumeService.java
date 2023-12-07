package com.acidjobs.acidjobs.core.api.user.video_resume;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoResumeService {
	@Autowired
	private VideoResumeRepository videoResumeRepository;

	public VideoResume save(VideoResume videoResume) {
		VideoResume videoResume1=videoResumeRepository.findByUser(videoResume.getUser());
		if(videoResume1==null){
			return videoResumeRepository.save(videoResume);
		}
		else {
			videoResume1.setVideoUrl(videoResume.getVideoUrl());
			videoResume1.setDate(videoResume.getDate());
			videoResume1.setFileName(videoResume.getFileName());
			return videoResumeRepository.save(videoResume1);
		}
	}
}
