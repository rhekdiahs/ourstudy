<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%-- 로그인 된 경우에만 글쓰기버튼 활성화 --%>
<!DOCTYPE html>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/item.css">

				
				<!-- table 시작 -->
					<div class="it1" style="height:66vh;">
					<br><h3 style="text-align:center"><b>물품생성</b></h3><br><br>
					<div class="acard d-flex justify-content-center" id="card-view" >
					<form:form action="itemWrite.do" class="item_write_form" modelAttribute="itemVO" enctype="multipart/form-data">
					<ul>
						<li>
							<label>물품 표시여부</label>
							<form:radiobutton path="item_p_status" value="1" checked="checked"/>표시
							<form:radiobutton path="item_p_status" value="2"/>미표시
						</li>
						<li>
							<label for="item_title">물품명</label>
							<form:input path="item_title"/>
							<form:errors path="item_title" cssClass="error-color"/>
						</li>
						<li>
							<label for="item_index">물품명 식별번호</label>
							<form:input path="item_index" type="number"/>
							<form:errors path="item_index" cssClass="error-color"/>
						</li>
						<li>
							<label for="upload">사진</label>
							<input type="file" name="upload" id="upload" accept="image/gif, image/png, image/jpeg">
							<form:errors path="item_ufile" cssClass="error-color"/>
						</li>
						<li>
							<label for="item_time">대여 기간(일)</label>
							<form:input path="item_time" type="number"/>
							<form:errors path="item_time" cssClass="error-color"/>
						</li>
					</ul>
					<div class="align-center">
					<form:button class="itemButton">생성</form:button>
					<input type="button" class="itemButton" value="목록" onclick="location.href='adminList.do'">
					</div>
				</form:form>
				</div>
				</div>
				
				
			<div class="it2">
				<h3 style="left:150px; position: relative;"><b>물품생성</b></h3><br>
				<div class="acard2 d-flex justify-content-center" id="card-view" >
				<form:form action="itemWrite.do" class="item_write_form2" modelAttribute="itemVO" enctype="multipart/form-data">
				
					<label>상품표시여부</label><br>
					<form:radiobutton path="item_p_status" value="1" checked="checked"/>표시
					<form:radiobutton path="item_p_status" value="2"/>미표시<br>
					
					<label for="item_title">물품명</label><br>
					<form:input path="item_title"/><br>
					<form:errors path="item_title" cssClass="error-color"/><br>
					
					<label for="item_index">물품명 식별번호</label><br>
					<form:input path="item_index" type="number"/><br>
					<form:errors path="item_index" cssClass="error-color"/><br>
					
					<label for="upload">사진</label><br>
					<input type="file" name="upload" id="upload" accept="image/gif, image/png, image/jpeg"><br>
					<form:errors path="item_ufile" cssClass="error-color"/><br>
					
					<label for="item_time">대여 기간(일)</label><br>
					<form:input path="item_time" type="number"/><br>
					<form:errors path="item_time" cssClass="error-color"/><br>
				<div class="align-center">	
				<form:button class="itemButton">생성</form:button>
				<input type="button" class="itemButton" value="목록" onclick="location.href='adminList.do'">
				</div>
			</form:form>
				</div>			
		</div>