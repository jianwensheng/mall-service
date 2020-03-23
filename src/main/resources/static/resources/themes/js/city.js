/*
*AUI素材网是一家提供网页模板下载、手机模板网站下载、微信小程序页面下载、app内嵌页面下载、以企业官网、个人网站、社区论坛、后台网站、个人博客、商城购物网站、专题模板等html源码下载，以及在线交流的平台，致力于打造好用，免费网页模板下载基地；AUI素材网（“北京索引时代网络技术有限公司”），以网页模板源码下载为主。AUI素材网的使命是用web前端技术，让web前端开发更简单，便捷，轻便，快速开发，AUI素材网和大家一起交流web前端技术，打造用户体验一流的网站源码，提供用户下载。我们的使命是用web前端技术，让前端开发更简单，便捷，轻便，快速开发，AUI素材网和大家一起交流前端技术，打造用户体验一流的网站源码，提供用户下载。 本站所有模板均来自团队自制上传，仅用于分享交流，请勿用作商业用途。 请勿侵权，技术支持请联系 aui_cn@163.com ，我们会在第一时间进行回复及处理，如有给您带来不便，敬请谅解。免费模板素材www.588sucai.com www.a-ui.cn 网站设计 PSD 网站前端开发 html5 APP JS JQ Node Vue React 如有需求可联系:18801061167 前端交流群QQ群号:521504936
*  author:lzy-aui
*  city.js
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

    var util = ydui.util = {

        pageScroll: function() {
            var fn = function(e) {
                e.preventDefault();
                e.stopPropagation();
            };
            var islock = false;

            return {
                lock: function() {
                    if (islock)
                        return;
                    islock = true;
                    doc.addEventListener('touchmove', fn);
                },
                unlock: function() {
                    islock = false;
                    doc.removeEventListener('touchmove', fn);
                }
            };
        }(),

    };

    $.fn.emulateTransitionEnd = function(duration) {
    }
    ;

    if (typeof define === 'function') {
        define(ydui);
    } else {
        window.YDUI = ydui;
    }

    var $body = $(window.document.body);

    function CitySelect(element, options) {
        this.$element = $(element);
        this.options = $.extend({}, CitySelect.DEFAULTS, options || {});
        this.init();
    }

    CitySelect.DEFAULTS = {
        provance: '',
        city: '',
        area: ''
    };

    CitySelect.prototype.init = function() {
        var _this = this
          , options = _this.options;

        if (typeof YDUI_CITYS == 'undefined') {
            console.error('请在ydui.js前引入ydui.citys.js。下载地址：http://cityselect.ydui.org');
            return;
        }

        _this.citys = YDUI_CITYS;

        _this.createDOM();

        _this.defaultSet = {
            provance: options.provance,
            city: options.city,
            area: options.area
        };
    }
    ;

    CitySelect.prototype.open = function() {
        var _this = this;

        $body.append(_this.$mask);

        YDUI.device.isMozilla && _this.$element.blur();

        _this.$mask.on('click.ydui.cityselect.mask', function() {
            _this.close();
        });

        var $cityElement = _this.$cityElement
          , defaultSet = _this.defaultSet;

        $cityElement.find('.cityselect-content').removeClass('cityselect-move-animate cityselect-next cityselect-prev');

        _this.loadProvance();

        if (defaultSet.provance) {
            _this.setNavTxt(0, defaultSet.provance);
        } else {
            $cityElement.find('.cityselect-nav a').eq(0).addClass('crt').html('请选择');
        }

        if (defaultSet.city) {
            _this.loadCity();
            _this.setNavTxt(1, defaultSet.city)
        }

        if (defaultSet.area) {
            _this.loadArea();
            _this.ForwardView(false);
            _this.setNavTxt(2, defaultSet.area);
        }

        $cityElement.addClass('brouce-in');
    }
    ;

    CitySelect.prototype.close = function() {
        var _this = this;

        _this.$mask.remove();
        _this.$cityElement.removeClass('brouce-in').find('.cityselect-nav a').removeClass('crt').html('');
        _this.$itemBox.html('');
    }
    ;

    CitySelect.prototype.createDOM = function() {
        var _this = this;

        _this.$mask = $('<div class="mask-black"></div>');

        _this.$cityElement = $('' + '<div class="m-cityselect">' + '    <div class="cityselect-header">' + '        <p class="cityselect-title">所在地区</p>' + '        <div class="cityselect-nav">' + '            <a href="javascript:;" ></a>' + '            <a href="javascript:;"></a>' + '            <a href="javascript:;"></a>' + '        </div>' + '    </div>' + '    <ul class="cityselect-content">' + '        <li class="cityselect-item">' + '            <div class="cityselect-item-box"></div>' + '        </li>' + '        <li class="cityselect-item">' + '            <div class="cityselect-item-box"></div>' + '        </li>' + '        <li class="cityselect-item">' + '            <div class="cityselect-item-box"></div>' + '        </li>' + '    </ul>' + '</div>');

        $body.append(_this.$cityElement);

        _this.$itemBox = _this.$cityElement.find('.cityselect-item-box');

        _this.$cityElement.on('click.ydui.cityselect', '.cityselect-nav a', function() {
            var $this = $(this);

            $this.addClass('crt').siblings().removeClass('crt');

            $this.index() < 2 ? _this.backOffView() : _this.ForwardView(true);
        });
    }
    ;

    CitySelect.prototype.setNavTxt = function(index, txt) {

        var $nav = this.$cityElement.find('.cityselect-nav a');

        index < 2 && $nav.removeClass('crt');

        $nav.eq(index).html(txt);
        $nav.eq(index + 1).addClass('crt').html('请选择');
        $nav.eq(index + 2).removeClass('crt').html('');
    }
    ;

    CitySelect.prototype.backOffView = function() {
        this.$cityElement.find('.cityselect-content').removeClass('cityselect-next').addClass('cityselect-move-animate cityselect-prev');
    }
    ;

    CitySelect.prototype.ForwardView = function(animate) {
        this.$cityElement.find('.cityselect-content').removeClass('cityselect-move-animate cityselect-prev').addClass((animate ? 'cityselect-move-animate' : '') + ' cityselect-next');
    }
    ;

    CitySelect.prototype.bindItemEvent = function() {
        var _this = this
          , $cityElement = _this.$cityElement;

        $cityElement.on('click.ydui.cityselect', '.cityselect-item-box a', function() {
            var $this = $(this);

            if ($this.hasClass('crt'))
                return;
            $this.addClass('crt').siblings().removeClass('crt');

            var tag = $this.data('tag');

            _this.setNavTxt(tag, $this.text());

            var $nav = $cityElement.find('.cityselect-nav a')
              , defaultSet = _this.defaultSet;

            if (tag == 0) {

                _this.loadCity();
                $cityElement.find('.cityselect-item-box').eq(1).find('a').removeClass('crt');

            } else if (tag == 1) {

                _this.loadArea();
                _this.ForwardView(true);
                $cityElement.find('.cityselect-item-box').eq(2).find('a').removeClass('crt');

            } else {

                defaultSet.provance = $nav.eq(0).html();
                defaultSet.city = $nav.eq(1).html();
                defaultSet.area = $nav.eq(2).html();

                _this.returnValue();
            }
        });
    }
    ;

    CitySelect.prototype.returnValue = function() {
        var _this = this
          , defaultSet = _this.defaultSet;

        _this.$element.trigger($.Event('done.ydui.cityselect', {
            provance: defaultSet.provance,
            city: defaultSet.city,
            area: defaultSet.area
        }));

        _this.close();
    }
    ;

    CitySelect.prototype.scrollPosition = function(index) {

        var _this = this
          , $itemBox = _this.$itemBox.eq(index)
          , itemHeight = $itemBox.find('a.crt').height()
          , itemBoxHeight = $itemBox.parent().height();

        $itemBox.parent().animate({
            scrollTop: $itemBox.find('a.crt').index() * itemHeight - itemBoxHeight / 3
        }, 0, function() {
            _this.bindItemEvent();
        });
    }
    ;

    CitySelect.prototype.fillItems = function(index, arr) {
        var _this = this;

        _this.$itemBox.eq(index).html(arr).parent().animate({
            scrollTop: 0
        }, 10);

        _this.scrollPosition(index);
    }
    ;

    CitySelect.prototype.loadProvance = function() {
        var _this = this;

        var arr = [];
        $.each(_this.citys, function(k, v) {
            arr.push($('<a class="' + (v.n == _this.defaultSet.provance ? 'crt' : '') + '" href="javascript:;"><span>' + v.n + '</span></a>').data({
                citys: v.c,
                tag: 0
            }));
        });
        _this.fillItems(0, arr);
    }
    ;

    CitySelect.prototype.loadCity = function() {
        var _this = this;

        var cityData = _this.$itemBox.eq(0).find('a.crt').data('citys');

        var arr = [];
        $.each(cityData, function(k, v) {
            arr.push($('<a class="' + (v.n == _this.defaultSet.city ? 'crt' : '') + '" href="javascript:;"><span>' + v.n + '</span></a>').data({
                citys: v.a,
                tag: 1
            }));
        });
        _this.fillItems(1, arr);
    }
    ;

    CitySelect.prototype.loadArea = function() {
        var _this = this;

        var areaData = _this.$itemBox.eq(1).find('a.crt').data('citys');

        var arr = [];
        $.each(areaData, function(k, v) {
            arr.push($('<a class="' + (v == _this.defaultSet.area ? 'crt' : '') + '" href="javascript:;"><span>' + v + '</span></a>').data({
                tag: 2
            }));
        });

        if (arr.length <= 0) {
            arr.push($('<a href="javascript:;"><span>全区</span></a>').data({
                tag: 2
            }));
        }
        _this.fillItems(2, arr);
    }
    ;

    function Plugin(option) {
        var args = Array.prototype.slice.call(arguments, 1);

        return this.each(function() {
            var $this = $(this)
              , citySelect = $this.data('ydui.cityselect');

            if (!citySelect) {
                $this.data('ydui.cityselect', (citySelect = new CitySelect(this,option)));
            }

            if (typeof option == 'string') {
                citySelect[option] && citySelect[option].apply(citySelect, args);
            }
        });
    }

    $.fn.citySelect = Plugin;

}(window);

!function(window) {
    var doc = window.document
      , ydui = window.YDUI
      , ua = window.navigator && window.navigator.userAgent || '';

    var ipad = !!ua.match(/(iPad).*OS\s([\d_]+)/)
      , ipod = !!ua.match(/(iPod)(.*OS\s([\d_]+))?/)
      , iphone = !ipad && !!ua.match(/(iPhone\sOS)\s([\d_]+)/);

    ydui.device = {
    };
}(window);
