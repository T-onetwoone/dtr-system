package cn.edu.mju.ccce.dtrsystem.bmo;

import cn.edu.mju.ccce.dtrsystem.bean.User;
import cn.edu.mju.ccce.dtrsystem.common.G;
import cn.edu.mju.ccce.dtrsystem.dao.UserDao;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.piccolo.util.DuplicateKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.bmo.FileBmoImpl<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-04-07 09:59<br>
 */
@Service("cn.edu.mju.ccce.dtrsystem.bmo.FileBmoImpl")
public class FileBmoImpl implements FileBmo {

    /**
     * 日志输出.
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("cn.edu.mju.ccce.dtrsystem.dao.UserDao")
    protected UserDao userDao;

    @Override
    public Map<String, Object> parseExcelFileByUser(InputStream excelFile, String fileName) {
        try {
            Map<String, Object> map = parseExcelFileByUser_0(excelFile, fileName);
            Map<String, Object> returnMap = G.bmo.returnMap(true, "ok");
            returnMap.put("ExcelStatus", map);
            return returnMap;
        } catch (Exception e) {
            log.error("Excel解析异常", e);
            return G.bmo.returnMap(false, "Excel解析异常");
        }
    }

    private Map<String, Object> parseExcelFileByUser_0(InputStream excelFile, String fileName) throws Exception {
        Workbook workbook = getWorkbook(excelFile, fileName);
        int nodone = 0;
        int done = 0;
//        User user = null;
        for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
            Sheet hssfSheet = workbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                Row hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    User user = new User();
                    String name = "";
                    String sex = "";
                    String type = "";
                    String phone = "";
                    String nbr = "";
                    try {
                        hssfRow.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                        phone = hssfRow.getCell(3).getStringCellValue();
                    } catch (Exception e) {
                        // 电话号码可为空
                        // ignore
                    }
                    try {
                        for (int i = 0; i <= 4; i++) {
                            hssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
                        }
                        name = hssfRow.getCell(0).getStringCellValue();
                        sex = hssfRow.getCell(1).getStringCellValue();
                        type = hssfRow.getCell(2).getStringCellValue();
                        nbr = hssfRow.getCell(4).getStringCellValue();
                        // NullPointerException 有时候会漏查，为保险起见在做一遍非空检查
                        if ((name.length() == 0) || (sex.length() == 0) || (type.length() == 0) || (nbr.length() == 0)) {
                            nodone += 1;
                            continue;
                        }
                    } catch (NullPointerException e) {
                        // 为空跳出
                        nodone += 1;
                        continue;
                    }
                    user.setUSER_NAME(name);
                    user.setUSER_SEX(sex);
                    user.setUSER_PASS("123456");
                    user.setUSER_STATUS(0);
                    user.setTYPE_NAME(type);
                    int typeID = userDao.selectUserTypeID(type);
                    user.setTYPE_ID(typeID);
                    user.setUSER_NBR(BigInteger.valueOf(Integer.valueOf(nbr)));
                    user.setCREAT_TIME(new Date());
                    if (!phone.isEmpty()) {
                        user.setUSER_PHONE(new BigInteger(phone));
                    }
                    try{
                        int i = userDao.createUser(user);
                        if (i > 0) {
                            done += 1;
                        } else {
                            nodone += 1;
                        }
                    }catch (Exception e){
                        // 此处异常为数据库异常，有插入失败、数据重复等问题，一旦报错便认为此条数据插入失败
                        // ignore
                        nodone += 1;
                    }
                }
            }
        }
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("done", done);
        returnMap.put("nodone", nodone);
        return returnMap;
    }

    private String getExcelType(String fileName) throws Exception {
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (".xls".equals(fileType)) {
            return ".xls";
        } else if (".xlsx".equals(fileType)) {
            return ".xlsx";
        } else {
            throw new Exception("请上传.xls/.xlsx格式文件！");
        }
    }

    private Workbook getWorkbook(InputStream in, String fileName) throws Exception {
        Workbook workbook = null;
        String fileType = getExcelType(fileName);
        if (".xls".equals(fileType)) {
            workbook = new HSSFWorkbook(in);
        } else if (".xlsx".equals(fileType)) {
            workbook = new XSSFWorkbook(in);
        } else {
            throw new Exception("请上传.xls/.xlsx格式文件！");
        }
        return workbook;
    }
}
