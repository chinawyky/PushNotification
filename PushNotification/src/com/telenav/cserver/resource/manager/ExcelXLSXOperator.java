package com.telenav.cserver.resource.manager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;  
import java.io.FileNotFoundException;
import java.io.IOException;    
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import java.util.concurrent.ArrayBlockingQueue;

import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;  
import org.apache.poi.ss.usermodel.Sheet;  
  
import org.apache.poi.xssf.usermodel.XSSFWorkbook;  


  
public class ExcelXLSXOperator {
 

    public static void main(String[] args) throws Exception {

       File file = new File("E:/telenav/project/USCC_push/USCC.xlsx");

       String[][] result = getData(file, 1);

       int rowLength = result.length;

       for(int i=0;i<rowLength;i++) {

           for(int j=0;j<result[i].length;j++) {

              System.out.print(result[i][j]+"\t\t");

           }

           System.out.println();

       }
       
       System.out.println("OK");

      

    }
    
    public static ArrayBlockingQueue<String[]> getData2Buffer(File file, int ignoreRows)

            throws FileNotFoundException, IOException {

        List<String[]> result = new ArrayList<String[]>();

        int rowSize = 0;

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(

               file));

        

        //POIFSFileSystem fs = new POIFSFileSystem(in);

        XSSFWorkbook wb = new XSSFWorkbook(in);

        Cell cell = null;

        for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {

            Sheet st = wb.getSheetAt(sheetIndex);

           

            for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {

               Row row = st.getRow(rowIndex);

               if (row == null) {

                   continue;

               }

               int tempRowSize = row.getLastCellNum() + 1;

               if (tempRowSize > rowSize) {

                   rowSize = tempRowSize;

               }

               String[] values = new String[rowSize];

               Arrays.fill(values, "");

               boolean hasValue = false;

               for (int columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {

                   String value = "";

                   cell = row.getCell(columnIndex);

                   if (cell != null) {       

                      switch (cell.getCellType()) {

                      case Cell.CELL_TYPE_STRING:

                          value = cell.getStringCellValue();

                          break;

                      case Cell.CELL_TYPE_NUMERIC:

                          if (DateUtil.isCellDateFormatted(cell)) {

                             Date date = cell.getDateCellValue();

                             if (date != null) {

                                 value = new SimpleDateFormat("yyyy-MM-dd")

                                        .format(date);

                             } else {

                                 value = "";

                             }

                          } else {

                             value = new DecimalFormat("0").format(cell

                                    .getNumericCellValue());

                          }

                          break;

                      case Cell.CELL_TYPE_FORMULA:

                         

                          if (!cell.getStringCellValue().equals("")) {

                             value = cell.getStringCellValue();

                          } else {

                             value = cell.getNumericCellValue() + "";

                          }

                          break;

                      case Cell.CELL_TYPE_BLANK:

                          break;

                      case Cell.CELL_TYPE_ERROR:

                          value = "";

                          break;

                      case Cell.CELL_TYPE_BOOLEAN:

                          value = (cell.getBooleanCellValue() == true ? "Y"

                                 : "N");

                          break;

                      default:

                          value = "";

                      }

                   }

                   if (columnIndex == 0 && value.trim().equals("")) {

                      break;

                   }

                   values[columnIndex] = rightTrim(value);

                   hasValue = true;

               }

  

               if (hasValue) {

                   result.add(values);

               }

            }

        }

        in.close();
        
        ArrayBlockingQueue<String[]> origin = new ArrayBlockingQueue<String[]>(result.size());
        UserDataManager.setUserDataArrayOrigin(origin);
        ArrayBlockingQueue<String[]> buffer = new ArrayBlockingQueue<String[]>(result.size());
        UserDataManager.setUserDataArray(buffer);

        //String[][] returnArray = new String[result.size()][rowSize];

        for (int i = 0; i < result.size(); i++) 
        {
        	try
        	{
        		UserDataManager.getUserDataArrayOrigin().put(result.get(i));
        	} 
        	catch(InterruptedException e)
        	{
        		// loop again.
        		i--;
        		continue;
        	}
        }

        return UserDataManager.getUserDataArrayOrigin();

     }   

    public static String[][] getData(File file, int ignoreRows)

           throws FileNotFoundException, IOException {

       List<String[]> result = new ArrayList<String[]>();

       int rowSize = 0;

       BufferedInputStream in = new BufferedInputStream(new FileInputStream(

              file));

       

       //POIFSFileSystem fs = new POIFSFileSystem(in);

       XSSFWorkbook wb = new XSSFWorkbook(in);

       Cell cell = null;

       for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {

           Sheet st = wb.getSheetAt(sheetIndex);

          

           for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {

              Row row = st.getRow(rowIndex);

              if (row == null) {

                  continue;

              }

              int tempRowSize = row.getLastCellNum() + 1;

              if (tempRowSize > rowSize) {

                  rowSize = tempRowSize;

              }

              String[] values = new String[rowSize];

              Arrays.fill(values, "");

              boolean hasValue = false;

              for (int columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {

                  String value = "";

                  cell = row.getCell(columnIndex);

                  if (cell != null) {       

                     switch (cell.getCellType()) {

                     case Cell.CELL_TYPE_STRING:

                         value = cell.getStringCellValue();

                         break;

                     case Cell.CELL_TYPE_NUMERIC:

                         if (DateUtil.isCellDateFormatted(cell)) {

                            Date date = cell.getDateCellValue();

                            if (date != null) {

                                value = new SimpleDateFormat("yyyy-MM-dd")

                                       .format(date);

                            } else {

                                value = "";

                            }

                         } else {

                            value = new DecimalFormat("0").format(cell

                                   .getNumericCellValue());

                         }

                         break;

                     case Cell.CELL_TYPE_FORMULA:

                        

                         if (!cell.getStringCellValue().equals("")) {

                            value = cell.getStringCellValue();

                         } else {

                            value = cell.getNumericCellValue() + "";

                         }

                         break;

                     case Cell.CELL_TYPE_BLANK:

                         break;

                     case Cell.CELL_TYPE_ERROR:

                         value = "";

                         break;

                     case Cell.CELL_TYPE_BOOLEAN:

                         value = (cell.getBooleanCellValue() == true ? "Y"

                                : "N");

                         break;

                     default:

                         value = "";

                     }

                  }

                  if (columnIndex == 0 && value.trim().equals("")) {

                     break;

                  }

                  values[columnIndex] = rightTrim(value);

                  hasValue = true;

              }

 

              if (hasValue) {

                  result.add(values);

              }

           }

       }

       in.close();

       String[][] returnArray = new String[result.size()][rowSize];

       for (int i = 0; i < returnArray.length; i++) {

           returnArray[i] = (String[]) result.get(i);

       }

       return returnArray;

    }

   

     public static String rightTrim(String str) {

       if (str == null) {

           return "";

       }

       int length = str.length();

       for (int i = length - 1; i >= 0; i--) {

           if (str.charAt(i) != 0x20) {

              break;

           }

           length--;

       }

       return str.substring(0, length);

    }

}  