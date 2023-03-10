package kr.spring.info.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.spring.info.service.InformationService;
import kr.spring.info.vo.InformationVO;
import kr.spring.member.vo.MemberVO;
import kr.spring.util.PagingUtil;


@Controller
public class InformationController {
	private static final Logger logger =
			LoggerFactory.getLogger(InformationController.class);
	
	private int rowCount = 10;
	
	@Autowired
	private InformationService informationService;
	
	//자바빈(VO) 초기화
	@ModelAttribute
	public InformationVO initCommand() {
		return new InformationVO();
	}
		
	//==안내사항 게시판 글목록
	@RequestMapping("/info/informationList.do")
	public ModelAndView process(
			 @RequestParam(value="pageNum",defaultValue="1") int currentPage,
			 @RequestParam(value="keyfield",defaultValue="1") String keyfield, String keyword) { 
				
			Map<String,Object> map = 
						new HashMap<String,Object>();
			map.put("keyfield", keyfield);
			map.put("keyword", keyword);
			
			//필독 제외 글개수
			int count = informationService.selectinfoRowCount(map); 
			logger.debug("<<count>> : " + count);
			
			//필독 개수 체크
			int countimport = informationService.checkImportant();
			logger.debug("<<필독 개수>> : " + countimport);
						
			//페이지 처리(필독 개수마다 다르게)
			PagingUtil page;
			if(countimport == 1) {
				page = new PagingUtil(keyfield,keyword,currentPage,count,10-1,10,"informationList.do");
			} else if(countimport == 2) {
				page = new PagingUtil(keyfield,keyword,currentPage,count,10-2,10,"informationList.do");
			} else if(countimport == 3) {
				page = new PagingUtil(keyfield,keyword,currentPage,count,10-3,10,"informationList.do");
			} else {
				//필독 0개일때,
				page = new PagingUtil(keyfield,keyword,currentPage,count,10,10,"informationList.do");
			}
			List<InformationVO> listimport = null;
			if(countimport>0)
				listimport = informationService.selectImportList();
			logger.debug("<<필독처리완료>> : ");
			
			map.put("start", page.getStartRow());
			map.put("end", page.getEndRow());
			
			
			List<InformationVO> list = null;
			if(count > 0) {
				map.put("start", page.getStartRow());
				map.put("end", page.getEndRow());
				
				list = informationService.selectInfoList(map);
				logger.debug("<<필독 제외글 처리완료>> : ");
			}
			ModelAndView mav = new ModelAndView();
			mav.setViewName("informationList");
			mav.addObject("count",count);
			mav.addObject("informationList",list);
			mav.addObject("page",page.getPage());
			
			mav.addObject("countimport",countimport);
			mav.addObject("informationImportList",listimport);
			
			return mav;
	}
	
	//==글쓰기
	//등록 폼 호출
	@GetMapping("/info/infoWrite.do")
	public String form() {
		return "informationWrite";
	}
	
	//등록 폼에서 전송된 데이터 처리
	@PostMapping("/info/infoWrite.do")
	public String submit(@Valid InformationVO informationVO, BindingResult result,
			Model model,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws Exception{
		logger.debug("<<게시판 글쓰기>> : " + informationVO);
		logger.debug("<<업로드 파일 용량>> : " 
		           + informationVO.getUploadfile().length);
		
		if(informationVO.getUploadfile().length > 5*1024*1024) {//5MB
			result.reject("limitUploadSize",new Object[]{"5MB"},null);
		}
		
		//유효성 체크 결과 오류가 있으면 폼을 호출
		if(result.hasErrors()) {
			return form();
		}
		
		MemberVO user = (MemberVO)session.getAttribute("user");
		informationVO.setMem_num(user.getMem_num());
		
		//필독 개수 체크
		int count = informationService.checkImportant();
		
		if(informationVO.getInfo_pin()==1 && count == 3) {
			 response.setContentType("text/html; charset=UTF-8"); PrintWriter out =
			 response.getWriter();
			 out.println("<script>alert('필독은 3개까지만 가능합니다.');</script>"); out.flush();
			 return form();
		}
		
		//글쓰기	
		informationService.insertInformation(informationVO);
		
		model.addAttribute("message", "등록 되었습니다.");
		model.addAttribute("url", request.getContextPath()+"/info/informationList.do");
		return "common/resultView";
	}
	
	//==글상세
	
	 @GetMapping("/info/infoDetail.do") 
	 public ModelAndView process(@RequestParam int info_num) {
		 
		 logger.debug("<<info_num>> : " + info_num);
		 
		 InformationVO informationVO =
				 informationService.selectInformation(info_num);
		 
		 return new ModelAndView("informationView","information",informationVO); 
	 
	 }
	 
	
	
	//==글수정
	//수정 폼 호출
	@GetMapping("/info/infoUpdate.do")
	public String formUpdate(@RequestParam int info_num, Model model) { 
		InformationVO informationVO = informationService.selectInformation(info_num);
		model.addAttribute("informationVO",informationVO);
	
		return "informationModify";
	}
	/*
	 * public String formUpdate(HttpSession session, Model model) {
	 * 
	 * MemberVO user = (MemberVO)session.getAttribute("user"); InformationVO
	 * information =
	 * (InformationVO)informationService.selectInformation(user.getMem_num());
	 * model.addAttribute("user", user); model.addAttribute("information",
	 * information); return "infoUpdate"; }
	 */
	
	//폼에서 전송된 데이터 처리
	@PostMapping("/info/infoUpdate.do")
	public String submitUpdate(@Valid InformationVO informationVO,
								BindingResult result,
								HttpServletRequest request,
								Model model,
								HttpSession session) {
		
		logger.debug("<<안내사항 수정>> : " + informationVO);
//		logger.debug("<<업로드 파일 용량>> : " 
//		           + informationVO.getUploadfile().length);
//		
//		if(informationVO.getUploadfile().length > 5*1024*1024) {//5MB
//			result.reject("limitUploadSize",new Object[]{"5MB"},null);
//		}
		
		if(result.hasErrors()) {
			InformationVO vo = informationService.selectInformation(
									informationVO.getInfo_num());
			informationVO.setFilename(vo.getFilename());
			return "informationModify";
		}
		MemberVO user = (MemberVO)session.getAttribute("user");
		informationVO.setMem_num(user.getMem_num());
		
		
		informationService.updateInformation(informationVO);

	
		model.addAttribute("message", "수정 되었습니다.");
		model.addAttribute("url",
				request.getContextPath()+"/info/informationList.do");
		return "common/resultView";
	}
	
	//==글삭제
	@RequestMapping("/info/infoDelete.do")
	public String submitDelete(@RequestParam int info_num,Model model,
			HttpServletRequest request) {
		logger.debug("<<안내사항 글삭제>> : " + info_num);
		
		informationService.deleteInformation(info_num);
		
		return "redirect:/info/informationList.do";
	}
	
	//==파일삭제
	@RequestMapping("/info/deleteFile.do")
	@ResponseBody
	public Map<String,String> processFile(
			                   int info_num,
			                   HttpSession session){
		Map<String,String> mapJson = 
				new HashMap<String,String>();
		MemberVO user = (MemberVO)session.getAttribute("user");
		if(user==null) {
			mapJson.put("result", "logout");
		}else {
			informationService.deleteFile(info_num);
			
			mapJson.put("result", "success");
		}
		
		return mapJson;
		
	}	
	//==파일다운
	@RequestMapping("/info/file.do")
	public ModelAndView download(
	         @RequestParam int info_num) {
		InformationVO info = 
		informationService.selectInformation(info_num);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("downloadView");
		mav.addObject("downloadFile", 
				             info.getUploadfile());
		mav.addObject("filename", info.getFilename());
		
		return mav;
	}
	
	//등록 폼 호출
	@RequestMapping("/info/faq.do")
		public String faq() {
			return "faq";
		}
	
	
}
