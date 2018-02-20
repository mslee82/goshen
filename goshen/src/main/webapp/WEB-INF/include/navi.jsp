<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<script>
	$(document).ready(function(event){
		menu_effect();
	});

	/**
	 * 메뉴 효과 처리
	 */
	function menu_effect(){
		$('.lnb_2lev > li > a').each(function(){
			if($(this).hasClass('menuactive')){
				$(this).parents('.lnb_nav > ul > li').addClass('active');
			}
		});
		$('.lnb_nav > ul > li > a').on("click",function(){
			$(this).parents('li').children('.lnb_2lev').slideDown(200);
		});
		
		$('.lnb_nav > ul > li').mouseleave(function(){
			$(this).children('.lnb_2lev').slideUp(200);
			
		});
	}
	</script>
	<div class="header_wrap">
		<h1 class="logo">
			GOSHEN
		</h1>
		<div class="header_group">			
			<div class="gnb_nav">			
				<div class="login_statue" style="display:none">			
					<span><i class="fa fa-sign-out"></i></span><a href="#" id="logout">logout</a>
				</div>
				<ul>				
				</ul>
			</div>
			<div class="lnb_nav">			
				<ul>
					<li><a href="/main/home.do">메인</a></li>
					<li>
						<a href="#none">판매</a>
						<ul class="lnb_2lev">
							<li><a href="/sell/sellUpload.do">판매 엑셀 업로드</a></li>
							<li><a href="/sell/sellListPage.do">판매 내역</a></li>
							<li><a href="/sell/sellRegPage.do">판매 등록</a></li>
							<li><a href="/sellReturn/sellReturnListPage.do">반품내역 조회</a></li>
						</ul>
					</li>
					<li>
						<a href="#none">단가표</a>
						<ul class="lnb_2lev">
							<li><a href="/product/productPriceUpload.do">단가표 업로드</a></li>
							<li><a href="/product/productPriceListPage.do">단가표 조회</a></li>
						</ul>
					</li>
					<li>
						<a href="/receipt/viewReceipt.do">영수증 조회</a>
					</li>
					<li>
						<a href="#none">작업표</a>
						<ul class="lnb_2lev">
							<li><a href="/sell/deliveryInfoListPage.do">작업표 조회 및 다운로드</a></li>
						</ul>
					</li>
					<li>
						<a href="#none">관리</a>
						<ul class="lnb_2lev">
							<li><a href="/product/productListPage.do">상품관리</a></li>
							<li><a href="">재고관리</a></li>
							<li><a href="">고객관리</a></li>							
						</ul>
					</li>
					<li>
						<a href="#none">통계</a>
						<ul class="lnb_2lev">
							<li><a href="">판매순위</a></li>
							<li><a href="">고객별 판매 추이</a></li>
						</ul>
					</li>
				</ul>
			</div>
		</div>
	</div>	