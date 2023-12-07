package com.acidjobs.acidjobs.core.api.user.profile.share_profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
	private String firstName;
	private String lastName;
	private String email;
	private String profileImage;
	private String imageUrl;
}
