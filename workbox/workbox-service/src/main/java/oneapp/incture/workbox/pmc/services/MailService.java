package oneapp.incture.workbox.pmc.services;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.pmc.dto.MailRequestDto;
import oneapp.incture.workbox.pmc.dto.RemainderMailDto;
import oneapp.incture.workbox.pmc.entity.SlaManagementDo;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;
import oneapp.incture.workbox.util.ServicesUtil;

@Service("MailService")
public class MailService {
	
	private static final Logger logger=LoggerFactory.getLogger(MailService.class);

	EntityManager em;
	
	/*private SessionFactory sessionFactory;*/

	public MailService(EntityManager em) {
		logger.error("Const");
		this.em = em;
	}
	public MailService() {
	}

	public ResponseMessage sendMail(MailRequestDto requestDto) {
		logger.error(" *** sendMail started >: RequestDto " + requestDto);
		final String username = "incturepmcmail@gmail.com";
		final String password = "Incture123";
		final String mailFrom = "incturepmcmail@gmail.com";
		ResponseMessage response = new ResponseMessage();
		//String response = null;
		if (!ServicesUtil.isEmpty(requestDto) && !ServicesUtil.isEmpty(requestDto.getMailTo())) {
			logger.error(" *** sendMail started >: RequestDto MailTo " + requestDto.getMailTo());
			String mailTo = requestDto.getMailTo();
			String mailSubject = null;
			String mailBody = null;
			String sla = null;
			String urgentSla = null;
			String processName = null;
			if (!ServicesUtil.isEmpty(requestDto.getSlaId())) {
				logger.error(" *** sendMail started >: requestDto.getSlaId() " + requestDto.getSlaId());
				SlaManagementDo slaManagementDO = new SlaManagementDo();
				slaManagementDO = em.find(SlaManagementDo.class, requestDto.getSlaId());
				if (!ServicesUtil.isEmpty(slaManagementDO)) {
					mailSubject = slaManagementDO.getSubject();
					mailBody = slaManagementDO.getDescription();
					sla = slaManagementDO.getSla();
					urgentSla = slaManagementDO.getUrgentSla();
					processName = slaManagementDO.getProcessName();
				}
			} else {
				logger.error(" *** sendMail started >: mailBody " + requestDto.getMailBody() + " mailSubject   " + requestDto.getMailSubject());

				mailBody = requestDto.getMailBody();
				mailSubject = requestDto.getMailSubject();
			}
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");

			Session session = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			try {
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(mailFrom));
				message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
				message.setSubject(mailSubject);
				StringBuffer messageBody = new StringBuffer();
				messageBody.append(ServicesUtil.isEmpty(processName) ? "" : "Process Name : " + processName + "\n");
				messageBody.append(ServicesUtil.isEmpty(mailBody) ? "" : mailBody + "\n");
				messageBody.append(ServicesUtil.isEmpty(sla) ? "" : "SLA : " + sla + "\n");
				messageBody.append(ServicesUtil.isEmpty(urgentSla) ? "" : "Urgent SLA : " + urgentSla + "\n");
				message.setText(messageBody.toString());
				Transport.send(message);
				response.setStatusCode("0");
				response.setStatus("Success");
				response.setMessage("Mail Sent Successfully!");
				
			} catch (MessagingException ex) {
				response.setStatusCode("1");
				response.setStatus("Failure");
				response.setMessage("Mail Not Sent, Exception "+ex.getMessage());
				ex.printStackTrace();
			}
		} else {
			response.setStatusCode("1");
			response.setStatus("Failure");
			response.setMessage("Request Dto Or MailTo is NULL");
		}
		return response;
	}
	
	public ResponseMessage sendGenMail(RemainderMailDto remainderDto) {
		logger.error(" *** sendMail started >: RequestDto " + remainderDto);
		final String username = "incturepmcmail@gmail.com";
		final String password = "Incture123";
		final String mailFrom = "incturepmcmail@gmail.com";
		ResponseMessage response = new ResponseMessage();
		//String response = null;
		if (!ServicesUtil.isEmpty(remainderDto) && !ServicesUtil.isEmpty(remainderDto.getMailTo())) {
			logger.error(" *** sendMail started >: RequestDto MailTo " + remainderDto.getMailTo());
			String mailTo = remainderDto.getMailTo();
			String mailSubject = remainderDto.getMailSubject();
			String mailBody = remainderDto.getMailBody();

			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");

			Session session = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			try {
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(mailFrom));
				message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
				message.setSubject(mailSubject);
				message.setText(mailBody);
				Transport.send(message);
				response.setStatusCode("0");
				response.setStatus("Success");
				response.setMessage("Mail Sent Successfully!");
				
			} catch (MessagingException ex) {
				response.setStatusCode("1");
				response.setStatus("Failure");
				response.setMessage("Mail Not Sent, Exception "+ex.getMessage());
				ex.printStackTrace();
			}
		} else {
			response.setStatusCode("1");
			response.setStatus("Failure");
			response.setMessage("Request Dto Or MailTo is NULL");
		}
		return response;
	}
	
}
