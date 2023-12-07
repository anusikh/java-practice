package com.acidjobs.acidjobs.core.api.user.Skill;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Repository
public interface SkillsRepository extends JpaRepository<Skills,Long> {
	List<Skills> findByUser(User user);
	Skills findByUserAndSkillName(User user,String skillName);
}
