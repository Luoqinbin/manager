package com.badminton.result;

import com.badminton.interceptors.mySqlHelper.pagehelper.PageInfo;

import java.util.List;

/**
 * 封装DATATABLE服务器模示展示时所需要的几个必备参数
 *
 * @param <T> the type parameter
 */
public class PageResult<T> extends BaseResult {

    //必要。上面提到了，Datatables发送的draw是多少那么服务器就返回多少。 这里注意，作者出于安全的考虑，强烈要求把这个转换为整形，即数字后再返回，而不是纯粹的接受然后返回，这是 为了防止跨站脚本（XSS）攻击。
   private long draw;

    //必要。即没有过滤的记录数（数据库里总共记录数）
    private  long recordsTotal;

    // 必要。过滤后的记录数（如果有接收到前台的过滤条件，则返回的是过滤后的记录数）
    private long recordsFiltered;

    //必要。表中中需要显示的数据。这是一个对象数组，也可以只是数组，区别在于 纯数组前台就不需要用 columns绑定数据，会自动按照顺序去显示 ，而对象数组则需要使用 columns绑定数据才能正常显示。 注意这个 data的名称可以由 ajaxOption ajax不定时一讲 的 ajax.dataSrcOption columns.dataSrc 1不定时一讲 columns.dataSrc 2不定时一讲 控制
    private List<T> data;

    //可选。你可以定义一个错误来描述服务器出了问题后的友好提示
    private String error;

    /**
     * Gets draw.
     *
     * @return the draw
     */
    public long getDraw() {
        return draw;
    }

    /**
     * Sets draw.
     *
     * @param draw the draw
     */
    public void setDraw(long draw) {
        this.draw = draw;
    }

    /**
     * Gets records total.
     *
     * @return the records total
     */
    public long getRecordsTotal() {
        return recordsTotal;
    }

    /**
     * Sets records total.
     *
     * @param recordsTotal the records total
     */
    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    /**
     * Gets records filtered.
     *
     * @return the records filtered
     */
    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    /**
     * Sets records filtered.
     *
     * @param recordsFiltered the records filtered
     */
    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<T> getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * Gets error.
     *
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * Sets error.
     *
     * @param error the error
     */
    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "draw=" + draw +
                ", recordsTotal=" + recordsTotal +
                ", recordsFiltered=" + recordsFiltered +
                ", data=" + data +
                ", error='" + error + '\'' +
                '}';
    }

    /**
     * Instantiates a new Page result.
     *
     * @param pageInfo the page info
     */
    public PageResult(PageInfo<T> pageInfo){
        this.data=pageInfo.getList();
        this.recordsTotal=pageInfo.getTotal();
        this.recordsFiltered=pageInfo.getTotal();
    }

    /**
     * Instantiates a new Page result.
     *
     * @param list         the list
     * @param recordsTotal the records total
     */
    public PageResult(List<T> list, long recordsTotal){
        this.data=list;
        this.recordsTotal=recordsTotal;
        this.recordsFiltered=recordsTotal;
    }


}
