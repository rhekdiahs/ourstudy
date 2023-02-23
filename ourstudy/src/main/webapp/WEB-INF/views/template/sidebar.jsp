<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 상단 시작 -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
<%-- Bootstrap core CSS --%>
<link href="/docs/5.0/dist/css/bootstrap.min.css" rel="stylesheet" 
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous">
<%--아이콘--%>
<link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css">
<%--폰트 --%>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Leckerli+One&display=swap" rel="stylesheet">
<%--로그인 유효성 체크 --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/logincheck.js"></script>
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<script>
Kakao.init('29a4ee7bbc4eb20216c3708400363a9a'); //발급받은 키 중 javascript키를 사용해준다.
console.log(Kakao.isInitialized()); // sdk초기화여부판단
//카카오로그인
function kakaoLogin() {
    Kakao.Auth.login({
      success: function (response) {
        Kakao.API.request({
          url: '/v2/user/me',
          success: function (response) {
        	  console.log(response)
          },
          fail: function (error) {
            console.log(error)
          },
        })
      },
      fail: function (error) {
        console.log(error)
      },
    })
  }
//카카오로그아웃  
function kakaoLogout() {
    if (Kakao.Auth.getAccessToken()) {
      Kakao.API.request({
        url: '/v1/user/unlink',
        success: function (response) {
        	console.log(response)
        },
        fail: function (error) {
          console.log(error)
        },
      })
      Kakao.Auth.setAccessToken(undefined)
    }
  }  
</script>
<%-- 사이드바 시작 --%>
<div>
<%-- 로그인 모달 시작 --%>
<div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="loginModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content head">
			<div class="modal-body p-2">
				<div class="row">
					<div class="col-3"></div>
					<div class="col-7">
						<h3 class="modal-title ourstudy mb-3" id="loginModalLabel">OurStudy</h3>
					</div>
					<div class="col-2">
						<%-- x 닫기 --%>
						<button type="button" class="close btn" style="border:none;"
							data-bs-dismiss="modal" aria-label="Close">
							<i class="bi bi-x-lg"></i>
						</button>
					</div>
				</div>
				
				<form action="${pageContext.request.contextPath}/member/login.do" method="post" id="login_form">
						<ul>
							<li><label for="mem_id"></label>
								<input type="text" name="mem_id" id="mem_id" placeholder=" ID"/></li>
								<li><span id="login_Id"></span></li>
							<li><label for="mem_pw"></label>
								<input type="password" name="mem_pw" id="mem_pw" placeholder=" Password"/></li>
								<li><span id="login_Pw"></span></li>
							<li>&nbsp;<input type="checkbox" name="auto" id="auto">&nbsp;로그인 상태 유지
							</li>
						</ul>
					
						<button type="submit" class="btn login-submit 
						text-white rounded submit px-2" style="background-color:#037332;">Login
						</button>
				</form>
				
				<ul class="find-id my-3">
					<li>아이디 찾기</li>
					<li>&nbsp;|&nbsp;</li>
					<li>비밀번호 찾기</li>
					<li>&nbsp;|&nbsp;</li>
					<li>회원가입</li>
				</ul>
				
				<div class="my-4 d-flex justify-content-center" onclick="kakaoLogin()">
					<a href="javascript:void(0)"><img src="${pageContext.request.contextPath}/image_bundle/kakao.PNG" width="30" height="30" class="my-photo">카카오톡 간편로그인</a>
				</div>
				
				<div class="my-4 d-flex justify-content-center" style="color:#037332;">비회원으로 계속하기</div>
			</div>
		</div>
	</div>
</div>
<%-- 로그인 모달 끝 --%>

	<div class="main-side">
		<nav id="main-sidebar">
			<div class="flex-shrink-0 shadow p-1 sidescroll" style="width:230px; height:100%;">
				<div style="text-align:center;">
					<a href="${pageContext.request.contextPath}/main/main.do"
						class="d-flex align-items-center pb-3 mb-3 link-dark text-decoration-none border-bottom">
						<svg class="bi pe-none me-2" width="30" height="24"></svg>
							<span class="fs-3 fw-semibold text-center ourstudy">OurStudy</span>
					</a>
				<c:if test="${empty user}">
					<img src="${pageContext.request.contextPath}/image_bundle/face.png" width="130" height="130" class="my-photo">
				</c:if>
				<c:if test="${!empty user} && ${empty member.mem_photo}">
					<img src="${pageContext.request.contextPath}/image_bundle/face.png" width="130" height="130" class="my-photo">
				</c:if>
				<c:if test="${!empty user}">
					<img src = "${pageContext.request.contextPath}/mypage/photoView.do" width="130" height="130" class="my-photo">
				</c:if>
				<div>
					<c:if test="${empty user}">
						<button class="btn text-white mt-4 mb-4 rounded" data-bs-toggle="modal" data-bs-target="#loginModal" 
						style="background-color:#037332;">로그인 | 회원가입</button>
					</c:if>
						
					<%--임시로 넣어둠--%>
					<c:if test="${user.mem_status == 0}">
						<button class="btn text-white mt-4 mb-4 rounded" style="background-color:gray">퇴실</button>
					</c:if>
					<c:if test="${user.mem_status == 1}">
						<button class="btn text-white mt-4 mb-4 rounded" style="background-color:red">입실중</button>
					</c:if>
					<c:if test="${user.mem_status == 2}">
						<button class="btn text-white mt-4 mb-4 rounded" style="background-color:blue">외출중</button>
					</c:if>
				</div>
				</div>
				
				<div class="side-bottom">
       				<button class="btn btn-toggle  d-inline-flex  align-items-center rounded collapsed border-white" 
        				data-bs-toggle="collapse" data-bs-target="#item-collapse" aria-expanded="false">
          				&nbsp;<i class="bi bi-ticket-perforated"></i>
          				<span class="innerhome">이용권 구매</span>
        			</button>
        			<div class="collapse" id="item-collapse">
         		 	<ul class="btn-toggle-nav fw-normal pb-1 small">
           				<li><a href="${pageContext.request.contextPath}/ticket/ticketList.do" class="link-dark d-inline-flex rounded">좌석 이용권</a></li>
            			<li><a href="${pageContext.request.contextPath}/ticket/ticketList.do" class="link-dark d-inline-flex rounded">사물함 이용권</a></li>
          			</ul>
        			</div>
        		</div>
				
				<div class="side-inline chair-bottom" style="cursor: pointer;" onclick="location.href=''">
					<ul>
						<li><i class="fas fa-regular fa-chair"></i></li>
						<%-- <c:if test="${user.mem_auth == 1}"> --%>
						<li class="chair"><a href="${pageContext.request.contextPath}/seat/selectSeatForm.do">좌석 선택</a></li>
						<%-- </c:if> --%>
					</ul>
				</div>
				
				<div class="side-inline laptop-bottom" style="cursor: pointer;" onclick="location.href=''">
					<ul>
						<li><i class="fas fa-regular fa-laptop"></i></li>
						<li class="laptop"><a href="${pageContext.request.contextPath}/item/userList.do">물품 대여</a></li>
					</ul>
				</div>
				
				<div class="side-bottom">
       				<button class="btn btn-toggle  d-inline-flex  align-items-center rounded collapsed border-white" 
        				data-bs-toggle="collapse" data-bs-target="#notice-collapse" aria-expanded="false">
          				&nbsp;<i class="fas fa-regular fa-bullhorn equal-left"></i>
          				<span class="notice-span">시설 안내</span>
        			</button>
        			<div class="collapse" id="notice-collapse">
         		 	<ul class="btn-toggle-nav fw-normal pb-1 small">
           				<li><a href="${pageContext.request.contextPath}/community/informationList.do" class="link-dark d-inline-flex rounded">안내사항</a></li>
            			<li><a href="#" class="link-dark d-inline-flex rounded">자주 묻는 질문</a></li>
          			</ul>
        			</div>
        		</div>
				
				<div class="side-bottom">
       				<button class="btn btn-toggle  d-inline-flex  align-items-center rounded collapsed border-white" 
        				data-bs-toggle="collapse" data-bs-target="#comm-collapse" aria-expanded="false">
          				&nbsp;<i class="bi bi-people-fill equal-left"></i>
          				<span class="comm-span">커뮤니티</span>
        			</button>
        			<div class="collapse" id="comm-collapse">
         		 	<ul class="btn-toggle-nav fw-normal pb-1 small">
           				<li><a href="#" class="link-dark d-inline-flex rounded">분실물 찾기</a></li>
            			<li><a href="#" class="link-dark d-inline-flex rounded">이용후기</a></li>
          			</ul>
        			</div>
        		</div>
        		<c:if test="${!empty user && user.mem_auth == 1}">
				<div>
					<ul	class="side-inline" style="cursor: pointer;" onclick="location.href='${pageContext.request.contextPath}/mypage/myPageMain.do'">
						<li><i class="fas fa-light fa-id-card"></i></li>
						<li class="myp">마이페이지</li>
					</ul>
				</div>
				</c:if>
				<br>
				<c:if test="${!empty user}">
						<button class="btn text-white rounded logout-btn" style="background-color:#037332;"
						onclick="location.href='${pageContext.request.contextPath}/member/logout.do'">로그아웃</button>
				</c:if>
				<br><br>
			</div>
		</nav>
	</div>
</div>
<%-- 사이드바 끝 --%>
