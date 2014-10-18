package br.com.centralit.evm.citsmartevm.schedulers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.inject.Inject;

import org.joda.time.DateTime;

import br.com.centralit.evm.citsmartevm.dao.ITarefasDAO;
import br.com.centralit.evm.citsmartevm.entity.Tarefas;
import br.com.centralit.evm.citsmartevm.util.CronExpression;
import br.com.centralit.evm.citsmartevm.util.MapaMemoria;


@Singleton
public class ExecutarTarefa {
	private Logger logger = Logger.getLogger(ExecutarTarefa.class.getName());
	
	@Inject
	ProdutorDeMensagens executarDeMensagens;
	
	@Inject
	ITarefasDAO tarefasAgendadas;
	

	public void enviarXMLCTM(String XML) {

		executarDeMensagens.setMessage(XML);
		executarDeMensagens.send();
	}
	
	
	/**
	 * Coloque aqui neste método tudo o que deve acontecer depois que acontecer a injeção de dependência
	 */
	@PostConstruct
	public void inicializarBean() {
		MapaMemoria.getInstance();
		sincronizarTarefasMemoriaBanco();

	}
	
	@Schedule(second = "*/30", hour = "*", minute="*", persistent = false)
	@Lock(LockType.READ)
	public void processarTarefasAgendadas() {
		
		
		for (Iterator tarefa = MapaMemoria.tarefasAgendadas.iterator(); tarefa.hasNext();) {
			Tarefas tarefas = (Tarefas) tarefa.next();
			
			
			// Verifica se esta na hora de executar a tarefa
			if (new DateTime().isAfter(tarefas.getProximaHora())) {
				logger.info("Hora de executar a tarefa " + " - " + tarefas.getDescricao());
				
				// Atualiza a proxima hora de execucao
				CronExpression expressaoCron = new CronExpression(tarefas.getCron());
				tarefas.setProximaHora(expressaoCron.nextTimeAfter(new DateTime()));
				
			}
			
			logger.info(tarefas.getDescricao() + " - " + tarefas.getProximaHora());
			
		}
		
		
	}
	
	private void sincronizarTarefasMemoriaBanco() {
		MapaMemoria.atualizarTarefasAgendadas(tarefasAgendadas.listAll());
		
		for (Iterator tarefa = MapaMemoria.tarefasAgendadas.iterator(); tarefa.hasNext();) {
			Tarefas tarefas = (Tarefas) tarefa.next();
			
			CronExpression expressaoCron = new CronExpression(tarefas.getCron());
			tarefas.setProximaHora(expressaoCron.nextTimeAfter(new DateTime()));
			
			logger.info("Atualizou o mapa de memória com os dados do banco!!");
			
		}
		
	}
	
	
	@Schedule(second = "*/295", hour = "*", minute="*/5", persistent = false)
	@Lock(LockType.READ)
	public void agendadorSecundario() {
		sincronizarTarefasMemoriaBanco();
//		taskEnviarXMLsCTM();
	}
	
	
	public String taskEnviarXMLsCTM() {

		String resultado = "";
		try {

			// Início

			String target_dir = "C:\\cristian\\inventários";
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
	@AccessTimeout(value = 20, unit = TimeUnit.MINUTES)
	private void generateReport(Timer timer) {

//	    logger.info("!!--timeout invoked here "+new Date());
//	    System.out.println("!!--timeout invoked here "+new Date());

	 

	}	
	
	
	
}
