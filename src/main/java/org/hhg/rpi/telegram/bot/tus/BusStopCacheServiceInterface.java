package org.hhg.rpi.telegram.bot.tus;

import java.util.List;

import org.hhg.rpi.telegram.tus.model.BusStationInfo;

public interface BusStopCacheServiceInterface {
	public BusStationInfo findById(Integer id);
	public List<BusStationInfo> findByName(String name);
}
