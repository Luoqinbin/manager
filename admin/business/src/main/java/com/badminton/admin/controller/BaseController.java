package com.badminton.admin.controller;


import com.badminton.order.BaseOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;

/**
 * 通用controller
 * @author Administrator
 *
 */
public class BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
	//表单校验
	public void orderCheck(BaseOrder order, BindingResult bindingResult) throws Exception {
		if(order==null){
			logger.info("表单参数校验失败:{}",bindingResult.getAllErrors());
			throw new IllegalArgumentException();
		}
		valid(bindingResult);
	}
	//入参效验
	public void valid(BindingResult bindingResult) throws Exception{
		if (bindingResult.hasErrors()) {
			logger.info("表单参数校验失败:{}",bindingResult.getAllErrors());
			throw new IllegalArgumentException();
		}
	}

}
