package core.test;

import core.api.IAdmin;
import core.api.impl.Admin;
import core.api.IStudent;
import core.api.impl.Student;
import core.api.IInstructor;
import core.api.impl.Instructor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAdmin {
	private IAdmin admin;
	private IInstructor instructor;
	
	@Before
	public void setup() {
		this.admin = new Admin();
		this.instructor = new Instructor();
	}
	
	// createClass(): Must WORK: Working example
	@Test
	public void testCreateClass() {
		this.admin.createClass("TestClass", 2017, "Instructor", 15);
        assertTrue(this.admin.classExists("TestClass", 2017));
	}
	
	// createClass(): Must NOT WORK: Class cannot be in the past.
	@Test
	public void testCreateClass2() {
		this.admin.createClass("TestClass", 2016, "Instructor", 15);
        assertFalse(this.admin.classExists("TestClass", 2016));
	}
	
	// createClass(): Must NOT WORK: Max capacity > 0, case 0
	@Test
	public void testCreateClass3() {
		this.admin.createClass("TestClass", 2017, "Instructor", 0);
        assertFalse(this.admin.classExists("TestClass", 2017));
	}
	
	// createClass(): Must NOT WORK: Max capacity > 0, case -1
	@Test
	public void testCreateClass4() {
		this.admin.createClass("TestClass", 2017, "Instructor", -1);
        assertFalse(this.admin.classExists("TestClass", 2017));
	}
	
	// createClass(): Must NOT WORK: Class name / year pair is not unique.
	@Test
	public void testCreateClass5() {
		this.admin.createClass("TestClass", 2017, "Instructor", 15);
		this.admin.createClass("TestClass", 2017, "Instructor2", 20);
		this.instructor.addHomework("Instructor2","TestClass",2017,"Big Project");
	    assertFalse(this.instructor.homeworkExists("TestClass",2017,"Big Project"));
	}
	
	// createClass(): Must NOT WORK: Instructor can't teach more than two classes in a year.
	@Test
	public void testCreateClass6() {
		this.admin.createClass("TestClass", 2017, "Instructor", 15);
		this.admin.createClass("TestClass2", 2017, "Instructor", 3);
		this.admin.createClass("TestClass3", 2017, "Instructor", 3);
        assertFalse(this.admin.classExists("TestClass3", 2017));
	}
	
	// createClass(): Must WORK: Corner case: Instructor CAN teach EXACTLY two classes in a year.
	@Test
	public void testCreateClass7() {
		this.admin.createClass("TestClass", 2017, "Instructor", 15);
		this.admin.createClass("TestClass2", 2017, "Instructor", 3);
		assertFalse(this.admin.classExists("TestClass2", 2017));
	}
	
	// changeCapacity(): Must WORK: Working example - expand class 0/1 to 0/2.
	@Test
	public void testChangeCapacity() {
		this.admin.createClass("TestClass", 2017, "Instructor", 1);
	    this.admin.changeCapacity("TestClass", 2017, 2);
	    assertEquals(this.admin.getClassCapacity("TestClass", 2017),2);
	}
		
	// changeCapacity(): Must WORK: Working example - expand class 1/1 to 1/2.
	@Test
	public void testChangeCapacity2() {
		this.admin.createClass("TestClass", 2017, "Instructor", 1);
		IStudent s = new Student();
		s.registerForClass("TestStudent1", "Test", 2017);
        this.admin.changeCapacity("TestClass", 2017, 2);
        assertEquals(this.admin.getClassCapacity("TestClass", 2017),2);
	}
	
	// changeCapacity(): Must NOT WORK: try expanding class 2/2 to 2/1
	@Test
	public void testChangeCapacity3() {
		this.admin.createClass("TestClass", 2017, "Instructor", 1);
		IStudent s = new Student();
		s.registerForClass("TestStudent1", "TestClass", 2017);
		IStudent s2 = new Student();
		s2.registerForClass("TestStudent2", "TestClass", 2017);
        this.admin.changeCapacity("TestClass", 2017, 1);
        assertNotEquals(this.admin.getClassCapacity("TestClass", 2017),1);
	}
	
	// changeCapacity(): Must WORK: try expanding class 1/2 to 1/3
	@Test
	public void testChangeCapacity4() {
		this.admin.createClass("TestClass", 2017, "Instructor", 1);
		IStudent s = new Student();
		s.registerForClass("TestStudent1", "TestClass", 2017);
		IStudent s2 = new Student();
		s2.registerForClass("TestStudent2", "TestClass", 2017);
	    this.admin.changeCapacity("TestClass", 2017, 3);
	    assertEquals(this.admin.getClassCapacity("TestClass", 2017),3);
	}
	
	// changeCapacity(): Must NOT WORK: Wrong year
	@Test
	public void testChangeCapacity5() {
		this.admin.createClass("TestClass", 2017, "Instructor", 1);
		this.admin.createClass("TestClass", 2018, "Instructor", 1);
	    this.admin.changeCapacity("TestClass", 2017, 2);
	    assertEquals(this.admin.getClassCapacity("TestClass", 2018),2);
	}
	// changeCapacity(): Must NOT WORK: Wrong class
	@Test
	public void testChangeCapacity6() {
		this.admin.createClass("TestClass", 2017, "Instructor", 1);
		this.admin.createClass("TestClass2", 2017, "Instructor2", 2);
	    this.admin.changeCapacity("TestClass", 2017, 3);
	    assertEquals(this.admin.getClassCapacity("TestClass2", 2017),2);
	}
}
