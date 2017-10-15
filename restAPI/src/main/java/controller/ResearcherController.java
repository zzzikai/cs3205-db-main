package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Date;

import entity.Researcher;
import utils.db.MySQLAccess;

public class ResearcherController {
	
	public Researcher login(Researcher login) {
		String sql = "SELECT * FROM researcher WHERE researcher_username = ? AND password = ?";
		Researcher researcher = new Researcher();
		Connection connect = MySQLAccess.connectDatabase();
		
		//GET RESEARCHER ENTITY BASED ON USERNAME
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, login.getResearcher_username());
			ps.setString(2, login.getPassword());
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			//System.out.println("Sucessfully retrieved user!");
			
			while (rs.next()) {
				researcher.setResearcher_id(rs.getInt(1));
				researcher.setResearcher_username(rs.getString(2));
				researcher.setPassword(rs.getString(3));
				researcher.setFirstname(rs.getString(4));
				researcher.setLastname(rs.getString(5));
				researcher.setNric(rs.getString(6));
				researcher.setDob(new Date(rs.getDate(7).getTime()));
				researcher.setGender(rs.getString(8));
				researcher.setPhone1(rs.getString(9));
				researcher.setPhone2(rs.getString(10));
				researcher.setAddress1(rs.getString(11));
				researcher.setAddress2(rs.getString(12));
				researcher.setZipcode1(rs.getInt(13));
				researcher.setZipcode2(rs.getInt(14));
				researcher.setQualification(rs.getString(15));
				researcher.setQualification_name(rs.getString(16));
			}
			
			//System.out.println(researcher.getNric() + researcher.getQualifcation() + researcher.getAddress2());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(researcher.getResearcher_id() >= 0) {
			sql = "SELECT category_id FROM researcher_category WHERE researcher_id = ? AND approval_status = 'Approved'";
			ArrayList<Integer> researchCategory = new ArrayList<Integer>();
			try {
				PreparedStatement ps = connect.prepareStatement(sql);
				ps.setInt(1, researcher.getResearcher_id());
				ResultSet rs = MySQLAccess.readDataBasePS(ps);
				
				while(rs.next()) {
					researchCategory.add(rs.getInt(1));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			researcher.setResearchCategory(researchCategory);
		}
		
		ArrayList<Integer> categories = researcher.getResearchCategory();
		
		for(int i : categories) {
			System.out.println("Category ID: " + i);
		}
		
		MySQLAccess.close();
		return researcher;
	}
	
	public boolean addResearcher(Researcher researcher) {
		String sql = "INSERT INTO researcher (researcher_username, password, firstname, lastname, nric, dob, gender,"
				+ "phone1, phone2, address1, address2, zipcode1, zipcode2, qualification, qualification_name) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		Connection connect = MySQLAccess.connectDatabase();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, researcher.getResearcher_username());
			ps.setString(2, researcher.getPassword());
			ps.setString(3, researcher.getFirstname());
			ps.setString(4, researcher.getLastname());
			ps.setString(5, researcher.getNric());
			ps.setDate(6, new java.sql.Date(researcher.getDob().getTime()));
			ps.setString(7, researcher.getGender());
			ps.setString(8, researcher.getPhone1());
			ps.setString(9, researcher.getPhone2());
			ps.setString(10, researcher.getAddress1());
			ps.setString(11, researcher.getAddress2());
			ps.setInt(12, researcher.getZipcode1());
			ps.setInt(13, researcher.getZipcode2());
			ps.setString(14, researcher.getQualification());
			ps.setString(15, researcher.getQualification_name());
			
			MySQLAccess.updateDataBasePS(ps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MySQLAccess.close();
			return false;
		}
		
		MySQLAccess.close();
		return true;
	}
	
	public boolean deleteResearcher(int researcher_id) {
		String sql = "DELETE FROM researcher WHERE researcher_id = ?";
		
		Connection connect = MySQLAccess.connectDatabase();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setInt(1, researcher_id);
			
			MySQLAccess.updateDataBasePS(ps);
		}catch (Exception e) {
			e.printStackTrace();
			MySQLAccess.close();
			return false;
		}
		
		MySQLAccess.close();
		return true;
	}
	
	//TODO: UPDATE RESEARCHER
}