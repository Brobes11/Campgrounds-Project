package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.ProjectDAO;

public class JDBCProjectDAO implements ProjectDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCProjectDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Project> getAllActiveProjects() {
		List<Project> projects= new ArrayList<>();
		String sql="SELECT project_id,name,from_date,to_date FROM project WHERE from_date IS NOT NULL ;";
		SqlRowSet results= jdbcTemplate.queryForRowSet(sql);
		while(results.next()) {
			projects.add(mapRowToProjects(results));
			
		}
		return projects;
		
	}

	
	

	@Override
	public void removeEmployeeFromProject(Long projectId, Long employeeId) {
		String sql = "DELETE FROM project_employee WHERE project_id = ? AND employee_id = ?;";
		jdbcTemplate.update(sql,projectId,employeeId);
		
	}

	@Override
	public void addEmployeeToProject(Long projectId, Long employeeId) {
		String sql = "INSERT INTO project_employee VALUES (?,?);";
		jdbcTemplate.update(sql,projectId,employeeId);
		
	}
	private Project mapRowToProjects(SqlRowSet rows) {

		Project p = new Project();
		p.setId(rows.getLong("project_id"));
		p.setName(rows.getString("name"));
		if(rows.getDate("from_date") != null) {
		p.setStartDate(rows.getDate("from_date").toLocalDate());
		}
		if(rows.getDate("to_date") != null) {
		p.setEndDate(rows.getDate("to_date").toLocalDate());
		}
		
		return p;
	}

}
