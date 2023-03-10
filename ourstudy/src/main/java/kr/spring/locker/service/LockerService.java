package kr.spring.locker.service;

import java.util.List;

import kr.spring.locker.vo.LockerVO;
import kr.spring.pay.vo.PayVO;

public interface LockerService {
   //사물함 등록
   public void insertLocker(LockerVO vo);
   //사물함 이용시작
   //선택한 사물함번호, 회원번호, 회원이름, 이용시작 날짜 입력
   public void selectLocker(LockerVO vo);
   
   //사물함 정보 불러오기
   public List<LockerVO> getLockerList();
   
   //시작일 불러오기
   public String getLocker_start(LockerVO vo);
   
   //사물함 마감날짜 가져오기
   public String getLockerEnd(int mem_num);
   
   //내가 썼던 사물함 중에서 지금 쓰고 있는 사물함번호 가져오기
   public Integer getLockerNum(Integer mem_num);
   
   //종료일, 남은 시각 저장
   public void insertEndAndDiff(LockerVO vo);
   
   //기존에 등록된 사물함 갯수 검사 불러오기
   public int getMyLockerCount(int mem_num);
   
   //사물함 이용불가 상태로 변경
   public void lockerStatusIn(PayVO vo);
   //사물함 이용가능 상태로 변경
   public void lockerStatusOut(Integer locker_num);
}