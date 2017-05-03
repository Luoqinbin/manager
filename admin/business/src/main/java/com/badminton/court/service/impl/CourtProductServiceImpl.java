package com.badminton.court.service.impl;

import com.badminton.court.mapper.CourtProductMapper;
import com.badminton.court.service.BookCustomerService;
import com.badminton.court.service.CourtProductService;
import com.badminton.court.service.FixedOrderService;
import com.badminton.entity.court.BookCustomer;
import com.badminton.entity.court.CourtProduct;
import com.badminton.entity.court.FixedOrder;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.Condition;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.ConditionConstant;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.OrderCondition;
import com.badminton.interceptors.mySqlHelper.pagehelper.util.StringUtil;
import com.badminton.utils.DateUtil;
import com.badminton.utils.TimestampPkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Luoqb on 2017/3/15.
 */
@Service("courtProductService")
public class CourtProductServiceImpl implements CourtProductService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CourtProductMapper courtProductMapper;
    @Autowired
    private BookCustomerService bookCustomerService;
    @Autowired
    private FixedOrderService fixedOrderService;

    @Override
    public List<CourtProduct> query(CourtProduct courtProduct) {
        if(courtProduct.getOrderDir().equals("asc")) {
            courtProduct.setCondition(Condition.build().addOrder(courtProduct.getOrderColumn(), OrderCondition.Direction.ASC));
        }else if(courtProduct.getOrderDir().equals("desc")) {
            courtProduct.setCondition(Condition.build().addOrder(courtProduct.getOrderColumn(), OrderCondition.Direction.DESC));
        }
        Condition query = Condition.build();
        if(StringUtil.isNotEmpty(courtProduct.getStartTimeQuery())){

            courtProduct.setCondition(query.addWhere(ConditionConstant.PREFIX_GT + "start_time", courtProduct.getStartTimeQuery() ));
        }
        if(StringUtil.isNotEmpty(courtProduct.getEndTimeQuery())){
            courtProduct.setCondition(query.addWhere(ConditionConstant.PREFIX_LT+"end_time",courtProduct.getEndTimeQuery()));
        }
        return courtProductMapper.select(courtProduct);
    }

    @Override
    public boolean checkOrder(CourtProduct courtProduct) {
        Condition query = Condition.build();
        if(StringUtil.isNotEmpty(courtProduct.getStartTimeQuery())){
            courtProduct.setCondition(query.addWhere(ConditionConstant.PREFIX_GE + "start_time", DateUtil.getInitialTime( courtProduct.getStartTimeQuery()) ));
        }
        if(StringUtil.isNotEmpty(courtProduct.getEndTimeQuery())){
            courtProduct.setCondition(query.addWhere(ConditionConstant.PREFIX_LE+"end_time",courtProduct.getEndTimeQuery()));
        }
        courtProduct.setState(1);
        List<CourtProduct> list = courtProductMapper.select(courtProduct);
        return list.size()>0;
    }

    @Override
    public List<CourtProduct> queryByDate(String date,String courtId) {
        CourtProduct courtProduct = new CourtProduct();
        Condition query = Condition.build();
        String startTime = date+" 00:00:00";
        String endTime = date+" 23:59:59";
        courtProduct.setCourtId(Long.parseLong(courtId));
        courtProduct.setCondition(query.addWhere(ConditionConstant.PREFIX_GE + "start_time", startTime));
        courtProduct.setCondition(query.addWhere(ConditionConstant.PREFIX_LE+"end_time",endTime));
        return courtProductMapper.select(courtProduct);
    }

    @Override
    public CourtProduct queryById(Long id) {
        try{

            return courtProductMapper.selectByPrimaryKey(id);
        }catch (Exception e){
            e.printStackTrace();;
        }
        return null;
    }

    @Override
    public void update(CourtProduct courtProduct) throws Exception {
        this.courtProductMapper.updateByPrimaryKeySelective(courtProduct);
    }

    @Override
    public String updateProductFixeOrder(FixedOrder fixedOrder, String cycel, String start, String end, String startTime, String endTime, String courtInfoId,String number) throws Exception {
        //周期
        int index = 0;
        int listSize = 0;
        String[] str= cycel.split(",");
        List<Date> listDate = getBetweenDates(DateUtil.string2Date(start,"yyyy-MM-d"),DateUtil.string2Date(end,"yyyy-MM-dd"));
        listDate.add(0,DateUtil.string2Date(start,"yyyy-MM-d"));
        listDate.add(listDate.size(),DateUtil.string2Date(end,"yyyy-MM-dd"));
        for(Date date:listDate){
            //根据日期获得周期
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if(intWeek <= 0){intWeek = 7;}
            for(String s:str){
                if(intWeek==Integer.parseInt(s)){
                    //得到相应的日期
                    System.out.println("========>"+DateUtil.date2String(date,"yyyy-MM-dd"));

                    //startTime
                    String sd = DateUtil.date2String(date,"yyyy-MM-dd")+" "+startTime+":00:00";
                    String ed =  DateUtil.date2String(date,"yyyy-MM-dd")+" "+endTime+":00:00";
                    List<CourtProduct> list = this.courtProductMapper.queryByTime(sd,ed,courtInfoId);
                    listSize += list.size();
                    for(CourtProduct c:list){
                        if (c.getState()==1) {
                            CourtProduct c1 = new CourtProduct();
                            c1.setId(c.getId());
                            c1.setState(2);
                            c1.setType(1);
                            this.courtProductMapper.updateByPrimaryKeySelective(c1);
                            //添加到订场表
                            BookCustomer customer = new BookCustomer();
                            customer.setPrice(fixedOrder.getPrice());
                            customer.setProductId(c.getId().toString());
                            customer.setPayType(Double.parseDouble(fixedOrder.getPayWay()));
                            customer.setMobile(fixedOrder.getPhone());
                            customer.setSource(2D);
                            customer.setState(2);
                            customer.setRefundState(0);
                            customer.setPerson(fixedOrder.getName());
                            customer.setCreatedDt(new Date());
                            customer.setType(2);
                            customer.setMemberNum(number);
                            long id = new TimestampPkGenerator().next(getClass());
                            customer.setId(id);
                            customer.setFixedOrderId(fixedOrder.getId() + "");
                            bookCustomerService.insert(customer);
                        }else{
                            index++;
                        }
                    }
                }
            }
        }
       return "";

       /* */

    }

    @Override
    public List<CourtProduct> queryByType(CourtProduct courtProduct) {
        return this.courtProductMapper.select(courtProduct);
    }

    @Override
    public List<CourtProduct> queryTime(String area,String time) {
        return courtProductMapper.queryTime(area,time);
    }

    @Override
    public List<CourtProduct> queryTime1(String area, String time) {
        return courtProductMapper.queryTime1(area,time);
    }

    private List<Date> getBetweenDates(Date start, Date end) {
        List<Date> result = new ArrayList<Date>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        tempStart.add(Calendar.DAY_OF_YEAR, 1);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        while (tempStart.before(tempEnd)) {
            result.add(tempStart.getTime());
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;
    }
}
