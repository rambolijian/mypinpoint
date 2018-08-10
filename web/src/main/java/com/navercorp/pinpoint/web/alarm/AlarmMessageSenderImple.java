package com.navercorp.pinpoint.web.alarm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.navercorp.pinpoint.web.alarm.checker.AlarmChecker;
import com.navercorp.pinpoint.web.service.UserGroupService;
import com.navercorp.pinpoint.web.alarm.AlarmMessageSender;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class AlarmMessageSenderImple implements AlarmMessageSender {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserGroupService userGroupService;
    
    @Override
    public void sendSms(AlarmChecker checker, int sequenceCount) {
        List<String> receivers = userGroupService.selectPhoneNumberOfMember(checker.getuserGroupId());

        if (receivers.size() == 0) {
            return;
        }

        for (Object message : checker.getSmsMessage()) {
        	
        	String msgStr = Objects.toString(message, "");
        	
            logger.info("send SMS : {}", msgStr);

            // TODO Implement logic for sending SMS
        }
    }

    @Override
    public void sendEmail(AlarmChecker checker, int sequenceCount) {
        List<String> receivers = userGroupService.selectEmailOfMember(checker.getuserGroupId());
        
        if (receivers.size() == 0) {
            return;
        }

//        for (String message : checker.getEmailMessage()) {
        String alarm_info = checker.getEmailMessage();
            
        logger.info("send email : {}", alarm_info);
        logger.info("email receivers: {}", receivers);
            // TODO Implement logic for sending email
//        }
        
        String emailContent = alarm_info + "Please refer to the <a href=\"http://192.168.13.122:7071\">Pinpoint</a>.<br>";
//        String to = "lijian@xxxxx.cn,luzhenyu@xxxxx.cn";
//        String from = "lijian@xxxxx.cn";
        String from = "sh-tech-qa@xxxxx.cn";
        
        
        InternetAddress[] address=null;
        try {
            List list = new ArrayList();
//            String []median=to.split(",");
//            for(int i=0;i<median.length;i++){
//                list.add(new InternetAddress(median[i]));
//            }
            for(int i=0;i<receivers.size();i++) {
            	list.add(new InternetAddress(receivers.get(i)));
            }
            address =(InternetAddress[])list.toArray(new InternetAddress[list.size()]);
            logger.info("email address: {}", address);
       } catch (AddressException e) {
    	   logger.info("AddressException....");
           e.printStackTrace();
       }
        
        try {
	        Properties props = new Properties();
	        props.setProperty("mail.debug", "true");
		   	props.setProperty("mail.transport.protocol", "smtp");
		   	props.setProperty("mail.host", "smtp.xxxxx.cn");
//		   	props.setProperty("mail.smtp.port", "25");
		   	props.setProperty("mail.smtp.auth", "true");
//		   	props.setProperty("mail.smtp.timeout","1000");
	           
	        Session session = Session.getInstance(props);  
	           
	        Message msg = new MimeMessage(session);  
	        msg.setSubject("**Pinpoint 监控告警邮件**");
	 
//	        msg.setText(emailContent);
	        msg.setContent(emailContent, "text/html;charset=utf-8");
	
	        msg.setFrom(new InternetAddress("Pinpoint"));  
	          
	        Transport transport = session.getTransport();  
	 
	        transport.connect(from, "hbtest!@#");  
	   
	        transport.sendMessage(msg, address);
	        transport.close();
	        logger.info("Sent message successfully....");
        }catch (MessagingException mex) {
           logger.info("Sent message MessagingException....");
           mex.printStackTrace();
        }
//   ######################################################################
   
//        try{
//        	Properties props = new Properties();
//	    	 props.setProperty("mail.transport.protocol", "SMTP");
//	    	 props.setProperty("mail.smtp.host", "smtp.xxxxx.cn");
//	    	 props.setProperty("mail.smtp.port", "25");
//	    	 props.setProperty("mail.smtp.auth", "true");
//	    	 props.setProperty("mail.smtp.timeout","1000");
//	    	 
//	    	 Authenticator auth = new Authenticator() {
//	    		 public PasswordAuthentication getPasswordAuthentication(){
//	    		    return new PasswordAuthentication(from, "HB006781");
//	    		            }
//	    		        };
//	    	 Session session = Session.getInstance(props, auth);
//	    	 Message message = new MimeMessage(session);
//	    	 message.setFrom(new InternetAddress(from));
//	    	 message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
//	    	 message.setSubject("**Pinpoint 监控告警邮件**");
//	    	 message.setContent(emailContent, "text/html;charset=utf-8");
//	    	 Transport.send(message);
//           logger.info("Sent message successfully....");
//        }catch (MessagingException mex) {
//           logger.info("Sent message MessagingException....");
//           mex.printStackTrace();
//        }
    }
}