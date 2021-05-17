document.write("<script src='/js/SweetAlert2.js'></script>");
document.write("<script src='/js/template.js'></script>");
document.write("<link href='/js/star/jquery.raty.css' />");
document.write("<script src='/js/star/jquery.raty.js'></script>");
document.write("<dic id='where' style='display: none'></dic>");
var App = function () {
    // 全局设置
    var globalSetting = function () {
        // 扩展时间日期格式功能
        Date.prototype.format = function (fmt) {
            fmt = (fmt = $.trim(fmt)) === '' ? 'yyyy/MM/dd hh:mm:ss' : fmt;
            var o = {
                "M+": this.getMonth() + 1,                 //月份
                "d+": this.getDate(),                    //日
                "h+": this.getHours(),                   //小时
                "m+": this.getMinutes(),                 //分
                "s+": this.getSeconds(),                 //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds()             //毫秒
            };
            if (/(y+)/.test(fmt)) {
                fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            }
            for (var k in o) {
                if (new RegExp("(" + k + ")").test(fmt)) {
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
                }
            }
            return fmt;
        };
        // 异步ajax报错异常处理
        // $(document).ajaxError(function(event, xhr, settings, thrownError) {
        //     App.topAlert(thrownError?thrownError:xhr.statusText,2);
        // });
        // 异步ajax全局参数设定
        $.ajaxSetup({timeout: '120000'});
        $.extend({
            // 默认没有loader遮罩层
            postJSON: function (url, data, options) {
                var _url = (((typeof url) == 'string') ? url : undefined);
                var _data = (((typeof data) == 'object' && data != null) ? JSON.stringify(data) : (((typeof data) == 'string') ? data : undefined));
                var _options = (((typeof options) == 'object' && options != null) ? $.extend(true, {}, options) : {});
                var _success = ($.isFunction(options) ? options : undefined);
                var _options_default = {
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json;charset=UTF-8',
                    url: _url,
                    data: _data,
                    success: _success
                };
                var _options_apply = $.extend(true, {}, _options_default, _options);
                return $.ajax(_options_apply);
            },
            // 调用postJSON发送接收数据，默认使用loader遮罩层
            loadJSON: function (url, data, options) {
                var _options = (((typeof options) == 'object' && options != null) ? $.extend(true, {}, options) : {});
                var _beforeSend_function = $.isFunction(_options.beforeSend) ? _options.beforeSend : function () {
                };
                var beforeSend_options = {
                    beforeSend: function () {
                        App.loader();
                        _beforeSend_function();
                    }
                };
                var _options_apply = $.extend(true, {}, _options, beforeSend_options);
                return $.postJSON(url, data, _options_apply).always(function () {
                    App.removeLoader();
                });
            },
            // 页面中最大的层值
            maxZindex: function (el) {
                var $el = $(el || 'body'), $elArr = $el.andChildren().andChildren().andChildren().add($('div', $el));
                return Math.max.apply(null, $elArr.map(function () {
                    if ($(this).css('position') !== 'static') {
                        return (isNaN($(this).css('z-index')) ? 1 : $(this).css('z-index'));
                    }
                }));
            },
        });
        // 扩展$元素能力
        $.fn.extend({
            andChildren: function () {
                return $(this).add($(this).find('>*'));
            },
        });
    };
    // page_turn页面跳转函数集
    var page_turn_functions = (function () {
        // 顶部跳转
        // 笨方法，不过能用就好了
        $("#header-goto-home").off('click').on('click', function () {
            App.goHomeBySelf();
        });
        $("#header-goto-issue").off('click').on('click', function () {
            App.goIssueBySelf();
        });
        $("#header-goto-reservation").off('click').on('click', function () {
            App.goReservationBySelf();
        });
        $("#header-goto-evaluate").off('click').on('click', function () {
            App.goEvaluateBySelf();
        });
        $("#header-goto-history").off('click').on('click', function () {
            App.goHistoryBySelf();
        });
        $("#header-loginOut").off('click').on('click', function () {
            var uName = $('#header-userDropdown').data('uName');
            var uNbr = $('#header-userDropdown').data('uNbr');
            App.loginOut(uNbr, uName);
        });
        return {
            //在自身窗口打开
            goLoginBySelf: function () {
                window.open("/dtr/login", "_self");
            },
            goHomeBySelf: function () {
                window.open("/dtr/home", "_self", "scrollbars=yes,resizable=1,modal=false,alwaysRaised=yes");
            },
            goIssueBySelf: function () {
                App.getUserMsg(function (uNbr, uName, uType) {
                    if (App.isTeacher(uType)) {
                        window.open('/dtr/issue', "_self");
                    } else {
                        App.alert('错误', '没有权限', 2);
                    }
                });
            },
            goReservationBySelf: function () {
                window.open('/dtr/reservation', "_self");
            },
            goEvaluateBySelf: function () {
                window.open('/dtr/evaluate', "_self");
            },
            goHistoryBySelf: function () {
                window.open('/dtr/history', "_self");
            },
            goAdminLoginBySelf:function () {
                window.open("/dtr/admin-login", "_self", "scrollbars=yes,resizable=1,modal=false,alwaysRaised=yes");
            },
            goAdminHomeBySelf:function () {
                window.open("/dtr/admin", "_self", "scrollbars=yes,resizable=1,modal=false,alwaysRaised=yes");
            },
            //新窗口打开
            goHomeByNewPage: function () {
                window.open('/dtr/home', "_blank");
            },
            goIssueByNewPage: function () {
                App.getUserMsg(function (uNbr, uName, uType) {
                    if (App.isTeacher(uType)) {
                        window.open('/dtr/issue', "_blank");
                    } else {
                        App.alert('错误', '没有权限', 2);
                    }
                });
            },
            goReservationByNewPage: function () {
                window.open('/dtr/reservation', "_blank");
            },
            goEvaluateByNewPage: function () {
                window.open('/dtr/evaluate', "_blank");
            },
            goHistoryByNewPage: function () {
                window.open('/dtr/history', "_blank");
            },
            goAdminLoginByNewPage:function () {
                window.open("/dtr/admin-login", "_blank");
            },
            goAdminHomeByNewPage:function () {
                window.open("/dtr/admin", "_blank");
            },
        }
    })();
    // utils工具函数集
    var utils_functions = (function () {
        $(document).on('show.bs.modal', '.modal', function (event) {
            $(this).appendTo($('body'));
        }).on('shown.bs.modal', '.modal.in', function (event) {
            App.setModalsAndBackdropsOrder();
        }).on('hidden.bs.modal', '.modal', function (event) {
            App.setModalsAndBackdropsOrder();
        });
        $("#user-msg").off("click").on("click",function () {
            $.loadJSON('/dtr/user/getUserMsg').done(function (data) {
                if (!App.checker(data)) {
                    return;
                } else {
                    var userData = data.user;
                    App.paddingSelfMsgBox(userData)
                        .then(function(){$("#myselfMsg").modal('show');return this;})
                        .then(function(){$("#myselfMsg").data('user',userData);return this;})
                }
            });
        });
        return {
            loginOut: function (uNbr, uName) {
                App.selectAlert('操作提示', "用户" + uName + "确定退出吗?", 3, function () {
                    $.loadJSON('/dtr/user/loginOut', {
                        uNbr: uNbr
                    }).done(function (data) {
                        if (!App.checker(data)) {
                            return;
                        } else {
                            App.topAlert("退出成功！", 1, 1300, function () {
                                window.open("/dtr/login", "_self");
                            });
                            return;
                        }
                    });
                })
            },
            getUserMsg: function (callback) {
                return $.Deferred(function (defer) {
                    $.loadJSON('/dtr/user/getUser').done(function (data) {
                        if (!App.checker(data)) {
                            App.alert('访问失败', '请先登录', 2, function () {
                                App.goLoginBySelf();
                                defer.resolve();
                            });
                        } else {
                            $('.header-userName').html(data.user.USER_NAME + "(" + data.user.TYPE_NAME + ")");
                            $('#header-userDropdown').data("uNbr", data.user.USER_NBR);
                            $('#header-userDropdown').data("uName", data.user.USER_NAME);
                            $('#header-userDropdown').data("uType", data.user.TYPE_NAME);
                            if ($.isFunction(callback)) {
                                callback(data.user.USER_NBR, data.user.USER_NAME, data.user.TYPE_NAME);
                            }
                            defer.resolve();
                        }
                    });
                }).promise();
            },
            formatDateString: function (value) {
                return new Date(value).format('yyyy-MM-dd hh:mm:ss');
            },
            returnRowData: function (e) {
                var index = $(e).parent().attr("ud-index");
                console.log(uiduck.data[index]);
                return uiduck.data[index];
            },
            setModalsAndBackdropsOrder: function () {
                var modalZIndex = 1040;
                $('.modal.in').each(function (index) {
                    var $modal = $(this);
                    modalZIndex++;
                    $modal.css('zIndex', modalZIndex);
                    $modal.next('.modal-backdrop.in').addClass('hidden').css('zIndex', modalZIndex - 1);
                });
                $('.modal.in:visible:last').focus().next('.modal-backdrop.in').removeClass('hidden');
            },
            panelClick: function () {
                var where = App.getWhere();
                switch (where) {
                    case "issue":
                        return issue.panelClick();
                    case "evaluate":
                        return evaluate.panelClick();
                    case "reservation":
                        return reservation.panelClick();
                    default:
                        return;
                }
            },
            setWhere: function (where) {
                return $.Deferred(function (defer) {
                    $('#where').data('where', where);
                    defer.resolve();
                }).promise();
            },
            getWhere: function () {
                return $('#where').data('where');
            },
            // 生成两数之间的随机整数
            randomNum: function (minNum, maxNum) {
                switch (arguments.length) {
                    case 1:
                        return parseInt(Math.random() * minNum + 1, 10);
                        break;
                    case 2:
                        return parseInt(Math.random() * (maxNum - minNum + 1) + minNum, 10);
                        break;
                    default:
                        return 0;
                        break;
                }
            },
            //rgb颜色随机
            rgb: function () {
                var r = Math.floor(Math.random() * 256);
                var g = Math.floor(Math.random() * 256);
                var b = Math.floor(Math.random() * 256);
                var rgb = '(' + r + ',' + g + ',' + b + ')';
                return rgb;
            },
            //十六进制颜色随机
            color16: function () {
                var r = Math.floor(Math.random() * 256);
                var g = Math.floor(Math.random() * 256);
                var b = Math.floor(Math.random() * 256);
                var color = '#' + r.toString(16) + g.toString(16) + b.toString(16) +"";
                return color;
            },
            // 深色随机
            shengColor: function(){
                return '#' +
                    (function(color) {
                        return(color += '5678956789defdef' [Math.floor(Math.random() * 16)]) &&
                        (color.length == 6) ? color : arguments.callee(color);
                    })('');
            },
            // 浅色随机
            qianColor: function(){
                return '#' +
                    (function(color) {
                        return(color += '0123401234abcabc' [Math.floor(Math.random() * 16)]) &&
                        (color.length == 6) ? color : arguments.callee(color);
                    })('');
            },
            randomBackgroundColor:function (_classname) {
                return $.Deferred(function (defer) {
                    var itme = document.getElementsByClassName(_classname);
                    for (var i=0;i<=itme.length;i++){
                        switch (i) {
                            case 0:
                                itme[i].style.backgroundColor= "#00a8ff";
                                break;
                            case 1:
                                itme[i].style.backgroundColor= "#4cd137";
                                break;
                            case 2:
                                itme[i].style.backgroundColor= "#ff6b81";
                                break;
                            case 3:
                                itme[i].style.backgroundColor= "#57606f";
                                break;
                            case 4:
                                itme[i].style.backgroundColor= "#5352ed";
                                break;
                            default:
                                itme[i].style.backgroundColor=App.shengColor();
                                break;
                        }
                    }
                    defer.resolve();
                }).promise();
            },
            paddingSelfMsgBox: function(data) {
                return $.Deferred(function (defer) {
                    $("#self-msg-box-container").html(template("self-msg-box-art",{data:data}));
                    defer.resolve();
                }).promise();
            },

        }
    })();
    // utils常用ui函数集
    var utils_ui_functions = (function () {
        return {
            loader: function () {
                var $loader = $('#loader');
                if ($loader.length === 0) {
                    $loader = $('<div id="loader" style="z-index:' + ($.maxZindex() + 1) + ';"><div class="loader-masker fade in" style="z-index: "' + ($.maxZindex() + 1) + ';></div><h4 class="loader-loading"><span class="glyphicon glyphicon-user"> <span class="spinner-border" style="font-size: 20px;width: 30px;height: 30px" role="status" aria-hidden="true"></span>Loading... </span></h4></div>').appendTo('body');
                } else {
                    var _za = $loader.css('z-index');
                    var _czArray = $loader.data('cache-z-index-value-array');
                    _czArray = $.isArray(_czArray) ? _czArray : [];
                    _czArray.push(_za);
                    $loader.css('z-index', +$.maxZindex() + 1);
                    $loader.data('cache-z-index-value-array', _czArray);
                }
                var vivi = +$loader.data('loader-counts-vivi');
                $loader.data('loader-counts-vivi', '' + ((isNaN(vivi) ? 0 : vivi) + 1));
                return this;
            },
            // 移除遮罩层
            removeLoader: function (force) {
                var $loader = $('#loader');
                if ($loader.length > 0) {
                    var vivi = +$loader.data('loader-counts-vivi');
                    vivi = Math.max((isNaN(vivi) ? 0 : vivi) - 1, 0);
                    if (force || vivi <= 0) {
                        $loader.removeData('loader-counts-vivi').removeData('cache-z-index-value-array').remove();
                    } else {
                        $loader.data('loader-counts-vivi', '' + vivi);
                        var _czArray = $loader.data('cache-z-index-value-array');
                        _czArray = $.isArray(_czArray) ? _czArray : [];
                        $loader.css('z-index', _czArray.pop() || $.maxZindex());
                        $loader.data('cache-z-index-value-array', _czArray);
                    }
                }
                return this;
            },
            alertClose: function () {
                swal.close();
            },
            checkAlertType: function (type) {
                switch (type) {
                    case 1:
                        return 'success';
                    case 2:
                        return 'error';
                    case 3:
                        return 'warning';
                    case 4:
                        return 'info';
                    case 5:
                        return 'question';
                    default:
                        return;
                }
            },
            alert: function (title = '提示', text = '操作成功', type = 1, callback) {
                var icon = App.checkAlertType(type);
                Swal.fire({
                    title: title,
                    text: text,
                    icon: icon,
                    confirmButtonText: '确定',
                    allowEscapeKey: true,
                    backdrop: `rgba(0, 0, 0, 0.6)`
                }).then(function () {
                    if ($.isFunction(callback)) {
                        callback();
                        return;
                    }
                });
                return;
            },
            selectAlert: function (title = '操作提示', text = '确定吗', type = 1, callback) {
                var icon = App.checkAlertType(type);
                Swal.fire({
                    icon: icon, // 弹框类型
                    title: title, //标题
                    text: text, //显示内容
                    confirmButtonColor: '#3085d6', // 确定按钮的 颜色
                    confirmButtonText: '确定', // 确定按钮的 文字
                    showCancelButton: true, // 是否显示取消按钮
                    cancelButtonColor: '#d33', // 取消按钮的 颜色
                    cancelButtonText: "取消", // 取消按钮的 文字
                    focusCancel: true, // 是否聚焦 取消按钮
                    reverseButtons: false // 是否 反转 两个按钮的位置 默认是  左边 确定  右边 取消
                }).then(function (isConfirm) {
                    try {
                        //判断 是否 点击的 确定按钮
                        if (isConfirm.value) {
                            if ($.isFunction(callback)) {
                                callback();
                                return;
                            }
                        } else {
                            return;
                        }
                    } catch (e) {
                        alert(e);
                    }
                });
            },
            topAlert: function (title = '操作提示', type = 1, timer = 3000, callback) {
                var icon = App.checkAlertType(type);
                Swal.fire({
                    toast: true,
                    position: 'top',
                    showConfirmButton: false,
                    //时间进度条
                    // timerProgressBar:true,
                    timer: timer, //毫秒
                    icon: icon,
                    title: title
                }).then(function () {
                    if ($.isFunction(callback)) {
                        callback();
                        return;
                    }
                });
                return;
            },
            msgAlert: function (title, content) {
                Swal.fire({
                    title: '<strong>' + title + '</strong>',
                    type: 'info',
                    html: content, // HTML
                    focusConfirm: true, //聚焦到确定按钮
                    showCloseButton: true,//右上角关闭
                })

            },
            inputAlert: function (input = 'text',title = '操作提示', text =  '请输入',  callback) {
                Swal.mixin({
                    input: input,
                    confirmButtonColor: '#3085d6', // 确定按钮的 颜色
                    confirmButtonText: '确定', // 确定按钮的 文字
                    showCancelButton: true, // 是否显示取消按钮
                    cancelButtonColor: '#d33', // 取消按钮的 颜色
                    cancelButtonText: "取消", // 取消按钮的 文字
                }).queue([{
                    title: title,
                    text: text
                }]).then(function(result){
                    if (result.value) {
                        if ($.isFunction(callback)) {
                            callback(result.value);
                            return;
                        }
                    }
                })
            },
            // 检验后端返回的数据成功or失败，并可以控制在屏幕顶部提示
            // 设置时间输入框
            setInputBoxForTime: function (_id_or_class) {
                $(_id_or_class).datetimepicker({
                    bootcssVer: 4,
                    format: 'yyyy-mm-dd hh:ii',
                    todayBtn: 'linked',
                    todayHighlight: true,
                    autoClose: true,
                    Integer: 1,
                    startDate: new Date(),
                    // endDate:App.getAfterTwoMonth(),
                    language: 'zh-CN'
                });
            },
            //
            // getAfterTwoMonth:function () {
            //     var now = new Date();
            //     var year = now.getFullYear();
            //     var month = now.getMonth() + 1;
            //     var day = now.getDay();
            //     if (month === 2){
            //         if ()
            //             }
            //     return newDate
            // }
        }
    })();
    // utils检验函数集
    var utils_checker = (function () {
        return {
            checker: function (_response, _tips) {
                _tips = _tips === undefined ? true : _tips;
                if (typeof (_response) !== 'object' || _response == null
                    || typeof (_response['health']) !== 'object' || _response['health'] == null) {
                    if (_tips) {
                        App.topAlert('arguments error!');
                    }
                    return false;
                }
                var health = _response['health'];
                var _function_tips = function () {
                    if (_tips) {
                        App.topAlert(health.message, 2);
                    }
                };
                switch (+health.code) {
                    case 0:
                        return true;
                    default:
                        _function_tips();
                        return false;
                }
            },
            isTeacher: function (type) {
                return type === '教师';
            }
        }
    })();
    return {
        init: function () {
            // 初始化全局设置
            return App.bindEvents();
        },
        bindEvents: function () {
            return $.Deferred(function (defer) {
                globalSetting($.extend(true, App, utils_functions, utils_ui_functions, utils_checker, page_turn_functions)); // 全局功能设置
                defer.resolve();
            }).promise();
        },
    }
}();
$(function () {
    App.init();
});