package com.acidjobs.acidjobs.core.api.user.education.graduation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;

@Service
public class GraduationService {
	@Autowired
	private GraduationRepository graduationRepository;

	public List<Graduation> save(Graduation graduation) throws GenericException {
		try{
			Graduation graduation1= graduationRepository.findByUserAndQualification(graduation.getUser(),graduation.getQualification());
			if(graduation1==null){
				graduationRepository.save(graduation);
				return  graduationRepository.findByUser(graduation.getUser());
			}
			else{
				throw  new GenericException("This qualification is already exists !!");
			}
		}
		catch(Exception ex){
			 throw  new GenericException("This qualification is already exists !!");
		}
	}

	public List<Graduation> get(User user) throws GenericException {
		try{
			return graduationRepository.findByUser(user);
		}
		catch(Exception ex){
			throw new GenericException("No data found !!");
		}
	}

	public List<Graduation> delete(User user, String qualification) throws GenericException {
		try{
			Graduation graduation= graduationRepository.findByUserAndQualification(user,qualification);
			if(graduation!=null){
				graduationRepository.delete(graduation);
				return graduationRepository.findByUser(user);
			}
			else{
				throw new GenericException("Data is not availabe !!");
			}

		}
		catch (Exception ex){
			throw new GenericException("This is not availabe !!");
		}
	}

	public List<Graduation> update(Graduation graduation) throws GenericException {
		try{
			Graduation graduation1=graduationRepository.findByUserAndQualification(graduation.getUser(),graduation.getQualification());
			if(graduation1!=null){
				graduation1.setMarks(graduation.getMarks());
				graduation1.setHighestQualification(graduation.isHighestQualification());
				graduation1.setMarkSheet(graduation.getMarkSheet());
				graduation1.setMarkSheetUrl(graduation.getMarkSheetUrl());
				graduation1.setPassOutMonth(graduation.getPassOutMonth());
				graduation1.setPassOutYear(graduation.getPassOutYear());
				graduation1.setBranch(graduation.getBranch());
				graduation1.setCourse(graduation.getCourse());
				graduation1.setInstitute(graduation.getInstitute());
				graduation1.setUniversity(graduation.getUniversity());
				graduation1.setState(graduation.getState());
				graduation1.setMarksType(graduation.getMarksType());
				graduationRepository.save(graduation1);
				return graduationRepository.findByUser(graduation.getUser());
			}
			else{
				throw new GenericException("Data does not exists !!");
			}
		}
		catch(Exception ex){
			throw new GenericException("Graduation failed to  update !! ");
		}
	}
}
