define(["dojo"], function (dojo) {

	var module = {
		
		/**
		 * 初始化默认的分页table
		 */
		initPageTable: function (/**Node*/dom, /**String*/url, /**Array*/aoColumns, /**Array*/aoColumnDefs, /**function*/conditionFun, /**function*/initHandlerFun, /**String*/language,isSeq) {

			var t = $(dom).dataTable(module._getDefaultConfig(url, aoColumns, aoColumnDefs, conditionFun, initHandlerFun, language));
            t.on('click', 'tbody tr', function () {
            	var that = $(this);

				if (that.hasClass('active') ) {
					that.removeClass('active');
				}
				else {
					$('tbody tr.active').removeClass('active');
					that.addClass('active');
				}
            });
            return t;
		},
        /**
         * 初始化默认的分页table
         */
        initPageTableNoSeq: function (/**Node*/dom, /**String*/url, /**Array*/aoColumns, /**Array*/aoColumnDefs, /**function*/conditionFun, /**function*/initHandlerFun, /**String*/language,isSeq) {
            var t = $(dom).dataTable(module._getDefaultConfig(url, aoColumns, aoColumnDefs, conditionFun, initHandlerFun, language,"1"));
            t.on('click', 'tbody tr', function () {
                var that = $(this);

                if (that.hasClass('active') ) {
                    that.removeClass('active');
                }
                else {
                    $('tbody tr.active').removeClass('active');
                    that.addClass('active');
                }
            });
            return t;
        },
		getRowData:function (dom) {
			var table = $(dom).dataTable();
            var nTrs = table.fnGetNodes();
            for(var i = 0; i < nTrs.length; i++){
                if($(nTrs[i]).hasClass('active')){
                  return table.fnGetData(nTrs[i]);//fnGetData获取一行的数据
                }
            }
        },

		/**
		 * 初始化自定义配置的table
		 */
		initCustomTable: function (/**Node*/dom, /**Object*/opts) {
			return $(dom).dataTable(opts);
		},
		
		
		initCustomTable1: function (/**Node*/dom, /**String*/url, /**Array*/aoColumns, /**Array*/aoColumnDefs) {
			var config = module.config._basic;
			dojo.mixin(config, {
				'language': module.config._language.zh,
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
			$(dom).DataTable(config);
		},
		
		
		initCustomTable2: function(tableId, url, aoColumns, aoColumnDefs, data, handler) {
			var config = module.config._basic;
			dojo.mixin(config, {
				ajax: url + "?" + data,
				'language': module.config._language.zh,
				"fnDrawCallback":function() {
					var api = this.api();
					var startIndex= api.context[0]._iDisplayStart;
					api.column(0).nodes().each(function(cell, i) {
						cell.innerHTML = startIndex + i + 1;
						//cell.innerHTML =  i + 1;
					});
					if (handler) handler();
				}
			});
			return $('#' + tableId).dataTable(config);
		},
		
		
		initCustomTable3: function (tableId, url, aoColumns, aoColumnDefs, myData) {
			var config = module.config._basic;
			
			dojo.mixin(config, {
				"oLanguage": module.config._language.oLanguage,
				"aoColumns": aoColumns,
				"aoColumnDefs": aoColumnDefs,
				"sAjaxSource": url,//ajax数据源
				"bLengthChange":false, //关闭每页显示多少条数据
				"bAutoWidth" : false, //是否自适应宽度
				"searching": false,//搜索按钮是否显示
				/*"ordering": true,//排序按钮否显示总开关*/
				"paging": true,//是否分页
				"scrollY": 400,//在两边加滚动条效果
				"fnServerData": function (Source, data, fnCallback, oSettings) {
					$(myData).attr("aoData", JSON.stringify(data));
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
				}
			});
			
			var tbl = $('#' + tableId).DataTable(config);

			//动态生成序号封装
			tbl.on('draw', function () {
				var startIndex = tbl.context[0]._iDisplayStart; //获取到本页开始的条数
				tbl.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
					cell.innerHTML = startIndex + i + 1;
				});
			});
			return tbl;
		},

		initCustomTable4: function (tableId, url, aoColumns, aoColumnDefs, data) {
			var config = module.config._basic;
			dojo.mixin(config, {
				"aoColumns" : aoColumns,
				"aoColumnDefs":aoColumnDefs,
				ajax: url + "?" + data,
				"fnDrawCallback": function() {
					var api = this.api();
					//var startIndex= api.context[0]._iDisplayStart;
					//api.column(0).nodes().each(function(cell, i) {
					//cell.innerHTML = startIndex + i + 1;
					//cell.innerHTML =  i + 1;
					//});
				}
			});
			return $('#' + tableId).dataTable(config);
		},


		initCustomTable5: function (tableId, url, aoColumns, aoColumnDefs, data) {
			var config = module.config._basic;
			dojo.mixin(config, {
				'language' : {
					'emptyTable' : '没有数据',
					'loadingRecords' : '加载中...'
				},
				"processing" : false,
				"bSort" : false,
				"bLengthChange": false, //关闭每页显示多少条数据
				"bAutoWidth": false,
				"bInfo": false, //页脚信息
				"aoColumns" : aoColumns,
				"aoColumnDefs":aoColumnDefs,
				"serverSide" : false,// 打开后台分页
				ajax: url + "?" + data,
				"bPaginate" : false,
				"fnDrawCallback":function() {
					var api = this.api();
					var startIndex= api.context[0]._iDisplayStart;
					api.column(0).nodes().each(function(cell, i) {
						cell.innerHTML = startIndex + i + 1;
						cell.innerHTML =  i + 1;
					});
				}
			});
			
			return $('#' + tableId).dataTable(config);
		},
			
		
		/**
		 * 获取DataTable配置
		 * 
		 * @param url 查询数据的url
		 * @param aoColumns
		 * @param aoColumnDefs
		 * @param conditionFun 查询条件处理方法
		 * @param language 语言 （zh<默认> | en）
		 * @returns
		 */
		_getDefaultConfig: function (/**String*/url, /**Array*/aoColumns, /**Array*/aoColumnDefs, /**function*/conditionFun, /**function*/initHandlerFun, /**String*/language,isSeq) {
			if (!url || !aoColumns || !aoColumnDefs) {
				new Error("参数不全！");
			}
			
			var config = module.config._basic;
			return dojo.mixin(config, {
				'language': (!language || language === "zh") ? module.config._language.zh : module.config._language.en,
				"aoColumns": aoColumns,
				"aoColumnDefs":aoColumnDefs,
				"ajax": {
					"url": url,
					"type": "post",
					"beforeSend": function(request) {
                        request.setRequestHeader("X-CSRF-TOKEN", $("#csrfToken").val());
                    },
					"data": !conditionFun ? function (data) {} : conditionFun
				},
				"fnDrawCallback":function() {
					if (initHandlerFun) initHandlerFun();
					var api = this.api();
					var startIndex= api.context[0]._iDisplayStart;
					if(isSeq!="1"){
                        api.column(0).nodes().each(function(cell, i) {
                            cell.innerHTML = startIndex + i + 1;
                        });
					}

				}
			});
		},
		
		
		// 基础配置信息
		config: {
			
			// 基础配置
			_basic: {
				"bFilter" : false, // 搜索栏
				"processing" : true,
				"bSort" : true,
				"bLengthChange": true,
				"bAutoWidth": true,
				"bInfo": true,//页脚信息
				"serverSide" : true,// 打开后台分页
				"sServerMethod":"post",
			},
			
			//datatable语言配置
			_language : {
				// 中文
				zh: {
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
				// 英文
				en: {
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
				
				oLanguage: {
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
				}
			}
		}
		
	};
	return module;
});