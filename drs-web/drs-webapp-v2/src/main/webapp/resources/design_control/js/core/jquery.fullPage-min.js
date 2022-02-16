!function(e,n,t,o,i){"use strict";e.fn.fullpage=function(s){function a(e){e.find(".fp-slides").after('<div class="fp-controlArrow fp-prev"></div><div class="fp-controlArrow fp-next"></div>'),"#fff"!=s.controlArrowColor&&(e.find(".fp-controlArrow.fp-next").css("border-color","transparent transparent transparent "+s.controlArrowColor),e.find(".fp-controlArrow.fp-prev").css("border-color","transparent "+s.controlArrowColor+" transparent transparent")),s.loopHorizontal||e.find(".fp-controlArrow.fp-prev").hide()}function l(){e("body").append('<div id="fp-nav"><ul></ul></div>'),wn=e("#fp-nav"),wn.addClass(s.navigationPosition);for(var n=0;n<e(".fp-section").length;n++){var t="";s.anchors.length&&(t=s.anchors[n]);var o='<li><a href="#'+t+'"><span></span></a>',i=s.navigationTooltips[n];"undefined"!=typeof i&&""!==i&&(o+='<div class="fp-tooltip '+s.navigationPosition+'">'+i+"</div>"),o+="</li>",wn.find("ul").append(o)}}function r(){e(".fp-section").each(function(){var n=e(this).find(".fp-slide");n.length?n.each(function(){Y(e(this))}):Y(e(this))}),e.isFunction(s.afterRender)&&s.afterRender.call(this)}function c(){var i;if(!s.autoScrolling||s.scrollBar){for(var a=e(n).scrollTop(),l=0,r=o.abs(a-t.getElementsByClassName("fp-section")[0].offsetTop),c=t.getElementsByClassName("fp-section"),f=0;f<c.length;++f){var d=c[f],p=o.abs(a-d.offsetTop);r>p&&(l=f,r=p)}i=e(c).eq(l)}if(!s.autoScrolling||s.scrollBar){if(!i.hasClass("active")){In=!0;var u=e(".fp-section.active"),v=u.index(".fp-section")+1,h=U(i),g=i.data("anchor"),m=i.index(".fp-section")+1,S=i.find(".fp-slide.active");if(S.length)var w=S.data("anchor"),y=S.index();An&&(i.addClass("active").siblings().removeClass("active"),e.isFunction(s.onLeave)&&s.onLeave.call(u,v,m,h),e.isFunction(s.afterLoad)&&s.afterLoad.call(i,g,m),D(g,m-1),s.anchors.length&&(mn=g,G(y,w,g,m))),clearTimeout(zn),zn=setTimeout(function(){In=!1},100)}s.fitToSection&&(clearTimeout(Rn),Rn=setTimeout(function(){An&&(e(".fp-section.active").is(i)&&(kn=!0),b(i),kn=!1)},1e3))}}function f(e){return e.find(".fp-slides").length?e.find(".fp-slide.active").find(".fp-scrollable"):e.find(".fp-scrollable")}function d(e,n){if(Bn[e]){var t,o;if("down"==e?(t="bottom",o=gn.moveSectionDown):(t="top",o=gn.moveSectionUp),n.length>0){if(!O(t,n))return!0;o()}else o()}}function p(t){var i=t.originalEvent;if(!u(t.target)&&v(i)){s.autoScrolling&&t.preventDefault();var a=e(".fp-section.active"),l=f(a);if(An&&!bn){var r=ln(i);Pn=r.y,Hn=r.x,a.find(".fp-slides").length&&o.abs(Fn-Hn)>o.abs(Nn-Pn)?o.abs(Fn-Hn)>e(n).width()/100*s.touchSensitivity&&(Fn>Hn?Bn.right&&gn.moveSlideRight():Bn.left&&gn.moveSlideLeft()):s.autoScrolling&&o.abs(Nn-Pn)>e(n).height()/100*s.touchSensitivity&&(Nn>Pn?d("down",l):Pn>Nn&&d("up",l))}}}function u(n,t){t=t||0;var o=e(n).parent();return t<s.normalScrollElementTouchThreshold&&o.is(s.normalScrollElements)?!0:t==s.normalScrollElementTouchThreshold?!1:u(o,++t)}function v(e){return"undefined"==typeof e.pointerType||"mouse"!=e.pointerType}function h(n){var t=n.originalEvent;if(s.fitToSection&&e("html,body").stop(),v(t)){var o=ln(t);Nn=o.y,Fn=o.x}}function g(e,n){for(var t=0,i=e.slice(o.max(e.length-n,1)),s=0;s<i.length;s++)t+=i[s];return o.ceil(t/n)}function m(t){if(s.autoScrolling){t=n.event||t;var i=t.wheelDelta||-t.deltaY||-t.detail,a=o.max(-1,o.min(1,i));En.length>149&&En.shift(),En.push(o.abs(i)),s.scrollBar&&(t.preventDefault?t.preventDefault():t.returnValue=!1);var l=e(".fp-section.active"),r=f(l);if(An){var c=g(En,10),p=g(En,70),u=c>=p;u&&(0>a?d("down",r):d("up",r))}return!1}s.fitToSection&&e("html,body").stop()}function S(n){var t=e(".fp-section.active"),o=t.find(".fp-slides");if(o.length&&!bn){var i=o.find(".fp-slide.active"),a=null;if(a="prev"===n?i.prev(".fp-slide"):i.next(".fp-slide"),!a.length){if(!s.loopHorizontal)return;a=i.siblings("prev"===n?":last":":first")}bn=!0,z(o,a)}}function w(){e(".fp-slide.active").each(function(){rn(e(this))})}function b(t,o,i){var a=t.position();if("undefined"!=typeof a){var l={element:t,callback:o,isMovementUp:i,dest:a,dtop:a.top,yMovement:U(t),anchorLink:t.data("anchor"),sectionIndex:t.index(".fp-section"),activeSlide:t.find(".fp-slide.active"),activeSection:e(".fp-section.active"),leavingSection:e(".fp-section.active").index(".fp-section")+1,localIsResizing:kn};if(!(l.activeSection.is(t)&&!kn||s.scrollBar&&e(n).scrollTop()===l.dtop)){if(l.activeSlide.length)var r=l.activeSlide.data("anchor"),c=l.activeSlide.index();s.autoScrolling&&s.continuousVertical&&"undefined"!=typeof l.isMovementUp&&(!l.isMovementUp&&"up"==l.yMovement||l.isMovementUp&&"down"==l.yMovement)&&(l=C(l)),t.addClass("active").siblings().removeClass("active"),An=!1,G(c,r,l.anchorLink,l.sectionIndex),e.isFunction(s.onLeave)&&!l.localIsResizing&&s.onLeave.call(l.activeSection,l.leavingSection,l.sectionIndex+1,l.yMovement),y(l),mn=l.anchorLink,D(l.anchorLink,l.sectionIndex)}}}function y(n){if(s.css3&&s.autoScrolling&&!s.scrollBar){var t="translate3d(0px, -"+n.dtop+"px, 0px)";j(t,!0),setTimeout(function(){k(n)},s.scrollingSpeed)}else{var o=x(n);e(o.element).animate(o.options,s.scrollingSpeed,s.easing).promise().done(function(){k(n)})}}function x(e){var n={};return s.autoScrolling&&!s.scrollBar?(n.options={top:-e.dtop},n.element="."+Ln):(n.options={scrollTop:e.dtop},n.element="html, body"),n}function C(n){return n.isMovementUp?e(".fp-section.active").before(n.activeSection.nextAll(".fp-section")):e(".fp-section.active").after(n.activeSection.prevAll(".fp-section").get().reverse()),cn(e(".fp-section.active").position().top),w(),n.wrapAroundElements=n.activeSection,n.dest=n.element.position(),n.dtop=n.dest.top,n.yMovement=U(n.element),n}function T(n){n.wrapAroundElements&&n.wrapAroundElements.length&&(n.isMovementUp?e(".fp-section:first").before(n.wrapAroundElements):e(".fp-section:last").after(n.wrapAroundElements),cn(e(".fp-section.active").position().top),w())}function k(n){T(n),e.isFunction(s.afterLoad)&&!n.localIsResizing&&s.afterLoad.call(n.element,n.anchorLink,n.sectionIndex+1),An=!0,setTimeout(function(){e.isFunction(n.callback)&&n.callback.call(this)},600)}function A(){var e=n.location.hash.replace("#","").split("/"),t=e[0],o=e[1];t&&Q(t,o)}function E(){if(!In){var e=n.location.hash.replace("#","").split("/"),t=e[0],o=e[1];if(t.length){var i="undefined"==typeof mn,s="undefined"==typeof mn&&"undefined"==typeof o&&!bn;(t&&t!==mn&&!i||s||!bn&&Sn!=o)&&Q(t,o)}}}function L(n){clearTimeout(Vn);var o=e(t.activeElement);if(!o.is("textarea")&&!o.is("input")&&!o.is("select")&&s.keyboardScrolling&&s.autoScrolling){var i=n.which,a=[40,38,32,33,34];a.indexOf(i)>-1&&n.preventDefault(),Vn=setTimeout(function(){B(n)},150)}}function B(n){var t=n.shiftKey;switch(n.which){case 38:case 33:gn.moveSectionUp();break;case 32:if(t){gn.moveSectionUp();break}case 40:case 34:gn.moveSectionDown();break;case 36:gn.moveTo(1);break;case 35:gn.moveTo(e(".fp-section").length);break;case 37:gn.moveSlideLeft();break;case 39:gn.moveSlideRight();break;default:return}}function M(e){An&&(e.pageY<Dn?gn.moveSectionUp():e.pageY>Dn&&gn.moveSectionDown()),Dn=e.pageY}function z(n,t){var o=t.position(),i=n.find(".fp-slidesContainer").parent(),a=t.index(),l=n.closest(".fp-section"),r=l.index(".fp-section"),c=l.data("anchor"),f=l.find(".fp-slidesNav"),d=t.data("anchor"),p=kn;if(s.onSlideLeave){var u=l.find(".fp-slide.active"),v=u.index(),h=q(v,a);p||"none"===h||e.isFunction(s.onSlideLeave)&&s.onSlideLeave.call(u,c,r+1,v,h)}t.addClass("active").siblings().removeClass("active"),"undefined"==typeof d&&(d=a),!s.loopHorizontal&&s.controlArrows&&(l.find(".fp-controlArrow.fp-prev").toggle(0!==a),l.find(".fp-controlArrow.fp-next").toggle(!t.is(":last-child"))),l.hasClass("active")&&G(a,d,c,r);var g=function(){p||e.isFunction(s.afterSlideLoad)&&s.afterSlideLoad.call(t,c,r+1,d,a),bn=!1};if(s.css3){var m="translate3d(-"+o.left+"px, 0px, 0px)";N(n.find(".fp-slidesContainer"),s.scrollingSpeed>0).css(fn(m)),setTimeout(function(){g()},s.scrollingSpeed,s.easing)}else i.animate({scrollLeft:o.left},s.scrollingSpeed,s.easing,function(){g()});f.find(".active").removeClass("active"),f.find("li").eq(a).find("a").addClass("active")}function R(){if(I(),yn){var i=e(t.activeElement);if(!i.is("textarea")&&!i.is("input")&&!i.is("select")){var s=e(n).height();o.abs(s-Un)>20*o.max(Un,s)/100&&(gn.reBuild(!0),Un=s)}}else clearTimeout(On),On=setTimeout(function(){gn.reBuild(!0)},500)}function I(){if(s.responsive){var t=Cn.hasClass("fp-responsive");e(n).width()<s.responsive?t||(gn.setAutoScrolling(!1,"internal"),gn.setFitToSection(!1,"internal"),e("#fp-nav").hide(),Cn.addClass("fp-responsive")):t&&(gn.setAutoScrolling(Mn.autoScrolling,"internal"),gn.setFitToSection(Mn.autoScrolling,"internal"),e("#fp-nav").show(),Cn.removeClass("fp-responsive"))}}function N(e){var n="all "+s.scrollingSpeed+"ms "+s.easingcss3;return e.removeClass("fp-notransition"),e.css({"-webkit-transition":n,transition:n})}function F(e){return e.addClass("fp-notransition")}function P(n,t){var i=825,s=900;if(i>n||s>t){var a=100*n/i,l=100*t/s,r=o.min(a,l),c=r.toFixed(2);e("body").css("font-size",c+"%")}else e("body").css("font-size","100%")}function H(n,t){s.navigation&&(e("#fp-nav").find(".active").removeClass("active"),n?e("#fp-nav").find('a[href="#'+n+'"]').addClass("active"):e("#fp-nav").find("li").eq(t).find("a").addClass("active"))}function V(n){s.menu&&(e(s.menu).find(".active").removeClass("active"),e(s.menu).find('[data-menuanchor="'+n+'"]').addClass("active"))}function D(e,n){V(e),H(e,n)}function O(e,n){return"top"===e?!n.scrollTop():"bottom"===e?n.scrollTop()+1+n.innerHeight()>=n[0].scrollHeight:void 0}function U(n){var t=e(".fp-section.active").index(".fp-section"),o=n.index(".fp-section");return t==o?"none":t>o?"up":"down"}function q(e,n){return e==n?"none":e>n?"left":"right"}function Y(e){e.css("overflow","hidden");var n,t=e.closest(".fp-section"),o=e.find(".fp-scrollable");o.length?n=o.get(0).scrollHeight:(n=e.get(0).scrollHeight,s.verticalCentered&&(n=e.find(".fp-tableCell").get(0).scrollHeight));var i=Tn-parseInt(t.css("padding-bottom"))-parseInt(t.css("padding-top"));n>i?o.length?o.css("height",i+"px").parent().css("height",i+"px"):(s.verticalCentered?e.find(".fp-tableCell").wrapInner('<div class="fp-scrollable" />'):e.wrapInner('<div class="fp-scrollable" />'),e.find(".fp-scrollable").slimScroll({allowPageScroll:!0,height:i+"px",size:"10px",alwaysVisible:!0})):W(e),e.css("overflow","")}function W(e){e.find(".fp-scrollable").children().first().unwrap().unwrap(),e.find(".slimScrollBar").remove(),e.find(".slimScrollRail").remove()}function X(e){e.addClass("fp-table").wrapInner('<div class="fp-tableCell" style="height:'+K(e)+'px;" />')}function K(e){var n=Tn;if(s.paddingTop||s.paddingBottom){var t=e;t.hasClass("fp-section")||(t=e.closest(".fp-section"));var o=parseInt(t.css("padding-top"))+parseInt(t.css("padding-bottom"));n=Tn-o}return n}function j(e,n){n?N(Cn):F(Cn),Cn.css(fn(e)),setTimeout(function(){Cn.removeClass("fp-notransition")},10)}function Q(n,t){var o;"undefined"==typeof t&&(t=0),o=isNaN(n)?e('[data-anchor="'+n+'"]'):e(".fp-section").eq(n-1),n===mn||o.hasClass("active")?$(o,t):b(o,function(){$(o,t)})}function $(e,n){if("undefined"!=typeof n){var t=e.find(".fp-slides"),o=t.find('[data-anchor="'+n+'"]');o.length||(o=t.find(".fp-slide").eq(n)),o.length&&z(t,o)}}function _(e,n){e.append('<div class="fp-slidesNav"><ul></ul></div>');var t=e.find(".fp-slidesNav");t.addClass(s.slidesNavPosition);for(var o=0;n>o;o++)t.find("ul").append('<li><a href="#"><span></span></a></li>');t.css("margin-left","-"+t.width()/2+"px"),t.find("li").first().find("a").addClass("active")}function G(e,n,t,o){var i="";s.anchors.length?(e?("undefined"!=typeof t&&(i=t),"undefined"==typeof n&&(n=e),Sn=n,J(i+"/"+n)):"undefined"!=typeof e?(Sn=n,J(t)):J(t),Z(location.hash)):Z("undefined"!=typeof e?o+"-"+e:String(o))}function J(e){if(s.recordHistory)location.hash=e;else if(yn||xn)history.replaceState(i,i,"#"+e);else{var t=n.location.href.split("#")[0];n.location.replace(t+"#"+e)}}function Z(n){n=n.replace("/","-").replace("#",""),e("body")[0].className=e("body")[0].className.replace(/\b\s?fp-viewing-[^\s]+\b/g,""),e("body").addClass("fp-viewing-"+n)}function en(){var e,o=t.createElement("p"),s={webkitTransform:"-webkit-transform",OTransform:"-o-transform",msTransform:"-ms-transform",MozTransform:"-moz-transform",transform:"transform"};t.body.insertBefore(o,null);for(var a in s)o.style[a]!==i&&(o.style[a]="translate3d(1px,1px,1px)",e=n.getComputedStyle(o).getPropertyValue(s[a]));return t.body.removeChild(o),e!==i&&e.length>0&&"none"!==e}function nn(){t.addEventListener?(t.removeEventListener("mousewheel",m,!1),t.removeEventListener("wheel",m,!1)):t.detachEvent("onmousewheel",m)}function tn(){t.addEventListener?(t.addEventListener("mousewheel",m,!1),t.addEventListener("wheel",m,!1)):t.attachEvent("onmousewheel",m)}function on(){if(yn||xn){var n=an();e(t).off("touchstart "+n.down).on("touchstart "+n.down,h),e(t).off("touchmove "+n.move).on("touchmove "+n.move,p)}}function sn(){if(yn||xn){var n=an();e(t).off("touchstart "+n.down),e(t).off("touchmove "+n.move)}}function an(){var e;return e=n.PointerEvent?{down:"pointerdown",move:"pointermove"}:{down:"MSPointerDown",move:"MSPointerMove"}}function ln(e){var n=[];return n.y="undefined"!=typeof e.pageY&&(e.pageY||e.pageX)?e.pageY:e.touches[0].pageY,n.x="undefined"!=typeof e.pageX&&(e.pageY||e.pageX)?e.pageX:e.touches[0].pageX,n}function rn(e){gn.setScrollingSpeed(0,"internal"),z(e.closest(".fp-slides"),e),gn.setScrollingSpeed(Mn.scrollingSpeed,"internal")}function cn(e){if(s.scrollBar)Cn.scrollTop(e);else if(s.css3){var n="translate3d(0px, -"+e+"px, 0px)";j(n,!1)}else Cn.css("top",-e)}function fn(e){return{"-webkit-transform":e,"-moz-transform":e,"-ms-transform":e,transform:e}}function dn(e,n){switch(n){case"up":Bn.up=e;break;case"down":Bn.down=e;break;case"left":Bn.left=e;break;case"right":Bn.right=e;break;case"all":gn.setAllowScrolling(e)}}function pn(){cn(0),e("#fp-nav, .fp-slidesNav, .fp-controlArrow").remove(),e(".fp-section").css({height:"","background-color":"",padding:""}),e(".fp-slide").css({width:""}),Cn.css({height:"",position:"","-ms-touch-action":"","touch-action":""}),e(".fp-section, .fp-slide").each(function(){W(e(this)),e(this).removeClass("fp-table active")}),F(Cn),F(Cn.find(".fp-easing")),Cn.find(".fp-tableCell, .fp-slidesContainer, .fp-slides").each(function(){e(this).replaceWith(this.childNodes)}),e("html, body").scrollTop(0)}function un(e,n,t){s[e]=n,"internal"!==t&&(Mn[e]=n)}function vn(){s.continuousVertical&&(s.loopTop||s.loopBottom)&&(s.continuousVertical=!1,hn("warn","Option `loopTop/loopBottom` is mutually exclusive with `continuousVertical`; `continuousVertical` disabled")),s.continuousVertical&&s.scrollBar&&(s.continuousVertical=!1,hn("warn","Option `scrollBar` is mutually exclusive with `continuousVertical`; `continuousVertical` disabled")),e.each(s.anchors,function(n,t){(e("#"+t).length||e('[name="'+t+'"]').length)&&hn("error","data-anchor tags can not have the same value as any `id` element on the site (or `name` element for IE).")})}function hn(e,n){console&&console[e]&&console[e]("fullPage: "+n)}var gn=e.fn.fullpage;s=e.extend({menu:!1,anchors:[],navigation:!1,navigationPosition:"right",navigationTooltips:[],slidesNavigation:!1,slidesNavPosition:"bottom",scrollBar:!1,css3:!0,scrollingSpeed:700,autoScrolling:!0,fitToSection:!0,easing:"easeInOutCubic",easingcss3:"ease",loopBottom:!1,loopTop:!1,loopHorizontal:!0,continuousVertical:!1,normalScrollElements:null,scrollOverflow:!1,touchSensitivity:5,normalScrollElementTouchThreshold:5,keyboardScrolling:!0,animateAnchor:!0,recordHistory:!0,controlArrows:!0,controlArrowColor:"#fff",verticalCentered:!0,resize:!1,sectionsColor:[],paddingTop:0,paddingBottom:0,fixedElements:null,responsive:0,sectionSelector:".s5_slidesection",slideSelector:".slide",afterLoad:null,onLeave:null,afterRender:null,afterResize:null,afterReBuild:null,afterSlideLoad:null,onSlideLeave:null},s),vn(),e.extend(e.easing,{easeInOutCubic:function(e,n,t,o,i){return(n/=i/2)<1?o/2*n*n*n+t:o/2*((n-=2)*n*n+2)+t}}),e.extend(e.easing,{easeInQuart:function(e,n,t,o,i){return o*(n/=i)*n*n*n+t}}),gn.setAutoScrolling=function(n,t){un("autoScrolling",n,t);var o=e(".fp-section.active");s.autoScrolling&&!s.scrollBar?(e("html, body").css({overflow:"hidden",height:"100%"}),gn.setRecordHistory(s.recordHistory,"internal"),Cn.css({"-ms-touch-action":"none","touch-action":"none"}),o.length&&cn(o.position().top)):(e("html, body").css({overflow:"visible",height:"initial"}),gn.setRecordHistory(!1,"internal"),Cn.css({"-ms-touch-action":"","touch-action":""}),cn(0),o.length&&e("html, body").scrollTop(o.position().top))},gn.setRecordHistory=function(e,n){un("recordHistory",e,n)},gn.setScrollingSpeed=function(e,n){un("scrollingSpeed",e,n)},gn.setFitToSection=function(e,n){un("fitToSection",e,n)},gn.setMouseWheelScrolling=function(e){e?tn():nn()},gn.setAllowScrolling=function(n,t){"undefined"!=typeof t?(t=t.replace(" ","").split(","),e.each(t,function(e,t){dn(n,t)})):n?(gn.setMouseWheelScrolling(!0),on()):(gn.setMouseWheelScrolling(!1),sn())},gn.setKeyboardScrolling=function(e){s.keyboardScrolling=e},gn.moveSectionUp=function(){var n=e(".fp-section.active").prev(".fp-section");n.length||!s.loopTop&&!s.continuousVertical||(n=e(".fp-section").last()),n.length&&b(n,null,!0)},gn.moveSectionDown=function(){var n=e(".fp-section.active").next(".fp-section");n.length||!s.loopBottom&&!s.continuousVertical||(n=e(".fp-section").first()),n.length&&b(n,null,!1)},gn.moveTo=function(n,t){var o="";o=isNaN(n)?e('[data-anchor="'+n+'"]'):e(".fp-section").eq(n-1),"undefined"!=typeof t?Q(n,t):o.length>0&&b(o)},gn.moveSlideRight=function(){S("next")},gn.moveSlideLeft=function(){S("prev")},gn.reBuild=function(t){if(!Cn.hasClass("fp-destroyed")){kn=!0;var o=e(n).width();Tn=e(n).height(),s.resize&&P(Tn,o),e(".fp-section").each(function(){var n=e(this).find(".fp-slides"),t=e(this).find(".fp-slide");s.verticalCentered&&e(this).find(".fp-tableCell").css("height",K(e(this))+"px"),e(this).css("height",Tn+"px"),s.scrollOverflow&&(t.length?t.each(function(){Y(e(this))}):Y(e(this))),t.length&&z(n,n.find(".fp-slide.active"))});var i=e(".fp-section.active");i.index(".fp-section")&&b(i),kn=!1,e.isFunction(s.afterResize)&&t&&s.afterResize.call(Cn),e.isFunction(s.afterReBuild)&&!t&&s.afterReBuild.call(Cn)}};var mn,Sn,wn,bn=!1,yn=navigator.userAgent.match(/(iPhone|iPod|iPad|Android|playbook|silk|BlackBerry|BB10|Windows Phone|Tizen|Bada|webOS|IEMobile|Opera Mini)/),xn="ontouchstart"in n||navigator.msMaxTouchPoints>0||navigator.maxTouchPoints,Cn=e(this),Tn=e(n).height(),kn=!1,An=!0,En=[],Ln="fullpage-wrapper",Bn={up:!0,down:!0,left:!0,right:!0},Mn=e.extend(!0,{},s);gn.setAllowScrolling(!0),Cn.removeClass("fp-destroyed"),s.css3&&(s.css3=en()),e(this).length?(Cn.css({height:"100%",position:"relative"}),Cn.addClass(Ln)):hn("error","Error! Fullpage.js needs to be initialized with a selector. For example: $('#myContainer').fullpage();"),e(s.sectionSelector).each(function(){e(this).addClass("fp-section")}),e(s.slideSelector).each(function(){e(this).addClass("fp-slide")}),s.navigation&&l(),e(".fp-section").each(function(n){var t=e(this),o=e(this).find(".fp-slide"),i=o.length;if(n||0!==e(".fp-section.active").length||e(this).addClass("active"),e(this).css("height",Tn+"px"),(s.paddingTop||s.paddingBottom)&&e(this).css("padding",s.paddingTop+" 0 "+s.paddingBottom+" 0"),"undefined"!=typeof s.sectionsColor[n]&&e(this).css("background-color",s.sectionsColor[n]),"undefined"!=typeof s.anchors[n]&&(e(this).attr("data-anchor",s.anchors[n]),e(this).hasClass("active")&&D(s.anchors[n],n)),i>1){var l=100*i,r=100/i;o.wrapAll('<div class="fp-slidesContainer" />'),o.parent().wrap('<div class="fp-slides" />'),e(this).find(".fp-slidesContainer").css("width",l+"%"),s.controlArrows&&a(e(this)),s.slidesNavigation&&_(e(this),i),o.each(function(){e(this).css("width",r+"%"),s.verticalCentered&&X(e(this))});var c=t.find(".fp-slide.active");c.length?rn(c):o.eq(0).addClass("active")}else s.verticalCentered&&X(e(this))}).promise().done(function(){gn.setAutoScrolling(s.autoScrolling,"internal");var o=e(".fp-section.active").find(".fp-slide.active");o.length&&(0!==e(".fp-section.active").index(".fp-section")||0===e(".fp-section.active").index(".fp-section")&&0!==o.index())&&rn(o),s.fixedElements&&s.css3&&e(s.fixedElements).appendTo("body"),s.navigation&&(wn.css("margin-top","-"+wn.height()/2+"px"),wn.find("li").eq(e(".fp-section.active").index(".fp-section")).find("a").addClass("active")),s.menu&&s.css3&&e(s.menu).closest(".fullpage-wrapper").length&&e(s.menu).appendTo("body"),s.scrollOverflow?("complete"===t.readyState&&r(),e(n).on("load",r)):e.isFunction(s.afterRender)&&s.afterRender.call(Cn),I();var i=n.location.hash.replace("#","").split("/"),a=i[0];if(a.length){var l=e('[data-anchor="'+a+'"]');!s.animateAnchor&&l.length&&(s.autoScrolling?cn(l.position().top):(cn(0),Z(a),e("html, body").scrollTop(l.position().top)),D(a,null),e.isFunction(s.afterLoad)&&s.afterLoad.call(l,a,l.index(".fp-section")+1),l.addClass("active").siblings().removeClass("active"))}e(n).on("load",function(){A()})});var zn,Rn,In=!1;e(n).on("scroll",c);var Nn=0,Fn=0,Pn=0,Hn=0;e(n).on("hashchange",E),e(n).keydown(L);var Vn;Cn.mousedown(function(e){2==e.which&&(Dn=e.pageY,Cn.on("mousemove",M))}),Cn.mouseup(function(e){2==e.which&&Cn.off("mousemove")});var Dn=0;e(t).on("click touchstart","#fp-nav a",function(n){n.preventDefault();var t=e(this).parent().index();b(e(".fp-section").eq(t))}),e(t).on("click touchstart",".fp-slidesNav a",function(n){n.preventDefault();var t=e(this).closest(".fp-section").find(".fp-slides"),o=t.find(".fp-slide").eq(e(this).closest("li").index());z(t,o)}),s.normalScrollElements&&(e(t).on("mouseenter",s.normalScrollElements,function(){gn.setMouseWheelScrolling(!1)}),e(t).on("mouseleave",s.normalScrollElements,function(){gn.setMouseWheelScrolling(!0)})),e(".fp-section").on("click touchstart",".fp-controlArrow",function(){e(this).hasClass("fp-prev")?gn.moveSlideLeft():gn.moveSlideRight()}),e(n).resize(R);var On,Un=Tn;gn.destroy=function(o){gn.setAutoScrolling(!1,"internal"),gn.setAllowScrolling(!1),gn.setKeyboardScrolling(!1),Cn.addClass("fp-destroyed"),e(n).off("scroll",c).off("hashchange",E).off("resize",R),e(t).off("click","#fp-nav a").off("mouseenter","#fp-nav li").off("mouseleave","#fp-nav li").off("click",".fp-slidesNav a").off("mouseover",s.normalScrollElements).off("mouseout",s.normalScrollElements),e(".fp-section").off("click",".fp-controlArrow"),o&&pn()}}}(jQuery,window,document,Math);