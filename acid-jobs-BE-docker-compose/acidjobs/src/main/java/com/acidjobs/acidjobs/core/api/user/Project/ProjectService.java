package com.acidjobs.acidjobs.core.api.user.Project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acidjobs.acidjobs.core.api.user.employment.Employment;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	public List<Project> save(Project project) throws GenericException {
	try{
		projectRepository.save(project);
		return projectRepository.findByUser(project.getUser());
	}
	catch(Exception ex){
		throw new GenericException("failed to add information !!");
	}
}

	public List<Project> update(Project project) throws GenericException {
		try {
			Project project1 = projectRepository.findById(project.getId()).get();
			project1.setClient(project.getClient());
			project1.setDescription(project.getDescription());
			project1.setStatus(project.getStatus());
			project1.setTitle(project.getTitle());
			project1.setEndMonth(project.getEndMonth());
			project1.setEndYear(project.getEndYear());
			project1.setStartMonth(project.getStartMonth());
			project1.setStartYear(project.getStartYear());
			projectRepository.save(project1);
			return projectRepository.findByUser(project.getUser());
		}
		catch (Exception ex){
			throw  new GenericException("Data not found !!");
		}
	}

	public List<Project> delete(Long id, User user) throws GenericException {
		try{
			Project project = projectRepository.findById(id).get();
			projectRepository.delete(project);
			return projectRepository.findByUser(user);
		}
		catch (Exception exception){
			throw  new GenericException("Data not found !!");
		}
	}

	public List<Project> get(User user) throws GenericException {
		try{

			return projectRepository.findByUser(user);
		}
		catch (Exception exception){
			throw  new GenericException("Data not found !!");
		}
	}
}
