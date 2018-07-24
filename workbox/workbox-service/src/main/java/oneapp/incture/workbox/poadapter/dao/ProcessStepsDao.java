package oneapp.incture.workbox.poadapter.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.pmc.dto.ProcessStepsDto;
import oneapp.incture.workbox.pmc.entity.ProcessStepsDo;
import oneapp.incture.workbox.util.ExecutionFault;
import oneapp.incture.workbox.util.InvalidInputFault;
import oneapp.incture.workbox.util.NoResultFault;
import oneapp.incture.workbox.util.ServicesUtil;

@Repository("ProcessStepsDao")
@Transactional
public class ProcessStepsDao extends BaseDao<ProcessStepsDo, ProcessStepsDto> {
	
	@Override
	protected ProcessStepsDo importDto(ProcessStepsDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		ProcessStepsDo entity = new ProcessStepsDo();
		if (!ServicesUtil.isEmpty(fromDto.getProcessId()))
			entity.setProcessId(fromDto.getProcessId());
		if (!ServicesUtil.isEmpty(fromDto.getTaskStep()))
			entity.setTaskStep(fromDto.getTaskStep());
		if (!ServicesUtil.isEmpty(fromDto.getActivityType()))
			entity.setActivityType(fromDto.getActivityType());
		if (!ServicesUtil.isEmpty(fromDto.getProcessName()))
			entity.setProcessName(fromDto.getProcessName());
		if (!ServicesUtil.isEmpty(fromDto.getSubject()))
			entity.setSubject(fromDto.getSubject());
		if (!ServicesUtil.isEmpty(fromDto.getDescription()))
			entity.setDescription(fromDto.getDescription());
		return entity;
	}

	@Override
	protected ProcessStepsDto exportDto(ProcessStepsDo entity) {
		ProcessStepsDto processStepsDto = new ProcessStepsDto();
		if (!ServicesUtil.isEmpty(entity.getProcessId()))
			processStepsDto.setProcessId(entity.getProcessId());
		if (!ServicesUtil.isEmpty(entity.getTaskStep()))
			processStepsDto.setTaskStep(entity.getTaskStep());
		if (!ServicesUtil.isEmpty(entity.getActivityType()))
			processStepsDto.setActivityType(entity.getActivityType());
		if (!ServicesUtil.isEmpty(entity.getProcessName()))
			processStepsDto.setProcessName(entity.getProcessName());
		if (!ServicesUtil.isEmpty(entity.getSubject()))
			processStepsDto.setSubject(entity.getSubject());
		if (!ServicesUtil.isEmpty(entity.getDescription()))
			processStepsDto.setDescription(entity.getDescription());
		return processStepsDto;
	}

	public List<ProcessStepsDto> getProcessSteps(String processName) {
		List<ProcessStepsDto> processStepsDtos = null;
		try {
			processStepsDtos = getSpecificActiveResults("ProcessStepsDo", "processName", processName);
		} catch (NoResultFault e) {
			e.printStackTrace();
		}
		return processStepsDtos;

	}
}
