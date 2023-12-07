package com.acidjobs.acidjobs.core.api.user.profile.complete_profile;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompleteProfileResponse {
	private int strength;
	private Set<String> pendingAction;
	private boolean isComplete;
}
