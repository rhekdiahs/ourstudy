package kr.spring.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.admin.dao.AdminMemberMapper;
import kr.spring.admin.vo.AdminMemberVO;

@Service
@Transactional
public class AdminMemberServiceImpl implements AdminMemberService{

	@Autowired
	private AdminMemberMapper adminmemberMapper;
	
	//현재고객현황판
	@Override
	public int countUsingNum() {
		return adminmemberMapper.countUsingNum();
	}

	@Override
	public int countTermNum() {
		return adminmemberMapper.countTermNum();
	}

	@Override
	public int countTimeNum() {
		return adminmemberMapper.countTimeNum();
	}
	
	//회원목록
	@Override
	public List<AdminMemberVO> selectList(Map<String, Object> map) {
		return adminmemberMapper.selectList(map);
	}

	@Override
	public int selectRowCount(Map<String, Object> map) {
		return adminmemberMapper.selectRowCount(map);
	}

}




