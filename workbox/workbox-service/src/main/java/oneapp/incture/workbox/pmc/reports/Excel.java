package oneapp.incture.workbox.pmc.reports;

import java.io.ByteArrayOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.pmc.dto.DownloadReportResponseDto;
import oneapp.incture.workbox.pmc.dto.ReportFormattedDto;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;
import oneapp.incture.workbox.util.ServicesUtil;

/**
 * @author Saurabh
 *
 */
@Component("Excel")
public class Excel extends File {

	private static final Logger logger=LoggerFactory.getLogger(Excel.class);
	
	public DownloadReportResponseDto pushData(ReportFormattedDto reportFormattedDto) {
		DownloadReportResponseDto responseDto = new DownloadReportResponseDto();
		ResponseMessage message = new ResponseMessage();
		logger.error("[PMC] REPORT - Excel  - pushData() - Started with ReportFormattedDto - " + reportFormattedDto);
		if (!ServicesUtil.isEmpty(reportFormattedDto)) {
			Workbook workbook = new XSSFWorkbook();
			Sheet detailSheet = null;
			if (!ServicesUtil.isEmpty(reportFormattedDto.getSheetName())) {
				detailSheet = workbook.createSheet(reportFormattedDto.getSheetName());

			}
			byte[] bytes = null;
			try {
				CellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setFillForegroundColor(HSSFColorPredefined.CORNFLOWER_BLUE.getIndex());
				cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderTop(BorderStyle.THIN);
				Font headerFont = workbook.createFont();
				headerFont.setBold(true);;
				cellStyle.setFont(headerFont);

				CellStyle dataCellStyle = workbook.createCellStyle();
				dataCellStyle.setBorderBottom(BorderStyle.THIN);
				dataCellStyle.setBorderRight(BorderStyle.THIN);
				dataCellStyle.setBorderLeft(BorderStyle.THIN);
				dataCellStyle.setBorderTop(BorderStyle.THIN);

				/**
				 * Printing Header on 1st row of sheet
				 */
				int cellNumber = 0;
				Row row1 = null;
				if (!ServicesUtil.isEmpty(reportFormattedDto.getHeaders())) {
					row1 = detailSheet.createRow(0);
					for (String header : reportFormattedDto.getHeaders()) {
						Cell headerCell = row1.createCell(cellNumber++);
						headerCell.setCellStyle(cellStyle);
						headerCell.setCellValue(header);
					}
					detailSheet.createFreezePane(0, 1);
				}

				/**
				 * Printing Report Data List from 2nd row of sheet till end of Report Data.
				 */
				int rowNumber = 1;
				if (!ServicesUtil.isEmpty(reportFormattedDto.getDataRows())) {
					for (Object[] obj : reportFormattedDto.getDataRows()) {
						Row row = detailSheet.createRow(rowNumber++);
						for (int i = 0; i < obj.length; i++) {
							Cell dataCell = row.createCell(i);
							dataCell.setCellStyle(dataCellStyle);
							dataCell.setCellValue(obj[i] == null ? "" : obj[i].toString());
						}
					}
				}
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				bytes = bos.toByteArray();
				workbook.close();
				if (!ServicesUtil.isEmpty(bytes)) {
					Base64 base64 = new Base64();
					if (bytes != null) {
						responseDto.setFile(bytes);
						responseDto.setBase64(new String(base64.encode(bytes)));
						responseDto.setFilename(detailSheet.getSheetName());
						message.setStatus("SUCCESS");
						message.setStatusCode("0");
						message.setMessage("EXCEL Bytes Successfully created");
						responseDto.setMessage(message);
					}
				}
			} catch (Exception e) {
				message.setStatus("FAILURE");
				message.setStatusCode("0");
				message.setMessage("EXCEL Bytes creation Failed");
				responseDto.setMessage(message);
				logger.error("[PMC] REPORT - Excel  - pushData() - Exception Caught while building Excel sheet - " + e.getMessage());
			}
		} else {
			message.setStatus("SUCCESS");
			message.setStatusCode("1");
			message.setMessage("NO REPORT DATA ");
			responseDto.setMessage(message);
			logger.error("ReportFormattedDto is null, So No Sheet Generated.");

		}
		logger.error("[PMC] REPORT - Excel  - pushData() - Ended with DownloadReportResponseDto - " + responseDto);
		return responseDto;
	}
}
