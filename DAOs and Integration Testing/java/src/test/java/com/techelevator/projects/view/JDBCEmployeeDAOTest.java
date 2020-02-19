package com.techelevator.projects.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.jdbc.JDBCDepartmentDAO;
import com.techelevator.projects.model.jdbc.JDBCEmployeeDAO;

public class JDBCEmployeeDAOTest {
	private static SingleConnectionDataSource dataSource;
	private JDBCEmployeeDAO dao;
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
		dao = new JDBCEmployeeDAO(dataSource);
		JdbcTemplate test = new JdbcTemplate(dataSource);
		test.update("TRUNCATE employee CASCADE");
		// test.update("INSERT INTO department(department_id, name) VALUES (1, 'Test
		// Department');");
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

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
	public void findEmployeeByDepartmentId_finds_correct_employee() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(
				"INSERT INTO employee (department_id, first_name, last_name, birth_date, hire_date, gender) VALUES (?, ?, ?, ?, ?, ?);",
				TEST_EMPLOYEE_DEPT_ID, TEST_EMPLOYEE_FIRST_NAME, TEST_EMPLOYEE_LAST_NAME, TEST_EMPLOYEE_BIRTHDAY,
				TEST_EMPLOYEE_STARTDATE, TEST_EMPLOYEE_GENDER);
		List<Employee> foundEmployees = dao.getEmployeesByDepartmentId(TEST_EMPLOYEE_DEPT_ID);
		assertNotNull(foundEmployees);
		assertEquals(1, foundEmployees.size());
	}

	@Test
	public void testCreateEmployee() {
		Employee billybob = createTestEmployee();
		dao.createEmployee(billybob);
		List<Employee> newGuy = dao.getAllEmployees();

		Employee foundEmployee = newGuy.get(0);
		assertEquals(billybob.getId(), foundEmployee.getId());
	}

	@Test
	public void testGetAllEmployees() {
		for (int i = 0; i < 100; i++) {
			Employee newStooge = createTestEmployee();
			newStooge.setFirstName(newStooge.getFirstName() + i);
			dao.createEmployee(newStooge);
		}
		List<Employee> testAllDepartments = dao.getAllEmployees();

		assertEquals(100, testAllDepartments.size());
	}

	@Test
	public void testSearchEmployeesByName() {
		for (int i = 0; i < 100; i++) {
			Employee newStooge = createTestEmployee();
			newStooge.setFirstName(newStooge.getFirstName() + i);
			dao.createEmployee(newStooge);
		}
		List<Employee> testAllDepartments = dao.searchEmployeesByName(TEST_EMPLOYEE_FIRST_NAME,
				TEST_EMPLOYEE_LAST_NAME);

		assertEquals(100, testAllDepartments.size());
	}

	@Test
	public void testGetEmployeesWithoutProjects() {
		Employee newStooge = createTestEmployee();
		Employee projectStooge = createTestEmployee();
		projectStooge.setFirstName("project");
		dao.createEmployee(projectStooge);
		dao.createEmployee(newStooge);
		JdbcTemplate stoogeUpdate = new JdbcTemplate(dataSource);
		stoogeUpdate.update("INSERT INTO project_employee (project_id,employee_id) " + "VALUES(?,?);", 1,
				projectStooge.getId());
		List<Employee> testProjectlessEmployees = dao.getEmployeesWithoutProjects();
		assertNotNull(testProjectlessEmployees);
		assertEquals(1, testProjectlessEmployees.size());
	}

	@Test
	public void testGetEmployeesByProjectId() {
		Employee newStooge = createTestEmployee();
		Employee projectStooge = createTestEmployee();
		projectStooge.setFirstName("project");
		dao.createEmployee(projectStooge);
		dao.createEmployee(newStooge);
		JdbcTemplate stoogeUpdate = new JdbcTemplate(dataSource);
		stoogeUpdate.update("INSERT INTO project_employee (project_id,employee_id) " + "VALUES(?,?);", 1,
				projectStooge.getId());
		List<Employee> testProjectlessEmployees = dao.getEmployeesByProjectId((long) 1);
		assertNotNull(testProjectlessEmployees);
		assertEquals(1, testProjectlessEmployees.size());
	}

	@Test
	public void testChangeEmployeeDpt() {
		Employee topStooge = createTestEmployee();
		dao.createEmployee(topStooge);
		Department newDepartment = new Department();
		newDepartment.setName("StoogeHeaven");
		JDBCDepartmentDAO depDao = new JDBCDepartmentDAO(dataSource);
		newDepartment = depDao.createDepartment(newDepartment);

		dao.changeEmployeeDepartment(topStooge.getId(), newDepartment.getId().longValue());
		List<Employee> newList = dao.getEmployeesByDepartmentId(newDepartment.getId());

		assertNotNull(newList);
		assertEquals(topStooge.getId(), newList.get(0).getId());
	}

}
