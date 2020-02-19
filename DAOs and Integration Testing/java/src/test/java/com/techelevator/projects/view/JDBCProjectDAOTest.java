package com.techelevator.projects.view;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.jdbc.JDBCEmployeeDAO;
import com.techelevator.projects.model.jdbc.JDBCProjectDAO;

public class JDBCProjectDAOTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCProjectDAO dao;
	private static final String TEST_PROJECT_NAME = "Fun Project For Stooges";
	private static final LocalDate TEST_PROJECT_FROM = LocalDate.of(2018, 11, 11);
	private static final LocalDate TEST_PROJECT_TO = LocalDate.of(2022, 01, 11);
	private static final long TEST_PROJECT_ID = -1;
	private static final String TEST_EMPLOYEE_FIRST_NAME = "Test";
	private static final String TEST_EMPLOYEE_LAST_NAME = "Name";
	private static final long TEST_EMPLOYEE_ID = -1;
	private static final long TEST_EMPLOYEE_DEPT_ID = 2;
	private static final LocalDate TEST_EMPLOYEE_BIRTHDAY = LocalDate.of(2000, 11, 11);
	private static final LocalDate TEST_EMPLOYEE_STARTDATE = LocalDate.of(2019, 11, 11);
	private static final char TEST_EMPLOYEE_GENDER = 'F';

	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/projects");
		dataSource.setUsername("postgres");
		dataSource.setPassword(System.getenv("DB_PASSWORD"));
		dataSource.setAutoCommit(false); // allows rollback

	}

	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	@Before
	public void setup() {
		dao = new JDBCProjectDAO(dataSource);
		JdbcTemplate test = new JdbcTemplate(dataSource);
		test.update("TRUNCATE project CASCADE");
		test.update("INSERT INTO project (name, from_date, to_date, project_id) VALUES (?,?,?,?);",
				TEST_PROJECT_NAME, TEST_PROJECT_FROM, TEST_PROJECT_TO, TEST_PROJECT_ID);
		
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

//	private Project createTestProject() {
//		Project newProject = new Project();
//		newProject.setName(TEST_PROJECT_NAME);
//		newProject.setStartDate(TEST_PROJECT_FROM);
//		newProject.setEndDate(TEST_PROJECT_TO);
//
//		return newProject;
//	}
	
	private Employee createTestEmployee() {
		Employee newEmployee = new Employee();
		newEmployee.setFirstName(TEST_EMPLOYEE_FIRST_NAME);
		newEmployee.setLastName(TEST_EMPLOYEE_LAST_NAME);
		newEmployee.setDepartmentId(TEST_EMPLOYEE_DEPT_ID);
		newEmployee.setId(TEST_EMPLOYEE_ID);
		newEmployee.setBirthDay(TEST_EMPLOYEE_BIRTHDAY);
		newEmployee.setHireDate(TEST_EMPLOYEE_STARTDATE);
		newEmployee.setGender(TEST_EMPLOYEE_GENDER);

		return newEmployee;
	}

	@Test
	public void GetAllActiveProjects_returns_correct_amount() {

		List<Project> testAllProjects = dao.getAllActiveProjects();

		assertEquals(1, testAllProjects.size());

	}
	
	@Test
	public void addEmployeeToProject_works_as_expected() {
		Employee newEmployee = createTestEmployee();
		JDBCEmployeeDAO jdbcEmployeeDAO = new JDBCEmployeeDAO(dataSource);
		jdbcEmployeeDAO.createEmployee(newEmployee);
		boolean success = dao.addEmployeeToProject(TEST_PROJECT_ID, newEmployee.getId().longValue());
		
		assertNotNull(newEmployee);
		assertTrue(success);
	}
	
	@Test
	public void removeEmployeeToProject_works_as_expected() {
		Employee newEmployee = createTestEmployee();
		JDBCEmployeeDAO jdbcEmployeeDAO = new JDBCEmployeeDAO(dataSource);
		jdbcEmployeeDAO.createEmployee(newEmployee);
		boolean addResult = dao.addEmployeeToProject(TEST_PROJECT_ID, newEmployee.getId().longValue());
		boolean removeResult = dao.removeEmployeeFromProject(TEST_PROJECT_ID, newEmployee.getId().longValue());
	
		
		assertNotNull(newEmployee);
		assertTrue(addResult);
		assertTrue(removeResult);
	}
	
	
	
	

}
