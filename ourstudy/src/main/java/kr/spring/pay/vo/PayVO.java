package kr.spring.pay.vo;

import java.sql.Date;

import kr.spring.ticket.vo.TicketVO;

public class PayVO {
	private int pay_num;
	private int pay_price;
	private int pay_plan;
	private String pay_content;
	private Date pay_date;
	
	private int mem_num;
	private int ticket_num;
	private int point_num;
	
	private int use_point;
	private boolean check_useP;
	
	private int point_point;
	
	private int locker_num;
	private String mem_name;
	private String locker_end;
	
	public String getLocker_end() {
		return locker_end;
	}

	public void setLocker_end(String locker_end) {
		this.locker_end = locker_end;
	}

	public String getMem_name() {
		return mem_name;
	}

	public void setMem_name(String mem_name) {
		this.mem_name = mem_name;
	}

	public int getLocker_num() {
		return locker_num;
	}

	public void setLocker_num(int locker_num) {
		this.locker_num = locker_num;
	}

	//이용권 정보 받아오기
	private TicketVO ticketVO;

	public int getPay_num() {
		return pay_num;
	}

	public void setPay_num(int pay_num) {
		this.pay_num = pay_num;
	}

	public int getPay_price() {
		return pay_price;
	}

	public void setPay_price(int pay_price) {
		this.pay_price = pay_price;
	}

	public int getPay_plan() {
		return pay_plan;
	}

	public void setPay_plan(int pay_plan) {
		this.pay_plan = pay_plan;
	}

	public String getPay_content() {
		return pay_content;
	}

	public void setPay_content(String pay_content) {
		this.pay_content = pay_content;
	}

	public Date getPay_date() {
		return pay_date;
	}

	public void setPay_date(Date pay_date) {
		this.pay_date = pay_date;
	}

	public int getMem_num() {
		return mem_num;
	}

	public void setMem_num(int mem_num) {
		this.mem_num = mem_num;
	}

	public int getTicket_num() {
		return ticket_num;
	}

	public void setTicket_num(int ticket_num) {
		this.ticket_num = ticket_num;
	}

	public int getPoint_num() {
		return point_num;
	}

	public void setPoint_num(int point_num) {
		this.point_num = point_num;
	}

	public TicketVO getTicketVO() {
		return ticketVO;
	}

	public void setTicketVO(TicketVO ticketVO) {
		this.ticketVO = ticketVO;
	}

	public int getPoint_point() {
		return point_point;
	}

	public void setPoint_point(int point_point) {
		this.point_point = point_point;
	}

	@Override
	public String toString() {
		return "PayVO [pay_num=" + pay_num + ", pay_price=" + pay_price + ", pay_plan=" + pay_plan + ", pay_content="
				+ pay_content + ", pay_date=" + pay_date + ", mem_num=" + mem_num + ", ticket_num=" + ticket_num
				+ ", point_num=" + point_num + ", use_point=" + use_point + ", check_useP=" + check_useP +"]";
	}

	public int getUse_point() {
		return use_point;
	}

	public void setUse_point(int use_point) {
		this.use_point = use_point;
	}

	public boolean getCheck_useP() {
		return check_useP;
	}

	public void setCheck_useP(boolean check_useP) {
		this.check_useP = check_useP;
	}
}
