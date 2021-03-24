package edu.human.com.authorrole;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.impl.SimpleLog;
import org.apache.log4j.Logger;

import egovframework.com.cmm.LoginVO;
import egovframework.rte.fdl.security.userdetails.EgovUserDetails;
import egovframework.rte.fdl.security.userdetails.jdbc.EgovUsersByUsernameMapping;

/**
 * EgovSessionMapping 클래스는 
 * context-security.xml의 쿼리결과를 변수로 담을 공간
 * @author 뚫어봐
 *
 */
public class EgovSessionMapping extends EgovUsersByUsernameMapping {

	private Logger logger = Logger.getLogger(SimpleLog.class);
	
	public EgovSessionMapping(DataSource ds, String usersByUsernameQuery) {
		super(ds, usersByUsernameQuery);
		// 생성자 메소드 
	}
	
	@Override
	protected EgovUserDetails mapRow(ResultSet rs, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		logger.debug("메세지");
		String strUserId = rs.getString("user_id");
		String strPassword = rs.getString("password");
		boolean strEnabled = rs.getBoolean("enabled");
		String strUserNm = rs.getString("user_nm");
		String strUserSe = rs.getString("user_se");
		String strUserEmail = rs.getString("user_email");
		String strOrgnztId = rs.getString("orgnzt_id");
		String strUniqId = rs.getString("esntl_id");
		String strOrgnztNm = rs.getString("orgnzt_nm");
		
		LoginVO loginVO = new LoginVO();
		loginVO.setId(strUserId);
		loginVO.setPassword(strPassword);
		loginVO.setName(strUserNm);
		loginVO.setUserSe(strUserSe);
		loginVO.setEmail(strUserEmail);
		loginVO.setOrgnztId(strOrgnztId);
		loginVO.setUniqId(strUniqId);
		loginVO.setOrgnztNm(strOrgnztNm);
		
		EgovUserDetails egovUserDetails = new EgovUserDetails(strUserId, strPassword, strEnabled, loginVO);

		return egovUserDetails;
	}

}
