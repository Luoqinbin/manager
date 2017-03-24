package com.badminton.court.service.impl;

import com.badminton.court.mapper.FixedOrderMapper;
import com.badminton.court.service.CourtProductService;
import com.badminton.court.service.FixedOrderService;
import com.badminton.court.service.FlowRecordService;
import com.badminton.entity.court.FixedOrder;
import com.badminton.entity.court.FlowRecord;
import com.badminton.entity.member.MemberInfo;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.Condition;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.OrderCondition;
import com.badminton.member.service.IMemberInfoService;
import com.badminton.result.BaseResult;
import com.badminton.utils.DateUtil;
import com.badminton.utils.TimestampPkGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Luoqb on 2017/3/16.
 */
@Service("fixedOrderService")
public class FixedOrderServiceImpl implements FixedOrderService {
    @Resource
    private FixedOrderMapper fixedOrderMapper;

    @Autowired
    private IMemberInfoService memberInfoService;
    @Autowired
    private FlowRecordService flowRecordService;
    @Autowired
    private CourtProductService courtProductService;

    @Override
    public List<FixedOrder> query(FixedOrder fixedOrder) {
        if (fixedOrder != null){
            if (fixedOrder.getOrderDir().equals("asc")) {
                fixedOrder.setCondition(Condition.build().addOrder(fixedOrder.getOrderColumn(), OrderCondition.Direction.ASC));
            } else if (fixedOrder.getOrderDir().equals("desc")) {
                fixedOrder.setCondition(Condition.build().addOrder(fixedOrder.getOrderColumn(), OrderCondition.Direction.DESC));
            }
        }

        return fixedOrderMapper.select(fixedOrder);
    }

    @Override
    public void delete(Long id) {
        fixedOrderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void insert(FixedOrder fixedOrder) {
        fixedOrderMapper.insert(fixedOrder);
    }

    @Override
    public FixedOrder queryOne(FixedOrder fixedOrder) {
        return fixedOrderMapper.selectOne(fixedOrder);
    }

    @Override
    public FixedOrder queryById(Object id) {
        return fixedOrderMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(FixedOrder fixedOrder) {
        fixedOrderMapper.updateByPrimaryKeySelective(fixedOrder);
    }

    @Override
    public BaseResult addFixedOrder(FixedOrder fixedOrder) throws Exception {
        BaseResult baseResult = new BaseResult();
        fixedOrder.setId(new TimestampPkGenerator().next(getClass()));
        fixedOrder.setStartDate(DateUtil.string2Date(fixedOrder.getStartDateStr()));
        fixedOrder.setEndDate(DateUtil.string2Date(fixedOrder.getEndDateStr()));

        FixedOrder f = new FixedOrder();
        f.setStartDate(DateUtil.string2Date(fixedOrder.getStartDateStr()));
        f.setEndDate(DateUtil.string2Date(fixedOrder.getEndDateStr()));
        f.setStartTime(fixedOrder.getStartTime());
        f.setEndTime(fixedOrder.getEndTime());
        f.setCycle(fixedOrder.getCycle());
        f.setCourtInfoId(fixedOrder.getCourtInfoId());
        FixedOrder fo = this.queryOne(f);
        if(fo==null) {
            //查询支付方式
            if (fixedOrder.getPayWay().equals("6")){
                //验证储值卡手机号
                MemberInfo memberInfo = new MemberInfo();
                memberInfo.setPhone(fixedOrder.getPhone());
                MemberInfo m = this.memberInfoService.queryOne(memberInfo);
                if (m == null) {
                    baseResult.setMessage("没有找到储值卡，请检查手机号码是否正确!");
                    baseResult.setCode(BaseResult.CODE_FAIL);
                    return baseResult;
                } else {
                    //扣费
                    Double dou = m.getAccount() - fixedOrder.getPrice();
                    if (m.getAccount() >= fixedOrder.getPrice()) {
                        m.setAccount(m.getAccount() - fixedOrder.getPrice());
                        this.memberInfoService.update(m);
                        //添加记录
                        FlowRecord flowRecord = new FlowRecord();
                        flowRecord.setId(new TimestampPkGenerator().next(getClass()));
                        flowRecord.setCreatedDt(new Date());
                        flowRecord.setOperateType(11);
                        flowRecord.setAmount( fixedOrder.getPrice());
                        flowRecord.setDirection(1);
                        this.flowRecordService.insert(flowRecord);
                    } else {
                        baseResult.setMessage("此储值卡余额不足，请充值后在操作!");
                        baseResult.setCode(BaseResult.CODE_FAIL);
                        return baseResult;
                    }
                }
            }
            this.courtProductService.updateProductFixeOrder(fixedOrder,fixedOrder.getCycle(), fixedOrder.getStartDateStr(), fixedOrder.getEndDateStr(), fixedOrder.getStartTime(), fixedOrder.getEndTime(), fixedOrder.getCourtInfoId());
            /*if(flag.equals("1")){
                baseResult.setCode(BaseResult.CODE_FAIL);
                baseResult.setMessage("订场失败");
            }else{*/
                baseResult.setCode(BaseResult.CODE_OK);
                baseResult.setMessage("订场成功");
                this.insert(fixedOrder);
          //  }
        }else{
            baseResult.setCode(BaseResult.CODE_FAIL);
            baseResult.setMessage("已经添加了相同的固定场");
        }
        return baseResult;
    }
}
