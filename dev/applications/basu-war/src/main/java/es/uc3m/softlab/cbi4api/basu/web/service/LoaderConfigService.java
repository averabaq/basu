/*
 * $Id: LoaderConfigService.java,v 1.0 2014-01-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.web.service;

import es.uc3m.softlab.cbi4api.basu.web.config.LoaderConfig;
import es.uc3m.softlab.cbi4api.basu.web.config.LoaderConfigurationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author: averab
 */
@Controller
@RequestMapping(value = "/config", produces = {"application/json", "application/xml"})
public class LoaderConfigService {
    /** Log for tracing */
    private static final Logger logger = Logger.getLogger(LoaderConfigService.class);
    /** Loader configurator service */
    @Autowired LoaderConfig config;
    /** Loader configurator service */
    //@Autowired EventFactFacade eventFactFacade;

    /**
     *
     * @param model
     * @return
     */
    @RequestMapping(value="/{model}", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void setupModel(@PathVariable("model") String model) {
        try {
            config.setup(model);
        } catch (LoaderConfigurationException lcex) {
            logger.error(lcex.fillInStackTrace());
        }

//        try {
//            EventFact _eventFact = eventFactFacade.getEventFact(1L);
//            logger.info(_eventFact);
//
//            ProcessInstance instance = new ProcessInstance();
//            instance.setId(1L);
//            instance.setDescription("Instance Process " + 1);
//            instance.setName("Instance");
//            instance.setInstanceSrcId("BASU1 Instance");
//
//            EventFact eventFact = new EventFact();
//            eventFact.setProcessing(1000L);
//            eventFact.setChangeOver(10L);
//            eventFact.setWait(0L);
//            eventFact.setTurnAround(918L);
//            eventFact.setEndTime(new Date());
//            eventFact.setStartTime(new Date());
//            eventFact.setProcess(instance);
//
//           // EventFactFacade eventFactFacade = (EventFactFacade)context.getBean(StaticResources.COMPONENT_NAME_EVENT_FACT_FACADE);
//            eventFactFacade.storeEventFact(eventFact);
//
//            //config.setup(model);
//        } catch (EventFactException efex) {
//            logger.error(efex.fillInStackTrace());
//        } /* catch (LoaderConfigurationException lcex) {
//            logger.error(lcex.fillInStackTrace());
//        } */
    }
}