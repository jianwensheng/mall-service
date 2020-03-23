function make_post_set(num) {

	var page = $('#page').html();
	var cid = $('#cid').html();
	var is_q = $('#is_q').html();
	var is_qtz = $('#is_qtz').html();
	var is_tb = $('#is_tb').html();
	var is_jd = $('#is_jd').html();
	var is_pdd = $('#is_pdd').html();
	var is_tm = $('#is_tm').html();
	var is_site = $('#is_site').html();
	var is_by = $('#is_by').html();
	var is_ht = $('#is_ht').html();
	var is_xfzbz = $('#is_xfzbz').html();
	var is_tkld = $('#is_tkld').html();
	var is_hplg = $('#is_hplg').html();
	var is_zhlg = $('#is_zhlg').html();
	var is_tbjp = $('#is_tbjp').html();
	var is_type = $('#is_type').html();

	var so_fee_1 = $('#so_fee_1').val();
	if (so_fee_1 != '') {
		$('#is_fee_1').html(so_fee_1);
		var is_fee_1 = $('#is_fee_1').html();
	} else {
		$('#is_fee_1').html('0');
		var is_fee_1 = $('#is_fee_1').html();
	}
	var so_fee_2 = $('#so_fee_2').val();
	if (so_fee_2 != '') {
		$('#is_fee_2').html(so_fee_2);
		var is_fee_2 = $('#is_fee_2').html();
	} else {
		$('#is_fee_2').html('0');
		var is_fee_2 = $('#is_fee_2').html();
	}

	var so_bl_1 = $('#so_bl_1').val();
	if (so_bl_1 != '') {
		$('#is_bl_1').html(so_bl_1);
		var is_bl_1 = $('#is_bl_1').html();
	} else {
		$('#is_bl_1').html('0');
		var is_bl_1 = $('#is_bl_1').html();
	}
	var so_bl_2 = $('#so_bl_2').val();
	if (so_bl_2 != '') {
		$('#is_bl_2').html(so_bl_2);
		var is_bl_2 = $('#is_bl_2').html();
	} else {
		$('#is_bl_2').html('0');
		var is_bl_2 = $('#is_bl_2').html();
	}

	var key = $('#key').html();
	if($('#keyword').val()!=''){
	    var key = $('#keyword').val();
		$('#key').html($('#keyword').val());
		$('.so_key').html($('#keyword').val());
	}

	var post_set = "&key=" + key + "&cid=" + cid + "&is_q=" + is_q + "&is_qtz=" + is_qtz + "&is_tb=" + is_tb + "&is_jd=" + is_jd + "&is_pdd=" + is_pdd + "&is_tm=" + is_tm + "&is_site=" + is_site + "&is_by=" + is_by + "&is_ht=" + is_ht + "&is_xfzbz=" + is_xfzbz + "&is_tkld=" + is_tkld + "&is_hplg=" + is_hplg + "&is_zhlg=" + is_zhlg + "&is_fee_1=" + is_fee_1 + "&is_fee_2=" + is_fee_2 + "&is_bl_1=" + is_bl_1 + "&is_bl_2=" + is_bl_2 + "&is_type=" + is_type;
	$('#post_set').attr('value', post_set);
	$('#clear').hide();$('#suggest2').hide();$('.suggest').hide();
	if(num==1){
	$('.nbso_main').show();
	     rand_hot();
	    setTimeout(function() {
            $("#keyword").focus();
        }, 333);
	}else{
	$('.nbso_main').hide();
	}
	
	$('input').blur();
	show_his();
	//rand_hot();
	
	setTimeout(function() {
   console.log('to load list');
   loadlist();
    }, 8);
}


function show_his(){
                    var tmpHtml = "";
                    var searchArr;
					var nbso_his = window.localStorage.getItem('nbso_his');
					if(nbso_his){
                    searchArr= nbso_his.split(",")
                    }else{
                    searchArr = [];
                    }
					var len = searchArr.length;
					if(len>0){
					$('#his_div').show();
					}
					if(len > 6){
					 var len = 6;
					}
                    for (var i=0;i<len;i++){
						if(searchArr[i]!=''){
                        var this_key = searchArr[i];
						this_key = this_key.replace("%","");
						console.log("this_key:"+this_key);
						var this_txt = decodeURIComponent(this_key);
						
						
						tmpHtml += '<li class="sug-menu-li" onclick="so_his(this);" id="'+this_txt+'"><a href="javascript:">'+this_txt+'</a></li>';
						}
                    }
                    $("#his").html(tmpHtml);
}





function del_his(){

layer.msg('确认删除搜索历史？', {
  time: 99999, 
  btn: ['确认','取消']
  ,yes: function(){
    window.localStorage.removeItem('nbso_his');
	layer.msg('删除成功', {
    time: 1500
    });
	$('#his_div').hide();
  }
  ,btn2: function(){
    layer.closeAll();
  }
});
}

function show_nbso(){
    rand_hot();
	$('.nbso_main').show();
	setTimeout(function() {
            $("#keyword").focus();
        }, 333);
}
function hide_nbso(){
    $('.nbso_main').hide();
}
function nbsosq(obj){
var jumpnbq = obj.name;
if(jumpnbq){
$("#stop").html('0');
$('#key').html(jumpnbq);
$('#keyword').val(jumpnbq);
$('.so_key').html(jumpnbq);
$('#clear').hide();$('#suggest2').hide();$('.suggest').hide();$('.nbso_main').hide();
//make_post_set();
make_post_set_his();
}
}
function nbso_cl() {
	$('.nbso-all').slideUp(150);
	$('.nbso-all-bak').fadeOut(100);
	$('.a-up').addClass('a-down');
	$('.a-down').removeClass('a-up');
}

function again(){
$("#stop").html('0');
$('#html').html('');
$('#page').html('1');
$('#capage').html('');
$('#stop').html('0');
$('#html').html('<div style="width:100%;text-align: center;padding-top:50px;" id="iidpic"><images src="../addons/bsht_tbk/res/v3/images/loading-1.svg" class="nbloadingsvg"></div>');
}

function setok() {
	again();
	make_post_set();
	console.log('setok');
}

function reset() {
	again();
	$('.menu-li').removeClass('active');
	$('#so_fee_1').val('');
	$('#so_fee_2').val('');
	$('#so_bl_1').val('');
	$('#so_bl_2').val('');
	//
	$('#is_q').html('0');
	$('#is_tm').html('0');
	$('#is_site').html('0');
	$('#is_by').html('0');
	$('#is_ht').html('0');
	$('#is_xfzbz').html('0');
	$('#is_tkld').html('0');
	$('#is_hplg').html('0');
	$('#is_zhlg').html('0');
	$('#is_tbjp').html('0');
	$('#is_fee_1').html('0');
	$('#is_fee_2').html('0');
	$('#is_bl_1').html('0');
	$('#is_bl_2').html('0');
	make_post_set();
	//
	console.log('reset');
	layer.msg('重置完成');
}

function is_any(name) {
	$("#stop").html('0');
	$('#nbso-isq').attr('style','padding-right: 15px;');
	var cksite = $('#is_site').html();
	var ckisjd = $('#is_jd').html();
	var ckisvip = $('#is_vip').html();
	var ckissn = $('#is_sn').html();
	if(cksite == '1' || cksite == 1){
	  $('.menu-li').attr('style','pointer-events: none;color: #bbb;');
	  $('.is_site').attr('style','');
	  if(ckisjd==1){
	  $('.is_by').attr('style','');
	  }
	  if(ckissn==1){
	  $('.is_by').attr('style','');
	  }
	  $('.is_tm').attr('style','');
      if(ckisjd!=1){
	  $('.is_zhlg').attr('style','');
	  }
	  if(name != 'is_site' && name != 'is_tm' && name != 'is_zhlg'){
	  return false;
	  }
	}else{
	  $('.menu-li').attr('style','');
	  if(ckisjd==1){
	    $('.menu-li').attr('style','pointer-events: none;color: #bbb;');
		$('.is_by').attr('style','');
		$('.is_tm').attr('style','');
		$('.is_site').attr('style','');
	  }
	  if(ckissn==1){
	    $('.menu-li').attr('style','pointer-events: none;color: #bbb;');
		$('.is_by').attr('style','');
		$('.is_tm').attr('style','');
		$('.is_site').attr('style','pointer-events: none;color: #bbb;');
	  }
	}
	var is_any = $('#' + name).html();
	console.log(is_any);
	if (is_any == '1' || is_any == 1) {
		$('.' + name).removeClass('active');
		$('#' + name).html(0);
	} else {
		$('.' + name).addClass('active');
		$('#' + name).html(1);
	}
	var cksite = $('#is_site').html();
	if(cksite == '1' || cksite == 1){
	  
	  $('.menu-li').attr('style','pointer-events: none;color: #bbb;');
	  $('.is_site').attr('style','');
	  if(ckisjd==1){
	  $('.is_by').attr('style','');
	  }
	  if(ckissn==1){
	    $('.is_by').attr('style','');
	    $('.is_site').attr('style','pointer-events: none;color: #bbb;');
	  }
	  $('.is_tm').attr('style','');
      if(ckisjd!=1){
	  $('.is_zhlg').attr('style','');
	  }
	  //$('#nbso-isq span').attr('style','color:#F40');
	  //$('#nbso-isq span images').removeClass('off');
	  //$('#is_q').html('1');
	}else{
	  $('.menu-li').attr('style','');
	  if(ckisjd==1){
	    $('.menu-li').attr('style','pointer-events: none;color: #bbb;');
		$('.is_by').attr('style','');
		$('.is_tm').attr('style','');
		$('.is_site').attr('style','');
	  }
	  if(ckissn==1){
	    $('.menu-li').attr('style','pointer-events: none;color: #bbb;');
		$('.is_by').attr('style','');
		$('.is_tm').attr('style','');
		$('.is_site').attr('style','pointer-events: none;color: #bbb;');
	  }
	}
	//make_post_set();
}

function is_qtz(name) {
	var is_qtz = $('#is_qtz').html();
	console.log(is_qtz);
	if (is_qtz != name) {
		$('.qtz_' + name).removeClass('active');
		$('#is_qtz').html(name);
	} else {
		$('.qtz_' + name).addClass('active');
		$('#is_qtz').html('2836');
	}
	make_post_set();
}
function to_top() {
	$(window).scrollTop() > 0 ? $(".go_top").fadeIn(166) : $(".go_top").hide();
	setTimeout('$("html,body").animate({scrollTop:$("body").offset().top},500);',66);
}