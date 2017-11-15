package core.test;

import core.api.IAdmin;
import core.api.impl.Admin;
import core.api.IInstructor;
import core.api.impl.Instructor;
import core.api.IStudent;
import core.api.impl.Student;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestInstructor {
	private IAdmin admin;
	private IInstructor instructor;
	private IStudent student;
	
	@Before
	public void setup() {
		this.admin = new Admin();
		this.instructor = new Instructor();
		this.student = new Student();
	}
	
	// addHomework(): Must WORK: Working Example
	@Test
	public void testAddHomework() {
		this.admin.createClass("TestClass", 2017, "Bob", 1);
		instructor.addHomework("Bob", "TestClass", 2017, "Big Project");
		assertTrue(this.instructor.homeworkExists("TestClass", 2017, "Big Project"));
	}
	
	// addHomework(): Must NOT WORK: Instructor not assigned to class
	@Test
	public void testAddHomework2() {
		this.admin.createClass("TestClass", 2017, "Bob", 1);
		this.admin.createClass("TestClass2", 2017, "Jim", 1);
		instructor.addHomework("Jim", "TestClass2", 2017, "Big Project");
		assertFalse(this.instructor.homeworkExists("TestClass", 2017, "Big Project"));
	}
	
	// assignGrade(): Must WORK: Working Example
	@Test
	public void testAssignGrade() {
		this.admin.createClass("TestClass", 2017, "Bob", 1);
		this.instructor.addHomework("Bob", "TestClass", 2017, "Big Project");
		this.student.registerForClass("TestStudent", "TestClass", 2017);
		this.student.submitHomework("TestClass", "Big Project", "original answer", "TestClass", 2017);
		this.instructor.assignGrade("Bob", "TestClass", 2017, "Big Project", "TestStudent", 90);
		assertNotNull(this.instructor.getGrade("TestClass",2017,"Big Project","TestStudent"));
	}
	
	// assignGrade(): Must NOT WORK: Instructor not assigned to class
	@Test
	public void testAssignGrade2() {
		this.admin.createClass("TestClass", 2017, "Jim", 1);
		this.instructor.addHomework("Bob", "TestClass", 2017, "Big Project");
		this.student.registerForClass("TestStudent", "TestClass", 2017);
		this.student.submitHomework("TestClass", "Big Project", "original answer", "TestClass", 2017);
		this.instructor.assignGrade("Bob", "TestClass", 2017, "Big Project", "TestStudent", 90);
		assertNull(this.instructor.getGrade("TestClass",2017,"Big Project","TestStudent"));
	}
	
	// assignGrade(): Must NOT WORK: Homework not assigned
	@Test
	public void testAssignGrade3() {
		this.admin.createClass("TestClass", 2017, "Bob", 1);
		this.student.registerForClass("TestStudent", "TestClass", 2017);
		this.student.submitHomework("TestClass", "Big Project", "original answer", "TestClass", 2017);
		this.instructor.assignGrade("Bob", "TestClass", 2017, "Big Project", "TestStudent", 90);
		assertNull(this.instructor.getGrade("TestClass",2017,"Big Project","TestStudent"));
	}
	
	// assignGrade(): Must NOT WORK: Student didn't submit homework.
	@Test
	public void testAssignGrade4() {
		this.admin.createClass("TestClass", 2017, "Bob", 1);
		this.instructor.addHomework("Bob", "TestClass", 2017, "Big Project");
		this.student.registerForClass("TestStudent", "TestClass", 2017);
		this.instructor.assignGrade("Bob", "TestClass", 2017, "Big Project", "TestStudent", 90);
		assertNull(this.instructor.getGrade("TestClass",2017,"Big Project","TestStudent"));
	}
	
	// assignGrade(): Must NOT WORK: Grade less than 0
	@Test
	public void testAssignGrade5() {
		this.admin.createClass("TestClass", 2017, "Bob", 1);
		this.instructor.addHomework("Bob", "TestClass", 2017, "Big Project");
		this.student.registerForClass("TestStudent", "TestClass", 2017);
		this.student.submitHomework("TestClass", "Big Project", "original answer", "TestClass", 2017);
		this.instructor.assignGrade("Bob", "TestClass", 2017, "Big Project", "TestStudent", -1);
		assertNull(this.instructor.getGrade("TestClass",2017,"Big Project","TestStudent"));
	}
	
	// assignGrade(): Must WORK: Corner Case: Grade exactly 0
	@Test
	public void testAssignGrade6() {
		this.admin.createClass("TestClass", 2017, "Bob", 1);
		this.instructor.addHomework("Bob", "TestClass", 2017, "Big Project");
		this.student.registerForClass("TestStudent", "TestClass", 2017);
		this.student.submitHomework("TestClass", "Big Project", "original answer", "TestClass", 2017);
		this.instructor.assignGrade("Bob", "TestClass", 2017, "Big Project", "TestStudent", 0);
		assertNotNull(this.instructor.getGrade("TestClass",2017,"Big Project","TestStudent"));
	} // NOTE TO SELF: IS GRADE > 100 OKAY?
		
}
