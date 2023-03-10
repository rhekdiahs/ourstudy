package kr.spring.pay.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.locker.dao.LockerMapper;
import kr.spring.locker.vo.LockerVO;
import kr.spring.member.vo.MemberVO;
import kr.spring.mypage.dao.MypageMapper;
import kr.spring.pay.dao.PayMapper;
import kr.spring.pay.vo.PayVO;
import kr.spring.point.vo.PointVO;
import kr.spring.ticket.vo.TicketVO;

@Service
@Transactional
public class PayServiceImpl implements PayService{
	
	@Autowired
	private PayMapper payMapper;
	
	@Autowired
	private MypageMapper mypageMapper;

	@Autowired
	private LockerMapper lockerMapper;
	
	@Override
	public int selectPay_num() {
		return payMapper.selectPay_num();
	}

	@Override
	public void insertPay(PayVO payVO) {
		
		payVO.setPay_num(payMapper.selectPay_num());
		payMapper.insertPay(payVO);
		
		mypageMapper.insertPoint(payVO);
	}
	
	@Override
	public List<PayVO> selectListPay(Map<String, Object> map) {
		return payMapper.selectListPay(map);
	}

	@Override
	public int selectPoint(Integer mem_num) {
		return payMapper.selectPoint(mem_num);
	}

	@Override
	public TicketVO selectTicket(Integer ticket_num) {
		return payMapper.selectTicket(ticket_num);
	}

	@Override
	public void updateMemberHistory_Term(Integer time, Integer mem_num) {
		payMapper.updateMemberHistory_Term(time, mem_num);
		
	}

	@Override
	public void updateMemberHistory_Hour(Integer time, Integer mem_num) {
		payMapper.updateMemberHistory_Hour(time, mem_num);
		
	}

	@Override
	public int checkUsingLocker(Integer mem_num) {
		int result;
		try {
			result = payMapper.checkUsingLocker(mem_num);
		}catch(Exception e){
			result = 0;
		}
		return result;
	}

	@Override
	public void updateLocker_end(LockerVO lockerVO) {
		payMapper.updateLocker_end(lockerVO);
	}

	@Override
	public String selectLockerEnd(Integer mem_num) {
		return payMapper.selectLockerEnd(mem_num);
	}

	@Override
	public void insertNewLockerMember(PayVO payVO) {
		payMapper.insertNewLockerMember(payVO);
	}

	@Override
	public String checkTime(Integer mem_num) {
		return payMapper.checkTime(mem_num);
	}

	@Override
	public String checkTerm(Integer mem_num) {
		return payMapper.checkTerm(mem_num);
	}

	@Override
	public void updateTerm(String mem_ticket_term, Integer mem_num) {
		payMapper.updateTerm(mem_ticket_term, mem_num);
	}

}

