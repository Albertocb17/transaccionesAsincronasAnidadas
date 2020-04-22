package com.example.demo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioAsincrono {
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private ServicioAuditoria auditoria;
	
	@Async
	@Transactional(rollbackFor = Exception.class)
	public void cambiarNombreAsincronoTransaccional(Persona persona) throws Exception {
//		persona = em.find(Persona.class, persona.getId());

		// Ejecuto algo que tarde mucho
		pausa();

		String nombre = persona.getNombre();
		persona.setNombre("Modificado");
		persona.setConsultas(1);
		
//		em.merge(persona);
		
		auditoria.log("Modificando nombre de persona " + nombre + " a " + persona.getNombre());
		
//		throw new Exception("ERROR DE PRUEBA");

	}
	
	private void pausa() {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
