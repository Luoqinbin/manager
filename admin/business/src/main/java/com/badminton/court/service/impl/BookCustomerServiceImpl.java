package com.badminton.court.service.impl;

import com.badminton.court.mapper.BookCustomerMapper;
import com.badminton.court.service.BookCustomerService;
import com.badminton.court.service.CourtInfoService;
import com.badminton.court.service.CourtProductService;
import com.badminton.court.service.FlowRecordService;
import com.badminton.entity.court.BookCustomer;
import com.badminton.entity.court.CourtProduct;
import com.badminton.entity.court.FlowRecord;
import com.badminton.entity.court.query.BookCustomerInfoQuery;
import com.badminton.entity.member.MemberInfo;
import com.badminton.entity.system.SysHoliday;
import com.badminton.member.service.IMemberInfoService;
import com.badminton.member.service.SysHolidayService;
import com.badminton.result.BaseResult;
import com.badminton.utils.DateUtil;
import com.badminton.utils.TimestampPkGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Luoqb on 2017/3/15.
 */
@Service("bookCustomerService")
public class BookCustomerServiceImpl implements BookCustomerService{
    @Resource
    private BookCustomerMapper mapper;
    @Autowired
    private IMemberInfoService memberInfoService;
    @Autowired
    private FlowRecordService flowRecordService;

    @Autowired
    private CourtInfoService courtInfoService;
    @Autowired
    private CourtProductService courtProductService;
    @Autowired
    private SysHolidayService sysHolidayService;

    @Override
    public List<BookCustomerInfoQuery> query(BookCustomer customer) {
        try{
            return mapper.query(customer);
        }catch (Exception e){
            e.printStackTrace();
        }
      return null;
    }

    @Override
    public BookCustomer queryOne(BookCustomer customer) {
        return mapper.selectOne(customer);
    }

    @Override
    public void insert(BookCustomer customer) throws Exception{
        mapper.insert(customer);
    }

    @Override
    public void deleteById(BookCustomer bookCustomer) {
        mapper.delete(bookCustomer);
    }

    @Override
    @Transactional
    public BaseResult insertOrder(String areaId, String price, String payType, String phone, String date, String person, String startTime, String endTime) throws Exception {
        BaseResult baseResult = new BaseResult();
        //查询场地是否被预定
        String[] strIds = areaId.split(",");
        int num = 0;
        for(int i=0;i<strIds.length;i++){
            BookCustomer c = new BookCustomer();
            c.setProductId(strIds[i]);
            BookCustomer customer = this.queryOne(c);
            if(customer!=null){
                num++;
            }
        }
        String number = "";
        if(num>0){
            baseResult.setMessage("预订失败，该场地已经被人预定");
            baseResult.setCode(BaseResult.CODE_FAIL);
            return baseResult;
        }else {
            //判断是不是储值卡
            if (payType.equals("1")) {
                //验证储值卡手机号
                MemberInfo memberInfo = new MemberInfo();
                memberInfo.setPhone(phone);
                MemberInfo m = this.memberInfoService.queryOne(memberInfo);
                number = m.getNumber();
                if (m == null) {
                    baseResult.setMessage("没有找到储值卡，请检查手机号码是否正确!");
                    baseResult.setCode(BaseResult.CODE_FAIL);
                    return baseResult;
                } else {
                    //扣费

                    double dou = m.getAccount() - Double.parseDouble(price);
                    if (m.getAccount() >= Double.parseDouble(price)) {
                        m.setAccount(dou);
                        this.memberInfoService.update(m);
                        //添加记录
                        FlowRecord flowRecord = new FlowRecord();
                        flowRecord.setId(new TimestampPkGenerator().next(getClass()));
                        flowRecord.setCreatedDt(new Date());
                        flowRecord.setOperateType(11);
                        flowRecord.setAmount(Double.parseDouble( price));
                        flowRecord.setDirection(1);
                        this.flowRecordService.insert(flowRecord);
                    } else {
                        baseResult.setMessage("此储值卡余额不足，请充值后在操作!");
                        baseResult.setCode(BaseResult.CODE_FAIL);
                        return baseResult;
                    }
                }
            }
            double d = Double.parseDouble(price) / strIds.length;
            String dd = String.format("%.2f", d);//保留2位小数
            for (int i = 0; i < strIds.length; i++) {
                BookCustomer customer = new BookCustomer();
                customer.setProductId(strIds[i]);
                customer.setCreatedDt(DateUtil.string2Date(date));
                CourtProduct courtProduct = this.courtProductService.queryById(Long.parseLong(strIds[i]));
                //customer.setPrice(courtProduct.getPrice());
                customer.setPayType(Double.parseDouble(payType));
                customer.setMobile(phone);
                customer.setSource(2D);
                customer.setState(2);
                customer.setRefundState(0);
                customer.setPerson(person);
                customer.setPrice(Double.parseDouble(dd));
                customer.setId(new TimestampPkGenerator().next(getClass()));
                customer.setMemberNum(number);
                courtProduct.setState(3);
                customer.setNote(startTime + "-" + endTime);
                this.courtProductService.update(courtProduct);
                this.insert(customer);
            }
            baseResult.setMessage("预订成功");
            baseResult.setCode(BaseResult.CODE_OK);
            return baseResult;
        }
    }

    public Boolean isBusyTime(Date date, Boolean isHoliday){
        if(isHoliday){
            //节假日全天都是忙时
            return true;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //平时16点前为闲时
        cal.set(Calendar.HOUR_OF_DAY, 16);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return !date.before(cal.getTime());
    }
}
