package com.acidjobs.acidjobs.core.api.user.socail_account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acidjobs.acidjobs.core.api.user.Skill.Skills;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;

@Service
public class SocialAccountService {
	@Autowired
	private SocialAccountRepository socialAccountRepository;

	public List<SocialAccount> save(SocialAccount socialAccount) throws GenericException {
		try{
			SocialAccount socialAccount1= socialAccountRepository.findByUserAndAccountName(socialAccount.getUser(), socialAccount.getAccountName());
			if(socialAccount1==null)
			{
				socialAccountRepository.save(socialAccount);
				return socialAccountRepository.findByUser(socialAccount.getUser());
			}
			throw new GenericException("This is Already Exists!!! ");

		}
		catch (Exception ex){
			throw new GenericException("This is Already Exists!!! ");
		}
	}

	public List<SocialAccount> update(SocialAccount socialAccount) throws GenericException {

		try{
			SocialAccount socialAccount1= socialAccountRepository.findByUserAndAccountName(socialAccount.getUser(), socialAccount.getAccountName());
		      if(socialAccount1!=null){
				  socialAccount1.setDescription(socialAccount.getDescription());
				  socialAccount1.setUrl(socialAccount.getUrl());
				  socialAccountRepository.save(socialAccount1);
				  return socialAccountRepository.findByUser(socialAccount1.getUser());
			  }
			throw new GenericException("Data not found");
		}
		catch (Exception ex){
			ex.printStackTrace();
			throw new GenericException("Data failed to update!! ");
		}
	}

	public List<SocialAccount> delete(String accountName, User user) throws GenericException {
		try{
			SocialAccount socialAccount= socialAccountRepository.findByUserAndAccountName(user, accountName);
			socialAccountRepository.delete(socialAccount);
			return socialAccountRepository.findByUser(socialAccount.getUser());
		}
		catch (Exception ex){
			throw new GenericException("Data failed to delete!! ");
		}
	}

	public List<SocialAccount> get(User user) throws GenericException {
		try{
			return socialAccountRepository.findByUser(user);
		}
		catch (Exception ex){
			throw new GenericException("Data failed to get!! ");
		}

	}
}
