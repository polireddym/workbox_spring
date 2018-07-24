package oneapp.incture.workbox.pmc.reports;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import oneapp.incture.workbox.pmc.dto.AgingResponseDto;
import oneapp.incture.workbox.pmc.dto.AgingTableDto;
import oneapp.incture.workbox.pmc.dto.DownloadReportResponseDto;
import oneapp.incture.workbox.pmc.dto.ManageTasksDto;
import oneapp.incture.workbox.pmc.dto.PMCReportBaseDto;
import oneapp.incture.workbox.pmc.dto.ProcessAgeingResponse;
import oneapp.incture.workbox.pmc.dto.ProcessDetailsResponse;
import oneapp.incture.workbox.pmc.dto.ReportFormattedDto;
import oneapp.incture.workbox.pmc.dto.responses.ManageTasksResponseDto;
import oneapp.incture.workbox.pmc.dto.responses.TaskAgeingResponse;
import oneapp.incture.workbox.poadapter.dto.ProcessEventsDto;
import oneapp.incture.workbox.util.MapResponseDto;
import oneapp.incture.workbox.util.PMCConstant;
import oneapp.incture.workbox.util.ServicesUtil;

public abstract class File {

	public abstract DownloadReportResponseDto pushData(ReportFormattedDto reportFormattedDto);

	public ReportFormattedDto setSheetName(String sheetName) {
		System.err.println("[PMC] REPORT - File  - setSheetName() - Started");
		ReportFormattedDto formattedDto = new ReportFormattedDto();
		formattedDto.setSheetName(PMCConstant.REPORT + " " + sheetName);
		System.err.println("[PMC] REPORT - File  - setSheetName() - Ended with sheetName - " + formattedDto.getSheetName());
		return formattedDto;
	}

	public ReportFormattedDto exportToFormattedDto(PMCReportBaseDto pmcReportBaseDto, ReportFormattedDto formattedDto) {
		System.err.println("[PMC] REPORT - File  - exportToFormattedDto() - Started with pmcReportBaseDto - " + pmcReportBaseDto);
		if (!ServicesUtil.isEmpty(pmcReportBaseDto)) {
			if (pmcReportBaseDto.getClass().getSimpleName().equals(ProcessDetailsResponse.class.getSimpleName())) {
				System.err.println("[PMC] REPORT - File  - exportToFormattedDto() - pmcReportBaseDto.getClass() - " + pmcReportBaseDto.getClass().getSimpleName());
				return this.exportfromProcessDetailsResponse(pmcReportBaseDto, formattedDto);
			} else if (pmcReportBaseDto.getClass().getSimpleName().equals(TaskAgeingResponse.class.getSimpleName())) {
				System.err.println("[PMC] REPORT - File  - exportToFormattedDto() - pmcReportBaseDto.getClass() - " + pmcReportBaseDto.getClass().getSimpleName());
				return this.exportfromTaskAgeingResponse(pmcReportBaseDto, formattedDto);
			} else if (pmcReportBaseDto.getClass().getSimpleName().equals(ManageTasksResponseDto.class.getSimpleName())) {
				System.err.println("[PMC] REPORT - File  - exportToFormattedDto() - pmcReportBaseDto.getClass() - " + pmcReportBaseDto.getClass().getSimpleName());
				return this.exportfromManageTasksResponseDto(pmcReportBaseDto, formattedDto);
			} else if (pmcReportBaseDto.getClass().getSimpleName().equals(ProcessAgeingResponse.class.getSimpleName())) {
				System.err.println("[PMC] REPORT - File  - exportToFormattedDto() - pmcReportBaseDto.getClass() - " + pmcReportBaseDto.getClass().getSimpleName());
				return this.exportfromProcessAgeingResponse(pmcReportBaseDto, formattedDto);
			}
		}
		System.err.println("[PMC] REPORT - File  - exportToFormattedDto() - Ended formattedDto with null ");
		return formattedDto;
	}
	
	private ReportFormattedDto exportfromProcessAgeingResponse(PMCReportBaseDto pmcReportBaseDto, ReportFormattedDto formattedDto) {
		System.err.println("[PMC] REPORT - File  - exportfromManageTasksResponseDto() - Started with pmcReportBaseDto - " + pmcReportBaseDto);
		if (!ServicesUtil.isEmpty(pmcReportBaseDto)) {
			ProcessAgeingResponse ageingResponse = ((ProcessAgeingResponse) pmcReportBaseDto);
			if (!ServicesUtil.isEmpty(ageingResponse) && !ServicesUtil.isEmpty(ageingResponse.getAgeingTable())) {
				List<String> headerRow = new ArrayList<String>();
				if (!ServicesUtil.isEmpty(ageingResponse.getAgeingTable().getHeaderMap())) {
//					Map<String, BigDecimal> headerMap = ageingResponse.getAgeingTable().getHeaderMap();
//					Iterator<Entry<String, BigDecimal>> it = headerMap.entrySet().iterator();
//					while (it.hasNext()) {
//						Entry<String, BigDecimal> entry = it.next();
//						headerRow.add(entry.getKey());
//					}
					List<MapResponseDto> dtoList = ageingResponse.getAgeingTable().getHeaderMap().getEntry(); 
					for(MapResponseDto dto : dtoList){
						headerRow.add(dto.getKey());
					}
					formattedDto.setHeaders(headerRow);
				}
				if (!ServicesUtil.isEmpty(ageingResponse.getAgeingTable().getTupleDtos())) {
					List<AgingTableDto> tupleDtos = ageingResponse.getAgeingTable().getTupleDtos();
					List<Object[]> dataRows = new ArrayList<Object[]>();
					for (AgingTableDto dto : tupleDtos) {
						Object[] row = new Object[headerRow.size()];
						row[0] = ServicesUtil.isEmpty(dto.getProcessDisplayName()) ? ServicesUtil.isEmpty(dto.getName()) ? null : dto.getName() : dto.getProcessDisplayName();
						Map<String, BigDecimal> dataMap = dto.getOldDataMap();
						Iterator<Entry<String, BigDecimal>> it = dataMap.entrySet().iterator();
						int i = 1;
						while (it.hasNext()) {
							Entry<String, BigDecimal> entry = it.next();
							row[i++] = entry.getValue();
						}
						row[i] = dto.getCount();
						dataRows.add(row);
					}
					formattedDto.setDataRows(dataRows);
				}
			}
		}
		return formattedDto;
	}

	private ReportFormattedDto exportfromManageTasksResponseDto(PMCReportBaseDto pmcReportBaseDto, ReportFormattedDto formattedDto) {
		System.err.println("[PMC] REPORT - File  - exportfromManageTasksResponseDto() - Started with pmcReportBaseDto - " + pmcReportBaseDto);
		if (!ServicesUtil.isEmpty(pmcReportBaseDto)) {
			List<ManageTasksDto> dtos = ((ManageTasksResponseDto) pmcReportBaseDto).getTasks();
			if (!ServicesUtil.isEmpty(dtos)) {
				List<String> headerRow = new ArrayList<String>();
				headerRow.add("RequestId");
				headerRow.add("Process Name");
				headerRow.add("Subject");
				headerRow.add("Created At");
				headerRow.add("Owners Name");
				headerRow.add("Status");
				formattedDto.setHeaders(headerRow);
				List<Object[]> dataRows = new ArrayList<Object[]>();
				DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yy hh:mm:ss a");
				for (ManageTasksDto dto : dtos) {
					Object[] row = new Object[headerRow.size()];
					row[0] = ServicesUtil.isEmpty(dto.getRequestId()) ? null : dto.getRequestId();
					row[1] = ServicesUtil.isEmpty(dto.getProcessDisplayName()) ? null : dto.getProcessDisplayName();
					row[2] = ServicesUtil.isEmpty(dto.getTaskSubject()) ? null : dto.getTaskSubject();
					row[3] = ServicesUtil.isEmpty(dto.getCreatedDate()) ? null : dateFormatter.format(dto.getCreatedDate());
					row[4] = ServicesUtil.isEmpty(dto.getOwners()) ? null : dto.getOwners();
					row[5] = ServicesUtil.isEmpty(dto.getStatus()) ? null : dto.getStatus();
					dataRows.add(row);
				}
				formattedDto.setDataRows(dataRows);
			}
		}
		System.err.println("[PMC] REPORT - File  - exportfromManageTasksResponseDto() - Ended with formattedDto - " + formattedDto);
		return formattedDto;
	}

	private ReportFormattedDto exportfromTaskAgeingResponse(PMCReportBaseDto pmcReportBaseDto, ReportFormattedDto formattedDto) {
		System.err.println("[PMC] REPORT - File  - exportfromTaskAgeingResponse() - Started with pmcReportBaseDto - " + pmcReportBaseDto);
		if (!ServicesUtil.isEmpty(pmcReportBaseDto)) {
			AgingResponseDto dtos = ((TaskAgeingResponse) pmcReportBaseDto).getAgeingTable();
			System.err.println("AgingResponseDto from AgingResponseDto()" + dtos);
			if (!ServicesUtil.isEmpty(dtos) && !ServicesUtil.isEmpty(dtos.getHeaderMap()) && !ServicesUtil.isEmpty(dtos.getTupleDtos())) {
				/*Map<String, BigDecimal> headerMap = dtos.getHeaderMap();
				List<String> headerRow = new ArrayList<String>();
				if (!ServicesUtil.isEmpty(headerMap)) {
					Iterator<String> it = headerMap.keySet().iterator();
					while (it.hasNext()) {
						headerRow.add(it.next());
					}
				}*/
				List<String> headerRow = new ArrayList<String>();
				List<MapResponseDto> dtoList = dtos.getHeaderMap().getEntry(); 
				for(MapResponseDto dto : dtoList){
					headerRow.add(dto.getKey());
				}
				formattedDto.setHeaders(headerRow);

				List<AgingTableDto> tupleDtos = dtos.getTupleDtos();
				List<Object[]> dataRows = new ArrayList<Object[]>();
				if (!ServicesUtil.isEmpty(tupleDtos)) {
					for (AgingTableDto dto : tupleDtos) {
						Object[] row = new Object[headerRow.size()];
						row[0] = ServicesUtil.isEmpty(dto.getUserName()) ? null : dto.getUserName();
						Map<String, BigDecimal> dataMap = dto.getOldDataMap();
						Iterator<String> it = dataMap.keySet().iterator();
						int rowCount = 1;
						while (it.hasNext()) {
							row[rowCount++] = dataMap.get(it.next());
						}
						dataRows.add(row);
					}
					formattedDto.setDataRows(dataRows);
				}
			}
		}
		System.err.println("[PMC] REPORT - File  - exportfromTaskAgeingResponse() - Ended with formattedDto - " + formattedDto);
		return formattedDto;
	}

	private ReportFormattedDto exportfromProcessDetailsResponse(PMCReportBaseDto pmcReportBaseDto, ReportFormattedDto formattedDto) {
		System.err.println("[PMC] REPORT - File  - exportfromProcessDetailsResponse() - Started with pmcReportBaseDto - " + pmcReportBaseDto);
		if (!ServicesUtil.isEmpty(pmcReportBaseDto)) {
			List<ProcessEventsDto> dtos = ((ProcessDetailsResponse) pmcReportBaseDto).getProcessEventsList();
			System.err.println("List<ProcessEventsDto> from getProcessEventsList() - " + dtos);
			if (!ServicesUtil.isEmpty(dtos)) {
				List<String> headerRow = new ArrayList<String>();
				headerRow.add("RequestId");
				headerRow.add("Process Name");
				headerRow.add("Subject");
				headerRow.add("Started At");
				headerRow.add("Started By");
				formattedDto.setHeaders(headerRow);
				List<Object[]> dataRows = new ArrayList<Object[]>();
				System.err.println("User Workload data size : " + dtos.size() );
				DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yy hh:mm:ss a");
				for (ProcessEventsDto dto : dtos) {
					Object[] row = new Object[headerRow.size()];
					row[0] = ServicesUtil.isEmpty(dto.getRequestId()) ? null : dto.getRequestId();
					row[1] = ServicesUtil.isEmpty(dto.getProcessDisplayName()) ? null : dto.getProcessDisplayName();
					row[2] = ServicesUtil.isEmpty(dto.getSubject()) ? null : dto.getSubject();
					row[3] = ServicesUtil.isEmpty(dto.getStartedAt()) ? null : dateFormatter.format(dto.getStartedAt());
					row[4] = ServicesUtil.isEmpty(dto.getStartedByDisplayName()) ? null : dto.getStartedByDisplayName();
					dataRows.add(row);
				}
				formattedDto.setDataRows(dataRows);
			}
		}
		System.err.println("[PMC] REPORT - File  - exportfromProcessDetailsResponse() - Ended with formattedDto - " + formattedDto);
		return formattedDto;
	}

}
