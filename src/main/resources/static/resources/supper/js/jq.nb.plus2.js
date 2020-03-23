function Base64() {  
   
    // private property  
    _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";  
   
    // public method for encoding  
    this.encode = function (input) {  
        var output = "";  
        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;  
        var i = 0;  
        input = _utf8_encode(input);  
        while (i < input.length) {  
            chr1 = input.charCodeAt(i++);  
            chr2 = input.charCodeAt(i++);  
            chr3 = input.charCodeAt(i++);  
            enc1 = chr1 >> 2;  
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);  
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);  
            enc4 = chr3 & 63;  
            if (isNaN(chr2)) {  
                enc3 = enc4 = 64;  
            } else if (isNaN(chr3)) {  
                enc4 = 64;  
            }  
            output = output +  
            _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +  
            _keyStr.charAt(enc3) + _keyStr.charAt(enc4);  
        }  
        return output;  
    }  
   
    // public method for decoding  
    this.decode = function (input) {  
        var output = "";  
        var chr1, chr2, chr3;  
        var enc1, enc2, enc3, enc4;  
        var i = 0;  
        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");  
        while (i < input.length) {  
            enc1 = _keyStr.indexOf(input.charAt(i++));  
            enc2 = _keyStr.indexOf(input.charAt(i++));  
            enc3 = _keyStr.indexOf(input.charAt(i++));  
            enc4 = _keyStr.indexOf(input.charAt(i++));  
            chr1 = (enc1 << 2) | (enc2 >> 4);  
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);  
            chr3 = ((enc3 & 3) << 6) | enc4;  
            output = output + String.fromCharCode(chr1);  
            if (enc3 != 64) {  
                output = output + String.fromCharCode(chr2);  
            }  
            if (enc4 != 64) {  
                output = output + String.fromCharCode(chr3);  
            }  
        }  
        output = _utf8_decode(output);  
        return output;  
    }  
   
    // private method for UTF-8 encoding  
    _utf8_encode = function (string) {  
        string = string.replace(/\r\n/g,"\n");  
        var utftext = "";  
        for (var n = 0; n < string.length; n++) {  
            var c = string.charCodeAt(n);  
            if (c < 128) {  
                utftext += String.fromCharCode(c);  
            } else if((c > 127) && (c < 2048)) {  
                utftext += String.fromCharCode((c >> 6) | 192);  
                utftext += String.fromCharCode((c & 63) | 128);  
            } else {  
                utftext += String.fromCharCode((c >> 12) | 224);  
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);  
                utftext += String.fromCharCode((c & 63) | 128);  
            }  
   
        }  
        return utftext;  
    }  
   
    // private method for UTF-8 decoding  
    _utf8_decode = function (utftext) {  
        var string = "";  
        var i = 0;  
        var c = c1 = c2 = 0;  
        while ( i < utftext.length ) {  
            c = utftext.charCodeAt(i);  
            if (c < 128) {  
                string += String.fromCharCode(c);  
                i++;  
            } else if((c > 191) && (c < 224)) {  
                c2 = utftext.charCodeAt(i+1);  
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));  
                i += 2;  
            } else {  
                c2 = utftext.charCodeAt(i+1);  
                c3 = utftext.charCodeAt(i+2);  
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));  
                i += 3;  
            }  
        }  
        return string;  
    }  
} 
function isIos() {
	return browser.versions.ios || browser.versions.iPhone || browser.versions.iPad
}
var browser = {
	versions: function() {
		var u = navigator.userAgent,
			app = navigator.appVersion;
		return {
			trident: u.indexOf('Trident') > -1,
			presto: u.indexOf('Presto') > -1,
			webKit: u.indexOf('AppleWebKit') > -1,
			gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1,
			mobile: !! u.match(/AppleWebKit.*Mobile.*/),
			ios: !! u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/),
			android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1 || u.indexOf("Adr"),
			iPhone: u.indexOf('iPhone') > -1,
			iPad: u.indexOf('iPad') > -1,
			webApp: u.indexOf('Safari') == -1
		}
	}(),
	language: (navigator.browserLanguage || navigator.language).toLowerCase()
};

function popTao(img, title, content, tip, op) {
	var pop;
	var poph;
	if (op == 9158) {
		if (isIos()) {
			poph = ""
		} else {
			poph = "height:140px"
		}
	}
	if (!document.querySelector(".popw")) {
		var frag = document.createDocumentFragment(),
			html = "";
		var popw = document.createElement("div");
		frag.appendChild(popw);
		popw.className = "popw";
		html += "<style>.popwbox {height: 258px;}</style><div class='popwbg'></div>";
		html += "<div class='popwbox taobox'>";
		html += "<div class='taologo btndh' style='background-image:url(" + img + ")' id='" + img + "'><img src='" + img + "' style='    width:60px;height:60px;top:-30px;left:50%;border-radius:0px;z-index: 100002;'></div>";
		html += "<div style='background-image:url(../addons/bsht_tbk/res/images/nbclose.png);position:absolute;right:50px;width:30px;height: 30px;background-size: cover;background-repeat: no-repeat;bottom: -18px;margin-right: -60px;box-shadow: 0 2px 5px #999;border: 2px solid #fff;border-radius: 50px;' class='nbclose'></div>";
		html += "<div class='taocon'>";
		html += "<div class='taotitle'>";
		html += "<div class='popwtitle taotitle'>长按虚线框内文字——>>全选——>>复制</div>";
		html += "<div class='popwcontent' id='taocontent'></div>";
		html += "</div></div>";
		html += "<div style='width:100%;'><div class='taotip' style='width:40%;display:inline-block;'></div><div class='taotip2' style='display:block;float:right;' id='nbcopy'></div></div>";
		html += "<div class='taokaobox'>";
		if (!tip) {
			if (isIos()) {
				html += "<!--a href='javascript:;' class='taokao taokaocopy'><img src='../addons/bsht_tbk/res/images/copytip.png'  style='width:100%;max-width:650px'></a-->"
			} else {
				html += "<!--a href='javascript:;' class='taokao taokaocopy'><img src='../addons/bsht_tbk/res/images/copytip.png' style='width:100%;max-width:650px'></a-->"
			}
		} else {
			html += "<a href='javascript:;' class='taokao'>" + tip + "</a>"
		}
		html += "</div>";
		html += "</div>";
		popw.innerHTML = html;
		document.body.appendChild(frag);
		selection();
		canclepopw();
	}
	if (isIos()) {
		//content = content.replace(/\<\/?(br|p){1}\s*\/?\s*\>/gim, "");
		//content = content.replace(/&nbsp;/gim,"");
		$(".popwcontent").html("<div id='copy_key_ios_nb' class='nbtxt share' data-taowords='" + content + "' style='ime-mode: disabled;line-height:18px;font-size:13px;display: block;text-align: left;padding-left: 0px;padding-right: 5px;height: 100px;width:100%;background:transparent;border-style:none;color:#f54d23;-webkit-user-select:text;-webkit-appearance: textarea;-webkit-writing-mode: horizontal-tb;overflow-y: scroll;'>" + content + "</div>");
		setTimeout('$("html,body").animate({scrollTop:$("body").offset().top},500);',200);
		//setTimeout('$("#copybtn").click();',888);
	} else {
		content = content.replace(/\<\/?(br|p){1}\s*\/?\s*\>/gim, "");
		content = content.replace(/&nbsp;/gim,"");
		$(".popwcontent").html("<textarea style='ime-mode: disabled;line-height:18px;font-size:13px;display: block;text-align: left;padding-left: 0px;padding-right: 5px;height: 100px;width:100%;background:transparent;border-style:none;color:#f54d23;-webkit-user-select:text;-webkit-appearance: textarea;-webkit-writing-mode: horizontal-tb;' class='copybox share nbtxt' id='copy_key_android_nb' data-taowords='" + content + "' oninput='regain();'>" + content + "</textarea>");
		setTimeout('$("html,body").animate({scrollTop:$("body").offset().top},500);',200);
		//setTimeout('$("#copybtn").click();',888);
		
	}
	pop = document.querySelector(".popw");
	pop.style.display = "-webkit-box";
	$(".popwbox").addClass("popwboxshow");
	$(".copybox").on("input", function() {
		$(this).val(content)
	});
	if ($(".partnerwx").length < 1) {
		selection();
	} else {
		$(".copybox").hide();
	}
}
function canclepopw() {
	$(".popwcancel,.popwbg,.popwcomfirm,.nbclose").click(function() {
		$(".popwbox").removeClass("popwboxshow");
		$(".popw").remove()
	})
}
function userAction(name, content, headerportrait) {
	var act;
	if (!document.querySelector(".useract")) {
		var useract = document.createElement("div");
		document.body.appendChild(useract);
		useract.className = "useract"
	}
	act = document.querySelector(".useract");
	act.innerHTML = "<img src='" + headerportrait + "'>" + name + ":" + content;
	act.classList.add("useractshow")
}
function openWXimg() {
	$(".pjimg").each(function(index) {
		var imgsObj = $(this).find("div");
		var imgs = new Array();
		for (var i = 0; i < imgsObj.size(); i++) {
			imgs.push(imgsObj.eq(i).attr('src'))
		}
		$(this).find("div").on('click', function() {
			WeixinJSBridge.invoke('imagePreview', {
				'current': $(this).attr('src'),
				'urls': imgs
			})
		})
	})
}
function openWXimgnb(obj) {
	var img = obj.id;
	wx.previewImage({
		'current': img,
		'urls': [img]
	})
}
function nbclose() {
	$('.popwbox').hide();
	$('.popwbg').hide()
}
function selection() {
	if (isIos()) {
		$("#copy_key_ios_nb").css("display", "block");
		$("#copy_key_android_nb").css("display", "none")
	} else {
		$("#copy_key_ios_nb").css({
			"display": "block",
			opacity: 0,
			position: "relative",
			"z-index": 1
		});
		$("#copy_key_android_nb").height($("#copy_key_ios_nb").height() + "px");
		$("#copy_key_ios_nb").hide()
	}
	/*
	document.addEventListener("selectionchange", function(e) {
		if (window.getSelection().anchorNode.parentNode.id == 'copy_key_ios_nb' || window.getSelection().anchorNode.id == 'copy_key_ios_nb') {
			var key = document.getElementById('copy_key_ios_nb');
			window.getSelection().selectAllChildren(key)
		}
	}, false);
	*/
}


function getios() {
	var ua = navigator.userAgent.toLowerCase();
	if (ua.match(/iphone/i) == "iphone" || ua.match(/ipad/i) == "ipad") {
		return true;
	} else {
		return false;
	}
}
