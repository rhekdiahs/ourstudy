package kr.spring.mypage.controller;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.servlet.support.RequestContextUtils;

import kr.spring.util.AuthCheckException;
import kr.spring.util.FileUtil;
import kr.spring.util.PagingUtil;
import kr.spring.community.service.LostService;
import kr.spring.community.vo.LostVO;
import kr.spring.item.service.ItemService;
import kr.spring.item.vo.ItemVO;
import kr.spring.locker.service.LockerService;
import kr.spring.member.service.MemberService;
import kr.spring.member.vo.MemberVO;
import kr.spring.mypage.service.MypageService;
import kr.spring.pay.vo.PayVO;
import kr.spring.point.vo.PointVO;
import kr.spring.seat.service.SeatService;
import kr.spring.seat.vo.SeatVO;

@Controller
public class MypageController {
	private static final Logger logger = LoggerFactory.getLogger(MypageController.class);
	
	//자바빈(VO) 초기화
	@ModelAttribute
	public PointVO initCommand() {
		return new PointVO();
	}
	@ModelAttribute
	public MemberVO initCommand1() {
		return new MemberVO();
	}
	@Autowired
	private MypageService mypageService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private SeatService seatService;
	@Autowired
	private LostService lostService;
	@Autowired
	private LockerService lockerService;
	
	
	//마이페이지 메인 호출
	@RequestMapping("/mypage/myPageMain.do")
	public String form(@RequestParam(value="pageNum", defaultValue="1")int currentPage, HttpSession session, Model model, HttpServletRequest request) {
		
		//회원 기본 정보 세팅
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		MemberVO member = mypageService.selectMember(user.getMem_num());
		
		SeatVO seat = mypageService.selectCurSeat(user.getMem_num());
		
		member.setMem_status(seatService.getMem_status(user.getMem_num()));
		
		Integer pointSum = mypageService.selectTotalPoint(user.getMem_num());
		
		Float remainTime = mypageService.selectRemainTime(user.getMem_num());
		
		String remainTerm = mypageService.selectRemainTimeTerm(user.getMem_num());
		
		Integer locker_num = lockerService.getLockerNum(user.getMem_num());
		
		if(pointSum == null) {
			pointSum = 0;
		}
		
		if(remainTerm != null) {
			//remainTime 은 2023-03-12 이렇게 들어올거임
			//이 날짜를 로컬 데이트 타입으로 변환해준뒤 현재 시간과 비교한다
			//비교해서 나온 값을 시간단위 or 초단위로 변환해서 메인으로 넘겨준다.
			remainTerm = mypageService.selectRemainTimeTerm(user.getMem_num());;
	        LocalDate end_date = LocalDate.parse(remainTerm, DateTimeFormatter.ISO_DATE);
			
			LocalDate now = LocalDate.now();

			Period remainTermDuration = Period.between(now, end_date);
			logger.debug("seconds : {}", remainTermDuration.getDays());
			
			
			model.addAttribute("remainTerm", remainTermDuration.getDays());
		}
		
		logger.debug("<<마이페이지 멤버 정보>> : " + member);
		
		String[] setThisWeek = new String[7];
		Integer[] setTotalTime_thisWeek = new Integer[7];
		LocalDate now = LocalDate.now();
		LocalDate setThisMonday = now.with(DayOfWeek.MONDAY);
		LocalDate setNextMonday = setThisMonday.plusDays(7);
		
		for(int i = 0; i < setThisWeek.length; i++) {
			setThisWeek[i] = setThisMonday.plusDays(i).toString();
		}
		for(int i = 0; i < setTotalTime_thisWeek.length; i++) {
			if(i == setTotalTime_thisWeek.length - 1) {
				setTotalTime_thisWeek[i] = mypageService.selectSumTotalTimeForGraph(setThisWeek[i], setNextMonday.toString(), user.getMem_num());
			}else {
				setTotalTime_thisWeek[i] = mypageService.selectSumTotalTimeForGraph(setThisWeek[i], setThisWeek[i+1], user.getMem_num());
			}
		}
		
		
		//날짜별 시간 담은 배열 넘겨주기
		model.addAttribute("week", setThisWeek);
		System.out.println("week 찍어보기" + setThisWeek[0]+" "+setThisWeek[1]+" "+setThisWeek[2]+" "+setThisWeek[3]+" "+setThisWeek[4]+" "+setThisWeek[5]+" "+setThisWeek[6]);
		//한 주 날짜 넘겨주기
		model.addAttribute("time", setTotalTime_thisWeek);
		System.out.println("time 찍어보기" + setTotalTime_thisWeek[0]+" "+setTotalTime_thisWeek[1]+" "+setTotalTime_thisWeek[2]+" "+setTotalTime_thisWeek[3]+" "+setTotalTime_thisWeek[4]+" "+setTotalTime_thisWeek[5]+" "+setTotalTime_thisWeek[6]);

		
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		int count = itemService.rentalItemCount(2, user.getMem_num());
		
		PagingUtil page = new PagingUtil(currentPage, count, 3, 5, "myPageMain.do");
		
		List<ItemVO> list = null;
		
		map.put("item_r_status", 2);
		map.put("mem_num", user.getMem_num());
		if(count > 0) {
			map.put("start", page.getStartRow());
			map.put("end", page.getEndRow());
			
			list = itemService.rentalItemList(map);
		}
		
		List<LostVO> list2 = mypageService.getLostFoundList(user.getMem_num());
				
		Map<String, ?> flashMap = (Map<String, ?>) RequestContextUtils.getInputFlashMap(request);
		
		if(flashMap!=null) {
            model.addAttribute("mem_statusForCheckIn", flashMap.get("mem_statusForCheckIn"));
            //String stat = (String) flashMap.get("mem_statusForCheckIn");
            if(flashMap.get("mem_numForCheckIn") != null || flashMap.get("mem_numForCheckIn") != "") {
            	model.addAttribute("mem_numForCheckIn", flashMap.get("mem_numForCheckIn"));
            }
        }
		if(locker_num != null) {
			model.addAttribute("locker_num", locker_num);			
		}
		

		
		
		model.addAttribute("count", count);
		model.addAttribute("list", list);
		model.addAttribute("list2",list2);
		model.addAttribute("member", member);
		model.addAttribute("seat", seat);
		model.addAttribute("pointSum", pointSum);
		
		model.addAttribute("remainTime", remainTime * 3600);
		model.addAttribute("page", page.getPage());
		
		return "myPageMain"; //타일스 설정값
	}
	
	//회원정보
	@RequestMapping("/mypage/myPageMemInfo.do")
	public String formInfo(HttpSession session, Model model) {
		
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		MemberVO member = mypageService.selectMember(user.getMem_num());
		
		SeatVO seat = mypageService.selectCurSeat(user.getMem_num());
		
		Integer pointSum = mypageService.selectTotalPoint(user.getMem_num());
		
		Float remainTime = mypageService.selectRemainTime(user.getMem_num());
		
		String remainTerm = mypageService.selectRemainTimeTerm(user.getMem_num());
		
		Integer locker_num = lockerService.getLockerNum(user.getMem_num());
		
		String locker_end = mypageService.getLockerEnd(user.getMem_num());
		
		if(locker_end != null) {
			model.addAttribute("locker_end", locker_end.split(" ")[0]);
		}
		
		if(pointSum == null) {
			pointSum = 0;
		}
		logger.debug("<<마이페이지 멤버 정보>> : " + member);
		if(remainTerm != null) {
			remainTerm = mypageService.selectRemainTimeTerm(user.getMem_num());;
	        LocalDate end_date = LocalDate.parse(remainTerm, DateTimeFormatter.ISO_DATE);
			
			LocalDate now = LocalDate.now();

			Period remainTermDuration = Period.between(now, end_date);
			logger.debug("seconds : {}", remainTermDuration.getDays());
			
			
			model.addAttribute("remainTerm", remainTermDuration.getDays());
		}
		if(locker_num != null) {
			model.addAttribute("locker_num", locker_num);			
		}
		model.addAttribute("member", member);
		model.addAttribute("seat", seat);
		model.addAttribute("pointSum", pointSum);
		model.addAttribute("remainTime", remainTime * 3600);
		
		return "myPageMemInfo";
	}
	
	//회원정보 수정 폼 호출
	@RequestMapping("/mypage/myPageModify.do")
	public String myPageModifyForm(HttpSession session, Model model) {
		
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		MemberVO member = (MemberVO)mypageService.selectMember(user.getMem_num());
		
		SeatVO seat = mypageService.selectCurSeat(user.getMem_num());
		
		Integer pointSum = mypageService.selectTotalPoint(user.getMem_num());
		
		Float remainTime = mypageService.selectRemainTime(user.getMem_num());
		
		String remainTerm = mypageService.selectRemainTimeTerm(user.getMem_num());
		
		Integer locker_num = lockerService.getLockerNum(user.getMem_num());
		
		String locker_end = mypageService.getLockerEnd(user.getMem_num());
		if(pointSum == null) {
			pointSum = 0;
		}
		if(locker_end != null) {
			model.addAttribute("locker_end", locker_end.split(" ")[0]);
		}
		if(locker_num != null) {
			model.addAttribute("locker_num", locker_num);			
		}
		if(remainTerm != null) {
			remainTerm = mypageService.selectRemainTimeTerm(user.getMem_num());;
	        LocalDate end_date = LocalDate.parse(remainTerm, DateTimeFormatter.ISO_DATE);
			
			LocalDate now = LocalDate.now();

			Period remainTermDuration = Period.between(now, end_date);
			logger.debug("seconds : {}", remainTermDuration.getDays());
			
			
			model.addAttribute("remainTerm", remainTermDuration.getDays());
		}
		model.addAttribute("member", member);
		model.addAttribute("seat", seat);
		model.addAttribute("pointSum", pointSum);
		model.addAttribute("remainTime", remainTime * 3600);
		
		return "myPageModify";
	}
	//수정폼에서 받은 데이터 처리
	@PostMapping("/mypage/myPageModify.do")
	public String submitMypageUpdate(@Valid MemberVO member, BindingResult result, HttpSession session) {
		
		
		logger.debug("<<회원정보수정 처리>> : " + member);
		if(result.hasErrors()) {
			return "myPageModify";
		}
		
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		member.setMem_num(user.getMem_num());
		
		mypageService.updateMember_detail(member);
		
		return "redirect:/mypage/myPageMain.do";
		
	}
	
	//비밀번호 변경 폼 호출
	@GetMapping("/mypage/myPagechangePasswd.do")
	public String myPageChangePasswdForm(HttpSession session, Model model) {
		
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		MemberVO member = (MemberVO)mypageService.selectMember(user.getMem_num());
		
		SeatVO seat = mypageService.selectCurSeat(user.getMem_num());
		
		Integer pointSum = mypageService.selectTotalPoint(user.getMem_num());
		
		Float remainTime = mypageService.selectRemainTime(user.getMem_num());
		
		String remainTerm = mypageService.selectRemainTimeTerm(user.getMem_num());
		
		Integer locker_num = lockerService.getLockerNum(user.getMem_num());
		
		if(pointSum == null) {
			pointSum = 0;
		}
		
		if(locker_num != null) {
			model.addAttribute("locker_num", locker_num);			
		}
		if(remainTerm != null) {
			remainTerm = mypageService.selectRemainTimeTerm(user.getMem_num());;
	        LocalDate end_date = LocalDate.parse(remainTerm, DateTimeFormatter.ISO_DATE);
			
			LocalDate now = LocalDate.now();

			Period remainTermDuration = Period.between(now, end_date);
			logger.debug("seconds : {}", remainTermDuration.getDays());
			
			
			model.addAttribute("remainTerm", remainTermDuration.getDays());
		}
		model.addAttribute("member", member);
		model.addAttribute("seat", seat);
		model.addAttribute("pointSum", pointSum);
		model.addAttribute("remainTime", remainTime * 3600);
		
		return "myPagechangePasswd";
	}
	
	//비밀번호 변경 폼에서 받은 데이터 처리
	@PostMapping("/mypage/myPagechangePasswd.do")
	public String submitMypageChangePasswd(@Valid MemberVO member, BindingResult result, HttpSession session, Model model, HttpServletRequest request) {
		
		
		
		if(result.hasFieldErrors("now_passwd") || result.hasFieldErrors("mem_pw")) {
			
			return myPageChangePasswdForm(session, model);
		}
		
		//세션에서 user 정보 불러오기
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		member.setMem_num(user.getMem_num());
		
		//user 번호로 db에 저장된 비밀번호 확인을 위해 member 정보 불러오기
		MemberVO db_member = mypageService.selectMember(member.getMem_num());
		
		//비밀번호 변경 폼에서 받아 member에 저장된 mem_pw(새비밀번호), now_passwd(원래비밀번호) 따로 저장
		String np = member.getMem_pw();
		String op = member.getNow_passwd();
		
		//변경 후 마이페이지 메인에서 필요한 나머지 정보 member에 담아주기
		member = (MemberVO)mypageService.selectMember(user.getMem_num());
		
		//user정보로 불러와서 mem_pw는 현재비밀번호, now_passwd는 null이 됐으므로 다시 세팅해주기
		member.setMem_pw(np);
		member.setNow_passwd(op);		
		
		if(!db_member.getMem_pw().equals(member.getNow_passwd())) {
			result.rejectValue("now_passwd", "invalidPassword");
			
			return myPageChangePasswdForm(session, model);
		}
		
		logger.debug("<<변경폼 이후 멤버 정보>> : " + member);
		
		mypageService.updatePassword(member);
		
		memberService.deleteAuto_id(member.getMem_num());
		
		model.addAttribute("message", "비밀번호가 변경되었습니다.");
		model.addAttribute("url", request.getContextPath() + "/mypage/myPageMain.do");
		
		return "common/resultView";
		
	}
	
	//회원탈퇴 폼 호출
	@GetMapping("/mypage/myPagedeleteMember.do")
	public String myPageDeleteMemberForm(HttpSession session, Model model) {
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		MemberVO member = (MemberVO)mypageService.selectMember(user.getMem_num());
		
		SeatVO seat = mypageService.selectCurSeat(user.getMem_num());
		
		Integer pointSum = mypageService.selectTotalPoint(user.getMem_num());
		
		Float remainTime = mypageService.selectRemainTime(user.getMem_num());
		
		String remainTerm = mypageService.selectRemainTimeTerm(user.getMem_num());
		
		Integer locker_num = lockerService.getLockerNum(user.getMem_num());
		if(pointSum == null) {
			pointSum = 0;
		}
		
		if(locker_num != null) {
			model.addAttribute("locker_num", locker_num);			
		}
		if(remainTerm != null) {
			remainTerm = mypageService.selectRemainTimeTerm(user.getMem_num());;
	        LocalDate end_date = LocalDate.parse(remainTerm, DateTimeFormatter.ISO_DATE);
			
			LocalDate now = LocalDate.now();

			Period remainTermDuration = Period.between(now, end_date);
			logger.debug("seconds : {}", remainTermDuration.getDays());
			
			
			model.addAttribute("remainTerm", remainTermDuration.getDays());
		}
		model.addAttribute("member", member);
		model.addAttribute("seat", seat);
		model.addAttribute("pointSum", pointSum);
		model.addAttribute("remainTime", remainTime * 3600);
		
		return "myPagedeleteMember";
	}
	
	//회원탈퇴 폼에서 전송받은 데이터 처리
	@PostMapping("/mypage/myPagedeleteMember.do")
	public String submitMypageDeleteMember(@Valid MemberVO member, BindingResult result, HttpSession session, Model model, HttpServletRequest request) {
		
		if(result.hasFieldErrors("mem_id") || result.hasFieldErrors("mem_pw")) {
			return myPageDeleteMemberForm(session, model);
		}
		
		MemberVO user = (MemberVO)session.getAttribute("user");
		MemberVO db_member = mypageService.selectMember(user.getMem_num());
		
		boolean check = false;
		
		try {
			if(db_member != null && db_member.getMem_id().equals(member.getMem_id())) {
				check = db_member.isCheckedPassword(member.getMem_pw());
			}
			
			if(check) {
				mypageService.deleteMember(user.getMem_num());
				
				session.invalidate();
				
				model.addAttribute("accessMsg", "들어주셔서 감사합니다!");
				//model.addAttribute("url", request.getContextPath() + "/mypage/myPageMain.do");
				//return "common/notice";
				return "common/resultViewEnd";
			}
			
			throw new AuthCheckException();
		}catch(AuthCheckException e) {
			result.reject("invalidIdOrPassword");
			
			return myPageDeleteMemberForm(session, model);
		}
	}
	
	//포인트 목록
	@RequestMapping("/mypage/pointList.do")
	public ModelAndView pointList(@RequestParam(value = "pageNum", defaultValue = "1") int currentPage, @RequestParam(value = "keyfield", defaultValue = "1") String keyfield, HttpSession session, Model model) {
		
		ModelAndView mav = new ModelAndView();
		
		//헤더 정보 세팅
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		MemberVO member = (MemberVO)mypageService.selectMember(user.getMem_num());
		
		SeatVO seat = mypageService.selectCurSeat(user.getMem_num());		
		
		Integer pointSum = mypageService.selectTotalPoint(user.getMem_num());
		
		Float remainTime = mypageService.selectRemainTime(user.getMem_num());
		
		String remainTerm = mypageService.selectRemainTimeTerm(user.getMem_num());
		
		Integer locker_num = lockerService.getLockerNum(user.getMem_num());
		
		if(pointSum == null) {
			pointSum = 0;
		}
		if(locker_num != null) {
			mav.addObject("locker_num", locker_num);
		}
		if(remainTerm != null) {
			remainTerm = mypageService.selectRemainTimeTerm(user.getMem_num());;
	        LocalDate end_date = LocalDate.parse(remainTerm, DateTimeFormatter.ISO_DATE);
			
			LocalDate now = LocalDate.now();

			Period remainTermDuration = Period.between(now, end_date);
			logger.debug("seconds : {}", remainTermDuration.getDays());
			
			
			mav.addObject("remainTerm", remainTermDuration.getDays());
		}
		mav.addObject("member", member);
		mav.addObject("seat", seat);
		mav.addObject("pointSum", pointSum);
		//mav.addObject("remainTerm", remainTerm * 3600);
		mav.addObject("remainTime", remainTime * 3600);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("keyfield", keyfield);
		map.put("mem_num", user.getMem_num());
		int count = mypageService.selectPointListCountByMemNum(map);
		
		PagingUtil page = new PagingUtil(keyfield, null, currentPage, count, 5, 5, "pointList.do");
		
		List<PayVO> list = null;
		
		if(count > 0) {
			map.put("start", page.getStartRow());
			map.put("end", page.getEndRow());
			map.put("keyfield", keyfield);
			
			
			list = mypageService.selectPointListByMemNum(map);
		}
		
		mav.addObject("count", count); 
		mav.addObject("list", list);
		mav.addObject("page", page.getPage());
		
		mav.setViewName("pointList");
		return mav;
	}
	
	//공부시간 목록
	@RequestMapping("/mypage/studyTimeList.do")
	public ModelAndView studyTimeList(@RequestParam(value = "pageNum", defaultValue = "1") int currentPage, @RequestParam(value = "keyfield", defaultValue = "1") String keyfield , HttpSession session, Model model) {
		
		ModelAndView mav = new ModelAndView();
		
		//마이페이지 헤더 정보
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		MemberVO member = (MemberVO)mypageService.selectMember(user.getMem_num());
		
		SeatVO seat = mypageService.selectCurSeat(user.getMem_num());
		
		Integer pointSum = mypageService.selectTotalPoint(user.getMem_num());
		
		Float remainTime = mypageService.selectRemainTime(user.getMem_num());
		
		String remainTerm = mypageService.selectRemainTimeTerm(user.getMem_num());
		
		Integer locker_num = lockerService.getLockerNum(user.getMem_num());
		
		if(pointSum == null) {
			pointSum = 0;
		}
		if(locker_num != null) {
			mav.addObject("locker_num", locker_num);
		}
		if(remainTerm != null) {
			remainTerm = mypageService.selectRemainTimeTerm(user.getMem_num());;
	        LocalDate end_date = LocalDate.parse(remainTerm, DateTimeFormatter.ISO_DATE);
			
			LocalDate now = LocalDate.now();

			Period remainTermDuration = Period.between(now, end_date);
			logger.debug("seconds : {}", remainTermDuration.getDays());
			
			
			mav.addObject("remainTerm", remainTermDuration.getDays());
		}
		mav.addObject("member", member);
		mav.addObject("seat", seat);
		mav.addObject("pointSum", pointSum);
		//mav.addObject("remainTerm", remainTerm * 3600);
		mav.addObject("remainTime", remainTime * 3600);
		
		//공부시간 리스트
		Map<String, Object> map = new HashMap<String, Object>();
		
		logger.debug("<<user>> : " + user.getMem_num());
		
		int count = mypageService.selectSeatDetailRowCount(user.getMem_num());
		
		logger.debug("<<count>>" + count);
				
		PagingUtil page = new PagingUtil(keyfield, null, currentPage, count, 5, 5, "studyTimeList.do");
		
		List<SeatVO> list = null; 
		
		if(count > 0) {
		
		map.put("start", page.getStartRow()); 
		map.put("end", page.getEndRow());
		map.put("keyfield", keyfield);
		map.put("mem_num", user.getMem_num());
		
		list = mypageService.selectSeatDetailListByMem_num(map);
		}
		
		
		mav.addObject("count", count); 
		mav.addObject("list", list);
		mav.addObject("page", page.getPage());
		
		logger.debug("list" + list);
		
		mav.setViewName("studyTimeList");
		
		return mav;
	}
	
	
	
	//좌석선택 폼
	@GetMapping("/mypage/myPageselectSeat.do")
	public String myPageSelectSeat() {
		return "myPageselectSeat";
	}
	
	//=======================프로필사진 처리==========================//
	
	//프로필 사진 공통코드
	public void viewProfile(MemberVO member, HttpServletRequest request, Model model) {
		if(member == null || member.getMem_photo_name() == null) {
			byte[] readbyte = FileUtil.getBytes(request.getServletContext().getRealPath("/image_bundle/face.png"));
			model.addAttribute("imageFile", readbyte);
			model.addAttribute("filename", "face.png");
		}else {
			model.addAttribute("imageFile", member.getMem_photo());
			model.addAttribute("filename", member.getMem_photo_name());
		}
	}
	
	//프로필 사진 출력(회원번호 지정)
	@RequestMapping("/mypage/viewProfile.do")
	public String getProfileByMem_num(@RequestParam int mem_num, HttpSession session, HttpServletRequest request, Model model) {
		
		MemberVO member = mypageService.selectMember(mem_num);
		
		
		viewProfile(member, request, model);
		
		return "imageView";
	}
	
	//프로필사진 출력(로그인 전용)
	@RequestMapping("/mypage/photoView.do")
	public String getProfile(HttpSession session, HttpServletRequest request, Model model) {
		
		MemberVO user = (MemberVO)session.getAttribute("user");
				
		if(user == null) {
			byte[] readbyte = FileUtil.getBytes(
					request.getServletContext().getRealPath("/image_bundle/face.png"));
			model.addAttribute("imageFile", readbyte);
			model.addAttribute("filename", "face.png");
		}else {
			MemberVO memberVO = mypageService.selectMember(user.getMem_num());
			viewProfile(memberVO,request,model);
		}
		return "imageView";
	}
	
	//프로필 사진 업로드
	@RequestMapping("/mypage/updateProfileImg.do")
	@ResponseBody
	public Map<String, String> updateProfile(MemberVO member, HttpSession session){
		
		Map<String, String> mapAjax = new HashMap<String, String>();
		
		MemberVO user = (MemberVO)session.getAttribute("user");
				
		if(user == null) {
			mapAjax.put("result", "logout");
		}else {
			member.setMem_num(user.getMem_num());
			mypageService.updateProfile(member);
			mapAjax.put("result", "success");
		}
		
		return mapAjax;
	}
}






