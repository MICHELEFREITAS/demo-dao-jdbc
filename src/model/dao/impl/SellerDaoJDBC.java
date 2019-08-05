package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

}
