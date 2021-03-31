package edu.human.com.authorrole.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import edu.human.com.authorrole.service.AuthorRoleVO;
import edu.human.com.common.EgovComAbstractMapper;
import edu.human.com.util.PageVO;

@Repository
public class AuthorRoleDAO extends EgovComAbstractMapper{
	
	public void deleteAuthorRole(int AUTHORROLE_ID) throws Exception{
		delete("authorroleMapper.deleteAuthorRole", AUTHORROLE_ID);
	}
	public void insertAuthorRole(AuthorRoleVO authorRoleVO)throws Exception{
		insert("authorroleMapper.insertAuthorRole",authorRoleVO);
	}
	public AuthorRoleVO viewAuthorRole(int AUTHORROLE_ID)throws Exception {
		return selectOne("authorroleMapper.viewAuthorRole", AUTHORROLE_ID);
	}
	public int countAuthorRole(PageVO pageVO) throws Exception{
		return selectOne("authorroleMapper.countAuthorRole", pageVO);
	}
	public List<AuthorRoleVO> selectAuthorRole(PageVO pageVO) throws Exception {
		//pageVO는 초기값이 필요
		List<AuthorRoleVO> authorRoleList = selectList("authorroleMapper.selectAuthorRole",pageVO);
		return authorRoleList;
	}
	public void updateAuthorRole(AuthorRoleVO authorRoleVO) {
		update("authorroleMapper.updateAuthorRole",authorRoleVO);
	}
}
