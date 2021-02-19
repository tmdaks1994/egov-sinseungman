package edu.human.com.admin.web;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.human.com.member.service.EmployerInfoVO;
import edu.human.com.member.service.MemberService;
import edu.human.com.util.PageVO;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.let.cop.bbs.service.BoardMasterVO;
import egovframework.let.cop.bbs.service.BoardVO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import egovframework.let.cop.bbs.service.EgovBBSManageService;
import egovframework.let.utl.sim.service.EgovFileScrty;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class AdminController {
	
	@Inject
	private MemberService memberService;
	
	@Autowired
	private EgovBBSAttributeManageService bbsAttrbService;

	@Autowired
	private EgovPropertyService propertyService;
	
	@Autowired
	private EgovBBSManageService bbsMngService;
	
	@RequestMapping(value="/admin/board/view_board.do")
	public String view_board(@ModelAttribute("searchVO") BoardVO boardVO, ModelMap model) throws Exception {
		LoginVO user = new LoginVO();
	    if(EgovUserDetailsHelper.isAuthenticated()){
	    	user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		}

		// 조회수 증가 여부 지정
		boardVO.setPlusCount(true);

		//---------------------------------
		// 2009.06.29 : 2단계 기능 추가
		//---------------------------------
		if (!boardVO.getSubPageIndex().equals("")) {
		    boardVO.setPlusCount(false);
		}
		////-------------------------------

		boardVO.setLastUpdusrId(user.getUniqId());
		BoardVO vo = bbsMngService.selectBoardArticle(boardVO);

		model.addAttribute("result", vo);

		model.addAttribute("sessionUniqId", user.getUniqId());

		//----------------------------
		// template 처리 (기본 BBS template 지정  포함)
		//----------------------------
		BoardMasterVO master = new BoardMasterVO();

		master.setBbsId(boardVO.getBbsId());
		master.setUniqId(user.getUniqId());

		BoardMasterVO masterVo = bbsAttrbService.selectBBSMasterInf(master);

		if (masterVo.getTmplatCours() == null || masterVo.getTmplatCours().equals("")) {
		    masterVo.setTmplatCours("/css/egovframework/cop/bbs/egovBaseTemplate.css");
		}

		model.addAttribute("brdMstrVO", masterVo);

		return "admin/board/view_board";
	}
	@RequestMapping(value="/admin/board/list_board.do")
	public String list_board(@ModelAttribute("searchVO") BoardVO boardVO, ModelMap model) throws Exception {
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();

		boardVO.setBbsId(boardVO.getBbsId());
		boardVO.setBbsNm(boardVO.getBbsNm());

		BoardMasterVO vo = new BoardMasterVO();

		System.out.println("디버그: 게시판아이디는 "+boardVO.getBbsId());
		vo.setBbsId(boardVO.getBbsId());
		vo.setUniqId(user.getUniqId());

		BoardMasterVO master = bbsAttrbService.selectBBSMasterInf(vo);

		//-------------------------------
		// 방명록이면 방명록 URL로 forward
		//-------------------------------
		if (master.getBbsTyCode().equals("BBST04")) {
		    return "forward:/cop/bbs/selectGuestList.do";
		}
		////-----------------------------

		boardVO.setPageUnit(propertyService.getInt("pageUnit"));
		boardVO.setPageSize(propertyService.getInt("pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();

		paginationInfo.setCurrentPageNo(boardVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(boardVO.getPageUnit());
		paginationInfo.setPageSize(boardVO.getPageSize());

		boardVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		boardVO.setLastIndex(paginationInfo.getLastRecordIndex());
		boardVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		Map<String, Object> map = bbsMngService.selectBoardArticles(boardVO, vo.getBbsAttrbCode());
		int totCnt = Integer.parseInt((String)map.get("resultCnt"));

		paginationInfo.setTotalRecordCount(totCnt);

		//-------------------------------
		// 기본 BBS template 지정
		//-------------------------------
		if (master.getTmplatCours() == null || master.getTmplatCours().equals("")) {
		    master.setTmplatCours("/css/egovframework/cop/bbs/egovBaseTemplate.css");
		}
		////-----------------------------

		model.addAttribute("resultList", map.get("resultList"));
		model.addAttribute("resultCnt", map.get("resultCnt"));
		model.addAttribute("boardVO", boardVO);
		model.addAttribute("brdMstrVO", master);
		model.addAttribute("paginationInfo", paginationInfo);
		
		return "admin/board/list_board";
	}
	
	@RequestMapping(value="/admin/member/delete_member.do",method=RequestMethod.POST)
	public String delete_member(EmployerInfoVO memberVO, RedirectAttributes rdat) throws Exception {
		
		memberService.deleteMember(memberVO.getEMPLYR_ID());
		rdat.addFlashAttribute("msg","삭제");
		return "redirect:/admin/member/list_member.do";
	}
	@RequestMapping(value="/admin/member/view_member.do",method=RequestMethod.GET)
	public String view_member(Model model,@RequestParam("emplyr_id") String emplyr_id) throws Exception {
		EmployerInfoVO memberVO = memberService.viewMember(emplyr_id);
		model.addAttribute("memberVO",memberVO);
		
		//System.out.println("디버그:" + memberService.selectCodeMap("COM999"));
		//맵결과: 디버그:{P={CODE=P, CODE_NM=활성}, S={CODE=S, CODE_NM=비활성}}
		model.addAttribute("codeMap", memberService.selectCodeMap("COM999"));
		model.addAttribute("codeGroup", memberService.selectGroupMap());
		return "admin/member/view_member";
	}
	
	@RequestMapping(value="/admin/member/insert_member.do",method=RequestMethod.GET)
	public String insert_member(Model model) throws Exception {
		model.addAttribute("codeMap", memberService.selectCodeMap("COM999"));
		model.addAttribute("codeGroup", memberService.selectGroupMap());
		return "admin/member/insert_member";
	}
	
	@RequestMapping(value="/admin/member/insert_member.do",method=RequestMethod.POST)
	public String insert_member(EmployerInfoVO memberVO,RedirectAttributes rdat) throws Exception {
		
		String formpassword = memberVO.getPASSWORD();
		String encPassword = EgovFileScrty.encryptPassword(formpassword, memberVO.getEMPLYR_ID());
		memberVO.setPASSWORD(encPassword);
		memberVO.setESNTL_ID("USERCNFRM_" + memberVO.getEMPLYR_ID());
		memberService.insertMember(memberVO);
		rdat.addFlashAttribute("msg","입력");
		return "redirect:/admin/member/list_member.do";
	}
	
	@RequestMapping(value="/admin/member/update_member.do",method=RequestMethod.POST)
	public String update_member(EmployerInfoVO memberVO,RedirectAttributes rdat) throws Exception {
		
		if(memberVO.getPASSWORD() != null) {
			String formPassword = memberVO.getPASSWORD();
			String encPassword = EgovFileScrty.encryptPassword(formPassword,memberVO.getEMPLYR_ID());
			memberVO.setPASSWORD(encPassword);
		}
		memberService.updateMember(memberVO);
		rdat.addFlashAttribute("msg","수정");
		return "redirect:/admin/member/view_member.do?emplyr_id=" +memberVO.getEMPLYR_ID();
	}
	
	@RequestMapping(value="/admin/member/list_member.do",method=RequestMethod.GET)
	public String list_member(Model model,@ModelAttribute("pageVO") PageVO pageVO) throws Exception {
		if(pageVO.getPage() == null) {
			pageVO.setPage(1);
		}		
		pageVO.setPerPageNum(5);//하단의 페이징보여줄 개수
		pageVO.setQueryPerPageNum(10);//쿼리에서 1페이당 보여줄 개수=화면에서 1페이당 보여줌
		
		List<EmployerInfoVO> listMember = memberService.selectMember(pageVO);
		System.out.println("현재 검색된 회원의 수 : "+listMember.size());
		pageVO.setTotalCount(listMember.size());
		model.addAttribute("listMember", listMember);
		
		return "admin/member/list_member";
	}
	
	@RequestMapping(value="/admin/home.do", method=RequestMethod.GET)
	public String home() throws Exception {
		//관리자 메인 페이지로 이동
		return "admin/home";
	}
}