package kr.spring.talk.vo;

public class TalkVO {
	private int talk_num;
	private int talkroom_num;
	private int mem_num;
	private String message;
	private String chat_date;
	
	private int read_count;
	private String mem_id;
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	public int getTalk_num() {
		return talk_num;
	}
	public void setTalk_num(int talk_num) {
		this.talk_num = talk_num;
	}
	public int getTalkroom_num() {
		return talkroom_num;
	}
	public void setTalkroom_num(int talkroom_num) {
		this.talkroom_num = talkroom_num;
	}
	public int getMem_num() {
		return mem_num;
	}
	public void setMem_num(int mem_num) {
		this.mem_num = mem_num;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getChat_date() {
		return chat_date;
	}
	public void setChat_date(String chat_date) {
		this.chat_date = chat_date;
	}
	public int getRead_count() {
		return read_count;
	}
	public void setRead_count(int read_count) {
		this.read_count = read_count;
	}
	
	@Override
	public String toString() {
		return "TalkVO [talk_num=" + talk_num + ", talkroom_num=" + talkroom_num + ", mem_num=" + mem_num + ", message="
				+ message + ", chat_date=" + chat_date + ", read_count=" + read_count + ", mem_id=" + mem_id + "]";
	}
	
	
	
	
}

