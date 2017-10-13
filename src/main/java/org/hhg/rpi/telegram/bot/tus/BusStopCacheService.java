package org.hhg.rpi.telegram.bot.tus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hhg.rpi.reportservice.reporting.LogServiceInterface;
import org.hhg.rpi.telegram.tus.model.BusStationInfo;
import org.hhg.rpi.telegram.tus.model.TUSStopItem;
import org.hhg.rpi.telegram.tus.model.TUSStopListResponse;
import org.hhg.rpi.telegram.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BusStopCacheService implements BusStopCacheServiceInterface{

	@Autowired
	private LogServiceInterface logService;

	@Autowired
	private TUSDataRetrievalServiceInterface busService;

	private HashMap<Integer, BusStationInfo> stationInfoMap = new HashMap<>();

	@Scheduled(fixedRate = Constants.TUS_STATION_UPDATE_RATE)
	public void updateCache() {
		int removedEntries = 0;
		int addedEntries = 0;
		int updatedEntries = 0;
		List<Integer> newRetrievedKeys = new LinkedList<Integer>();
		try {
			TUSStopListResponse stationInfo = busService.retrieveBusStopInformation();
			// We add the retrieved stops information to our map.
			for (TUSStopItem item : stationInfo.getResults()) {
				newRetrievedKeys.add(item.getStopId());
				if(stationInfoMap.containsKey(item.getStopId())){
					updatedEntries++;
				}else{
					addedEntries++;
				}
				stationInfoMap.put(item.getStopId(),
						new BusStationInfo(item.getStopId(), item.getStopName(), item.getAddress()));
			}

			// We remove items from our map which were not retrieved during the
			// previous operation.
			// This cleans the main list from outdated entries.
			Iterator<Integer> oldKeysIterator = stationInfoMap.keySet().iterator();
			while (oldKeysIterator.hasNext()) {
				Integer oldKey = oldKeysIterator.next();
				if (!newRetrievedKeys.contains(oldKey)) {
					oldKeysIterator.remove();
					removedEntries++;
				}
			}
			logService.info("Bus stations cache updated: "+addedEntries+" new elements, "+updatedEntries+" updated elements, "+removedEntries+" removed elements.");
		} catch (Exception ex) {
			logService.error("An error occured while updating the station list." + ex.getMessage());
		}
	}

	@Override
	public BusStationInfo findById(Integer id) {
		return stationInfoMap.get(id);
	}

	@Override
	public List<BusStationInfo> findByName(String name) {
		List<BusStationInfo> result = new LinkedList<>();
		for(BusStationInfo item : stationInfoMap.values()){
			if(item.getStationName().toLowerCase().contains(name.toLowerCase())){
				result.add(item);
			}
		}
		
		return result;
	}

}
