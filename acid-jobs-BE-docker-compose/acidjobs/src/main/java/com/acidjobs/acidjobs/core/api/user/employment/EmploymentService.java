package com.acidjobs.acidjobs.core.api.user.employment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;

@Service
public class EmploymentService {

	@Autowired
	private EmploymentRepository employmentRepository;

	public List<Employment> save(Employment employment) throws GenericException {
		try{
			employmentRepository.save(employment);
			return employmentRepository.findByUser(employment.getUser());
		}
		catch(Exception ex){
			throw new GenericException("failed to add information !!");
		}
	}

	public List<Employment> update(Employment employment) throws GenericException {
		try {
			Employment employment1 = employmentRepository.findById(employment.getId()).get();
			employment1.setDescription(employment.getDescription());
			employment1.setDesignation(employment.getDesignation());
			employment1.setCurrentCompany(employment.isCurrentCompany());
			employment1.setEndMonth(employment.getEndMonth());
			employment1.setStartMonth(employment.getStartMonth());
			employment1.setOrganization(employment.getOrganization());
			employment1.setStartYear(employment.getStartYear());
			employment1.setEndYear(employment.getEndYear());
			employmentRepository.save(employment1);
			return employmentRepository.findByUser(employment.getUser());
		}
		catch (Exception ex){
			throw  new GenericException("Data not found !!");
		}
	}

	public List<Employment> delete(Long id, User user) throws GenericException {
		try{
			Employment employment = employmentRepository.getById(id);
			employmentRepository.delete(employment);
			return employmentRepository.findByUser(user);
		}
		catch (Exception exception){
			throw  new GenericException("Data not found !!");
		}
	}

	public List<Employment> get(User user) throws GenericException {
		try{

			return employmentRepository.findByUser(user);
		}
		catch (Exception exception){
			throw  new GenericException("Data not found !!");
		}
	}
}
