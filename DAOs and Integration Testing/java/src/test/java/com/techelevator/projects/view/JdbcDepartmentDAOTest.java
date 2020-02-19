package com.techelevator.projects.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.jdbc.JDBCDepartmentDAO;

public class JdbcDepartmentDAOTest {
	private static SingleConnectionDataSource dataSource;
	private JDBCDepartmentDAO dao;
	private static final long TEST_DEPARTMENT_ID = -1;
	private static final String TEST_DEPARTMENT_NAME="Test Department";

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
		dao = new JDBCDepartmentDAO(dataSource);

	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}


	@Test
	public void findDepartmentByIdSuccess() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("INSERT INTO department (department_id, name) VALUES (?, ?);",TEST_DEPARTMENT_ID, TEST_DEPARTMENT_NAME);
		Department foundDepartment = dao.getDepartmentById(TEST_DEPARTMENT_ID);
		assertNotNull(foundDepartment);
		assertEquals(TEST_DEPARTMENT_ID,foundDepartment.getId().longValue());
		assertEquals(TEST_DEPARTMENT_NAME, foundDepartment.getName());
	}
	
	private Department createTestDepartment() {
		Department newDepartment = new Department();
		//newDepartment.setId(TEST_DEPARTMENT_ID);
		newDepartment.setName(TEST_DEPARTMENT_NAME);
		return newDepartment;
	}
	
	@Test
	public void createDepartment_Success() {
		Department newDepartment = createTestDepartment();
		dao.createDepartment(newDepartment);
		Department foundDepartment = dao.getDepartmentById(newDepartment.getId());
		assertNotNull(foundDepartment);
		assertEquals(newDepartment.getName(),foundDepartment.getName());
	}
	
	@Test
	public void saveNewNameTest() {
		Department newDepartment=createTestDepartment();
		dao.createDepartment(newDepartment);
		newDepartment.setName("SuperSweetDepartment");
		boolean success= dao.saveDepartment(newDepartment);
		assertTrue(success);
	}
	@Test
	public void getDepartmentsByNameReturnsCorrectAmount() {
		
		for (int i = 0 ; i < 100; i++) {
			Department newD = createTestDepartment();
			newD.setName(newD.getName() + i);
			dao.createDepartment(newD);
		
		}
		List<Department> testAllDepartments = dao.searchDepartmentsByName(TEST_DEPARTMENT_NAME);
		
		assertEquals(100,testAllDepartments.size());
		
	}
	
	@Test
	public void getAllDepartments_returns_all_departments() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("TRUNCATE department CASCADE;");
		
		for (int i = 0 ; i < 100; i++) {
			Department newD = createTestDepartment();
			newD.setName(newD.getName() + i);
			dao.createDepartment(newD);
		
		}
		List<Department> testAllDepartments = dao.getAllDepartments();
		
		assertEquals(100,testAllDepartments.size());	
	}
	

}
