package kr.spring.seat.vo;

import javax.validation.constraints.NotEmpty;

public class SeatVO {
   private int seat_detail_num;
   private int seat_num;
   private int seat_status;
   @NotEmpty
   private String seat_name;
   @NotEmpty
   private int mem_num;
   private String mem_name;
   private String in_time;
   private String out_time;
   private int total_time;
   
   public int getSeat_detail_num() {
      return seat_detail_num;
   }
   public void setSeat_detail_num(int seat_detail_num) {
      this.seat_detail_num = seat_detail_num;
   }
   
   public int getSeat_num() {
      return seat_num;
   }
   public void setSeat_num(int seat_num) {
      this.seat_num = seat_num;
   }
   public int getSeat_status() {
      return seat_status;
   }
   public void setSeat_status(int seat_status) {
      this.seat_status = seat_status;
   }
   public String getSeat_name() {
      return seat_name;
   }
   public void setSeat_name(String seat_name) {
      this.seat_name = seat_name;
   }
   public int getMem_num() {
      return mem_num;
   }
   public void setMem_num(int mem_num) {
      this.mem_num = mem_num;
   }
   public String getMem_name() {
      return mem_name;
   }
   public void setMem_name(String mem_name) {
      this.mem_name = mem_name;
   }
   public String getIn_time() {
      return in_time;
   }
   public void setIn_time(String in_time) {
      this.in_time = in_time;
   }
   public String getOut_time() {
      return out_time;
   }
   public void setOut_time(String out_time) {
      this.out_time = out_time;
   }
   public int getTotal_time() {
      return total_time;
   }
   public void setTotal_time(int total_time) {
      this.total_time = total_time;
   }
   @Override
   public String toString() {
      return "SeatVO [seat_detail_num=" + seat_detail_num + ", seat_num=" + seat_num + ", seat_status=" + seat_status
            + ", seat_name=" + seat_name + ", mem_num=" + mem_num + ", mem_name=" + mem_name + ", in_time="
            + in_time + ", out_time=" + out_time + ", total_time=" + total_time + "]";
   }
   
}