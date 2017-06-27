package com.core.work;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import com.core.work.IndexedThread;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * Created by SunilDeP on 6/21/2017.
 */
public class TestMail {


    public static void main(String a[]) throws IOException, MessagingException {
        boolean flag=false;
        File dir = new File("C:\\work\\QAWorkbookAutomation\\QA_Workbook\\new");
        while (flag==false) {
            for (File file : dir.listFiles()) {
                if (file.getName().equalsIgnoreCase("Asante")) {
                    File dir1 = new File("C:\\work\\QAWorkbookAutomation\\QA_Workbook\\new\\" + file.getName());
                    while (flag==false) {
                        for (File file1 : dir1.listFiles()) {

                            if (file1.getName().equalsIgnoreCase("Asante_06_26_2017_Trendsheet.xlsm")) {
                                flag=compareAndSendMail(file1);

                            }
                        }

                    }


                }
            }
        }



    }

    public static boolean compareAndSendMail(File file) throws IOException, MessagingException {

        boolean flag=false;
        List<MailData> mailDataList = new ArrayList<MailData>();
        MailData mailData = null;

        try {

            FileInputStream ios = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(ios);
            XSSFSheet sheet = workbook.getSheetAt(1);
            Iterator<Row> rowIterator = sheet.iterator();
            // List<Integer> columndata = new ArrayList<Integer>();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                if (row.getRowNum() > 0 && row != null) { /////////////// For excluding Headers

                    Cell cell1 = row.getCell(12);
                    if (cell1.getRichStringCellValue().getString().trim().equalsIgnoreCase("CRITICAL")
                            || cell1.getRichStringCellValue().getString().trim().equalsIgnoreCase("CAUTIONARY")
                            || cell1.getRichStringCellValue().getString().trim().equalsIgnoreCase("TRIVIAL")) {

                        Cell cell = cellIterator.next();

                        mailData = new MailData(row.getCell(0).getRichStringCellValue().getString(),
                                row.getCell(2).getRichStringCellValue().getString(), row.getCell(3).getRichStringCellValue().toString(),
                                row.getCell(5).getRichStringCellValue().getString(), row.getCell(7).getNumericCellValue(),
                                row.getCell(8).getNumericCellValue(), row.getCell(9).getNumericCellValue(),
                                row.getCell(10).getNumericCellValue(), row.getCell(11).getNumericCellValue(),
                                row.getCell(12).getRichStringCellValue().getString());
                        mailDataList.add(mailData);

                    }
                }
            }

            ios.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, SubSummary> map= new HashMap<String, SubSummary>();
        int i=0, j=0, k=0;
        List<ResultWise> resultList= new ArrayList<ResultWise>();

        for (MailData m:mailDataList) {
            ResultWise resultWise = new ResultWise(m.getTrend(), 0, 0, 0);
            int index = resultList.indexOf(new ResultWise(m.getTrend(), 0, 0, 0));
            if (index >= 0) {
                resultWise = resultList.get(index);
            } else {
                resultList.add(resultWise);
            }

            switch (m.getResult()) {
                case "CRITICAL":
                    resultWise.setCritical(resultWise.getCritical() + 1);
                    break;
                case "TRIVIAL":
                    resultWise.setTrivial(resultWise.getTrivial() + 1);
                    break;
                case "CAUTIONARY":
                    resultWise.setCautionay(resultWise.getCautionay() + 1);
                    break;
            }
        }


           /* String s;
            String s2;
            if ((s2=(s=m.getTrend().replaceAll("\\s","")).replaceAll("-","")).equalsIgnoreCase("MedicalPMPM")){

                list.get(0).setA("MedicalPMPM");
                if (m.getResult().equalsIgnoreCase("Critical")){

                    i=list.get(0).getCritical();
                    list.get(0).setCritical(++i);
                }
                if (m.getResult().equalsIgnoreCase("Trivial")){

                    j=list.get(0).getTrivial();
                    list.get(0).setTrivial(++j);
                }
                if (m.getResult().equalsIgnoreCase("Cautionary")){

                    k=list.get(0).getCautionay();
                    list.get(0).setCautionay(++k);
                }*/
            // }



        String msg=createEmailMessage(mailDataList,resultList);
        Mailer mail= new Mailer();
        flag=mail.send("parikshitd92@gmail.com","Parik@00","parikshitd92@outlook.com","Helllooooooo",msg);
        return flag;

    }

    public static void sendEmail(String emailContent) throws MessagingException {
        // Set up the SMTP server.
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.live.com");
        Session session = Session.getDefaultInstance(props, null);
        // Construct the message
        FileInputStream inputStream = null;
        /*try {
            inputStream = new FileInputStream("C:\\work\\QAWorkbookAutomation\\config.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        /*if (inputStream != null) {
            try {
                props.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        String to = "parikshitd92@outlook.com";
        String from = "parikshitd92@outlook.com";
        String subject = "Files Moved to SFTP";
        Message msg = new MimeMessage(session);
        try {
            InternetAddress[] arrayTo = InternetAddress.parse(to);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, arrayTo);
            msg.setSubject(subject);
            msg.setText(emailContent);
            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public static String createEmailMessage(List<MailData> list, List<ResultWise> resultList) {


        StringBuilder msg = new StringBuilder();
        Mailer mailContent= new Mailer();
        String s=mailContent.mailMatter(list,resultList);
        msg.append(s);
        System.out.println(msg.toString());
        return msg.toString();
    }


}
