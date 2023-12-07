package com.acidjobs.acidjobs.core.api.user.socail_account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount,Long> {
	List<SocialAccount> findByUser(User user);
	SocialAccount findByUserAndAccountName(User user,String accountName);
}
