package com.core.work;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Properties;

import static java.awt.SystemColor.text;

/**
 * Created by SunilDeP on 5/23/2017.
 */
public class Student {


    public static void main(String[] arg) throws FileNotFoundException, MessagingException {




        String to = "parikshitd92@gmail.com";//change accordingly
        String from = "parikshitd92@gmail.com";
        String host = "localhost";//or IP address

        String msg="ahdsudsn&bdsjds_sds-sfh:dhksdjdsdsds";

        msg=msg.replaceAll("[^a-zA-Z0-9]", "");
        System.out.print(msg);


        //Get the session object
    }




}

