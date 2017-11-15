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

public class TestStudent {
	private IAdmin admin;
	private IInstructor instructor;
	private IStudent student;
	
	@Before
	public void setup() {
		this.admin = new Admin();
		this.student = new Student();
	}
	
	// registerForClass(): Must WORK: Working example
	@Test
	public void testRegisterForClass() {
		this.admin.createClass("TestClass", 2017, "TestInstructor", 1);
        this.student.registerForClass("TestStudent", "TestClass", 2017);
        assertTrue(this.student.isRegisteredFor("TestStudent", "TestClass", 2017));
	}
	
	// registerForClass(): Must NOT WORK: Class doesn't exist.
	@Test
	public void testRegisterForClass2() {
		this.admin.createClass("TestClass", 2017, "TestInstructor", 1);
        this.student.registerForClass("TestStudent", "BogusClass", 2017);
        assertFalse(this.student.isRegisteredFor("TestStudent", "BogusClass", 2017));
	}
	
	// registerForClass(): Must NOT WORK: Can't enroll in class at max capacity.
	@Test
	public void testRegisterForClass3() {
		this.admin.createClass("TestClass", 2017, "TestInstructor", 1);
        this.student.registerForClass("TestStudent", "TestClass", 2017);
        IStudent s2 = new Student();
        s2.registerForClass("TestStudent2", "TestClass", 2017);
        assertFalse(s2.isRegisteredFor("TestStudent2", "TestClass", 2017));
	}
	
	// dropClass(): Must WORK: Working example
	@Test
	public void testDropClass() {
		this.admin.createClass("TestClass", 2017, "TestInstructor", 1);
		boolean indicator;
        this.student.registerForClass("TestStudent", "TestClass", 2017);
		if(this.student.isRegisteredFor("TestStudent", "TestClass", 2017))
			indicator = true;
		else
			indicator = false;
        this.student.dropClass("TestStudent", "TestClass", 2017);
        assertFalse(!indicator && this.student.isRegisteredFor("TestStudent", "TestClass", 2017));
	}
	
	// dropClass(): Must NOT WORK: Student is not registered for class.
	@Test
	public void testDropClass2() {
		this.admin.createClass("TestClass", 2017, "TestInstructor", 1);
		
		// Make sure that student was never registered in the first place
		boolean indicator;
		if(this.student.isRegisteredFor("TestStudent", "TestClass", 2017))
			indicator = true;
		else
			indicator = false;
		
        this.student.dropClass("TestStudent", "BogusClass", 2017);
        
        // Make sure that if the indicator is false, and student dropped class, student still isn't in the class.
        assertFalse(indicator && this.student.isRegisteredFor("TestStudent", "BogusClass", 2017));

	}
	
	// submitHomework(): Must WORK: Working Example
	@Test
	public void testSubmitHomework() {
		this.admin.createClass("TestClass", 2017, "TestInstructor", 1);
		instructor = new Instructor();
		this.student.registerForClass("TestStudent", "TestClass", 2017);
        this.instructor.addHomework("TestInstructor", "TestClass", 2017, "Big Project");
        this.student.submitHomework("TestStudent", "Big Project", "original answer", "TestClass", 2017);
        assertTrue(this.student.hasSubmitted("TestStudent", "Big Project", "TestClass", 2017));
	}
	
	// submitHomework(): Must NOT WORK: Homework doesn't exist.
	@Test
	public void testSubmitHomework2() {
		this.admin.createClass("TestClass", 2017, "TestInstructor", 1);
		instructor = new Instructor();
		this.student.registerForClass("TestStudent", "TestClass", 2017);
        this.student.submitHomework("TestStudent", "Big Project", "original answer", "TestClass", 2017);
        assertFalse(this.student.hasSubmitted("TestStudent", "Big Project", "TestClass", 2017));
	}
	
	// submitHomework(): Must NOT WORK: Student is not registered for class.
	@Test
	public void testSubmitHomework3() {
		this.admin.createClass("TestClass", 2017, "TestInstructor", 1);
		instructor = new Instructor();
        this.instructor.addHomework("TestInstructor", "TestClass", 2017, "Big Project");
        this.student.submitHomework("TestStudent", "Big Project", "original answer", "TestClass", 2017);
        assertFalse(this.student.hasSubmitted("TestStudent", "Big Project", "TestClass", 2017));
	}
	
	// submitHomework(): Must NOT WORK: Class taught not in current year.
	@Test
	public void testSubmitHomework4() {
		this.admin.createClass("TestClass", 2018, "TestInstructor", 1);
		instructor = new Instructor();
		this.student.registerForClass("TestStudent", "TestClass", 2018);
	    this.instructor.addHomework("TestInstructor", "TestClass", 2018, "Big Project");
	    this.student.submitHomework("TestStudent", "Big Project", "original answer", "TestClass", 2018);
	    assertFalse(this.student.hasSubmitted("TestStudent", "Big Project", "TestClass", 2018));
	}
}