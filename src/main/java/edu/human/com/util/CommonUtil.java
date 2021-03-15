package edu.human.com.util;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import edu.human.com.member.service.EmployerInfoVO;
import edu.human.com.member.service.MemberService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.let.uat.uia.service.EgovLoginService;
import egovframework.rte.fdl.string.EgovObjectUtil;

@Controller
public class CommonUtil {
	@Inject
	private MemberService memberService;
	
	@Autowired
	private EgovLoginService loginService;
	@Autowired
	private EgovMessageSource egovMessageSource;
	/**
	 * 기존 로그인 처리는 egov것 그대로 사용하고,
	 * 단, 로그은 처리 이후 이동할 페이지를 OLD에서 NEW로 변경합니다.
	 */
	
	public Boolean getAuthorities() throws Exception {
		Boolean authority = Boolean.FALSE;
		//인증체크(로그인 상태인지, 아닌지 판단)
		if (EgovObjectUtil.isNull((LoginVO) RequestContextHolder.getRequestAttributes().getAttribute("LoginVO", RequestAttributes.SCOPE_SESSION))) {
			return authority;
		}
		//권한체크(관리자인지, 일반사용자인지 판단)
		LoginVO sessionLoginVO = (LoginVO) RequestContextHolder.getRequestAttributes().getAttribute("LoginVO", RequestAttributes.SCOPE_SESSION);
		EmployerInfoVO memberVO = memberService.viewMember(sessionLoginVO.getId());
		if( "GROUP_00000000000000".equals(memberVO.getGROUP_ID()) ) {
			authority = Boolean.TRUE;
		}
		//여기까지 true값을 가져오면, 관리자라고 명시.
		return authority;
	}
	@RequestMapping(value = "/login_action.do")//변경1
	public String actionLogin(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, ModelMap model) throws Exception {

		// 1. 일반 로그인 처리
		LoginVO resultVO = loginService.actionLogin(loginVO);

		boolean loginPolicyYn = true;

		if (resultVO != null && resultVO.getId() != null && !resultVO.getId().equals("") && loginPolicyYn) {
			//로그인 성공시
			request.getSession().setAttribute("LoginVO", resultVO);
			//로그인 성공후 관리자그룹일때 관리자 세션 ROLE_ADMIN명 추가
			LoginVO sessionLoginVO = (LoginVO) RequestContextHolder.getRequestAttributes().getAttribute("LoginVO", RequestAttributes.SCOPE_SESSION);
			EmployerInfoVO memberVO = memberService.viewMember(sessionLoginVO.getId());
			if( "GROUP_00000000000000".equals(memberVO.getGROUP_ID()) ) {
				request.getSession().setAttribute("ROLE_ADMIN", memberVO.getGROUP_ID());
			}
			return "forward:/tiles/home.do";//변경2 NEW홈으로 이동
		} else {
			//로그인 실패시
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
			return "login.tiles";//변경3 NEW로그인폼으로 이동
		}

	}
	
	
	/**
     * XSS 방지 처리. 자바스크립트 코드를 실행하지 못하는 특수문자로 replace(교체)하는내용
     * 접근권한 protected 현재클래스(패키지)만 이용가능 -> public
     * @param data 
     * @return 
     */
    public String unscript(String data) {
        if (data == null || data.trim().equals("")) {
            return "";
        }
        String ret = data;
        ret = ret.replaceAll("<(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;script");
        ret = ret.replaceAll("</(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;/script");
        ret = ret.replaceAll("<(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;object");
        ret = ret.replaceAll("</(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;/object");
        ret = ret.replaceAll("<(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;applet");
        ret = ret.replaceAll("</(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;/applet");
        ret = ret.replaceAll("<(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");
        ret = ret.replaceAll("</(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");
        ret = ret.replaceAll("<(F|f)(O|o)(R|r)(M|m)", "&lt;form");
        ret = ret.replaceAll("</(F|f)(O|o)(R|r)(M|m)", "&lt;form");
        return ret;
    }
    
	@RequestMapping(value="/idcheck.do",method=RequestMethod.GET)
	@ResponseBody //반환값으로 페이지를 명시하지않고, text라고 명시
	public String idcheck(@RequestParam("emplyr_id") String emplyr_id) throws Exception {
		String result = "0";//기본값으로 중복ID가 없다는 표시 0
		EmployerInfoVO memberVO = memberService.viewMember(emplyr_id);
		if(memberVO != null) {
			result = "1";//중복ID가 있을때 표시 1
		}
		return result;//1.jsp 이페이지로 이동X, text값으로 반환합니다.
	}
}
