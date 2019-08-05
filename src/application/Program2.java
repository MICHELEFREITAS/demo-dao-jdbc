package application;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Soundbank;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		
		DepartmentDao departmentdao = DaoFactory.createDepartmentDao();
		
		System.out.println("=== TESTE 1: department findById ==== ");
		
		Department department = departmentdao.findById(2);
		
		System.out.println(department);
		
		
		
		System.out.println("\n=== TESTE 2: department findAll ==== ");
		
		List<Department> list = departmentdao.findAll();
		
		for(Department dep : list) {
			System.out.println(dep);
		}
		
		
		System.out.println("\n=== TESTE 3: department insert ==== ");
		
		Department depnew = new Department(null, "Music");
		
		departmentdao.insert(depnew);
		
		System.out.println("Inserted! New id: " + depnew.getId());
		
	}

}
