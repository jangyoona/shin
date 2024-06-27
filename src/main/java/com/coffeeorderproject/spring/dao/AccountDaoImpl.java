package com.coffeeorderproject.spring.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.tags.shaded.org.apache.xalan.res.XSLTErrorResources_zh_TW;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.coffeeorderproject.spring.dto.UserDto;

import lombok.Setter;


public class AccountDaoImpl implements AccountDao {
	
	@Setter
	private JdbcTemplate jdbcTemplate;
	
	private Connection connection() throws Exception {
		
//		Class.forName("oracle.jdbc.driver.OracleDriver");
//		return DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521/xe", "green_cloud", "oracle");
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://3.37.123.170:3306/hollys?serverTimezone=UTC", "hollys", "mysql");
	}
	
	@Override
	public void insertUser(UserDto user) {
		
		String sql = "INSERT INTO user (userid, username, usernickname, userphone, useremail, userpw) "
					+ "VALUES (?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, user.getUserId(), user.getUserName(), user.getUserNickname(), user.getUserPhone(), 
								 user.getUserEmail(), user.getUserPw());
		
	}
	
	
	@Override
	public Boolean idCheck(UserDto user) {

		String sql = "SELECT COUNT(userid) from user WHERE userid = ?";

		int isHave = jdbcTemplate.queryForObject(sql, 
				new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt(1);
			}
		},
				user.getUserId());
		
		return isHave < 1;
			
	}
	
	@Override
	public UserDto selectUser(UserDto user) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserDto member = null;
		
		try {
			connection = connection();
			
			// 3. 명령 객체 만들기 // c.userid IS NOT NULL << (쿠폰 있으면1 없으면0)
			String sql = "SELECT u.userid, username, usernickname, userphone, useremail, userpw, useradmin, userregidate, useractive, c.couponid "
					+ "FROM user u "
					+ "LEFT JOIN coupon c ON u.userid = c.userid "
					+ "WHERE u.userid = ? AND u.userpw = ? AND u.useractive = false";		
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getUserPw());
			
			// 4. 명령 실행 (결과가 있으면 결과 저장 - select 인 경우)
			rs = pstmt.executeQuery();
			
			// 5. 결과가 있으면 결과 처리
			if (rs.next()) {
				member = new UserDto();
				member.setUserId(rs.getString(1));
				member.setUserName(rs.getString(2));
				member.setUserNickname(rs.getString(3));
				member.setUserPhone(rs.getString(4));
				member.setUserEmail(rs.getString(5));
				member.setUserPw(rs.getString(6));
				member.setUserAdmin(rs.getBoolean(7));
				member.setUserRegidate(rs.getDate(8));
				member.setUserActive(rs.getBoolean(9));
				// member.setCouponId(rs.getInt(10));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 6. 연결 종료
			try { rs.close(); } catch (Exception e) {}
			try { pstmt.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
		}
		
		return member;
	}
	
	@Override
	public UserDto selectUserEmail(String id) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserDto member = null;
		
		try {
			connection = connection();
			
			// 3. 명령 객체 만들기
			String sql = "SELECT useremail from user WHERE userid = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, id);
			
			// 4. 명령 실행 (결과가 있으면 결과 저장 - select 인 경우)
			rs = pstmt.executeQuery();
			
			// 5. 결과가 있으면 결과 처리
			if (rs.next()) {
				member = new UserDto();
				member.setUserEmail(rs.getString(1));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 6. 연결 종료
			try { rs.close(); } catch (Exception e) {}
			try { pstmt.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
		}
		
		return member;
	}
	
	@Override
	public void updateUserPw(String userId, String newPw) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		
		try {
			connection = connection();
			
			
			// 3. 명령 객체 만들기
			String sql = "UPDATE user SET userpw = ? WHERE userid = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, newPw);
			pstmt.setString(2, userId);
			
			// 4. 명령 실행 (결과가 있으면 결과 저장 - select 인 경우)
			pstmt.executeUpdate();
			
			// 5. 결과가 있으면 결과 처리
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 6. 연결 종료
			try { pstmt.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
		}
	}
	@Override
	public void updateUser(String userId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
	
		try {
			conn = connection();
	
			String sql = "UPDATE user SET useractive = 1 WHERE userid = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
	
			pstmt.executeUpdate();
	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 6. 연결 종료
			try {
				pstmt.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}	
	
}