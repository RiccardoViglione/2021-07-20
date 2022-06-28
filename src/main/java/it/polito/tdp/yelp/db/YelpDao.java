package it.polito.tdp.yelp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.yelp.model.Adiacenza;
import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Review;
import it.polito.tdp.yelp.model.User;

public class YelpDao {

	public List<Business> getAllBusiness(){
		String sql = "SELECT * FROM Business";
		List<Business> result = new ArrayList<Business>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Business business = new Business(res.getString("business_id"), 
						res.getString("full_address"),
						res.getString("active"),
						res.getString("categories"),
						res.getString("city"),
						res.getInt("review_count"),
						res.getString("business_name"),
						res.getString("neighborhoods"),
						res.getDouble("latitude"),
						res.getDouble("longitude"),
						res.getString("state"),
						res.getDouble("stars"));
				result.add(business);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Review> getAllReviews(){
		String sql = "SELECT * FROM Reviews";
		List<Review> result = new ArrayList<Review>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Review review = new Review(res.getString("review_id"), 
						res.getString("business_id"),
						res.getString("user_id"),
						res.getDouble("stars"),
						res.getDate("review_date").toLocalDate(),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("review_text"));
				result.add(review);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void getAllUsers(Map<String,User>idMap){
		String sql = "SELECT * FROM Users";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
if(!idMap.containsKey(res.getString("user_id"))) {
				User user = new User(res.getString("user_id"),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("name"),
						res.getDouble("average_stars"),
						res.getInt("review_count"));
				
idMap.put(res.getString("user_id"), user);
}
			}
			res.close();
			st.close();
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
	public List<User>getVertici(int n,Map<String,User>idMap){
		String sql="SELECT DISTINCT u.`user_id` "
				+ "from Users u, Reviews r "
				+ "where u.`user_id`=r.`user_id` "
				+ "GROUP BY u.`user_id` "
				+ "having COUNT(r.`review_id`)>=? ";
		List<User> result = new ArrayList<User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, n);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {

				result.add(idMap.get(res.getString("user_id")));
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	
	
		
	}
	public List <Adiacenza>getArchi(int anno,Map<String,User>idMap){
		String sql="SELECT u.`user_id`as u1I,u2.`user_id`as u2I,COUNT(*) as peso "
				+ "from Users u, Reviews r,`Users`u2,`Reviews`r2 "
				+ "where u.`user_id`=r.`user_id`and u.`user_id`>u2.`user_id`and u2.`user_id`=r2.`user_id`and YEAR(r.`review_date`)=? and YEAR(r2.`review_date`)=YEAR(r.`review_date`) and r.`business_id`=r2.`business_id` "
				+ "GROUP BY u.`user_id`,u2.`user_id` ";
		
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
		
		st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {

				result.add(new Adiacenza(idMap.get(res.getString("u1I")),idMap.get(res.getString("u2I")),res.getInt("peso")));
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	
				
	}
	
	
}
