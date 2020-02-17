package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.EmployeeDAO;

public class JDBCEmployeeDAO implements EmployeeDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCEmployeeDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> employees= new ArrayList<>();
		String sql="SELECT first_name,last_name, employee_id, department_id, birth_date,gender,hire_date FROM employee";
		SqlRowSet results= jdbcTemplate.queryForRowSet(sql);
		while(results.next()) {
			employees.add(mapRowToEmployee(results));
			
		}
		return employees;
	}

	@Override
	public List<Employee> searchEmployeesByName(String firstNameSearch, String lastNameSearch) {
		List<Employee> employees= new ArrayList<>();
		String sql="SELECT first_name,last_name, employee_id, department_id, birth_date,gender,hire_date "
				+ "FROM employee WHERE first_name= ? AND last_name=?";
		SqlRowSet results= jdbcTemplate.queryForRowSet(sql,firstNameSearch,lastNameSearch);
		while(results.next()) {
			employees.add(mapRowToEmployee(results));
			
		}
		return employees;
		
	}

	@Override
	public List<Employee> getEmployeesByDepartmentId(long id) {
		List<Employee> employees= new ArrayList<>();
		String sql="SELECT first_name,last_name, employee_id, department_id, birth_date,gender,hire_date "
				+ "FROM employee WHERE department_id=?;";
		SqlRowSet results= jdbcTemplate.queryForRowSet(sql,id);
		while(results.next()) {
			employees.add(mapRowToEmployee(results));
			
		}
		return employees;
	}

	@Override
	public List<Employee> getEmployeesWithoutProjects() {
		List<Employee> employees= new ArrayList<>();
		String sql="SELECT first_name,last_name, employee_id, department_id, birth_date,gender,hire_date "
				+ "FROM employee WHERE employee_id NOT IN (SELECT employee_id FROM project_employee);";
		SqlRowSet results= jdbcTemplate.queryForRowSet(sql);
		while(results.next()) {
			employees.add(mapRowToEmployee(results));
			
		}
		return employees;

	}

	@Override
	public List<Employee> getEmployeesByProjectId(Long projectId) {
		List<Employee> employees= new ArrayList<>();
		String sql="SELECT first_name,last_name, employee.employee_id, department_id, birth_date,gender,hire_date "
				+ "FROM employee JOIN project_employee ON project_employee.employee_id = employee.employee_id "
				+ "WHERE project_employee.project_id =?;";
		SqlRowSet results= jdbcTemplate.queryForRowSet(sql,projectId);
		while(results.next()) {
			employees.add(mapRowToEmployee(results));
					}
		return employees;
	}
	
	@Override
	public void changeEmployeeDepartment(Long employeeId, Long departmentId) {
		String sql = "UPDATE employee " + "Set department_id = ?  WHERE employee_id =?;";
		jdbcTemplate.update(sql,departmentId, employeeId);
	}

	private Employee mapRowToEmployee(SqlRowSet rows) {

		Employee e = new Employee();
		e.setId(rows.getLong("employee_id"));
		e.setFirstName(rows.getString("first_name"));
		e.setLastName(rows.getString("last_name"));
		e.setDepartmentId(rows.getLong("department_id"));
		e.setBirthDay(rows.getDate("birth_date").toLocalDate());
		e.setHireDate(rows.getDate("hire_date").toLocalDate());
		e.setGender(rows.getString("gender").charAt(0));

		return e;
	}
}