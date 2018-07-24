package oneapp.incture.workbox.pmc.rest;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.util.SequenceGenerator;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/sequence", produces = "application/json")
public class SequenceGeneratorRest {

	@RequestMapping(value = "number", method = RequestMethod.GET)
	public String getSequenceNo() {
		Integer i = SequenceGenerator.nextNum();
		System.err.println("SequenceGenerator : " + i);
		return String.valueOf(i);
	}
}
