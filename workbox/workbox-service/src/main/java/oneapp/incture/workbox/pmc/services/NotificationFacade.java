package oneapp.incture.workbox.pmc.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.pmc.dto.MailRequestDto;
import oneapp.incture.workbox.pmc.dto.RemainderDto;
import oneapp.incture.workbox.pmc.dto.RemainderMailDto;
import oneapp.incture.workbox.pmc.wsdlconsumers.UMEManagementEngineConsumer;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;
import oneapp.incture.workbox.util.PMCConstant;
import oneapp.incture.workbox.util.ServicesUtil;

/**
 * Session Bean implementation class NotificationFacade
 */
@Service("NotificationFacade")
public class NotificationFacade implements NotificationFacadeLocal {

	
	private static final Logger logger=LoggerFactory.getLogger(NotificationFacade.class);
	
	/*@EJB
	UserManagementFacadeWsdlConsumerLocal umeConsumer;*/
	
	@Autowired
	private UMEManagementEngineConsumer umeConsumer ;
	
	@Autowired
	private SessionFactory sf;
	
	
	
	
	
	@Override
	public ResponseMessage sendNotification(MailRequestDto requestDto) {
		MailService sendMail = new MailService();
		return sendMail.sendMail(requestDto);
	}

	
	@Override
	public ResponseMessage sendProcessRemainder() {
		ResponseMessage response = new ResponseMessage();
		String query = "SELECT Pe.Process_Id               AS PROCESS_ID, " +
				"  CAST(Pe.Started_At AS TIMESTAMP) AS STARTED_AT, " +
				"  Pct.Sla                          AS SLA, " +
				"  Rt.Threshold_Limit               AS THRESHOLD, " +
				"  Rt.Action                        AS ACTION, " +
				"  Rt.To_Email                      AS EMAIL_ID, " +
				"  Rt.To_Email_Subject              AS EMAIL_SUB, " +
				"  Rt.To_Email_Body                 AS EMAIL_BODY, " +
				"  Pe.Name                          AS PROCESS_NAME, " +
				"  Pe.STARTED_BY                    AS STARTED_BY " +
				" FROM Process_Config_Tb pct, " +
				"  Process_Events pe, " +
				"  Rule_Tb Rt " +
				"WHERE Pe.Name = Rt.Process_Name " +
				"AND Pe.Name   = Pct.Process_Name " +
				"AND pe.Status = 'IN_PROGRESS' " +
				"AND Rt.Type   = 'PROCESS' " +
				"AND Pct.Sla  IS NOT NULL";

		logger.error("[PMC][sendProcessRemainder][getProcessRemainderDetails : Query] " + query);
		Query qry =sf.openSession().createSQLQuery(query);
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = qry.list();
		DateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm");
		if (!ServicesUtil.isEmpty(resultList)) {
			for (Object[] obj : resultList) {
				
				RemainderDto rDto = new RemainderDto();
				
				rDto.setId(obj[0] == null ? null : (String) obj[0]);
				rDto.setStartedAt(obj[1] == null ? null : df.format(ServicesUtil.resultAsDate(obj[1])));
				rDto.setSla(obj[2] == null ? null : (String) obj[2]);
				rDto.setThreshold(obj[3] == null ? null : (String) obj[3]);
				rDto.setAction(obj[4] == null ? null : (String) obj[4]);
				rDto.setEmailIds(obj[5] == null ? null : (String) obj[5]);
				rDto.setEmailSub(obj[6] == null ? null : (String) obj[6]);
				rDto.setEmailBody(obj[7] == null ? null : (String) obj[7]);
				rDto.setJobName(obj[8] == null ? null : (String) obj[8]);
				rDto.setUserId(obj[9] == null ? null : (String) obj[9]);
				logger.error("[PMC][sendProcessRemainder][RemainderDtos] " + rDto.toString());
				
				//Date createdAt = null;
				Date currDateTime = null;
				Calendar createdAtCal = null;
				Calendar completedAtCal = null;
				Date toBeCompletedAt = new Date();
				Date notifyBy = new Date();
				DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm");
				try {
					//createdAt = dateFormat.parse(rDto.getStartedAt());
					currDateTime = dateFormat.parse(dateFormat.format(new Date()));
				} catch (ParseException e) {
					e.printStackTrace();
				}

				createdAtCal = Calendar.getInstance();
				try {
					if(!ServicesUtil.isEmpty(rDto.getStartedAt())){
						createdAtCal.setTime(df.parse(rDto.getStartedAt()));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(!ServicesUtil.isEmpty(rDto.getSla()) && !ServicesUtil.isEmpty(rDto.getThreshold())){
					toBeCompletedAt.setTime((ServicesUtil.getSLADueDate(createdAtCal, rDto.getSla())).getTimeInMillis());
					completedAtCal = Calendar.getInstance();
					completedAtCal.setTime(toBeCompletedAt);
					notifyBy.setTime((ServicesUtil.getNotifyByDate(completedAtCal, rDto.getThreshold())).getTimeInMillis());
					logger.error("[PMC][sendProcessRemainder][ProcessDetails] "+rDto.getId() +", "+toBeCompletedAt +", "+notifyBy);
					if (notifyBy.compareTo(currDateTime) == 0) {
						// mail sending code
						logger.error("[PMC][sendProcessRemainder][details][comparison_success] "+rDto.getId() );
						this.sendProcRemMail(rDto);
					}
					response.setMessage("Process : " + rDto.getJobName() + " Sla : " + rDto.getSla() + " Threshold : "
							+ rDto.getThreshold() + " Notify By : " + notifyBy);
					response.setStatus("Remainder Sent");
					response.setStatusCode("1");
				} else {
					response.setMessage("Sla or Threshold is Empty");
					response.setStatus("Remainder Not Sent");
					response.setStatusCode("0");
				}
			}
		}
		return response;
	}
	
	private ResponseMessage sendProcRemMail(RemainderDto dto) {
		logger.error("[PMC][sendProcRemMail][into_mail] "+dto.getId());
		umeConsumer = new UMEManagementEngineConsumer();
		MailService mailer = null;
		RemainderMailDto rmDto = null;
		String emailSubject = "";
		String emailBody = "";
		//if(dto.getAction().equalsIgnoreCase("Send Notification")){
		if(!ServicesUtil.isEmpty(dto.getAction())){
			if(PMCConstant.SEND_NOTIFICATION.equalsIgnoreCase(dto.getAction())){
				mailer = new MailService();
				rmDto = new RemainderMailDto();
				String emailIds = (dto.getEmailIds());
				String emailOfStartedBy = umeConsumer.getUserEmailByuserId(dto.getUserId());
				if(!ServicesUtil.isEmpty(emailOfStartedBy) && !ServicesUtil.isEmpty(dto.getEmailIds())){
					emailIds = emailIds + ", " + emailOfStartedBy;
				} else {
					emailIds = emailOfStartedBy;
				}
				if(!ServicesUtil.isEmpty(dto.getEmailSub())){
					emailSubject = dto.getEmailSub();
				} else {
					emailSubject = "Notification for Process : "+dto.getJobName()+"";
				}
				if(!ServicesUtil.isEmpty(dto.getEmailBody())){
					emailBody = dto.getEmailBody();
				} else {
					emailBody = "You have "+dto.getThreshold()+" left to complete this Process";
				}
				rmDto = new RemainderMailDto();
				rmDto.setMailTo(emailIds);
				rmDto.setMailSubject(emailSubject);
				rmDto.setMailBody(emailBody);
				return mailer.sendGenMail(rmDto);
			} //else if(dto.getAction().equalsIgnoreCase("Remind Me")){
			else if(PMCConstant.REMIND_ME.equalsIgnoreCase(dto.getAction())){
				mailer = new MailService();
				rmDto = new RemainderMailDto();
				rmDto.setMailTo(dto.getEmailIds());
				rmDto.setMailSubject(dto.getEmailSub());
				rmDto.setMailBody(dto.getEmailBody());
				return mailer.sendGenMail(rmDto);
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@WebMethod(operationName = "sendTaskRemainder", exclude = false)
	@Override
	public ResponseMessage sendTaskRemainder() {
		ResponseMessage response = new ResponseMessage();

		String query = "SELECT Tev.Event_Id AS EVENT_ID, " +
				"  Rt.Task_Name AS TASK_NAME, " +
				"  Ts.Sla AS SLA, " +
				"  Rt.Threshold_Limit AS THRESHOLD_LIMIT, " +
				"  Rt.Action AS ACTION, " +
				"  Tev.Created_At AS CREATED_AT, " +
				"  Tev.Status AS STATUS," +
				"  Rt.To_Email AS EMAIL_ID, " +
				"  Rt.To_Email_Subject AS EMAIL_SUB, " +
				"  Rt.To_Email_Body AS EMAIL_BODY," +
				"  TEV.CUR_PROC AS CUR_PROC " +
				" FROM Rule_Tb rt, " +
				"  Task_Sla ts, " +
				"  Task_Events tev " +
				"WHERE rt.Type    = 'TASK' " +
				//"AND Tev.Name     = Rt.Task_Name " +
				"AND Rt.Task_Name = Ts.Task_Def " +
				"AND( Tev.Status = 'READY' OR Tev.Status = 'RESERVED')" +
				"AND Ts.Sla      IS NOT NULL";

		logger.error("[PMC][sendTaskRemainder][getTaskRemainderDetails : Query] " + query);
		Query qry = sf.openSession().createSQLQuery(query);
		List<Object[]> resultList = qry.list();
		DateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm");

		if (!ServicesUtil.isEmpty(resultList)) {
			for (Object[] obj : resultList) {
				RemainderDto rDto = new RemainderDto();
				rDto.setId(obj[0] == null ? null : (String) obj[0]);
				rDto.setJobName(obj[1] == null ? null : (String) obj[1]);
				rDto.setSla(obj[2] == null ? null : (String) obj[2]);
				rDto.setThreshold(obj[3] == null ? null : (String) obj[3]);
				rDto.setAction(obj[4] == null ? null : (String) obj[4]);
				rDto.setStartedAt(obj[5] == null ? null : df.format(ServicesUtil.resultAsDate(obj[5])));
				rDto.setStatus(obj[6] == null ? null : (String) obj[6]);
				rDto.setEmailIds(obj[7] == null ? null : (String) obj[7]);
				rDto.setEmailSub(obj[8] == null ? null : (String) obj[8]);
				rDto.setEmailBody(obj[9] == null ? null : (String) obj[9]);
				rDto.setUserId(obj[10] == null ? null : (String) obj[10]);
				logger.error("[PMC][sendTaskRemainder][RemainderDtos] " + rDto.toString());

				//Date createdAt = null;
				Date currDateTime = null;
				Date toBeCompletedAt = new Date();
				Date notifyBy = new Date();
				Calendar createdAtCal = null;
				Calendar completedAtCal = null;
				DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm");
				try {
					//createdAt = dateFormat.parse(rDto.getStartedAt());
					currDateTime = dateFormat.parse(dateFormat.format(new Date()));
				} catch (ParseException e) {
					e.printStackTrace();
				}

				createdAtCal = Calendar.getInstance();
				try {
					if(!ServicesUtil.isEmpty(rDto.getStartedAt())){
						createdAtCal.setTime(df.parse(rDto.getStartedAt()));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(!ServicesUtil.isEmpty(rDto.getSla()) && !ServicesUtil.isEmpty(rDto.getThreshold())){
					toBeCompletedAt.setTime((ServicesUtil.getSLADueDate(createdAtCal, rDto.getSla())).getTimeInMillis());
					completedAtCal = Calendar.getInstance();
					completedAtCal.setTime(toBeCompletedAt);
					notifyBy.setTime((ServicesUtil.getNotifyByDate(completedAtCal, rDto.getThreshold())).getTimeInMillis());

					logger.error("[PMC][sendTaskRemainder][TaskDetails] "+rDto.getId() +", "+toBeCompletedAt +", "+notifyBy + ", "+currDateTime);
					if (notifyBy.compareTo(currDateTime) == 0) {
						// mail sending code
						logger.error("[PMC][sendTaskRemainder][comparision_success]");
						this.sendTaskRemMail(rDto);
					}
					response.setMessage("Task : " + rDto.getJobName() + " Sla : " + rDto.getSla() + " Threshold : "
							+ rDto.getThreshold() + " Notify By : " + notifyBy);
					response.setStatus("Remainder Sent");
					response.setStatusCode("1");
				} else {
					response.setMessage("Sla or Threshold is Empty");
					response.setStatus("Remainder Not Sent");
					response.setStatusCode("0");
				}
			}
		}
		return response;
	}
	
	private ResponseMessage sendTaskRemMail(RemainderDto dto) {
		MailService mailer = null;
		RemainderMailDto rmDto = null;
		String emailSubject = "";
		String emailBody = "";
		if(!ServicesUtil.isEmpty(dto.getAction())){
			if(dto.getStatus().equalsIgnoreCase("READY")){
				//if(dto.getAction().equalsIgnoreCase("Send Notification")){
				if(PMCConstant.SEND_NOTIFICATION.equalsIgnoreCase(dto.getAction())){
					mailer = new MailService();
					rmDto = new RemainderMailDto();
					String eventId = dto.getId();
					
					String query = "SELECT TASK_OWNER_EMAIL AS EMAILS FROM TASK_OWNERS WHERE EVENT_ID = '"+eventId+"'";
					logger.error("[PMC][sendTaskRemMail][getTaskOwnersEmails : Query] "+query);
					Query qry = sf.openSession().createSQLQuery(query);
					@SuppressWarnings("unchecked")
					List<Object> emailsList = qry.list();
					List<String> emailsString = new ArrayList<String>();
					
					for(Object obj : emailsList){
						if(!ServicesUtil.isEmpty(obj)){
							emailsString.add((String) obj);
						}
					}
					if(!ServicesUtil.isEmpty(emailsString)){
						String emailIds = String.join(",", emailsString);
						if(!ServicesUtil.isEmpty(dto.getEmailSub())){
							emailSubject = dto.getEmailSub();
						} else {
							emailSubject = "Notification for Task : "+dto.getJobName();
						}
						if(!ServicesUtil.isEmpty(dto.getEmailBody())){
							emailBody = dto.getEmailBody();
						} else {
							emailBody = "You have "+dto.getThreshold()+" left to complete this Task";
						}
						
						rmDto = new RemainderMailDto();
						
						rmDto.setMailTo(emailIds);
						rmDto.setMailSubject(emailSubject);
						rmDto.setMailBody(emailBody);
						
						return mailer.sendGenMail(rmDto);
					} else {
						rmDto = new RemainderMailDto();
						
						rmDto.setMailTo(dto.getEmailIds());
						rmDto.setMailSubject(dto.getEmailSub());
						rmDto.setMailBody(dto.getEmailBody());
					}
				} //else if(dto.getAction().equalsIgnoreCase("Remind Me")){
				else if(PMCConstant.REMIND_ME.equalsIgnoreCase(dto.getAction())){
					mailer = new MailService();
					rmDto = new RemainderMailDto();
					rmDto.setMailTo(dto.getEmailIds());
					rmDto.setMailSubject(dto.getEmailSub());
					rmDto.setMailBody(dto.getEmailBody());
					return mailer.sendGenMail(rmDto);
				}
			} else if(dto.getStatus().equalsIgnoreCase("RESERVED")){
				//if(dto.getAction().equalsIgnoreCase("Send Notification")){
				if(PMCConstant.SEND_NOTIFICATION.equalsIgnoreCase(dto.getAction())){
					umeConsumer = new UMEManagementEngineConsumer();
					String emailIds= "";
					String ownerEmail = umeConsumer.getUserEmailByuserId(dto.getUserId());
					mailer = new MailService();
					rmDto = new RemainderMailDto();
					if(!ServicesUtil.isEmpty(dto.getEmailIds())){
						emailIds = ownerEmail;
					} else {
						emailIds = dto.getEmailIds() +", "+ownerEmail;
					}
					if(ServicesUtil.isEmpty(dto.getEmailSub())){
						emailSubject = "Notification for Task : "+dto.getJobName();
					} else {
						emailSubject = dto.getEmailSub();
					}
					if(ServicesUtil.isEmpty(dto.getEmailBody())){
						emailBody = "You have "+dto.getThreshold()+" left to complete this Task";
					} else {
						emailBody = dto.getEmailBody();
					}
					
					rmDto.setMailTo(emailIds);
					rmDto.setMailSubject(emailSubject);
					rmDto.setMailBody(emailBody);
					
					return mailer.sendGenMail(rmDto);
					
				} //else if(dto.getAction().equalsIgnoreCase("Remind Me")){
				else if(PMCConstant.REMIND_ME.equalsIgnoreCase(dto.getAction())){
					mailer = new MailService();
					rmDto = new RemainderMailDto();
					rmDto.setMailTo(dto.getEmailIds());
					rmDto.setMailSubject(dto.getEmailSub());
					rmDto.setMailBody(dto.getEmailBody());
					return mailer.sendGenMail(rmDto);
				}
			}
		}
		return null;
	}
	
}
