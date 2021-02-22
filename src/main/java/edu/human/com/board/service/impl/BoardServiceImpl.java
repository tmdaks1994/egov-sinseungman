package edu.human.com.board.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import edu.human.com.board.service.BoardService;

@Service
public class BoardServiceImpl implements BoardService {

	@Inject
	private BoardDAO boardDAO;
	
	@Override
	public Integer delete_board(Integer nttId) throws Exception {
		// DAO 호출
		return boardDAO.delete_board(nttId);
	}
}
