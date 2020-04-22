package com.example.demo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioAuditoria {
	
	@PersistenceContext
	private EntityManager em;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void log (String event) {
		
		Auditoria auditoria = new Auditoria();
		auditoria.setEvent(event);
		
		em.persist(auditoria);
	}


	public void log (Persona persona) {
		
		Auditoria auditoria = new Auditoria();
		String mensaje = String.format("Consulta num %d a %s", persona.getConsultas(), persona.getNombre());
		auditoria.setEvent(mensaje);

		persona.setConsultas(persona.getConsultas()+1);

		em.persist(auditoria);
	}

	public List<Auditoria> todas() {
		return em.createQuery("select a from Auditoria a order by a.id", Auditoria.class).getResultList();
	}

}
