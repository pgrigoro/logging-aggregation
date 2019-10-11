namespace java org.thrift.logging.aggregation

enum LogLevel {
	TRACE,	// finer-grained informational events than the DEBUG.	
	DEBUG,	// fine-grained informational events, useful to debug an application.
	INFO,	// informational messages that highlight the progress of the application.
	WARN,	// potentially harmful events.
	ERROR,	// error events that might still allow the application to run.
	FATAL	// very severe error events.
}

struct LogEvent {
    1: string uuid,       // event uuid 
    2: i16 v=1,           // version of schema
    3: string time,       // time of the event 
    4: string m,          // event message
	5: LogLevel logLevel, // logging level
    6: string hostIp,     // the host IP address
	7: string appName,    // the client application name
    8: string userName    // the user related to the event, if any
}

service LogEventService {

   void publish(1:LogEvent event)

}
