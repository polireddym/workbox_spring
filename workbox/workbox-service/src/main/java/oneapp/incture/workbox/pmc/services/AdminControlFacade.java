package oneapp.incture.workbox.pmc.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.pmc.dto.AdminControlDto;
import oneapp.incture.workbox.pmc.dto.AgingRangeConfigDto;
import oneapp.incture.workbox.pmc.dto.ProcessConfigDto;
import oneapp.incture.workbox.pmc.dto.ReportAgingDto;
import oneapp.incture.workbox.pmc.dto.ReportDto;
import oneapp.incture.workbox.pmc.dto.WorkloadRangeDto;
import oneapp.incture.workbox.poadapter.dao.ProcessConfigDao;
import oneapp.incture.workbox.poadapter.dao.ReportAgingDao;
import oneapp.incture.workbox.poadapter.dao.WorkloadRangeDao;
import oneapp.incture.workbox.poadapter.dto.BaseDto;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;
import oneapp.incture.workbox.util.NoResultFault;
import oneapp.incture.workbox.util.PMCConstant;
import oneapp.incture.workbox.util.ServicesUtil;

/**
 * Session Bean implementation class AdminControlBean
 */
@Service("AdminControlFacade")
public class AdminControlFacade implements AdminControlFacadeLocal {

	private static final Logger logger=LoggerFactory.getLogger(AdminControlFacade.class);
	
	@Autowired
	private ConfigurationFacadeLocal config;

	@Autowired
	private ProcessConfigDao processConfigDao;

	@Autowired
	private WorkloadRangeDao workloadRangeDao;

	@Autowired
	private ReportAgingDao reportAgingDao;

	public ResponseMessage deleteProcessConfig(String processName) {
		ResponseMessage responseDto = new ResponseMessage();
		try {
			ProcessConfigDto processConfigDto = new ProcessConfigDto();
			processConfigDto.setProcessName(processName);
			processConfigDao.delete(processConfigDto);
			responseDto.setMessage("SUCCESS");
			responseDto.setStatus("SUCCESS");
			responseDto.setStatusCode("0");
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
			responseDto.setStatus("FAILURE");
			responseDto.setStatusCode("1");
		}
		return responseDto;
	}

	@Override
	public AdminControlDto getAdminConfigurationData() {

		AdminControlDto resultDto = new AdminControlDto();
		List<ProcessConfigDto> configDtos = null;

		try {
			configDtos = processConfigDao.getAllProcessConfigEntry();
		} catch (NoResultFault e) {
			logger.error("NO Result found for any process Configured");
		}

		List<WorkloadRangeDto> workloadRangeDtos = config.getWorkLoadRange().getWorkloadRangeDtos();

		List<ReportAgingDto> reportAgingDtos = reportAgingDao.getAllReportConfiguration();

		List<AgingRangeConfigDto> agingRangeConfigDtos = new ArrayList<AgingRangeConfigDto>();
		AgingRangeConfigDto agingRangeConfigDto = null;
		int processCount = 0;
		int taskCount = 0;
		int taskStatusCount = 0;

		List<ReportDto> diffReportDtoList = new ArrayList<ReportDto>();

		ReportDto processAging, taskStatus, taskAging;
		processAging = new ReportDto();
		taskStatus = new ReportDto();
		taskAging = new ReportDto();

		List<ReportAgingDto> processAging_list, taskStatus_list, taskAging_list;
		processAging_list = new ArrayList<ReportAgingDto>();
		taskStatus_list = new ArrayList<ReportAgingDto>();
		taskAging_list = new ArrayList<ReportAgingDto>();

		if (!ServicesUtil.isEmpty(reportAgingDtos)) {
			Iterator<ReportAgingDto> it = reportAgingDtos.iterator();
			while (it.hasNext()) {
				ReportAgingDto dto = it.next();
				if (!ServicesUtil.isEmpty(dto.getAgingRange())) {
					agingRangeConfigDto = new AgingRangeConfigDto();
					agingRangeConfigDto.setAgingRange(dto.getAgingRange());
					agingRangeConfigDto.setReportName(dto.getReportName());
					agingRangeConfigDto.setReportId(dto.getId());
					agingRangeConfigDtos.add(agingRangeConfigDto);
					it.remove();
				} else {
					if (dto.getReportName().equals(PMCConstant.PROCESS_AGING_REPORT)) {
						processAging.setReportName("PROCESS AGING");
						processAging_list.add(dto);
						++processCount;
					} else if (dto.getReportName().equals(PMCConstant.TASK_AGING_REPORT)) {
						taskAging.setReportName("TASK AGING");
						taskAging_list.add(dto);
						++taskCount;
					} else if (dto.getReportName().equals(PMCConstant.USER_TASK_STATUS_GRAPH)) {
						taskStatus.setReportName("TASK STATUS");
						taskStatus_list.add(dto);
						++taskStatusCount;
					}
				}
			}
		}
		processAging.setReportDtoList(processAging_list);
		taskAging.setReportDtoList(taskAging_list);
		taskStatus.setReportDtoList(taskStatus_list);
		diffReportDtoList.add(processAging);
		diffReportDtoList.add(taskAging);
		diffReportDtoList.add(taskStatus);

		resultDto.setProcessConfigDtos(configDtos);
		resultDto.setWorkloadRangeDtos(workloadRangeDtos);
		resultDto.setReportDto(diffReportDtoList);
		resultDto.setAgingRangeConfigDto(agingRangeConfigDtos);
		resultDto.setProcessCount(processCount);
		resultDto.setTaskCount(taskCount);
		resultDto.setTaskStatusCount(taskStatusCount);
		return resultDto;

	}

	@Override
	public ResponseMessage createUpdateDataAdmin(AdminControlDto adminControlDto) {

		ResponseMessage responseDto = new ResponseMessage();

		if (!ServicesUtil.isEmpty(adminControlDto)) {
			List<ProcessConfigDto> processConfigDtos = adminControlDto.getProcessConfigDtos();
			List<WorkloadRangeDto> workloadRangeDtos = adminControlDto.getWorkloadRangeDtos();
			List<ReportDto> reportAgingDtos = adminControlDto.getReportDto();
			List<AgingRangeConfigDto> agingRangeConfigDtos = adminControlDto.getAgingRangeConfigDto();
			try {

				if (!ServicesUtil.isEmpty(processConfigDtos)) {
					for (ProcessConfigDto dto : processConfigDtos) {
						updateCreateDelete(dto);
					}
				}

				if (!ServicesUtil.isEmpty(workloadRangeDtos)) {

					for (WorkloadRangeDto dto1 : workloadRangeDtos) {
						try {
							workloadRangeDao.update(dto1);
							logger.error("Workload Range Updated");
						} catch (NoResultFault e) {
							try {
								workloadRangeDao.create(dto1);
								logger.error("WorkloadRange Created");
							} catch (Exception eWorkloadRange) {
								logger.error("Exception : " + eWorkloadRange.getMessage());
							}
						} catch (Exception eWorkloadRange) {
							logger.error("Exception : " + eWorkloadRange.getMessage());
						}
					}
				}
				if (!ServicesUtil.isEmpty(reportAgingDtos)) {

					for (int i = 0; i < reportAgingDtos.size(); i++) {
						for (ReportAgingDto dto2 : reportAgingDtos.get(i).getReportDtoList()) {
							updateCreateDelete(dto2);
							// dont send dao not required as it is in same file.
						}
					}

				}

				if (!ServicesUtil.isEmpty(agingRangeConfigDtos)) {
					for (AgingRangeConfigDto dto4 : agingRangeConfigDtos) {
						ReportAgingDto dto = new ReportAgingDto();
						dto.setAgingRange(dto4.getAgingRange());
						dto.setReportName(dto4.getReportName());
						dto.setId(dto4.getReportId());

						try {
							reportAgingDao.update(dto);
							logger.error("Aging Range Updated");
						} catch (NoResultFault e) {
							try {
								reportAgingDao.create(dto);
								logger.error("Aging Range Created");
							} catch (Exception eAgingRange) {
								logger.error("Exception1 : " + eAgingRange.getMessage());
							}
						} catch (Exception eAgingRange) {
							logger.error("Exception2 : " + eAgingRange.getMessage());
						}
					}
				}

				responseDto.setMessage("SUCCESS");
				responseDto.setStatus("SUCCESS");
				responseDto.setStatusCode("0");

			} catch (Exception e) {
				logger.error("Exception : " + e.getMessage());
				responseDto.setMessage("Failed");
				responseDto.setStatus("Failed");
				responseDto.setStatusCode("1");

			}
		}
		return responseDto;
	}

	private void updateCreateDelete(BaseDto baseDto) {
		String expr = baseDto.getClass().getSimpleName();

		if (expr.equals("ProcessConfigDto")) {
			ProcessConfigDto dto = (ProcessConfigDto) baseDto;
			if (!ServicesUtil.isEmpty(dto.getIsDeleted()) && dto.getIsDeleted().equals(true)) {
				try {
					processConfigDao.delete(dto);
					logger.error("Process Config deleted");
				} catch (Exception e) {
					logger.error("Exception : " + e.getMessage());
				}
			} else {
				try {
					processConfigDao.update(dto);
					logger.error("Process Config Updated");
				} catch (NoResultFault e) {
					try {
						processConfigDao.create(dto);
						logger.error("Process Config Created");
					} catch (Exception eReportingAging) {
						logger.error("Exception : " + eReportingAging.getMessage());
					}
				} catch (Exception eReportingAging) {
					logger.error("Exception : " + eReportingAging.getMessage());
				}
			}
		} else if (expr.equals("ReportAgingDto")) {
			ReportAgingDto dto = (ReportAgingDto) baseDto;
			if (!ServicesUtil.isEmpty(dto.getIsDeleted()) && dto.getIsDeleted().equals(true)) {
				try {
					reportAgingDao.delete(dto);
					logger.error("Reporting Aging deleted");
				} catch (Exception e) {
					logger.error("Exception : " + e.getMessage());
				}
			} else {
				try {
					reportAgingDao.update(dto);
					logger.error("Reporting Aging Updated");
				} catch (NoResultFault e) {
					try {
						reportAgingDao.create(dto);
						logger.error("Reporting Aging Created");
					} catch (Exception eReportingAging) {
						logger.error("Exception : " + eReportingAging.getMessage());
					}
				} catch (Exception eReportingAging) {
					logger.error("Exception : " + eReportingAging.getMessage());
				}
			}
		}

	}

}
