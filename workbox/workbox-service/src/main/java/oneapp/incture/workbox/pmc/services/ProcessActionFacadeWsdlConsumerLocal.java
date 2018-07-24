package oneapp.incture.workbox.pmc.services;

import oneapp.incture.workbox.pmc.dto.ProcessActionDto;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;

public interface ProcessActionFacadeWsdlConsumerLocal {

	ResponseMessage cancelProcess(ProcessActionDto processList);

}
