package kr.spring.interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import kr.spring.info.service.InformationService;
import kr.spring.info.vo.InformationVO;
import kr.spring.member.vo.MemberVO;


public class WriterCheckInterceptor 
                       implements HandlerInterceptor{
	private static final Logger logger = 
			         LoggerFactory.getLogger(
					    WriterCheckInterceptor.class);
	
	@Autowired
	private InformationService informationService;

	
	
	@Override
	public boolean preHandle(
			          HttpServletRequest request,
			          HttpServletResponse response,
			          Object handler)throws Exception{
		
		logger.debug("<<로그인 회원번호와 작성자 회원번호 일치 여부 체크>>");
		
		//로그인 회원번호 구하기
		HttpSession session = request.getSession();
		MemberVO user = 
			   (MemberVO)session.getAttribute("user");
		
		//작성자의 회원번호 구하기
		int info_num = Integer.parseInt(
				   request.getParameter("info_num"));
		InformationVO info = informationService.selectInformation(info_num);
		
		if(user!=null) {
			logger.debug("<<로그인 회원번호>> : " + user.getMem_num());
			logger.debug("<<작성자 회원번호>> : " + info.getMem_num());
		}
		
		
		
		//로그인 회원번호와 작성자 회원번호 일치 여부 체크
		if(user==null || 
				user.getMem_num() != info.getMem_num()) {
			logger.debug("<<로그인 회원번호와 작성자 회원번호 일치>>");
			
			request.setAttribute("accessMsg", 
					  "해당글 작성자가 아닙니다");
			request.setAttribute("accessBtn", "게시판 목록");
			request.setAttribute("accessUrl", 
				request.getContextPath()+"/info/informationList.do");
			
			
			
			//포워드 방식으로 화면 호출
			RequestDispatcher dispatcher = 
					request.getRequestDispatcher(
					 "/WEB-INF/views/common/notice.jsp");
			dispatcher.forward(request, response);
			
			return false;
		}
		
		logger.debug("<<로그인 회원번호와 작성자 회원번호 일치>>");
		
		return true;
	}
	

}




