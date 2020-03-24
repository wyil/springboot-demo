

<!--弹出框-->

$(function(){
	
	$("#showbox").click(function(){
		$("#TB_overlayBG").css({
			display:"block",height:$(document).height()
		});
		$("#box").css({
			left:($("body").width()-$("#box").width())/2-2+"px",
			top:($(window).height()-$("#box").height())/3+$(window).scrollTop()+"px",
			display:"block"
		});		
	});
	
	$("#showbox2").click(function(){
		$("#TB_overlayBG").css({
			display:"block",height:$(document).height()
		});
		$("#box2").css({
			left:($("body").width()-$("#box2").width())/2-2+"px",
			top:($(window).height()-$("#box2").height())/3+$(window).scrollTop()+"px",
			display:"block"
		});
	});
	
	$("#showbox3").click(function(){
		$("#TB_overlayBG").css({
			display:"block",height:$(document).height()
		});
		$("#box3").css({
			left:($("body").width()-$("#box3").width())/2-2+"px",
			top:($(window).height()-$("#box3").height())/3+$(window).scrollTop()+"px",
			display:"block"
		});
	});
	
	$("#showbox4").click(function(){
		$("#TB_overlayBG").css({
			display:"block",height:$(document).height()
		});
		$("#box4").css({
			left:($("body").width()-$("#box4").width())/2-2+"px",
			top:($(window).height()-$("#box4").height())/3+$(window).scrollTop()+"px",
			display:"block"
		});
	});
	
	$("#showbox5").click(function(){
		$("#TB_overlayBG").css({
			display:"block",height:$(document).height()
		});
		$("#box5").css({
			left:($("body").width()-$("#box5").width())/2-2+"px",
			top:($(window).height()-$("#box5").height())/3+$(window).scrollTop()+"px",
			display:"block"
		});
	});
	
	$("#showbox6").click(function(){
		$("#TB_overlayBG").css({
			display:"block",height:$(document).height()
		});
		$("#box6").css({
			left:($("body").width()-$("#box6").width())/2-2+"px",
			top:($(window).height()-$("#box6").height())/3+$(window).scrollTop()+"px",
			display:"block"
		});
	});
})


// 关闭弹窗

function clsmsgbox(){
			$("#TB_overlayBG").css({ 
			display:"none" 
			});
			$("#box").css({ 
			display:"none" 
			});
			$("#box2").css({ 
			display:"none" 
			});
			$("#box3").css({ 
			display:"none" 
			});
			$("#box4").css({ 
			display:"none" 
			});
			$("#box5").css({ 
			display:"none" 
			});
			$("#box6").css({ 
			display:"none" 
			});
}


$(function(){
	/*============================
	@author:flc
	@time:2014-02-11 18:16:09
	@qq:3407725
	============================*/
	$(".select").each(function(){
		var s=$(this);
		var z=parseInt(s.css("z-index"));
		var dt=$(this).children("dt");
		var dd=$(this).children("dd");
		var _show=function(){dd.slideDown(200);dt.addClass("cur");s.css("z-index",z+1);};  //展开效果
		var _hide=function(){dd.slideUp(200);dt.removeClass("cur");s.css("z-index",z);};   //关闭效果
		dt.click(function(){dd.is(":hidden")?_show():_hide();});
		dd.find("a").click(function(){dt.html($(this).html());_hide();});    //选择效果（如需要传值，可自定义参数，在此处返回对应的“value”值 ）
		$("body").click(function(i){ !$(i.target).parents(".select").first().is(s) ? _hide():"";});
	})
})


