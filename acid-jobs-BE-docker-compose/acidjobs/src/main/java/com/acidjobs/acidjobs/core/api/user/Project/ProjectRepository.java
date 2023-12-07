package com.acidjobs.acidjobs.core.api.user.Project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
	List<Project> findByUser(User user);

}
