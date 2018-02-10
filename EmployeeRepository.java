import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;

public class DatabaseGUI extends JFrame implements ActionListener {

	public Container contentPane; // container object
	public JTextArea textA; // text area object
	public String txt1; // for reading input method name
	public JTextField input1; // text field object
	public JButton load, save; // button object
	Database d = new Database(); // database object for file stream
	Employee q = new Employee(); // Employee object for file stream

	public DatabaseGUI() {
		setSize(1000, 400); // frame properties
		setLocation(100, 200);
		contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout()); // layout

		// adding labels, fields and text area accordingly
		JLabel text1 = new JLabel("Enter function"); // Text field
		contentPane.add(text1);
		input1 = new JTextField();
		input1.setColumns(80);
		input1.addActionListener(this);
		contentPane.add(input1);

		textA = new JTextArea(); // Text Area
		textA.setColumns(20);
		textA.setRows(10);
		textA.setBorder(BorderFactory.createLineBorder(Color.black));
		textA.setText("");
		contentPane.add(textA);

		load = new JButton("Load"); // Load button
		load.addActionListener(this);
		contentPane.add(load);
		save = new JButton("Save"); // Save button
		save.addActionListener(this);
		contentPane.add(save);
	}

	public void actionPerformed(ActionEvent e) { // event method
		File inFile = new File("C:/data/IDS401.dat"); // attaching file for
														// input and output
		File outFile = new File("C:/data/IDS401.dat");
		if (e.getSource() instanceof JTextField) {
			JTextField textField = (JTextField) e.getSource();
			String t = textField.getText();
			txt1 = input1.getText(); // splitting string to form parameters
			String[] splitArray = txt1.split(" ");
			// the object of split array act as parameter for add, delete modify
			// and read, and are parsed and passed according to need
			if (t.startsWith("add")) { // for add method

				d.add(splitArray[1], splitArray[2], splitArray[3], Integer.parseInt(splitArray[4]),
						Integer.parseInt(splitArray[5]));
				textA.append(" Employee added \n");

			}

			else if (t.startsWith("delete")) { // for delete method
				d.delete(splitArray[1], splitArray[2], splitArray[3], Integer.parseInt(splitArray[4]),
						Integer.parseInt(splitArray[5]));
				textA.append(d.delemp() + " Employees deleted \n");
			}

			else if (t.startsWith("read")) { // for read method
				d.read(splitArray[1], splitArray[2]);
				int p = 0;

				while (d.read1[p] != null) {
					{
						textA.append(".Last Name: " + d.read1[p].getLname() + " First Name: " + d.read1[p].getFname()
								+ " Department: " + d.read1[p].getdepartment() + " ID: " + d.read1[p].getid()
								+ " JobCode:" + d.read1[p].getjobcode() + "\n");
						p++;
					}
				}
				if (p == 0) {
					textA.append(" employee not found  \n");
				}
			}

			else if (t.startsWith("modify")) { // for modify method
				d.modify(splitArray[1], splitArray[2], splitArray[3]);
				textA.append(d.modemp() + "  Employees modified \n");
			}

		}

		else if (e.getSource() instanceof JButton) {
			JButton click = (JButton) e.getSource();

			if (click == load) { // load button

				try {

					FileInputStream inFileStream = new FileInputStream(inFile); // input
																				// stream
					ObjectInputStream inObjectStream = new ObjectInputStream(inFileStream); // object
																							// stream

					while (true) {
						Object obj = inObjectStream.readObject(); // search for
																	// object in
																	// file

						if (obj instanceof Employee) { // if object is instance
														// of Employee, adds the
														// object in the
														// database
							q.setDepartment(((Employee) obj).getdepartment());
							q.setFirstName(((Employee) obj).getFname());
							q.setLastName(((Employee) obj).getLname());
							q.setId(((Employee) obj).getid());
							q.setjobcode(((Employee) obj).getjobcode());
							d.add(q.getLname(), q.getFname(), q.getdepartment(), q.getid(), q.getjobcode());
						} else
							break;
					}
					JOptionPane.showMessageDialog(null, "Database Loaded ");
					inObjectStream.close(); // close input object stream

				} catch (Exception m) {

					// TODO Auto-generated catch block
					System.out.println("");
				}

			}

			if (click == save) { // save button functions

				try {
					int k = 0;
					FileOutputStream fout = new FileOutputStream(outFile);
					ObjectOutputStream outObjectStream = new ObjectOutputStream(fout); // output
																						// stream
					for (k = 0; k < d.counter; k++) {
						outObjectStream.writeObject(d.e[k]); // saves the
																// database
																// array objects
																// to file
					}
					JOptionPane.showMessageDialog(null, "Entry saved in database ");
					textA.append("entry saved");
					outObjectStream.close(); // close output object stream
				} catch (Exception l) {
					System.out.println("not found");
				}

			}

		}

	}

	public static void main(String args[]) {

		DatabaseGUI f = new DatabaseGUI();
		f.setVisible(true);

	}

	class Database implements Serializable {
		// initializing the Employee array and counter//
		public Employee[] e = new Employee[0];
		int counter = 0;
		/*
		 * a counter for the current number of Employees in the array,
		 * initialized to zero
		 */

		Employee read1[]; // array to save the match from employee array

		int l = 0; // counter for modified employee

		public int modemp() {
			return l;
		}

		int c = 0; // counter for deleted employee

		public int delemp() {
			return c;
		}

		/*
		 * match method definition, to match value of an attribute of the
		 * Employee object array (E) matchValue and field are string parameters
		 * in the method. If match is found, the method would return boolean
		 * true or else false.
		 */
		public boolean match(String field, String matchValue) {
			int i; /* loop variables */

			Boolean x = false; /* return boolean variable initialization */
			/* loop searches for field name and matches the object value */
			for (i = 0; i < counter; i++) {
				if (field.equals("lastName") && e[i].getLname().equals(matchValue))
					x = true;
				else if (field.equals("firstName") && e[i].getFname().equals(matchValue))
					x = true;
				else if (field.equals("department") && e[i].getdepartment().equals(matchValue))
					x = true;

			}
			return x;
		}

		/*
		 * modify method definition to match and modify value of an attribute of
		 * the matchValue, newValue and field are parameters, method search the
		 * 'field', and then changes the 'matchValue' for particular field with
		 * 'newValue'.
		 */

		public void modify(String field, String matchValue, String newValue) {
			int i, j = 0; /* loop variables */
			l = 0;

			for (i = 0; i < counter; i++) {
				if (field.equals("lastName") && e[i].getLname().equals(matchValue)) {
					e[i].setLastName(newValue);
					l++;
				} else if (field.equals("firstName") && e[i].getFname().equals(matchValue)) {
					e[i].setFirstName(newValue);
					l++;
				} else if (field.equals("department") && e[i].getdepartment().equals(matchValue)) {
					e[i].setDepartment(newValue);
					l++;
				}

			}

		}

		/*
		 * add method definition, to add new object referring employee
		 * information in parameter array 'e' parameters are three strings, 2
		 * integers and an employee array Employee class methods are called to
		 * put parameter value to object
		 */
		public void add(String lastName, String firstName, String department, int id, int jobcode) {

			Employee e1 = new Employee(); /* new employee object created */
			/*
			 * parameter values are passed in the new employee object variables
			 */
			e1.setFirstName(firstName);
			e1.setLastName(lastName);
			e1.setDepartment(department);
			e1.setId(id);
			e1.setjobcode(jobcode);
			int i = 0; /* loop variable */
			/* to find the last used object of array */
			/* updating counter variable */

			Employee[] new1 = new Employee[e.length + 1];
			// for loop to go thorough the list one by one
			for (int a = 0; a < e.length; a++) {
				// value is stored here in the new list from the old one
				new1[a] = e[a];
			}
			// all the values of the itemLists are stored in a bigger array
			// named new1
			e = new1;

			while (e[i] != null) {
				i++;
			}
			e[i] = e1;
			counter++;

		}
		/*
		 * delete method definition to delete object from the parameter array 3
		 * string variable, 2 integer and an employee object array as
		 * parameters, referring to class Employee if parameter values matches
		 * the array object value, object is deleted
		 */

		public void delete(String lastName, String firstName, String department, int id, int jobcode) {
			int i = 0;
			c = 0;
			while (i < counter) {
				if (e[i].getLname().equals(lastName) && e[i].getFname().equals(firstName)
						&& e[i].getdepartment().equals(department) && e[i].getid() == id
						&& e[i].getjobcode() == (jobcode)) {

					e[i] = e[counter - 1]; /*
											 * matched object is overwritten by
											 * last filled index of array
											 */
					e[counter - 1] = null; /* last filled object is nulled */
					i--; /* reverse counter for the for loop */
					counter--; /* counter variable updated */
					c++;
				}
				i++; /* incrementing variable for loop */

			}
			JOptionPane.showMessageDialog(null, c + " employee added deleted \n");
			// a1.setVisible(true);

		}
		/*
		 * read method definition to print the values of object of object array.
		 * field and match value as parameters if the values are matched,
		 * employee information is printed
		 */

		public void read(String field, String matchValue) {
			read1 = new Employee[e.length + 1];
			int x = 0;

			for (int i = 0; i < e.length; i++) {

				if (e[i] != null) { /* to search only filled indexes */
					if (field.equals("department") && e[i].department.equals(matchValue)) {
						System.out.println(".Last Name: " + e[i].getLname() + " First Name: " + e[i].getFname()
								+ " Department: " + e[i].getdepartment() + " ID: " + e[i].getid() + " JobCode:"
								+ e[i].getjobcode() + "\n");
						read1[x] = e[i];
						x++;

					} else if (field.equals("jobCode") && e[i].jobcode == Integer.parseInt(matchValue)) {
						System.out.println(".Last Name: " + e[i].getLname() + " First Name: " + e[i].getFname()
								+ " Department: " + e[i].getdepartment() + " ID: " + e[i].getid() + " JobCode:"
								+ e[i].getjobcode() + "\n");
						read1[x] = e[i];
						x++;

					} else {

						System.out.println(" \n No result found ");
					}

				}
			}

		}
	}

	public class Employee implements Serializable { // employee class
		private String lastName, firstName, department;
		private int id, jobcode;

		public void setLastName(String Lname) {
			lastName = Lname;
		}

		public void setFirstName(String Fname) {
			firstName = Fname;
		}

		public void setDepartment(String Dept) {
			department = Dept;
		}

		public void setId(int Id1) {
			id = Id1;
		}

		public void setjobcode(int jd) {
			jobcode = jd;
		}

		public int getid() {
			return id;
		}

		public int getjobcode() {
			return jobcode;
		}

		public String getFname() {
			return firstName;
		}

		public String getLname() {
			return lastName;
		}

		public String getdepartment() {
			return department;
		}

	}

}
