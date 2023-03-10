<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/item.css">
<div class="align-center">
<h2>물품 생성</h2>
</div><br>
<form:form action="itemWrite.do" id="item_write_form" modelAttribute="itemVO" enctype="multipart/form-data">
	<ul>
		<li>
			<label>상품표시여부</label>
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
	<form:button>전송</form:button>
	<input type="button" value="목록" onclick="location.href='adminList.do'">
	</div>
</form:form>