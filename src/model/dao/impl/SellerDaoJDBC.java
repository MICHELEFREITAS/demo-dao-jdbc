package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	//Dao terá uma dependencia com a conexão. Esse obj conn ficará disposição qualquer lugar dessa classe
	private Connection conn;
	
	//construtor
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+"FROM seller INNER JOIN department "
					+"ON seller.DepartmentId = department.Id " 
					+ "WHERE seller.Id = ? ");
			
			//vai receber id da entrada da função
			st.setInt(1, id);
			//vem o resultado no rs
			rs = st.executeQuery();
			if(rs.next()) {
				
				//chama a função e passa o rs como argumento
				Department dep = instantiateDepartment(rs);
				
				//Instanciando o seller. Chama a função.
				Seller obj = instatiateSeller(rs, dep);
				
				return obj;
				
			}
			return null;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultset(rs);
		}

	}

	//throws SQLException - propagar a excecao
	private Seller instatiateSeller(ResultSet rs, Department dep) throws SQLException {
		
		Seller obj = new Seller();
		//obj seller com todos atributos definidos
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		
		return obj;
	}


	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		//setando dados dele
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		
		return dep;
	}


	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName " 
					+ "FROM seller INNER JOIN department " 
					+ "ON seller.DepartmentId = department.Id " 
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			
			st.setInt(1, department.getId());
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			
			//chave é o Integer que é o id do departament e o valor de cada obj será do tipo department
			//Map vazio que vai guardar o department que eu instanciar
			Map<Integer, Department> map = new HashMap<>();
			
			//Percorrer rs enquanto tiver um próximo
			while(rs.next()) {
				
				//vê se o department existe
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep==null) {
					//vai instanciar depart com o rs
					dep = instantiateDepartment(rs);
					//salvar dentro map para que na proxima vez verifica e ve que ele existe
					map.put(rs.getInt("DepartmentId"), dep);
				}
		
				//instancia vendedor apontando para o dep, seja ele existente ou um novo depart.
				Seller obj = instatiateSeller(rs, dep);
				
				//adicionar vendedor na lista
				list.add(obj);
			}
			return list;
		}catch(SQLException e){
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultset(rs);
		}
	}

}
