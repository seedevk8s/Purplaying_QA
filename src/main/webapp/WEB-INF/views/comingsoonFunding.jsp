<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
  <!-- meta태그, CSS, JS, 타이틀 인클루드  -->
  <%@ include file ="meta.jsp" %>
  <link rel="stylesheet" href="resources/assets/css/heart.css">
  <link rel="stylesheet" href="resources/assets/css/indexHover.css">
  <script src="resources/assets/js/heart.js"></script>
</head>
<body>
  <!--헤더 인클루드-->
  <%@ include file ="header.jsp" %>
    
  <!--메인 컨테이너 -->
  <section>
    <h1 class="visually-hidden">HOME</h1>
    <div class="contentsWrap">
    
      <!--컨텐츠 영역-->
      
      <!-- 펀딩 프로젝트 -->
      <div class="album">
        <div class="container py-4">
          <h3>Comingsoon ! 펀딩예정💖</h3>
          <c:choose>
          <c:when test="${fn:length(list_c) eq 0 }">
          	  <div class="text-center my-4">
               	<h5>현재 예정중인 펀딩이 없습니다.<br>
               		조금만 기다려주세요 👀
               	</h5>
               	</div>
          </c:when>
          <c:otherwise>
          <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
          	<c:forEach var="ProjectDto" items="${list_c }">
            <div class="col">
              <div class="card shadow-sm">
                <!-- 좋아요 버튼 -->
                <button class="likeBtn" onclick="clickBtntest()"><i class="fa-regular fa-heart ${fn:contains(Likelist, ProjectDto.prdt_id)? 'fas active' : 'far' }"></i></button>
                <div onclick="location.href='/purplaying/project/${ProjectDto.prdt_id}'" disabled="disabled" style="cursor:pointer"></div>               
                <div onclick="alert(${ProjectDto.prdt_comingday}+'일 뒤 공개 예정입니다.'); return false;" style="cursor:pointer">
                <img class="bd-placeholder-img" width="100%" height="225" id="prdt_thumbnail" name="prdt_thumbnail"
                		src="${ProjectDto.prdt_thumbnail}" style=" ${ProjectDto.prdt_thumbnail == null ? 'display:none' : '' }">
                </div>
                <div class="card-body">
	                  <div class="d-flex justify-content-between">
                  		<c:choose>
	                  		<c:when test="${ProjectDto.prdt_genre eq 1 }"><p class="card-cate" onclick="location.href='genre/literature'">문학</p></c:when>
	                  		<c:when test="${ProjectDto.prdt_genre eq 2 }"><p class="card-cate" onclick="location.href='genre/poemessay'">시/에세이</p></c:when>
	                  		<c:when test="${ProjectDto.prdt_genre eq 3 }"><p class="card-cate" onclick="location.href='genre/webtoon'">웹툰</p></c:when>
	                  		<c:otherwise><p class="card-cate">장르</p></c:otherwise>
                  		</c:choose>
                    	<small class="text-danger">공개까지 <b>D-${ProjectDto.prdt_comingday}</b></small>
                  	  </div>
                  	  <div class="link-div" onclick="alert(${ProjectDto.prdt_comingday}+'일 뒤 공개 예정입니다.'); return false;">
	                  	<p class="card-text"><h5>${ProjectDto.prdt_name }</h5></p>
                   	  </div>
                </div>
              </div>
             </div>
             </c:forEach>	
            </div>
            </c:otherwise>
            </c:choose>	
          </div>
        </div>
      </div>
  </section>
  
  <!--푸터 인클루드-->
  <%@ include file ="footer.jsp" %>
  
</body>
</html>