# PiReportServices

 - Changelog:
  v.0.2.0:  - Alarms can be set-up using the command: /alarma {stopId} {lineId} {maxMinutesLeft}. The bot will send you a message 		when the next bus of that line will arrive at the provided stop. At most, maxMinutesLeft minutes must be left for the 		    bus to arrive before the alarm will be set off.
  v.0.1.0:  - Added information about bus stops.
            - Information can be queried by using the station name, instead of only its number. If more than one matching stations are found by that name, a choice will be offered to click on it for the results. Those results will remain on the "choice keyboard" until a new multi-result query is done, so they can be clicked once and again to keep retrieving updated results without having to re-type the station name or number.

This service can be used as a Telegram bot to retrieve information about next buses arriving at Santander bus stops. It also automatically reports (with Twitter integration) on server status updates or changes

Eventually, a managing interface will be implemented..

It works as a Maven project, which can be constructed and deployed in a web container like Apache (untested) or JBoss (tested and deployed). 

This is an experimental project to learn more on Spring application configuration, integration between services, and social network APIs.

https://twitter.com/PiReportService

