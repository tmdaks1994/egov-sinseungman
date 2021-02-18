package edu.human.com.admin.web;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.human.com.member.service.EmployerInfoVO;
import edu.human.com.member.service.MemberService;
import edu.human.com.util.PageVO;
import egovframework.let.utl.sim.service.EgovFileScrty;

@Controller
public class AdminController {
	
	@Inject
	private MemberService memberService;
	
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
	public String list_member(Model model,PageVO pageVO) throws Exception {
		if(pageVO.getPage() == null) {
			pageVO.setPage(1);
		}		
		pageVO.setPerPageNum(5);//하단의 페이징보여줄 개수
		pageVO.setQueryPerPageNum(10);//쿼리에서 1페이당 보여줄 개수=화면에서 1페이당 보여줌
		List<EmployerInfoVO> listMember = memberService.selectMember(pageVO);
		model.addAttribute("listMember", listMember);
		return "admin/member/list_member";
	}
	
	@RequestMapping(value="/admin/home.do", method=RequestMethod.GET)
	public String home() throws Exception {
		//관리자 메인 페이지로 이동
		return "admin/home";
	}
}