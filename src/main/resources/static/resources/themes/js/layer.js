/*
*AUI素材网是一家提供网页模板下载、手机模板网站下载、微信小程序页面下载、app内嵌页面下载、以企业官网、个人网站、社区论坛、后台网站、个人博客、商城购物网站、专题模板等html源码下载，以及在线交流的平台，致力于打造好用，免费网页模板下载基地；AUI素材网（“北京索引时代网络技术有限公司”），以网页模板源码下载为主。AUI素材网的使命是用web前端技术，让web前端开发更简单，便捷，轻便，快速开发，AUI素材网和大家一起交流web前端技术，打造用户体验一流的网站源码，提供用户下载。我们的使命是用web前端技术，让前端开发更简单，便捷，轻便，快速开发，AUI素材网和大家一起交流前端技术，打造用户体验一流的网站源码，提供用户下载。 本站所有模板均来自团队自制上传，仅用于分享交流，请勿用作商业用途。 请勿侵权，技术支持请联系 aui_cn@163.com ，我们会在第一时间进行回复及处理，如有给您带来不便，敬请谅解。免费模板素材www.588sucai.com www.a-ui.cn 网站设计 PSD 网站前端开发 html5 APP JS JQ Node Vue React 如有需求可联系:18801061167 前端交流群QQ群号:521504936
*  author:lzy-aui
*  layer.js
*  http://azenui.com/
*  http://a-ui.cn/
*  http://www.yidianc.com/
*  http://588sucai.com/
*  https://weibo.com/525135676
*  https://xihazahuopu.taobao.com/
*  合作电话:18519232894
*  QQ:874731831
*  北京索引时代工作室
*/
!function(window) {
    "use strict";

    var doc = window.document
      , ydui = {};

    $(window).on('load', function() {
        typeof FastClick == 'function' && FastClick.attach(doc.body);
    });

    var util = ydui.util = {

        parseOptions: function(string) {
            if ($.isPlainObject(string)) {
                return string;
            }

            var start = (string ? string.indexOf('{') : -1)
              , options = {};

            if (start != -1) {
                try {
                    options = (new Function('','var json = ' + string.substr(start) + '; return JSON.parse(JSON.stringify(json));'))();
                } catch (e) {}
            }
            return options;
        },

    };

    if (typeof define === 'function') {
        define(ydui);
    } else {
        window.YDUI = ydui;
    }

}(window);

!function(window) {
    "use strict";

    var doc = window.document
      , $doc = $(doc)
      , $body = $(doc.body)
      , $mask = $('<div class="mask-black"></div>');

    function ActionSheet(element, closeElement) {
        this.$element = $(element);
        this.closeElement = closeElement;
        this.toggleClass = 'actionsheet-toggle';
    }

    ActionSheet.prototype.open = function() {

        YDUI.device.isIOS && $('.g-scrollview').addClass('g-fix-ios-overflow-scrolling-bug');

        var _this = this;
        $body.append($mask);

        $mask.on('click.ydui.actionsheet.mask', function() {
            _this.close();
        });

        if (_this.closeElement) {
            $doc.on('click.ydui.actionsheet', _this.closeElement, function() {
                _this.close();
            });
        }

        _this.$element.addClass(_this.toggleClass).trigger('open.ydui.actionsheet');
    }
    ;

    ActionSheet.prototype.close = function() {
        var _this = this;

        YDUI.device.isIOS && $('.g-scrollview').removeClass('g-fix-ios-overflow-scrolling-bug');

        $mask.off('click.ydui.actionsheet.mask').remove();
        _this.$element.removeClass(_this.toggleClass).trigger('close.ydui.actionsheet');
    }
    ;

    function Plugin(option) {
        var args = Array.prototype.slice.call(arguments, 1);

        return this.each(function() {
            var $this = $(this)
              , actionsheet = $this.data('ydui.actionsheet');

            if (!actionsheet) {
                $this.data('ydui.actionsheet', (actionsheet = new ActionSheet(this,option.closeElement)));
                if (!option || typeof option == 'object') {
                    actionsheet.open();
                }
            }

            if (typeof option == 'string') {
                actionsheet[option] && actionsheet[option].apply(actionsheet, args);
            }
        });
    }

    $doc.on('click.ydui.actionsheet.data-api', '[data-ydui-actionsheet]', function(e) {
        e.preventDefault();

        var options = window.YDUI.util.parseOptions($(this).data('ydui-actionsheet'))
          , $target = $(options.target)
          , option = $target.data('ydui.actionsheet') ? 'open' : options;

        Plugin.call($target, option);
    });

    $.fn.actionSheet = Plugin;

}(window);

!function(window) {
    window.document.addEventListener('touchstart', function(event) {/* Do Nothing */
    }, false);
}(window);

!function(window) {
    var doc = window.document
      , ydui = window.YDUI
      , ua = window.navigator && window.navigator.userAgent || '';

    var ipad = !!ua.match(/(iPad).*OS\s([\d_]+)/)
      , ipod = !!ua.match(/(iPod)(.*OS\s([\d_]+))?/)
      , iphone = !ipad && !!ua.match(/(iPhone\sOS)\s([\d_]+)/);

    ydui.device = {};
}(window);
