package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataIntegrityViolationException;
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
		List<Project> projects = new ArrayList<>();
		String sql = "SELECT project_id,name,from_date,to_date FROM project WHERE from_date IS NOT NULL ;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while (results.next()) {
			projects.add(mapRowToProjects(results));

		}
		return projects;

	}

	@Override
	public boolean removeEmployeeFromProject(Long projectId, Long employeeId) {
		boolean success = false;
		String sql = "DELETE FROM project_employee WHERE project_id = ? AND employee_id = ?;";
		try{
			jdbcTemplate.update(sql, projectId, employeeId);
			success = true;
		}catch (DataIntegrityViolationException e) {
			// if it fails it returns false
		}
		
		return success;

	}

	@Override
	public boolean addEmployeeToProject(Long projectId, Long employeeId) {
		boolean success = false;
		String sql = "INSERT INTO project_employee VALUES (?,?);";
		try{
			jdbcTemplate.update(sql, projectId, employeeId);
			success = true;
		} catch (DataIntegrityViolationException e) {
			// if fails it results to false
		}
		
		return success;

	}

	private Project mapRowToProjects(SqlRowSet rows) {

		Project p = new Project();
		p.setId(rows.getLong("project_id"));
		p.setName(rows.getString("name"));
		if (rows.getDate("from_date") != null) {
			p.setStartDate(rows.getDate("from_date").toLocalDate());
		}
		if (rows.getDate("to_date") != null) {
			p.setEndDate(rows.getDate("to_date").toLocalDate());
		}

		return p;
	}
	

}
