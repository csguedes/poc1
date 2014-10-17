package br.com.centralit.correlacaoeventos.esper.subscribers;


import br.com.centralit.evm.citsmartevm.entity.ctm.EventoCTM;
import br.com.centralit.evm.citsmartevm.entity.legado.EventoLegado;
import br.com.centralit.evm.correlacaoeventos.esper.EsperSingleton;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.event.map.MapEventBean;

public class SampleListener implements UpdateListener {

	
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		
		
		
		if (newEvents[0].getEventType().getName().equalsIgnoreCase("EventoCTM")) {
	        EventoCTM eventoCTM = (EventoCTM) newEvents[0].getUnderlying();
	        EsperSingleton.log.info(String.valueOf(eventoCTM.getId()) + " - " +eventoCTM.getMensagem());
	        EsperSingleton.log.info("tipo:" + newEvents[0].get("tipo"));
	        EsperSingleton.resultado="EventoCTM - evento processado!";
	        
			
		}
		
		
		else if (newEvents[0].getEventType().getName().equalsIgnoreCase("EventoLegado")) {
			EventoLegado eventoLegado =  (EventoLegado) newEvents[0].getUnderlying();
	        EsperSingleton.log.info(String.valueOf(eventoLegado.getId()) + " - " +eventoLegado.getMensagem());
	        EsperSingleton.log.info("tipo:" + newEvents[0].get("tipo"));
	        EsperSingleton.resultado="EventoLegado - evento processado!";
			
		} else {
			
			if (newEvents[0]instanceof MapEventBean) {
				
				MapEventBean mapEventBean = (MapEventBean) newEvents[0];
				EsperSingleton.log.info(mapEventBean.getProperties().toString());
				
		        EsperSingleton.resultado="Evento processado!";
			}
		}
      
		
	}
}
