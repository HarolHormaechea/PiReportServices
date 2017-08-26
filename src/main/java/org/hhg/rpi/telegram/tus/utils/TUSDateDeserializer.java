package org.hhg.rpi.telegram.tus.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import org.hhg.rpi.telegram.utils.Constants;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class TUSDateDeserializer extends JsonDeserializer<GregorianCalendar>{

	@Override
	public GregorianCalendar deserialize(JsonParser parser, DeserializationContext ctx)
			throws IOException, JsonProcessingException {
		GregorianCalendar calendar = null;
		try {
			calendar = new GregorianCalendar();
			calendar.setTime(new SimpleDateFormat(Constants.TUS_DATE_FORMAT).parse(parser.getText()));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar;
	}

	
}
