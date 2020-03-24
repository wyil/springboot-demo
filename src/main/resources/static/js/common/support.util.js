//对数组的奇数项添加换行符
var echarts_xAxis_label_formatter = function(array, formatterMixSize) {
	if (array.length > formatterMixSize) {
		for (var i = 0; i < array.length; i++) {
			if (i % 2 == 1) {
				array[i] = '\n' + array[i];
			}
		}
	}
	return array;
}

/*
 * echarts 图表y轴格式化 formatter scale 单位数量,如 1000, 10000 pointNum 小数点位数, 如 0, 2
 * unit 单位 ,如 '千', '万'
 */
var num_formatter = function(scale, pointNum){
	return function(value){
		if(scale !='-' || scale !='' || scale !=undefined){
			return scale.toFixed(pointNum);
		}
	};
}
/*
 * 
 * scale 单位数量,如 1000.0000, 10000.0002 pointNum 小数点位数, 如 0, 2 return
 * 1000.00,10000.00
 */
var num_decimal_formatter = function(scale, num){
	return function(value){
		if(scale !='-' || scale !='' || scale !=undefined){
			return scale.toFixed(pointNum) + "%";
		}else {
			return '--';
		}
	};
}

/*
 * 数字格式化
 */
var format_number_common = {
	// 数字千分位格式化
	thousands_format : function(num) {
		// isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除
		if (num === "" || num == null) {
			return num;
		}
		if (!isNaN(num)) {
			if (num >= 0) {
				var str = '' + num;
				var index = str.indexOf(".");
				var s1 = str.split('.')[0];
				var re = /(?=(?!(\b))(\d{3})+$)/g;
				s1 = s1.replace(re, ",");
				if (index >= 0) {
					var s2 = str.split('.')[1];
					return s1 + '.' + s2;
				} else {
					return s1;
				}
			} else {
				var str = (0 - num) + '';
				var index = str.indexOf(".");
				var s1 = str.split('.')[0];
				var re = /(?=(?!(\b))(\d{3})+$)/g;
				s1 = s1.replace(re, ",");
				if (index >= 0) {
					var s2 = str.split('.')[1];
					return '-' + s1 + '.' + s2;
				} else {
					return '-' + s1;
				}
			}
		} else {
			return num;
		}
	},
	thousands_format_default_Line : function(num) {
		// isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除
		if (num === "" || num == null) {
			return "--";
		}
		return format_number_common.thousands_format(num); 
	}
};

// 动态绑定属性值到format表单元素
var form_elements_assignment = function(valueObj, suffix){
	var key;
	var value;
	for(var inx in valueObj){
		key = inx;
		value = valueObj[inx];
		if(value instanceof Object ){
			form_elements_assignment(value, suffix);
		}else{
			if($('#'+inx).length > 0){
				if(value && (!isNaN(value))){
					value = value + suffix;
				}else{
					value = '--';
				}
				
				if($('#'+inx)[0].tagName == 'INPUT'){
					$('#'+inx).val(value);
				}
				if($('#'+inx)[0].tagName == 'SPAN'){
					$('#'+inx).text(value);
				}
			}
		}
	}
}

// ajax 文件(excel模板)下载，实际上就是模拟表单提交，代码如下：
function exportExcel(data,url) {
	// url and data options required
	if (data) {
		var inputs = '';
		if(typeof data == 'string'){
			jQuery.each(data.split('&'), function() {
				var pair = this.split('=');
				inputs += '<input type="hidden" name="' + pair[0] + '" value="'
						+ pair[1] + '" />';
			});
		}else{
			/* 遍历对象 */
			$.each(data,function(i,item){
				inputs += '<input type="hidden" name="' + i + '" value="'
				+ item + '" />';
			});
			
		}
		// send request
		jQuery(
				'<form action="' + url + '" method="' + ('post')
						+ '" contentType="application/json; charset=utf-8">' + inputs + '</form>').appendTo('body').submit()
				.remove();
	}
};

function exportExcelGet(data,url) {
	// url and data options required
	if (data) {
		var inputs = '';
		if(typeof data == 'string'){
			jQuery.each(data.split('&'), function() {
				var pair = this.split('=');
				inputs += '<input type="hidden" name="' + pair[0] + '" value="'
						+ pair[1] + '" />';
			});
		}else{
			/* 遍历对象 */
			$.each(data,function(i,item){
				inputs += '<input type="hidden" name="' + i + '" value="'
				+ item + '" />';
			});
			
		}
		// send request
		jQuery(
				'<form action="' + url + '" method="' + ('get')
						+ '" contentType="application/json; charset=utf-8">' + inputs + '</form>').appendTo('body').submit()
				.remove();
	}
};

/**
 * 为form表单添加导出功能
 * 
 * @class 数据导出
 * @memberOf {TypeName}
 */
(function($){
	$.fn.extend({
		exportData:function(setting){
			var id = new Date();
			frame = (
				jQuery("<iframe frameborder='0' width='0' height='0' style='display:none'/>")
				.attr({'id':id,'name':id,src:''})
			).appendTo( document.documentElement );
	        // frame加载后的回调喊出
	        function cb(){
	            var response = ''
		            try { //
	                var doc = frame.contents();
	                if(doc && doc[0].body){// 处理html数据
	                	// var jsonHtml=
						// doc.find('body').html().replaceAll('</generated>','').replaceAll('<pre>','').replaceAll('</pre>','');
	                	var jsonHtml= doc.find('body').text();
	                    response =eval('(' + jsonHtml + ')'); 
	                }
	                if(doc && doc[0].XMLDocument){// 处理xml数据
	                    response = doc[0].XMLDocument;
	                }else {// 其他数据 当作文本来处理
	                    response = eval('(' + doc + ')');
	                }
		            }
	            catch(e) {
	            }
				frame.unbind('load',cb);
				if(setting.callback && typeof setting.callback == "function"){
					setting.callback.call(frame,response,setting);
				}
	            setTimeout(function(){frame.remove();}, 100);
	        }
	        frame.bind('load',cb);
	        // form.submit();
	        // 拼参数
	        var p = setting.data;
            if(typeof p == "object"){
                p = $.param(p);
            }
            if($.ajaxDefaultParams){
                var extras = $.param($.ajaxDefaultParams);
                p = p ? (p + '&' + extras) : extras;
            }
	        // 如果用自己定义的参数就不用从表单中取
	        if(setting.data==null){
	                setting.url = setting.url || $(this).attr('action');
	                var f = $(this).serialize();
	                p = p ? (p + '&' + f) : f;
	         }	        
			// 最终的url
	         setting.url += (setting.url.indexOf('?') != -1 ? '&' : '?') + '_dc=' + (new Date().getTime());
	         setting.url += (setting.url.indexOf('?') != -1 ? '&' : '?') + p;
	         setting.url += (setting.url.indexOf('?') != -1 ? '&' : '?') + "acceptContentType=html";
	        frame.attr('src',setting.url);
		}
		
	})
})(jQuery);


var search_period_year_init = function(domId){
	   var year = new  Date().getFullYear();
	   var month = new  Date().getMonth()+1;
	   if(month < 4){
		   year = year -1;
	   }
	   
	   $("#" + domId).val(year);
       $("#" + domId).unbind("focus");
       $("#" + domId).on("focus",function(dp){  
 	      WdatePicker({  
 	    	  lang:'zh-cn',
 	    	  dateFmt:'yyyy',
 	    	  realDateFmt:'yyyy',
 	    	  // minDate:minMon,
 	    	  // maxDate:maxMon,
 	    	  readOnly:true,
 	    	  isShowClear:false,
 	    	  opposite:false,
 	          // disabledDates:date_Array,//绑定数组
 	          onpicked:function(dp){ // 点击某一日期后触发
 	               // 获取当前选中的日期
 	               var curDate=dp.cal.getNewDateStr();
 	          }  
 	      });  
	    });  
}


// 查询周期初始化-最近一天的日期
var search_period_day_start_init = function(domId){
	$("#" + domId).unbind("focus");
    $("#" + domId).on("focus",function(dp){  
	      WdatePicker({  
	    	  lang:'zh-cn',
	    	  dateFmt:'yyyy-MM-dd',
	    	  realDateFmt:'yyyy-MM-dd',
	    	  maxDate:'#F{$dp.$D(\'endDate\')}',
	    	  readOnly:true,
	    	  isShowClear:true,
	    	  opposite:false,
// disabledDates:date_Array,//绑定数组
	          onpicked:function(dp){ // 点击某一日期后触发
	        	// 获取当前选中的日期
	  			var curDate = dp.cal.getNewDateStr();
	          }  
	      });  
	  }); 
}   

var search_period_day_end_init = function(domId){
	
	$("#" + domId).unbind("focus");
    $("#" + domId).on("focus",function(dp){  
	      WdatePicker({  
	    	  lang:'zh-cn',
	    	  dateFmt:'yyyy-MM-dd',
	    	  realDateFmt:'yyyy-MM-dd',
	    	  minDate:'#F{$dp.$D(\'startDate\')}',
	    	  readOnly:true,
	    	  isShowClear:true,
	    	  opposite:false,
	          onpicked:function(dp){ // 点击某一日期后触发
	        	// 获取当前选中的日期
	  			var curDate = dp.cal.getNewDateStr();
	          }  
	      });  
	  }); 
}

// --------------------start--------------------
// 查询周期初始化
var common_search_period_date_init_with_fn = function(domId,dateFmt,realDateFmt,minDate,maxDate,readOnly,isShowClear,opposite,disabledDatesArr,fn){
    var options = {
    		lang:'zh-cn',
    		dateFmt:dateFmt,
    		realDateFmt:realDateFmt,
    		minDate:minDate,
    		maxDate:maxDate,
    		readOnly:readOnly,
    		isShowClear:isShowClear,
    		opposite:opposite,
    		disabledDates:disabledDatesArr
    	};
    
    if(fn && (Object.propotype.toString.call(object) === "[object Function]")){
    	options['onpicked'] = fn;
    }else{
    	options['onpicked'] = function(dp){ // 点击某一日期后触发
             // 获取当前选中的日期
             var curDate=dp.cal.getNewDateStr();
        };
    }
    
    $("#" + domId).unbind("focus");
    $("#" + domId).on("focus",function(dp){  
	      WdatePicker(
	    	  options
	      );  
	  });
}

// 稽核结果总览页面查询周期初始化
var check_overview_month_init_fn = function(domId, fn) {
	$.ajax({
		type : "GET",
		url : appendExtraParams(window.location.protocol + "//"
				+ window.location.host + "/cimp/api/channel/audit/date/info"),
		dataType : "json",
		data : {},
		async : false,
		success : function(data) {

			if (data.data != undefined && data.data.minMon != undefined
					&& data.data.maxMon != null && data.data.minMon != ''
					&& data.data.maxMon != '') {
				var data = data.data;
				var minMon = data.minMon;
				var maxMon = data.maxMon;
				minMon = minMon.substr(0, 4) + '-' + minMon.substr(4);
				maxMon = maxMon.substr(0, 4) + '-' + maxMon.substr(4);
				var date_Array = {};

				if (data.data != undefined && data.data.length > 0) {
					date_Array = data.data;
					for (var i = 0; i < date_Array.length; i++) {
						date_Array[i] = date_Array[i].substr(0, 4) + '-'
								+ date_Array[i].substr(4);
					}
				}
				$("#" + domId).val(maxMon);

				$("#" + domId).unbind("focus");
				$("#" + domId).on("focus", function(dp) {
					WdatePicker({
						lang : 'zh-cn',
						dateFmt : 'yyyy-MM',
						realDateFmt : 'yyyy-MM',
						minDate : minMon,
						maxDate : maxMon,
						readOnly : true,
						isShowClear : false,
						opposite : false,
						disabledDates : date_Array,// 绑定数组
						onpicked : fn
					});
				});

			} else {

				$("#" + domId).unbind("focus");
				$("#" + domId).on("focus", function(dp) {
					WdatePicker({
						lang : 'zh-cn',
						dateFmt : 'yyyy-MM',
						realDateFmt : 'yyyy-MM',
						// minDate:minMon,
						maxDate : new Date(),
						readOnly : true,
						isShowClear : false,
						opposite : false,
						disabledDates : date_Array,// 绑定数组
						onpicked : fn
					});
				});

			}

		}
	});
}
// -------------------end-----------------------

// 处理多个元素
var dom_elements_put_value_percent_array =function(domIdArray, valueObjArray){
	if(domIdArray && domIdArray.length > 0 && valueObjArray && valueObjArray.length > 0 && domIdArray.length == valueObjArray.length){
		for(var i=0 ; i < valueObjArray.length; i++){
			dom_elements_put_value(domIdArray[i], valueObjArray[i], '%');
		}
	}
}

// 处理单个元素
var dom_elements_put_value_percent =function(domId, valueObj){
	dom_elements_put_value(domId, valueObj, '%');
}
// 根据传入值动态修改元素class
var dom_elements_put_value =function(domId, valueObj, suffix){
	if($('#'+ domId).length == 0){
		return ;
	}
	var bool = true;
	var classNames = $('#'+ domId)[0].className;
	if(classNames && ((classNames.indexOf('f_qudao2') > -1) || (classNames.indexOf('f_qudao3') > -1)  || (classNames.indexOf('f_qudao4') > -1))){
		bool = true;
	}
	if(classNames && (classNames.indexOf('f_qudao1') > -1)){
		bool = false;
	}
	$('#' + domId).removeClass('f_qudao2 f_qudao3 f_qudao4');
	if(valueObj){
		if(!isNaN(valueObj)){
			if(bool){
				if(Number(valueObj) == 0){
					$('#' + domId).addClass('f_qudao4');
				}else if(Number(valueObj) > 0){
					$('#' + domId).addClass('f_qudao2');
				}else{
					$('#' + domId).addClass('f_qudao3');
				}
			}
			valueObj = valueObj + suffix;
		}else{
			valueObj = valueObj.replace('%','');
			if(!isNaN(valueObj)){
				if(bool){
					if(Number(valueObj) == 0){
						$('#' + domId).addClass('f_qudao4');
					}else if(Number(valueObj) > 0){
						$('#' + domId).addClass('f_qudao2');
					}else{
						$('#' + domId).addClass('f_qudao3');
					}
				}
				valueObj = valueObj + suffix;
			}
			if(valueObj.indexOf('--') > -1 ){
				valueObj = '--';
			}
		}
		
	}else{
		valueObj = '--';
	}
	if($('#'+domId)[0].tagName == 'INPUT'){
		$('#'+domId).val(valueObj);
	}
	if($('#'+domId)[0].tagName == 'SPAN'){
		$('#'+domId).text(valueObj);
	}
	if($('#'+domId)[0].tagName == 'LABEL'){
		$('#'+domId).text(valueObj);
	}
}


/**
 * 将Array按照array中对象的prop属性进行排序
 * 
 * @param params
 * @param arrObj
 *            obj 必填 数组对象
 * @param keyName
 *            string 必填 要排序的属性名称
 * @param type
 *            int 选填 默认 type:0 升序 type:1 降序
 */
function array_sort_by_property(arrObj, keyName, type) {
	if(arrObj.length < 1){
		return arrObj;
	}
// var tempArrObj = arrObj.slice(0);
	var tempArrObj = arrObj;
	var compare = function(keyName, type) {
		return function(obj1, obj2) {
			var val1 = obj1[keyName];
			var val2 = obj2[keyName];
			if (!isNaN(Number(val1)) && !isNaN(Number(val2))) {
				val1 = Number(val1);
				val2 = Number(val2);
			}
			// 如果值为空的，放在最后
			if (val1 == null && val2 == null) {
				return 0;
			} else if (val1 == null && val2 != null) {
				return (type == 1 ? -1 : 1);
			} else if (val2 == null && val1 != null) {
				return (type == 1 ? 1 : -1);
			}
			// 排序
			if (val1 < val2) {
				return (type == 1 ? 1 : -1);
			} else if (val1 > val2) {
				return (type == 1 ? -1 : 1);
			} else {
				return 0;
			}
		}
	}
	return tempArrObj.sort(compare(keyName, type));
}
// 用js将时间戳转yyyy/MM/dd类似的格式
function formatDate(inputTime) {
	var date = new Date(inputTime);
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	m = m < 10 ? ('0' + m) : m;
	var d = date.getDate();
	d = d < 10 ? ('0' + d) : d;
	var h = date.getHours();
	h = h < 10 ? ('0' + h) : h;
	var minute = date.getMinutes();
	var second = date.getSeconds();
	minute = minute < 10 ? ('0' + minute) : minute;
	second = second < 10 ? ('0' + second) : second;
	// return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
	return y + '/' + m + '/' + d;
// return y + '' + m + '' + d;
};

// 比较日期 yyyy/MM/dd
function compareDate(DateOne, DateTwo) {
	if (Date.parse(DateOne) > Date
			.parse(DateTwo)) {
		return true;
	} else {
		return false;
	}
}

var getParamter = function(paramName) {
    var PARAMTER_VALUE;    
    if(!PARAMTER_VALUE) {   // 第一次初始化
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

    // ADD BY guo_jg
    if(paramValue) {    
        return paramValue;    
    }else{
		return "";
	}  	
}

// 多选案例
var multipleSelect=function(elem,data,keyCode,keyName,text,allSelctElem,elmStyle){
	var dd=[];
		$.each(data,function(index,obj){
			dd.push('<li data-code="'+obj[keyCode]+'">' +
                '<label for="region_'+elem.substr(1)+''+obj[keyCode]+'">' +
                '<input name="region_area" data-name="'+obj[keyName]+'" value="'+obj[keyCode]+
                '" id="region_'+elem.substr(1)+obj[keyCode]+'" type="checkbox">&nbsp;'+obj[keyName]+'</label></li>');
		});
		$(elem).empty().html(dd.join(''));
		$(elem).find('input').change(function(){
			var allSize = $(elem).find('li').size();
			var size=$(elem).find('input:checked').length;
			var checked = $(allSelctElem).prop('checked');
			var simpleChecked = $(this).prop('checked');
			if(checked  && !simpleChecked){
				$(allSelctElem).attr('checked',false);
			}
			if(allSize == size){
				$(allSelctElem).attr('checked',true);
			}
			
			$(elem).closest('dl').children('dt').text('已选择'+size+'个'+text);
			$(elmStyle).find('dt').removeClass("sel_placeholder");
			if(size==0){
			$(elem).closest('dl').children('dt').text(text);	
			$(elmStyle).find('dt').addClass("sel_placeholder");
			}
		});
}

/**
 * 多选案例,初始化选择一些项
 * 
 * @param elem
 * @param data
 * @param keyCode
 * @param keyName
 * @param text
 * @param allSelctElem
 * @param elmStyle
 * @param isKeyCode
 *            是否根据keyCode来选中项
 * @param selectedArray
 *            需要选中的项数组
 * @param editable
 *            是否可以编辑修改 true 可编辑; false 不可编辑
 */
var multipleSelectWithSelected=function(elem,data,keyCode,keyName,text,allSelctElem,elmStyle, isKeyCode, selectedArray, editable){
    var selectedSize = 0;
    var all = $(elem).find('li').size();
	var dd=[];
		$.each(data,function(index,obj){
            var str1 = '<li data-code="'+obj[keyCode]+'"><label for="region_'+elem.substr(1)+''+obj[keyCode]+'">' +
                '<input name="region_area" data-name="'+obj[keyName]+'" value="'+obj[keyCode]+'" id="region_'+elem.substr(1)+
                obj[keyCode]+'" type="checkbox" ';
            var str2 = ' checked="checked" ';
            var str3 = (editable ? '': 'disabled') + '>&nbsp;'+obj[keyName]+'</label></li>';

		    if (isKeyCode){
                if ( selectedArray.indexOf(obj[keyCode]) > -1){
                    selectedSize++;
                    dd.push(str1 + str2 + str3);
                }else{
                    dd.push(str1 + str3);
                }
            }else{
                if ( selectedArray.indexOf(obj[keyName]) > -1){
                    selectedSize++;
                    dd.push(str1 + str2 + str3);
                }else{
                    dd.push(str1 + str3);
                }
            }

		});

		$(elem).empty().html(dd.join(''));
		if (selectedSize > 0) {
            $(elem).closest('dl').children('dt').text('已选择'+ selectedSize +'个'+text);
            $(elmStyle).find('dt').removeClass("sel_placeholder");
        }

        if(all == selectedSize){
            $(allSelctElem).attr('checked',true);
        }

        if (!editable){
            $(allSelctElem).attr('disabled','disabled');
        }

		$(elem).find('input').change(function(){
			var allSize = $(elem).find('li').size();
			var size=$(elem).find('input:checked').length;
			var checked = $(allSelctElem).prop('checked');
			var simpleChecked = $(this).prop('checked');
			if(checked  && !simpleChecked){
				$(allSelctElem).attr('checked',false);
			}
			if(allSize == size){
				$(allSelctElem).attr('checked',true);
			}

			$(elem).closest('dl').children('dt').text('已选择'+size+'个'+text);
			$(elmStyle).find('dt').removeClass("sel_placeholder");
			if(size==0){
			$(elem).closest('dl').children('dt').text(text);
			$(elmStyle).find('dt').addClass("sel_placeholder");
			}
		});
}

// 过滤输入框
var filterInput=function(id,pattren1,pattren2){
$(id).keyup(function(e){
            var value=$(this).val();
            if(!(pattren1).test(value)){
            $(this).val(value.replace(pattren2, ""));
            }
});
}

/**
 * 根据name属性给表单赋值
 * 
 * @param formId
 *            form表单的id
 * @param jsonValue
 *            数据源
 * @param emptyForm
 *            当值为空时的显示形式,如:"", "--"
 * @returns
 */
function formSetValue(formId, jsonValue, emptyForm){
	var obj = $('#' + formId);
    $.each(jsonValue, function (name, ival) {
    	var $input = obj.find("input:[name=" + name + "]");
        if(!ival){
        	ival = emptyForm;
        }
        if ($input.attr("type") == "radio" || $input.attr("type") == "checkbox") {
            $input.each(function () {
                if (Object.prototype.toString.apply(ival) == '[object Array]') {  
                    for (var i = 0; i < ival.length; i++) {
                        if ($(this).val() == ival[i])
                            $(this).attr("checked", "checked");
                    }
                } else {
                    if ($(this).val() == ival)
                        $(this).attr("checked", "checked");
                }
            });
        } else if ($input.attr("type") == "textarea") {
            obj.find("[name=" + name + "]").html(ival);
        } else {
            obj.find("[name=" + name + "]").val(ival);
            obj.find("[name=" + name + "]").text(ival);
            if(ival){
            	$(obj.find("[name=" + name + "]")).attr('title', ival);
            }
        }
    });
}

/**
 * cimp公共权限鉴定
 * 
 * @param resourceKey
 *            资源key,多个使用'&'连接
 * @param operationKey
 *            操作key,多个使用'&'连接
 * @returns
 */
function cimpCommonPermissionAuth(resourceKey, operationKey){
	$.ajax({
		type : "GET",
		async : false,
		data : {'resourceKey': resourceKey, 'operationKey': operationKey},
		url :  appendExtraParams(window.location.protocol + "//" + window.location.host +
				"/cimp/api/channel/v4.0/portal/permission/operate"),
		dataType : "json",
		success : function(data) {
			return data.data;
		},
		error : function(errorMsg) {
			alert("cimp公共权限鉴定请求失败!");
			return new Array();
		}
	});
}

/**
 * 使用一个resourceKey和一个operationKey,对多个dom元素,进行鉴权,无权限,则隐藏dom元素
 * 
 * @param domSelectorArray
 *            dom选择器数组
 * @param resourceKey
 *            一个resourceKey
 * @param operationKey
 *            一个operationKey
 * @returns
 */
function displayElementsPermissionAuth(domSelectorArray, resourceKey, operationKey){
	
	$.ajax({
		type : "GET",
		async : false,
		data : {'resourceKey': resourceKey, 'operationKey': operationKey},
		url :  appendExtraParams(window.location.protocol + "//" + window.location.host +
				"/cimp/api/channel/v4.0/portal/permission/operate"),
		dataType : "json",
		success : function(data) {
			if(data.data && (data.data.length > 0)){
				var auth = data.data;
				if (Object.prototype.toString.apply(domSelectorArray) == '[object Array]') {
					for(var i = 0;i < domSelectorArray.length;i++){
						if(auth[0]){
							$(domSelectorArray[i]).css('display', 'inline-block');
						}else{
							$(domSelectorArray[i]).css('display', 'none');
						}
					}
				}else{
					$(domSelectorArray).css('display', (auth[0] == true ? 'inline-block' : 'none'));
				}
			}
		},
		error : function(errorMsg) {
			alert("cimp公共权限鉴定请求失败!");
			return new Array();
		}
	});

}

// 跳转集团公司详情页面
function linkToDetailPage(code){
    var param = '?code=' + code + '&from=';
    var url='/support/static/page/workorder/detail.shtml';
    linkTo( url + param, false);
}

//跳转集团公司接口人分组详情页面
function linkToGroupDetailPage(groupId){
    var param = '?groupId=' + groupId + '&from=';
    var url='/support/static/page/workorder/group_detail.shtml';
    linkTo( url + param, false);
}

//跳转集团公司详情页面
function linkToDetailPageFrom(code){
    var param = '?code=' + code + '&from=from';
    var url='/support/static/page/workorder/detail.shtml';
    linkTo( url + param, false);
}

// 跳转省站详情页面
function linkToPsDetailPage(code){
    var param = '?code=' + code;
    var url='/support/static/page/workorder/ps/detail_ps.shtml';
    linkTo( url + param, false);
}

/**
 * 公共跳转页面方法 用法示例: CommonLinkToPage({ 'code': '123456' },
 * '/support/static/page/workorder/ps/list_my_deal_ps.shtml', false);
 * 
 * @param pageParam
 *            需要传输的参数,如 {'a': 123, 'b': 'str'}
 * @param pageUrl
 *            需要跳转到的页面路径, 如 '/support/static/page/ps/detail_ps.html'
 * @param isNewWindows
 *            是否开启浏览器新窗口,true 开启,false 不开启.默认为 false
 * @constructor
 */
function CommonLinkToPage(pageParam, pageUrl, isNewWindows){
    isNewWindows = isNewWindows || false;
    var param = '';
    if (pageParam){
        param = '?';
        for (var k in pageParam){
            var tmp = "" + k + "=" + pageParam[k] + "&";
            param += tmp;
        }
        if (param){
            param = param.substring(0, param.length - 1);
        }
    }
    linkTo( pageUrl + param, isNewWindows);
}

/**
 * 判断元素是否存在数组中
 * 
 * @param stringToSearch
 * @param arrayToSearch
 * @returns 存在/不存在 true/false
 */
function in_array(stringToSearch, arrayToSearch) {
    for (s = 0; s < arrayToSearch.length; s++) {
     thisEntry = arrayToSearch[s].toString();
     if (thisEntry == stringToSearch) {
      return true;
     }
    }
    return false;
}

// 查看弹窗
function showLayuiDiv(code){
	showSearchCaseDiv_index = layer.open({
		  title: "工单回复省份(公司)统计",
		  type: 1,
		  resize: false,
		  area: ['500px', '230px'],
		  content: $('#list_my_created_box'), 
		  cancel:function(){
		  $('#list_my_created_box').css('display','none');
		}
	});
    var jsonUrl = appendExtraParams(window.location.protocol + "//" + window.location.host + "/support/workorder/listHandleStatus");

    $.post(jsonUrl, {'code': code}, function(data){
        var processedNum = 0;
        var processedPros = ' ';
        var untreatedNum = 0;
        var untreatedPros = ' ';
        var arr1 = new Array();
        var arr2 = new Array();
        $.each(data.data, function (index, obj) {
        	// 已完成对象列表
        	var completeList = obj.completeList;
        	$.each(completeList, function (index, obj1) {
        		arr1.push(obj1.name)
        		processedNum++;
	            processedPros += obj1.name + '、';
        	});
            // 未完成对象列表
        	var pendList = obj.pendList;
        	$.each(pendList, function (index, obj2) {
	            arr2.push(obj2.name);
	            untreatedNum++;
        		untreatedPros += obj2.name + '、';
        	});
        	// 未回复
            /*if ((obj.status == "00" || obj.status == "21") && !in_array(obj.provinceCode,arr1)){
            	arr1.push(obj.provinceCode);
                untreatedNum++;
                untreatedPros += obj.provinceName + '、';
            }
        	// 已回复
            if ((obj.status == "10" || obj.status == "20")&& !in_array(obj.provinceCode,arr2)){
            	arr2.push(obj.provinceCode);
                processedNum++;
                processedPros += obj.provinceName + '、';
            }*/
        });
        untreatedPros = untreatedPros.substr(0, untreatedPros.length -1);
        processedPros = processedPros.substr(0, processedPros.length -1);
        $('#processedNum').text("已回复省份（公司）（" + processedNum + "）：");
        $('#processedPros').text(processedPros);
        $('#untreatedNum').text("未回复省份（公司）（" + untreatedNum + "）：");
        $('#untreatedPros').text(untreatedPros);
    });
}

// 关闭弹窗按钮
function showSearchCaseDivFun(target){
	layer.close(showSearchCaseDiv_index);
	// $('#showSearchCaseDiv').css('display','none');
	$(target).parent().parent().parent().css('display','none');
}

// 工单基本信息
function information(data) {
	$("#topic").html(data.topic);// 工单标题
	$("#code").html(data.code);// 工单编号
	$("#createUser").html(data.createUserName);// 创建人
	$("#contactOfficer").html(data.contactOfficer);// 联系人
	$("#phone").html(data.phone);// 联系人电话
	$("#email").html(data.email);// 联系人邮箱
	$("#createDate").html(data.createDate);// 创建时间
	$("#deadline").html(data.deadline);// 结束时间
	$("#handlerGroupName").html(data.handlerGroupName);//接口人分组
	templateType = data.templateType;
	if (data.degree == '1') {
		$("#degree").html("普通");
	} else if (data.degree == '2') {
		$("#degree").html("紧急");
	} else if (data.degree == '3') {
		$("#degree").html("非常紧急");
	}
	if (data.templateType ==  '1') {
		$("#templateType").html("系统模板");
	} else if (data.templateType == '2') {
		$("#templateType").html("自建模板");
	} else if (data.templateType == '3') {
		$("#templateType").html("无模板");
	}
	if (data.status == '10') {
		$("#status").html("待提交");
	} else if (data.status == '11') {
		$("#audit").show();
		$("#status").html("审核不通过");
		var flag = false;
		$.each(data.flowLogList, function(index, param) {
			if (param.handleResult == '2' && param.type == '3' && !flag ) {
				$("#tostatus").html("审核不通过");
				$("#coent").html(param.handleOpinions);
				flag = true;
			}
		});
		
	} else if (data.status == '20') {
		$("#status").html("待审核");
	} else if (data.status == '30') {
		$("#status").html("待处理");
	} else if (data.status == '31') {
		$("#status").html("已回复");
	} else if (data.status == '32') {
		$("#status").html("已结单");
	} else if (data.status == '40') {
		$("#status").html("已撤回");
	}
	if (data.smsRemind && !data.emailRemind) {
		$("#emailRemind").html("短信");
	}
	if (data.emailRemind && !data.smsRemind) {
		$("#emailRemind").html("邮件");
	}
	if (data.smsRemind && data.emailRemind) {
		$("#emailRemind").html("短信，邮件");
	}
	// 工单内容
	// createKindEditor('workOrderDescString', data.content, '100%', '100', false);
	$('#workOrderDescString').html(data.content);

	if(data.templateType == '1'){
		 // 1.系统模板：选择系统模板必填附件
		 $('#templateattachfileid').val(data.templateId);
		 if(data.systemTemplateName){
			 $('#templateattachfileid').text(data.systemTemplateName + ".xlsx");
			 $("#templateattachfileid").click(function(){
		    	 templateDowload(data.templateId);
		    	});
			// cmop1.1.1.0 修改：增加系统模板附件合并下载按钮
			$('#downloadSysTemplateAttachMerge').show();
		 }
	 }else if(data.templateType == '2'){
		//2.自建模板 ：选择自建模板附件
		 var createAttachmentFileBo = data.createAttachmentFileBo;
		 if(createAttachmentFileBo != null && createAttachmentFileBo.length > 0 && createAttachmentFileBo != undefined){
			 // cmop1.1.2.3 集成多附件 
			 common_attachment_upload_detail("templateattachfileid", createAttachmentFileBo);
			 // setHrefFun('templateattachfileid', createAttachmentFileBo[0],'templateAttachFileId');
		 }
	 }else{
		 // 3.无模板
	 }
	 // 4.相关附件回显
	 var attachmentFileBo = data.attachmentFileBo;
	 if(attachmentFileBo != null && attachmentFileBo.length > 0 && attachmentFileBo != undefined){
		// cmop1.1.2.3 集成多附件 
		 common_attachment_upload_detail("attachGroupId", attachmentFileBo);
		 // setHrefFun('attachGroupId', attachmentFileBo[0],'attachGroupId')
	 }
}

/**
 * 给附件文件设置超连接
 * @param span_Id
 * @param attachGroupId
 * @param fileName
 * @returns
 */
function setHrefFun(span_Id, data, attachFileId){
	var url = (window.location.protocol + "//" + window.location.host +
    "/support/workorder/downloadAttachContent?attachFileId=") + data.attachFileId;

    $("#" + span_Id).html(data.fileName);
    $("#" + span_Id).attr("href", url);
    $("#" + span_Id).attr("target", "_blank");
    $('#' + attachFileId).val(data.attachGroupId);
}

/**
 * 给附件文件设置超连接
 * @param span_Id
 * @param attachGroupId
 * @param fileName
 * @returns
 */
function setHrefUrlFun(span_Id, data, attachFileId,url){
	var url = (window.location.protocol + "//" + window.location.host + url + "?attachFileId=") + data.attachFileId;
    $("#" + span_Id).html(data.fileName);
    $("#" + span_Id).attr("href", url);
    $("#" + span_Id).attr("target", "_blank");
    $('#' + attachFileId).val(data.attachGroupId);
}

// 下载选择模板
function templateDowload(templateId) { 
	var exportUrl = appendExtraParams(window.location.protocol + "//"
			+ window.location.host + "/support/workorder/downloadSysTemplateAttach?code=" + templateId);
	var data = {};
	var exportUrl = exportExcel(data, exportUrl);
}


