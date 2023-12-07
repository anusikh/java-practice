package com.acidjobs.acidjobs.core.api.user.education.basic_education;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.acidjobs.acidjobs.core.api.user.resume.Resume;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;

@Service
public class BasicEducationService {
	   @Autowired
	 private BasicEducationRepository basicEducationRepository;

	public List<BasicEducation> save(BasicEducation basicEducation) throws GenericException {


			try{
				BasicEducation basicEducation1= basicEducationRepository.findByUserAndQualification(basicEducation.getUser(),basicEducation.getQualification());
				if(basicEducation1==null){
					basicEducationRepository.save(basicEducation);
					return basicEducationRepository.findByUser(basicEducation.getUser());
				}
				else {
					throw new GenericException("This Qualification is Already added !!");
				}

			}
			catch (Exception ex){

				throw new GenericException("This Qualification is Already added !!");
			}

	}

	public List<BasicEducation> get(User user) throws GenericException {

		try{
			return basicEducationRepository.findByUser(user);
		}
		catch (Exception exception){
			throw new GenericException("information not available");
		}
	}

	public List<BasicEducation> update(BasicEducation basicEducation) throws GenericException {
		try {
			BasicEducation basicEducation1=basicEducationRepository.findByUserAndQualification(basicEducation.getUser(),basicEducation.getQualification());
			basicEducation1.setBoard(basicEducation.getBoard());
			basicEducation1.setHighestQualification(basicEducation.isHighestQualification());
			basicEducation1.setMarkSheet(basicEducation.getMarkSheet());
			basicEducation1.setMarkSheetUrl(basicEducation.getMarkSheetUrl());
			basicEducation1.setPassOutMonth(basicEducation.getPassOutMonth());
			basicEducation1.setPassOutYear(basicEducation.getPassOutYear());
			basicEducation1.setSchool(basicEducation.getSchool());
			basicEducation1.setMarks(basicEducation.getMarks());
			basicEducation1.setMarksType(basicEducation.getMarksType());
			basicEducationRepository.save(basicEducation1);
			return basicEducationRepository.findByUser(basicEducation.getUser());

		}
		catch (Exception exception){
			throw new GenericException("information not available");
		}
	}

	public List<BasicEducation> delete(User user, String qualification) throws GenericException {
		try{
			BasicEducation basicEducation= basicEducationRepository.findByUserAndQualification(user,qualification);
			basicEducationRepository.delete(basicEducation);
			return basicEducationRepository.findByUser(user);
		}
		catch(Exception ex){
			throw new GenericException("information not available");
		}
	}
}
