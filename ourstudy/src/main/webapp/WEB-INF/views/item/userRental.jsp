<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/item.css">
<div class="page-main">
	
	<div class="it1" style="height:69vh;">
	<br><h1 class="align-center" style="font-weight:bold; padding-left:40px;">물품대여</h1><br>
	<h2 class="align-center" style="font-weight:bold; padding-left:40px;">${oneitem.item_title}</h2><br>
	<div class="rcard1 d-flex justify-content-center" id="card-view" >
	
		<div class="item-image">
			<img src="imageView.do?item_num=${oneitem.item_num}" width="250" height="300" class="my-photo">
		</div>
		<div class="item-detail">
			<form:form action="userItemRental.do" id="rental_form" modelAttribute="itemVO">
			<ul>
				<li>재고 : ${count}</li>
				<li>대여시간 : ${oneitem.item_time}일</li>
				<li>
				<label>물품 선택 : </label>
				
				<form:select path="item_num">
				<c:forEach var="item" items="${list}" varStatus="status">
			    	<form:option value="${item.item_num}">${item.item_title} ${status.index+1}</form:option>
				</c:forEach>
				</form:select>
				</li>
			</ul>
			<div>
				<c:if test="${count!=0}">
				<form:button class="itemButton">대여</form:button>
				</c:if>
				<input type="button" class="itemButton" value="물품목록" onclick="location.href='userList.do'">
			</div>
			</form:form>
		</div>
	
	<div class="page_clear"></div>
	
	</div>
	</div>
	
	<div class="it2">
	<br>
	<h1 id="rental">물품대여</h1><br>
	<h2 id="rental_name">${oneitem.item_title}</h2><br>
	<div class="rcard2 d-flex justify-content-center" id="card-view" >
		<div class="item-image2">
			<img src="imageView.do?item_num=${oneitem.item_num}" width=170" height="170" class="my-photo">
		</div>
		<div class="item-detail2">
			<form:form action="userItemRental.do" id="rental_form"  modelAttribute="itemVO">
				재고 : ${count}<br>
				대여시간 : ${oneitem.item_time}일<br>
				<form:select path="item_num">
				<c:forEach var="item" items="${list}" varStatus="status">
			    	<form:option value="${item.item_num}">${item.item_title} ${status.index+1}</form:option>
				</c:forEach><br>
				</form:select>
			<div>
				<c:if test="${count!=0}">
				<form:button class="itemButton">대여</form:button>
				</c:if>
				<input type="button" class="itemButton" value="물품목록" onclick="location.href='userList.do'">
			</div>
			</form:form>
		</div>
		<br>
	<div class="page_clear"></div>
	</div>
	</div>
</div>
