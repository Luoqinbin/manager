package com.badminton.utils;

import jxl.*;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;

/**
 * excel 导出
 */
@Component
public class JxlExcelUtils {

    private DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
    /**
     * 导出Excel<br>
     * 方 法 名：getExcelStream <br>
     */
    public int getExcelStream(String sheetName, LinkedHashMap<String, String> keyMap, List listContent,
                              OutputStream os, Map<String, String> methodMap) {
        int flag = 0;
        // 声明工作簿
        WritableWorkbook workbook;
        try {
            // 根据传进来的file对象创建可写入的Excel工作薄
            workbook = Workbook.createWorkbook(os);
            // 创建一个工作表
            WritableSheet ws = workbook.createSheet(sheetName, 0);

            SheetSettings ss = ws.getSettings();
            ss.setVerticalFreeze(1);// 冻结表头

            // 设置字体
            WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 12);
            WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);

            // 标题居中
            WritableCellFormat titleFormat = new WritableCellFormat(BoldFont);
            titleFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
            titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            titleFormat.setAlignment(Alignment.CENTRE); // 文字水平对齐
            titleFormat.setWrap(false); // 文字是否换行

            // 正文居中
            WritableCellFormat contentCenterFormat = new WritableCellFormat(NormalFont);
            contentCenterFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            contentCenterFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            contentCenterFormat.setAlignment(Alignment.CENTRE);
            contentCenterFormat.setWrap(false);

            // 正文右对齐
            WritableCellFormat contentRightFormat = new WritableCellFormat(NormalFont);
            contentRightFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            contentRightFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            contentRightFormat.setAlignment(Alignment.RIGHT);
            contentRightFormat.setWrap(false);

            // 设置标题,标题内容为keyMap中的value值,标题居中粗体显示
            Iterator titleIter = keyMap.entrySet().iterator();
            int titleIndex = 0;
            while (titleIter.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) titleIter.next();
                ws.addCell(new Label(titleIndex++, 0, entry.getValue(), titleFormat));
            }

            // 设置正文内容
            for (int i = 0; i < listContent.size(); i++) {
                Iterator contentIter = keyMap.entrySet().iterator();
                int colIndex = 0;
                while (contentIter.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) contentIter.next();
                    String key = entry.getKey().toString();
                    Field field = listContent.get(i).getClass().getDeclaredField(key);
                    field.setAccessible(true);
                    Object content = field.get(listContent.get(i));
                    String value = "";
                    if (null != content) {
                        value = content.toString();
                    }
                    if (methodMap != null) {
                        String methodName = methodMap.get(key);
                        if (methodName != null) {
                            Method convertMethod = this.getClass().getDeclaredMethod(methodName, String.class);
                            value = (String) convertMethod.invoke(this, value);
                        }
                    }

                    ws.addCell(new Label(colIndex++, i + 1, value, contentCenterFormat));
                }

            }

            // 宽度自适应。能够根据内容增加宽度，但对中文的支持不好，如果内容中包含中文，会有部分内容被遮盖
            for (int i = 0; i < keyMap.size(); i++) {
                CellView cell = ws.getColumnView(i);
                cell.setAutosize(true);
                ws.setColumnView(i, cell);
            }


            // 写入Exel工作表
            workbook.write();

            // 关闭Excel工作薄对象
            workbook.close();

            // 关闭流
            os.flush();
            os.close();
            os = null;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            flag = 0;
            ex.printStackTrace();
        }
        return flag;
    }

    public  List<List<List<String>>> readExcel(File file)
            throws BiffException, IOException {
        Workbook workBook = Workbook.getWorkbook(file);

        if (workBook == null)
            return null;

        return getDataInWorkbook(workBook);
    }
    private  List<List<List<String>>> getDataInWorkbook(Workbook workBook) {
        // 获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了
        Sheet[] sheet = workBook.getSheets();

        List<List<List<String>>> dataList = new ArrayList<List<List<String>>>();

        if (sheet != null && sheet.length > 0) {
            // 对每个工作表进行循环
            for (int i = 0; i < sheet.length; i++) {
                List<List<String>> rowList = new ArrayList<List<String>>();
                // 得到当前工作表的行数
                int rowNum = sheet[i].getRows();
                int colNum = sheet[i].getColumns();
                for (int j = 0; j < rowNum; j++) {
                    // 得到当前行的所有单元格
                    Cell[] cells = sheet[i].getRow(j);
                    if (cells != null && cells.length > 0) {
                        List<String> cellList = new ArrayList<String>();
                        // 对每个单元格进行循环
                        for (int k = 0; k < colNum; k++) {
                            Cell cell = sheet[i].getCell(k, j);
                            String cellValue = "";
                            // 判断单元格的值是否是数字
                            if (cell.getType() == CellType.NUMBER) {
                                NumberCell numberCell = (NumberCell) cell;
                                double value = numberCell.getValue();
                                cellValue = decimalFormat.format(value);
                            } else if (cell.getType() == CellType.NUMBER_FORMULA
                                    || cell.getType() == CellType.STRING_FORMULA
                                    || cell.getType() == CellType.BOOLEAN_FORMULA
                                    || cell.getType() == CellType.DATE_FORMULA
                                    || cell.getType() == CellType.FORMULA_ERROR) {
                                FormulaCell nfc = (FormulaCell) cell;
                                cellValue = nfc.getContents();
                            } else {
                                // 读取当前单元格的值
                                cellValue = cell.getContents();
                                // 特殊字符处理
                                cellValue = excelCharaterDeal(cellValue);
                            }
                            // 去掉空格
                            cellList.add(cellValue.trim());
                        }
                        rowList.add(cellList);
                    }
                }
                dataList.add(rowList);
            }
        }
        // 最后关闭资源，释放内存
        workBook.close();

        return dataList;
    }

    /**
     * @Title: excelCharaterDeal
     * @Description: Excel特殊字符处理
     * @param str
     *            字符串
     * @return
     */
    private  String excelCharaterDeal(String str) {
        String[] val = { "-", "_", "/" };// 定义特殊字符
        for (String i : val) {
            str = toToken(str, i);
        }
        return str;
    }
    /**
     * @Title: toToken
     * @Description: 除去字符串中指定的分隔符
     * @param s
     *            字符串
     * @param val
     *            指定的分隔符
     * @return
     */
    private  String toToken(String s, String val) {
        if (s == null || s.trim().equals("")) {
            return s;
        }
        if (val == null || val.equals("")) {
            return s;
        }
        StringBuffer stringBuffer = new StringBuffer();
        String[] result = s.split(val);
        for (int x = 0; x < result.length; x++) {
            stringBuffer.append(" ").append(result[x]);
        }
        return stringBuffer.toString();

    }

/*    public static void main(String[] args) {
        List<PaymentPersonnel> list = new ArrayList<>();
        PaymentPersonnel p = new PaymentPersonnel();
        p.setAccountNo("11111");
        p.setName("张三");
        list.add(p);

        JxlExcelUtils jxlExcelUtils = new JxlExcelUtils();
        LinkedHashMap<String, String> keyMap = new LinkedHashMap<String, String>();
        keyMap.put("accountNo", "1");
        keyMap.put("name", "名字");

        Map<String, String> methodMap = new HashMap<String, String>();
        methodMap.put("accountNo", "cardTypeConverter");
        JxlExcelUtils util = new JxlExcelUtils();
        OutputStream out = null;
        try {
            out = new FileOutputStream("D://aa.xls");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        jxlExcelUtils.getExcelStream("人员列表", keyMap, list, out, methodMap);
    }*/

}
