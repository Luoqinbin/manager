/**
 * 公有js
 */
//表格方法
function tableContent(tableId,url,aoColumns,aoColumnDefs){
	$('#'+tableId).DataTable({
		'language' : {
			'emptyTable' : '没有数据',
			'loadingRecords' : '加载中...',
			'processing' : '查询中...',
			'search' : '搜索:',
			'lengthMenu' : '每页 _MENU_ 条记录',
			'zeroRecords' : '没有数据',
			'paginate' : {
				'next' : '上一页',
				'previous' : '下一页'
			},
			'info' : '第 _PAGE_ 页 / 共 _PAGES_ 页',
			'infoEmpty' : '没有数据',
			'infoFiltered' : '(从 _MAX_ 条记录中筛选)'
		},
		"bFilter" : false,// 搜索栏
		"processing" : true,
		"bSort" : true,
		"bLengthChange": true,
		"bAutoWidth": true,
		"aoColumns" : aoColumns,
		"aoColumnDefs":aoColumnDefs,
		"serverSide" : true,// 打开后台分页
		"bDestroy" : true,
		"sAjaxSource" : url,
		"fnServerData" : function(sSource, aoData, fnCallback) {
			$.ajax({
				"type" : 'post',
				"url" : sSource,
				"dataType" : "json",
				"data" : {
					aoData : JSON.stringify(aoData)
				},
				"success" : function(resp) {
					fnCallback(resp);
				}
			});
		}
	});
}

function tableContent1(tableId,url,aoColumns,aoColumnDefs,data, handler){
	var tbl = $('#'+tableId).dataTable({
		'language' : {
			'emptyTable' : '没有数据',
			'loadingRecords' : '加载中...',
			'processing' : '查询中...',
			'search' : '搜索:',
			'lengthMenu' : '每页 _MENU_ 条记录',
			'zeroRecords' : '没有数据',
			'paginate' : {
				'next' : '下一页',
				'previous' : '上一页'
			},
			'info' : '第 _PAGE_ 页 / 共 _PAGES_ 页',
			'infoEmpty' : '没有数据',
			'infoFiltered' : '(从 _MAX_ 条记录中筛选)'
		},
		"bFilter" : false,// 搜索栏
		"processing" : true,
		"bSort" : true,
		"bLengthChange": true,
		"bAutoWidth": true,
		"bInfo": true,//页脚信息
		"aoColumns" : aoColumns,
		"aoColumnDefs":aoColumnDefs,
		"serverSide" : true,// 打开后台分页
		"sServerMethod":"post",
		 ajax: url+"?"+data,
		"fnDrawCallback":function(){
			var api = this.api();
			var startIndex= api.context[0]._iDisplayStart;
			api.column(0).nodes().each(function(cell, i) {
				cell.innerHTML = startIndex + i + 1;
				//cell.innerHTML =  i + 1;
			});
			if (handler) handler();
		}

	});
	return tbl;
}
//+++++++++++++++++++++++++++++++++++++++++
function tableContent2(tableId,url,aoColumns,aoColumnDefs,myData) {

	var tbl = $('#'+tableId).DataTable({
		"bProcessing" : true, //DataTables载入数据时，是否显示‘进度’提示
		"bAutoWidth" : false, //是否自适应宽度
		"sAjaxSource": url,//ajax数据源
		"bServerSide": true,//是否服务端模式
		"searching": false,//搜索按钮是否显示
		"bLengthChange":false, //关闭每页显示多少条数据
		/*"ordering": true,//排序按钮否显示总开关*/
		"paging": true,//是否分页
		"scrollY": 400,//在两边加滚动条效果
		"bSort" : true, //是否启动各个字段的排序功能
		"bFilter" : false, //是否启动过滤、搜索功能
		"aoColumns": aoColumns,
		"aoColumnDefs": aoColumnDefs,
		"oLanguage": {
			"sProcessing": "正在加载中......",
			"sLengthMenu": "每页显示 _MENU_ 条记录",
			"sZeroRecords": "没有数据！",
			"sEmptyTable": "表中无数据存在！",
			"sInfo": "当前显示 _START_ 到 _END_ 条，共 _TOTAL_ 条记录",
			"sInfoEmpty": "显示0到0条记录",
			"sInfoFiltered": "数据表中共为 _MAX_ 条记录",
			"oPaginate": {
				"sFirst": "首页",
				"sPrevious": "上一页",
				"sNext": "下一页",
				"sLast": "末页"
			}
		},
		"fnServerData": function (Source, data, fnCallback, oSettings) {
			$(myData).attr("aoData",JSON.stringify(data));
			oSettings.jqXHR = $.ajax({
				"dataType": 'json',
				"type": "POST",
				"url": Source,
				"data": myData,
				"success": function (data) {
					console.log(data);
					fnCallback(data);
				},
				"error": function (e) {
					console.log(e.message);
				}
			});
		},
	});

	//动态生成序号封装
	tbl.on('draw', function () {
		var startIndex = tbl.context[0]._iDisplayStart;//获取到本页开始的条数
		tbl.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
			cell.innerHTML = startIndex + i + 1;
		});
	});
	return tbl;
}

function tableContent3(tableId,url,aoColumns,aoColumnDefs,data){
	var tbl = $('#'+tableId).dataTable({
		'language' : {
			'emptyTable' : '没有数据',
			'loadingRecords' : '加载中...',
			'processing' : '查询中...',
			'search' : '搜索:',
			'lengthMenu' : '每页 _MENU_ 条记录',
			'zeroRecords' : '没有数据',
			'paginate' : {
				'next' : '下一页',
				'previous' : '上一页'
			},
			'info' : '第 _PAGE_ 页 / 共 _PAGES_ 页',
			'infoEmpty' : '没有数据',
			'infoFiltered' : '(从 _MAX_ 条记录中筛选)'
		},
		"bFilter" : false,// 搜索栏
		"processing" : true,
		"bSort" : true,
		"bLengthChange": true,
		"bAutoWidth": true,
		"bInfo": true,//页脚信息
		"aoColumns" : aoColumns,
		"aoColumnDefs":aoColumnDefs,
		"serverSide" : true,// 打开后台分页
		"sServerMethod":"post",
		ajax: url+"?"+data,
		"fnDrawCallback":function(){
			var api = this.api();
			//var startIndex= api.context[0]._iDisplayStart;
			//api.column(0).nodes().each(function(cell, i) {
			//cell.innerHTML = startIndex + i + 1;
			//cell.innerHTML =  i + 1;
			//});
		}

	});
	return tbl;
}


function tableContent4(tableId,url,aoColumns,aoColumnDefs,data){
	var tbl = $('#'+tableId).dataTable({
		'language' : {
			'emptyTable' : '没有数据',
			'loadingRecords' : '加载中...'
		},
		"bFilter" : false,// 搜索栏
		"processing" : false,
		"bSort" : false,
		"bLengthChange": false,
		"bAutoWidth": false,
		"bInfo": false,//页脚信息
		"aoColumns" : aoColumns,
		"aoColumnDefs":aoColumnDefs,
		"serverSide" : false,// 打开后台分页
		"sServerMethod":"post",
		ajax: url+"?"+data,
		"bPaginate" : false,
		"fnDrawCallback":function(){
			var api = this.api();
			var startIndex= api.context[0]._iDisplayStart;
			api.column(0).nodes().each(function(cell, i) {
				cell.innerHTML = startIndex + i + 1;
				cell.innerHTML =  i + 1;
			});
		}

	});
	return tbl;
}

//加法
function accAdd(arg1,arg2){  
    var r1,r2,m;  
    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}  
    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}  
    m=Math.pow(10,Math.max(r1,r2));  
    return (arg1*m+arg2*m)/m;  
}
//减法
function accSub(arg1,arg2){      
    return accAdd(arg1,-arg2);  
}
//乘法
function accMul(arg1,arg2)  
{  
    var m=0,s1=arg1.toString(),s2=arg2.toString();  
    try{m+=s1.split(".")[1].length}catch(e){}  
    try{m+=s2.split(".")[1].length}catch(e){}  
    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)  
}
//除法
function accDiv(arg1,arg2){  
    var t1=0,t2=0,r1,r2; 
    try{t1=arg1.toString().split(".")[1].length}catch(e){}  
    try{t2=arg2.toString().split(".")[1].length}catch(e){}  
    with(Math){  
        r1=Number(arg1.toString().replace(".",""))  
        r2=Number(arg2.toString().replace(".",""))  
        return (r1/r2)*pow(10,t2-t1);  
    }  
}
/**
 * 判断是否为空
 * 空 : true
 * 非空 : false
 */
function isEmpty(_value){
	if(_value == null || _value == undefined || _value == "" || _value.length == 0 ){
		return true;
	}
	return false;
}
