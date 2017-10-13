package org.hhg.rpi.telegram.bot.tus;

import static org.hhg.rpi.telegram.utils.Constants.TUS_QUERY_PARADAS;
import static org.hhg.rpi.telegram.utils.Constants.TUS_QUERY_TIEMPOS_ESTIMADOS;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.hhg.rpi.telegram.tus.model.BusStationInfo;
import org.hhg.rpi.telegram.tus.model.TUSBusEstimationResponse;
import org.hhg.rpi.telegram.tus.model.TUSStopListResponse;
import org.hhg.rpi.telegram.utils.CustomHttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service tasked with network requests to retrieve information from TUS data services.
 * 
 * 
 * @author Harold Hormaechea
 *
 */
@Service
public class TUSDataRetrievalService implements TUSDataRetrievalServiceInterface{
	
	@Autowired
	private BusStopCacheServiceInterface cacheService;
	
	private RestTemplate restTemplate = new RestTemplate();
	{
		restTemplate.setInterceptors(
				Arrays.asList(new CustomHttpRequestInterceptor[] { new CustomHttpRequestInterceptor() }));
	}
	
	/**
	 * Retrieves information about the next buses scheduled for arrival to the queried stop.
	 * 
	 * 
	 * TODO: Accept input strings to ask for stop names instead of stop numbers.
	 * 
	 * @throws NumberFormatException If the query parameter is not parseable to a number
	 */
	@Override
	public TUSBusEstimationResponse retrieveBusInformation(Integer stopNumber) throws NumberFormatException{
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("idParada", stopNumber);
		return restTemplate.getForObject(TUS_QUERY_TIEMPOS_ESTIMADOS, TUSBusEstimationResponse.class, params);
	}
	
	
	/**
	 * Retrieves the list of bus stops from the server.
	 */
	@Override
	public TUSStopListResponse retrieveBusStopInformation() {
		return restTemplate.getForObject(TUS_QUERY_PARADAS, TUSStopListResponse.class, new Object[0]);
	}
	
	/**
	 * Retrieves information about a bus stop by its id.
	 */
	@Override
	public BusStationInfo retrieveBusStopInformation(Integer id){
		return cacheService.findById(id);
	}


	@Override
	public List<BusStationInfo> retrieveBusStopInformation(String stopName) {
		return cacheService.findByName(stopName);
	}
	
	
}
