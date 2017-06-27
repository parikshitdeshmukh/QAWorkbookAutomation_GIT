package com.core.work;

import com.sun.mail.smtp.SMTPTransport;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by SunilDeP on 6/22/2017.
 */
class Mailer{



    public static void main(String[] args) {
        //from,password,to,subject,message
        send("parikshitd92@gmail.com","Parik@00","parikshitd92@gmail.com","hello javatpoint","pop");
        //change from, password and to
    }

    public static boolean sendEmail(String emailContent, String sub) throws MessagingException {
        // Set up the SMTP server.
        boolean flag=false;
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.crimsonservices.com");
        Session session = Session.getDefaultInstance(props, null);
        // Construct the message
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream("X:\\CPRM\\Required_Backups\\QA_Workbook\\emailRecipients.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            try {
                props.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String to = props.getProperty("user");
       // String to = "sunildep@advisory.com";
        String from = "sunildep@advisory.com";
        String subject = sub;
        Message msg = new MimeMessage(session);
        try {
            InternetAddress[] arrayTo = InternetAddress.parse(to);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, arrayTo);
            msg.setSubject(subject);
            msg.setContent(emailContent, "text/html");
            Transport.send(msg);
            flag=true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean send(String from,String password,String to,String sub, String msg){


       /* PrintWriter out= new PrintWriter("text.html");
        out.println("<html><body>"
                + "<table style='border:2px solid black'>");
        out.println("<tr bgcolor=\"#33CC99\">");
        out.println("<td> ajkdjks </td>");
        out.println("<td> 1234 </td>");
        out.println("<tr>");
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
        out.close();*/



        boolean flag=false;
        //Get properties object
        Properties props = new Properties();
        final String from1=from;
        final String pass=password;
        props.put("mail.smtp.host", "smtp.gmail.com"); // for gmail use smtp.gmail.com
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from1,pass);
                    }
                });
        //compose message
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject(sub);
            message.setContent(msg, "text/html");
           // message.setText(msg.toString());
            //send message
            SMTPTransport t = (SMTPTransport)session.getTransport("smtps");
            t.connect("smtp.gmail.com", from, pass);
            t.send(message);
            flag=true;
            System.out.println("message sent successfully");
        } catch (MessagingException e) {throw new RuntimeException(e);}
        return flag;
    }

    public String mailMatter(List<MailData> l, List<ResultWise> resultList){
        StringBuilder msg= new StringBuilder();

        /*msg.append("<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "table, th, td {\n" +
                "    border: 1px solid black; \n" +
                "   border-collapse: collapse;\n" +
                "}\n" +
                "th, td {\n"+
                "    padding: 15px;\n"+
                "}\n"+
                "</style>\n" +
                "</head>     <body>"
                + "<table style=\"width:100%\" '>");

        msg.append("<tr style=\"background-color:skyblue;>");
        msg.append("<th> Trend </th>");
        msg.append("<th> Critical </th>");
        msg.append("<th> Trivial </th>");
        msg.append("<th> Cautionary </th> ");
        msg.append("</tr>");



        for (MailData content:l){
            msg.append("<tr bgcolor=\"white\">");
            msg.append("<td>"+content.getPopulation()+"</td>");
            msg.append("<td>"+content.getCost_Basis()+"</td>");
            msg.append("<td>"+content.getPeriod()+"</td>");
            msg.append("<td>"+content.getTrend()+"</td>");
            msg.append("<td>"+content.getStaging_Data()+"</td>");
            msg.append("<td>"+content.getProduction_Data()+"</td>");
            msg.append("<td>"+content.getPerc()+"</td>");
            msg.append("<td>"+content.getCritical()+"</td>");
            msg.append("<td>"+content.getTrivial()+"</td>");

            if (content.getResult().equalsIgnoreCase("Critical")){
                msg.append("<td>"+content.getResult()+"</td>");
            }
            msg.append("</tr>");
        }

        msg.append("</table>");*/

        msg.append("<html><body>");
        msg.append("<p><b> <font face=\"verdana\" size=\"2\" color=\"#009900\">Summary Report Table</font></b></p>");
        msg.append("<table bgcolor=\"#668cff\" border=\"1\" cellpadding=\"8\">");
        msg.append("<tr>");
        msg.append("<th> Trend </th>");
        msg.append("<th> Critical </th>");
        msg.append("<th> Trivial </th>");
        msg.append("<th> Cautionary </th> ");
        msg.append("</tr>");

        for (ResultWise content:resultList){
            msg.append("<tr bgcolor=\"white\">");
            msg.append("<td>"+content.getA()+"</td>");
            msg.append("<td>"+content.getCritical()+"</td>");
            msg.append("<td>"+content.getTrivial()+"</td>");
            msg.append("<td>"+content.getTrivial()+"</td>");
            msg.append("</tr>");
        }

        msg.append("</table>");

        msg.append("\n\n\n\n\n\n\n");

        msg.append("<p><b> <font face=\"verdana\" size=\"2\" color=\"#009900\">Report</font></b></p>");
        msg.append("<table bgcolor=\"#668cff\" border=\"1\" cellpadding=\"8\">");
        msg.append("<tr>");
        msg.append("<th> Population </th>");
        msg.append("<th> Cost_Basis </th>");
        msg.append("<th> Period </th> <th> Trend </th> <th> Staging_Data </th> <th> Production_Data </th> <th> Perc </th>");
        msg.append("<th> Critical </th> <th> Trivial </th> <th> Result </th> ");
        msg.append("</tr>");

      /*  msg.append("<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "table, th, td {\n" +
                "    border: 1px solid black; \n" +
                "   border-collapse: collapse;\n" +
                "}\n" +
                "th, td {\n"+
                "    padding: 15px;\n"+
                "}\n"+
                "</style>\n" +
                "</head>     <body>"
                + "<table style=\"width:100%\" '>");

        msg.append("<tr style=\"background-color:skyblue;>");
        msg.append("<th> Population </th>");
        msg.append("<th> Cost_Basis </th>");
        msg.append("<th> Period </th> <th> Trend </th> <th> Staging_Data </th> <th> Production_Data </th> <th> Perc </th>");
        msg.append("<th> Critical </th> <th> Trivial </th> <th> Result </th> ");
        msg.append("</tr>");*/



        for (MailData content:l){
            msg.append("<tr bgcolor=\"white\">");
            msg.append("<td>"+content.getPopulation()+"</td>");
            msg.append("<td>"+content.getCost_Basis()+"</td>");
            msg.append("<td>"+content.getPeriod()+"</td>");
            msg.append("<td>"+content.getTrend()+"</td>");
            msg.append("<td>"+content.getStaging_Data()+"</td>");
            msg.append("<td>"+content.getProduction_Data()+"</td>");
            msg.append("<td>"+content.getPerc()+"</td>");
            msg.append("<td>"+content.getCritical()+"</td>");
            msg.append("<td>"+content.getTrivial()+"</td>");

            if (content.getResult().equalsIgnoreCase("Critical")){
                msg.append("<td bgcolor=\"#ff3333\"> <font color=\"white\">"+content.getResult()+"</td>");
            }
            if (content.getResult().equalsIgnoreCase("Trivial")){
                msg.append("<td>"+content.getResult()+"</td>");
            }
            if (content.getResult().equalsIgnoreCase("CAUTIONARY")){
                msg.append("<td>"+content.getResult()+"</td>");
            }
            msg.append("</tr>");
        }

        msg.append("</table>");

        msg.append("<p> <font face=\"verdana\" size=\"2\" color=\"#990000\">Thank You!</font></b></p>");
        msg.append("<p> <font face=\"verdana\" size=\"2\" color=\"#990000\">CPRM Team</font></b></p>");
        msg.append("</body></html>");
        String s=msg.toString();
        return s;

    }
}