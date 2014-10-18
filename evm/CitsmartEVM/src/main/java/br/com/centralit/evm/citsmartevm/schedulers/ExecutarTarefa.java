package br.com.centralit.evm.citsmartevm.schedulers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.inject.Inject;

import br.com.centralit.evm.citsmartevm.dao.ITarefasDAO;
import br.com.centralit.evm.citsmartevm.entity.Tarefas;
import br.com.centralit.evm.citsmartevm.util.MapaMemoria;


@Singleton
public class ExecutarTarefa {
	private Logger logger = Logger.getLogger(ExecutarTarefa.class.getName());
	
	@Resource
	TimerService timerService;	
	
	@Inject
	ProdutorDeMensagens executarDeMensagens;
	
	@Inject
	ITarefasDAO tarefasAgendadas;
	

	public void enviarXMLCTM(String XML) {

		executarDeMensagens.setMessage(XML);
		executarDeMensagens.send();
	}
	
	
	@Schedule(second = "*/5", hour = "*", minute="*", persistent = false)
	public void processarTarefasAgendadas() {
		
		for (Iterator tarefa = MapaMemoria.getInstance().tarefasAgendadas.iterator(); tarefa.hasNext();) {
			Tarefas tarefas = (Tarefas) tarefa.next();
			
			
			
		}
		
	}
	
	@Schedule(second = "*", hour = "*", minute="/2", persistent = false)
	public void agendadorSecundario() {
		MapaMemoria.getInstance().atualizarTarefasAgendadas(tarefasAgendadas.listAll());
	}
	
	
	@Schedule(second = "*", hour = "*", minute="/2", persistent = false)
	@Lock(LockType.READ)
	/**
	 * Executar o método abaixo a cada segundo.
	 * @return
	 */
	public String taskEnviarXMLsCTM() {

		String resultado = "";
		try {

			// Início

			String target_dir = "C:\\Users\\cristian.guedes\\Desktop\\inventários";
			File dir = new File(target_dir);
			File[] files = dir.listFiles();

			for (File f : files) {
				if (f.isFile()) {
					BufferedReader inputStream = null;

					try {
						inputStream = new BufferedReader(new FileReader(f));
						String line;

						while ((line = inputStream.readLine()) != null) {
							resultado += line;
						}
					} finally {
						if (inputStream != null) {
							inputStream.close();
						}
					}
				}

				resultado = resultado
						.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>",
								"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?><EventoCTM>");
				resultado = resultado + "</EventoCTM>";

				// Remover caracteres "inválidos"
				resultado = resultado.replaceAll("\0", "_");

				enviarXMLCTM(resultado);

				resultado = "";

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resultado;
	}
	
	@Timeout
	private void generateReport(Timer timer) {

//	    logger.info("!!--timeout invoked here "+new Date());
//	    System.out.println("!!--timeout invoked here "+new Date());

	 

	}	
	
	
	private void teste() {
		ScheduleExpression expression = new ScheduleExpression();
        expression.second("*/1").minute("*").hour("*");
//        timerService.createCalendarTimer
		
		
	}
			
	
	
}
