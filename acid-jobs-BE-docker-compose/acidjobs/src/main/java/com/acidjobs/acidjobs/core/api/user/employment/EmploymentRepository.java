package com.acidjobs.acidjobs.core.api.user.employment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Repository
public interface EmploymentRepository extends JpaRepository<Employment,Long> {
    List<Employment> findByUser(User user);

}
