package oneapp.incture.workbox.pmc.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.inbox.entity.CollaborationDo;
import oneapp.incture.workbox.pmc.dto.CollaborationDto;
import oneapp.incture.workbox.pmc.dto.CollaborationMessagesDto;
import oneapp.incture.workbox.pmc.dto.CollaborationNotificationDto;
import oneapp.incture.workbox.pmc.dto.MessageDto;
import oneapp.incture.workbox.poadapter.dao.BaseDao;
import oneapp.incture.workbox.util.ExecutionFault;
import oneapp.incture.workbox.util.InvalidInputFault;
import oneapp.incture.workbox.util.NoResultFault;
import oneapp.incture.workbox.util.PMCConstant;
import oneapp.incture.workbox.util.ServicesUtil;

@Repository("CollaborationDao")
@Transactional
public class CollaborationDao extends BaseDao<CollaborationDo, CollaborationDto> {
	
	
	private static final Logger logger=LoggerFactory.getLogger(CollaborationDao.class);
	
	SimpleDateFormat df = new SimpleDateFormat(PMCConstant.DETAILDATE_AMPM_FORMATE);

	@Override
	protected CollaborationDo importDto(CollaborationDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		CollaborationDo entity = new CollaborationDo();
		if (!ServicesUtil.isEmpty(fromDto.getProcessId()))
			entity.setProcessId(fromDto.getProcessId());
		if (!ServicesUtil.isEmpty(fromDto.getEventId()))
			entity.setEventId(fromDto.getEventId());
		if (!ServicesUtil.isEmpty(fromDto.getUserId()))
			entity.setUserId(fromDto.getUserId());
		if (!ServicesUtil.isEmpty(fromDto.getUserDisplayName()))
			entity.setUserDisplayName(fromDto.getUserDisplayName());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedAt()))
			entity.setCreatedAt(fromDto.getCreatedAt());
		if (!ServicesUtil.isEmpty(fromDto.getMessage()))
			entity.setMessage(fromDto.getMessage());
		if (!ServicesUtil.isEmpty(fromDto.getChatId()))
			entity.setChatId(fromDto.getChatId());
		if (!ServicesUtil.isEmpty(fromDto.getChatDisplayName()))
			entity.setChatDisplayName(fromDto.getChatDisplayName());
		if (!ServicesUtil.isEmpty(fromDto.getTaggedUserId()))
			entity.setTaggedUserId(fromDto.getTaggedUserId());

		return entity;
	}

	@Override
	protected CollaborationDto exportDto(CollaborationDo entity) {

		CollaborationDto dto = new CollaborationDto();
		if (!ServicesUtil.isEmpty(entity.getProcessId()))
			dto.setProcessId(entity.getProcessId());
		if (!ServicesUtil.isEmpty(entity.getEventId()))
			dto.setEventId(entity.getEventId());
		if (!ServicesUtil.isEmpty(entity.getUserId()))
			dto.setUserId(entity.getUserId());
		if (!ServicesUtil.isEmpty(entity.getUserDisplayName()))
			dto.setUserDisplayName(entity.getUserDisplayName());
		if (!ServicesUtil.isEmpty(entity.getCreatedAt()))
			dto.setCreatedAt(entity.getCreatedAt());
		if (!ServicesUtil.isEmpty(entity.getMessage()))
			dto.setMessage(entity.getMessage());
		if (!ServicesUtil.isEmpty(entity.getChatId()))
			dto.setChatId(entity.getChatId());
		if (!ServicesUtil.isEmpty(entity.getChatDisplayName()))
			dto.setChatDisplayName(entity.getChatDisplayName());
		if (!ServicesUtil.isEmpty(entity.getTaggedUserId()))
			dto.setTaggedUserId(entity.getTaggedUserId());
		return dto;
	}

	@SuppressWarnings("unchecked")
	public String createCollaborationDetail(CollaborationDto dto) {
		if (!ServicesUtil.isEmpty(dto.getTaggedUserId())) {

			List<String> taggedUsers = new ArrayList<String>(Arrays.asList(dto.getTaggedUserId().split(",\\s")));
			String userList = "";
			String userArray = "";
			for (String str : taggedUsers) {
				logger.error("[PMC][CollaborationDao][createTaskOwner][userList]  : " + str);
				userList += "'" + str + "',";
			}
			userList = userList.substring(0, userList.length() - 1);
			logger.error("[PMC][CollaborationDao][createTaskOwner][userList]  : " + userList);
			String queryName = "SELECT p.taskOwnersDoPK.taskOwner FROM TaskOwnersDo p where p.taskOwnersDoPK.taskOwner in("
					+ userList + ") and p.taskOwnersDoPK.eventId='" + dto.getEventId() + "'";
			List<String> resultList = (List<String>) this.getSession().createQuery(queryName).list();
			logger.error("[PMC][CollaborationDao][createTaskOwner][QueryName]" + queryName);

			logger.error("[PMC][CollaborationDao][createTaskOwner][resultListQuery]  :" + resultList);

			for (String str : resultList) {
				taggedUsers.remove((String) str);

			}
			if (!taggedUsers.isEmpty()) {
				for (String st2 : taggedUsers) {
					userArray = userArray + st2 + ", ";
				}
				userArray = userArray.substring(0, userArray.length() - 2);
				dto.setTaggedUserId(userArray);
			} else {
				dto.setTaggedUserId(null);
			}
		}
		try {
			create(dto);
			logger.error("[PMC][CollaborationDao][createTaskOwner][success]" + dto.getTaggedUserId());
			return "SUCCESS";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[PMC][CollaborationDao][createTaskOwner][failed]" + e.getMessage());
		}
		return "FAILURE";
	}

	private List<MessageDto> getMessageDto(String eventId, String qryType) {
		List<MessageDto> listMessageDto = new ArrayList<MessageDto>();
		String getMessageQuery = "SELECT P.PROCESS_ID AS PROCESS_ID, " + "  P.EVENT_ID AS EVENT_ID, "
				+ "  P.CREATED_AT AS CREATED_AT, " + "  P.MESSAGE AS MESSAGE, " + "  P.USER_ID AS USER_ID, "
				+ "  P.USER_DISPLAY_NAME AS USER_DISPLAY_NAME, " + "  P.CHAT_DISPLAY_NAME AS CHAT_DISPLAY_NAME, "
				+ "  P.CHAT_ID AS CHAT_ID" + "  FROM COLLABORATION p ";

		if (qryType.equalsIgnoreCase("task")) {
			getMessageQuery += " WHERE P.EVENT_ID = '" + eventId + "' ";
		} else if (qryType.equalsIgnoreCase("process")) {
			getMessageQuery += " WHERE P.PROCESS_ID = '" + eventId + "' ";
		}
		getMessageQuery += " ORDER BY P.CREATED_AT";

		logger.error("[PMC][ColaaborationDao][getMessageDto][getMessageQuery]" + getMessageQuery);
		Query query = this.getSession().createSQLQuery(getMessageQuery);
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			for (Object[] obj : resultList) {
				MessageDto messageDto = new MessageDto();
				messageDto.setProcessId(ServicesUtil.isEmpty(obj[0]) ? "" : (String) obj[0]);
				messageDto.setEventId(ServicesUtil.isEmpty(obj[1]) ? "" : (String) obj[1]);
				messageDto.setCreatedAt(ServicesUtil.isEmpty(obj[2]) ? null : ServicesUtil.resultAsDate(obj[2]));
				messageDto.setCreatedAtDisplayDate(
						ServicesUtil.isEmpty(obj[2]) ? null : df.format(ServicesUtil.resultAsDate(obj[2])));
				messageDto.setMessage(ServicesUtil.isEmpty(obj[3]) ? "" : (String) obj[3]);
				messageDto.setUserId(ServicesUtil.isEmpty(obj[4]) ? "" : (String) obj[4]);
				messageDto.setUserDisplayName(ServicesUtil.isEmpty(obj[5]) ? "" : (String) obj[5]);
				messageDto.setChatDisplayName(ServicesUtil.isEmpty(obj[6]) ? "" : (String) obj[6]);
				messageDto.setChatId(ServicesUtil.isEmpty(obj[7]) ? "" : (String) obj[7]);
				listMessageDto.add(messageDto);
			}
		}
		return listMessageDto;
	}

	public List<CollaborationMessagesDto> getMessageDetails(String processId, String taskId) {
		List<CollaborationMessagesDto> listOfProcessMessageDto = new ArrayList<CollaborationMessagesDto>();
		CollaborationMessagesDto messageDto = new CollaborationMessagesDto();
		List<MessageDto> listMessageDto = new ArrayList<MessageDto>();
		if (!ServicesUtil.isEmpty(processId) && ServicesUtil.isEmpty(taskId)) {
			listMessageDto = this.getMessageDto(processId, "process");
			messageDto.setMessageDtos(listMessageDto);
		} else {
			if (!ServicesUtil.isEmpty(taskId)) {
				listMessageDto = this.getMessageDto(taskId, "task");
				messageDto.setMessageDtos(listMessageDto);
			}
		}
		listOfProcessMessageDto.add(messageDto);
		return listOfProcessMessageDto;
	}

	@SuppressWarnings("unchecked")
	public List<CollaborationMessagesDto> getMessageOfOwner(String userId) {
		List<CollaborationMessagesDto> listProcessTask = new ArrayList<CollaborationMessagesDto>();
		MessageDto messageDto = null;
		CollaborationMessagesDto processTask;
		String queryString = "SELECT p.MESSAGE AS MESSAGE , p.USER_DISPLAY_NAME AS USER_DISPLAY_NAME,p.CREATED_AT AS CREATED_AT,p.PROCESS_ID AS PROCESS_ID,p.EVENT_ID AS EVENT_ID FROM COLLABORATION p where (p.process_id in(select q.PROCESS_ID from collaboration q where q.TAGGED_USER_ID LIKE '%"
				+ userId
				+ "%') and p.EVENT_ID is null  )or (p.EVENT_ID in(select q.event_id from collaboration q where q.TAGGED_USER_ID LIKE '%"
				+ userId
				+ "%')) union SELECT c1.MESSAGE AS MESSAGE,c1.USER_DISPLAY_NAME AS USER_DISPLAY_NAME,c1.CREATED_AT AS CREATED_AT,c1.PROCESS_ID AS PROCESS_ID,c1.EVENT_ID AS EVENT_ID FROM COLLABORATION c1 join TASK_OWNERS o1 on c1.EVENT_ID=o1.EVENT_ID where o1.TASK_OWNER LIKE '%"
				+ userId + "%' order by CREATED_AT desc";
		logger.error("[PMC][collaborationDao][getMessageOfOwner][queryString]- " + queryString);
		Query query = this.getSession().createSQLQuery(queryString);
		String dateValue;
		List<Object[]> resultList = query.list();
		logger.error("[PMC][ColaaborationDao][getMessageOfOwner][count]" + resultList.size() + "[result list] : "
				+ resultList);
		Map<String, Integer> eventsDtoMap = new LinkedHashMap<String, Integer>();
		for (Object[] obj : resultList) {
			logger.error(
					"[PMC][ColaborationDao][getMessageOfOwner][count]" + obj[0] + obj[1] + obj[2] + obj[3] + obj[4]);

			messageDto = new MessageDto();
			messageDto.setMessage((String) obj[0]);
			messageDto.setUserDisplayName((String) obj[1]);
			messageDto.setCreatedAt(obj[2] == null ? null : ServicesUtil.resultAsDate(obj[2]));
			dateValue = obj[2] == null ? null : df.format(ServicesUtil.resultAsDate(obj[2]));
			messageDto.setCreatedAtDisplayDate(dateValue);
			messageDto.setProcessId((String) obj[3]);
			messageDto.setEventId((String) obj[4]);

			if (eventsDtoMap.containsKey(obj[4])) {
				listProcessTask.get(eventsDtoMap.get(obj[4])).getMessageDtos().add(messageDto);
				logger.error("[PMC][ColaborationDao][getMessageOfOwner][map] : " + eventsDtoMap.get(obj[4]));
			} else {
				List<MessageDto> listMessageDto = new ArrayList<MessageDto>();
				listMessageDto.add(messageDto);
				processTask = new CollaborationMessagesDto();
				processTask.setMessageDtos(listMessageDto);
				listProcessTask.add(processTask);
				logger.error("[PMC][ColaborationDao][getMessageOfOwner][listMessageDto] : " + listMessageDto);
				eventsDtoMap.put((String) obj[4], listProcessTask.size() - 1);

			}
		}
		logger.error("[PMC][ColaborationDao][getMessageOfOwner][eventDtoMap] : " + eventsDtoMap.size() + "and "
				+ eventsDtoMap.entrySet());
		return listProcessTask;

	}

	@SuppressWarnings("unchecked")
	public List<CollaborationMessagesDto> getAllDetailsOfProcessWithTask(String processId) {
		MessageDto messageDto = null;
		List<CollaborationMessagesDto> listProcessTask = new ArrayList<CollaborationMessagesDto>();
		CollaborationMessagesDto processTask;
		String queryName = "SELECT p.message,p.userDisplayName,p.createdAt,p.processId,p.eventId FROM CollaborationDo p "
				+ "WHERE p.processId ='" + processId + "' order by p.createdAt";
		logger.error("queryName - " + queryName);
		Query query = this.getSession().createQuery(queryName);
		List<Object[]> resultList = query.list();
		logger.error("[PMC][ColaaborationDao][getAllDetailsOfProcessWithTask][count]" + resultList.size()
				+ "[result list] : " + resultList);
		Map<String, Integer> eventsDtoMap = new LinkedHashMap<String, Integer>();
		for (Object[] obj : resultList) {

			messageDto = new MessageDto();
			messageDto.setMessage((String) obj[0]);
			messageDto.setUserDisplayName((String) obj[1]);
			messageDto.setCreatedAt((Date) obj[2]);
			messageDto.setProcessId((String) obj[3]);
			messageDto.setEventId((String) obj[4]);

			if (eventsDtoMap.containsKey(obj[4])) {
				listProcessTask.get(eventsDtoMap.get(obj[4])).getMessageDtos().add(messageDto);
				logger.error("[PMC][ColaborationDao][getAllDetailsOfProcessWithTask][eventId] : "
						+ eventsDtoMap.get(obj[4]));
			} else {
				List<MessageDto> listMessageDto = new ArrayList<MessageDto>();
				listMessageDto.add(messageDto);
				processTask = new CollaborationMessagesDto();
				processTask.setMessageDtos(listMessageDto);
				listProcessTask.add(processTask);
				logger.error(
						"[PMC][ColaborationDao][getAllDetailsOfProcessWithTask][eventDtoMap] : " + listMessageDto);
				eventsDtoMap.put((String) obj[4], listProcessTask.size() - 1);

			}
		}
		return listProcessTask;
	}

	@SuppressWarnings("unchecked")
	public List<CollaborationNotificationDto> getNotificationForOwner(String userId) {
		List<CollaborationNotificationDto> dtoList = new ArrayList<CollaborationNotificationDto>();
		CollaborationNotificationDto dto ;
		String queryString = "SELECT c.MESSAGE AS MESSAGE ,c.CREATED_AT AS CREATED_AT,c.PROCESS_ID AS PROCESS_ID ,c.EVENT_ID AS EVENT_ID ,d.SUBJECT AS TASK_SUBJECT FROM COLLABORATION c join TASK_EVENTS d on c.EVENT_ID=d.EVENT_ID where TAGGED_USER_ID LIKE '%"
				+ userId + "%' order by c.CREATED_AT desc";
		logger.error("[PMC][collaborationDao][getMessageOfOwner][queryString]- " + queryString);
		Query query = this.getSession().createSQLQuery(queryString);
		List<Object[]> resultList = query.list();
		logger.error("[PMC][ColaaborationDao][getMessageOfOwner][count]" + resultList.size() + "[result list] : "
				+ resultList);
		for (Object[] obj : resultList) {
			dto=new CollaborationNotificationDto();
			dto.setMessage((String) obj[0]);
			dto.setCreatedAt(ServicesUtil.isEmpty(obj[2]) ? null : df.format(ServicesUtil.resultAsDate(obj[1])));
			
			dto.setProcessId((String) obj[2]);
			dto.setEventId((String) obj[3]);
			dto.setTaskSubject((String) obj[4]);
			dtoList.add(dto);
		}

		return dtoList;

	}
}
