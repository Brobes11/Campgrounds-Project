package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;

public class JDBCDepartmentDAO implements DepartmentDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCDepartmentDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Department> getAllDepartments() {
		List<Department> result = new ArrayList<Department>();

		String sql = "SELECT name, department_id FROM department";

		SqlRowSet departments = jdbcTemplate.queryForRowSet(sql);

		while (departments.next()) {

			result.add(mapRowToDepartment(departments));
		}

		return result;
	}

	@Override
	public List<Department> searchDepartmentsByName(String nameSearch) {
		List<Department> result = new ArrayList<>();
		String sql = "SELECT name, department_id FROM department WHERE name ILIKE ?";
		SqlRowSet departments = jdbcTemplate.queryForRowSet(sql, "%" + nameSearch + "%");
		while (departments.next()) {

			result.add(mapRowToDepartment(departments));
		}
		return result;
	}

	@Override
	public boolean saveDepartment(Department updatedDepartment) {
		boolean success = false;
		String sql = "UPDATE Department " + "Set name = ?  WHERE department_id = ?;";
		try {
			jdbcTemplate.update(sql, updatedDepartment.getName(), updatedDepartment.getId());
			success = true;
		} catch (DataIntegrityViolationException f) {
			// result defaults to false
		}

		return success;

	}

	@Override
	public Department createDepartment(Department newDepartment) {
		String sql = "INSERT INTO department(name) VALUES (?) RETURNING department_id;";
		SqlRowSet departments = jdbcTemplate.queryForRowSet(sql, newDepartment.getName());
		if (departments.next()) {
			newDepartment.setId(departments.getLong("department_id"));
		}
		return newDepartment;
	}

	@Override
	public Department getDepartmentById(Long id) {
		Department result = null;
		String sql = "SELECT name, department_id FROM department WHERE department_id = ?;";
		SqlRowSet departments = jdbcTemplate.queryForRowSet(sql, id);
		if (departments.next()) {
			result = mapRowToDepartment(departments);

		}
		return result;
	}

	private Department mapRowToDepartment(SqlRowSet rows) {

		Department d = new Department();
		d.setId(rows.getLong("department_id"));
		d.setName(rows.getString("name"));
		return d;
	}
}
