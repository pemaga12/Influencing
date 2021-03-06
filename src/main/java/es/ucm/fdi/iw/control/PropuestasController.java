package es.ucm.fdi.iw.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.Propuesta;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Controller()
@RequestMapping("busquedaPropuesta")
public class PropuestasController {
    
    private static final Logger log = LogManager.getLogger(PropuestasController.class);
    
    @Autowired 
    private EntityManager entityManager;
    
    @GetMapping("")
	public String getPropuestas(Model model, HttpSession session) {
		List<Propuesta> propuestas = entityManager.createNamedQuery("Propuesta.getAllProposals", Propuesta.class).getResultList();
                
	    model.addAttribute("propuestas", propuestas);
        
        return "busquedaPropuesta";
    }
  
    @GetMapping("/propuesta")
    public String propuesta(Model model, HttpSession session, @RequestParam long idPropuesta) {
        Propuesta p = entityManager.find(Propuesta.class, idPropuesta);
        model.addAttribute("propuesta", p);
        return "modals/propuesta";
    } // copiado

}
