package com.core.work;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.apache.xmlbeans.impl.xb.xsdschema.All;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SunilDeP on 5/26/2017.
 */
public class WriteIntoExcel<T> {


    public void writeExcel(List list,  int sheetNo, XSSFWorkbook workbook) throws IOException {
        //XSSFWorkbook workbook = null;

        try {
            //POIFSFileSystem fs = new POIFSFileSystem(
               //     new FileInputStream("C:\\work\\QAWorkbookAutomation\\QA Workbook\\QA_Trendsheet_V1.xlsm"));
            //HSSFWorkbook wb = new  HSSFWorkbook(fs, true);
             /*FileInputStream fsIP = new FileInputStream(new File("C:\\work\\QAWorkbookAutomation\\QA Workbook\\QA_Trendsheet_V1.xlsm")); //Read the spreadsheet that needs to be updated

            try {

                workbook = new XSSFWorkbook(fsIP);
                fsIP.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
*/

            int rowCount;
            XSSFSheet sheet = workbook.getSheetAt(sheetNo);

            switch (sheetNo) {

                case 0:
                    rowCount = 0;
                    List<AllTrend_t> l1=new ArrayList(list);

                    for (AllTrend_t l : l1) {
                        Row row = sheet.getRow(++rowCount);
                        writeBookAllTrend(l, row, "stage");


                    }
                    break;

                case 1:
                    rowCount = 0;
                    List<AllTrend_t> l2=new ArrayList(list);

                    for (AllTrend_t l : l2) {
                        Row row = sheet.getRow(++rowCount);
                        writeBookAllTrend(l, row, "prod");

                    }
                    break;

                case 2:
                    rowCount = 0;
                    List<IBNRData_t> l3=new ArrayList(list);
                    for (IBNRData_t l : l3) {
                        Row row = sheet.getRow(++rowCount);
                        writeBookIBNR(l, row, "stage");


                    }
                    break;

                case 3:
                    rowCount = 0;
                    List<IBNRData_t> l4=new ArrayList(list);
                    for (IBNRData_t l : l4) {
                        Row row = sheet.getRow(++rowCount);
                        writeBookIBNR(l, row, "prod");


                    }
                    break;

                case 10:
                    rowCount = 2;

                    List<OtherChecks_t> l5=new ArrayList(list);
                    for (OtherChecks_t l : l5) {
                        Row row = sheet.getRow(++rowCount);
                        writeBookOtherChecks(l, row, "stage");


                    }
                    break;

                case 11:
                    rowCount = 2;

                    List<OtherChecks_t> l6=new ArrayList(list);
                    for (OtherChecks_t l : l6) {
                        Row row = sheet.getRow(++rowCount);
                        writeBookOtherChecks(l, row, "prod");


                    }
                    break;



            }

            workbook.setForceFormulaRecalculation(true);




        } catch (Exception e) {

            e.printStackTrace();
        }

    }


    private void writeBookAllTrend(AllTrend_t l, Row row, String s) {
        if (s.equalsIgnoreCase("stage")) {
            Cell cell = row.createCell(0);
            cell.setCellValue(l.getPopulation());

            cell = row.createCell(1);
            cell.setCellValue(l.getDisplayName());

            cell = row.createCell(2);
            cell.setCellValue(l.getCost_Basis());

            cell = row.createCell(3);
            cell.setCellValue(l.getPeriod());

            cell = row.createCell(4);
            cell.setCellValue(l.getIsCompleted());

            cell = row.createCell(5);
            cell.setCellValue(l.getTrend());

            cell = row.createCell(6);
            cell.setCellValue(l.getTrend_Order());

            cell = row.createCell(7);
            cell.setCellValue(l.getData());

        } else {

            Cell cell = row.createCell(1);
            cell.setCellValue(l.getPopulation());

            cell = row.createCell(2);
            cell.setCellValue(l.getDisplayName());

            cell = row.createCell(3);
            cell.setCellValue(l.getCost_Basis());

            cell = row.createCell(4);
            cell.setCellValue(l.getPeriod());

            cell = row.createCell(5);
            cell.setCellValue(l.getIsCompleted());

            cell = row.createCell(6);
            cell.setCellValue(l.getTrend());

            cell = row.createCell(7);
            cell.setCellValue(l.getTrend_Order());

            cell = row.createCell(8);
            cell.setCellValue(l.getData());

        }
    }

    private void writeBookIBNR(IBNRData_t l, Row row, String s) {
        if (s.equalsIgnoreCase("stage")) {
            Cell cell = row.createCell(0);
            cell.setCellValue(l.getPopulation());

            cell = row.createCell(1);
            cell.setCellValue(l.getDisplayName());

            cell = row.createCell(2);
            cell.setCellValue(l.getCost_Basis());

            cell = row.createCell(3);
            cell.setCellValue(l.getPeriod());

            cell = row.createCell(4);
            cell.setCellValue(l.getIncurrred_claims());

            cell = row.createCell(5);
            cell.setCellValue(l.getPaid_claims());


        } else {

            Cell cell = row.createCell(1);
            cell.setCellValue(l.getPopulation());

            cell = row.createCell(2);
            cell.setCellValue(l.getDisplayName());

            cell = row.createCell(3);
            cell.setCellValue(l.getCost_Basis());

            cell = row.createCell(4);
            cell.setCellValue(l.getPeriod());

            cell = row.createCell(5);
            cell.setCellValue(l.getIncurrred_claims());

            cell = row.createCell(6);
            cell.setCellValue(l.getPaid_claims());

        }

    }

    private void writeBookOtherChecks(OtherChecks_t l, Row row, String s) {
        if (s.equalsIgnoreCase("stage")) {
            Cell cell = row.createCell(1);
            cell.setCellValue(l.getPopulation());

            cell = row.createCell(2);
            cell.setCellValue(l.getDisplayName());

            cell = row.createCell(3);
            cell.setCellValue(l.getTabs());

            cell = row.createCell(4);
            cell.setCellValue(l.getChecks());

            cell = row.createCell(5);
            cell.setCellValue(l.getResults());


        } else {

            Cell cell = row.createCell(3);
            cell.setCellValue(l.getPopulation());

            cell = row.createCell(4);
            cell.setCellValue(l.getDisplayName());

            cell = row.createCell(5);
            cell.setCellValue(l.getTabs());

            cell = row.createCell(6);
            cell.setCellValue(l.getChecks());

            cell = row.createCell(7);
            cell.setCellValue(l.getResults());



        }


    /*private XSSFWorkbook getWorkbook(String excelFilePath){
        XSSFWorkbook workbook = null;


        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }*/

    }
}
