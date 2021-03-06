package es.ucm.fdi.iw.control;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.model.Candidatura;
import es.ucm.fdi.iw.model.Evento;
import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.Usuario;

/**
 * Usuario-administration controller
 * 
 * @author mfreire
 */
@Controller()
@RequestMapping("message")
public class MessageController {

	private static final Logger log = LogManager.getLogger(MessageController.class);
	
	@Autowired 
	private EntityManager entityManager;
		
	@GetMapping("/")
	public String getMessages(Model model, HttpSession session) {
		return "messages";
	}

	@GetMapping(path = "/received", produces = "application/json")
	@Transactional // para no recibir resultados inconsistentes
	@ResponseBody // para indicar que no devuelve vista, sino un objeto (jsonizado)
	public List<Message.Transfer> retrieveMessages(HttpSession session) {
		long UsuarioId = ((Usuario)session.getAttribute("u")).getId();
		Usuario u = entityManager.find(Usuario.class, UsuarioId);
		log.info("Generating message list for Usuario {} ({} messages)", 
				u.getNombre(), u.getReceived().size());
		return Message.asTransferObjects(u.getReceived());
	}
	
	@GetMapping(path = "/getChat", produces = "application/json")
	@Transactional // para no recibir resultados inconsistentes
	@ResponseBody // para indicar que no devuelve vista, sino un objeto (jsonizado)
	
	public List<Evento.TransferChat> devuelveChat(HttpSession session, @RequestParam long idCandidatura) {
		
		long userId = ((Usuario)session.getAttribute("u")).getId();
		List<Evento> mensajes = entityManager.createNamedQuery("Evento.getChat").setParameter("idCandidatura", idCandidatura).getResultList();
		Usuario u = entityManager.find(Usuario.class, userId);
		log.info("Generating message list for user {} ({} messages)", 
				u.getNombre(), u.getReceived().size());
		return Evento.asTransferObjects(mensajes, u);
		
		
	}	

	@GetMapping(path = "/unread", produces = "application/json")
	@ResponseBody
	public String checkUnread(HttpSession session) {
		long UsuarioId = ((Usuario)session.getAttribute("u")).getId();		
		long unread = entityManager.createNamedQuery("Message.countUnread", Long.class)
			.setParameter("UsuarioId", UsuarioId)
			.getSingleResult();
		session.setAttribute("unread", unread);
		return "{\"unread\": " + unread + "}";
	}
}
