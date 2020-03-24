/**
 * 从ump.common.js截取部分js
 */
var CONTEXT_PATCH = '';//获取
var isShowLoading = false;
var msgDialogP = 9;
var msgLeftDialogP = 2;

var STORAGE_TICKET = 'adminticket'
var _DOMAIN = 'admin';

/**
 * 重写jquery.ajax()函数
 * @param $
 * @returns
 */
var ajax = $.ajax;
$.ajax = function (opt) {
    //备份opt中error和success方法
    var fn = {
        success: function (data, textStatus, jqXHR) {
        }
    }
    if (opt.success) {
        fn.success = opt.success;
    }
    //扩展增强处理
    var _opt = $.extend(opt, {
        success: function (data, textStatus, jqXHR) {
            //alert('重写success事件');
        	if(data && data.redirectUrl) {
    			top.location.href=data.redirectUrl;
    			return;
    		}
            fn.success(data, textStatus, jqXHR);
        }
    });
    var def = ajax.call($, _opt); // 兼容不支持异步回调的版本
    if('done' in def){
        var done = def.done;
        def.done = function (func) {
            function _done(data) {
            	if(data && data.redirectUrl) {
        			top.location.href=result.redirectUrl;
        			return;
        		}
                func(data);
            }

            done.call(def, _done);
            return def;
        };
    }
    return def;
};

// jQuery全局设置
var token = window.sessionStorage.getItem("token");
var vTicket2 = window.sessionStorage.getItem(STORAGE_TICKET);
if(vTicket2 == null || vTicket2 == undefined){
	vTicket2 = '';
}
$.ajaxSetup({
    dataType: "json",
    cache: false,
    headers: {
		"tdgoken": token,
		"tdgket": vTicket2
    },
    xhrFields: {
        withCredentials: true
	},
	//完成请求后触发。即在success或error触发后触发
　	complete: function (xhr, status) { 
		window.sessionStorage.setItem("request_action","true");
	}
});

appendExtraParams = function(url){
    var vDomain = _DOMAIN;
    if (url.indexOf("?") == -1) {
        return url + "?domain=" + vDomain;
    } else {
        return url + "&domain=" + vDomain;
    }
};

function linkTo(url,isBlank) {
	var href = url;
	href = addDefaultParamToUrl(href);
	if( window.parent.document.getElementById('infosafetyPage')){
		 window.parent.document.getElementById('infosafetyPage').src ='http://'+window.location.host+'/qm/'+href;
	}else{
	if (isBlank){
		var _openWin = window.open(href);
		return _openWin;
	}else{
		window.location.href = href;
	}
	}
}

function addDefaultParamToUrl(url){

	var _params = $.ajaxDefaultParams || {};
	for (var obj in _params){
		url = addUrlParam(url,obj,_params[obj]);
	}
	var vCpuSerial = getCookie('ProcessorID'); 
	if (vCpuSerial) {
		url = url + '&clientCpuSerial=' + vCpuSerial;
	}
	var vClientMac = getCookie('MACAddress');
	if (vClientMac) {
		url = url + '&clientMac=' + vClientMac;
	}
	var vClinetIps = getCookie('IPAddress');
	if (vClinetIps) {
		url = url + '&clientIps=' + vClinetIps;
	}
	return url;
}

var getCookie = function (name){
    var start = document.cookie.indexOf( name + "=" );
    var len = start + name.length + 1;
    if ( ( !start ) && ( name != document.cookie.substring( 0, name.length ) ) ) {
    	return null;
    }
    if ( start == -1 )
        return null;

    var end = document.cookie.indexOf( ';', len );
    if ( end == -1 )
        end = document.cookie.length;

    return unescape( document.cookie.substring( len, end ));
}


function getDefaultDgConfig(config){
	var _default = {
					singleSelect : true,
					pagination : true,
					pageSize: '10',
					striped : true,
					method : 'POST',
					collapsible : true,
					fitColumns : true,
					remoteSort : true,
					onBeforeLoad : function(param){
						var sort = param['sort'];
						if (sort){
							var columnOption = $(this).datagrid('getColumnOption',sort);
							var sortName = sort;
							if (columnOption && columnOption['sortName']){
								sortName = columnOption['sortName'];
							}else{
								//排序默认选中
								var columnFields = $(this).datagrid('getColumnFields');
								for (var i = 0 ; i < columnFields.length; i++){
									var columnOptions = $(this).datagrid('getColumnOption',columnFields[i]);
									if (columnOptions['sortName'] == sort){
										var field = columnFields[i];
										var orderClass = param['sortOrder'] == 'desc'?'datagrid-sort-asc':'datagrid-sort-desc';
										$('td[field="' + field + '"]').find('div').addClass(orderClass);
									}
								}
							}
							$(this).datagrid('options').url = addUrlParam($(this).datagrid('options').url,'sortName',sortName);
						}
						
					},
					onLoadError : function(){
						//alert('加载列表数据异常');
					},
					loadFilter:_myLoadFilter,
					onResizeColumn : function(field, width){
						$('tr.datagrid-row').find('td[field="' + field + '"]').find('div').each(function(){
							renderFieldTip(this);
						});
					},
					onLoadSuccess:function(){
						var tip = true;
						var columnFields = $(this).datagrid('getColumnFields');
						for(var i = 0 ; i < columnFields.length; i++){
							var field = columnFields[i];
							var columnOptions = $(this).datagrid('getColumnOption',field);
							var showTip = columnOptions['showTip'];
							if(showTip==false){
								tip = false;
							}else{
								tip = true;
							}
							if(tip){//需添加tip提示
								$('tr.datagrid-row').find('td[field="' + field + '"]').find('div').each(function(){
									renderFieldTip(this);
								});
							}
						}
					}
	}
	return $.extend(_default,config);	
}

//datagrid 返回的原始数据转换
function _myLoadFilter(data, parent){
	var newobj = {"success": true,"total":10,"pageSize":[]};
	if (data.data && data.success && data.data.list){
		newobj.pageSize = data.data.list;
		newobj.total = data.total;
	}else{
		//alert('加载列表数据出错');
	}
	
	var pageSize = newobj.pageSize;
	if (null == pageSize || pageSize.length == 0)return newobj;
	//处理转义
	if(jQuery.isArray(pageSize)){
		jQuery.each(pageSize, function(i, one){
			jQuery.each(one,function(key,val){
				if(typeof one[key] == 'string'){
					one[key] = _transfer(one[key]);
				}
		    });
		});
	}
	
	
	//以下扩展了二级元素获取
	var columnFields = $(this).datagrid('getColumnFields');
	var _columns = [];
	for (var i = 0 ; i < columnFields.length; i++){
		var columnOptions = $(this).datagrid('getColumnOption',columnFields[i]);
		var field = columnOptions['field'];
		if (field.indexOf('.')  > 0){
			_columns.push(field);
		}
	}
	if (_columns.length > 0){
		for (var i = 0; i < _columns.length; i++){
			for (var j =0 ; j < pageSize.length;j++){
				pageSize[j][_columns[i]] = $.str2Value(pageSize[j],_columns[i]);
			}
		}
	}
	newobj.pageSize = pageSize;
	return newobj;
}

//仅仅展示时转换左右尖括号，提交数据时转化须谨慎
var _transfer = function(dataStr){
	return dataStr.replace(/</g, '＜').replace(/>/g, '＞');
}; 
	
function renderFieldTip(_this){
	if (!$(_this).data('txt')){
		$(_this).data('txt',$(_this).html());
	}
	var dhtml = $(_this).data('txt');
	$(_this).html(dhtml);
	var dtext = $(_this).text();
	var fontSize = $(_this).css("font-size").replace("px","");
    var csize = parseInt(fontSize) / 2;
    var divlen = $(_this).width();
    var txtlen = getCharLength(dtext);
    $(_this).unbind('mouseover').unbind('mouseout');
    if((txtlen*csize)>divlen){
         //超长标题以...结尾
         var len = Math.floor(divlen/csize) - 6;
		 var shortHtml = '' ;
		 //debugger;
		 if($(_this).find('[word-break=break-all]').length+0 > 0){
			shortHtml = getSubStr_(dtext,fontSize,divlen);
		 }else{
			shortHtml = getSubStr(dtext,fontSize,divlen);
		 }
		 var _span = $('<span title="'+$(_this).text()+'"/>').html($(_this).html().replace(dtext,shortHtml));
         $(_this).html(_span);
    }else{
		var _span = $('<span title="'+$(_this).text()+'"/>').html($(_this).html());
        $(_this).html(_span);
	}
}


/**
 * 为url组装指定参数
 */
function addUrlParam(url, name, value) {
		var newUrl = "";
		if(!url){
			return newUrl;
		}
		var reg = new RegExp("(^|)" + name + "=([^&]*)(|$)");
		var tmp = name + "=" + value;
				
		if (url.match(reg) != null) {
			newUrl = url.replace(eval(reg), tmp);
		} else {
			if (url.match("[\?]")) {
				newUrl = url + "&" + tmp;
			} else {
				newUrl = url + "?" + tmp;
			}
		}
		
		return newUrl;
	}

/**
 * 创建一个html富文本组件
 * @param id: textarea的id
 * @param htmlContent: 组件显示的内容
 * @param kwidth: 组件宽度 px
 * @param kheight: 组件高度 px
 * @param editable: 是否可编辑 true 可编辑;false 不可编辑
 * @returns {undefined|*|KEditor|KEditor}
 */
function createKindEditor(id, htmlContent, kwidth, kheight, editable){
    var items = [];
    if (editable) {
        items = ['selectall', 'cut', 'copy', 'paste',
            'plainpaste', 'wordpaste',
            '|', 'justifyleft', 'justifycenter', 'justifyright','justifyfull', 'insertorderedlist',
            'insertunorderedlist', 'indent', 'outdent', 'subscript','superscript',
            '|', '-','formatblock', 'fontname', 'fontsize',
            '|', 'forecolor', 'hilitecolor', 'bold','italic', 'underline', 'strikethrough', 'lineheight','removeformat', 'clearhtml',
            '|', 'table','hr', 'link', 'unlink',
            '|', 'undo', 'redo',
            '/', 'fullscreen', 'print', 'preview'];
    }
    var editor = KindEditor.create('#'+id, {
        items: items,
        width:kwidth,
        height:kheight,
        readonlyMode: !editable,
        allowImageUpload:false, //禁用图片上传功能
        resizeType:1, //2:可以拖动改变宽度和高度，1:只能改变高度，0:不能拖动
        afterCreate : function(){
            this.sync();//同步数据到textarea
        },
        afterBlur:function(){
            this.sync();//同步数据到textarea
            if(window.checkKindEditor) {
                checkKindEditor();
            }
        }
    });
    editor.html(htmlContent);
    //editor.focus();
    return editor;
}

var PARAMTER_VALUE = null;
/**
 * @namespace 获取url参数，参数做缓存,获取多个参数时性能较优
 * @param paramName 参数名
 * @returns
 */
function getParamter(paramName) {

    if(!PARAMTER_VALUE) {   //第一次初始化
        PARAMTER_VALUE = new Array();
        var paramStr = location.search.substring(1);
        var paramArr = paramStr.split("&");
        var len = paramArr.length;
        var tempArr;
        for(var i = 0; i < len; i++) {
            tempArr = paramArr[i].split("=");
            PARAMTER_VALUE[tempArr[0]] = tempArr[1];
        }
    }
    var paramValue = PARAMTER_VALUE[paramName];

    if(paramValue) {
        return paramValue;
    }else{
        return "";
    }
}

/**
 * Validform初始化默认配置
 * 
 */
var getValidformDefaultConfig = function(config) {
	var _default = {
		tiptype: 3,
		ajaxPost: false,
		showAllError:true,
		//dragonfly:true,
		datatype: { //传入自定义datatype类型，可以是正则，也可以是函数（函数内会传入一个参数）;
			"zh1-6" : /^[\u4E00-\u9FA5\uf900-\ufa2d]{1,6}$/,
			"zh1-25" : /^[\u4E00-\u9FA5\uf900-\ufa2d]{1,25}$/,
			"s1-6":/^[\u4E00-\u9FA5\uf900-\ufa2d\w\.\s]{1,6}$/,
			"cm_mobile1":/^13[0-9]{9}$|14[0-9]{9}|17[0-9]{9}|15[0-9]{9}$|18[0-9]{9}$/,
			"cm_mobile" : function(gets, obj, curform, regxp){
				if (!gets) {
					return false;
				}
				var mobile = null;
				$.ajax({  
			        type:'GET',  
			        url:'qm/qmConfigValue.action',  
			        data:{'configName':'QM_VALID_CM_MOBILE'}, 
			        dataType:'json',  
			        cache : false,  
			        async : false,  
			        success:function(data){ 
			        	var configValue = data.data.configValue;
			        	if(configValue!=null && configValue!=''){
			        		mobile = eval(data.data.configValue);
			        	}
			        },  
			        error : function(error) {  
			        	mobile = null;
			        }  
			    });
				if(mobile==null){
					return "读取配置出错";
				}
				if(!mobile.test(gets)){
					return false;
				}
				return true;
	        },
			"email" : function(gets, obj, curform, regxp) {
				if (!gets) {
					return false;
				}
				var email = /^(-|\.|\w)+\@((-|\w)+\.)+[A-Za-z]{2,}$/;
				if(email.test(gets)){
					if(gets.length > 64){
						return "邮件地址长度不能超过64个字符";
					}
				}else{
					return "邮箱地址格式不对";
				}
				return true;
			},
			"specialCharAndPunc" : function(gets, obj, curform, regxp) {
				if (!gets) {
					return false;
				}
				var patternNum = /^\d{1,}$/;
				var pattern = /^[\u4E00-\u9FA5\uf900-\ufa2d\w\s\d]{1,100}$/;
				if(pattern.test(gets)){
					if(gets.length > 100){
						return "不能超过100个字符或者50个汉字";
					}
				}else{
					return "不能包含特殊字符,标点符号";
				}
				return true;
			},
			"s1-100":function(gets, obj, curform, regxp){
				if (!gets) {
					return false;
				}
				var sl00 = /^[\u4E00-\u9FA5\uf900-\ufa2d\w\.\s]{1,100}$/;
				if(!sl00.test(gets)){
					return "请输入100字符以内的中文或英文";
				}
				return true;
			},
			"s10-4000":function(gets, obj, curform, regxp){
				if (!gets) {
					return false;
				}
				var len = 0,_len = 4000;
				if (gets){
					len = gets.replace(/[^\x00-\xff]/g, '..').length;
				}
				if (len < 20 || len > _len){
					return false;
				}
				return true;
			},
			"validateMaxLength" :function(gets, obj, curform, regxp) {
				if (!gets) {
					return false;
				}
				var len = 0,_len = obj.attr('maxlength');
				if (gets){
					//把换行和中文替换成..
					len = obj.val().replace(/\r|\n|(\r\n)|[^\x00-\xff]/g, '..').length;
				}
				if (_len && len > _len){
					return '长度不能超过' + _len + '个字符或'+ _len/2 + '个汉字';
				}
				return true;
			},
			"validateMaxLength_share" :function(gets, obj, curform, regxp) {
				var len = 0,_len = obj.attr('maxlength');
				if (gets){
					//把换行和中文替换成..
					len = obj.val().replace(/\r|\n|(\r\n)|[^\x00-\xff]/g, '..').length;
				}
				if (_len && len > _len){
					return '长度不能超过' + _len + '个字符或'+ _len/2 + '个汉字';
				}
				return true;
			},
			"startTimeValid": function(gets, obj, curform, regxp) {
				//参数gets是获取到的表单元素值，obj为当前表单元素，curform为当前验证的表单，regxp为内置的一些正则表达式的引用;
				if (!gets) {
					return false;
				}
				var from = obj,
					to = $('#' + obj.attr('endtimeflag'));
				if (from.val() != '' && to.val() != '' && !isEndTimeGtStartTime(from.val(), to.val())) {
					return false;
				}
				return true;
				//注意return可以返回true 或 false 或 字符串文字，true表示验证通过，返回字符串表示验证失败，字符串作为错误提示显示，返回false则用errmsg或默认的错误提示;
			},
			"endTimeValid": function(gets, obj, curform, regxp) {
				//参数gets是获取到的表单元素值，obj为当前表单元素，curform为当前验证的表单，regxp为内置的一些正则表达式的引用;
				if (!gets) {
					return false;
				}
				var st = obj.attr('starttimeflag');
				if (st) {
					var from = $('#' + st),
						to = obj;
					if (from.val() != '' && to.val() != '' && !isEndTimeGtStartTime(from.val(), to.val())) {
						return false;
					}
				}
				return true;
				//注意return可以返回true 或 false 或 字符串文字，true表示验证通过，返回字符串表示验证失败，字符串作为错误提示显示，返回false则用errmsg或默认的错误提示;
			},
			"endTimeValid2": function(gets, obj, curform, regxp) {
				//参数gets是获取到的表单元素值，obj为当前表单元素，curform为当前验证的表单，regxp为内置的一些正则表达式的引用;
				if (!gets) {
					return false;
				}
				var st = obj.attr('starttimeflag');
				if (st) {
					var from = $('#' + st),
						to = obj;
					if (!from.val()){
						return '开始时间不能为空';
					}
					if (from.val() != '' && to.val() != '' && !isEndTimeGtStartTime(from.val(), to.val())) {
						return false;
					}
				}
				return true;
				//注意return可以返回true 或 false 或 字符串文字，true表示验证通过，返回字符串表示验证失败，字符串作为错误提示显示，返回false则用errmsg或默认的错误提示;
			},
			"endTimeValid3": function(gets, obj, curform, regxp) {
				//参数gets是获取到的表单元素值，obj为当前表单元素，curform为当前验证的表单，regxp为内置的一些正则表达式的引用;
				if (!gets) {
					return false;
				}
				var st = obj.attr('starttimeflag');
				if (st) {
					var from = $('#' + st),
						to = obj;
					
					if (from.val() != '' && to.val() != '' && !isEndTimeGtStartTime(from.val(), to.val())) {
						return false;
					}
				}
				return true;
				//注意return可以返回true 或 false 或 字符串文字，true表示验证通过，返回字符串表示验证失败，字符串作为错误提示显示，返回false则用errmsg或默认的错误提示;
			},
			"validProductName": function(gets, obj, curform, regxp) {
				//参数gets是获取到的表单元素值，obj为当前表单元素，curform为当前验证的表单，regxp为内置的一些正则表达式的引用;
				if (obj.attr('pid') && obj.attr('pid') != '') {
					return true;
				}
				return false;
			},
			"validReportScore":function(gets, obj, curform, regxp){
				//月报审核打分校验，需根据审核结果来定，同意则打分，不同意不打分
				var opn = obj.attr("refer");
				if(opn=='201'){
					if(!gets){
						return false;
					}
				}
			},
			"validReplyLimitDate":function(gets, obj, curform, regxp){
				//公告截止回复时间校验
				var opn = obj.attr("refer");
				if(opn=='Y'){
					if(!gets){
						return false;
					}
				}
			},
			"checkMonthValid": function(gets, obj, curform, regxp) {
				//参数gets是获取到的表单元素值，obj为当前表单元素，curform为当前验证的表单，regxp为内置的一些正则表达式的引用;
				if (!gets) {
					return false;
				}
				var date = new Date();
				var year = date.getFullYear();
				var mon = date.getMonth()+1;
				var mstr = mon;
				if(mon<10){
					mstr = "0"+mon;
				}
				var vle = year+mstr;
				if(parseInt(gets)>parseInt(vle)){
					return false;
				}
				return true;
			},
			"checkNumber": function(gets, obj, curform, regxp){
				if (!gets) {
					return false;
				}
				var pattern=/^(([0-9]\d*(\.\d?[0-9])?)|(0\.[1-9][0-9])|(0\.[0][1-9]))$/;
				var len = 0;
				var maxNum = parseInt(obj.attr('maxNum'));
				var minNum = parseInt(obj.attr('minNum'));
				if(!pattern.test(gets)){
					if(!isNaN(parseInt(minNum))){
						return "请输入"+ minNum + "到" + maxNum +"的数字，最多保留2位小数";
					}else{
						return "请输入不超过"+ maxNum +"的分数，最多保留2位小数";
					}					
				}else{
					if(!isNaN(parseInt(minNum))){
						if(gets < minNum || gets > maxNum){
							return "请输入"+ minNum + "到" + maxNum +"的数字，最多保留2位小数";
						}
					}else{
						if(gets < 0.01 || gets > maxNum){
							return "请输入不超过"+ maxNum +"的分数，最多保留2位小数";
						}
					}					
				}
				return true;
			},
			"checkNullNumber": function(gets, obj, curform, regxp){
				if (!gets) {
					//可以为空
					return true;
				}
				var pattern=/^(([0-9]\d*(\.\d?[0-9])?)|(0\.[1-9][0-9])|(0\.[0][1-9]))$/;
				var len = 0;
				var maxNum = parseInt(obj.attr('maxNum'));
				var minNum = parseInt(obj.attr('minNum'));
				if(!pattern.test(gets)){
					if(!isNaN(parseInt(minNum))){
						return "请输入"+ minNum + "到" + maxNum +"的数字，最多保留2位小数";
					}else{
						return "请输入不超过"+ maxNum +"的分数，最多保留2位小数";
					}					
				}else{
					if(!isNaN(parseInt(minNum))){
						if(gets < minNum || gets > maxNum){
							return "请输入"+ minNum + "到" + maxNum +"的数字，最多保留2位小数";
						}
					}else{
						if(gets < 0.01 || gets > maxNum){
							return "请输入不超过"+ maxNum +"的分数，最多保留2位小数";
						}
					}					
				}
				return true;
			},
			"checkNumberCompare": function(gets, obj, curform, regxp){
				if (!gets) {
					//可以为空
					return true;
				}
				var pattern=/^(\-?)([0-9]{1,}[.][0-9]*)$/;
				var len = 0;
				var maxNum1 = $('#'+obj.attr('maxNumId')).val();
				var minNum1 = $('#'+obj.attr('minNumId')).val();
/*				var maxNum2 = parseInt($(obj.attr('maxNum')).val());
				var minNum2 = parseInt($(obj.attr('minNum')).val());*/
				var maxNum;
				var minNum; 
				if(maxNum1){
					maxNum = maxNum1;
				}else{
					maxNum = parseFloat('');
				}
				if(minNum1){
					minNum = minNum1;
				}else{
					minNum = parseFloat('');
				}
				if(!pattern.test(gets)){
					/*if(!isNaN(parseInt(minNum))){
						return "请大于"+ minNum;;
					}else{
						return "请小于"+ maxNum;
					}*/			
					return "请输入小数！";
				}else{
					if(!isNaN(parseFloat(minNum))){
						if(parseFloat(gets) < parseFloat(minNum) || gets > maxNum){
							return "请大于"+ minNum;
						}
					}else{
						if(parseFloat(gets) > parseFloat(maxNum)){
							return "请小于"+ maxNum ;
						}
					}					
				}
				
				return true;
			},
			"checkIntCompare": function(gets, obj, curform, regxp){
				if (!gets) {
					//可以为空
					return true;
				}
				var pattern=/^(\-?)\d{1,}$/;
				var len = 0;
				var maxNum1 = $('#'+obj.attr('maxNumId')).val();
				var minNum1 = $('#'+obj.attr('minNumId')).val();
/*				var maxNum2 = parseInt($(obj.attr('maxNum')).val());
				var minNum2 = parseInt($(obj.attr('minNum')).val());*/
				var maxNum;
				var minNum; 
				if(maxNum1){
					maxNum = maxNum1;
				}else{
					maxNum = parseInt('');
				}
				if(minNum1){
					minNum = minNum1;
				}else{
					minNum = parseInt('');
				}
				if(!pattern.test(gets)){
					/*if(!isNaN(parseInt(minNum))){
						return "请大于"+ minNum;;
					}else{
						return "请小于"+ maxNum;
					}*/		
					return "请输入整数！";
				}else{
					if(!isNaN(parseInt(minNum))){
						if(parseInt(gets) < parseInt(minNum) || gets > maxNum){
							return "请大于"+ minNum;
						}
					}else{
						if(parseInt(gets) > parseInt(maxNum)){
							return "请小于"+ maxNum ;
						}
					}					
				}
				
				return true;
			},
			"checkDecimal":function(gets, obj, curform, regxp){
				if (!gets) {
					return false;
				}
				var dn = parseInt(obj.attr('dn'));
				var pattern=/^\d+(.[0-9]{0,9})?$/;
				if(!pattern.test(gets)){
					return "请输入正确数字";
				}else{
					if(gets.indexOf(".")>0){
						var aft = gets.split(".")[1];
						if(aft.length>parseInt(dn)){
							return "最多保留"+dn+"位小数";
						}
					}
				}
				return true;
			},
			"checkProductManager":function(gets, obj, curform, regxp){
				//检查产品在支撑公司是否配置产品经理
				if (!gets) {
					return false;
				}
				var ignore = obj.attr("ignore");
				var errormsg = obj.attr("errormsg");
				var supporterType = getParamter("supporterType");
				if(supporterType==null || supporterType==''){
					supporterType = getParamter("roleType");
				}
				
				if(ignore=='ignore'){
					return true;
				}
				var count = 0;
				$.ajax({  
			        type:'GET',  
			        url:'qm/product/product!countProductManager.action',  
			        data:{'subProduct.productId':gets,'supporterType':supporterType}, 
			        dataType:'json',  
			        cache : false,  
			        async : false,  
			        success:function(data){  
			        	count = data.data.count;//产品经理数量
			        },  
			        error : function(error) {  
			        	count = -1;
			        }  
			    });
				if(count<=0){
					return errormsg;
				}
				return true;
			},
			"checkInputNum":function(gets, obj, curform, regxp){
				if (!gets) {
					return false;
				}
				var maxNum = obj.attr("maxNum");
				if(maxNum==''){
					maxNum = "10";
				}
				var pattern=/^\d{1,}$/;
				if(!pattern.test(gets)){
					return "请输入数字";
				}
				if(parseInt(gets)>parseInt(maxNum)){
					return "请输入不大于"+maxNum+"的数字";
				}
				return true;
			},
			"checkIntNum":function(gets, obj, curform, regxp){
				if (!gets) {
					//可以为空
					return true;
				}
				var maxNum = obj.attr("maxNum");
				var minNum = obj.attr("minNum");
				var pattern=/^\d+$/;
				if(!pattern.test(gets)){
					return "请输入正确数字";
				}
				if(parseInt(gets)<parseInt(minNum)){
					return "请输入不小于"+minNum+"的数字";
				}
				if(parseInt(gets)>parseInt(maxNum)){
					return "请输入不大于"+maxNum+"的数字";
				}
				return true;
			},
			"checkDoubleNum":function(gets, obj, curform, regxp){
				if (!gets) {
					//可以为空
					return true;
				}
				var maxNum = obj.attr("maxNum");
				var minNum = obj.attr("minNum");
				var pattern=/^([0-9]{1,}[.][0-9]*)$/;
				if(!pattern.test(gets)){
					return "请输入小数";
				}
				if(parseInt(gets)<parseInt(minNum)){
					return "请输入不小于"+minNum+"的小数";
				}
				if(parseInt(gets)>parseInt(maxNum)){
					return "请输入不大于"+maxNum+"的小数";
				}
				return true;
			},
			"validateMaxLengthNew" :function(gets, obj, curform, regxp) {
				if (!gets) {
					return true;
				}
				var len = 0,_len = obj.attr('maxlength');
				if (gets){
					//把换行和中文替换成..
					len = obj.val().replace(/\r|\n|(\r\n)|[^\x00-\xff]/g, '..').length;
				}
				if (_len && len > _len){
					return '长度不能超过' + _len + '个字符或'+ _len/2 + '个汉字';
				}
				return true;
			}
		}
	}
	return typeof config == 'object' ? $.extend(_default, config) : _default;
}
var Validform=function(forms,settings,inited){
	var settings=$.extend({},Validform.defaults,settings);
	settings.datatype && $.extend(Validform.util.dataType,settings.datatype);
	
	var brothers=this;
	brothers.tipmsg={w:{}};
	brothers.forms=forms;
	brothers.objects=[];
	
	//创建子对象时不再绑定事件;
	if(inited===true){
		return false;
	}
	
	forms.each(function(){
		//已经绑定事件时跳过，避免事件重复绑定;
		if(this.validform_inited=="inited"){return true;}
		this.validform_inited="inited";
		
		var curform=this;
		curform.settings=$.extend({},settings);
		
		var $this=$(curform);
		
		//防止表单按钮双击提交两次;
		curform.validform_status="normal"; //normal | posting | posted;
		
		//让每个Validform对象都能自定义tipmsg;	
		$this.data("tipmsg",brothers.tipmsg);

		//bind the blur event;
		$this.delegate("[datatype]","blur",function(){
			//判断是否是在提交表单操作时触发的验证请求；
			var subpost=arguments[1];
			Validform.util.check.call(this,$this,subpost);
		});
		
		$this.delegate(":text","keypress",function(event){
			if(event.keyCode==13 && $this.find(":submit").length==0){
				$this.submit();
			}
		});
		
		//点击表单元素，默认文字消失效果;
		//表单元素值比较时的信息提示增强;
		//radio、checkbox提示信息增强;
		//外调插件初始化;
		Validform.util.enhance.call($this,curform.settings.tiptype,curform.settings.usePlugin,curform.settings.tipSweep);
		
		curform.settings.btnSubmit && $this.find(curform.settings.btnSubmit).bind("click",function(){
			$this.trigger("submit");
			return false;
		});
					
		$this.submit(function(){
			var subflag=Validform.util.submitForm.call($this,curform.settings);
			subflag === undef && (subflag=true);
			return subflag;
		});
		
		$this.find("[type='reset']").add($this.find(curform.settings.btnReset)).bind("click",function(){
			Validform.util.resetForm.call($this);
		});
		
	});
	
	//预创建pop box;
	if( settings.tiptype==1 || (settings.tiptype==2 || settings.tiptype==3) && settings.ajaxPost ){		
		creatMsgbox();
	}
}
function isSupportPlaceholder(){
	return 'placeholder' in document.createElement('input') && !window.navigator.userAgent.indexOf("MSIE")>=1;
}
var clearPlaceholder = function(){
	if (isSupportPlaceholder())return;
	$("textarea, input[type='text']").each(function(index, element){
		$(element).trigger("parentformsubmitted");
	});
}

function closeWin() {
	if(isWorkBench){
		window.history.go(-1);
	}else{
		if (navigator.userAgent.indexOf("MSIE") > 0) {
			if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
				window.opener = null;
				window.close();
			} else {
				window.open('', '_top');
				window.top.close();
			}
		} else if (navigator.userAgent.indexOf("Firefox") > 0) {
			window.opener = null;
			window.open('', '_self', '');
			window.close();
			// window.history.go(-2);
		} else {
			window.opener = null;
			window.open('', '_self', '');
			window.close();
		}
	}
}


var upload = layui.upload; //上传组件
var layer = layui.layer;
var form = layui.form;
var showSearchCaseDiv_index;
var editor = "";

/**
 * 根据数据库配置,初始化公用附件上传组件,适用于新增和修改页面场景
 * @param upLoadButtonText  自定义附件按钮名字，默认为'上传附件'，其它为自定义名称
 * @param attachTypeId 数据库配置的attachmentTypeId
 * @param uploadButtonId 上传按钮ID
 * @param attachGroupIdName attachGroupId表单后台接收字段
 * @param attachFileIdsName attachFileId表单后台接收字段,多个id使用逗号分隔
 * @param existFilesArray 已上传文件列表数组(没有时,请设置为null),必须包含 fileName, attachGroupId, attachFileId 属性,
 * 如 [{"fileName": "", "attachGroupId": "", "attachFileId": ""}...]
 *
 * example: common_attachment_upload_init(NULL,'NOTICE_ID', 'upfile', 'attachGroupId', 'attachFileIds', null);
 */
function common_attachment_upload_init(upLoadButtonText, attachTypeId, uploadButtonId, attachGroupIdName, attachFileIdsName, existFilesArray){

    // 获取数据库配置的 attachment_type 信息
    var viewTypeUrl = appendExtraParams(window.location.protocol + "//" + window.location.host +
        "/support/attachment/viewType.ajax?attachTypeId=" + attachTypeId);

    $.ajax({
        type: 'get',
        url: viewTypeUrl,
        cache: false,
        data: {},
        dataType: 'json',
        success: function (res) {
            if (res && res.result && res.result.resultCode == "1") {
                var data = res.data;
                var accept = 'file';
                var size = data.singleSizeLimit / 1024;
                var number = data.attachCountLimit || 1;
                var maxSize = data.attachSizeLimit / 1024;
                var acceptMime = '';
                if (data.fileSuffixLimit){
                    var fileSuffix = data.fileSuffixLimit;
                    acceptMime = "." + fileSuffix.replace(/;/g, ",.");
                }
                common_attachment_upload_modify(upLoadButtonText, attachTypeId, uploadButtonId, attachGroupIdName, attachFileIdsName,
                    accept, acceptMime, size, number, maxSize, existFilesArray);
            }else{
                layer.msg(res.result.resultDes);
            }

        },
        error: function () {
            layer.msg("获取数据库配置的 attachment_type 信息失败!");
        }
    });
}


/**
 * 公用附件上传组件,适用于新增页面场景
 * @param attachTypeId 数据库配置的attachmentTypeId
 * @param uploadButtonId 上传按钮ID
 * @param attachGroupIdName attachGroupId表单后台接收字段
 * @param attachFileIdsName attachFileId表单后台接收字段,多个id使用逗号分隔
 * @param accept 指定允许上传时校验的文件类型，可选值有：images（图片）、file（所有文件）、video（视频）、audio（音频）
 * @param acceptMime 规定打开文件选择框时，筛选出的文件类型，值为用逗号隔开的 MIME 类型列表。如：
 *        acceptMime: 'image/*'（只显示图片文件）
 *        acceptMime: 'image/jpg, image/png'（只显示 jpg 和 png 文件）
 * @param maxSize 设置同时可上传的文件总大小.
 * @param size 设置文件最大可允许上传的大小，单位 KB。不支持ie8/9
 * @param number 设置同时可上传的文件数量.
 */
function common_attachment_upload_add(attachTypeId, uploadButtonId, attachGroupIdName, attachFileIdsName, accept, acceptMime, size, maxSize, number){
    var currentNumber = 0;

    var originalCss = $('#' + uploadButtonId).css('display');

    var attachGroupIdTmp_name = uploadButtonId + "_attachGroupIdTmp";
    var attachFileIds_name = uploadButtonId + "_attachFileIds";
    var attachmentShowDiv_name = uploadButtonId + "_attachmentShowDiv";

    // 附件组Id隐藏域
    $('#' + uploadButtonId).after('<input type="hidden" id="' + attachGroupIdTmp_name + '" name="' + attachGroupIdName + '">');
    // 已上传文件Id隐藏域,逗号分隔
    $('#' + uploadButtonId).after('<input type="hidden" id="' + attachFileIds_name + '" name="' + attachFileIdsName + '">');
    // 附件显示区域
    $('#' + uploadButtonId).after('<div id="' + attachmentShowDiv_name + '"></div>');

    // 按钮动态文字
    // $('#' + uploadButtonId).text("上传附件(总大小" + (maxSize/1024).toFixed(0) + "M,最多" + number + "个)");
    $('#' + uploadButtonId).text("上传附件(最大" + (maxSize/1024).toFixed(0) + "M)");

    upload.render({
        elem: '#' + uploadButtonId, //绑定元素
        url: appendExtraParams(window.location.protocol + "//" + window.location.host + "/support/attachment/addForMulti.ajax"),   //上传接口
        accept: accept,
        acceptMime: acceptMime,
        size: size,
        data: {
            'attachTypeId': attachTypeId,
            'attachGroupId': function () {
                return $('#' + attachGroupIdTmp_name).val();
            },
            'attachFileIds': function () {
                return $('#' + attachFileIds_name).val();
            }
        },
        multiple: false,
        number: number,
        auto: true,
        before: function (obj) {
            layer.load(1); //上传loading
        },
        done: function (res) {
            layer.closeAll('loading'); //关闭loading
            if (res && res.result && res.result.resultCode == "1") {

                if (res.data) {
                    var data = res.data;
                    var attachment_download_url = (window.location.protocol + "//" + window.location.host +
                        "/support/attachmentFile/downLoad.ajax?attachFileId=") + data.attachFileId;

                    $('#' + attachmentShowDiv_name).append('<div class="cm_upload_div">\n' +
                        '    <div class="cm_upload_content">\n' +
                        '        <div class="cm_upload_file">\n' +
                        '            <a target="excel" class="cm_upload_href" href="' + attachment_download_url +'" title="' + data.fileName + '">' + data.fileName + '</a>\n' +
                        '        </div>\n' +
                        '    </div>\n' +
                        '    <div class="cm_close_item">\n' +
                        '        <a href="javascript:;" fileId="' + data.attachFileId + '" groupId="' + data.attachGroupId + '"></a>\n' +
                        '    </div>\n' +
                        '</div>');

                    // 附件组Id
                    $('#' + attachGroupIdTmp_name).val(data.attachGroupId);

                    // 已上传文件列表
                    var fileList = $('#' + attachFileIds_name).val();
                    if (!fileList){
                        fileList += data.attachFileId;
                    }else{
                        fileList += ',' + data.attachFileId;
                    }
                    $('#' + attachFileIds_name).val(fileList);

                    currentNumber++;
                    enabledDisabledButton(uploadButtonId, currentNumber, number, originalCss);

                    $('#' + attachmentShowDiv_name + ' .cm_upload_div').find('.cm_close_item').unbind().bind("click",function() {
                        var p = $(this).parent('div');
                        var o = $(this).find('a');
                        var withdrawUrl = appendExtraParams(window.location.protocol + "//" + window.location.host +
                            "/support/attachment/withdraw.ajax");

                        $.ajax({
                            type: 'get',
                            url: withdrawUrl,
                            cache: false,
                            data: {'attachFileId': o.attr('fileId'), 'attachGroupId': o.attr('groupId')},
                            dataType: 'json',
                            success: function (result) {
                                p.remove();

                                // 删除已上传文件列表中的项
                                fileList = $('#' + attachFileIds_name).val();

                                $('#' + attachFileIds_name).val(upload_string_replace(fileList, o.attr('fileId')));

                                currentNumber--;
                                enabledDisabledButton(uploadButtonId, currentNumber, number, originalCss);
                            },
                            error: function () {
                                layer.msg("删除附件失败");
                            }
                        });
                    });
                }

                layer.msg(res.result.resultDes);
            } else {
                layer.msg(res.result.resultDes);
            }
        },
        error: function () {
            layer.closeAll('loading');
            layer.msg("附件上传出错!");
        }
    });
}

/**
 * 公用附件上传组件,适用于修改页面场景(展示已上传文件列表,初始化上传按钮)
 * @param upLoadButtonText  自定义附件按钮名字，默认为'上传附件'，其它为自定义名称
 * @param attachTypeId 数据库配置的attachmentTypeId
 * @param uploadButtonId 上传按钮ID
 * @param attachGroupIdName attachGroupId表单后台接收字段
 * @param attachFileIdsName attachFileId表单后台接收字段,多个id使用逗号分隔
 * @param accept 指定允许上传时校验的文件类型，可选值有：images（图片）、file（所有文件）、video（视频）、audio（音频）
 * @param acceptMime 规定打开文件选择框时，筛选出的文件类型，值为用逗号隔开的 MIME 类型列表。如：
 *        acceptMime: 'image/*'（只显示图片文件）
 *        acceptMime: 'image/jpg, image/png'（只显示 jpg 和 png 文件）
 * @param size 设置文件最大可允许上传的大小，单位 KB。不支持ie8/9
 * @param number 设置同时可上传的文件数量.
 * @param maxSize 设置同时可上传的文件总大小.
 * @param existFilesArray 已上传文件列表数组,必须包含 fileName, attachGroupId, attachFileId 属性,
 * 如 [{"fileName": "", "attachGroupId": "", "attachFileId": ""}...]
 */
function common_attachment_upload_modify(upLoadButtonText, attachTypeId, uploadButtonId, attachGroupIdName, attachFileIdsName, accept, acceptMime, size, number, maxSize, existFilesArray){

    var currentNumber = 0;

    var originalCss = $('#' + uploadButtonId).css('display');

    var attachment_download_url = window.location.protocol + "//" + window.location.host +
        "/support/attachmentFile/downLoad.ajax?attachFileId=";

    var attachGroupIdTmp_name = uploadButtonId + "_attachGroupIdTmp";
    var attachFileIds_name = uploadButtonId + "_attachFileIds";
    var attachmentShowDiv_name = uploadButtonId + "_attachmentShowDiv";

    // 附件组Id隐藏域
    $('#' + uploadButtonId).after('<input type="hidden" id="' + attachGroupIdTmp_name + '" name="' + attachGroupIdName + '">');
    // 已上传文件Id隐藏域,逗号分隔
    $('#' + uploadButtonId).after('<input type="hidden" id="' + attachFileIds_name + '" name="' + attachFileIdsName + '">');
    // 附件显示区域
    $('#' + uploadButtonId).after('<div id="' + attachmentShowDiv_name + '"></div>');

    // 按钮动态文字
    // $('#' + uploadButtonId).text("上传附件(总大小" + (maxSize/1024).toFixed(0) + "M,最多" + number + "个)");
    if(upLoadButtonText == null || upLoadButtonText == undefined || upLoadButtonText == ''){
    	upLoadButtonText = '上传附件';
    }
    $('#' + uploadButtonId).text(upLoadButtonText + "(最大" + (maxSize/1024).toFixed(0) + "M)");

    // 已上传文件列表
    var fileList = $('#' + attachFileIds_name).val();
    
    if(existFilesArray && (existFilesArray.length > 0)){
        for (var i = 0; i < existFilesArray.length; i++) {

            $('#' + attachGroupIdTmp_name).val(existFilesArray[i].attachGroupId);

            if($('#' + attachFileIds_name).val()){
                $('#' + attachFileIds_name).val($('#' + attachFileIds_name).val() + "," + existFilesArray[i].attachFileId);
            }else{
                $('#' + attachFileIds_name).val(existFilesArray[i].attachFileId);
            }

            $('#' + attachmentShowDiv_name).append('<div class="cm_upload_div">\n' +
                '    <div class="cm_upload_content">\n' +
                '        <div class="cm_upload_file">\n' +
                '            <a target="excel" class="cm_upload_href" href="' + attachment_download_url + existFilesArray[i].attachFileId + '" title="' + existFilesArray[i].fileName + '">' + existFilesArray[i].fileName + '</a>\n' +
                '        </div>\n' +
                '    </div>\n' +
                '    <div class="cm_close_item">\n' +
                '        <a href="javascript:;" fileId="' + existFilesArray[i].attachFileId + '" groupId="' + existFilesArray[i].attachGroupId + '"></a>\n' +
                '    </div>\n' +
                '</div>');

            currentNumber++;
            enabledDisabledButton(uploadButtonId, currentNumber, number, originalCss);

        }
        
        $('#' + attachmentShowDiv_name + ' .cm_upload_div').find('.cm_close_item').unbind().bind("click",function() {
            var p = $(this).parent('div');
            var o = $(this).find('a');
            var withdrawUrl = appendExtraParams(window.location.protocol + "//" + window.location.host +
                "/support/attachment/withdraw.ajax");

            $.ajax({
                type: 'get',
                url: withdrawUrl,
                cache: false,
                data: {'attachFileId': o.attr('fileId'), 'attachGroupId': o.attr('groupId')},
                dataType: 'json',
                success: function (result) {
                    p.remove();

                    currentNumber--;
                    enabledDisabledButton(uploadButtonId, currentNumber, number, originalCss);

                    // 删除已上传文件列表中的项
                    fileList = $('#' + attachFileIds_name).val();

                    $('#' + attachFileIds_name).val(upload_string_replace(fileList, o.attr('fileId')));

                },
                error: function () {
                    layer.msg("删除附件失败");
                }
            });
        });
    }

    upload.render({
        elem: '#' + uploadButtonId, //绑定元素
        url: appendExtraParams(window.location.protocol + "//" + window.location.host + "/support/attachment/addForMulti.ajax"),   //上传接口
        accept: accept,
        acceptMime: acceptMime,
        size: size,
        data: {
            'attachTypeId': attachTypeId,
            'attachGroupId': function () {
                return $('#' + attachGroupIdTmp_name).val();
            },
            'attachFileIds': function () {
                return $('#' + attachFileIds_name).val();
            }
        },
        multiple: false,
        number: number,
        auto: true,
        before: function (obj) {
            layer.load(1); //上传loading
        },
        done: function (res) {
            layer.closeAll('loading'); //关闭loading
            if (res && res.result && res.result.resultCode == "1") {

                if (res.data) {
                    var data = res.data;

                    var appendHtml ='<div class="cm_upload_div">\n' +
                        '    <div class="cm_upload_content">\n' +
                        '        <div class="cm_upload_file">\n' +
                        '            <a target="excel" class="cm_upload_href" href="' + attachment_download_url + data.attachFileId +'" title="' + data.fileName + '">' + data.fileName + '</a>\n' +
                        '        </div>\n' +
                        '    </div>\n' +
                        '    <div class="cm_close_item">\n' +
                        '        <a href="javascript:;" fileId="' + data.attachFileId + '" groupId="' + data.attachGroupId + '"></a>\n' +
                        '    </div>\n' +
                        '</div>';

                    $('#' + attachmentShowDiv_name).append(appendHtml);

                    // 附件组Id
                    $('#' + attachGroupIdTmp_name).val(data.attachGroupId);

                    fileList = $('#' + attachFileIds_name).val();

                    if (!fileList){
                        fileList += data.attachFileId;
                    }else{
                        fileList += ',' + data.attachFileId;
                    }
                    $('#' + attachFileIds_name).val(fileList);

                    currentNumber++;
                    enabledDisabledButton(uploadButtonId, currentNumber, number, originalCss);

                    $('#' + attachmentShowDiv_name + ' .cm_upload_div').find('.cm_close_item').unbind().bind("click",function() {
                        var p = $(this).parent('div');
                        var o = $(this).find('a');
                        var withdrawUrl = appendExtraParams(window.location.protocol + "//" + window.location.host +
                            "/support/attachment/withdraw.ajax");

                        $.ajax({
                            type: 'get',
                            url: withdrawUrl,
                            cache: false,
                            data: {'attachFileId': o.attr('fileId'), 'attachGroupId': o.attr('groupId')},
                            dataType: 'json',
                            success: function (result) {
                                p.remove();

                                // 删除已上传文件列表中的项
                                fileList = $('#' + attachFileIds_name).val();

                                $('#' + attachFileIds_name).val(upload_string_replace(fileList, o.attr('fileId')));

                                currentNumber--;
                                enabledDisabledButton(uploadButtonId, currentNumber, number, originalCss);

                            },
                            error: function () {
                                layer.msg("删除附件失败");
                            }
                        });
                    });
                }

                layer.msg(res.result.resultDes);
            } else {
                layer.msg(res.result.resultDes);
            }
        },
        error: function () {
            layer.closeAll('loading');
            layer.msg("附件上传出错!");
        }
    });
}

/**
 * 公用附件上传展示已上传文件列表,适用于详情页面场景
 * @param showFilesDivId 已上传文件列表展示div区域id
 * @param existFilesArray 已上传文件列表数组,必须包含 fileName, attachFileId 属性,
 * 如 [{"fileName": "", "attachFileId": ""}...]
 */
function common_attachment_upload_detail(showFilesDivId, existFilesArray){

    var attachment_download_url = window.location.protocol + "//" + window.location.host +
        "/support/attachmentFile/downLoad.ajax?attachFileId=";

    if(existFilesArray && (existFilesArray.length > 0)){
        for (var i = 0; i < existFilesArray.length; i++) {

            $('#' + showFilesDivId).append('<div class="cm_upload_div">\n' +
                '    <div class="cm_upload_content">\n' +
                '        <div class="cm_upload_file">\n' +
                '            <a target="excel" class="cm_upload_href" href="' + attachment_download_url + existFilesArray[i].attachFileId + '" title="' + existFilesArray[i].fileName + '">' + existFilesArray[i].fileName + '</a>\n' +
                '        </div>\n' +
                '    </div>\n' +
                '</div>');
        }
    }

}

function upload_string_replace(targetStr, replaceStr){
    var res = targetStr;
    if (!res){
        return '';
    }else{
        res = res.replace(replaceStr, '').replace(/,{2,}/g, ',');
        if(res == ',' || res == ''){
            return '';
        }
        if (res.indexOf(',') == 0){
            res = res.substr(1);
        }
        if (res.substring(res.length - 1, res.length) === ','){
            res = res.substr(0, res.length - 1);
        }
    }
    return res;
}

function enabledDisabledButton(uploadButtonId, currentNumber, size, originalCss) {
    var disCss = originalCss || 'block';
    if (currentNumber >= size){
        $('#' + uploadButtonId).css('display', 'none');
    }else{
        $('#' + uploadButtonId).css('display', disCss);
    }
}