package oneapp.incture.workbox.poadapter.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.pmc.dto.ActionDto;
import oneapp.incture.workbox.pmc.dto.RuleManagementDto;
import oneapp.incture.workbox.pmc.dto.TaskNameDto;
import oneapp.incture.workbox.pmc.entity.RuleManagementDo;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;
import oneapp.incture.workbox.util.ExecutionFault;
import oneapp.incture.workbox.util.InvalidInputFault;
import oneapp.incture.workbox.util.NoResultFault;
import oneapp.incture.workbox.util.PMCConstant;
import oneapp.incture.workbox.util.ServicesUtil;

@Repository("RuleManagementDao")
@Transactional
public class RuleManagementDao extends BaseDao<RuleManagementDo, RuleManagementDto> {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.incture.pmc.dao.BaseDao#importDto(com.incture.pmc.dto.BaseDto)
	 */
	
	private static final Logger logger=LoggerFactory.getLogger(RuleManagementDao.class);
	
	@Override
	protected RuleManagementDo importDto(RuleManagementDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		RuleManagementDo entity = new RuleManagementDo();
		if (!ServicesUtil.isEmpty(fromDto.getAction()))
			entity.setAction(fromDto.getAction());
		if (!ServicesUtil.isEmpty(fromDto.getName()))
			entity.setName(fromDto.getName());
		if (!ServicesUtil.isEmpty(fromDto.getStatus()))
			entity.setStatus(fromDto.getStatus());
		if (!ServicesUtil.isEmpty(fromDto.getType()))
			entity.setType(fromDto.getType());
		if (!ServicesUtil.isEmpty(fromDto.getTaskName()))
			entity.setTaskName(fromDto.getTaskName());
		if (!ServicesUtil.isEmpty(fromDto.getThresholdLimitUnit())
				&& !ServicesUtil.isEmpty(fromDto.getThresholdLimitCount())) {
			entity.setThresholdLimit(fromDto.getThresholdLimitCount() + " " + fromDto.getThresholdLimitUnit());
		}
		if (!ServicesUtil.isEmpty(fromDto.getEmailIdList()))
			entity.setEmailIdList(fromDto.getEmailIdList());
		if (!ServicesUtil.isEmpty(fromDto.getEmailSubject()))
			entity.setEmailSubject(fromDto.getEmailSubject());
		if (!ServicesUtil.isEmpty(fromDto.getEmailBody()))
			entity.setEmailBody(fromDto.getEmailBody());
		return entity;
	}

	@Override
	protected RuleManagementDto exportDto(RuleManagementDo entity) {
		RuleManagementDto dto = new RuleManagementDto();
		String[] threshold;
		if (!ServicesUtil.isEmpty(entity.getRuleId()))
			dto.setRuleId(entity.getRuleId());
		if (!ServicesUtil.isEmpty(entity.getName()))
			dto.setName(entity.getName());
		if (!ServicesUtil.isEmpty(entity.getTaskName()))
			dto.setTaskName(entity.getTaskName());
		if (!ServicesUtil.isEmpty(entity.getAction())) {
			dto.setAction(entity.getAction());
			dto.setActionOld(entity.getAction());
		}
		if (!ServicesUtil.isEmpty(entity.getStatus())) {
			dto.setStatus(entity.getStatus());
			dto.setStatusOld(entity.getStatus());
		}
		if (!ServicesUtil.isEmpty(entity.getType()))
			dto.setType(entity.getType());
		if (!ServicesUtil.isEmpty(entity.getThresholdLimit())) {
			threshold = entity.getThresholdLimit().split("\\s");
			if (!ServicesUtil.isEmpty(threshold[1])) {
				dto.setThresholdLimitCount(threshold[0]);
				dto.setThresholdLimitUnit(threshold[1]);
				dto.setThresholdCountOld(threshold[0]);
				dto.setThresholdUnitOld(threshold[1]);
			}
		}
		if (!ServicesUtil.isEmpty(entity.getEmailIdList()))
			dto.setEmailIdList(entity.getEmailIdList());
		if (!ServicesUtil.isEmpty(entity.getEmailSubject()))
			dto.setEmailSubject(entity.getEmailSubject());
		if (!ServicesUtil.isEmpty(entity.getEmailBody()))
			dto.setEmailBody(entity.getEmailBody());
		return dto;
	}

	private RuleManagementDto exportObj(Object[] obj) {

		RuleManagementDto dto = new RuleManagementDto();
		dto.setRuleId((String) obj[0]);
		dto.setType((String) obj[1]);
		dto.setName((String) obj[2]);

		if (!ServicesUtil.isEmpty((String) obj[3])) {
			String[] s = ((String) obj[3]).split("\\s");
			if (!ServicesUtil.isEmpty(s[1])) {
				dto.setThresholdLimitCount(s[0]);
				dto.setThresholdLimitUnit(s[1]);
				dto.setThresholdCountOld(s[0]);
				dto.setThresholdUnitOld(s[1]);
			}
		}
		dto.setAction((String) obj[4]);
		dto.setStatus((String) obj[5]);
		dto.setTaskName((String) obj[6]);
		if (!ServicesUtil.isEmpty((String) obj[7])) {
			String[] t = ((String) obj[7]).split("\\s");
			if (!ServicesUtil.isEmpty(t[1])) {
				dto.setSlaUnit(t[1]);
				dto.setSlaCount(t[0]);
			}
		}
		if(dto.getType().equals("TASK")){
			if (!ServicesUtil.isEmpty(obj[8])) {
				dto.setSlaId((String) obj[8]);
			}

		}
		else{
			dto.setProcessDisplayName(!ServicesUtil.isEmpty(obj[8])?(String) obj[8]:null);
		}
		dto.setEmailIdList(!ServicesUtil.isEmpty(obj[9])?(String) obj[9]:null);
		dto.setEmailSubject(!ServicesUtil.isEmpty(obj[10])?(String) obj[10]:null);
		dto.setEmailBody(!ServicesUtil.isEmpty(obj[11])?(String) obj[11]:null);

		logger.error("[PMC][RuleManagementDao][getRules][exportObj][dto]   -   " + dto);
		return dto;
	}

	@SuppressWarnings("unchecked")
	public List<RuleManagementDto> getRules(String processName) {


		String taskQuery = "select r.RULE_ID AS RULE_ID ,r.TYPE AS TYPE,r.PROCESS_NAME AS PROCESS_NAME,r.THRESHOLD_LIMIT AS THRESHOLD_LIMIT,r.ACTION AS ACTION,r.STATUS AS STATUS,r.TASK_NAME AS TASK_NAME,s.SLA AS SLA,s.TASK_SLA_ID AS SLA_ID ,r.EMAIL_ID AS EMAIL_ID,r.EMAIL_SUBJECT AS EMAIL_SUBJECT,r.EMAIL_BODY AS EMAIL_BODY  from RULE_TB r inner join TASK_SLA s on r.TASK_NAME = s.TASK_DEF where r.PROCESS_NAME = '"
				+ processName + "' and r.TYPE = 'TASK'";
		logger.error("[PMC][RuleManagementDao][getRules][query]   -   " + taskQuery);
		List<Object[]> taskResultList = this.getSession().createSQLQuery(taskQuery).list();
		logger.error("[PMC][RuleManagementDao][getRules][taskResult][size]   -   " + taskResultList.size());
		String processQuery = "select r.RULE_ID AS RULE_ID ,r.TYPE AS TYPE,r.PROCESS_NAME AS PROCESS_NAME,r.THRESHOLD_LIMIT AS THRESHOLD_LIMIT,r.ACTION AS ACTION,r.STATUS AS STATUS,r.TASK_NAME AS TASK_NAME,p.SLA AS SLA,p.PROCESS_DISPLAY_NAME AS PROCESS_DISPLAY_NAME,r.EMAIL_ID AS EMAIL_ID,r.EMAIL_SUBJECT AS EMAIL_SUBJECT,r.EMAIL_BODY AS EMAIL_BODY   from RULE_TB r inner join PROCESS_CONFIG_TB p on r.PROCESS_NAME = p.PROCESS_NAME where p.PROCESS_NAME = '"
				+ processName + "' and r.TYPE = 'PROCESS'";
		logger.error("[PMC][RuleManagementDao][getRules][query]   -   " + processQuery);
		List<Object[]> processResultList = this.getSession().createSQLQuery(processQuery).list();
		logger.error(
				"[PMC][RuleManagementDao][getRules][processResultList][size]   -   " + processResultList.size());
		if (!ServicesUtil.isEmpty(processResultList)) {
			taskResultList.addAll(processResultList);
		}
		if (!ServicesUtil.isEmpty(taskResultList)) {
			List<RuleManagementDto> dtoList = new ArrayList<RuleManagementDto>();
			for (Object[] obj : taskResultList) {
				dtoList.add(this.exportObj(obj));
			}
			return dtoList;
		} else {
			return null;
		}
	}

	public ResponseMessage submitRules(List<RuleManagementDto> dtoList) {
		ResponseMessage response = new ResponseMessage();
		if (!ServicesUtil.isEmpty(dtoList)) {
			int i = 1;
			for (RuleManagementDto dto : dtoList) {
				if (dto.getChange().equals(PMCConstant.UPDATE)) {
					if (!ServicesUtil.isEmpty(dto.getRuleId())) {
						i = this.updateRule(dto);
					} else {
						response.setStatus(PMCConstant.STATUS_FAILURE);
						response.setMessage("Id missing to update");
						response.setStatusCode("1");
						break;
					}
				} else if (dto.getChange().equals(PMCConstant.CREATE)) {
					try {
						this.getSession().persist(this.importDto(dto));
					} catch (InvalidInputFault e) {
						response.setStatus(PMCConstant.STATUS_FAILURE);
						response.setMessage(e.getMessage());
						logger.error(e.getMessage());
						response.setStatusCode("1");
						break;
					} catch (ExecutionFault e) {
						response.setStatus(PMCConstant.STATUS_FAILURE);
						response.setMessage(e.getMessage());
						response.setStatusCode("1");
						logger.error(e.getMessage());
						break;
					} catch (NoResultFault e) {
						response.setStatus(PMCConstant.STATUS_FAILURE);
						response.setMessage(e.getMessage());
						response.setStatusCode("1");
						logger.error(e.getMessage());
						break;
					}
				} else if (dto.getChange().equals(PMCConstant.DELETE)) {

					if (!ServicesUtil.isEmpty(dto.getRuleId())) {
						i = this.deleteRule(dto.getRuleId());
					} else {
						response.setStatus(PMCConstant.STATUS_FAILURE);
						response.setMessage("Id missing to delete");
						response.setStatusCode("1");
						break;
					}

				}
				if (i == 0) {
					response.setStatus(PMCConstant.STATUS_FAILURE);
					response.setStatusCode("1");
					response.setMessage(PMCConstant.INTERNAL_ERROR + " " + dto.getRuleId());
					break;
				} else {
					response.setStatusCode("0");
					response.setStatus(PMCConstant.STATUS_SUCCESS);
				}
			}
		}
		return response;
	}

	private Integer deleteRule(String ruleId) {
		Query query = this.getSession().createQuery("delete from RuleManagementDo p where  p.ruleId =:id");
		query.setParameter("id", ruleId);
		return query.executeUpdate();
	}

	private Integer updateRule(RuleManagementDto dto) {
		Query query = this.getSession().createQuery(
				"update RuleManagementDo p set p.thresholdLimit = :threshold , p.action = :action , p.status = :status ,p.emailIdList =:emailIdList,p.emailSubject = :emailSubject,p.emailBody = :emailBody  where  p.ruleId =:id");
		query.setParameter("id", dto.getRuleId());
		if (!ServicesUtil.isEmpty(dto.getThresholdLimitUnit()) && !ServicesUtil.isEmpty(dto.getThresholdLimitCount())) {
			query.setParameter("threshold", dto.getThresholdLimitCount() + " " + dto.getThresholdLimitUnit());
		} else {
			query.setParameter("threshold", null);
		}
		query.setParameter("action", dto.getAction());
		query.setParameter("emailIdList", dto.getEmailIdList());
		query.setParameter("emailSubject", dto.getEmailSubject());
		query.setParameter("emailBody", dto.getEmailBody());
		query.setParameter("status", dto.getStatus());

		return query.executeUpdate();
	}

	//	private Integer updateRule(RuleManagementDto dto) {
	//		try {
	//			update(dto);
	//			return 1;
	//		} catch (Exception e) {
	//			logger.error("[PMC][RuleManagementDao][updateRule] failed because"+e.getMessage());
	//		} 
	//		return 0;
	//	}
	@SuppressWarnings("unchecked")
	public List<TaskNameDto> getTasks(String processName) {
		logger.error("[PMC][RuleManagementDao][getTasks] method invoked");
		try {
			Query query = this.getSession().createQuery(
					"select p.taskName, p.sla from SlaManagementDo p where  p.processName =:processName and upper(p.taskName) <> 'END' and upper(p.taskName) <> 'START'");
			query.setParameter("processName", processName);
			List<Object[]> list = (List<Object[]>) query.list();
			logger.error("[PMC][RuleManagementDao][getTasks] stringList query invoked");

			if (!ServicesUtil.isEmpty(list)) {
				List<TaskNameDto> doList = new ArrayList<TaskNameDto>();
				for (Object[] obj : list) {
					logger.error("[PMC][RuleManagementDao][getTasks][StringList]" + obj[0] + "," + obj[1]);
					TaskNameDto dto = new TaskNameDto();
					dto.setTaskName((String) obj[0]);
					if (!ServicesUtil.isEmpty((String) obj[1])) {
						String[] s = ((String) obj[1]).split("\\s");
						if (!ServicesUtil.isEmpty(s[1])) {
							dto.setSlaCount(s[0]);
							dto.setSlaUnit(s[1]);
						}
					}
					doList.add(dto);
					logger.error("[PMC][RuleManagementDao][getTasks][dto]" + obj);
				}
				return doList;
			}
			return null;
		} catch (Exception e) {
			logger.error("[PMC][RuleManagementDao][getTasks][error]\n" + e.getMessage());
			return null;
		}
	}

	public List<ActionDto> getActions() {

		List<ActionDto> actionList = new ArrayList<ActionDto>();
		ActionDto action1 = new ActionDto();
		ActionDto action2 = new ActionDto();
		ActionDto action3 = new ActionDto();
		action1.setAction("");
		actionList.add(action1);
		action2.setAction(PMCConstant.SEND_NOTIFICATION);
		actionList.add(action2);
		action3.setAction(PMCConstant.REMIND_ME);
		actionList.add(action3);
		return actionList;
	}
}
