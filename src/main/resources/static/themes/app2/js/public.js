// menu
$(function(){
	var n=$(".top_menu ul .cur").index()
	$(".top_menu ul li").mouseenter(
		function(){
			var z=$(this).index()
			$(".top_menu i").stop().animate({left:(z*69)},300)
			$(this).addClass("cur")
			$(this).siblings().removeClass("cur")
		}
	)
	$(".top_menu ul").mouseleave(
		function(){
			$(".top_menu ul li").eq(n).addClass("cur")
			$(".top_menu ul li").eq(n).siblings().removeClass("cur")
			$(".top_menu i").stop().animate({left:69},300)
		}
	)
})

// input icon
$(function(){
	$(".text_box>div>div").click(
		function(){
			var n=$(this).index()
			$(".text_box div i img").eq(n).stop().animate({marginTop:-22},100)
		}
	)
	$(".text_box>div input").blur(
		function(){
			$(".text_box div i img").stop().animate({marginTop:0},100)
		}
	)
})


$(function(){
	if(!placeholderSupport()){   // 判断浏览器是否支持 placeholder
	   $('[placeholder]').focus(function() {
	       var input = $(this);
	       if (input.val() == input.attr('placeholder')) {
	           input.val('');
	           input.removeClass('placeholder');
	       }
	   }).blur(function() {
	       var input = $(this);
	       if (input.val() == '' || input.val() == input.attr('placeholder')) {
	           input.addClass('placeholder');
	           input.val(input.attr('placeholder'));
	       }
	   }).blur();
	};
})
function placeholderSupport() {
   return 'placeholder' in document.createElement('input');
}