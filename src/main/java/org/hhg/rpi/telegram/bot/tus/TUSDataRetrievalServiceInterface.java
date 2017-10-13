package org.hhg.rpi.telegram.bot.tus;

import java.util.List;

import org.hhg.rpi.telegram.tus.model.BusStationInfo;
import org.hhg.rpi.telegram.tus.model.TUSBusEstimationResponse;
import org.hhg.rpi.telegram.tus.model.TUSStopListResponse;

public interface TUSDataRetrievalServiceInterface {

	public TUSBusEstimationResponse retrieveBusInformation(Integer stopNumber) throws NumberFormatException;

	public BusStationInfo retrieveBusStopInformation(Integer id);
	public List<BusStationInfo> retrieveBusStopInformation(String stopName);

	public TUSStopListResponse retrieveBusStopInformation();

}
